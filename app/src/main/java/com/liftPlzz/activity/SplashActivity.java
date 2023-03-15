package com.liftPlzz.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.liftPlzz.BuildConfig;
import com.liftPlzz.R;
import com.liftPlzz.model.appupdate.AppUpdateModel;
import com.liftPlzz.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferencesIntro;
    private String referral_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferencesIntro = getSharedPreferences(Constants.SHARED_PREF_INTRO, Context.MODE_PRIVATE);
        String model = sharedPreferences.getString(Constants.APP_UPDATE_MODEL, null);
        if (model != null) {
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
            long formattedDate = Long.parseLong(df.format(c));
            AppUpdateModel appUpdateModel = new Gson().fromJson(model, AppUpdateModel.class);
            if (formattedDate > appUpdateModel.getDate()) {
                getVersionCode();
            } else {
                initFirebaseAnalytics();
            }
        } else {
            getVersionCode();
        }
    }

    private void getVersionCode() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.GET, "https://charpair.com/api/app_detail", response -> {
            Constants.hideLoader();
            try {
                JSONObject jObject = new JSONObject(response);
                Type appUpdateModel = new TypeToken<AppUpdateModel>() {
                }.getType();
                AppUpdateModel updateModel = new Gson().fromJson(jObject.toString(), appUpdateModel);

                if (updateModel != null && updateModel.isStatus()) {
                    int current_version_code = BuildConfig.VERSION_CODE;

                    if (updateModel.getNew_version() > current_version_code) {
                        /*update available*/
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                        alertDialogBuilder.setTitle("Update available");
                        alertDialogBuilder.setMessage(updateModel.getUpdate_message()).setCancelable(false);
                        alertDialogBuilder.setPositiveButton("Update", (dialog, id) -> {
                            dialog.cancel();
                            final String appPackageName = getPackageName();
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        });
                        alertDialogBuilder.setNegativeButton("Cancel", (dialog, id) -> {
                            Date c = Calendar.getInstance().getTime();
                            SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
                            String formattedDate = df.format(c);
                            updateModel.setDate(Long.parseLong(formattedDate));
                            //current_date: 16032023
                            sharedPreferences.edit().putString(Constants.APP_UPDATE_MODEL, updateModel.toString()).apply();
                            dialog.cancel();
                            initFirebaseAnalytics();
                        });
                        AlertDialog alert = alertDialogBuilder.create();
                        alert.show();
                    } else {
                        initFirebaseAnalytics();
                    }
                } else {
                    initFirebaseAnalytics();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                initFirebaseAnalytics();

            }
        }, error -> {
            initFirebaseAnalytics();
        });
        queue.add(sr);
    }

    private void initFirebaseAnalytics() {
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(this, pendingDynamicLinkData -> {
            Uri deepLink;
            if (pendingDynamicLinkData != null && pendingDynamicLinkData.getLink() != null) {
                deepLink = pendingDynamicLinkData.getLink();
                List<String> pathSeg = deepLink.getPathSegments();
                if (pathSeg.size() >= 1) {
                    referral_id = pathSeg.get(0);
                    Log.e("Referral_id", referral_id);
                }
            }
            goToNext();
        }).addOnFailureListener(this, e -> {
            goToNext();
        });
    }

    private void goToNext() {
        new Handler().postDelayed(() -> {
            if (sharedPreferencesIntro.getBoolean(Constants.IS_INTRO, false)) {
                if (sharedPreferences.getBoolean(Constants.IS_LOGIN, false)) {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class).putExtra("referral_id", referral_id));
                } else {
                    startActivity(new Intent(SplashActivity.this, AuthActivity.class).putExtra("referral_id", referral_id));
                }
            } else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class).putExtra("referral_id", referral_id));
            }
            finish();
        }, 2000);
    }

    public static String printKeyHash(Context context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }
}
