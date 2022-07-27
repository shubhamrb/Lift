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

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.liftPlzz.R;
import com.liftPlzz.utils.Constants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences sharedPreferencesIntro;
    private String referral_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferencesIntro =
                getSharedPreferences(Constants.SHARED_PREF_INTRO, Context.MODE_PRIVATE);
        initFirebaseAnalytics();
    }

    private void initFirebaseAnalytics() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    Uri deepLink;
                    if (pendingDynamicLinkData != null && pendingDynamicLinkData.getLink() != null) {
                        deepLink = pendingDynamicLinkData.getLink();
                        List<String> pathSeg = deepLink.getPathSegments();
                        if (pathSeg.size() > 1) {
                            referral_id = pathSeg.get(1);
                            Log.e("Referral_id", referral_id);
                        }
                    }
                    goToNext();
                })
                .addOnFailureListener(this, e -> {
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
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

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
