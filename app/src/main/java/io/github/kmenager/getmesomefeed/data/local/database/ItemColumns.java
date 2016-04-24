package io.github.kmenager.getmesomefeed.data.local.database;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by kevinmenager on 08/04/16.
 */
public interface ItemColumns {

    @DataType(DataType.Type.INTEGER)
    @AutoIncrement
    @PrimaryKey
    String ID = "_id";

    @DataType(DataType.Type.TEXT)
    String REMOTE_ID = "remote_id";

    @DataType(DataType.Type.INTEGER)
    String DATE = "date";

    @DataType(DataType.Type.TEXT)
    String CONTENT_OFFLINE = "content_offline";

    @DataType(DataType.Type.TEXT)
    String CONTENT_FULL = "content_full";

    @DataType(DataType.Type.TEXT)
    String TITLE = "title";

    @DataType(DataType.Type.TEXT)
    String SUMMARY = "summary";

    @DataType(DataType.Type.TEXT)
    String URL_IMAGE = "url_image";

    @DataType(DataType.Type.TEXT)
    String URL_ARTICLE = "url_article";
}
