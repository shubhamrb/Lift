package com.liftPlzz.activity;

import static com.liftPlzz.utils.Constants.NOTIFICATION_TYPE;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.liftPlzz.R;
import com.liftPlzz.adapter.MenuListAdapter;
import com.liftPlzz.dialog.EditLiftDaiFragment;
import com.liftPlzz.fragments.HomeFragment;
import com.liftPlzz.model.MenuItem;
import com.liftPlzz.model.completedLift.CompleteLiftData;
import com.liftPlzz.provider.AppNavigationProvider;
import com.liftPlzz.utils.Constants;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class HomeActivity extends AppNavigationProvider implements MenuListAdapter.ItemListener, PaymentResultListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    @BindView(R.id.placeHolder)
    FrameLayout placeHolder;
    @BindView(R.id.linerLayoutRoot)
    LinearLayout linerLayoutRoot;
    @BindView(R.id.left_drawer)
    RecyclerView leftDrawer;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    List<MenuItem> menuList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    @BindView(R.id.nav_view)
    NavigationView navView;
    TextView textViewName;
    TextView textViewNumber;
    CircleImageView imageViewConactImage;
    View header;
    LocationManager locationManager;

    @BindView(R.id.imfacebook)
    ImageView imfacebook;
    @BindView(R.id.iminstagram)
    ImageView iminstagram;
    @BindView(R.id.imtwitter)
    ImageView imtwitter;
    @BindView(R.id.imlinkedin)
    ImageView imlinkedin;

    FragmentManager fragmentManager;

    private final String id = "";
    private final String type = "";
    private Boolean isOpen = false;
    private String referral_id = null;

    @Override
    public int getPlaceHolder() {
        return R.id.placeHolder;
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        referral_id = getIntent().getStringExtra("referral_id");

        if (referral_id != null) {
            sendReferral();
        }

        fragmentManager = getSupportFragmentManager();
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        header = navView.getHeaderView(0);
        textViewName = header.findViewById(R.id.textViewName);
        textViewNumber = header.findViewById(R.id.textViewNumber);
        imageViewConactImage = header.findViewById(R.id.imageViewConactImage);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        header.setOnClickListener(v -> {
            closeDrawer();
            openProfileFragment(PerformFragment.REPLACE);
        });
        checkAndRequestPermissions();
        menuList.add(new MenuItem(1, getResources().getString(R.string.my_lifts), R.drawable.my_ride));
        menuList.add(new MenuItem(2, "Point Wallet", R.drawable.wallet));
        menuList.add(new MenuItem(3, getResources().getString(R.string.my_vehicle), R.drawable.my_vehicle));
        menuList.add(new MenuItem(4, getResources().getString(R.string.my_chat), R.drawable.chats));
        menuList.add(new MenuItem(5, "Refer & Earn", R.drawable.refer));
        menuList.add(new MenuItem(6, getResources().getString(R.string.txt_feedback), R.drawable.ic_white_liftplzz));
        menuList.add(new MenuItem(7, getResources().getString(R.string.txt_suggestion), R.drawable.ic_white_liftplzz));
        menuList.add(new MenuItem(8, getResources().getString(R.string.setting), R.drawable.setting));
        menuList.add(new MenuItem(9, getResources().getString(R.string.logout), R.drawable.logout));

        leftDrawer.setLayoutManager(new LinearLayoutManager(this));
        MenuListAdapter menuListAdapter = new MenuListAdapter(this, menuList, this);
        leftDrawer.setAdapter(menuListAdapter);

        //  printHashKey(this);
        imfacebook.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Char-pair-112112291298820/"));
            startActivity(intent);
        });
        iminstagram.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/___srb/"));
            startActivity(intent);
        });
        imtwitter.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/srb2305"));
            startActivity(intent);
        });
        imlinkedin.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/saurabh-sahu/"));
            startActivity(intent);
        });
        openHomeFragment(PerformFragment.REPLACE);

        getNotifications();
    }

    private void getNotifications() {
        if (getIntent().hasExtra("type")) {
            String type = getIntent().getStringExtra("type");
            int lift_id = -1, sub_cat_id = -1;
            boolean isPartner = false;

            if (getIntent().hasExtra(Constants.LIFT_ID)) {
                lift_id = getIntent().getIntExtra(Constants.LIFT_ID, -1);
            }
            if (getIntent().hasExtra(Constants.SUB_CATEGORY_ID)) {
                sub_cat_id = getIntent().getIntExtra(Constants.SUB_CATEGORY_ID, -1);
            }
            if (getIntent().hasExtra(Constants.PARTNER)) {
                isPartner = getIntent().getBooleanExtra(Constants.PARTNER, false);
            }
            if (getIntent().hasExtra("from")) {
                String from = getIntent().getStringExtra("from");
                openRideRequests(PerformFragment.REPLACE, false, lift_id, isPartner, from);
            } else {
                openRideRequests(PerformFragment.REPLACE, false, lift_id, isPartner, null);
            }
        }
    }

    @Override
    public void onclick(int s) {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        if (s == 1) {
            openMyRidesFragment(PerformFragment.REPLACE);
        } else if (s == 2) {
            openPointWalletFragment(PerformFragment.REPLACE);
        } else if (s == 3) {
            openMyVehicleFragment(PerformFragment.REPLACE);
        } else if (s == 4) {
            openMyChatFragment(PerformFragment.REPLACE);
        } else if (s == 5) {
            openReferFragment(PerformFragment.REPLACE);
        } else if (s == 6) {
            /*feedback*/
            openFeedbackSuggestionFragment(PerformFragment.REPLACE, "Feedback");
        } else if (s == 7) {
            /*suggestion*/
            openFeedbackSuggestionFragment(PerformFragment.REPLACE, "Suggestion");
        } else if (s == 8) {
            openSettingFragment(PerformFragment.REPLACE);
        } else if (s == 9) {
            final AlertDialog.Builder newBuilder = new AlertDialog.Builder(this);
            newBuilder.setMessage("Are you sure you want to Logout?");
            newBuilder.setPositiveButton("Yes", (dialog, which) -> {
                FirebaseAuth.getInstance().signOut();
                sharedPreferences.edit().putBoolean(Constants.IS_LOGIN, false).apply();
                sharedPreferences.edit().clear().apply();
                Intent intent = new Intent(HomeActivity.this, AuthActivity.class);
                startActivity(intent);
                finish();

            });
            newBuilder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            newBuilder.show();
        }
    }


    private void sendReferral() {
        /*API call*/
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "https://charpair.com/api/refferal_accepted", response -> {
            try {
                JSONObject jObject = new JSONObject(response);
                Log.d("success", jObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", Constants.API_KEY);
                params.put("client", Constants.ANDROID);
                params.put("token", sharedPreferences.getString(Constants.TOKEN, ""));
                params.put("refferal_id", referral_id);
                return params;
            }
        };
        queue.add(sr);
    }

    private boolean checkAndRequestPermissions() {
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarsePermision = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (coarsePermision != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), 1);
            return false;
        }
        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isOpen = false;
        if (NOTIFICATION_TYPE.equals("send-invitation") || NOTIFICATION_TYPE.equals("invitation-status-update")) {
            if (!isOpen) {
                openMyRidesFragment(PerformFragment.REPLACE);
                NOTIFICATION_TYPE = "";
                isOpen = true;
            }
        }
    }


    public void openHomeFragment(CompleteLiftData lift) {
        // openHomeFragment(PerformFragment.REPLACE);
        HomeFragment homeFragment = new HomeFragment();
        openFragment(homeFragment, HomeFragment.class.getName(), PerformFragment.REPLACE, false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void openDrawer() {
        textViewName.setText(sharedPreferences.getString(Constants.NAME, ""));
        textViewNumber.setText(sharedPreferences.getString(Constants.EMAIL, ""));
        Glide.with(this).load(sharedPreferences.getString(Constants.IMAGE, "hhh")).into(imageViewConactImage);
        drawerLayout.openDrawer(Gravity.LEFT);
    }


    public void closeDrawer() {
        drawerLayout.closeDrawer(Gravity.LEFT);

    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?").setCancelable(false).setPositiveButton("Goto Settings Page To Enable GPS", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void openride() {
        openMyRidesFragment(PerformFragment.REPLACE);
        getSupportFragmentManager().popBackStackImmediate(EditLiftDaiFragment.class.getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }


    @Override
    public void onPaymentSuccess(String s) {
        getPointWalletFragment().onPaymentSuccessResponse(s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        getPointWalletFragment().onPaymentErrorResponse(i, s);
    }
}
