package io.github.kmenager.getmesomefeed.injection.module;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import io.github.kmenager.getmesomefeed.injection.ActivityContext;

@Module
public class FragmentModule {
    private Activity mActivity;

    public FragmentModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return mActivity;
    }
}
