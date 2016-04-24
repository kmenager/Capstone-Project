package io.github.kmenager.getmesomefeed.data.local.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.OnConfigure;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;

@Database(version = FeedDatabase.VERSION,
        packageName = "io.github.kmenager.getmesomefeed.provider")
public class FeedDatabase {


    public static final int VERSION = 1;

    @Table(ItemColumns.class)
    public static final String ITEMS = "items";

    @Table(SubscriptionColumns.class)
    public static final String SUBSCRIPTIONS = "subscriptions";

    @OnCreate
    public static void onCreate(Context context, SQLiteDatabase db) {
    }

    @OnUpgrade
    public static void onUpgrade(Context context, SQLiteDatabase db, int oldVersion,
                                 int newVersion) {
    }

    @OnConfigure
    public static void onConfigure(SQLiteDatabase db) {
    }
}
