package io.github.kmenager.getmesomefeed.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import io.github.kmenager.getmesomefeed.FeedApplication;
import io.github.kmenager.getmesomefeed.data.DataManager;
import io.github.kmenager.getmesomefeed.injection.module.ApplicationModule;
import io.github.kmenager.getmesomefeed.widget.FeedWidgetRemoteViewsService;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(FeedApplication pulseApplication);

    DataManager dataManager();

    void inject(FeedWidgetRemoteViewsService feedWidgetRemoteViewsService);
}
