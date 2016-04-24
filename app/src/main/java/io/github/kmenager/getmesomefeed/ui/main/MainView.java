package io.github.kmenager.getmesomefeed.ui.main;

import java.util.List;

import io.github.kmenager.getmesomefeed.data.local.model.ItemView;
import io.github.kmenager.getmesomefeed.data.local.model.Stream;
import io.github.kmenager.getmesomefeed.data.model.MenuRowItem;
import io.github.kmenager.getmesomefeed.ui.base.MvpView;

public interface MainView extends MvpView {
    void showStream(Stream stream);

    void loadFeed(MenuRowItem menuRowItem);

    void showLocalItems(List<ItemView> items);
}
