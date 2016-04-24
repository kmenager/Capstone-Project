package io.github.kmenager.getmesomefeed.ui.base;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import io.github.kmenager.getmesomefeed.FeedApplication;
import io.github.kmenager.getmesomefeed.injection.component.ActivityComponent;
import io.github.kmenager.getmesomefeed.injection.component.DaggerActivityComponent;
import io.github.kmenager.getmesomefeed.injection.module.ActivityModule;


public class BaseActivity extends AppCompatActivity {
    private ActivityComponent mActivityComponent;


    public ActivityComponent activityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(FeedApplication.get(this).getComponent())
                    .build();
        }
        return mActivityComponent;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }
}
