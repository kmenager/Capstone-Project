package io.github.kmenager.getmesomefeed.data.local.database;


import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

public interface SubscriptionColumns {

    @DataType(DataType.Type.INTEGER)
    @AutoIncrement
    @PrimaryKey
    String ID = "_id";

    @DataType(DataType.Type.TEXT)
    String FEED_ID = "feed_id";

    @DataType(DataType.Type.TEXT)
    String TITLE = "title";

    @DataType(DataType.Type.TEXT)
    String ICON_URL = "icon_url";

    @DataType(DataType.Type.TEXT)
    String VISUAL_URL = "visual_url";

    @DataType(DataType.Type.INTEGER)
    String SUBSCRIBERS = "subscribers";
}
