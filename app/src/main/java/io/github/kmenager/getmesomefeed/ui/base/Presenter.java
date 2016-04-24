package io.github.kmenager.getmesomefeed.ui.base;


public interface Presenter<T> {

    void attachView(T mvpView);

    void detachView();
}
