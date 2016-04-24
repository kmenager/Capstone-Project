package io.github.kmenager.getmesomefeed.ui.search;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import io.github.kmenager.getmesomefeed.data.DataManager;
import io.github.kmenager.getmesomefeed.data.local.model.Result;
import io.github.kmenager.getmesomefeed.injection.ActivityContext;
import io.github.kmenager.getmesomefeed.ui.base.BasePresenter;
import io.github.kmenager.getmesomefeed.util.NetworkUtil;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class SearchPresenter extends BasePresenter<SearchFeedView> {

    private final Context mContext;
    private final DataManager mDataManager;
    private CompositeSubscription mCompositeSubscription;

    @Inject
    public SearchPresenter(@ActivityContext Context context, DataManager dataManager) {
        mContext = context;
        mDataManager = dataManager;
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void attachView(SearchFeedView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    public void searchFeed(String search) {
        checkViewAttached();
        if (NetworkUtil.isNetworkConnected(mContext)) {

            Subscription subscription = mDataManager.searchFeed(search)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<List<Result>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(List<Result> results) {
                            getMvpView().showSearchResult(results);
                        }
                    });
            mCompositeSubscription.add(subscription);
        }
    }

    public void subscribeFeed(final Result result) {
        checkViewAttached();
        if (NetworkUtil.isNetworkConnected(mContext)) {
            Subscription subscription = mDataManager.subscribeFeed(result)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            Result currentResult = result;
                            currentResult.isSaved = aBoolean;
                            getMvpView().updateList(currentResult);
                        }
                    });

            mCompositeSubscription.add(subscription);
        }
    }

    public void unSubscribeFeed(final Result result) {
        checkViewAttached();
        if (NetworkUtil.isNetworkConnected(mContext)) {
            Subscription subscription = mDataManager.unSubscribeFeed(result.feedId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<Boolean>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            Result currentResult = result;
                            currentResult.isSaved = !aBoolean;
                            getMvpView().updateList(currentResult);
                        }
                    });

            mCompositeSubscription.add(subscription);
        }
    }

    public boolean isLogged() {
        checkViewAttached();
        return mDataManager.getPreferencesHelper().getIsConnected();
    }
}
