package io.github.kmenager.getmesomefeed.data.local.database;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = FeedProvider.AUTHORITY,
        database = FeedDatabase.class,
        packageName = "io.github.kmenager.getmesomefeed.provider")
public final class FeedProvider {

    private FeedProvider() {
    }

    public static final String AUTHORITY = "io.github.kmenager.getmesomefeed.FeedProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    interface Path {
        String ITEMS = "items";
        String SUBSCRIPTIONS = "subscriptions";
        String WITH_REMOTE = "withRemote";
    }

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(table = FeedDatabase.ITEMS)
    public static class Items {

        @ContentUri(
                path = Path.ITEMS,
                type = "vnd.android.cursor.dir/item",
                defaultSort = ItemColumns.DATE + " DESC")
        public static final Uri CONTENT_URI = buildUri(Path.ITEMS);

        @InexactContentUri(
                path = Path.ITEMS + "/#",
                name = "ITEM_ID",
                type = "vnd.android.cursor.item/item",
                whereColumn = ItemColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.ITEMS, String.valueOf(id));
        }

        @InexactContentUri(
                path = Path.ITEMS + "/" + Path.WITH_REMOTE + "/#",
                name = "ITEM_WITH_REMOTE_ID",
                type = "vnd.android.cursor.item/item",
                whereColumn = ItemColumns.REMOTE_ID,
                pathSegment = 2)
        public static Uri withRemoteId(String remoteId) {
            return buildUri(Path.ITEMS, Path.WITH_REMOTE, remoteId);
        }
    }

    @TableEndpoint(table = FeedDatabase.SUBSCRIPTIONS)
    public static class Subscriptions {

        @ContentUri(
                path = Path.SUBSCRIPTIONS,
                type = "vnd.android.cursor.dir/subscription")
        public static final Uri CONTENT_URI = buildUri(Path.SUBSCRIPTIONS);

        @InexactContentUri(
                path = Path.SUBSCRIPTIONS + "/#",
                name = "SUBSCRIPTION_ID",
                type = "vnd.android.cursor.item/subscription",
                whereColumn = SubscriptionColumns.ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(Path.SUBSCRIPTIONS, String.valueOf(id));
        }

        @InexactContentUri(
                path = Path.SUBSCRIPTIONS + "/" + Path.WITH_REMOTE + "/#",
                name = "SUBSCRIPTION_WITH_REMOTE_ID",
                type = "vnd.android.cursor.item/subscription",
                whereColumn = SubscriptionColumns.FEED_ID,
                pathSegment = 2)
        public static Uri withRemoteId(String remoteId) {
            return buildUri(Path.SUBSCRIPTIONS, Path.WITH_REMOTE, remoteId);
        }
    }
}
