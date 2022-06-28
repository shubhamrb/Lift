package com.liftPlzz.activity;

import static com.liftPlzz.utils.Constants.NOTIFICATION_TYPE;
import static com.liftPlzz.utils.Constants.isLiftOnGoing;

import android.app.PictureInPictureParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Rational;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.liftPlzz.R;
import com.liftPlzz.adapter.MenuListAdapter;
import com.liftPlzz.dialog.EditLiftDaiFragment;
import com.liftPlzz.fragments.HomeFragment;
import com.liftPlzz.model.MenuItem;
import com.liftPlzz.model.completedLift.CompleteLiftData;
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

    @Override
    public int getPlaceHolder() {
        return R.id.placeHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void enterPipMode() {

        Rational aspectRatio = new Rational(2, 3);
        PictureInPictureParams params = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params = new PictureInPictureParams.Builder().setAspectRatio(aspectRatio)
                    .build();
        }
        if (params != null)
            enterPictureInPictureMode(params);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPause() {
        super.onPause();
        enterPipMode();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

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
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
        }
        // menuList.add(new MenuItem(1, getResources().getString(R.string.post_list), R.drawable.ic_white_liftplzz));
        menuList.add(new MenuItem(2, getResources().getString(R.string.my_lifts), R.drawable.ic_white_liftplzz));
        menuList.add(new MenuItem(10, "Payments & Recharge", R.drawable.ic_white_liftplzz));
        // menuList.add(new MenuItem(9, "Payments Recharge", R.drawable.ic_white_liftplzz));
        menuList.add(new MenuItem(4, getResources().getString(R.string.my_vehicle), R.drawable.ic_white_liftplzz));
        menuList.add(new MenuItem(5, getResources().getString(R.string.my_chat), R.drawable.ic_white_liftplzz));
        menuList.add(new MenuItem(3, "Refer & Earn", R.drawable.ic_white_liftplzz));
        //  menuList.add(new MenuItem(11, "Follow Char Pair", R.drawable.ic_white_liftplzz));
        menuList.add(new MenuItem(6, getResources().getString(R.string.help_ticket), R.drawable.ic_white_liftplzz));
        menuList.add(new MenuItem(7, getResources().getString(R.string.txt_faq), R.drawable.ic_white_liftplzz));
        menuList.add(new MenuItem(8, getResources().getString(R.string.setting), R.drawable.ic_white_liftplzz));
        menuList.add(new MenuItem(9, getResources().getString(R.string.logout), R.drawable.ic_white_liftplzz));

        leftDrawer.setLayoutManager(new LinearLayoutManager(this));
        MenuListAdapter menuListAdapter = new MenuListAdapter(this, menuList, this);
        leftDrawer.setAdapter(menuListAdapter);
        openHomeFragment(PerformFragment.REPLACE);
        //  printHashKey(this);
        imfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/Char-pair-112112291298820/"));
                startActivity(intent);
            }
        });

        iminstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/___srb/"));
                startActivity(intent);
            }
        });
        imtwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/srb2305"));
                startActivity(intent);
            }
        });
        imlinkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/saurabh-sahu/"));
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        isOpen = false;
        if (NOTIFICATION_TYPE.equals("send-invitation") ||
                NOTIFICATION_TYPE.equals("invitation-status-update")) {
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
        /*HomeFragment homeFragment = (HomeFragment) fragmentManager.findFragmentByTag(HomeFragment.class.getName());
        if(isLiftOnGoing){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if(homeFragment!=null && homeFragment.isVisible()){
                    enterPipMode();
                }else {
                    super.onBackPressed();
                }
            }else {
                super.onBackPressed();
            }
        }else {
            super.onBackPressed();
        }*/
    }

    @Override
    protected void onStop() {
        super.onStop();
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

    public void openride() {
        openMyRidesFragment(PerformFragment.REPLACE);
        getSupportFragmentManager().popBackStackImmediate(EditLiftDaiFragment.class.getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

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
//            intent.putExtra(Constants.LIFT_ID, lift.getId());
//            startActivity(intent);
            openMyRidesFragment(PerformFragment.REPLACE);
        } else if (s == 3) {
            Intent intent = new Intent(HomeActivity.this, PaymentHistory.class);
            startActivity(intent);
//             openInventoryFragment(PerformFragment.REPLACE);
        } else if (s == 4) {
            openMyVehicleFragment(PerformFragment.REPLACE);
        } else if (s == 5) {
            openMyChatFragment(PerformFragment.REPLACE);
        } else if (s == 6) {
//             openContactsFragment(PerformFragment.REPLACE);
//             openDriverListFragment(PerformFragment.REPLACE);
            Intent intent = new Intent(HomeActivity.this, TicketListActivity.class);
            startActivity(intent);
        } else if (s == 7) {
            openFaqFragment(PerformFragment.REPLACE);
        } else if (s == 8) {
            openSettingFragment(PerformFragment.REPLACE);
        } else if (s == 9) {
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
        } else if (s == 10) {
            Intent intent = new Intent(HomeActivity.this, PaymentPackage.class);
            startActivity(intent);
        }
    }
}
