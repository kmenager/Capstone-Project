package io.github.kmenager.getmesomefeed.injection.component;

import dagger.Component;
import io.github.kmenager.getmesomefeed.ui.detail.DetailActivity;
import io.github.kmenager.getmesomefeed.ui.main.MainActivity;
import io.github.kmenager.getmesomefeed.injection.PerActivity;
import io.github.kmenager.getmesomefeed.injection.module.ActivityModule;
import io.github.kmenager.getmesomefeed.ui.search.SearchActivity;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity mainActivity);

    void inject(DetailActivity detailActivity);

    void inject(SearchActivity searchActivity);
}
