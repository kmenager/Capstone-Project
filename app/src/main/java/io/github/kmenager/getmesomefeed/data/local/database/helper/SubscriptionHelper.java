package io.github.kmenager.getmesomefeed.data.local.database.helper;

import android.content.ContentValues;
import android.database.Cursor;

import io.github.kmenager.getmesomefeed.data.local.database.SubscriptionColumns;
import io.github.kmenager.getmesomefeed.data.local.model.database.SubscriptionDatabase;


public class SubscriptionHelper {

    public static SubscriptionDatabase parseCursor(Cursor cursor) {
        SubscriptionDatabase subscriptionDatabase = new SubscriptionDatabase();
        subscriptionDatabase.id = cursor.getInt(cursor.getColumnIndexOrThrow(SubscriptionColumns.ID));
        subscriptionDatabase.feedId = cursor.getString(cursor.getColumnIndexOrThrow(SubscriptionColumns.FEED_ID));
        subscriptionDatabase.title = cursor.getString(cursor.getColumnIndexOrThrow(SubscriptionColumns.TITLE));
        subscriptionDatabase.iconUrl = cursor.getString(cursor.getColumnIndexOrThrow(SubscriptionColumns.ICON_URL));
        subscriptionDatabase.visualUrl = cursor.getString(cursor.getColumnIndexOrThrow(SubscriptionColumns.VISUAL_URL));
        subscriptionDatabase.subscribers = cursor.getInt(cursor.getColumnIndexOrThrow(SubscriptionColumns.SUBSCRIBERS));

        return subscriptionDatabase;
    }

    public static ContentValues toContentValues(SubscriptionDatabase subscriptionDatabase) {
        ContentValues values = new ContentValues();
        values.put(SubscriptionColumns.FEED_ID, subscriptionDatabase.feedId);
        values.put(SubscriptionColumns.TITLE, subscriptionDatabase.title);
        values.put(SubscriptionColumns.ICON_URL, subscriptionDatabase.iconUrl);
        values.put(SubscriptionColumns.VISUAL_URL, subscriptionDatabase.visualUrl);
        values.put(SubscriptionColumns.SUBSCRIBERS, subscriptionDatabase.subscribers);
        return values;
    }
}
