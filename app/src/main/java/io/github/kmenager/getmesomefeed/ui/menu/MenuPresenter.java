package io.github.kmenager.getmesomefeed.ui.menu;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.github.kmenager.getmesomefeed.data.DataManager;
import io.github.kmenager.getmesomefeed.data.model.MenuItemView;
import io.github.kmenager.getmesomefeed.data.model.MenuRowItem;
import io.github.kmenager.getmesomefeed.injection.ActivityContext;
import io.github.kmenager.getmesomefeed.ui.base.BasePresenter;
import io.github.kmenager.getmesomefeed.util.NetworkUtil;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kevinmenager on 13/04/16.
 */
public class MenuPresenter extends BasePresenter<MenuView> {

    private final Context mContext;
    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public MenuPresenter(@ActivityContext Context context, DataManager dataManager) {
        mContext = context;
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MenuView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    public void loadMainMenu() {
        checkViewAttached();
        if (NetworkUtil.isNetworkConnected(mContext)) {
            mSubscription = mDataManager.getSubscriptions()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<List<io.github.kmenager.getmesomefeed.data.local.model.Subscription>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(List<io.github.kmenager.getmesomefeed.data.local.model.Subscription> subscriptions) {
                            if (subscriptions != null && subscriptions.size() > 0) {
                                List<MenuItemView> list = new ArrayList<>();
                                for (io.github.kmenager.getmesomefeed.data.local.model.Subscription subscription : subscriptions) {
                                    MenuRowItem menuRowItem = new MenuRowItem();
                                    menuRowItem.id = subscription.id;
                                    menuRowItem.title = subscription.title;
                                    menuRowItem.coverUrl = subscription.visualUrl;
                                    list.add(menuRowItem);
                                }
                                getMvpView().showListSubscription(list);
                            }
                        }
                    });
        }
    }

    public boolean isLoggedUser() {
        checkViewAttached();
        return mDataManager.getPreferencesHelper().getIsConnected();
    }
}
