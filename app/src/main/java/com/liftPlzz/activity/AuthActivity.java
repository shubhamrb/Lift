package com.liftPlzz.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AuthActivity extends AppNavigationProvider {

    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;

    @Override
    public int getPlaceHolder() {
        return R.id.placeHolder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
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
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_SMS
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

//    private boolean checkAndRequestPermissions() {
//
//
//        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
//
//
//                Manifest.permission.READ_SMS);
//
//
//        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
//
//
//        List<String> listPermissionsNeeded = new ArrayList<>();
//
//
//        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
//
//
//            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
//
//
//        }
//
//
//        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
//
//
//            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
//
//
//        }
//
//
//        if (!listPermissionsNeeded.isEmpty()) {
//
//
//            ActivityCompat.requestPermissions(AuthActivity.this,
//
//
//                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
//
//
//                    REQUEST_ID_MULTIPLE_PERMISSIONS);
//
//
//            return false;
//
//
//        }
//
//
//        return true;
//
//
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//
//
//                                           String permissions[], int[] grantResults) {
//
//
//        Log.d("Permission", "Permission callback called-------");
//
//
//        switch (requestCode) {
//
//
//            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
//
//
//                Map<String, Integer> perms = new HashMap<>();
//
//
//                // Initialize the map with both permissions
//
//
//                perms.put(Manifest.permission.READ_SMS, PackageManager.PERMISSION_GRANTED);
//
//
//                perms.put(Manifest.permission.RECEIVE_SMS, PackageManager.PERMISSION_GRANTED);
//
//
//                // Fill with actual results from user
//
//
//                if (grantResults.length > 0) {
//
//
//                    for (int i = 0; i < permissions.length; i++)
//
//
//                        perms.put(permissions[i], grantResults[i]);
//
//
//                    // Check for both permissions
//
//
//                    if (perms.get(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
//
//
//                            && perms.get(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
//
//
//                        Log.d("sms", "READ_SMS & RECEIVE_SMS services permission granted");
//
//
//                        // process the normal flow
//
//
//                        //else any one or both the permissions are not granted
//
//
//                    } else {
//
//
//                        Log.d("Some", "Some permissions are not granted ask again ");
//
//
//                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//
//
////                        // shouldShowRequestPermissionRationale will return true
//
//
//                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
//
//
//                        if (ActivityCompat.shouldShowRequestPermissionRationale(AuthActivity.this,
//
//
//                                Manifest.permission.READ_SMS) ||
//
//
//                                ActivityCompat.shouldShowRequestPermissionRationale(AuthActivity.this, Manifest.permission.RECEIVE_SMS)) {
//
//
//                            showDialogOK("Read and Recieve SMS Permission required for this app",
//
//
//                                    new DialogInterface.OnClickListener() {
//
//
//                                        @Override
//
//
//                                        public void onClick(DialogInterface dialog, int which) {
//
//
//                                            switch (which) {
//
//
//                                                case DialogInterface.BUTTON_POSITIVE:
//
//
//                                                    checkAndRequestPermissions();
//
//
//                                                    break;
//
//
//                                                case DialogInterface.BUTTON_NEGATIVE:
//
//
//                                                    // proceed with logic by disabling the related features or quit the app.
//
//
//                                                    break;
//
//
//                                            }
//
//
//                                        }
//
//
//                                    });
//
//
//                        }
//
//
//                        //permission is denied (and never ask again is  checked)
//
//
//                        //shouldShowRequestPermissionRationale will return false
//
//
//                        else {
//
//
//                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
//
//
//                            //                            //proceed with logic by disabling the related features or quit the app.
//
//
//                        }
//
//
//                    }
//
//
//                }
//
//
//            }
//
//
//        }
//
//
//    }
//
//
//    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
//
//
//        new AlertDialog.Builder(this)
//
//
//                .setMessage(message)
//
//
//                .setPositiveButton("OK", okListener)
//
//
//                .setNegativeButton("Cancel", okListener)
//
//
//                .create()
//
//
//                .show();
//
//
//    }

}
