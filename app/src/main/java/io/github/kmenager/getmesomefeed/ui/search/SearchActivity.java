package io.github.kmenager.getmesomefeed.ui.search;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.github.kmenager.getmesomefeed.R;
import io.github.kmenager.getmesomefeed.data.local.model.Result;
import io.github.kmenager.getmesomefeed.ui.base.BaseActivity;

public class SearchActivity extends BaseActivity implements SearchFeedView,
        SearchView.OnQueryTextListener,
        SearchAdapter.CallbackSearchAdapter {


    @Inject SearchPresenter mPresenter;
    @Inject SearchAdapter mSearchAdapter;


    @Bind(R.id.search_view)
    android.support.v7.widget.SearchView mSearchView;

    @Bind(R.id.recycler_view_search)
    RecyclerView mRecyclerViewResult;

    @Bind(android.R.id.empty)
    ProgressBar mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        mPresenter.attachView(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mSearchAdapter.setCallbackSearchAdapter(this);
        mRecyclerViewResult.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerViewResult.setAdapter(mSearchAdapter);



        mSearchView.setOnQueryTextListener(this);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, SearchActivity.class)));
        mSearchView.setIconified(false);
        mSearchView.setQueryHint("Find a feed");
        mSearchView.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_ACTION_SEARCH);
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                dismiss();
                return false;
            }
        });
        mSearchView.setQuery("", false);
    }

    public void dismiss() {
        setResult(Activity.RESULT_OK);
        ActivityCompat.finishAfterTransition(this);
        //overridePendingTransition(0, R.anim.slide_top_to_bottom);
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        ActivityCompat.finishAfterTransition(this);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mSearchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText.length() == 0) {
            mSearchAdapter.setResults(null);
        } else {
            mSearchAdapter.setResults(null);
            mLoading.setVisibility(View.VISIBLE);
            mPresenter.searchFeed(newText);
        }
        return true;
    }

    @Override
    public void onClickSearchFeed(Result result) {
        Intent intent = new Intent();
        Bundle args = new Bundle();
        args.putParcelable("result", result);
        intent.putExtras(args);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onSubscribeFeed(Result result) {
        if (mPresenter.isLogged())
            mPresenter.subscribeFeed(result);
        else
            Toast.makeText(this, R.string.log_save_item, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUnSubscribeFeed(Result result) {
        if (mPresenter.isLogged())
            mPresenter.unSubscribeFeed(result);
        else
            Toast.makeText(this, R.string.log_save_item, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSearchResult(List<Result> results) {
        mLoading.setVisibility(View.GONE);
        mSearchAdapter.setResults(results);
    }

    @Override
    public void updateList(Result result) {
        mSearchAdapter.updateResult(result);
    }
}
