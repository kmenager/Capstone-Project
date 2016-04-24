package io.github.kmenager.getmesomefeed.data.local.database;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.kmenager.getmesomefeed.data.local.database.helper.ItemHelper;
import io.github.kmenager.getmesomefeed.data.local.database.helper.SubscriptionHelper;
import io.github.kmenager.getmesomefeed.data.local.model.Alternate;
import io.github.kmenager.getmesomefeed.data.local.model.Item;
import io.github.kmenager.getmesomefeed.data.local.model.ItemView;
import io.github.kmenager.getmesomefeed.data.local.model.Result;
import io.github.kmenager.getmesomefeed.data.local.model.Subscription;
import io.github.kmenager.getmesomefeed.data.local.model.Visual;
import io.github.kmenager.getmesomefeed.data.local.model.database.ItemDatabase;
import io.github.kmenager.getmesomefeed.data.local.model.database.SubscriptionDatabase;
import io.github.kmenager.getmesomefeed.injection.ApplicationContext;
import io.github.kmenager.getmesomefeed.util.ParseHTML;
import rx.Observable;
import rx.Subscriber;

@Singleton
public class DatabaseHelper {

    private final Context mContext;

    @Inject
    public DatabaseHelper(@ApplicationContext Context context) {
        mContext = context;
    }

    public Observable<Void> saveItem(final Item item) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                if (subscriber.isUnsubscribed()) return;

                try {
                    Cursor cursor = mContext.getContentResolver()
                            .query(FeedProvider.Items.CONTENT_URI, null,
                                    ItemColumns.REMOTE_ID + "=?",
                                    new String[]{String.valueOf(item.id)}, null);
                    if (cursor != null) {
                        ItemDatabase itemDatabase;
                        if (cursor.moveToFirst()) {
                            itemDatabase = ItemHelper.parseCursor(cursor);
                            updateItemDatabase(itemDatabase, item);
                            mContext.getContentResolver()
                                    .update(FeedProvider.Items.CONTENT_URI,
                                            ItemHelper.toContentValues(itemDatabase),
                                            ItemColumns.ID + "=?",
                                            new String[]{String.valueOf(itemDatabase.id)});
                        } else {
                            itemDatabase = new ItemDatabase();
                            updateItemDatabase(itemDatabase, item);
                            mContext.getContentResolver()
                                    .insert(FeedProvider.Items.CONTENT_URI,
                                            ItemHelper.toContentValues(itemDatabase));
                        }
                        cursor.close();
                    }

                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(new Throwable(e));
                }
            }
        });
    }

    public Observable<Void> saveItem(final ItemDatabase item) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                if (subscriber.isUnsubscribed()) return;

                try {
                    Cursor cursor = mContext.getContentResolver()
                            .query(FeedProvider.Items.CONTENT_URI, null,
                                    ItemColumns.ID + "=?",
                                    new String[]{String.valueOf(item.id)}, null);
                    if (cursor != null) {
                        ItemDatabase itemDatabase;
                        if (cursor.moveToFirst()) {
                            itemDatabase = ItemHelper.parseCursor(cursor);
                            updateItemDatabase(itemDatabase, item);
                            mContext.getContentResolver()
                                    .update(FeedProvider.Items.CONTENT_URI,
                                            ItemHelper.toContentValues(itemDatabase),
                                            ItemColumns.ID + "=?",
                                            new String[]{String.valueOf(itemDatabase.id)});
                        } else {
                            itemDatabase = new ItemDatabase();
                            updateItemDatabase(itemDatabase, item);
                            mContext.getContentResolver()
                                    .insert(FeedProvider.Items.CONTENT_URI,
                                            ItemHelper.toContentValues(itemDatabase));
                        }
                        cursor.close();
                    }

                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(new Throwable(e));
                }
            }
        });
    }

    private void updateItemDatabase(ItemDatabase itemDatabase, Item item) {
        itemDatabase.title = item.title;
        itemDatabase.date = new Date(item.date);
        itemDatabase.remoteId = item.id;
        itemDatabase.contentFull = item.content.content;
        itemDatabase.contentOffline = ParseHTML.parseToSimpleText(item.content.content);
        Visual visual = item.visual;
        if (visual != null) {
            itemDatabase.urlImage = visual.urlImage;
        }
        List<Alternate> alternates = item.alternate;
        if (alternates != null && alternates.size() > 0) {
            itemDatabase.urlArticle = alternates.get(0).urlArticle;
        }
    }

    private void updateItemDatabase(ItemDatabase itemDatabase, ItemDatabase newItem) {
        itemDatabase.title = newItem.title;
        itemDatabase.date = newItem.date;
        itemDatabase.remoteId = newItem.remoteId;
        itemDatabase.contentFull = newItem.contentFull;
        itemDatabase.contentOffline = newItem.contentOffline;

        itemDatabase.urlImage = newItem.urlImage;
        itemDatabase.urlArticle = newItem.urlArticle;
    }

    public Observable<Void> deleteSavedItem(final Item item) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Cursor cursor = mContext.getContentResolver()
                        .query(FeedProvider.Items.CONTENT_URI, null,
                                ItemColumns.REMOTE_ID + "=?",
                                new String[]{String.valueOf(item.id)}, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        mContext.getContentResolver()
                                .delete(FeedProvider.Items.CONTENT_URI,
                                        ItemColumns.REMOTE_ID + "=?",
                                        new String[]{String.valueOf(item.id)});
                    }
                    cursor.close();
                }
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Void> deleteSavedItem(final ItemDatabase item) {
        return Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                Cursor cursor = mContext.getContentResolver()
                        .query(FeedProvider.Items.CONTENT_URI, null,
                                ItemColumns.ID + "=?",
                                new String[]{String.valueOf(item.id)}, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        mContext.getContentResolver()
                                .delete(FeedProvider.Items.CONTENT_URI,
                                        ItemColumns.ID + "=?",
                                        new String[]{String.valueOf(item.id)});
                    }
                    cursor.close();
                }
                subscriber.onCompleted();
            }
        });
    }


    /**
     * Save subscription from subscription in database
     *
     * @return
     */
    public Observable<List<Subscription>> saveSubscription(final List<Subscription> subscriptions) {
        return Observable.create(new Observable.OnSubscribe<List<Subscription>>() {
            @Override
            public void call(Subscriber<? super List<Subscription>> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                try {
                    for (Subscription subscription : subscriptions) {
                        Cursor cursor = mContext.getContentResolver()
                                .query(FeedProvider.Subscriptions.CONTENT_URI, null,
                                        SubscriptionColumns.FEED_ID + "=?",
                                        new String[]{String.valueOf(subscription.id)}, null);
                        if (cursor != null) {
                            SubscriptionDatabase subscriptionDatabase;
                            if (cursor.moveToFirst()) {
                                subscriptionDatabase = SubscriptionHelper.parseCursor(cursor);
                                setSubscription(subscriptionDatabase, subscription);
                                mContext.getContentResolver()
                                        .update(FeedProvider.Subscriptions.CONTENT_URI,
                                                SubscriptionHelper.toContentValues(subscriptionDatabase),
                                                SubscriptionColumns.ID + "=?",
                                                new String[]{String.valueOf(subscriptionDatabase.id)});
                            } else {
                                subscriptionDatabase = new SubscriptionDatabase();
                                setSubscription(subscriptionDatabase, subscription);
                                mContext.getContentResolver()
                                        .insert(FeedProvider.Subscriptions.CONTENT_URI,
                                                SubscriptionHelper.toContentValues(subscriptionDatabase));
                            }
                            cursor.close();

                        }
                    }
                    subscriber.onNext(subscriptions);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(new Throwable(e));
                }
            }
        });
    }

    private void setSubscription(SubscriptionDatabase subscriptionDatabase, Subscription subscription) {
        subscriptionDatabase.visualUrl = subscription.visualUrl;
        subscriptionDatabase.subscribers = 0;
        subscriptionDatabase.iconUrl = subscription.iconUrl;
        subscriptionDatabase.title = subscription.title;
        subscriptionDatabase.feedId = subscription.id;
    }

    public Observable<Boolean> deleteSubscription(final String feedId) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                try {
                    Cursor cursor = mContext.getContentResolver()
                            .query(FeedProvider.Subscriptions.CONTENT_URI, null,
                                    SubscriptionColumns.FEED_ID + "=?",
                                    new String[]{String.valueOf(feedId)}, null);
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            mContext.getContentResolver()
                                    .delete(FeedProvider.Subscriptions.CONTENT_URI,
                                            SubscriptionColumns.FEED_ID + "=?",
                                            new String[]{String.valueOf(feedId)});
                        }
                        cursor.close();
                    }
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(new Throwable(e));
                }

            }
        });
    }

    public Result isInDatabase(Result result) {
        Cursor cursor = mContext.getContentResolver()
                .query(FeedProvider.Subscriptions.CONTENT_URI, null,
                        SubscriptionColumns.FEED_ID + "=?",
                        new String[]{String.valueOf(result.feedId)}, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result.isSaved = true;
            }
            cursor.close();
        }
        return result;
    }

    public boolean isInDatabase(Item item) {
        Cursor cursor = mContext.getContentResolver()
                .query(FeedProvider.Items.CONTENT_URI, null,
                        ItemColumns.REMOTE_ID + "=?",
                        new String[]{String.valueOf(item.id)}, null);
        boolean isBookmarked = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                isBookmarked = true;
            }
            cursor.close();
        }
        return isBookmarked;
    }

    public Observable<List<ItemView>> getLocalItems() {
        return Observable.create(new Observable.OnSubscribe<List<ItemView>>() {
            @Override
            public void call(Subscriber<? super List<ItemView>> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                try {
                    Cursor cursor = mContext.getContentResolver()
                            .query(FeedProvider.Items.CONTENT_URI, null,
                                    null, null, null);
                    List<ItemView> items = new ArrayList<>();
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            ItemDatabase itemDatabase = ItemHelper.parseCursor(cursor);
                            items.add(itemDatabase);
                        }
                        cursor.close();
                    }
                    subscriber.onNext(items);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(new Throwable(e));
                }
            }
        });
    }
}
