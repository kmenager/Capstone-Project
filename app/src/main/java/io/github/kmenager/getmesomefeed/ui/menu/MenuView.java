package io.github.kmenager.getmesomefeed.ui.menu;

import java.util.List;

import io.github.kmenager.getmesomefeed.data.model.MenuItemView;
import io.github.kmenager.getmesomefeed.ui.base.MvpView;


public interface MenuView extends MvpView {

    void showListSubscription(List<MenuItemView> menuItemViews);
}
