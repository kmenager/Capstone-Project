package io.github.kmenager.getmesomefeed.ui.detail;

import io.github.kmenager.getmesomefeed.ui.base.MvpView;


public interface DetailView extends MvpView {

    void showToastItemBookmarked();

    void showBookmarked(boolean isBookmarked);
}
