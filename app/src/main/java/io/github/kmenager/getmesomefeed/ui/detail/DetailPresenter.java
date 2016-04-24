package io.github.kmenager.getmesomefeed.ui.detail;


import javax.inject.Inject;

import io.github.kmenager.getmesomefeed.data.DataManager;
import io.github.kmenager.getmesomefeed.data.local.model.Item;
import io.github.kmenager.getmesomefeed.data.local.model.ItemView;
import io.github.kmenager.getmesomefeed.ui.base.BasePresenter;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetailPresenter extends BasePresenter<DetailView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public DetailPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(DetailView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null && !mSubscription.isUnsubscribed())
            mSubscription.unsubscribe();
    }

    public void bookmarkItem(ItemView item) {
        checkViewAttached();
        mSubscription = mDataManager.saveItem(item)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().showToastItemBookmarked();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });
    }

    public void unBookmarkItem(ItemView item) {
        checkViewAttached();
        mSubscription = mDataManager.deleteItem(item)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        getMvpView().showBookmarked(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Void aVoid) {

                    }
                });
    }

    public void isInDatabase(Item item) {
        checkViewAttached();
        mSubscription = mDataManager.isItemBookmarked(item)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        getMvpView().showBookmarked(aBoolean);
                    }
                });
    }

    public boolean isLoggedUser() {
        checkViewAttached();
        return mDataManager.getPreferencesHelper().getIsConnected();
    }
}
