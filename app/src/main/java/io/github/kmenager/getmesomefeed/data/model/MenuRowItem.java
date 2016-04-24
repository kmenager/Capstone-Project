package io.github.kmenager.getmesomefeed.data.model;

import io.github.kmenager.getmesomefeed.util.Constants;


public class MenuRowItem implements MenuItemView {


    public String id;
    public String title;
    public String coverUrl;

    @Override
    public int getType() {
        return Constants.VIEW_TYPE_ITEM;
    }
}
