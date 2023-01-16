package com.liftPlzz.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import com.liftPlzz.R;
import com.liftPlzz.provider.AppNavigationProvider;


public class AuthActivity extends AppNavigationProvider {

    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;

    @Override
    public int getPlaceHolder() {
        return R.id.placeHolder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        String referral_id = getIntent().getStringExtra("referral_id");
        openLoginFragment(PerformFragment.REPLACE, referral_id);
        if (!checkLocationPermission()) {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
//                            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
//                            Manifest.permission.READ_SMS,
//                            Manifest.permission.RECEIVE_SMS
                    },
                    PERMISSION_REQUEST_CODE_LOCATION);
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
//                            Manifest.permission.READ_SMS,
//                            Manifest.permission.RECEIVE_SMS
                    },/**/
                    PERMISSION_REQUEST_CODE_LOCATION);
        }
    }

    public boolean checkLocationPermission() {

        int readStorage =
                getPackageManager()
                        .checkPermission(
                                Manifest.permission.ACCESS_FINE_LOCATION, getPackageName());

        /*int backgroundLocation =
                getPackageManager()
                        .checkPermission(
                                Manifest.permission.ACCESS_BACKGROUND_LOCATION, getPackageName());*/

        int writeStorage =
                getPackageManager()
                        .checkPermission(
                                Manifest.permission.ACCESS_COARSE_LOCATION, getPackageName());

        /*int readSms =
                getPackageManager()
                        .checkPermission(
                                Manifest.permission.READ_SMS, getPackageName());*/

        return readStorage == PackageManager.PERMISSION_GRANTED
//                && backgroundLocation == PackageManager.PERMISSION_GRANTED
                && writeStorage == PackageManager.PERMISSION_GRANTED;
//                && readSms == PackageManager.PERMISSION_GRANTED;
    }
}
