package io.github.kmenager.getmesomefeed.data.model;

import android.net.Uri;

import io.github.kmenager.getmesomefeed.util.Constants;

/**
 * Created by kevinmenager on 13/04/16.
 */
public class MenuHeader implements MenuItemView {

    public String displayName;
    public String email;
    public Uri urlProfile;

    @Override
    public int getType() {
        return Constants.VIEW_TYPE_HEADER;
    }
}
