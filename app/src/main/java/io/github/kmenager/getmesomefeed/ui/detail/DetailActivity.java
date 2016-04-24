package io.github.kmenager.getmesomefeed.ui.detail;

import android.content.ComponentName;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.kmenager.getmesomefeed.R;
import io.github.kmenager.getmesomefeed.data.local.model.Item;
import io.github.kmenager.getmesomefeed.data.local.model.ItemView;
import io.github.kmenager.getmesomefeed.data.local.model.database.ItemDatabase;
import io.github.kmenager.getmesomefeed.ui.base.BaseActivity;
import io.github.kmenager.getmesomefeed.util.Constants;
import io.github.kmenager.getmesomefeed.util.NetworkUtil;

public class DetailActivity extends BaseActivity implements DetailView {

    @Inject
    DetailPresenter mPresenter;

    //----- View ----//

    @Bind(R.id.webview)
    WebView mWebView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.button_open_website)
    Button mOpenWebsite;


    private ItemView mItem;
    private boolean mIsBookmarked;
    private CustomTabsServiceConnection mCustomTabsServiceConnection;
    private CustomTabsClient mClient;
    private CustomTabsSession mCustomTabsSession;
    public static final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        mPresenter.attachView(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            WebView.setWebContentsDebuggingEnabled(true);

        mItem = getIntent().getParcelableExtra(Constants.ARG_EXTRA_ITEMS);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (mPresenter.isLoggedUser()) {
            if (mItem instanceof Item) {
                Item item = (Item) mItem;
                mPresenter.isInDatabase(item);
            } else {
                mIsBookmarked = true;
            }
        } else {
            mIsBookmarked = false;
        }


        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("http")) {
                    launchUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mOpenWebsite.setVisibility(View.VISIBLE);
            }
        });
        String title = "<h1>" + mItem.getTitle() + "</h1>";
        String head = "<head><style>" +
                "body {background-color: white; margin: 16px; line-height: 170%; color: #393939;} " +
                "h1 {color: #2C2C2C; line-height: normal;} " +
                "a {color: #393939;} " +
                "img {float: left; margin: 8px 16px 8px 0; max-width: 100%; height: auto; border-radius: 2px;}" +
                "p iframe {max-width: 100%; height: auto; border: 0;}" +
                ".video-container {\n" +
                "    position: relative;\n" +
                "    padding-bottom: 56.25%;\n" +
                "    padding-top: 30px; height: 0; overflow: hidden;\n" +
                "}" +
                ".video-container iframe,\n" +
                ".video-container object,\n" +
                ".video-container embed {\n" +
                "    position: absolute;\n" +
                "    top: 0;\n" +
                "    left: 0;\n" +
                "    width: 100%;\n" +
                "    height: 100%;\n" +
                "    border: 0;\n" +
                "    border-radius: 2px;\n" +
                "}" +
                " </style></head>";

        String html = "<html>" + head + "<body>" + title;

        if (mItem.getContent() != null) {
            if (mItem instanceof ItemDatabase && NetworkUtil.isNetworkConnected(this)) {
                ItemDatabase item = (ItemDatabase) mItem;
                html += item.contentFull;
            } else {
                html += mItem.getContent();
            }
        } else if (mItem.getSummary() != null) {
            html += mItem.getSummary();
        }
        html += "</body></html>";
        StringBuilder builder = new StringBuilder(html);
        int position = builder.indexOf("<iframe");
        if (position != -1) {
            builder.insert(position, "<div class=\"video-container\">");
            position = builder.indexOf("</iframe>");
            builder.insert(position + 9, "</div>");
        }
        mWebView.loadDataWithBaseURL(null, builder.toString(), "text/html", "UTF-8", null);
        mWebView.getSettings().setJavaScriptEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem bookmarkItem = menu.findItem(R.id.action_bookmark);
        // set your desired icon here based on a flag if you like
        if (mIsBookmarked)
            bookmarkItem.setIcon(getResources().getDrawable(R.drawable.ic_bookmark_white_24dp));
        else
            bookmarkItem.setIcon(getResources().getDrawable(R.drawable.ic_bookmark_border_white_24dp));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_bookmark:
                if (mPresenter.isLoggedUser()) {
                    if (mIsBookmarked)
                        mPresenter.unBookmarkItem(mItem);
                    else
                        mPresenter.bookmarkItem(mItem);
                } else {
                    Toast.makeText(this, R.string.log_save_item, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showToastItemBookmarked() {
        showBookmarked(true);
        Toast.makeText(this, R.string.bookmarked, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showBookmarked(boolean isBookmarked) {
        mIsBookmarked = isBookmarked;
        invalidateOptionsMenu();
    }

    @OnClick(R.id.button_open_website)
    public void openWebsite() {
        launchUrl(mItem.getUrlArticle());
    }

    public void launchUrl(String url) {
        mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {

                //Pre-warming
                mClient = customTabsClient;
                mClient.warmup(0L);
                //Initialize a session as soon as possible.
                mCustomTabsSession = mClient.newSession(null);

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mClient = null;
            }
        };

        CustomTabsClient.bindCustomTabsService(this, CUSTOM_TAB_PACKAGE_NAME, mCustomTabsServiceConnection);

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder(mCustomTabsSession)
                .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setShowTitle(true)
                .build();

        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
}
