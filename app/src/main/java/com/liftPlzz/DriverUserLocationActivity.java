package com.liftPlzz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.liftPlzz.activity.StartRideActivity;
import com.liftPlzz.locationservice.LocationUpdatesService;
import com.liftPlzz.locationservice.Utils;
import com.liftPlzz.utils.Constants;

import org.jetbrains.annotations.NotNull;

public class DriverUserLocationActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener
        , OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase;
    private DatabaseReference StoreLoactiontoDatabaseReference;
    GoogleMap mGoogleMap;
    SharedPreferences sharedPreferences;
    SupportMapFragment mapFragment;
    Location Previouslocation = null;
    Location location;


//    private String strToken = "";

    @Override
    public void onConnected(@Nullable @org.jetbrains.annotations.Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull @NotNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(DriverUserLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DriverUserLocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        LatLng latLngOrigin = new LatLng(22.7244, 75.8839);
//        origin = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
//        pickupLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        googleMap.addMarker(new MarkerOptions()
                .position(latLngOrigin)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pic_location))
                .title("First"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 15.0f));
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    /**
     * Receiver for broadcasts sent by {@link LocationUpdatesService}.
     */
    private abstract class MyReceiver extends BroadcastReceiver {
        public void onReceive(View.OnClickListener context, Intent intent) {
             location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (Previouslocation == null) {
                Previouslocation = location;
            } else if (location == Previouslocation) {
                Log.e(TAG, "Location Same location");
            }
            if (location != null) {
//                Log.e(TAG , "Location Received"+Utils.getLocationText(location));
                // ...
                StoreLoactiontoDatabaseReference = FirebaseDatabase.getInstance().getReference();
                String livelocation = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
                Log.e(TAG, "Location livelocation" + livelocation);
                StoreLoactiontoDatabaseReference = StoreLoactiontoDatabaseReference.child("LocationMap").child("Drivers").child("1").child("location");
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

                    }
                });
//                placeTheCUrrentmarker(location);
//                mDatabase = FirebaseDatabase.getInstance().getReference();
//                mDatabase.child("LocationMap").child("Drivers").child("1").setValue(name);
//                Toast.makeText(DriverUserLocationActivity.this, Utils.getLocationText(location),
//                        Toast.LENGTH_SHORT).show();
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
                .title("First"));

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 15.0f));
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    private static final String TAG = DriverUserLocationActivity.class.getSimpleName();

    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private LocationUpdatesService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;
    // UI elements.
    private Button mRequestLocationUpdatesButton;
    private Button mRemoveLocationUpdatesButton;
    // The BroadcastReceiver used to listen from broadcasts from the service.
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
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_user_location);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View promptView = layoutInflater.inflate(R.layout.twobuttons, null);

        final AlertDialog alertD = new AlertDialog.Builder(this).create();
        alertD.setCanceledOnTouchOutside(true);
        Button driver = (Button) promptView.findViewById(R.id.driver);

        Button user = (Button) promptView.findViewById(R.id.user);

        driver.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertD.hide();

                Toast.makeText(DriverUserLocationActivity.this, "Hi", Toast.LENGTH_SHORT).show();
//                mService.requestLocationUpdates();
//                LocationManager locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
//                if (locationManager != null) {
//                    if (ActivityCompat.checkSelfPermission(mService, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mService, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        return;
//                    }
//                    if (ActivityCompat.checkSelfPermission(mService, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mService, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                        return;
//                    }
//                    Location location1 = locationManager
//                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                    if (location1 != null) {
//                       String latitude = String.valueOf(location1.getLatitude());
//                        String longitude = String.valueOf(location1.getLongitude());
//                        Toast.makeText(mService, latitude, Toast.LENGTH_SHORT).show();
//                        LatLng latLngOrigin2 = new LatLng(location1.getLatitude(), location1.getLongitude());
//                        mGoogleMap.addMarker(new MarkerOptions()
//                                .position(latLngOrigin2)
//                                .draggable(true)
//                                .icon(BitmapDescriptorFactory.defaultMarker())
//                                .title("Your Location"));
//
//                        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin2, 15.0f));
//                        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
//                    }
//
//                }
                // btnAdd1 has been clicked

            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // btnAdd2 has been clicked

            }
        });

        alertD.setView(promptView);

        alertD.show();
//        myReceiver = new MyReceiver();
//        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_matching);

        mapFragment.getMapAsync(this);
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
                Log.e("dataSnapshot" , ""+dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        // ...
        mDatabase = FirebaseDatabase.getInstance().getReference();
//        Query lastKnownLocation = mDatabase.child("Drivers").child("1").child("location");
        Query lastKnownLocation = mDatabase.child("LocationMap").child("Drivers").child("1").child("location");

//        ChildEventListener childEventListener = new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
//                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
//                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
//
//                // A comment has changed, use the key to determine if we are displaying this
//                // comment and if so displayed the changed comment.
////                Comment newComment = dataSnapshot.getValue(Comment.class);
//                String commentKey = dataSnapshot.getKey();
////                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
//                // ...
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());
//
//                // A comment has changed, use the key to determine if we are displaying this
//                // comment and if so remove it.
//                String commentKey = dataSnapshot.getKey();
//
//                // ...
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
//                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
//                Toast.makeText(DriverUserLocationActivity.this, "Failed to load comments.", Toast.LENGTH_SHORT).show();
//            }
//        };
//        lastKnownLocation.addChildEventListener(childEventListener);
        lastKnownLocation.addValueEventListener(postListener);
//        mDatabase.addValueEventListener(postListener);
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    }

    /**
     * Returns the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
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
//            Snackbar.make(
//                    findViewById(R.id.),
//                    R.string.permission_rationale,
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction(R.string.ok, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            // Request permission
//                            ActivityCompat.requestPermissions(MainActivity.this,
//                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                    REQUEST_PERMISSIONS_REQUEST_CODE);
//                        }
//                    })
//                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(DriverUserLocationActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        mRequestLocationUpdatesButton = (Button) findViewById(R.id.request_location_updates_button);
        mRemoveLocationUpdatesButton = (Button) findViewById(R.id.remove_location_updates_button);

        mRequestLocationUpdatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkPermissions()) {
                    requestPermissions();
                } else {
                    mService.requestLocationUpdates();
                }
            }
        });

        mRemoveLocationUpdatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mService.removeLocationUpdates();
            }
        });

        // Restore the state of the buttons when the activity (re)launches.
        setButtonsState(Utils.requestingLocationUpdates(this));

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        // Update the buttons state depending on whether location updates are being requested.
        if (s.equals(Utils.KEY_REQUESTING_LOCATION_UPDATES)) {
            setButtonsState(sharedPreferences.getBoolean(Utils.KEY_REQUESTING_LOCATION_UPDATES,
                    false));
        }
    }

    private void setButtonsState(boolean requestingLocationUpdates) {
        if (requestingLocationUpdates) {
            mRequestLocationUpdatesButton.setEnabled(false);
            mRemoveLocationUpdatesButton.setEnabled(true);
        } else {
            mRequestLocationUpdatesButton.setEnabled(true);
            mRemoveLocationUpdatesButton.setEnabled(false);
        }
    }

}