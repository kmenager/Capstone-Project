package io.github.kmenager.getmesomefeed.ui.menu;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.kmenager.getmesomefeed.R;
import io.github.kmenager.getmesomefeed.data.model.MenuHeader;
import io.github.kmenager.getmesomefeed.data.model.MenuItemView;
import io.github.kmenager.getmesomefeed.data.model.MenuRowItem;
import io.github.kmenager.getmesomefeed.ui.base.BaseFragment;
import io.github.kmenager.getmesomefeed.ui.main.MainActivity;
import io.github.kmenager.getmesomefeed.ui.main.MainView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends BaseFragment implements MenuView,
        MenuAdapter.MenuAdapterCallback,
        View.OnClickListener {


    public static final String TAG = MenuFragment.class.getSimpleName();
    @Inject
    MenuPresenter mMenuPresenter;

    @Inject
    MenuAdapter mMenuAdapter;

    @Bind(R.id.recycler_view_menu)
    RecyclerView mRecyclerView;

    @Bind(R.id.bottom_menu)
    LinearLayout mBlocBottom;

    @Bind(android.R.id.empty)
    ProgressBar loading;

    @Bind(R.id.sign_in_button)
    SignInButton mSignInButton;

    @Bind(R.id.menu_logout)
    LinearLayout mSignOut;

    @Bind(R.id.menu_my_saved_feed)
    LinearLayout mMyFeed;

    public MenuFragment() {
        // Required empty public constructor
    }


    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.bind(this, view);
        mMenuPresenter.attachView(this);


        List<MenuItemView> menuItemInterfaces = new ArrayList<>();


        mMenuAdapter.setMenuAdapterCallback(this);
        mMenuAdapter.setMenuItems(menuItemInterfaces);

        mRecyclerView.setAdapter(mMenuAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        mSignInButton.setSize(SignInButton.SIZE_STANDARD);
        mSignInButton.setScopes(((MainActivity)getActivity()).getGso().getScopeArray());
        mSignInButton.setOnClickListener(this);

        mSignOut.setOnClickListener(this);
        mMyFeed.setOnClickListener(this);

        if (mMenuPresenter.isLoggedUser())
            mMenuPresenter.loadMainMenu();
        return view;
    }

    public void refreshMenu() {
        if (mMenuPresenter.isLoggedUser()) {
            mMenuAdapter.removeMenuItems();
            mMenuPresenter.loadMainMenu();
        }
    }

    private void signIn() {
        ((MainActivity) getActivity()).setSignIn();
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(((MainActivity)getActivity()).getGoogleApiClient()).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        // [START_EXCLUDE]
                        setSignOut();
                        // [END_EXCLUDE]
                    }
                });
    }



    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        mMenuPresenter.detachView();
        super.onDestroyView();
    }

    @Override
    public void onItemClicked(MenuRowItem menuRowItem) {
        ((MainView) getActivity()).loadFeed(menuRowItem);
    }

    @Override
    public void showListSubscription(List<MenuItemView> menuItemViews) {
        mMenuAdapter.setMenuItems(menuItemViews);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.menu_logout:
                signOut();
                break;
            case R.id.menu_my_saved_feed:
                ((MainActivity) getActivity()).loadSavedFeed();
                break;
        }
    }

    public void setSignIn(MenuHeader menuHeader) {
        mSignInButton.setVisibility(View.GONE);
        mBlocBottom.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);

        mMenuAdapter.setHeader(menuHeader);
    }

    public void setSignOut() {
        mSignInButton.setVisibility(View.VISIBLE);
        mBlocBottom.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);

        mMenuAdapter.removeHeader();
    }
}
