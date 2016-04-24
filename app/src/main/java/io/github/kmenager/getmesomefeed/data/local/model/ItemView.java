package io.github.kmenager.getmesomefeed.data.local.model;

import android.os.Parcelable;

/**
 * Created by kevinmenager on 24/04/16.
 */
public interface ItemView extends Parcelable {

    String getId();

    String getTitle();

    String getUrlImage();

    String getUrlArticle();

    String getContent();

    String getSummary();
}
