package io.github.kmenager.getmesomefeed.ui.search;

import java.util.List;

import io.github.kmenager.getmesomefeed.data.local.model.Result;
import io.github.kmenager.getmesomefeed.ui.base.MvpView;

public interface SearchFeedView extends MvpView {

    void showSearchResult(List<Result> results);

    void updateList(Result result);
}
