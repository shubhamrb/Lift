package com.liftPlzz.activity.Navigation;

import android.app.PictureInPictureParams;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Rational;
import android.view.Display;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.liftPlzz.R;
/*import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.services.android.navigation.ui.v5.MapOfflineOptions;
import com.mapbox.services.android.navigation.ui.v5.NavigationView;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.services.android.navigation.ui.v5.OnNavigationReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.listeners.NavigationListener;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigationOptions;
import com.mapbox.services.android.navigation.v5.navigation.NavigationConstants;*/


public class MapboxNavigationActivity extends AppCompatActivity /*implements OnNavigationReadyCallback,
        NavigationListener*/ {

    //    private NavigationView navigationView;
    private AppCompatTextView txtShareCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        String share_code = getIntent().getStringExtra("share_code");

        /*navigationView = findViewById(R.id.navigationView);
        txtShareCode = findViewById(R.id.txtShareCode);
        if (share_code != null) {
            txtShareCode.setText(share_code);
            txtShareCode.setVisibility(View.VISIBLE);
        }

        navigationView.onCreate(savedInstanceState);
        initialize();*/
    }

    @Override
    public void onStart() {
        super.onStart();
//        navigationView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
//        navigationView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
//        navigationView.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        /*if (!navigationView.onBackPressed()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                enterPipMode();
            } else {
                super.onBackPressed();
            }
        }*/
    }

    private void enterPipMode() {
        try {
            Display d = getWindowManager()
                    .getDefaultDisplay();
            Point p = new Point();
            d.getSize(p);
            int width = p.x;
            int height = p.y;

            Rational ratio = new Rational(width, height);
            PictureInPictureParams.Builder pip_Builder = new PictureInPictureParams.Builder();
            pip_Builder.setAspectRatio(ratio).build();
            enterPictureInPictureMode(pip_Builder.build());
        } catch (Exception e) {
            e.printStackTrace();
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        navigationView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        navigationView.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
//        navigationView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
//        navigationView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        navigationView.onDestroy();
    }

    /*@Override
    public void onNavigationReady(boolean isRunning) {
        NavigationViewOptions.Builder options = NavigationViewOptions.builder();
        options.navigationListener(this);
        extractRoute(options);
        extractConfiguration(options);
        options.navigationOptions(MapboxNavigationOptions.builder().build());
        navigationView.startNavigation(options.build());
    }

    @Override
    public void onCancelNavigation() {
        finishNavigation();
    }

    @Override
    public void onNavigationFinished() {
        finishNavigation();
    }

    @Override
    public void onNavigationRunning() {
        // Intentionally empty
    }

    private void initialize() {
        Parcelable position = getIntent().getParcelableExtra(NavigationConstants.NAVIGATION_VIEW_INITIAL_MAP_POSITION);
        if (position != null) {
            navigationView.initialize(this, (CameraPosition) position);
        } else {
            navigationView.initialize(this);
        }
    }

    private void extractRoute(NavigationViewOptions.Builder options) {
        DirectionsRoute route = NavigationLauncher.extractRoute(this);
        options.directionsRoute(route);
    }

    private void extractConfiguration(NavigationViewOptions.Builder options) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        options.shouldSimulateRoute(preferences.getBoolean(NavigationConstants.NAVIGATION_VIEW_SIMULATE_ROUTE, false));
        String offlinePath = preferences.getString(NavigationConstants.OFFLINE_PATH_KEY, "");
        if (!offlinePath.isEmpty()) {
            options.offlineRoutingTilesPath(offlinePath);
        }
        String offlineVersion = preferences.getString(NavigationConstants.OFFLINE_VERSION_KEY, "");
        if (!offlineVersion.isEmpty()) {
            options.offlineRoutingTilesVersion(offlineVersion);
        }
        String offlineMapDatabasePath = preferences.getString(NavigationConstants.MAP_DATABASE_PATH_KEY, "");
        String offlineMapStyleUrl = preferences.getString(NavigationConstants.MAP_STYLE_URL_KEY, "");
        if (!offlineMapDatabasePath.isEmpty() && !offlineMapStyleUrl.isEmpty()) {
            MapOfflineOptions mapOfflineOptions = new MapOfflineOptions(offlineMapDatabasePath, offlineMapStyleUrl);
            options.offlineMapOptions(mapOfflineOptions);
        }
    }

    private void finishNavigation() {
        NavigationLauncher.cleanUpPreferences(this);
        finish();
    }*/
}
