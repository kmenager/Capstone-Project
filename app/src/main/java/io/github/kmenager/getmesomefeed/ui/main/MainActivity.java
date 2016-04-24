package io.github.kmenager.getmesomefeed.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.kmenager.getmesomefeed.FeedApplication;
import io.github.kmenager.getmesomefeed.R;
import io.github.kmenager.getmesomefeed.data.local.model.ItemView;
import io.github.kmenager.getmesomefeed.data.local.model.Result;
import io.github.kmenager.getmesomefeed.data.local.model.Stream;
import io.github.kmenager.getmesomefeed.data.model.MenuHeader;
import io.github.kmenager.getmesomefeed.data.model.MenuRowItem;
import io.github.kmenager.getmesomefeed.ui.base.BaseActivity;
import io.github.kmenager.getmesomefeed.ui.detail.DetailActivity;
import io.github.kmenager.getmesomefeed.ui.menu.MenuFragment;
import io.github.kmenager.getmesomefeed.ui.search.SearchActivity;
import io.github.kmenager.getmesomefeed.util.Constants;


public class MainActivity extends BaseActivity implements MainView, MainAdapter.OnItemClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_CODE_ITEM = 9001;
    public static final int RC_SIGN_IN = 9002;

    @Inject
    MainPresenter mPresenter;
    @Inject
    MainAdapter mMainAdapter;

    //----- View -----//
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.text_view_get_started)
    TextView mTvGetStarted;

    @Bind(android.R.id.empty)
    ProgressBar mLoading;

    private String mPlaceHolderIconUrl;
    private ArrayList<ItemView> mItemViewList;

    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions mGso;
    private Tracker mTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FeedApplication application = (FeedApplication) getApplication();
        mTracker = application.getDefaultTracker();

        setSupportActionBar(mToolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mGso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGso)
                .build();

        mMainAdapter.addOnItemClickListener(this);
        mRecyclerView.setAdapter(mMainAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPresenter.attachView(this);
        if (savedInstanceState == null) {
            mTvGetStarted.setVisibility(View.VISIBLE);
            if (mPresenter.isLoggedUser()) {
                mTvGetStarted.setText(getString(R.string.connected_start_search));
            }
            //mPresenter.fetchStream("feed/http://feeds.feedburner.com/KorbensBlog-UpgradeYourMind");
            refreshMenu();
        } else {
            mItemViewList = savedInstanceState.getParcelableArrayList(Constants.ARG_EXTRA_ITEMS);
            mMainAdapter.setItems(mItemViewList, mPlaceHolderIconUrl);
            mPlaceHolderIconUrl = savedInstanceState.getString(Constants.ARG_EXTRA_PLACEHOLDER);
            getSupportActionBar().setTitle(savedInstanceState.getString(Constants.ARG_EXTRA_TITLE));
        }
//        mPresenter.fetchStream("feed/http://feeds.feedburner.com/fubiz");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(Constants.ARG_EXTRA_ITEMS, mItemViewList);
        outState.putString(Constants.ARG_EXTRA_TITLE, getSupportActionBar().getTitle().toString());
        outState.putString(Constants.ARG_EXTRA_PLACEHOLDER, mPlaceHolderIconUrl);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + MainActivity.TAG);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        MenuFragment menuFragment = (MenuFragment) getSupportFragmentManager().findFragmentByTag(MenuFragment.TAG);
        mPresenter.setConnectionState(result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            MenuHeader menuHeader = new MenuHeader();
            menuHeader.email = acct.getEmail();
            menuHeader.displayName = acct.getDisplayName();
            menuHeader.urlProfile = acct.getPhotoUrl();
            if (menuFragment != null) {
                menuFragment.setSignIn(menuHeader);
            }
        } else {

            // Signed out, show unauthenticated UI.
            if (menuFragment != null) {
                menuFragment.setSignOut();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivityForResult(intent, REQUEST_CODE_ITEM);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ITEM:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Result result = data.getParcelableExtra(Constants.ARG_EXTRA_RESULT);
                        mPlaceHolderIconUrl = result.visualUrl;
                        mPresenter.fetchStream(result.feedId);
                    }
                    MenuFragment menuFragment = (MenuFragment) getSupportFragmentManager().findFragmentByTag(MenuFragment.TAG);
                    if (menuFragment != null) {
                        menuFragment.refreshMenu();
                    }
                }
                break;
            case RC_SIGN_IN:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void refreshMenu() {
        MenuFragment menuFragment = (MenuFragment) getSupportFragmentManager().findFragmentByTag(MenuFragment.TAG);
        if (menuFragment == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.contentPanel, MenuFragment.newInstance(), MenuFragment.TAG).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showStream(Stream stream) {
        mLoading.setVisibility(View.GONE);
        mTvGetStarted.setVisibility(View.GONE);
        getSupportActionBar().setTitle(stream.title);
        List<ItemView> list = new ArrayList<>();
        list.addAll(stream.items);
        mItemViewList = (ArrayList<ItemView>) list;
        mMainAdapter.setItems(list, mPlaceHolderIconUrl);
    }

    @Override
    public void loadFeed(MenuRowItem menuRowItem) {
        mLoading.setVisibility(View.VISIBLE);
        mTvGetStarted.setVisibility(View.GONE);
        if (mMainAdapter.getItems() != null) {
            mMainAdapter.getItems().clear();
            mMainAdapter.notifyDataSetChanged();
        }
        mPlaceHolderIconUrl = menuRowItem.coverUrl;
        mPresenter.fetchStream(menuRowItem.id);
        closeDrawer();
    }

    public void closeDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void showLocalItems(List<ItemView> items) {
        mTvGetStarted.setVisibility(View.GONE);
        mLoading.setVisibility(View.GONE);
        getSupportActionBar().setTitle(R.string.saved_feed_title);
        mItemViewList = (ArrayList<ItemView>) items;
        mMainAdapter.setItems(items, mPlaceHolderIconUrl);
    }

    @Override
    public void onItemClick(ItemView item) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Constants.ARG_EXTRA_ITEMS, item);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public GoogleSignInOptions getGso() {
        return mGso;
    }

    public void setSignIn() {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("Sign in")
                .build());
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, MainActivity.RC_SIGN_IN);
    }

    public void loadSavedFeed() {
        mPresenter.fetchSavedStream();
        closeDrawer();
    }
}
