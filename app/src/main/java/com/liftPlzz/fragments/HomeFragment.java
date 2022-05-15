package com.liftPlzz.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.liftPlzz.R;
import com.liftPlzz.activity.HomeActivity;
import com.liftPlzz.activity.MatchingRideActivity;
import com.liftPlzz.adapter.CheckPointsListAdapter;
import com.liftPlzz.adapter.MyVehicleListRideAdapter;
import com.liftPlzz.adapter.VehiclePagerAdapter;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.dialog.BottomSheetCheckPointsDialog;
import com.liftPlzz.model.CheckPoints;
import com.liftPlzz.model.FindLiftResponse;
import com.liftPlzz.model.createLift.CreateLiftResponse;
import com.liftPlzz.model.getVehicle.Datum;
import com.liftPlzz.model.on_going.InnerGoingResponse;
import com.liftPlzz.presenter.HomePresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.HomeView;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment<HomePresenter, HomeView> implements HomeView,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, NumberPicker.OnValueChangeListener, CheckPointsListAdapter.ItemListener, MyVehicleListRideAdapter.ItemListener, BottomSheetCheckPointsDialog.CallBackSelectionCheckPoints {

    private static final int ADDRESS_PICKER_REQUEST = 1;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageViewHome)
    ImageView imageViewHome;
    @BindView(R.id.imageViewNotification)
    ImageView imageViewNotification;
    @BindView(R.id.editTextPickupLocation)
    AppCompatTextView editTextPickupLocation;
    @BindView(R.id.layoutPickupLocation)
    LinearLayout layoutPickupLocation;
    @BindView(R.id.editTextDropLocation)
    AppCompatTextView editTextDropLocation;
    @BindView(R.id.layoutDropLocation)
    LinearLayout layoutDropLocation;
    @BindView(R.id.layoutSelectDateTime)
    LinearLayout layoutSelectDateTime;
    @BindView(R.id.layoutSelectSeat)
    LinearLayout layoutSelectSeat;
    @BindView(R.id.layoutGiveLift)
    LinearLayout layoutGiveLift;
    @BindView(R.id.layoutTakeLift)
    LinearLayout layoutTakeLift;
    @BindView(R.id.textViewSelectDateTime)
    AppCompatTextView textViewSelectDateTime;
    @BindView(R.id.textViewSelectSeat)
    AppCompatTextView textViewSelectSeat;
    @BindView(R.id.layoutCheckPoints)
    RelativeLayout layoutCheckPoints;
    @BindView(R.id.llchkpoint)
    LinearLayout llchkpoint;
    @BindView(R.id.textViewGiveLift)
    AppCompatTextView textViewGiveLift;
    @BindView(R.id.textViewTakeLift)
    AppCompatTextView textViewTakeLift;
    @BindView(R.id.buttonLift)
    AppCompatButton buttonLift;
    @BindView(R.id.recyclerViewCheckpoints)
    RecyclerView recyclerViewCheckpoints;
    @BindView(R.id.layoutRide)
    LinearLayout layoutRide;
    @BindView(R.id.callButton)
    ImageView callButton;
    @BindView(R.id.smsButton)
    ImageView smsButton;
    int listPos;

    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;

    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    LocationRequest mLocationRequest;
    int locationSelect = 1;
    LatLng pickupLocation;
    LatLng dropLocation;

    List<LatLng> pontos = new ArrayList<>();
    String distanceString = "";
    Polyline polyline;
    String origin;
    LatLng originLat, destinationLat;
    String startPoint = "", endPoint = "";
    String destination;
    PolylineOptions polylineOptions = new PolylineOptions();
    List<CheckPoints> checkPointsList = new ArrayList<>();
    CheckPointsListAdapter checkPointsListAdapter;
    boolean isMultiCheck = false;
    boolean isOfferLift = false;
    String seat = "1";
    String dateTime, liftTime = "";
    String wayPoints = "";
    @BindView(R.id.textViewCheckpoints)
    AppCompatTextView textViewCheckpoints;
    @BindView(R.id.textkm)
    AppCompatTextView textkm;
    int PagerPosition = 0;

    VehiclePagerAdapter pagerAdapter;
    LatLng latLngOrigin, latLngDestination;

    SharedPreferences sharedPreferences;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFragment;
    GoogleApiClient mGoogleApiClient;
    BottomSheetCheckPointsDialog bottomSheetCheckPointsDialog;

    Calendar calendar;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;


    @Override
    protected int createLayout() {
        return R.layout.fragment_home;
    }

    @Override
    protected void setPresenter() {
        presenter = new HomePresenter();
    }

    String sos;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    @Override
    protected HomeView createView() {
        return this;
    }

    private void sosnumbers() {
        Constants.showLoader(getActivity());
        RequestQueue queue = Volley.newRequestQueue(getActivity());
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
        }, error -> {
            Constants.hideLoader();
            Log.e("rise vo", "" + error.getMessage());
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", Constants.API_KEY);
                params.put("client", Constants.ANDROID);
                params.put("token", sharedPreferences.getString(Constants.TOKEN, ""));
                //   params.put("token", "064ywr3Ht5LPpFPF73J0foCAdvw3ylSDXJys8IqATQ2wyvwimen827FAPA5I");
                return params;
            }
        };
        queue.add(sr);

    }



    private String strToken = "";

    public void requestpermisson() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(sos, null, "This is emergency message from liftplzz app", null, null);
                    Toast.makeText(getActivity(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    @Override
    protected void bindData() {
        Constants.isLiftOnGoing = false;
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        MapUtility.apiKey = getResources().getString(R.string.maps_api_key);
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        sosnumbers();
        bottomSheetCheckPointsDialog = new BottomSheetCheckPointsDialog();
        bottomSheetCheckPointsDialog.setSelectionListner(HomeFragment.this);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("gfggf", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        try {
                            presenter.updateToken(sharedPreferences.getString(Constants.TOKEN, ""), token);
                            String msg = getString(R.string.msg_token_fmt, token);
                            Log.d("hfhfh", msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        // mapFragment.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
//        presenter.getOnGoing(sharedPreferences.getString(Constants.TOKEN, ""));
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @OnClick({R.id.callButton, R.id.smsButton, R.id.buttonLift, R.id.layoutCheckPoints, R.id.layoutGiveLift, R.id.layoutTakeLift, R.id.layoutSelectSeat, R.id.layoutSelectDateTime, R.id.imageViewHome, R.id.imageViewNotification, R.id.layoutPickupLocation, R.id.layoutDropLocation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewHome:
                ((HomeActivity) getActivity()).openDrawer();
                break;
            case R.id.buttonLift:
                if (buttonLift.getText().toString().equalsIgnoreCase(getResources().getString(R.string.find_lift))) {
                    if (editTextPickupLocation.getText().toString().isEmpty()) {
                        showMessage("Select pickup location");
                    } else if (editTextDropLocation.getText().toString().isEmpty()) {
                        showMessage("Select dropoff location");
                    } else if (textViewSelectDateTime.getText().toString().equalsIgnoreCase(getContext().getString(R.string.select_date_time))) {
                        showMessage("Select Data Time");
                    } else if (textViewSelectSeat.getText().toString().equalsIgnoreCase("Select Seat")) {
                        showMessage("Select Seats");
                    } else {
                        presenter.findLift(sharedPreferences.getString(Constants.TOKEN, ""), "add ride", seat, startPoint, endPoint, dateTime, liftTime, textkm.getText().toString());
                    }
                } else {
                    //create lift
                    if (editTextPickupLocation.getText().toString().isEmpty()) {
                        showMessage("Select pickup location");
                    } else if (editTextDropLocation.getText().toString().isEmpty()) {
                        showMessage("Select dropoff location");
                    } else if (textViewSelectDateTime.getText().toString().equalsIgnoreCase(getContext().getString(R.string.select_date_time))) {
                        showMessage("Select Data Time");
                    } else if (textViewSelectSeat.getText().toString().equalsIgnoreCase("Select Seat")) {
                        showMessage("Select Seats");
                    } else {
//                        rate_per_km = Integer.parseInt(etkm.getText().toString().trim());
                        if (pagerAdapter != null) {
                            onclickVehicle(data.get(PagerPosition));
                        } else {
                            onclickVehicle(data.get(0));
                        }
                    }
                }
                break;
            case R.id.layoutCheckPoints:
                if (isMultiCheck) {
                    if (editTextDropLocation.getText().toString().isEmpty()) {
                        showMessage("enter dropoff location first");
                    } else {
                        CheckPoints checkPoints = new CheckPoints();
                        checkPoints.setId(checkPointsList.size() + 1);
                        checkPoints.setAddress("Select Checkpoints " + (checkPointsList.size() + 1));
                        checkPointsList.add(checkPoints);
                        isMultiCheck = false;
                        bottomSheetCheckPointsDialog.setGroupList(checkPointsList);
                        bottomSheetCheckPointsDialog.show(
                                getActivity().getSupportFragmentManager(), "check");
                    }
                } else {
                    if (checkPointsList.size() < 3) {
                        CheckPoints checkPoints = new CheckPoints();
                        checkPoints.setId(checkPointsList.size() + 1);
                        checkPoints.setAddress("Select Checkpoints " + (checkPointsList.size() + 1));
                        checkPointsList.add(checkPoints);
                    }
                    bottomSheetCheckPointsDialog.setGroupList(checkPointsList);
                    bottomSheetCheckPointsDialog.show(
                            getActivity().getSupportFragmentManager(), "check");
                }
                break;
            case R.id.layoutSelectSeat:
                if (editTextDropLocation.getText().toString().length() > 0) {
                    show();
                } else {
                    Toast.makeText(getActivity(), "Please select drop location", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.layoutSelectDateTime:
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_InputMethod, HomeFragment.this, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
                break;
            case R.id.imageViewNotification:
                presenter.openNotification();
                break;
            case R.id.layoutPickupLocation:
                locationSelect = 1;
                Intent i = new Intent(getActivity(), LocationPickerActivity.class);
                startActivityForResult(i, ADDRESS_PICKER_REQUEST);
                break;
            case R.id.layoutDropLocation:
                locationSelect = 2;
                Intent i1 = new Intent(getActivity(), LocationPickerActivity.class);
                i1.putExtra("startLocation", true);
                startActivityForResult(i1, ADDRESS_PICKER_REQUEST);
                break;
            case R.id.layoutGiveLift:
                if (!isOfferLift){
                    presenter.getVehicle(sharedPreferences.getString(Constants.TOKEN, ""), textkm.getText().toString());
                }
                isMultiCheck = true;
                layoutGiveLift.setBackground(getResources().getDrawable(R.drawable.rounded_bg));
                layoutTakeLift.setBackground(getResources().getDrawable(R.drawable.rounded_bg_white));
                buttonLift.setText(textViewGiveLift.getText().toString());
                textViewGiveLift.setTextColor(getResources().getColor(R.color.colorWhite));
                textViewTakeLift.setTextColor(getResources().getColor(R.color.colorBlack));
                llchkpoint.setVisibility(View.VISIBLE);
                if(editTextDropLocation.getText().toString().length()>0) {
                    textViewSelectSeat.setText("" + vehicle_name + " | " + rate_per_km + "/km" + " | " + seat + " Seats");
                }
                break;
            case R.id.layoutTakeLift:
                isMultiCheck = false;
                layoutTakeLift.setBackground(getResources().getDrawable(R.drawable.rounded_bg));
                layoutGiveLift.setBackground(getResources().getDrawable(R.drawable.rounded_bg_white));
                buttonLift.setText(textViewTakeLift.getText().toString());
                textViewGiveLift.setTextColor(getResources().getColor(R.color.colorBlack));
                textViewTakeLift.setTextColor(getResources().getColor(R.color.colorWhite));
                llchkpoint.setVisibility(View.INVISIBLE);
                if(editTextDropLocation.getText().toString().length()>0) {
                    textViewSelectSeat.setText(seat + " Seats");
                }
                break;
            case R.id.callButton:
                if (sos.isEmpty()) {
                    Toast.makeText(getActivity(), "Emergency number not found", Toast.LENGTH_SHORT).show();
                } else {
                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                            "tel", sos, null));
                    startActivity(phoneIntent);
                }
                break;
            case R.id.smsButton:
                if (sos.isEmpty()) {
                    Toast.makeText(getActivity(), "Emergency number not found", Toast.LENGTH_SHORT).show();
                } else {
                    requestpermisson();
                }
        }
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                mapFragment.getMapAsync(HomeFragment.this);
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more detai ls.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        latLngOrigin = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        origin = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        pickupLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        originLat = latLngOrigin;
        mGoogleMap.clear();
        mGoogleMap.addMarker(new MarkerOptions()
                .position(latLngOrigin)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pic_location))
                .title("First"));

        // [START_EXCLUDE silent]
        editTextPickupLocation.setText(getCompleteAddressString(currentLocation.getLatitude(), currentLocation.getLongitude()));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 15.0f));
        startPoint = getJsonObjectFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude());
        // Zoom in the Google Map
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//        googleMap.moveCamera(new CameraUpdateFactory().newLatLngZoom(la));
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                Log.d("System out", "onMarkerDragEnd...");
                editTextPickupLocation.setText(getCompleteAddressString(arg0.getPosition().latitude, arg0.getPosition().longitude));
                startPoint = getJsonObjectFromLocation(arg0.getPosition().latitude, arg0.getPosition().longitude);
                latLngOrigin = new LatLng(arg0.getPosition().latitude, arg0.getPosition().longitude);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(arg0.getPosition(), 15.0f));
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
            }
        });

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        fetchLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    public String getJsonObject(Double latitue, Double longitute, Bundle bundle, String locationaddress) {
        String strAdd = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("LatLng", String.valueOf(latitue) + "," + String.valueOf(longitute));
            jsonObject.put("country", bundle.getString("country"));
            jsonObject.put("state", bundle.getString("state"));
            jsonObject.put("city", bundle.getString("city"));
            jsonObject.put("location", locationaddress);
            jsonObject.put("date", "");
            jsonObject.put("time", "");
            strAdd = jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            strAdd = "";
        }
        return strAdd;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra(MapUtility.ADDRESS) != null) {
                    String address = data.getStringExtra(MapUtility.ADDRESS);
                    double currentLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                    double currentLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);
                    Bundle completeAddress = data.getBundleExtra("fullAddress");
                    /**
                     * data in completeAddress bundle
                     "fulladdress"
                     "city"
                     "state"
                     "postalcode"
                     "country"
                     "addressline1"
                     "addressline2"
                     */

                    if (locationSelect == 1) {
                        pickupLocation = new LatLng(currentLatitude, currentLongitude);
                        latLngOrigin = new LatLng(currentLatitude, currentLongitude);
                        originLat = latLngOrigin;
                        StringBuilder strAdd = new StringBuilder().append
                                (completeAddress.getString("addressline2")).append
                                (completeAddress.getString("city")).append(",").append
                                (completeAddress.getString("state"));
                        Log.e("result ", "" + strAdd.toString() + "  ");
                        editTextPickupLocation.setText(address);
                        startPoint = getJsonObject(currentLatitude, currentLongitude, completeAddress, strAdd.toString());
                        if (dropLocation != null) {
                            mGoogleMap.clear();
                            mGoogleMap.addMarker(new MarkerOptions()
                                    .position(pickupLocation)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pic_location))
                                    .title("pickup"));

                            mGoogleMap.addMarker(new MarkerOptions()
                                    .position(dropLocation)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.drop_location))
                                    .title("dropoff"));
                            origin = pickupLocation.latitude + "," + pickupLocation.longitude;
                            destination = dropLocation.latitude + "," + dropLocation.longitude;

                            new GetDirection().execute(origin, destination);

                        } else {
                            mGoogleMap.addMarker(new MarkerOptions()
                                    .position(pickupLocation)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pic_location))
                                    .title("pickup"));
                        }
                    } else if (locationSelect == 2) {
                        StringBuilder stringBuilder = new StringBuilder().append
                                (completeAddress.getString("addressline2")).append
                                (completeAddress.getString("city")).append(",").append
                                (completeAddress.getString("state"));
                        editTextDropLocation.setText(address);
                        dropLocation = new LatLng(currentLatitude, currentLongitude);
                        destinationLat = dropLocation;
                        latLngDestination = new LatLng(currentLatitude, currentLongitude);
                        endPoint = getJsonObject(currentLatitude, currentLongitude, completeAddress, stringBuilder.toString());
                        destination = currentLatitude + "," + currentLongitude;
                        mGoogleMap.clear();
                        mGoogleMap.addMarker(new MarkerOptions()
                                .position(dropLocation)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.drop_location))
                                .title("dropoff"));

                        if (pickupLocation != null) {
                            mGoogleMap.addMarker(new MarkerOptions()
                                    .position(pickupLocation)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pic_location))
                                    .title("pickup"));
                            origin = pickupLocation.latitude + "," + pickupLocation.longitude;
                            destination = dropLocation.latitude + "," + dropLocation.longitude;
                            new GetDirection().execute(origin, destination);
                            //create lift
                            if (isMultiCheck) {
                                presenter.getVehicle(sharedPreferences.getString(Constants.TOKEN, ""), textkm.getText().toString());
                            }
                        }

                    } else {
                        checkPointsList.get(listPos).setAddress(new StringBuilder().append
                                (completeAddress.getString("addressline2")).append
                                (completeAddress.getString("city")).append(",").append
                                (completeAddress.getString("state")).toString());
                        dropLocation = new LatLng(currentLatitude, currentLongitude);
                        latLngDestination = new LatLng(currentLatitude, currentLongitude);
                        checkPointsList.get(listPos).setLat(currentLatitude);
                        checkPointsList.get(listPos).setCity(completeAddress.getString("city"));
                        checkPointsList.get(listPos).setState(completeAddress.getString("state"));
                        checkPointsList.get(listPos).setCountry(completeAddress.getString("country"));
                        checkPointsList.get(listPos).setLongi(currentLongitude);
                        bottomSheetCheckPointsDialog.nofityAdapter();
                        Log.e("Check List Size : ", "" + checkPointsList.size());
                        refreshGoogleMap((ArrayList<CheckPoints>) checkPointsList);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.e("My Current", strReturnedAddress.toString());
            } else {
                Log.e("My Current", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("My Current", "Canont get Address!");
        }
        return strAdd;
    }


    private String getJsonObjectFromLocation(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("LatLng", String.valueOf(returnedAddress.getLatitude()) + "," + String.valueOf(returnedAddress.getLongitude()));
                    jsonObject.put("country", returnedAddress.getCountryName());
                    jsonObject.put("state", returnedAddress.getAdminArea());
                    jsonObject.put("city", returnedAddress.getLocality());
                    jsonObject.put("location", returnedAddress.getAddressLine(0));
                    jsonObject.put("date", "");
                    jsonObject.put("time", "");
                    strAdd = jsonObject.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    strAdd = "";
                }

            } else {
                Log.e("My Current", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("My Current", "Can't get Address!");
        }
        return strAdd;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = dayOfMonth;
        myMonth = month;
        myHour = hour = calendar.get(Calendar.HOUR_OF_DAY);
        myMinute = minute = calendar.get(Calendar.MINUTE);
        showDialog(getActivity(), "Time Picker");
    }

    public void showDialog(Activity activity, String msg) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.time_picker_dialog);

        TimePicker simpleTimePicker = (TimePicker) dialog.findViewById(R.id.simpleTimePicker);
        simpleTimePicker.setIs24HourView(false);
        simpleTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                myHour = hourOfDay;
                myMinute = minute;
            }
        });
        TextView dialogButton = dialog.findViewById(R.id.btn_dialog);
        TextView btn_cancel = dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(v -> dialog.dismiss());
        dialogButton.setOnClickListener(v -> {
            Calendar myCalender = Calendar.getInstance();
            myCalender.set(myYear, myMonth, myday, myHour, myMinute);
            dateTime = new SimpleDateFormat("yyyy-MM-dd").format(myCalender.getTime());
            liftTime = new SimpleDateFormat("HH:mm:ss").format(myCalender.getTime());
            textViewSelectDateTime.setText(new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(myCalender.getTime()));
            dialog.dismiss();
        });
        dialog.show();
    }

    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins) {
        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
        textViewSelectDateTime.setText(aTime);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        myHour = hourOfDay;
        myMinute = minute;
        Calendar myCalender = Calendar.getInstance();
        myCalender.set(myYear, myMonth, myday, myHour, myMinute);
        dateTime = new SimpleDateFormat("yyyy-MM-dd").format(myCalender.getTime());
        liftTime = new SimpleDateFormat("HH:mm:ss").format(myCalender.getTime());
        textViewSelectDateTime.setText(new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(myCalender.getTime()));
    }
    ViewPager vehiclePager;
    EditText etkm;
    LinearLayout llrate;
    AppCompatButton btnSubmit;

    public void show() {
        final Dialog d = new Dialog(getActivity());
        d.setContentView(R.layout.dailog_seat);
        AppCompatButton b1 = d.findViewById(R.id.btnSubmit);
        vehiclePager = d.findViewById(R.id.vehiclePager);
        CircleIndicator indicator = d.findViewById(R.id.indicator);
        RelativeLayout vehicleLayout = d.findViewById(R.id.vehicleLayout);
        etkm = d.findViewById(R.id.etkm);
        llrate = d.findViewById(R.id.llrate);
        btnSubmit = d.findViewById(R.id.btnSubmit);

        TextView oneTxt = d.findViewById(R.id.oneTxt);
        TextView twoTxt = d.findViewById(R.id.twoTxt);
        TextView threeTxt = d.findViewById(R.id.threeTxt);
        TextView fourTxt = d.findViewById(R.id.fourTxt);
        TextView fiveTxt = d.findViewById(R.id.fiveTxt);

        if (seat.equals("1")) {
            oneTxt.setBackgroundResource(R.drawable.number_selected_bg);
            twoTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            threeTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            fourTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            fiveTxt.setBackgroundResource(R.drawable.number_unselected_bg);
        } else if (seat.equals("2")) {
            oneTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            twoTxt.setBackgroundResource(R.drawable.number_selected_bg);
            threeTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            fourTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            fiveTxt.setBackgroundResource(R.drawable.number_unselected_bg);
        } else if (seat.equals("3")) {
            oneTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            twoTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            threeTxt.setBackgroundResource(R.drawable.number_selected_bg);
            fourTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            fiveTxt.setBackgroundResource(R.drawable.number_unselected_bg);
        } else if (seat.equals("4")) {
            oneTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            twoTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            threeTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            fourTxt.setBackgroundResource(R.drawable.number_selected_bg);
            fiveTxt.setBackgroundResource(R.drawable.number_unselected_bg);
        } else if (seat.equals("5")) {
            oneTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            twoTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            threeTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            fourTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            fiveTxt.setBackgroundResource(R.drawable.number_selected_bg);
        }

        oneTxt.setOnClickListener(v -> {
            seat = "1";
            oneTxt.setBackgroundResource(R.drawable.number_selected_bg);
            twoTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            threeTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            fourTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            fiveTxt.setBackgroundResource(R.drawable.number_unselected_bg);
        });
        twoTxt.setOnClickListener(v -> {
            seat = "2";
            oneTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            twoTxt.setBackgroundResource(R.drawable.number_selected_bg);
            threeTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            fourTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            fiveTxt.setBackgroundResource(R.drawable.number_unselected_bg);
        });
        threeTxt.setOnClickListener(v -> {
            seat = "3";
            oneTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            twoTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            threeTxt.setBackgroundResource(R.drawable.number_selected_bg);
            fourTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            fiveTxt.setBackgroundResource(R.drawable.number_unselected_bg);
        });
        fourTxt.setOnClickListener(v -> {
            seat = "4";
            oneTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            twoTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            threeTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            fourTxt.setBackgroundResource(R.drawable.number_selected_bg);
            fiveTxt.setBackgroundResource(R.drawable.number_unselected_bg);
        });
        fiveTxt.setOnClickListener(v -> {
            seat = "5";
            oneTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            twoTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            threeTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            fourTxt.setBackgroundResource(R.drawable.number_unselected_bg);
            fiveTxt.setBackgroundResource(R.drawable.number_selected_bg);
        });


        if(data!=null){
            if(data.size()>0){
                if(data.get(0).getType().equals("two_wheeler")){
                    oneTxt.setBackgroundResource(R.drawable.number_selected_bg);
                    twoTxt.setBackgroundResource(R.drawable.number_unselected_bg);
                    threeTxt.setBackgroundResource(R.drawable.number_unselected_bg);
                    fourTxt.setBackgroundResource(R.drawable.number_unselected_bg);
                    fiveTxt.setBackgroundResource(R.drawable.number_unselected_bg);
                    twoTxt.setVisibility(View.GONE);
                    threeTxt.setVisibility(View.GONE);
                    fourTxt.setVisibility(View.GONE);
                    fiveTxt.setVisibility(View.GONE);
                    seat = "1";
                    etkm.setText("" + data.get(0).getRatePerKm());
                }
            }
            pagerAdapter = new VehiclePagerAdapter(getContext(), data);
            vehiclePager.setAdapter(pagerAdapter);
            indicator.setViewPager(vehiclePager);
            vehiclePager.setCurrentItem(PagerPosition);
            vehiclePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(final int i) {
                    if(data.get(i).getType().equals("two_wheeler")){
                        oneTxt.setBackgroundResource(R.drawable.number_selected_bg);
                        twoTxt.setBackgroundResource(R.drawable.number_unselected_bg);
                        threeTxt.setBackgroundResource(R.drawable.number_unselected_bg);
                        fourTxt.setBackgroundResource(R.drawable.number_unselected_bg);
                        fiveTxt.setBackgroundResource(R.drawable.number_unselected_bg);
                        twoTxt.setVisibility(View.GONE);
                        threeTxt.setVisibility(View.GONE);
                        fourTxt.setVisibility(View.GONE);
                        fiveTxt.setVisibility(View.GONE);
                        seat = "1";
                    }else {
                        twoTxt.setVisibility(View.VISIBLE);
                        threeTxt.setVisibility(View.VISIBLE);
                        fourTxt.setVisibility(View.VISIBLE);
                        fiveTxt.setVisibility(View.VISIBLE);
                    }
                    PagerPosition = i;
                    etkm.setText("" + data.get(i).getRatePerKm());
                    rate_per_km = data.get(i).getRatePerKm();
                }

                @Override
                public void onPageScrollStateChanged(int i) {
                }
            });
        }

        if (buttonLift.getText().toString().equalsIgnoreCase(getResources().getString(R.string.find_lift))) {
            vehicleLayout.setVisibility(View.GONE);
            llrate.setVisibility(View.GONE);
            twoTxt.setVisibility(View.VISIBLE);
            threeTxt.setVisibility(View.VISIBLE);
            fourTxt.setVisibility(View.VISIBLE);
            fiveTxt.setVisibility(View.VISIBLE);
        } else {
            vehicleLayout.setVisibility(View.VISIBLE);
            llrate.setVisibility(View.VISIBLE);
        }

        b1.setOnClickListener(v -> {
            textViewSelectSeat.setText(seat + " Seat");
            if (buttonLift.getText().toString().equalsIgnoreCase(getResources().getString(R.string.find_lift))) {
                textViewSelectSeat.setText(seat + " Seats");
            } else {
                rate_per_km =  Integer.parseInt(etkm.getText().toString().trim());
                textViewSelectSeat.setText("" + data.get(PagerPosition).getModel() + " | " + rate_per_km + "/km" + " | " + seat + " Seats");
            }
            d.dismiss();
        });
        d.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        d.show();
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        textViewSelectSeat.setText(newVal + " Seat");
        seat = String.valueOf(newVal);
    }

    @Override
    public void onclick(int s) {

    }

    @Override
    public void onDeleteClick(CheckPoints s) {
        checkPointsList.remove(s);
        checkPointsListAdapter.notifyDataSetChanged();
        if (checkPointsList.size() == 0) {
            layoutDropLocation.setVisibility(View.VISIBLE);
            recyclerViewCheckpoints.setVisibility(View.GONE);
            isMultiCheck = true;
        }
    }

    @Override
    public void setFindRideData(FindLiftResponse findRideData) {
        showMessage(findRideData.getMessage());
        showDialogFindLift(findRideData);
        textViewSelectSeat.setText("Select Seat");
        textViewSelectDateTime.setText("Select Time");
    }

    List<Datum> data;
    String vehicle_name;
    int rate_per_km;

    @Override
    public void setVehicle(List<Datum> data) {
        if (data.size() > 0) {
            this.data = data;
            isOfferLift = true;
            vehicle_name = data.get(0).getModel();
            rate_per_km = data.get(0).getRatePerKm();
            if (buttonLift.getText().toString().equalsIgnoreCase(getResources().getString(R.string.find_lift))) {
                textViewSelectSeat.setText(seat+" Seats");
            } else {
                textViewSelectSeat.setText("" + vehicle_name + " | " + rate_per_km+"/km" + " | " + seat+" Seats");
            }
        } else {
            showMessage("No Vehicle find.Please Add Your Vehicle");
            presenter.openMyVehicle();
        }
    }

    @Override
    public void setCreateRideData(CreateLiftResponse createRideData) {
        checkPointsList.clear();
        wayPoints = "";
        showDialogCreateLift(createRideData.getMessage(), createRideData.getSubMessage());
        layoutRide.setVisibility(View.VISIBLE);
        textViewSelectSeat.setText("Select Seat");
        textViewSelectDateTime.setText("Select Time");
        layoutDropLocation.setVisibility(View.VISIBLE);
        llchkpoint.setVisibility(View.VISIBLE);
        recyclerViewCheckpoints.setVisibility(View.GONE);
    }

    @Override
    public void setOnGoingData(InnerGoingResponse onGoingData) {
        if(onGoingData.getLifts().size()>0){
            Constants.isLiftOnGoing = true;
//            layoutRide.setVisibility(View.GONE);
        }
    }

    @Override
    public void onclickVehicle(Datum s) {
        List<String> data = new ArrayList<>();
        String listString = "";
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < checkPointsList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            if (!checkPointsList.get(i).getAddress().contains("Select Checkpoints")) {
                try {
                    jsonObject.put("LatLng", checkPointsList.get(i).getLat() + "," + checkPointsList.get(i).getLongi());
                    jsonObject.put("country", checkPointsList.get(i).getCountry());
                    jsonObject.put("state", checkPointsList.get(i).getState());
                    jsonObject.put("city", checkPointsList.get(i).getCity());
                    jsonObject.put("location", checkPointsList.get(i).getAddress());
                    jsonObject.put("date", "");
                    jsonObject.put("time", "");
                    jsonArray.put(jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                listString = String.join(", ", data);
            }
        }
        presenter.createLift(sharedPreferences.getString(Constants.TOKEN, ""), s.getId().toString(), "paid", "0", seat, startPoint, endPoint, jsonArray.toString(), dateTime, liftTime, textkm.getText().toString(), "" + rate_per_km);
    }

    @Override
    public void setCallBackSelectionCheckPoints(int preferredCallingMode) {
        locationSelect = 3;
        listPos = preferredCallingMode;
        Intent i1 = new Intent(getActivity(), LocationPickerActivity.class);
        startActivityForResult(i1, ADDRESS_PICKER_REQUEST);
    }

    @Override
    public void setCallBackSelectionCheckPointsDelete(int preferredCallingMode) {
        textViewCheckpoints.setText("Checkpoints :" + preferredCallingMode);
    }

    @Override
    public void getRemainingList(List<CheckPoints> groupLists) {
        refreshGoogleMap((ArrayList<CheckPoints>) groupLists);
    }

    @Override
    public void onDeleteCheckList(int position) {
        if (checkPointsList.size() > 0) {
            checkPointsList.remove(position);
            refreshGoogleMap((ArrayList<CheckPoints>) checkPointsList);
        }
    }


    public void refreshGoogleMap(ArrayList<CheckPoints> list) {

        int checkPointsCount = 0;
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getAddress().contains("Select Checkpoints"))
                checkPointsCount++;
        }
        textViewCheckpoints.setText("Checkpoints :" + checkPointsCount);
        wayPoints = "";
        mGoogleMap.clear();
        if (bottomSheetCheckPointsDialog != null) {
            bottomSheetCheckPointsDialog.dismiss();
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                if (!list.get(i).getAddress().contains("Select Checkpoints")) {
                    builder.append("=" + list.get(0).getLat() + "," + list.get(0).getLongi());
                    wayPoints = builder.toString();
                }
            } else {
                if (i != list.size()) {
                    if (!list.get(i).getAddress().contains("Select Checkpoints")) {
                        builder.append("|" + list.get(i).getLat() + "," + list.get(i).getLongi());
                    }
                } else {
                    if (!list.get(i).getAddress().contains("Select Checkpoints")) {
                        builder.append("" + list.get(i).getLat() + "," + list.get(i).getLongi());
                    }
                }
            }

            mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(list.get(i).getLat(), list.get(i).getLongi()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.drop_location))
                    .title(list.get(i).getCity()));

        }
        wayPoints = builder.toString();
        Log.e("Way Points", wayPoints);


        mGoogleMap.addMarker(new MarkerOptions()
                .position(originLat)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pic_location))
                .title("pickup"));

        mGoogleMap.addMarker(new MarkerOptions()
                .position(destinationLat)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.drop_location))
                .title("dropoff"));

        String source = "" + originLat.latitude + "," + originLat.longitude;
        String destination = "" + destinationLat.latitude + "," + destinationLat.longitude;
        Log.e("Origin ", "" + source + "\n Destination " + destination);
        new GetDirection().execute(source, destination);
    }

    class GetDirection extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            String stringUrl = "";
            distanceString = "";
            if (!wayPoints.equals("")) {
                stringUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=" + args[0] + "&destination=" + args[1] + "&waypoints" + wayPoints + "&key=" + getResources().getString(R.string.maps_api_key) + "&sensor=false";
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
                pontos = decodePoly(polyline);


                JSONArray legs = route.getJSONArray("legs");
                JSONObject steps;
                JSONObject distance = null;
                Float totalDistance = 0f;
                for (int i = 0; i < legs.length(); i++) {
                    steps = legs.getJSONObject(i);
                    distance = steps.getJSONObject("distance");
                    String[] total = distance.getString("text").split(" ");
                    totalDistance += Float.parseFloat(total[0]);
                }
                distanceString = "" + totalDistance + " Km";
                Log.e("Total Distance : ", "" + distanceString);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            LatLng src1 = null;
            LatLng dest = null;
            for (int i = 0; i < pontos.size() - 1; i++) {
                Log.e("call poly ", "loop = " + i);
                LatLng src = pontos.get(i);
                if (i == 0) {
                    src1 = src;
                }
                dest = pontos.get(i + 1);
                try {
                    polyline = mGoogleMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(src.latitude, src.longitude),
                                    new LatLng(dest.latitude, dest.longitude))
                            .width(7).color(Color.GREEN).geodesic(true));
                } catch (NullPointerException e) {
                    Log.e("Error", "NullPointerException onPostExecute: " + e.toString());
                } catch (Exception e2) {
                    Log.e("Error", "Exception onPostExecute: " + e2.toString());
                }
            }
            try {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(src1);
                builder.include(dest);
                LatLngBounds bounds = builder.build();
//                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                int padding = 250; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
                mGoogleMap.moveCamera(cu);
                getActivity().runOnUiThread(() -> textkm.setText("" + distanceString));

            } catch (Exception e) {

            }

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


    private void showDialogFindLift(FindLiftResponse findRideData) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(findRideData.getMessage());
        alertDialogBuilder.setMessage(findRideData.getSubMessage())
                .setCancelable(false);
        if (findRideData.getMatchesCount() != null && findRideData.getMatchesCount() > 0) {
            alertDialogBuilder.setPositiveButton("Check",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getActivity(), MatchingRideActivity.class);
                            intent.putExtra(Constants.LIFT_ID, findRideData.getLiftId());
                            getActivity().startActivity(intent);
                            getActivity().finish();
                            if (mGoogleMap != null) {
                                mGoogleMap.clear();
                            }
                        }
                    });
            alertDialogBuilder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });

        } else {
            alertDialogBuilder.setNegativeButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                ((HomeActivity) getActivity()).openride();
                                if (mGoogleMap != null) {
                                    mGoogleMap.clear();
                                }
                            } catch (Exception E) {

                            }
                            dialog.cancel();

                        }
                    });
        }


        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void showDialogCreateLift(String msg, String subMessage) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(msg);
        alertDialogBuilder.setMessage(subMessage)
                .setCancelable(false);
        alertDialogBuilder.setNegativeButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            ((HomeActivity) getActivity()).openride();
                            if (polyline != null) {
                                polyline.remove();
                            }
                            if (mGoogleMap != null) {
                                mGoogleMap.clear();
                            }
                        } catch (Exception E) {

                        }
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
