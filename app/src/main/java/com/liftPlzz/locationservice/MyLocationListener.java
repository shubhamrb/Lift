package com.liftPlzz.locationservice;

import android.location.Location;

public interface MyLocationListener {
    void onSuccess(Location location);
    void onFailed();
}
