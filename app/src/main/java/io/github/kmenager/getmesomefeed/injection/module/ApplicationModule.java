package io.github.kmenager.getmesomefeed.injection.module;

import android.app.Application;
import android.content.Context;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.github.kmenager.getmesomefeed.data.remote.FeedService;
import io.github.kmenager.getmesomefeed.injection.ApplicationContext;

@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    FeedService provideFeedService() {
        return FeedService.Factory.makeFeedService();
    }

}
