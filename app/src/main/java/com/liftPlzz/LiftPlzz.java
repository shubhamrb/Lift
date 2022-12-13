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
/*
        Mapbox.getInstance(this, "sk.eyJ1Ijoia29tYWxyYWdoYXZ6aWdseSIsImEiOiJjbDdlaml1cW0wMTEyM25scXc0ZWd0d3RuIn0.sy_5U7Uzfy_BmfAMD8cOAw");
*/
    }
}
