package io.github.kmenager.getmesomefeed.widget;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.github.kmenager.getmesomefeed.FeedApplication;
import io.github.kmenager.getmesomefeed.R;
import io.github.kmenager.getmesomefeed.data.DataManager;
import io.github.kmenager.getmesomefeed.data.local.model.Item;
import io.github.kmenager.getmesomefeed.util.Constants;


public class FeedWidgetRemoteViewsService extends RemoteViewsService {
    @Inject
    DataManager mDataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        FeedApplication.get(FeedWidgetRemoteViewsService.this).getComponent().inject(this);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(final Intent intent) {
        return new RemoteViewsFactory() {
            private List<Item> mItems;

            @Override
            public void onCreate() {

                Log.d("KM", "onCreate");
            }

            @Override
            public void onDataSetChanged() {
                Log.d("KM", "onDataSetChanged");
                try {
                    if (mDataManager.getPreferencesHelper().getIsConnected())
                        mItems = mDataManager.fetchSynchronousFeed();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDestroy() {

            }

            @Override
            public int getCount() {
                return mItems != null ? mItems.size() : 0;
            }

            @Override
            public RemoteViews getViewAt(int position) {
                Log.d("KM", "getViewAt");
                if (position == AdapterView.INVALID_POSITION || mItems == null || mItems.size() == 0)
                    return null;
                final RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_feed_item);

                Item item = mItems.get(position);
                views.setTextViewText(R.id.widget_title_feed, item.getTitle());
                if (item.origin != null)
                    views.setTextViewText(R.id.widget_title_stream, item.origin.title);
                Bitmap background = null;
                if (!TextUtils.isEmpty(item.getUrlImage())) {
                    try {
                        background = Picasso.with(FeedWidgetRemoteViewsService.this)
                                .load(item.getUrlImage())
                                .placeholder(R.drawable.background_cover_protection)
                                .get();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (background != null)
                    views.setImageViewBitmap(R.id.widget_background, background);
                else
                    views.setImageViewResource(R.id.widget_background, R.drawable.background_cover_protection);

                final Intent fillInIntent = new Intent();
                fillInIntent.putExtra(Constants.ARG_EXTRA_ITEMS, item);
                views.setOnClickFillInIntent(R.id.widget, fillInIntent);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_feed_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        };
    }
}
