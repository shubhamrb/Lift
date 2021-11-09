package com.liftPlzz.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.liftPlzz.R;
import com.liftPlzz.provider.AppNavigationProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


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
        openLoginFragment(PerformFragment.REPLACE);
        if (!checkLocationPermission()) {
            requestLocationPermission();
        }
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                PERMISSION_REQUEST_CODE_LOCATION);
    }

    public boolean checkLocationPermission() {

        int readStorage =
                getPackageManager()
                        .checkPermission(
                                Manifest.permission.ACCESS_FINE_LOCATION, getPackageName());
        int writeStorage =
                getPackageManager()
                        .checkPermission(
                                Manifest.permission.ACCESS_COARSE_LOCATION, getPackageName());

        int readSms =
                getPackageManager()
                        .checkPermission(
                                Manifest.permission.READ_SMS, getPackageName());

        return readStorage == PackageManager.PERMISSION_GRANTED
                && writeStorage == PackageManager.PERMISSION_GRANTED
                && readSms == PackageManager.PERMISSION_GRANTED;
    }

}
