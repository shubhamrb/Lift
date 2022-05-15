package com.liftPlzz.locationservice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.CountDownTimer;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LifecycleService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LiveLocation extends LifecycleService {
    String CHANNEL_ID = "ForegroundServiceChannel";
    CountDownTimer mCounter;
    FusedLocationProviderClient mFusedLocationClient;
    LocationRequest locRequest;
    LocationCallback locationCallback;
    MyLocationListener myLocationListener;

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action == "start_service") {
            init();
            initListener();
        } else if (action == "stop_service") {
            stopForeground(true);
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mFusedLocationClient != null && locationCallback != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
        super.onDestroy();
    }


    private void init() {
        getLocation();
        initCounter();
    }

    /**
     * Initialize counter
     */
    private void initCounter() {
        if (mCounter == null) {
            Long time = 5000L; //300000 //60000
            mCounter = new CountDownTimer(time, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }
                @Override
                public void onFinish() {
                    getLocation();
                }
            }.start();
        }
    }

    /**
     * Initialization of listener
     */
    private void initListener() {
        myLocationListener = new MyLocationListener() {
            @Override
            public void onSuccess(Location location) {
                sendLatLng(location);
                Log.e("Lat Long",""+location.getLatitude()+"\n"+location.getLongitude());
            }

            @Override
            public void onFailed() {

            }
        };
    }

    /**
     * Send lat lng to server
     */
    private void sendLatLng(Location location) {
        /*String lat = location.latitude.toString();
        val lng = location.longitude.toString()
        Log.e("Lat, Lng", "$lat,$lng")
        //Toast.makeText(baseContext, "$lat,$lng", Toast.LENGTH_SHORT).show()
        val anInterface: IApiService = APIClient.getClient().create(IApiService::class.java)
        val callLoginApi: Call<OutResponse> = anInterface.setLocation(
                "Bearer " + preferences.token,
                InputModel(
                        Helper.getCurrentTime(),
                        "Android", Helper.getDeviceId(baseContext),
                        lat, lng
                )
        )
        callLoginApi.enqueue(object : Callback<OutResponse> {
            override fun onResponse(call: Call<OutResponse>, response: Response<OutResponse>) {
                isApiCalling = false
                mCounter?.start()
                if (response.isSuccessful) {
                    try {
                        Log.e("Response : ", response.body().toString())
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<OutResponse>, t: Throwable) {
                isApiCalling = false
                mCounter?.start()
            }
        })*/
    }

    /**
     * Get location
     */
    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        )
        mFusedLocationClient = null;
        locRequest = null;
        locationCallback = null;
        mFusedLocationClient =
                LocationServices.getFusedLocationProviderClient(this);
        locRequest = LocationRequest.create();
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //locRequest?.interval = 0
        //locRequest?.fastestInterval = 10000
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null ) {
                        myLocationListener.onSuccess(location);
                    }
                }
                super.onLocationResult(locationResult);
            }
        };
        mFusedLocationClient.requestLocationUpdates(locRequest, locationCallback, null);
    }

}
