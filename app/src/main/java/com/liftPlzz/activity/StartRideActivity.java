package com.liftPlzz.activity;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.PictureInPictureParams;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Rational;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.liftPlzz.R;
import com.liftPlzz.adapter.LiftPartnerAdapter;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.locationservice.LocationUpdatesService;
import com.liftPlzz.locationservice.Utils;
import com.liftPlzz.model.on_going.InnerGoingResponse;
import com.liftPlzz.model.on_going.LiftUsers;
import com.liftPlzz.model.on_going.MainOnGoingResponse;
import com.liftPlzz.model.upcomingLift.Lift;
import com.liftPlzz.utils.Constants;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartRideActivity extends AppCompatActivity implements
        OnMapReadyCallback, PaymentResultListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase;
    private DatabaseReference StoreLoactiontoDatabaseReference;
    private DatabaseReference HistoryStoreLoactiontoDatabaseReference;
    GoogleMap mGoogleMap;
    SharedPreferences sharedPreferences;
    SupportMapFragment mapFragment;
    Button callButton;
    Button smsButton;
    String sos;
    Location Previouslocation = null;
    boolean ridestarted = false;
    boolean apimazoomcompleted = false;
    int startedcount = 0;
    int driverlocationcount = 0;
    ArrayList<LatLng> linelocationList = new ArrayList<>();
    ArrayList<LatLng> historylocationList = new ArrayList<>();
    private LatLng prelatLng;
    TextView totalpointt;
    String totalpoint;
    //++++++++++++++++++++++++++++++++++++++++++++Rating+++++++++++++
    private JSONArray users;
    AlertDialog finalAlert;
    private String driver_id;
    private Integer request_id = -1;
    private int bywhomRidestarted = -1;
    private Location location;
    private String tracking_lift_id;
    private Context mainContext;
    private boolean driverstarted = false;
    private Marker mDriverMarker;
    private boolean isAlreadyStarted = false;
    private InnerGoingResponse onGoingListResponse;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private final List<LiftUsers> liftUsersList = new ArrayList<>();
    private List<LatLng> pontos = new ArrayList<>();
    private Polyline polyline;
    List<Polyline> polylineList = new ArrayList<>();
    private String wayPoints = "";
    private Marker startMarker;
    private boolean isTrackingPath = false;
    private boolean oneTimeZoomed = false;
    private Dialog dialog;
    /*
        private DirectionsRoute currentRoute;
    */
    private FusedLocationProviderClient fusedLocationProviderClient;
    private AsyncTask<String, String, String> locationTask;


    /**
     * Receiver for broadcasts sent by {@link LocationUpdatesService}.
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
                Log.e("Driver started is", "Ride " + bywhomRidestarted);
                if (bywhomRidestarted == 0) {
                    Log.e(TAG, "Location Same location" + startedcount);
                    if (startedcount == 0) {
                        startedcount = 3;
                        if (!isAlreadyStarted)
                            startDriverLift(location, strToken);
                    } else {
                        if (location != null) {
                            if (Previouslocation == null) {
                                historylocationList.add(new LatLng(location.getLatitude(), location.getLongitude()));
                            } else if (location.getLatitude() == Previouslocation.getLatitude() || (location.getLongitude() == Previouslocation.getLongitude())) {
                                Log.e(TAG, "Location is Same");
                            } else {
                                historylocationList.add(new LatLng(location.getLatitude(), location.getLongitude()));
                            }
                            Previouslocation = location;
                            String livelocation = location.getLatitude() + "," + location.getLongitude();
                            Log.e(TAG, "Location livelocation" + livelocation);
                            StoreLoactiontoDatabaseReference = FirebaseDatabase.getInstance().getReference();
                            StoreLoactiontoDatabaseReference = StoreLoactiontoDatabaseReference.child("LocationMap").child("Drivers").child(sharedPreferences.getString(Constants.USER_ID, "")).child(tracking_lift_id).child("location");
                            StoreLoactiontoDatabaseReference.setValue(livelocation).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.e(TAG, "Location Success");
                                    StoreLoactiontoDatabaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                        @Override
                                        public void onSuccess(DataSnapshot dataSnapshot) {
                                            String[] Latbroken = dataSnapshot.getValue().toString().split(",");
                                            Log.e(TAG, "Location Retrieved" + Latbroken[0] + " and " + Latbroken[1]);
                                            Location targetLocation = new Location("");//provider name is unnecessary
                                            targetLocation.setLatitude(Double.parseDouble(Latbroken[0]));//your coords of course
                                            targetLocation.setLongitude(Double.parseDouble(Latbroken[1]));
//                                            placeTheCUrrentmarker(targetLocation);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {
                                            e.printStackTrace();
                                            Log.e(TAG, "Location Failed");

                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull @NotNull Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }
                } else if (bywhomRidestarted == 1) {
                    Log.e("Driver started is", "User" + bywhomRidestarted);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (myReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        }
        if (mService != null) {
            mService.removeLocationUpdates();
        }
        super.onDestroy();
    }

    private void placeTheCUrrentmarker(Location location) {
        LatLng latLngOrigin = new LatLng(location.getLatitude(), location.getLongitude());
        mGoogleMap.clear();
        mGoogleMap.addMarker(new MarkerOptions()
                .position(latLngOrigin)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pic_location))
                .title("Driver"));

        boolean contains = mGoogleMap.getProjection()
                .getVisibleRegion()
                .latLngBounds
                .contains(latLngOrigin);

        if (!apimazoomcompleted) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 15.0f));
        } else {
            if (!contains) {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latLngOrigin).zoom(15.0f).build();
                mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }
        apimazoomcompleted = true;
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    private static final String TAG = StartRideActivity.class.getSimpleName();

    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private LocationUpdatesService mService = null;
    private boolean mBound = false;

    private MyReceiver myReceiver;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    @BindView(R.id.txtShareCode)
    AppCompatTextView txtShareCode;

    @BindView(R.id.btn_navigate)
    RelativeLayout btn_navigate;

    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.imageViewOption)
    ImageView imageViewOption;
    @BindView(R.id.btn_start_ride)
    AppCompatTextView tvStartRide;

    @BindView(R.id.rel_bottom)
    RelativeLayout rel_bottom;

    @BindView(R.id.rl_arrival)
    LinearLayout rl_arrival;

    @BindView(R.id.txt_arrival_time)
    AppCompatTextView txt_arrival_time;

    @BindView(R.id.txt_distance)
    AppCompatTextView txt_distance;

    @BindView(R.id.txt_arrival_status)
    AppCompatTextView txt_arrival_status;

    private String strToken = "";
    private Lift lift;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_ride);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        callButton = findViewById(R.id.callButton);
        smsButton = findViewById(R.id.smsButton);
        callButton.setBackgroundResource(R.drawable.telephone);
        smsButton.setBackgroundResource(R.drawable.sms);
        mainContext = this;
        HistoryStoreLoactiontoDatabaseReference = FirebaseDatabase.getInstance().getReference();
        startedcount = 0;

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_matching);
        strToken = sharedPreferences.getString(Constants.TOKEN, "");

        lift = null;
        if (getIntent() != null) {
            lift = (Lift) getIntent().getSerializableExtra(Constants.LIFT_OBJ);
        } else {
            lift = null;
        }

        if (lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))) {
            imageViewOption.setVisibility(View.VISIBLE);
        } else {
            imageViewOption.setVisibility(View.GONE);
        }

        if (!lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))) {
            txtShareCode.setVisibility(View.VISIBLE);
        } else {
            toolBarTitle.setText(R.string.start_ride);
            txtShareCode.setVisibility(View.GONE);
        }
        if (lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))) {
            getUsers(1);
            if (lift.getIs_driver_start() == 1) {
                rel_bottom.setVisibility(View.GONE);
                bywhomRidestarted = 0;
                buildLocationCallBack();
                buildLocationRequest();
                getLocationAPI();
                turnByTurnNavigation();
            } else {
                tvStartRide.setText(mainContext.getResources().getString(R.string.start_ride));
                tvStartRide.setBackgroundTintList(ColorStateList.valueOf(mainContext.getResources().getColor(R.color.colorAccent)));
            }
        } else if (!lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))) {
            getUsers(2);
            if (lift.getIs_user_start() == 1) {
                tvStartRide.setVisibility(View.VISIBLE);
                bywhomRidestarted = 1;
                tvStartRide.setText(mainContext.getResources().getString(R.string.end_ride));
                tvStartRide.setBackgroundTintList(ColorStateList.valueOf(mainContext.getResources().getColor(R.color.colorRed)));
                Log.e("Lift", "Found");
                request_id = lift.getRequest_id();
                buildLocationCallBack();
                buildLocationRequest();
                getLocationAPI();
            } else {
//                tvStartRide.setVisibility(View.GONE);
                tvStartRide.setText("Refresh");
                tvStartRide.setBackgroundTintList(ColorStateList.valueOf(mainContext.getResources().getColor(R.color.colorAccent)));
            }
        } else {
            Toast.makeText(StartRideActivity.this, "Start ride to know user's location of your lift", Toast.LENGTH_LONG).show();
        }

        sosnumbers();

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sos.isEmpty()) {
                    Toast.makeText(mainContext, "Emergency number not found", Toast.LENGTH_SHORT).show();
                } else {
                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                            "tel", sos, null));
                    startActivity(phoneIntent);
                }
            }
        });

        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sos.isEmpty()) {
                    Toast.makeText(mainContext, "Emergency number not found", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + sos));
                    intent.putExtra("sms_body", "Hi");
                    startActivity(intent);

                }
            }
        });


        imageViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUsersListDialog();
            }
        });

        Log.e("lift.getLiftType()", "" + lift.getLiftType());

        myReceiver = new MyReceiver();
        mapFragment.getMapAsync(this);

        btn_navigate.setOnClickListener(view -> {
            /*if (currentRoute != null) {
                boolean simulateRoute = false;
                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                        .directionsRoute(currentRoute)
                        .shouldSimulateRoute(simulateRoute)
                        .build();
                if (lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))) {
                    NavigationLauncher.startNavigation(this, options, txtShareCode.getText().toString());
                } else {
                    NavigationLauncher.startNavigation(this, options, null);
                }
            }*/

            /*com.mapbox.geojson.Point destinationPoint = com.mapbox.geojson.Point.fromLngLat(Double.parseDouble(lift.getEndLatlong().split(",")[1]), Double.parseDouble(lift.getEndLatlong().split(",")[0]));
        com.mapbox.geojson.Point originPoint = com.mapbox.geojson.Point.fromLngLat(Double.parseDouble(lift.getStartLatlong().split(",")[1]), Double.parseDouble(lift.getStartLatlong().split(",")[0]));
        getRoute(originPoint, destinationPoint);*/

            String stringUrl = "";
            /*if (!wayPoints.equals("")) {
                stringUrl = "google.navigation:q=" + lift.getEndLatlong() + "+" + "Raipur";//wayPoints.replace("|", ",")
            } else {
                stringUrl = "google.navigation:q=" + lift.getEndLatlong();
            }*/
//            Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&origin=18.519513,73.868315&destination=18.518496,73.879259&waypoints=18.520561,73.872435|18.519254,73.876614|18.52152,73.877327|18.52019,73.879935&travelmode=driving");

            if (!wayPoints.equals("")) {
                stringUrl = "https://www.google.com/maps/dir/?api=1&destination=" + lift.getEndLatlong() + "&waypoints=" + wayPoints + "&travelmode=driving";
            } else {
                stringUrl = "https://www.google.com/maps/dir/?api=1&destination=" + lift.getEndLatlong() + "&travelmode=driving";
            }

            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse(stringUrl));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        });

    }

    private void turnByTurnNavigation() {
        btn_navigate.setVisibility(View.VISIBLE);
        /*com.mapbox.geojson.Point destinationPoint = com.mapbox.geojson.Point.fromLngLat(Double.parseDouble(lift.getEndLatlong().split(",")[1]), Double.parseDouble(lift.getEndLatlong().split(",")[0]));
        com.mapbox.geojson.Point originPoint = com.mapbox.geojson.Point.fromLngLat(Double.parseDouble(lift.getStartLatlong().split(",")[1]), Double.parseDouble(lift.getStartLatlong().split(",")[0]));
        getRoute(originPoint, destinationPoint);*/
    }

    private void showUsersListDialog() {
        getOnGoing(sharedPreferences.getString(Constants.TOKEN, ""), true);
    }

    @SuppressLint("MissingPermission")
    public void getLocationAPI() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(100);
        locationRequest.setSmallestDisplacement(1);
    }

    private void stopLocationUpdates() {
        if (fusedLocationClient != null)
            fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void buildLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location mlocation : locationResult.getLocations()) {
                    String latitude = String.valueOf(mlocation.getLatitude());
                    String longitude = String.valueOf(mlocation.getLongitude());
                    location = mlocation;
                    if (!isAlreadyStarted) {
                        if (lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))) {
                            startDriverLift(mlocation, strToken);
                        }
                    }
                    Log.e("", "" + latitude + "," + longitude);
                }
            }
        };
    }


    private void InitLocation(String user_id, String tracking_lift_id) {
        if (Utils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        }
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("dataSnapshot", "" + dataSnapshot);
                String[] startpoint = new String[0];
                if (dataSnapshot.getValue() == null) {
                    Log.e("dataSnapshot", "dataSnapshot is null");
                    driverstarted = false;
                    if (!lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))) {
                        Toast.makeText(StartRideActivity.this, "Driver has not started the ride", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                startpoint = Objects.requireNonNull(dataSnapshot.getValue()).toString().split(",");

                placeDriver(startpoint);
                driverstarted = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                driverstarted = false;
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query lastKnownLocation = mDatabase.child("LocationMap").child("Drivers").child(user_id).child(tracking_lift_id).child("location");
        lastKnownLocation.addValueEventListener(postListener);
    }

    private void placeDriver(String[] startpoint) {
        Log.d("usersresponse1", Arrays.toString(startpoint));

        double latitude = Double.parseDouble(startpoint[0]);
        double longitude = Double.parseDouble(startpoint[1]);
        LatLng latLng = new LatLng(latitude, longitude);

        String current = latitude + "," + longitude;

        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))) {
            //join all checkpoints
            if (locationTask != null) {
                locationTask.cancel(true);
            }
            locationTask = new GetDirection().execute(current, lift.getEndLatlong());
        } else {
            /*temp*/
//            rl_arrival.setVisibility(View.GONE);
            if (bywhomRidestarted == 1) {
                txt_arrival_status.setText("Reaching in : ");
                if (locationTask != null) {
                    locationTask.cancel(true);
                }
                locationTask = new GetDirection().execute(current, lift.getEndLatlong());
                /*reaching time*/
                showArrivalTimeCard(latitude, longitude, lift.getEndLatlong());
            } else {
                txt_arrival_status.setText("Arriving in : ");
                locationTask = new GetDirection().execute(current, lift.getStartLatlong());
                /*arrival time*/
                showArrivalTimeCard(latitude, longitude, lift.getStartLatlong());
            }
        }
    }

    private void showArrivalTimeCard(double curLat, double curLong, String lastLatlong) {

        /*RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://api.mapbox.com/directions/v5/mapbox/driving-traffic/" + curLong + "," + curLat + ";" + Double.parseDouble(lastLatlong.split(",")[1]) + "," + Double.parseDouble(lastLatlong.split(",")[0]) + "?geometries=geojson&access_token=" + Mapbox.getAccessToken();
        StringRequest sr = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject base = new JSONObject(response);
                JSONArray routeArray = base.getJSONArray("routes");
                JSONObject jsonObject = routeArray.getJSONObject(0);
                double duration = jsonObject.getDouble("duration");
                double distance = jsonObject.getDouble("distance");

                Log.e("RESPONSE : ", duration + " : " + distance);
                double finalTime = duration / 60;

//                double S = duration % 60;
                double H = duration / 60;
                double M = H % 60;
                H = H / 60;

                double finalDist = distance / 1000;

                txt_arrival_time.setText(Math.round(H) + " hr " + Math.round(M) + " min");
                txt_distance.setText(Math.round(finalDist) + " km");
                rl_arrival.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                rl_arrival.setVisibility(View.GONE);
                e.printStackTrace();
            }
        }, error -> {
            rl_arrival.setVisibility(View.GONE);
        });
        queue.add(sr);*/
    }

/*
    private void getRoute(com.mapbox.geojson.Point originPoint, com.mapbox.geojson.Point destinationPoint) {
        NavigationRoute.Builder builder = NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(originPoint)
                .destination(destinationPoint)
                .profile(DirectionsCriteria.PROFILE_DRIVING);

        if (wayPoints != null && !wayPoints.isEmpty()) {
            for (String waypoint : wayPoints.split("\\|")) {
                com.mapbox.geojson.Point point = com.mapbox.geojson.Point.fromLngLat(Double.parseDouble(waypoint.split(",")[1]), Double.parseDouble(waypoint.split(",")[0]));
                builder.addWaypoint(point);
            }
        }
        builder.build().getRoute(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.body() != null && response.body().routes().size() > 0) {
                    currentRoute = response.body().routes().get(0);
                    btn_navigate.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                t.printStackTrace();
                btn_navigate.setVisibility(View.GONE);
            }
        });
    }
*/

    /**
     * Returns the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(StartRideActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        driverlocationcount = 0;
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                        if (s.equals(Utils.KEY_REQUESTING_LOCATION_UPDATES)) {
                        }
                    }
                });
        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @OnClick({R.id.imageViewBack, R.id.btn_start_ride})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                Log.d("btn_start_ride", "onBackPressed");
                onBackPressed();
                break;

            case R.id.btn_start_ride:
                try {
                    String txt = tvStartRide.getText().toString();
                    //todo start ride will call from here
                    if (lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))) {
                        if (tvStartRide.getText().toString().equalsIgnoreCase(getResources().getString(R.string.start_ride))) {

                            Constants.showLoader(StartRideActivity.this);
                            Log.d("btn_start_ride", txt);
                            ridestarted = true;
                            bywhomRidestarted = 0;
                            mService.requestLocationUpdates();
                            Log.e("Line 561", "offer_lift match");
                        } else {
                            Log.e("Lift", "end by driver");
                            getOnGoing(sharedPreferences.getString(Constants.TOKEN, ""), true);
                        }
                    } else {
                        if (tvStartRide.getText().toString().equalsIgnoreCase(getResources().getString(R.string.end_ride))) {
                            endRideCinfirmationDialog(false);
                        } else {
                            Constants.showLoader(mainContext);
                            getUsers(2);
                        }
                    }
                } catch (Exception E) {
                    Log.e("Exception E", "" + E.getMessage());
                }
                break;
        }
    }

    private void getRideEndBYDriver(String strToken, int i) {
        try {
            if (location == null) {
                Toast.makeText(StartRideActivity.this, "Location is not valid", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(StartRideActivity.this, "Location is not valid", Toast.LENGTH_LONG).show();
            return;
        }
        Log.e("lift", "id" + lift.getId());
        Constants.showLoader(StartRideActivity.this);
        ApiService api = RetroClient.getApiService();
        Call<JsonObject> call = api.ridebyDriverEnd(Constants.API_KEY, Constants.ANDROID, strToken, lift.getId(), location.getLatitude(), location.getLongitude());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Constants.hideLoader();
                Log.d("endrideresponse", new Gson().toJson(response.body()));
                Log.d("requestidd", lift.getId().toString());
                if (response.code() == 200) {
                    try {
                        JSONObject mainjson = new JSONObject(new Gson().toJson(response.body()));
                        if (mainjson.getBoolean("status")) {
                            StringBuilder wholelatlong = new StringBuilder();
                            Log.e("historylocationList", "" + historylocationList.toString());
                            for (int x = 0; x < historylocationList.size(); x++) {
                                Log.e("historylocationList", "" + historylocationList.get(x).toString());
                                wholelatlong.append("(");
                                wholelatlong.append(historylocationList.get(x).latitude);
                                wholelatlong.append(",");
                                wholelatlong.append(historylocationList.get(x).latitude);
                                wholelatlong.append(")");
                            }
                            Log.e("wholelatlong", "" + wholelatlong);
                            HistoryStoreLoactiontoDatabaseReference.child("LocationMap").child("Drivers").child(sharedPreferences.getString(Constants.USER_ID, "")).child(tracking_lift_id)
                                    .child("locationhistory").setValue(wholelatlong.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.e("History", "Saved");
                                            mService.removeLocationUpdates();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {
                                            Log.e("History", "Failed to save");
                                            e.printStackTrace();
                                        }
                                    });

                            if (mainjson.getJSONArray("user_details").length() == 0) {
                                Toast.makeText(StartRideActivity.this, "Ride ended successfully,but no user found", Toast.LENGTH_SHORT).show();
                                sharedPreferences
                                        .edit()
                                        .putBoolean(Constants.IS_RIDE_ENDED, true)
                                        .apply();
                                finish();
                            } else {
                                for (int i = 0; i < mainjson.getJSONArray("user_details").length(); i++) {
                                    JSONObject user = mainjson.getJSONArray("user_details").getJSONObject(i);
                                    String user_id = "";
                                    try {
                                        user_id = user.getString("id");
                                        String username = user.getString("name");
                                        Rateusers(user_id, 1, username);
                                    } catch (JSONException e) {
                                        Rateusers(user_id, 1, "user");
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } else {
                            Toast.makeText(StartRideActivity.this, "Failed to end ride", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(StartRideActivity.this, "Failed to end ride,server error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.optString("message");
                        Toast.makeText(StartRideActivity.this, message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(StartRideActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++Driver started+++++++++++++++++++++++++++++
    private void startDriverLift(Location location, String strToken) {
        Log.d("liftiddd", String.valueOf(lift.getId()));
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "https://charpair.com/api/driver-liftstart", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Constants.hideLoader();
                    JSONObject base = new JSONObject(response);
                    Log.e("base", "is" + base);
                    if (base.getBoolean("status")) {
                        rel_bottom.setVisibility(View.VISIBLE);
                        isAlreadyStarted = true;
                        tvStartRide.setText(mainContext.getResources().getString(R.string.end_ride));
                        tvStartRide.setBackgroundTintList(ColorStateList.valueOf(mainContext.getResources().getColor(R.color.colorRed)));
                        Toast.makeText(StartRideActivity.this, "Please wait we are getting live location", Toast.LENGTH_LONG).show();
                        Log.d("usersresponse liftstart", response);
                        ridestarted = false;
                        bywhomRidestarted = 0;
                        mService.requestLocationUpdates();
                        if (lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))) {
                            turnByTurnNavigation();
                        }
                        getUsers(1);
                    } else {
                        Toast.makeText(StartRideActivity.this, "Drive already started", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    mService.removeLocationUpdates();
                    Toast.makeText(StartRideActivity.this, "Cannot Start your drive as ride has been removed", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, error -> {
            Constants.hideLoader();
            mService.removeLocationUpdates();
            Toast.makeText(StartRideActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", Constants.API_KEY);
                params.put("client", Constants.ANDROID);
                params.put("token", strToken);
                params.put("lat", String.valueOf(location.getLatitude()));
                params.put("long", String.valueOf(location.getLongitude()));
                params.put("lift_id", String.valueOf(lift.getId()));
                return params;
            }
        };
        queue.add(sr);
    }

    private void showDialogEnterCode(Integer lift_id) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Enter Code");
        alertDialogBuilder.setCancelable(false);
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_enter_otp,
                null);
        alertDialogBuilder.setView(customLayout);
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
        AppCompatEditText editText = customLayout.findViewById(R.id.editText);
        AppCompatTextView tvOk = customLayout.findViewById(R.id.btn_ok);
        AppCompatTextView tvCancel = customLayout.findViewById(R.id.btn_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = editText.getText().toString();
                if (!otp.equals("")) {
                    getLiftStartCodeMatch(strToken, otp, lift_id);
                    alert.dismiss();
                } else {
                    Toast.makeText(mainContext, "Please enter code.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(StartRideActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(StartRideActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        setSrcDestPath();
    }

    private void setSrcDestPath() {
        if (lift != null) {
            if (lift.getStartLatlong() != null && lift.getEndLatlong() != null) {
                LatLng originLatLng = new LatLng(Double.parseDouble(lift.getStartLatlong().split(",")[0]), Double.parseDouble(lift.getStartLatlong().split(",")[1]));
                LatLng destLatLng = new LatLng(Double.parseDouble(lift.getEndLatlong().split(",")[0]), Double.parseDouble(lift.getEndLatlong().split(",")[1]));

                mGoogleMap.addMarker(new MarkerOptions()
                        .position(originLatLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pic_location))
                        .title("pickup"));

                mGoogleMap.addMarker(new MarkerOptions()
                        .position(destLatLng)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.drop_location))
                        .title("dropoff"));
                String origin = lift.getStartLatlong();
                String destination = lift.getEndLatlong();

                if (locationTask != null) {
                    locationTask.cancel(true);
                }
                locationTask = new GetDirection().execute(origin, destination);
            }
        }
    }

    class GetDirection extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            String stringUrl;
            if (!wayPoints.equals("")) {
                stringUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=" + args[0] + "&destination=" + args[1] + "&waypoints=" + wayPoints + "&key=" + getResources().getString(R.string.maps_api_key) + "&sensor=false";
            } else {
                stringUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=" + args[0] + "&destination=" + args[1] + "&key=" + getResources().getString(R.string.maps_api_key) + "&sensor=false";
            }
            Log.e("URL : ", "" + stringUrl);
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpconn = (HttpURLConnection) url
                        .openConnection();
                if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader input = new BufferedReader(
                            new InputStreamReader(httpconn.getInputStream()),
                            8192);
                    String strLine;

                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();
                }

                String jsonOutput = response.toString();

                JSONObject jsonObject = new JSONObject(jsonOutput);

                // routesArray contains ALL routes
                JSONArray routesArray = jsonObject.getJSONArray("routes");
                // Grab the first route
                JSONObject route = routesArray.getJSONObject(0);

                JSONObject poly = route.getJSONObject("overview_polyline");
                String polyline = poly.getString("points");
                pontos = decodePoly(polyline);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            LatLng src1 = null;
            LatLng dest = null;
            if (polyline != null) {
                polyline.remove();
            }
            for (Polyline polyline : polylineList) {
                polyline.remove();
            }
            polylineList.clear();

            for (int i = 0; i < pontos.size() - 1; i++) {
                Log.e("call poly ", "loop = " + i);
                LatLng src = pontos.get(i);
                if (i == 0) {
                    src1 = src;
                }
                dest = pontos.get(i + 1);
                try {
                    if (!isTrackingPath) {
                        polyline = mGoogleMap.addPolyline(new PolylineOptions()
                                .add(new LatLng(src.latitude, src.longitude),
                                        new LatLng(dest.latitude, dest.longitude))
                                .width(7).color(Color.BLUE).geodesic(true));
                        polylineList.add(polyline);
                    } else {
                        if (lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))) {
                            polyline = mGoogleMap.addPolyline(new PolylineOptions()
                                    .add(new LatLng(src.latitude, src.longitude),
                                            new LatLng(dest.latitude, dest.longitude))
                                    .width(7).color(Color.GREEN).geodesic(true));

                            polylineList.add(polyline);
                        }
                    }

                } catch (NullPointerException e) {
                    Log.e("Error", "NullPointerException onPostExecute: " + e);
                } catch (Exception e2) {
                    Log.e("Error", "Exception onPostExecute: " + e2);
                }
            }

            try {
                if (!isTrackingPath) {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(src1);
                    builder.include(dest);
                    LatLngBounds bounds = builder.build();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
                    mGoogleMap.moveCamera(cu);
                } else {
                    if (mDriverMarker != null) {
                        mDriverMarker.remove();
                    }
                    mDriverMarker = mGoogleMap.addMarker(new MarkerOptions()
                            .position(pontos.get(0))
                            .draggable(true)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps_navigation))
                            .title("Driver"));
                    LatLngBounds bounds = mGoogleMap.getProjection().getVisibleRegion().latLngBounds;
                    try {
                        if (!oneTimeZoomed || !bounds.contains(Objects.requireNonNull(src1))) {
                            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(src1, 25.0f));
                            oneTimeZoomed = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            isTrackingPath = true;
        }
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }


    public void getOnGoing(String token, boolean showDialog) {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<MainOnGoingResponse> call = api.rideOnGoing(Constants.API_KEY, "android", token);
        call.enqueue(new Callback<MainOnGoingResponse>() {
            @Override
            public void onResponse(Call<MainOnGoingResponse> call, Response<MainOnGoingResponse> response) {
                Constants.hideLoader();
                if (response.body().getResponse() != null) {
                    Log.e("RES: ", "" + response.body().getResponse());
                    onGoingListResponse = response.body().getResponse();
                    liftUsersList.clear();
                    if (onGoingListResponse.getLifts().size() > 0) {
                        liftUsersList.addAll(onGoingListResponse.getLifts().get(0).getLift_users());
                    }
                    if (showDialog) {
                        showEndUserListDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<MainOnGoingResponse> call, Throwable throwable) {
                Toast.makeText(mainContext, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getLiftStartCodeMatch(String token, String code, Integer lift_id) {
        Constants.showLoader(StartRideActivity.this);
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }

            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        }).addOnSuccessListener(location -> {
            if (location != null) {
                JSONObject jsonObject = getJsonObjectFromLocation(location.getLatitude(), location.getLongitude());
                if (jsonObject != null) {
                    try {
                        ApiService api = RetroClient.getApiService();
                        Call<JsonObject> call = api.liftStartCodeMatch(Constants.API_KEY,
                                Constants.ANDROID, token,
                                lift_id,
                                Integer.parseInt(code),
                                jsonObject.getString("lat_long"),
                                jsonObject.getString("city"),
                                jsonObject.getString("address"));
                        call.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                Constants.hideLoader();
                                if (response.code() == 200) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.body().toString());
                                        boolean status = jsonObject.optBoolean("status");
                                        String msg = jsonObject.optString("message");
                                        Log.d("StartRideActivity", "msg" + msg);
                                        Toast.makeText(StartRideActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        if (status) {
                                            request_id = Integer.parseInt(jsonObject.getJSONObject("data").getString("request_id"));
//                                            tvStartRide.setText(getResources().getString(R.string.end_ride));
//                                            tvStartRide.setBackgroundTintList(ColorStateList.valueOf(mainContext.getResources().getColor(R.color.colorRed)));
//                                            bywhomRidestarted = 1;
//                                            getUsers(2);
//                                            mService.requestLocationUpdates();
                                            /*if (lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))) {
                                                turnByTurnNavigation();
                                            }*/
                                        } else {
                                            Toast.makeText(StartRideActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                        String message = jsonObject.optString("message");
                                        Toast.makeText(StartRideActivity.this, message, Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                                Constants.hideLoader();
                                Toast.makeText(StartRideActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });

    }

    public void getRideEnd(String token, boolean isDriverEnd) {
        Constants.showLoader(StartRideActivity.this);
        if (ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }

            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        }).addOnSuccessListener(location -> {
            if (location != null) {
                Log.e("Request", "id" + request_id);
                Constants.showLoader(StartRideActivity.this);

                JSONObject jsonObject = getJsonObjectFromLocation(location.getLatitude(), location.getLongitude());

                if (jsonObject != null) {
                    try {
                        ApiService api = RetroClient.getApiService();
                        Call<JsonObject> call = api.rideEnd(Constants.API_KEY,
                                Constants.ANDROID,
                                token, request_id,
                                jsonObject.getString("lat_long"),
                                jsonObject.getString("city"),
                                jsonObject.getString("address"));
                        call.enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                Constants.hideLoader();
                                Log.d("endrideresponse", new Gson().toJson(response.body()));
                                Log.d("requestidd", lift.getId().toString());
                                if (response.code() == 200) {
                                    try {
                                        Toast.makeText(StartRideActivity.this, "Ride ended successfully", Toast.LENGTH_SHORT).show();
                                        if (isDriverEnd) {
                                            getOnGoing(sharedPreferences.getString(Constants.TOKEN, ""), false);
                                        } else {
                                            getInvoice();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                        String message = jsonObject.optString("message");
                                        Toast.makeText(StartRideActivity.this, message, Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }


                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                                Constants.hideLoader();
                                Toast.makeText(StartRideActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }


    private JSONObject getJsonObjectFromLocation(double LATITUDE, double LONGITUDE) {
        JSONObject jsonObject = null;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                try {
                    jsonObject = new JSONObject();
                    jsonObject.put("lat_long", LATITUDE + "," + LONGITUDE);
                    jsonObject.put("city", returnedAddress.getLocality());
                    jsonObject.put("address", returnedAddress.getAddressLine(0));

                    return jsonObject;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            } else {
                Log.e("My Current", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("My Current", "Can't get Address!");
        }
        return jsonObject;
    }


    //+++++++++++++++++++++++++++++++++++++++++++Driver only rate user+++++++++++++++++++++++
    private void Rateusers(String user_id, int type, String username) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        if (type == 1) {
            alertDialogBuilder.setTitle("Rate " + username);
        } else {
            alertDialogBuilder.setTitle("Rate Driver");
        }
        alertDialogBuilder.setCancelable(false);
        final View customLayout = getLayoutInflater().inflate(R.layout.dialograteuser,
                null);
        RatingBar driver_rating = customLayout.findViewById(R.id.driver_rating);
        androidx.appcompat.widget.AppCompatEditText feedbacktext = customLayout.findViewById(R.id.feedbacktext);
        androidx.appcompat.widget.AppCompatTextView btn_ok = customLayout.findViewById(R.id.btn_ok);
        androidx.appcompat.widget.AppCompatTextView btn_cancel = customLayout.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalAlert.dismiss();
                onBackPressed();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (feedbacktext.getText().toString().isEmpty() || driver_rating.getRating() == 0) {
                    Toast.makeText(StartRideActivity.this, "Please specify some feedback", Toast.LENGTH_SHORT).show();
                } else {
                    rateUser();
                }
            }

            private void rateUser() {
                Log.d("liftiddd", String.valueOf(lift.getId()));
                RequestQueue queue = Volley.newRequestQueue(StartRideActivity.this);
                StringRequest sr = new StringRequest(Request.Method.POST, "https://charpair.com/api/set-reviews", new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        finalAlert.dismiss();
                        JSONObject jObject = null;
                        try {
                            jObject = new JSONObject(response);
                            Boolean res = jObject.getBoolean("status");
                            if (res) {
                                Toast.makeText(StartRideActivity.this, "Review send successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("usersresponse", response);
                    }
                }, error -> {
                    Log.d("usersresponse", "Error with partner ");
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("api_key", Constants.API_KEY);
                        params.put("client", Constants.ANDROID);
                        params.put("token", strToken);
                        params.put("to_user", user_id);
                        params.put("rating", String.valueOf(driver_rating.getRating()));
                        params.put("feedback", Objects.requireNonNull(feedbacktext.getText()).toString());

                        return params;
                    }
                };
                queue.add(sr);
            }
        });
        alertDialogBuilder.setView(customLayout);
        finalAlert = alertDialogBuilder.create();
        finalAlert.show();
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    public void getUsers(int type) {
        Log.d("liftiddd", String.valueOf(lift.getId()));
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "https://charpair.com/api/partner-latlong", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("usersresponse", response);
                Constants.hideLoader();
                try {
                    JSONObject jObject = new JSONObject(response);
                    JSONObject data = jObject.getJSONObject("data");
                    int is_ride_start = data.getInt("is_ride_start");
                    request_id = data.getInt("request_id");
                    if (is_ride_start == 1) {
                        lift.setIs_user_start(1);
                        bywhomRidestarted = 1;
                        tvStartRide.setText(mainContext.getResources().getString(R.string.end_ride));
                        tvStartRide.setBackgroundTintList(ColorStateList.valueOf(mainContext.getResources().getColor(R.color.colorRed)));
                        lift.setRequest_id(request_id);
                    }

                    users = data.getJSONArray("user");
                    tracking_lift_id = data.getString("tracking_lift_id");
                    StringBuilder builder = new StringBuilder();
                    if (type == 1) {
                        for (int i = 0; i < users.length(); i++) {
                            JSONObject user = users.getJSONObject(i);
                            try {
                                if (i == 0) {
                                    builder.append(user.getString("start_points"));
                                } else {
                                    builder.append("|").append(user.getString("start_points"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        wayPoints = builder.toString();
//                        wayPoints = "22.7412138,75.9002981";
                    }
                    try {
                        JSONObject driver = data.getJSONObject("driver");
                        driver_id = driver.getString("user_id");
                        InitLocation(driver_id, tracking_lift_id);
                    } catch (Exception e) {
                        Toast.makeText(StartRideActivity.this, "Driver not found or driver has not started the ride", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    //}
                } catch (JSONException e) {
                    Toast.makeText(StartRideActivity.this, "No valid ride information found", Toast.LENGTH_SHORT).show();
                    mService.removeLocationUpdates();
                    e.printStackTrace();
                }
            }
        }, error -> {
            mService.removeLocationUpdates();
            Log.d("usersresponse", "Error with partner ");
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", Constants.API_KEY);
                params.put("client", Constants.ANDROID);
                params.put("token", strToken);
                params.put("lift_id", String.valueOf(lift.getId()));
                return params;
            }
        };
        queue.add(sr);
    }


    private void placeUser(String[] startpoint) {
        Log.d("usersresponse1", String.valueOf(startpoint));
        double latitude = Double.parseDouble(startpoint[0]);
        double longitude = Double.parseDouble(startpoint[1]);
        LatLng latLng = new LatLng(latitude, longitude);
        mGoogleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker())
                .title("User"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }


    private void showEndUserListDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(StartRideActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        dialogBuilder.setCancelable(true);
        View dialogView = inflater.inflate(R.layout.end_ride_layout, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        TextView titleTxt = dialogView.findViewById(R.id.titleTxt);
        RecyclerView listview = dialogView.findViewById(R.id.userList);
        Button endAllRideBtn = dialogView.findViewById(R.id.endAllRideBtn);

        listview.setLayoutManager(new LinearLayoutManager(this));
        LiftPartnerAdapter adapter = new LiftPartnerAdapter(mainContext, liftUsersList, new LiftPartnerAdapter.OnButtonClick() {
            @Override
            public void onEndClick(LiftUsers user) {
                alertDialog.dismiss();
                request_id = user.getRequest_id();
                endRideCinfirmationDialog(false);
            }

            @Override
            public void onJoinClick(LiftUsers user) {
                alertDialog.dismiss();
                request_id = user.getRequest_id();
                showDialogEnterCode(user.getUser_lift_id());
            }
        });
        listview.setAdapter(adapter);

        endAllRideBtn.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    endRideCinfirmationDialog(true);
                }
        );
        alertDialog.show();
    }

    private void endRideCinfirmationDialog(boolean isAllUsers) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(StartRideActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        dialogBuilder.setCancelable(true);
        View dialogView = inflater.inflate(R.layout.end_ride_confirmation_layout, null);
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();
        AppCompatTextView title = dialogView.findViewById(R.id.title);
        Button endRideBtnConfirmation = dialogView.findViewById(R.id.endRideBtnConfirmation);
        Button cancelBtnConfirmation = dialogView.findViewById(R.id.cancelBtnConfirmation);
        if (isAllUsers) {
            title.setText(R.string.do_you_want_to_end_all_rides);
            endRideBtnConfirmation.setText(R.string.end_all_rides);
        }
        endRideBtnConfirmation.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    if (isAllUsers) {
                        getRideEndBYDriver(strToken, 1);
                    } else {
                        getRideEnd(strToken, lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift)));
                    }
                }
        );

        cancelBtnConfirmation.setOnClickListener(v -> {
                    alertDialog.dismiss();
                }
        );
        alertDialog.show();
    }


    private void getInvoice() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "https://charpair.com/api/get-invoice", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("invoice response", response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    JSONObject data = jObject.getJSONObject("data");
                    Log.d("data", data.toString());
                    JSONObject st = data.getJSONObject("start_point");
                    Log.d("st", st.toString());
                    String stlocation = st.getString("location");
                    Log.d("stloc", stlocation);
                    JSONObject end = data.getJSONObject("end_point");
                    Log.d("end", end.toString());
                    String etlocation = end.getString("location");
                    Log.d("etlocatio", etlocation);
                    String date = data.getString("date");
                    Log.d("date", date);
                    String distance = data.getString("distance");
                    Log.d("distance response", distance);
                    Integer perkm = data.getInt("per_km_point");
                    Log.d("perkm response", perkm.toString());
                    totalpoint = data.getString("total_point");
                    Log.d("totalpoint", totalpoint);

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(StartRideActivity.this);
                    // ...Irrelevant code for customizing the buttons and title
                    LayoutInflater inflater = getLayoutInflater();
                    dialogBuilder.setTitle("Invoice");
                    dialogBuilder.setCancelable(true);
                    View dialogView = inflater.inflate(R.layout.invoicelayout, null);
                    dialogBuilder.setView(dialogView);
                    TextView pickuplocation = dialogView.findViewById(R.id.pickuplocationn);
                    TextView droplocation = dialogView.findViewById(R.id.droplocationn);
                    TextView datee = dialogView.findViewById(R.id.datee);
                    TextView distancee = dialogView.findViewById(R.id.distance);
                    TextView kmpoint = dialogView.findViewById(R.id.kmpoint);
                    totalpointt = dialogView.findViewById(R.id.totalpoint);
                    Button paybtn = dialogView.findViewById(R.id.pay);
                    Button okayBtn = dialogView.findViewById(R.id.okayBtn);

                    pickuplocation.setText("Pickup Location : " + stlocation);
                    droplocation.setText("Drop Location : " + etlocation);
                    datee.setText("Date : " + date);
                    distancee.setText("Distance : " + distance);
                    kmpoint.setText("Per KM Point : " + perkm);
                    totalpointt.setText("Total Point : " + totalpoint);
                    AlertDialog alertDialog = dialogBuilder.create();
                    paybtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            pay();
                        }
                    });

                    okayBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });

                    alertDialog.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", Constants.API_KEY);
                params.put("client", Constants.ANDROID);
                params.put("token", strToken);
                params.put("request_id", String.valueOf(request_id));
                return params;
            }
        };
        queue.add(sr);
    }

    private void getPayedTODriver() {
        //+++++++++++++++++++++++++++++++Then rate+++++++++++++++++++++++++++++++++++++++
        Rateusers(driver_id, 2, "");
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++
    }

    private void pay() {
        Constants.showLoader(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "https://charpair.com/api/lift-payment", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Constants.hideLoader();
                Log.d("payapi", response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    boolean scss = jObject.getBoolean("status");
                    String msg = jObject.getString("message");
                    Toast.makeText(StartRideActivity.this, msg, Toast.LENGTH_SHORT).show();

                    if (scss) {
                        getPayedTODriver();
                    } else {
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", Constants.API_KEY);
                params.put("client", Constants.ANDROID);
                params.put("token", strToken);
                params.put("request_id", String.valueOf(request_id));
                params.put("amount", totalpoint);
                return params;
            }
        };
        queue.add(sr);
    }

    private void paymentintegration() {
        int amount = Math.round(Float.parseFloat(totalpoint) * 100);
        Checkout checkout = new Checkout();
        checkout.setKeyID("Enter your key id here");
        JSONObject object = new JSONObject();
        try {
            object.put("name", "Test Payment");
            object.put("description", "Test Description");
            object.put("theme.color", "");
            object.put("currency", "INR");
            object.put("amount", amount);
            object.put("prefill.contact", "0000000000");
            object.put("prefill.email", "test@test.com");
            checkout.open(StartRideActivity.this, object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sosnumbers() {
        Constants.showLoader(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "https://charpair.com/api/get-profile", new com.android.volley.Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                Constants.hideLoader();
                Log.e("history jjj", response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    JSONObject responsee = jObject.getJSONObject("response");
                    JSONObject userdata = responsee.getJSONObject("user");
                    sos = userdata.getString("sos");
                    txtShareCode.setText("Share code : " + userdata.getString("share_code"));

                    Log.d("sos", sos);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constants.hideLoader();
                Log.e("rise vo", "" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", Constants.API_KEY);
                params.put("client", Constants.ANDROID);
                params.put("token", sharedPreferences.getString(Constants.TOKEN, ""));
                return params;
            }
        };
        queue.add(sr);

    }

    @Override
    public void onPaymentSuccess(String s) {
        // this method is called on payment success.
        Toast.makeText(this, "Payment is successful : " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        // on payment failed.
        Toast.makeText(this, "Payment Failed due to error : " + s, Toast.LENGTH_SHORT).show();
        Log.d("payment error", s);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (startedcount == 3) {
            mService.requestLocationUpdates();
        } else {
            try {
                LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                        new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
            } catch (Exception e) {
                Log.e("it is", "the user no location needed");
                e.printStackTrace();
            }
            try {
                registerReceiver(receiver, new IntentFilter("FBR-IMAGE"));
            } catch (Exception e) {
                Log.e("it is", "the user no location needed");
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onStop() {
        ridestarted = false;
        startedcount = 0;
        stopLocationUpdates();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
       /* if (lift != null && lift.getIs_driver_start() == 1) {
            enterPipMode();
        } else {*/
        super.onBackPressed();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
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

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String title = intent.getStringExtra("title");
                String details = intent.getStringExtra("details");
                JSONObject jsonObject = new JSONObject(details);
                showNotificationDialog(title, jsonObject.getString("request_id"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void showNotificationDialog(String title, String request_id) {
        try {
            if (dialog == null) {
                dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.user_ride_end_dialog);
                Window window = dialog.getWindow();
                WindowManager.LayoutParams wlp = window.getAttributes();

                wlp.gravity = Gravity.TOP | Gravity.START;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                wlp.y = 120;
                window.setAttributes(wlp);


                AppCompatTextView tv = dialog.findViewById(R.id.tv);
                AppCompatTextView btnOk = dialog.findViewById(R.id.buttonOk);
                AppCompatTextView btnCancel = dialog.findViewById(R.id.buttonCancel);

                tv.setText(title);

                btnOk.setOnClickListener(v -> {
                    dialog.cancel();
                    rideEndAccept(request_id);
                });
                btnCancel.setOnClickListener(v -> {
                    dialog.cancel();
                });

                dialog.setOnCancelListener(dialogInterface -> {
                    dialog = null;
                });
                dialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void rideEndAccept(String request_id) {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<JsonObject> call = api.rideEndRequestAccept(Constants.API_KEY, Constants.ANDROID, strToken, Integer.parseInt(request_id));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Constants.hideLoader();
                if (response.code() == 200) {
                    try {
                        Toast.makeText(StartRideActivity.this, "Request accepted.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.optString("message");
                        Toast.makeText(StartRideActivity.this, message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(StartRideActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
