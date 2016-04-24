package io.github.kmenager.getmesomefeed.data.local.database.helper;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

import io.github.kmenager.getmesomefeed.data.local.database.ItemColumns;
import io.github.kmenager.getmesomefeed.data.local.model.database.ItemDatabase;

/**
 * Created by kevinmenager on 08/04/16.
 */
public class ItemHelper {

    public static ItemDatabase parseCursor(Cursor cursor) {
        ItemDatabase item = new ItemDatabase();
        item.id = cursor.getInt(cursor.getColumnIndexOrThrow(ItemColumns.ID));
        item.remoteId = cursor.getString(cursor.getColumnIndexOrThrow(ItemColumns.REMOTE_ID));
        long dateLong = cursor.getLong(cursor.getColumnIndexOrThrow(ItemColumns.DATE));
        item.date = dateLong == -1 ? null : new Date(dateLong);

        item.contentFull = cursor.getString(cursor.getColumnIndexOrThrow(ItemColumns.CONTENT_FULL));
        item.contentOffline = cursor.getString(cursor.getColumnIndexOrThrow(ItemColumns.CONTENT_OFFLINE));
        item.title = cursor.getString(cursor.getColumnIndexOrThrow(ItemColumns.TITLE));
        item.summary = cursor.getString(cursor.getColumnIndexOrThrow(ItemColumns.SUMMARY));

        item.urlArticle = cursor.getString(cursor.getColumnIndexOrThrow(ItemColumns.URL_ARTICLE));
        item.urlImage = cursor.getString(cursor.getColumnIndexOrThrow(ItemColumns.URL_IMAGE));

        return item;
    }

    public static ContentValues toContentValues(ItemDatabase itemDatabase) {
        ContentValues values = new ContentValues();
        values.put(ItemColumns.REMOTE_ID, itemDatabase.remoteId);
        values.put(ItemColumns.DATE, itemDatabase.date == null ? -1 : itemDatabase.date.getTime());
        values.put(ItemColumns.CONTENT_FULL, itemDatabase.contentFull);
        values.put(ItemColumns.CONTENT_OFFLINE, itemDatabase.contentOffline);
        values.put(ItemColumns.TITLE, itemDatabase.title);
        values.put(ItemColumns.SUMMARY, itemDatabase.summary);
        values.put(ItemColumns.URL_ARTICLE, itemDatabase.urlArticle);
        values.put(ItemColumns.URL_IMAGE, itemDatabase.urlImage);
        return values;
    }
}
