package com.liftPlzz;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.stetho.Stetho;

public class LiftPlzz extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setAutoLogAppEventsEnabled(true);
        Stetho.initializeWithDefaults(this);
    }
}
