package io.github.kmenager.getmesomefeed.ui.main;

import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import io.github.kmenager.getmesomefeed.data.DataManager;
import io.github.kmenager.getmesomefeed.data.local.model.ItemView;
import io.github.kmenager.getmesomefeed.data.local.model.Stream;
import io.github.kmenager.getmesomefeed.injection.ActivityContext;
import io.github.kmenager.getmesomefeed.ui.base.BasePresenter;
import io.github.kmenager.getmesomefeed.util.NetworkUtil;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kevinmenager on 08/04/16.
 */
public class MainPresenter extends BasePresenter<MainView> {

    private final Context mContext;
    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public MainPresenter(@ActivityContext Context context, DataManager dataManager) {
        mContext = context;
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MainView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null && !mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }

    public void fetchStream(String streamId) {
        checkViewAttached();
        if (NetworkUtil.isNetworkConnected(mContext)) {
            mSubscription = mDataManager.getStream(streamId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<Stream>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Stream stream) {
                            getMvpView().showStream(stream);
                        }
                    });
        }
    }

    public void fetchSavedStream() {
        checkViewAttached();
        mSubscription = mDataManager.getLocalItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<ItemView>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<ItemView> itemViews) {
                        getMvpView().showLocalItems(itemViews);
                    }
                });
    }

    public void setConnectionState(boolean isConnected) {
        checkViewAttached();
        mDataManager.getPreferencesHelper().putIsConnected(isConnected);
    }

    public boolean isLoggedUser() {
        checkViewAttached();
        return mDataManager.getPreferencesHelper().getIsConnected();
    }
}
