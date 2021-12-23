package com.liftPlzz.activity;

import android.Manifest;
import android.app.ProgressDialog;
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
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
import com.liftPlzz.adapter.PaymentHistoryAdatper;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.locationservice.LocationUpdatesService;
import com.liftPlzz.locationservice.Utils;
import com.liftPlzz.model.PaymentHistoryModel;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//++++++++++++++++++++++++++++++Driverliftstart havetobeimplimented+++++
public class StartRideActivity extends AppCompatActivity implements
        OnMapReadyCallback, PaymentResultListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    //+++++++++++++++++++++++++++++++++++++++++++++++get Location++++++++++++++++++++++++++++++++++++++++++++++++++++++
// Write a message to the database
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
    private String request_id;
    private int bywhomRidestarted = -1;
    private Location location;
    private String tracking_lift_id;
    //    private ProgressDialog drivedriverstartprogress;
    private Context mainContext;
    private boolean driverstarted = false;


    //+++++++++++++++++++++++++++++++++++++++++++++++
    //    private String strToken = "";

    /**
     * Receiver for broadcasts sent by {@link LocationUpdatesService}.
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
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


        }
    }

    private void placeTheCUrrentmarker(Location location) {
        LatLng latLngOrigin = new LatLng(location.getLatitude(), location.getLongitude());
//        origin = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
//        pickupLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        mGoogleMap.addMarker(new MarkerOptions()
                .position(latLngOrigin)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pic_location))
                .title("Driver"));
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

    // Tracks the bound state of the service.
    private boolean mBound = false;

    private MyReceiver myReceiver;
    //    // UI elements.
//    private Button mRequestLocationUpdatesButton;
//    private Button mRemoveLocationUpdatesButton;
// Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
//            if (tvStartRide.getText().toString().equalsIgnoreCase(getResources().getString(R.string.start_ride))) {
//                //todo start ride will call from here
//                if (lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))){
//
//                }
//            }

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
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.btn_start_ride)
    AppCompatTextView tvStartRide;
    private String strToken = "";
    private Lift lift;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_start_ride);
        ButterKnife.bind(this);
//        TextView btn_start_rideqw = (TextView) findViewById(R.id.btn_start_ride);
        callButton = findViewById(R.id.callButton);
        smsButton = findViewById(R.id.smsButton);
        callButton.setBackgroundResource(R.drawable.telephone);
        smsButton.setBackgroundResource(R.drawable.sms);
        sosnumbers();

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sos.isEmpty()){
                    Toast.makeText(mainContext, "Emergency number not found", Toast.LENGTH_SHORT).show();
                }else{
                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                            "tel", sos, null));
                    startActivity(phoneIntent);
                }


            }
        });

        smsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sos.isEmpty()){
                    Toast.makeText(mainContext, "Emergency number not found", Toast.LENGTH_SHORT).show();
                }else{
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
        Log.e("lift.getLiftType()", "" + lift.getLiftType());
        HistoryStoreLoactiontoDatabaseReference = FirebaseDatabase.getInstance().getReference();
//        assert lift != null;
//        Log.e("lift" , "getDriver_tracking_id"+lift.getDriver_tracking_id());
        startedcount = 0;
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        toolBarTitle.setText(getResources().getString(R.string.start_ride));
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_matching);
//        mapFragment.getMapAsync(StartRideActivity.this);
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
//        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_matching);
        myReceiver = new MyReceiver();
        mapFragment.getMapAsync(this);
//        tvStartRide.setText("End Ride");
//        InitLocation();
        if (tvStartRide.getText().toString().equalsIgnoreCase(getResources().getString(R.string.start_ride))) {
//            todo start ride will call from here
            if (!lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))) {
                Log.e("Lift", "Found");
                getUsers(2);

            } else {
                Toast.makeText(StartRideActivity.this, "Start ride to know user's location of your lift", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void InitLocation(String user_id, String tracking_lift_id) {

//
        // Check that the user hasn't revoked permissions by going to Settings.
        if (Utils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        }
        //++++++++++++++++++++++++++++++++Database+++++++++++++++++++++++++++++++++++++++
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
//                Post post = dataSnapshot.getValue(Post.class);
                // ..
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
        // ...
        mDatabase = FirebaseDatabase.getInstance().getReference();
//        Query lastKnownLocation = mDatabase.child("Drivers").child("1").child("location");
//        Query lastKnownLocation = mDatabase.child("LocationMap").child("Drivers").child("1").child("location");
        Query lastKnownLocation = mDatabase.child("LocationMap").child("Drivers").child(user_id).child(tracking_lift_id).child("location");
        lastKnownLocation.addValueEventListener(postListener);
//        mDatabase.addValueEventListener(postListener);
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        //++++++++++++++++++++++++++++++++++++++++++++++++
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

            mGoogleMap.addMarker(new MarkerOptions()
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

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
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
                        // Update the buttons state depending on whether location updates are being requested.
                        if (s.equals(Utils.KEY_REQUESTING_LOCATION_UPDATES)) {
//                            setButtonsState(sharedPreferences.getBoolean(Utils.KEY_REQUESTING_LOCATION_UPDATES,
//                                    false));
                        }
                    }
                });

//        if (!checkPermissions()) {
//            requestPermissions();
//        } else {
//            mService.requestLocationUpdates();
//        }

        // Restore the state of the buttons when the activity (re)launches.
//        setButtonsState(Utils.requestingLocationUpdates(this));

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
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

//                Log.d("btn_start_ride", txt);
                    if (tvStartRide.getText().toString().equalsIgnoreCase(getResources().getString(R.string.start_ride))) {
                        //todo start ride will call from here
                        if (lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))) {
                            Log.d("btn_start_ride", txt);
                            ridestarted = true;
                            bywhomRidestarted = 0;
                            mService.requestLocationUpdates();
                            Log.e("Line 561", "offer_lift match");
                            //  getLiftStartCodeMatch(strToken , tvStartRide.getText().toString());

//                        // Bind to the service. If the service is in foreground mode, this signals to the service
//                        // that since this activity is in the foreground, the service can exit foreground mode.
//                        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
//                                Context.BIND_AUTO_CREATE);
//                        if (!checkPermissions()) {
//                            requestPermissions();
//                        } else {
//                            mService.requestLocationUpdates();
//                        }
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
//                        driverendLift(strToken);
                            getRideEndBYDriver(strToken, 1);
                        } else {

                            getRideEnd(strToken, 2);
                        }
                    }
                }catch (Exception E){
                    Log.e("Exception E", ""+E.getMessage());
                }
                break;
        }
    }

    private void getRideEndBYDriver(String strToken, int i) {
//        drivedriverstartprogress = new ProgressDialog(StartRideActivity.this);
//        drivedriverstartprogress.setCancelable(false);
//        drivedriverstartprogress.setTitle("Starting drive");
//        drivedriverstartprogress.show();
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
//                        drivedriverstartprogress.dismiss();
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
//                            if(user.getString("user_id").equals(sharedPreferences.getString(Constants.USER_ID, "")))
//                            {
//                              placeUser(user);

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
//                        drivedriverstartprogress.dismiss();
                        Toast.makeText(StartRideActivity.this, "Failed to end ride,server error", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
//                    drivedriverstartprogress.dismiss();
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

    private void driverendLift(String strToken) {
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++Driver started+++++++++++++++++++++++++++++
    private void startDriverLift(Location location, String strToken) {
//        Constants.showLoader(StartRideActivity.this);
//        drivedriverstartprogress = new ProgressDialog(mainContext);
//        drivedriverstartprogress.setCancelable(false);
//        drivedriverstartprogress.setTitle("Starting drive");
//        drivedriverstartprogress.show();
        Log.d("liftiddd", String.valueOf(lift.getId()));
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "https://charpair.com/api/driver-liftstart", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject base = new JSONObject(response.toString());
                    Log.e("base", "is" + base);
                    if (base.getBoolean("status")) {
                        tvStartRide.setText("End Ride");
                        try {
                            Toast.makeText(StartRideActivity.this, base.getJSONObject("message").toString(), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(StartRideActivity.this, "Drive started", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                        Toast.makeText(StartRideActivity.this, "Please wait we are getting live location", Toast.LENGTH_LONG).show();
                        Log.d("usersresponse liftstart", response);
                        ridestarted = false;
                        bywhomRidestarted = 0;


                        getUsers(1);
                    } else {
                        Toast.makeText(StartRideActivity.this, "Drive already started", Toast.LENGTH_SHORT).show();
                    }

//                    Constants.hideLoader();
                } catch (JSONException e) {
//                    drivedriverstartprogress.dismiss();
                    mService.removeLocationUpdates();
                    //                    Constants.hideLoader();
                    Toast.makeText(StartRideActivity.this, "Cannot Start your drive as ride has been removed", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, error -> {
            mService.removeLocationUpdates();
//            drivedriverstartprogress.dismiss();
            Toast.makeText(StartRideActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
//            Constants.hideLoader();
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
//        alertDialogBuilder.setMessage(findRideData.getSubMessage())
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
                getLiftStartCodeMatch(strToken, otp);
//                Toast.makeText(StartRideActivity.this, otp, Toast.LENGTH_SHORT).show();
                alert.dismiss();
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
//            for (int i = 0; i < pontos.size() - 1; i++) {
//                LatLng src = pontos.get(i);
//                LatLng dest = pontos.get(i + 1);
//                try {
//                    //here is where it will draw the polyline in your map
//                    Polyline line = mGoogleMap.addPolyline(new PolylineOptions()
//                            .add(new LatLng(src.latitude, src.longitude),
//                                    new LatLng(dest.latitude, dest.longitude))
//                            .width(7).color(Color.GREEN).geodesic(true));
//                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//                } catch (NullPointerException e) {
//                    Log.e("Error", "NullPointerException onPostExecute: " + e.toString());
//                } catch (Exception e2) {
//                    Log.e("Error", "Exception onPostExecute: " + e2.toString());
//                }

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

//                        JSONObject res = jsonObject.optJSONObject("response");
                        boolean status = jsonObject.optBoolean("status");
                        String msg = jsonObject.optString("message");
                        Log.d("StartRideActivity", "msg" + msg);
                        Toast.makeText(StartRideActivity.this, msg, Toast.LENGTH_SHORT).show();
                        if (status) {
                            request_id = jsonObject.getJSONObject("data").getString("request_id");
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

    public void getRideEnd(String token, int ridender) {
//        mService.removeLocationUpdates();
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
        Call<JsonObject> call = api.rideEnd(Constants.API_KEY, Constants.ANDROID, token, Integer.parseInt(request_id), location.getLatitude(), location.getLongitude());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Constants.hideLoader();
                Log.d("endrideresponse", new Gson().toJson(response.body()));
                Log.d("requestidd", lift.getId().toString());
                if (response.code() == 200) {
                    try {
                        Toast.makeText(StartRideActivity.this, "Ride ended successfully", Toast.LENGTH_SHORT).show();
//                        finish();
                        if (ridender == 2) {
                            getInvoice();
                        } else {

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

//        alertDialogBuilder.setMessage(findRideData.getSubMessage())
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
//        if (lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))) {
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
//        if (lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "https://charpair.com/api/partner-latlong", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("usersresponse", response);
                try {
                    JSONObject jObject = new JSONObject(response);
//                    JSONObject res = jObject.getJSONObject("response");
//                    JSONObject res = jObject;
                    JSONObject data = jObject.getJSONObject("data");
                    users = data.getJSONArray("user");
                    tracking_lift_id = data.getString("tracking_lift_id");

                    if (type == 1) {
                        for (int i = 0; i < users.length(); i++) {

                            JSONObject user = users.getJSONObject(i);
//                            if(user.getString("user_id").equals(sharedPreferences.getString(Constants.USER_ID, "")))
//                            {
//                              placeUser(user);
                            String[] startpoint = new String[0];
                            try {
                                startpoint = user.getString("start_points").split(",");
                                placeUser(startpoint);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            }


                        }
//                        mService.removeLocationUpdates();

//                        drivedriverstartprogress.dismiss();
//                            JSONObject driver = data.getJSONObject("driver");
//                            InitLocation(driver.getString("user_id"));
                    } else {
                        try {
                            JSONObject driver = data.getJSONObject("driver");
                            driver_id = driver.getString("user_id");
                            InitLocation(driver_id, tracking_lift_id);
                        } catch (Exception e) {
                            Toast.makeText(StartRideActivity.this, "Driver not found or driver has not started the ride", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

//                        InitLocation(String.valueOf(99));
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
//        }
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
//                            getPayedTODriver();
                            alertDialog.dismiss();
                            pay();
//                            paymentintegration();
                        }
                    });
                    alertDialog.show();
//                    dialogBuilder.create().show();
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
//                params.put("api_key", "070b92d28adc166b3a6c63c2d44535d2f62a3e24");
//                params.put("client", "android");
//                params.put("token", "NRy4MvEaDj5O04r8S6GGSZAJ7T5tv1QvS969rtgyYe7qdyKv8q6wjWBozH5I");
//                params.put("request_id", "57");

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
//                    JSONObject res = jObject.getJSONObject("response");
                    String scss = jObject.getString("message");
//                    String scss = jObject.getString("message");
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
//                params.put("api_key", Constants.API_KEY);
//                params.put("client", Constants.ANDROID);
//                params.put("token", strToken);
//                params.put("request_id", "108");
//                params.put("amount", "189");


                return params;
            }
        };
        queue.add(sr);
    }

    private void paymentintegration() {
//        String samount = totalpoint;

        // rounding off the amount.
        int amount = Math.round(Float.parseFloat(totalpoint) * 100);
        // initialize Razorpay account.
        Checkout checkout = new Checkout();
        // set your id as below
        checkout.setKeyID("Enter your key id here");
        // set image
//        checkout.setImage(R.drawable.gfgimage);
        // initialize json object
        JSONObject object = new JSONObject();
        try {
            // to put name
            object.put("name", "Test Payment");
            // put description
            object.put("description", "Test Description");
            // to set theme color
            object.put("theme.color", "");
            // put the currency
            object.put("currency", "INR");
            // put amount
            object.put("amount", amount);
            // put mobile number
            object.put("prefill.contact", "0000000000");
            // put email
            object.put("prefill.email", "test@test.com");

            // open razorpay to checkout activity
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
                    Log.d("sos", sos);

                   } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constants.hideLoader();
                Log.e("rise vo",""+error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", Constants.API_KEY);
                params.put("client", Constants.ANDROID);
                params.put("token", strToken);
             //   params.put("token", "064ywr3Ht5LPpFPF73J0foCAdvw3ylSDXJys8IqATQ2wyvwimen827FAPA5I");
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
//       if(startedcount==3)
//       {
//           try{
//               LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
//                       new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
//
//               mService.requestLocationUpdates();
//           }catch (Exception e)
//           {
//               Log.e("it is","the user no lcoation needed");
//               e.printStackTrace();
//           }
//       }
        if(startedcount==3)
        {
            mService.requestLocationUpdates();
        }else {
            try{
                LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                        new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));


            }catch (Exception e)
            {
                Log.e("it is","the user no location needed");
                e.printStackTrace();
            }
        }



    }

    @Override
    protected void onStop() {
//        if(drivedriverstartprogress !=null || drivedriverstartprogress.isShowing())
//        {
//            drivedriverstartprogress.dismiss();
//        }
        mService.removeLocationUpdates();
        ridestarted =false;
        startedcount =0;
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
///+++++++++++++++++++++++++++++FOr user+++++++++++++++++++++++++Ride end actiivty+++++++++++++++++++ Liftstartcodematch

//package com.liftPlzz.activity;
//
//import android.Manifest;
//import android.content.BroadcastReceiver;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.ServiceConnection;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.preference.PreferenceManager;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.AppCompatEditText;
//import androidx.appcompat.widget.AppCompatTextView;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.app.ActivityCompat;
//import androidx.localbroadcastmanager.content.LocalBroadcastManager;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//import com.liftPlzz.DriverUserLocationActivity;
//import com.liftPlzz.R;
//import com.liftPlzz.api.ApiService;
//import com.liftPlzz.api.RetroClient;
//import com.liftPlzz.locationservice.LocationUpdatesService;
//import com.liftPlzz.locationservice.Utils;
//import com.liftPlzz.model.upcomingLift.Lift;
//import com.liftPlzz.utils.Constants;
//
//import org.jetbrains.annotations.NotNull;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
////++++++++++++++++++++++++++++++Driverliftstart havetobeimplimented+++++
/////+++++++++++++++++++++++++++++FOr user+++++++++++++++++++++++++Ride end actiivty+++++++++++++++++++ Liftstartcodematch
//public class StartRideActivity extends AppCompatActivity implements
//        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener {
////+++++++++++++++++++++++++++++++++++++++++++++++get Location++++++++++++++++++++++++++++++++++++++++++++++++++++++
//// Write a message to the database
//    FirebaseDatabase database = FirebaseDatabase.getInstance();
//    private DatabaseReference mDatabase;
//    private DatabaseReference StoreLoactiontoDatabaseReference;
//    GoogleMap mGoogleMap;
//    SharedPreferences sharedPreferences;
//    SupportMapFragment mapFragment;
//    Location Previouslocation = null;
//    //    private String strToken = "";
//
//    /**
//     * Receiver for broadcasts sent by {@link LocationUpdatesService}.
//     */
//    private class MyReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
//            if(Previouslocation ==null)
//            {
//                Previouslocation = location;
//            }else if(location == Previouslocation)
//            {
//                Log.e(TAG , "Location Same location");
//            }
//            if (location != null) {
////                Log.e(TAG , "Location Received"+Utils.getLocationText(location));
//                // ...
//                StoreLoactiontoDatabaseReference = FirebaseDatabase.getInstance().getReference();
//                String livelocation  =String.valueOf(location.getLatitude()) +","+String.valueOf(location.getLongitude() );
//                Log.e(TAG , "Location livelocation"+livelocation);
//                StoreLoactiontoDatabaseReference  = StoreLoactiontoDatabaseReference.child("LocationMap").child("Drivers").child(sharedPreferences.getString(Constants.USER_ID, "")).child("location");
//                StoreLoactiontoDatabaseReference.setValue(livelocation).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Log.e(TAG , "Location Success");
//                        StoreLoactiontoDatabaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//                            @Override
//                            public void onSuccess(DataSnapshot dataSnapshot) {
//
//                                String[] Latbroken = dataSnapshot.getValue().toString().split(",");
//                                Log.e(TAG , "Location Retrieved"+Latbroken[0] +" and "+Latbroken[1]);
//                                Location targetLocation = new Location("");//provider name is unnecessary
//                                targetLocation.setLatitude(Double.parseDouble(Latbroken[0]));//your coords of course
//                                targetLocation.setLongitude(Double.parseDouble(Latbroken[1]));
//
//
////                                Location retrivedloc = new Location(Latbroken[0] , Latbroken[1]) ;
//                                placeTheCUrrentmarker(targetLocation);
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull @NotNull Exception e) {
//                                e.printStackTrace();
//                                Log.e(TAG , "Location Failed");
//
//                            }
//                        });
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull @NotNull Exception e) {
//                        e.printStackTrace();
//                    }
//                });
////                placeTheCUrrentmarker(location);
////                mDatabase = FirebaseDatabase.getInstance().getReference();
////                mDatabase.child("LocationMap").child("Drivers").child("1").setValue(name);
////                Toast.makeText(DriverUserLocationActivity.this, Utils.getLocationText(location),
////                        Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//    private void placeTheCUrrentmarker(Location location) {
//        LatLng latLngOrigin = new LatLng(location.getLatitude(), location.getLongitude());
////        origin = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
////        pickupLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//
//        mGoogleMap.addMarker(new MarkerOptions()
//                .position(latLngOrigin)
//                .draggable(true)
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pic_location))
//                .title("Driver"));
//
//        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 15.0f));
//        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
//    }
//    private static final String TAG = StartRideActivity.class.getSimpleName();
//
//    // Used in checking for runtime permissions.
//    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
//    private LocationUpdatesService mService = null;
//
//    // Tracks the bound state of the service.
//    private boolean mBound = false;
//
//    private MyReceiver myReceiver;
//    //    // UI elements.
////    private Button mRequestLocationUpdatesButton;
////    private Button mRemoveLocationUpdatesButton;
//// Monitors the state of the connection to the service.
//    private final ServiceConnection mServiceConnection = new ServiceConnection() {
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
//            mService = binder.getService();
//            mService.requestLocationUpdates();
//            mBound = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            mService = null;
//            mBound = false;
//        }
//    };
//    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.toolBarTitle)
//    AppCompatTextView toolBarTitle;
//    @BindView(R.id.imageViewBack)
//    ImageView imageViewBack;
//    @BindView(R.id.btn_start_ride)
//    AppCompatTextView tvStartRide;
//    private String strToken = "";
//    private Lift lift;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_start_ride);
//        ButterKnife.bind(this);
//        if (getIntent() != null) {
//            lift = (Lift) getIntent().getSerializableExtra(Constants.LIFT_OBJ);
//        }
//        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        toolBarTitle.setText(getResources().getString(R.string.start_ride));
//        strToken = sharedPreferences.getString(Constants.TOKEN, "");
//        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_matching);
////        mapFragment.getMapAsync(StartRideActivity.this);
//        InitLocation();
//
//
//    }
//
//    private void InitLocation() {
//        myReceiver = new MyReceiver();
////        strToken = sharedPreferences.getString(Constants.TOKEN, "");
//        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_matching);
//
//        mapFragment.getMapAsync(this);
//        // Check that the user hasn't revoked permissions by going to Settings.
//        if (Utils.requestingLocationUpdates(this)) {
//            if (!checkPermissions()) {
//                requestPermissions();
//            }
//        }
//        //++++++++++++++++++++++++++++++++Database+++++++++++++++++++++++++++++++++++++++
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get Post object and use the values to update the UI
////                Post post = dataSnapshot.getValue(Post.class);
//                // ..
//                Log.e("dataSnapshot" , ""+dataSnapshot);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//            }
//        };
//        // ...
//        mDatabase = FirebaseDatabase.getInstance().getReference();
////        Query lastKnownLocation = mDatabase.child("Drivers").child("1").child("location");
////        Query lastKnownLocation = mDatabase.child("LocationMap").child("Drivers").child("1").child("location");
//        Query lastKnownLocation = mDatabase.child("LocationMap").child("Drivers").child(sharedPreferences.getString(Constants.USER_ID, "")).child("location");
//        lastKnownLocation.addValueEventListener(postListener);
////        mDatabase.addValueEventListener(postListener);
//        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//        //++++++++++++++++++++++++++++++++++++++++++++++++
//    }
//
//    /**
//     * Returns the current state of the permissions needed.
//     */
//    private boolean checkPermissions() {
//        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION);
//    }
//
//    private void requestPermissions() {
//        boolean shouldProvideRationale =
//                ActivityCompat.shouldShowRequestPermissionRationale(this,
//                        Manifest.permission.ACCESS_FINE_LOCATION);
//
//        // Provide an additional rationale to the user. This would happen if the user denied the
//        // request previously, but didn't check the "Don't ask again" checkbox.
//        if (shouldProvideRationale) {
//            Log.i(TAG, "Displaying permission rationale to provide additional context.");
//
//        } else {
//            Log.i(TAG, "Requesting permission");
//            // Request permission. It's possible this can be auto answered if device policy
//            // sets the permission in a given state or the user denied the permission
//            // previously and checked "Never ask again".
//            ActivityCompat.requestPermissions(StartRideActivity.this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    REQUEST_PERMISSIONS_REQUEST_CODE);
//        }
//    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        PreferenceManager.getDefaultSharedPreferences(this)
//                .registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
//                    @Override
//                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
//                        // Update the buttons state depending on whether location updates are being requested.
//                        if (s.equals(Utils.KEY_REQUESTING_LOCATION_UPDATES)) {
////                            setButtonsState(sharedPreferences.getBoolean(Utils.KEY_REQUESTING_LOCATION_UPDATES,
////                                    false));
//                        }
//                    }
//                });
//
////        if (!checkPermissions()) {
////            requestPermissions();
////        } else {
////            mService.requestLocationUpdates();
////        }
//
//        // Restore the state of the buttons when the activity (re)launches.
////        setButtonsState(Utils.requestingLocationUpdates(this));
//
//        // Bind to the service. If the service is in foreground mode, this signals to the service
//        // that since this activity is in the foreground, the service can exit foreground mode.
//        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
//                Context.BIND_AUTO_CREATE);
//
//    }
//    @OnClick({R.id.imageViewBack, R.id.btn_start_ride})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.imageViewBack:
//                Log.d("btn_start_ride", "onBackPressed");
//                onBackPressed();
//                break;
//
//            case R.id.btn_start_ride:
//                String txt = tvStartRide.getText().toString();
//
////                Log.d("btn_start_ride", txt);
//                if (tvStartRide.getText().toString().equalsIgnoreCase(getResources().getString(R.string.start_ride))) {
//                    //todo start ride will call from here
//                    if (lift.getLiftType().equalsIgnoreCase(getResources().getString(R.string.offer_lift))){
//                        Log.d("btn_start_ride", txt);
//                        //++++++++++++++++++++Driver
////                        getLiftStartCodeMatch();
//                        //+++++++++++++++++++++++++Changed from  getLiftStartCodeMatch()+++++++++++++++++
////                        getLiftStartCodeMatch(strToken , tvStartRide.getText().toString());
//
////                        // Bind to the service. If the service is in foreground mode, this signals to the service
////                        // that since this activity is in the foreground, the service can exit foreground mode.
//                        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
//                                Context.BIND_AUTO_CREATE);
//                        if (!checkPermissions()) {
//                            requestPermissions();
//                        } else {
//                            mService.requestLocationUpdates();
//                        }
//                    }else {
//                        showDialogEnterCode();
//                    }
//                } else {
//                    getRideEnd(strToken);
//                }
//                break;
//        }
//    }
//
//    private void showDialogEnterCode() {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setTitle("Enter Code");
////        alertDialogBuilder.setMessage(findRideData.getSubMessage())
//        alertDialogBuilder.setCancelable(false);
//        final View customLayout = getLayoutInflater().inflate(R.layout.dialog_enter_otp,
//                null);
//        alertDialogBuilder.setView(customLayout);
//        AlertDialog alert = alertDialogBuilder.create();
//        alert.show();
//        AppCompatEditText editText = customLayout.findViewById(R.id.editText);
//        AppCompatTextView tvOk = customLayout.findViewById(R.id.btn_ok);
//        AppCompatTextView tvCancel = customLayout.findViewById(R.id.btn_cancel);
//        tvCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alert.dismiss();
//            }
//        });
//
//        tvOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String otp = editText.getText().toString();
//                getLiftStartCodeMatch(strToken, otp);
////                Toast.makeText(StartRideActivity.this, otp, Toast.LENGTH_SHORT).show();
//                alert.dismiss();
//            }
//        });
//
//
//    }
//
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mGoogleMap = googleMap;
//        if (ActivityCompat.checkSelfPermission(StartRideActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(StartRideActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more detai ls.
//            return;
//        }
//        mGoogleMap.setMyLocationEnabled(true);
//        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
//        LatLng latLngOrigin = new LatLng(22.7244, 75.8839);
////        origin = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
////        pickupLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//
//        googleMap.addMarker(new MarkerOptions()
//                .position(latLngOrigin)
//                .draggable(true)
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pic_location))
//                .title("First"));
//
//        // [START_EXCLUDE silent]
////        editTextPickupLocation.setText(getCompleteAddressString(currentLocation.getLatitude(), currentLocation.getLongitude()));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 15.0f));
////        startPoint = getJsonObjectFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude());
//
//        // Zoom in the Google Map
////        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
////        googleMap.moveCamera(new CameraUpdateFactory().newLatLngZoom(la));
//        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
//
//    }
//
//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//
//    }
//
//    class GetDirection extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//        }
//
//        protected String doInBackground(String... args) {
////            String stringUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=" + origin + "&destination=" + destination + "&key=" + getResources().getString(R.string.maps_api_key) + "&sensor=false";
//            String stringUrl = "";
//            StringBuilder response = new StringBuilder();
//            try {
//                URL url = new URL(stringUrl);
//                HttpURLConnection httpconn = (HttpURLConnection) url
//                        .openConnection();
//                if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                    BufferedReader input = new BufferedReader(
//                            new InputStreamReader(httpconn.getInputStream()),
//                            8192);
//                    String strLine = null;
//
//                    while ((strLine = input.readLine()) != null) {
//                        response.append(strLine);
//                    }
//                    input.close();
//                }
//
//                String jsonOutput = response.toString();
//
//                JSONObject jsonObject = new JSONObject(jsonOutput);
//
//                // routesArray contains ALL routes
//                JSONArray routesArray = jsonObject.getJSONArray("routes");
//                // Grab the first route
//                JSONObject route = routesArray.getJSONObject(0);
//
//                JSONObject poly = route.getJSONObject("overview_polyline");
//                String polyline = poly.getString("points");
////                pontos = decodePoly(polyline);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return null;
//
//        }
//
//        protected void onPostExecute(String file_url) {
////            for (int i = 0; i < pontos.size() - 1; i++) {
////                LatLng src = pontos.get(i);
////                LatLng dest = pontos.get(i + 1);
////                try {
////                    //here is where it will draw the polyline in your map
////                    Polyline line = mGoogleMap.addPolyline(new PolylineOptions()
////                            .add(new LatLng(src.latitude, src.longitude),
////                                    new LatLng(dest.latitude, dest.longitude))
////                            .width(7).color(Color.GREEN).geodesic(true));
////                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
////                } catch (NullPointerException e) {
////                    Log.e("Error", "NullPointerException onPostExecute: " + e.toString());
////                } catch (Exception e2) {
////                    Log.e("Error", "Exception onPostExecute: " + e2.toString());
////                }
//
//        }
//
//
//        private List<LatLng> decodePoly(String encoded) {
//
//            List<LatLng> poly = new ArrayList<LatLng>();
//            int index = 0, len = encoded.length();
//            int lat = 0, lng = 0;
//
//            while (index < len) {
//                int b, shift = 0, result = 0;
//                do {
//                    b = encoded.charAt(index++) - 63;
//                    result |= (b & 0x1f) << shift;
//                    shift += 5;
//                } while (b >= 0x20);
//                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//                lat += dlat;
//
//                shift = 0;
//                result = 0;
//                do {
//                    b = encoded.charAt(index++) - 63;
//                    result |= (b & 0x1f) << shift;
//                    shift += 5;
//                } while (b >= 0x20);
//                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
//                lng += dlng;
//
//                LatLng p = new LatLng((((double) lat / 1E5)),
//                        (((double) lng / 1E5)));
//                poly.add(p);
//            }
//
//            return poly;
//        }
//
//    }
//
//    public void getLiftStartCodeMatch(String token, String code) {
//        Constants.showLoader(StartRideActivity.this);
//        ApiService api = RetroClient.getApiService();
//        Call<ResponseBody> call = api.liftStartCodeMatch(Constants.API_KEY, Constants.ANDROID, token, lift.getId(), Integer.parseInt(code), 0.0, 0.0);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Constants.hideLoader();
//                if (response.code() == 200) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.body().string());
//                        Log.d("StartRideActivity", jsonObject.toString());
//                        JSONObject res = jsonObject.optJSONObject("response");
//                        boolean status = res.optBoolean("status");
//                        String msg = res.optString("message");
//                        Toast.makeText(StartRideActivity.this, msg, Toast.LENGTH_SHORT).show();
//                        if (status) {
//                            tvStartRide.setText(getResources().getString(R.string.end_ride));
//                            tvStartRide.setBackground(getResources().getDrawable(R.drawable.rounded_bg_dark));
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
//                        String message = jsonObject.optString("message");
//                        Toast.makeText(StartRideActivity.this, message, Toast.LENGTH_SHORT).show();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
//                Constants.hideLoader();
//                Toast.makeText(StartRideActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public void getRideEnd(String token) {
////        mService.removeLocationUpdates();
//        Constants.showLoader(StartRideActivity.this);
//        ApiService api = RetroClient.getApiService();
//        Call<ResponseBody> call = api.rideEnd(Constants.API_KEY, Constants.ANDROID, token, lift.getId(), 0.0, 0.0);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Constants.hideLoader();
//                if (response.code() == 200) {
//                    try {
//                        Toast.makeText(StartRideActivity.this, "Ride ended successfully", Toast.LENGTH_SHORT).show();
//                        finish();
//
////                        String message = response.body().getResponse().getMessage();
////                        if (response.body().getResponse().getStatus()) {
////                            user = response.body().getResponse().getUser();
////                            setUserData(user);
//////                            Toast.makeText(DriverProfileActivity.this, message, Toast.LENGTH_SHORT).show();
////                        } else {
////                            Toast.makeText(StartRideActivity.this, message, Toast.LENGTH_SHORT).show();
////                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
//                        String message = jsonObject.optString("message");
//                        Toast.makeText(StartRideActivity.this, message, Toast.LENGTH_SHORT).show();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
//                Constants.hideLoader();
//                Toast.makeText(StartRideActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
//                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
//    }
//
//    @Override
//    protected void onStop() {
//        mService.removeLocationUpdates();
//        super.onStop();
//    }
//}