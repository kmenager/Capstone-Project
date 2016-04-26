package io.github.kmenager.getmesomefeed.data;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.kmenager.getmesomefeed.R;
import io.github.kmenager.getmesomefeed.data.local.PreferencesHelper;
import io.github.kmenager.getmesomefeed.data.local.database.DatabaseHelper;
import io.github.kmenager.getmesomefeed.data.local.model.FavoriteSubscription;
import io.github.kmenager.getmesomefeed.data.local.model.Item;
import io.github.kmenager.getmesomefeed.data.local.model.ItemView;
import io.github.kmenager.getmesomefeed.data.local.model.Profile;
import io.github.kmenager.getmesomefeed.data.local.model.QueryFeed;
import io.github.kmenager.getmesomefeed.data.local.model.Result;
import io.github.kmenager.getmesomefeed.data.local.model.Stream;
import io.github.kmenager.getmesomefeed.data.local.model.Subscription;
import io.github.kmenager.getmesomefeed.data.local.model.database.ItemDatabase;
import io.github.kmenager.getmesomefeed.data.remote.FeedService;
import io.github.kmenager.getmesomefeed.injection.ApplicationContext;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

@Singleton
public class DataManager {

    private final Context mContext;
    private final FeedService mFeedService;
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(@ApplicationContext Context context, FeedService feedService, DatabaseHelper databaseHelper, PreferencesHelper preferencesHelper) {
        mContext = context;
        mFeedService = feedService;
        mDatabaseHelper = databaseHelper;
        mPreferencesHelper = preferencesHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    /**
     * Get a list of item from a stream id
     *
     * @param streamId the stream id
     * @return a observable with a list of item
     */
    public Observable<Stream> getStream(String streamId) {
        return mFeedService.getStream(streamId);
    }

    /**
     * Save in database the current item
     *
     * @param item the item to save
     */
    public Observable<Void> saveItem(ItemView item) {
        if (item instanceof Item)
            return mDatabaseHelper.saveItem((Item) item);
        else if (item instanceof ItemDatabase)
            return mDatabaseHelper.saveItem((ItemDatabase) item);
        return Observable.empty();
    }

    /**
     * Delete from database the given saved item
     *
     * @param item the item to delete
     */
    public Observable<Void> deleteItem(ItemView item) {
        if (item instanceof Item)
            return mDatabaseHelper.deleteSavedItem((Item) item);
        else if (item instanceof ItemDatabase)
            return mDatabaseHelper.deleteSavedItem((ItemDatabase) item);
        return Observable.empty();
    }

    /**
     * Search for feed
     *
     * @param term the term to search
     * @return a list of Result item
     */
    public Observable<List<Result>> searchFeed(String term) {
        return mFeedService.searchFeed(term, FeedService.Util.getConstantForQuery())
                .concatMap(new Func1<QueryFeed, Observable<? extends List<Result>>>() {
                    @Override
                    public Observable<? extends List<Result>> call(QueryFeed queryFeed) {
                        List<Result> results = new ArrayList<>();
                        if (mPreferencesHelper.getIsConnected()) {
                            for (Result result : queryFeed.results) {
                                results.add(mDatabaseHelper.isInDatabase(result));
                            }
                        } else {
                            results.addAll(queryFeed.results);
                        }
                        return Observable.just(results);
                    }
                });
    }

    /**
     * @return a list of subscriptions
     */
    public Observable<List<Subscription>> getSubscriptions() {
        String auth = FeedService.Util.buildAuthorization();
        return mFeedService.getSubscriptions(auth)
                .concatMap(new Func1<List<Subscription>, Observable<? extends List<Subscription>>>() {
                    @Override
                    public Observable<? extends List<Subscription>> call(List<Subscription> subscriptions) {
                        return mDatabaseHelper.saveSubscription(subscriptions);
                    }
                });
    }

    /**
     * @param result the feed to subscribe
     * @return true if request success, false otherwise
     */
    public Observable<Boolean> subscribeFeed(final Result result) {
        String auth = FeedService.Util.buildAuthorization();
        FavoriteSubscription favoriteSubscription = new FavoriteSubscription();
        favoriteSubscription.id = result.feedId;
        favoriteSubscription.title = result.title;
        return mFeedService.subscribeFeed(auth, favoriteSubscription)
                .concatMap(new Func1<Response<ResponseBody>, Observable<? extends Boolean>>() {
                    @Override
                    public Observable<? extends Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        return Observable.just(responseBodyResponse.isSuccessful());
                    }
                });
    }

    /**
     * @param feedId the feed id to un subscribe
     * @return true if successfully un subscribe to the feed, false otherwise
     */
    public Observable<Boolean> unSubscribeFeed(final String feedId) {
        String auth = FeedService.Util.buildAuthorization();
        List<String> feedIds = new ArrayList<>();
        feedIds.add(feedId);
        return mFeedService.unSubscribeFeed(auth, feedIds)
                .concatMap(new Func1<Response<ResponseBody>, Observable<? extends Boolean>>() {
                    @Override
                    public Observable<? extends Boolean> call(Response<ResponseBody> responseBodyResponse) {
                        if (responseBodyResponse.isSuccessful())
                            return mDatabaseHelper.deleteSubscription(feedId);
                        else
                            return Observable.just(responseBodyResponse.isSuccessful());
                    }
                });
    }

    /**
     * @return a list of items from database
     */
    public Observable<List<ItemView>> getLocalItems() {
        return mDatabaseHelper.getLocalItems();
    }

    /**
     * @param item
     * @return true if item is database, false otherwise
     */
    public Observable<Boolean> isItemBookmarked(final Item item) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (subscriber.isUnsubscribed()) return;
                subscriber.onNext(mDatabaseHelper.isInDatabase(item));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<Stream> getUserStream() {
        final String auth = FeedService.Util.buildAuthorization();
        return mFeedService.getProfile(auth)
                .concatMap(new Func1<Profile, Observable<? extends Stream>>() {
                    @Override
                    public Observable<? extends Stream> call(Profile profile) {
                        return mFeedService.getStream(auth, mContext.getString(R.string.user_stream_id, profile.id));
                    }
                });
    }

    public List<Item> fetchSynchronousFeed() throws IOException {
        String auth = FeedService.Util.buildAuthorization();
        Call<Profile> callProfile = mFeedService.getProfileWidget(auth);
        Profile profile = callProfile.execute().body();
        List<Item> items = new ArrayList<>();
        if (profile != null) {

            Call<Stream> callStream = mFeedService.getStreamWidget(auth, mContext.getString(R.string.user_stream_id, profile.id));
            Stream stream = callStream.execute().body();
            if (stream != null) {
                items.addAll(stream.items);
            }
        }
        return items;
    }
}
