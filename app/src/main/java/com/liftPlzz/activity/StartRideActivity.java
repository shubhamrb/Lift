package com.liftPlzz.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.HashMap;
import java.util.List;
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
    private List<LiftUsers> liftUsersList = new ArrayList<>();


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
                        startDriverLift(location, strToken);
                        return;
                    } else {
                        if (location != null) {
//                Log.e(TAG , "Location Received"+Utils.getLocationText(location));
                            // ...
                            if (Previouslocation == null) {
                                historylocationList.add(new LatLng(location.getLatitude(), location.getLongitude()));
                            } else if (location.getLatitude() == Previouslocation.getLatitude() || (location.getLongitude() == Previouslocation.getLongitude())) {
                                Log.e(TAG, "Location is Same");
                            } else {
                                historylocationList.add(new LatLng(location.getLatitude(), location.getLongitude()));
                            }
                            Previouslocation = location;
                            String livelocation = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
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
//                                Location retrivedloc = new Location(Latbroken[0] , Latbroken[1]) ;
                                            placeTheCUrrentmarker(targetLocation);
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
//                placeTheCUrrentmarker(location);
//                mDatabase = FirebaseDatabase.getInstance().getReference();
//                mDatabase.child("LocationMap").child("Drivers").child("1").setValue(name);
//                Toast.makeText(DriverUserLocationActivity.this, Utils.getLocationText(location),
//                        Toast.LENGTH_SHORT).show();
                    }
                } else if (bywhomRidestarted == 1) {
                    Log.e("Driver started is", "User" + bywhomRidestarted);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void placeTheCUrrentmarker(Location location) {
        LatLng latLngOrigin = new LatLng(location.getLatitude(), location.getLongitude());
        mGoogleMap.clear();
        mGoogleMap.addMarker(new MarkerOptions()
                .position(latLngOrigin)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pic_location))
                .title("Driver"));
        /*AnimationUtil.animateMarkerTo(
                mDriverMarker,
                latLngOrigin);*/
        if (!apimazoomcompleted) {
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 15.0f));
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
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.imageViewOption)
    ImageView imageViewOption;
    @BindView(R.id.btn_start_ride)
    AppCompatTextView tvStartRide;

    @BindView(R.id.rel_bottom)
    RelativeLayout rel_bottom;

    private String strToken = "";
    private Lift lift;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_ride);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        callButton = findViewById(R.id.callButton);
        smsButton = findViewById(R.id.smsButton);
        callButton.setBackgroundResource(R.drawable.telephone);
        smsButton.setBackgroundResource(R.drawable.sms);
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

        mainContext = this;
        if (getIntent() != null) {
            lift = (Lift) getIntent().getSerializableExtra(Constants.LIFT_OBJ);
        }

        if (lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))) {
            imageViewOption.setVisibility(View.VISIBLE);
        } else {
            imageViewOption.setVisibility(View.GONE);
        }

        imageViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUsersListDialog();
            }
        });

//      getUsers(2);

        /*if (tvStartRide.getText().toString().equalsIgnoreCase(getResources().getString(R.string.start_ride))) {

        }*/

        Log.e("lift.getLiftType()", "" + lift.getLiftType());
        HistoryStoreLoactiontoDatabaseReference = FirebaseDatabase.getInstance().getReference();
        startedcount = 0;
        if (lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))) {
            txtShareCode.setVisibility(View.VISIBLE);
        } else {
            toolBarTitle.setText(R.string.start_ride);
            txtShareCode.setVisibility(View.GONE);
        }
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_matching);
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        myReceiver = new MyReceiver();
        mapFragment.getMapAsync(this);


        if (lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))) {
            if (lift.getIs_driver_start() == 1) {
                rel_bottom.setVisibility(View.GONE);
                bywhomRidestarted = 0;
                buildLocationCallBack();
                buildLocationRequest();
                getLocationAPI();
            } else {
                tvStartRide.setText(mainContext.getResources().getString(R.string.start_ride));
            }
        } else if (!lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))) {
            getUsers(2);
            if (lift.getIs_user_start() == 1) {
                tvStartRide.setText(mainContext.getResources().getString(R.string.end_ride));
                Log.e("Lift", "Found");
//                getUsers(2);
                request_id = lift.getRequest_id();
                buildLocationCallBack();
                buildLocationRequest();
                getLocationAPI();
            } else {
                tvStartRide.setText(mainContext.getResources().getString(R.string.start_ride));
            }
        } else {
            Toast.makeText(StartRideActivity.this, "Start ride to know user's location of your lift", Toast.LENGTH_LONG).show();
        }
    }

    private void showUsersListDialog() {
        getOnGoing(sharedPreferences.getString(Constants.TOKEN, ""), true);

//        showEndUserListDialog();
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
        // Check that the user hasn't revoked permissions by going to Settings.
        if (Utils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        }

//        tvStartRide.setText(mainContext.getString(R.string.end_ride));

        //++++++++++++++++++++++++++++++++Database+++++++++++++++++++++++++++++++++++++++
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("dataSnapshot", "" + dataSnapshot);
                String[] startpoint = new String[0];
                if (dataSnapshot.getValue() == null) {
                    Log.e("dataSnapshot", "dataSnapshot is null");
                    driverstarted = false;
                    Toast.makeText(StartRideActivity.this, "Driver has not started the ride", Toast.LENGTH_SHORT).show();
                    return;
                }
                startpoint = Objects.requireNonNull(dataSnapshot.getValue()).toString().split(",");
                if (driverlocationcount == 0) {
                    placeDriver(startpoint, 0);
                    driverlocationcount = 1;
                } else {
                    placeDriver(startpoint, 1);
                }
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

    private void placeDriver(String[] startpoint, int driverlocationcountx) {
        Log.d("usersresponse1", String.valueOf(startpoint));
        double latitude = Double.parseDouble(startpoint[0]);
        double longitude = Double.parseDouble(startpoint[1]);
        LatLng latLng = new LatLng(latitude, longitude);
        if (driverlocationcountx == 0) {
            prelatLng = new LatLng(latitude, longitude);
            Log.d("usersresponse1", "" + driverlocationcount);
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(prelatLng)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker())
                    .title("Driver Start here"));
            linelocationList.add(prelatLng);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        } else {
            driverlocationcount = 1;
            linelocationList.add(latLng);
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(Color.RED);
            polyOptions.width(5);
            polyOptions.addAll(linelocationList);
            mGoogleMap.clear();
            mGoogleMap.addPolyline(polyOptions);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng latLngx : linelocationList) {
                builder.include(latLngx);
            }
            final LatLngBounds bounds = builder.build();
            //BOUND_PADDING is an int to specify padding of bound.. try 100.
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
            mGoogleMap.animateCamera(cu);

            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(prelatLng)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker())
                    .title("Start"));

            mDriverMarker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker())
                    .title("Driver"));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        }

//

    }

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
               /*Intent intent = new Intent(
                        this,
                       LiveLocation.class);
                intent.setAction("start_service");
                ContextCompat.startForegroundService(this, intent);*/

                try {
                    String txt = tvStartRide.getText().toString();
                    if (tvStartRide.getText().toString().equalsIgnoreCase(getResources().getString(R.string.start_ride))) {
                        //todo start ride will call from here
                        if (lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))) {
                            Log.d("btn_start_ride", txt);
                            ridestarted = true;
                            bywhomRidestarted = 0;
                            mService.requestLocationUpdates();
                            Log.e("Line 561", "offer_lift match");
                        } else {
                            if (!driverstarted) {
                                Toast.makeText(StartRideActivity.this, "Let driver start the ride first", Toast.LENGTH_SHORT).show();
                            } else {
                                showDialogEnterCode();
                            }
                        }
                    } else {
                        if (lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))) {
                            Log.e("Lift", "end by driver");
                            getOnGoing(sharedPreferences.getString(Constants.TOKEN, ""), true);

//                            showEndUserListDialog();
                        } else {
                            endRideCinfirmationDialog(false);
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
                            Log.e("historylocationList", "" + historylocationList.get(0).toString());
                            for (int x = 0; x < historylocationList.size(); x++) {
                                Log.e("historylocationList", "" + historylocationList.get(x).toString());
                                wholelatlong.append("(");
                                wholelatlong.append(historylocationList.get(x).latitude);
                                wholelatlong.append(",");
                                wholelatlong.append(historylocationList.get(x).latitude);
                                wholelatlong.append(")");
                            }
                            Log.e("wholelatlong", "" + wholelatlong.toString());
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
                    JSONObject base = new JSONObject(response);
                    Log.e("base", "is" + base);
                    if (base.getBoolean("status")) {
                        rel_bottom.setVisibility(View.VISIBLE);
                        isAlreadyStarted = true;
                        tvStartRide.setText(mainContext.getResources().getString(R.string.end_ride));
                        Toast.makeText(StartRideActivity.this, "Please wait we are getting live location", Toast.LENGTH_LONG).show();
                        Log.d("usersresponse liftstart", response);
                        ridestarted = false;
                        bywhomRidestarted = 0;
                        mService.requestLocationUpdates();
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

    private void showDialogEnterCode() {
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
                    getLiftStartCodeMatch(strToken, otp);
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more detai ls.
            return;
        }
//        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
//        LatLng latLngOrigin = new LatLng(22.7244, 75.8839);
//        origin = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
//        pickupLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

//        googleMap.addMarker(new MarkerOptions()
//                .position(latLngOrigin)
//                .draggable(true)
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pic_location))
//                .title("First"));

        // [START_EXCLUDE silent]
//        editTextPickupLocation.setText(getCompleteAddressString(currentLocation.getLatitude(), currentLocation.getLongitude()));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 15.0f));
//        startPoint = getJsonObjectFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude());

        // Zoom in the Google Map
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//        googleMap.moveCamera(new CameraUpdateFactory().newLatLngZoom(la));
//        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    class GetDirection extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {
//            String stringUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=" + origin + "&destination=" + destination + "&key=" + getResources().getString(R.string.maps_api_key) + "&sensor=false";
            String stringUrl = "";
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection httpconn = (HttpURLConnection) url
                        .openConnection();
                if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader input = new BufferedReader(
                            new InputStreamReader(httpconn.getInputStream()),
                            8192);
                    String strLine = null;

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
//                pontos = decodePoly(polyline);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {

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


    public void getLiftStartCodeMatch(String token, String code) {
        Constants.showLoader(StartRideActivity.this);
        ApiService api = RetroClient.getApiService();
        Call<JsonObject> call = api.liftStartCodeMatch(Constants.API_KEY, Constants.ANDROID, token, lift.getId(), Integer.parseInt(code), 0.0, 0.0);
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
                            tvStartRide.setText(getResources().getString(R.string.end_ride));
                            tvStartRide.setBackground(getResources().getDrawable(R.drawable.rounded_bg_dark));
                            bywhomRidestarted = 1;
                            mService.requestLocationUpdates();
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
    }

    public void getRideEnd(String token, boolean isDriverEnd) {
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
        Log.e("Request", "id" + request_id);
        Constants.showLoader(StartRideActivity.this);
        ApiService api = RetroClient.getApiService();
        Call<JsonObject> call = api.rideEnd(Constants.API_KEY, Constants.ANDROID, token, request_id, location.getLatitude(), location.getLongitude());
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
                try {
                    JSONObject jObject = new JSONObject(response);
                    JSONObject data = jObject.getJSONObject("data");
                    users = data.getJSONArray("user");
                    tracking_lift_id = data.getString("tracking_lift_id");
                    if (type == 1) {
                        for (int i = 0; i < users.length(); i++) {
                            JSONObject user = users.getJSONObject(i);
                            String[] startpoint = new String[0];
                            try {
                                startpoint = user.getString("start_points").split(",");
                                placeUser(startpoint);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        try {
                            JSONObject driver = data.getJSONObject("driver");
                            driver_id = driver.getString("user_id");
                            InitLocation(driver_id, tracking_lift_id);
                        } catch (Exception e) {
                            Toast.makeText(StartRideActivity.this, "Driver not found or driver has not started the ride", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
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
        LiftPartnerAdapter adapter = new LiftPartnerAdapter(mainContext, liftUsersList, new LiftPartnerAdapter.OnEndClick() {
            @Override
            public void onButtonClick(LiftUsers user) {
                alertDialog.dismiss();
                request_id = user.getRequest_id();
                endRideCinfirmationDialog(false);
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
                    String etlocation = st.getString("location");
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
                    kmpoint.setText("Per KM Point : " + perkm.toString());
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
                    String scss = jObject.getString("message");
                    Log.d("success", scss);
                    if (scss.equalsIgnoreCase("Success")) {
                        Toast.makeText(StartRideActivity.this, "Payment Successfully Done!!", Toast.LENGTH_SHORT).show();
                        getPayedTODriver();
                    }
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
        }
    }

    @Override
    protected void onStop() {
//        mService.removeLocationUpdates();
        ridestarted = false;
        startedcount = 0;
        stopLocationUpdates();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
