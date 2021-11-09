package com.liftPlzz.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.liftPlzz.R;
import com.liftPlzz.adapter.MenuListAdapter;
import com.liftPlzz.model.MenuItem;
import com.liftPlzz.provider.AppNavigationProvider;
import com.liftPlzz.utils.Constants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeActivity extends AppNavigationProvider implements MenuListAdapter.ItemListener {


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
    ImageView imageViewConactImage;
    View header;
    LocationManager locationManager;


    @Override
    public int getPlaceHolder() {
        return R.id.placeHolder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        header = navView.getHeaderView(0);
        textViewName = header.findViewById(R.id.textViewName);
        textViewNumber = header.findViewById(R.id.textViewNumber);
        imageViewConactImage = header.findViewById(R.id.imageViewConactImage);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                openProfileFragment(PerformFragment.REPLACE);

            }
        });
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
        }
        menuList.add(new MenuItem(1, getResources().getString(R.string.post_list), R.drawable.ic_white_liftplzz));
        menuList.add(new MenuItem(2, getResources().getString(R.string.my_lifts), R.drawable.ic_white_liftplzz));
        menuList.add(new MenuItem(3, getResources().getString(R.string.my_payments), R.drawable.ic_white_liftplzz));
        menuList.add(new MenuItem(4, getResources().getString(R.string.my_vehicle), R.drawable.ic_white_liftplzz));
        menuList.add(new MenuItem(5, getResources().getString(R.string.help_ticket), R.drawable.ic_white_liftplzz));
        menuList.add(new MenuItem(6, getResources().getString(R.string.txt_faq), R.drawable.ic_white_liftplzz));
        menuList.add(new MenuItem(7, getResources().getString(R.string.setting), R.drawable.ic_white_liftplzz));
        menuList.add(new MenuItem(8, getResources().getString(R.string.logout), R.drawable.ic_white_liftplzz));

        leftDrawer.setLayoutManager(new LinearLayoutManager(this));
        MenuListAdapter menuListAdapter = new MenuListAdapter(this, menuList, this);
        leftDrawer.setAdapter(menuListAdapter);
        openHomeFragment(PerformFragment.REPLACE);
        printHashKey(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void openDrawer() {
        textViewName.setText(sharedPreferences.getString(Constants.NAME, ""));
        textViewNumber.setText(sharedPreferences.getString(Constants.EMAIL, ""));
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
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public void onclick(int s) {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
        if (s == 1) {
            //getSupportFragmentManager().popBackStack(FeedbackFragment.class.getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
//             openFeedbackFragment(PerformFragment.REPLACE);
        } else if (s == 2) {
//            Intent intent = new Intent(HomeActivity.this, MatchingRideActivity.class);
////            intent.putExtra(Constants.LIFT_ID, lift.getId());
//            startActivity(intent);
            openMyRidesFragment(PerformFragment.REPLACE);
        } else if (s == 3) {
//             openInventoryFragment(PerformFragment.REPLACE);
        } else if (s == 4) {
            openMyVehicleFragment(PerformFragment.REPLACE);
        } else if (s == 5) {
//             openContactsFragment(PerformFragment.REPLACE);
//            openDriverListFragment(PerformFragment.REPLACE);
            Intent intent = new Intent(HomeActivity.this, TicketListActivity.class);
            startActivity(intent);
        } else if (s == 6) {
            openFaqFragment(PerformFragment.REPLACE);
        } else if (s == 7) {
            openSettingFragment(PerformFragment.REPLACE);
        } else if (s == 8) {
            final AlertDialog.Builder newBuilder = new AlertDialog.Builder(this);
            newBuilder.setMessage("Are you sure you want to Logout?");
            newBuilder.setPositiveButton("Yes", (dialog, which) -> {
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
}
