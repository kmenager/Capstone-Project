package io.github.kmenager.getmesomefeed.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.github.kmenager.getmesomefeed.injection.ApplicationContext;

@Singleton
public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "feed_pref_file";

    private static final String PREF_KEY_IS_CONNECTED = "PREF_KEY_IS_CONNECTED";

    private final SharedPreferences mPref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        mPref.edit().clear().apply();
    }


    /**
     * @param isConnected the user state
     */
    public void putIsConnected(boolean isConnected) {
        mPref.edit().putBoolean(PREF_KEY_IS_CONNECTED, isConnected).apply();
    }

    /**
     * @return true if the user is connected, false otherwise
     */
    public boolean getIsConnected() {
        return mPref.getBoolean(PREF_KEY_IS_CONNECTED, false);
    }

}
