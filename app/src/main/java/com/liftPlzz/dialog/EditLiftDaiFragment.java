package com.liftPlzz.dialog;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
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
import com.google.gson.Gson;
import com.liftPlzz.R;
import com.liftPlzz.activity.HomeActivity;
import com.liftPlzz.activity.MatchingRideActivity;
import com.liftPlzz.adapter.CheckPointsListAdapter;
import com.liftPlzz.adapter.MyVehicleListRideAdapter;
import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.base.BaseDailogFragment;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.fragments.MyRidesFragment;
import com.liftPlzz.model.CheckPoints;
import com.liftPlzz.model.FindLiftResponse;
import com.liftPlzz.model.createLift.CreateLiftResponse;
import com.liftPlzz.model.editlift.EditVehicleData;
import com.liftPlzz.model.editlift.LiftLocationModel;
import com.liftPlzz.model.getVehicle.Datum;
import com.liftPlzz.model.upcomingLift.Lift;
import com.liftPlzz.presenter.EditLiftPresenter;
import com.liftPlzz.presenter.HomePresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.EditLiftView;
import com.liftPlzz.views.HomeView;
import com.shivtechs.maplocationpicker.LocationPickerActivity;
import com.shivtechs.maplocationpicker.MapUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditLiftDaiFragment extends BaseDailogFragment<EditLiftPresenter, EditLiftView> implements EditLiftView,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, NumberPicker.OnValueChangeListener, CheckPointsListAdapter.ItemListener, MyVehicleListRideAdapter.ItemListener, BottomSheetCheckPointsDialog.CallBackSelectionCheckPoints {

    private static View view;
    private static final int ADDRESS_PICKER_REQUEST = 1;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.editTextPickupLocation)
    AppCompatTextView editTextPickupLocation;
    @BindView(R.id.textViewLiftName)
    AppCompatTextView textViewLiftName;


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
    @BindView(R.id.layoutRideVehicle)
    RelativeLayout layoutRideVehicle;
    int listPos;
    @BindView(R.id.recyclerViewMyVehicle)
    RecyclerView recyclerViewMyVehicle;
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
    String startPoint = "", endPoint = "";
    String destination;
    PolylineOptions polylineOptions = new PolylineOptions();
    List<CheckPoints> checkPointsList = new ArrayList<>();
    CheckPointsListAdapter checkPointsListAdapter;
    boolean isMultiCheck = false;
    String seat = "1";
    String dateTime, liftTime = "";


    @BindView(R.id.textViewCheckpoints)
    AppCompatTextView textViewCheckpoints;

    @BindView(R.id.textkm)
    AppCompatTextView textkm;

    @BindView(R.id.etkm)
    EditText etkm;

    @BindView(R.id.btnSubmit)
    AppCompatButton btnSubmit;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;

    MyVehicleListRideAdapter myVehicleListRideAdapter;


    //
    LatLng latLngOrigin, latLngDestination;

    SharedPreferences sharedPreferences;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFragment;
    GoogleApiClient mGoogleApiClient;
    BottomSheetCheckPointsDialog bottomSheetCheckPointsDialog;

    Calendar calendar;
    int day, month, year, hour, minute;
    int myday, myMonth, myYear, myHour, myMinute;


    ///////
    private Lift lift;

    /* @Override
     protected View createLayout() {
         return null;//R.layout.fragment_home;
     }*/
    private EditVehicleData liftdata;
    String action = "";
    private UpdateRecordListiner updateRecordListiner;

    public EditLiftDaiFragment(String action) {
        this.action = action;
    }

    public void setLift(Lift lift, UpdateRecordListiner updateRecordListiner, String action) {
        this.lift = lift;
        this.updateRecordListiner = updateRecordListiner;
        this.action = action;
    }

    public interface UpdateRecordListiner {
        void done();
    }

    private void draw_check_point_from_api(EditVehicleData data) {
        Log.e("call else ", "both else");
        ArrayList<LiftLocationModel> list = new ArrayList<>();
        if (data.getCheck_point().size() > 0) {
            for (int i = 0; i < data.getCheck_point().size(); i++) {
                list.add(data.getCheck_point().get(i));

                CheckPoints checkPoints = new CheckPoints();
                checkPoints.setId(checkPointsList.size() + 1);
                checkPoints.setAddress("Select Checkpoints " + (checkPointsList.size() + 1));
                checkPointsList.add(checkPoints);

                checkPointsList.get(listPos).setAddress(new StringBuilder().append
                        (list.get(i).getCountry()).append
                        (list.get(i).getCity()).append(",").append
                        (list.get(i).getState()).toString());

                double currentLatitude = Double.parseDouble(list.get(i).getLatLng().split(",")[0]);//data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                double currentLongitude = Double.parseDouble(list.get(i).getLatLng().split(",")[1]);
                LatLng dropLocation = new LatLng(currentLatitude, currentLongitude);
                LatLng latLngDestination = new LatLng(currentLatitude, currentLongitude);
                checkPointsList.get(listPos).setLat(currentLatitude);
                checkPointsList.get(listPos).setCity(list.get(i).getCity());
                checkPointsList.get(listPos).setState(list.get(i).getState());
                checkPointsList.get(listPos).setCountry(list.get(i).getCity());
                checkPointsList.get(listPos).setLongi(currentLongitude);
//        bottomSheetCheckPointsDialog.nofityAdapter();
                String origin = "";
                if (listPos > 0) {
                    origin = checkPointsList.get(listPos - 1).getLat() + "," + checkPointsList.get(listPos - 1).getLongi();
                } else {
                    origin = destination;
                }
                String destination = currentLatitude + "," + currentLongitude;
                mGoogleMap.addMarker(new MarkerOptions()
                        .position(dropLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.drop_location))
                        .title("Marker in Sydney"));
//                        origin = pickupLocation.latitude + "," + pickupLocation.longitude;
//                        destination = dropLocation.latitude + "," + dropLocation.longitude;
//                        new GetDirection().execute();

                textViewCheckpoints.setText("Checkpoints :" + checkPointsList.size());
                listPos++;
            }
        }
    }

    public void setStarLift(EditVehicleData data) {
        liftdata = data;
        vehicle_id=data.getLift().getVehicleId();
        double currentLatitude = Double.parseDouble(data.getStart_point().getLatLng().split(",")[0]);//data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
        double currentLongitude = Double.parseDouble(data.getStart_point().getLatLng().split(",")[1]);
        pickupLocation = new LatLng(currentLatitude, currentLongitude);
        latLngOrigin = new LatLng(currentLatitude, currentLongitude);

        StringBuilder strAdd = new StringBuilder().append
                (data.getStart_point().getLocation());
        Log.e("result ", "" + strAdd.toString() + "  ");
        editTextPickupLocation.setText(strAdd);
        Bundle completeAddress = new Bundle();

        completeAddress.putString("country", "" + data.getStart_point().getCountry());
        completeAddress.putString("state", "" + data.getStart_point().getState());
        completeAddress.putString("city", "" + data.getStart_point().getCity());

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

        textViewLiftName.setText("" + (data.getLift().getLiftType().equals("offer") ? "Offer Lift" : "Find Lift"));
        if(data.getLift().getLiftType().equals("offer")){
            llchkpoint.setVisibility(View.VISIBLE);
        }else{
            llchkpoint.setVisibility(View.INVISIBLE);

        }
        setTimeEdit(data);
    }

    public void setEndLiftLoc(EditVehicleData data) {

        StringBuilder stringBuilder = new StringBuilder().append(data.getEnd_point().getLocation());
        editTextDropLocation.setText(stringBuilder.toString());


        double currentLatitude = Double.parseDouble(data.getEnd_point().getLatLng().split(",")[0]);//data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
        double currentLongitude = Double.parseDouble(data.getEnd_point().getLatLng().split(",")[1]);
        dropLocation = new LatLng(currentLatitude, currentLongitude);
        latLngDestination = new LatLng(currentLatitude, currentLongitude);
        Bundle completeAddress = new Bundle();

        completeAddress.putString("country", "" + data.getEnd_point().getCountry());
        completeAddress.putString("state", "" + data.getEnd_point().getState());
        completeAddress.putString("city", "" + data.getEnd_point().getCity());


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

        }
    }

    public void setTimeEdit(EditVehicleData data) {
        myHour = Integer.parseInt(data.getStart_point().getTime().split(":")[0]);
        myMinute = Integer.parseInt(data.getStart_point().getTime().split(":")[1]);
        ;
        Calendar myCalender = Calendar.getInstance();
        myYear = Integer.parseInt(data.getStart_point().getDate().split("-")[0]);
        myMonth = Integer.parseInt(data.getStart_point().getDate().split("-")[1]);
        myday = Integer.parseInt(data.getStart_point().getDate().split("-")[2]);
        myCalender.set(myYear, myMonth - 1, myday, myHour, myMinute);
        dateTime = new SimpleDateFormat("yyyy-MM-dd").format(myCalender.getTime());
        liftTime = new SimpleDateFormat("HH:mm:ss").format(myCalender.getTime());

        textViewSelectDateTime.setText(new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(myCalender.getTime()));

        textViewSelectSeat.setText("" + data.getLift().getPaidSeats() + " Seat");
        seat = String.valueOf(data.getLift().getPaidSeats());
    }

    @Override
    protected View createLayout(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
       /* if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_edit_lift, container, false);
        } catch (InflateException e) {
            *//* map is already there, just return view as it is *//*
        }*/
//      imageViewBack=(ImageView) view.findViewById(R.id.imageViewBack);
//        imageViewBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
        return inflater.inflate(R.layout.fragment_edit_lift, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            SupportMapFragment f = (SupportMapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            if (f != null)
                getFragmentManager().beginTransaction().remove(f).commit();
        } catch (Exception e) {

        }

    }


    @Override
    protected void setPresenter() {
        presenter = new EditLiftPresenter();
    }


    @Override
    protected EditLiftView createView() {
        return this;
    }

    @Override
    protected void bindData() {
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
        mapFragment = (SupportMapFragment) getFragmentManager()
                .findFragmentById(R.id.map);

        bottomSheetCheckPointsDialog = new BottomSheetCheckPointsDialog();
        bottomSheetCheckPointsDialog.setSelectionListner(EditLiftDaiFragment.this);

       /* FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("gfggf", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        presenter.updateToken(sharedPreferences.getString(Constants.TOKEN, ""), token);
                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d("hfhfh", msg);
                    }
                });*/
        // mapFragment.getMapAsync(this);

        // casetFindRideDatall api for get lift detail
        presenter.getEditVehicle(sharedPreferences.getString(Constants.TOKEN, ""), "" + lift.getId());


    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @OnClick({R.id.buttonLift, R.id.btnSubmit, R.id.layoutCheckPoints, R.id.layoutGiveLift, R.id.layoutTakeLift, R.id.layoutSelectSeat, R.id.layoutSelectDateTime, R.id.layoutPickupLocation, R.id.layoutDropLocation, R.id.imageViewBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.buttonLift:
                //find
                if (liftdata.getLift().getLiftType().equals("offer")) {//offer
                    if (editTextPickupLocation.getText().toString().isEmpty()) {
                        showMessage("Select pickup location");
                    } else if (editTextDropLocation.getText().toString().isEmpty()) {
                        showMessage("Select dropoff location");
                    } else if (textViewSelectDateTime.getText().toString().equalsIgnoreCase("Select Time")) {
                        showMessage("Select Data Time");
                    } else if (textViewSelectSeat.getText().toString().equalsIgnoreCase("Select Seat")) {
                        showMessage("Select Seats");
                    } else {
                        if (action.equalsIgnoreCase("add")) {
                            presenter.getRepeatVehicle(sharedPreferences.getString(Constants.TOKEN, ""),textkm.getText().toString());
                        } else {
                            presenter.getVehicle(sharedPreferences.getString(Constants.TOKEN, ""),textkm.getText().toString());
                        }
                    }
                } else {

                    if (editTextPickupLocation.getText().toString().isEmpty()) {
                        showMessage("Select pickup location");
                    } else if (editTextDropLocation.getText().toString().isEmpty()) {
                        showMessage("Select dropoff location");
                    } else if (textViewSelectDateTime.getText().toString().equalsIgnoreCase("Select Time")) {
                        showMessage("Select Data Time");
                    } else if (textViewSelectSeat.getText().toString().equalsIgnoreCase("Select Seat")) {
                        showMessage("Select Seats");
                    } else {
                        // JAGNARAYAN
                        Log.e("call update find", "" + liftdata.getLift().getId());
                        if (action.equalsIgnoreCase("add")) {
                            presenter.repeatfindLift(sharedPreferences.getString(Constants.TOKEN, ""), "add ride", seat, startPoint, endPoint, dateTime, liftTime,textkm.getText().toString());
                        } else {
                            presenter.findLift(sharedPreferences.getString(Constants.TOKEN, ""), "test ride", seat, startPoint, endPoint, dateTime, "" + liftdata.getLift().getId(), liftTime,textkm.getText().toString());
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
                show();
                break;
                case R.id.imageViewBack:
                dismiss();
                break;
            case R.id.layoutSelectDateTime:
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), EditLiftDaiFragment.this, myYear, myMonth - 1, myday);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

                break;

            case R.id.btnSubmit:
                if (etkm.getText().toString().trim().length() > 0) {
                    onclickVehicle(myVehicleListRideAdapter.verifiedLists.get(myVehicleListRideAdapter.selectionposition));
                }
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
                isMultiCheck = true;
                layoutGiveLift.setBackground(getResources().getDrawable(R.drawable.rounded_bg));
                layoutTakeLift.setBackground(getResources().getDrawable(R.drawable.rounded_bg_white));
                buttonLift.setText(textViewGiveLift.getText().toString());
                textViewGiveLift.setTextColor(getResources().getColor(R.color.colorWhite));
                textViewTakeLift.setTextColor(getResources().getColor(R.color.colorBlack));
                llchkpoint.setVisibility(View.VISIBLE);
                break;
            case R.id.layoutTakeLift:
                isMultiCheck = false;
                layoutTakeLift.setBackground(getResources().getDrawable(R.drawable.rounded_bg));
                layoutGiveLift.setBackground(getResources().getDrawable(R.drawable.rounded_bg_white));
                buttonLift.setText(textViewTakeLift.getText().toString());
                textViewGiveLift.setTextColor(getResources().getColor(R.color.colorBlack));
                textViewTakeLift.setTextColor(getResources().getColor(R.color.colorWhite));
                llchkpoint.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocation = location;
                mapFragment.getMapAsync(EditLiftDaiFragment.this);
                //   Toast.makeText(getActivity(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();


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
        ///   jag
       /* latLngOrigin = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        origin = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        pickupLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        googleMap.addMarker(new MarkerOptions()
                .position(latLngOrigin)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pic_location))
                .title("First"));

        // [START_EXCLUDE silent]
        editTextPickupLocation.setText(getCompleteAddressString(currentLocation.getLatitude(), currentLocation.getLongitude()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOrigin, 15.0f));
        startPoint = getJsonObjectFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude());
*/
        // Zoom in the Google Map
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//        googleMap.moveCamera(new CameraUpdateFactory().newLatLngZoom(la));
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    /*    googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                googleMap.clear();
            }
        });
        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                MarkerOptions markerOptions = new MarkerOptions().position(googleMap.getCameraPosition().target)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pic_location));
                googleMap.addMarker(markerOptions);
                editTextPickupLocation.setText(getCompleteAddressString(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude));

            }
        });*/
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
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
        //remove location updates so that it resets
        //import should be **import com.google.android.gms.location.LocationListener**;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //restart location updates with the new interval
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
                    // String address = data.getStringExtra(MapUtility.ADDRESS);
                    double currentLatitude = data.getDoubleExtra(MapUtility.LATITUDE, 0.0);
                    double currentLongitude = data.getDoubleExtra(MapUtility.LONGITUDE, 0.0);
                    Bundle completeAddress = data.getBundleExtra("fullAddress");
                    /* data in completeAddress bundle
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

                        StringBuilder strAdd = new StringBuilder().append
                                (completeAddress.getString("addressline2")).append
                                (completeAddress.getString("city")).append(",").append
                                (completeAddress.getString("state"));
                        Log.e("result ", "" + strAdd.toString() + "  ");
                        editTextPickupLocation.setText(strAdd.toString());
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
                        Log.e("result 2 ", "" + stringBuilder.toString() + "  ");
                        editTextDropLocation.setText(stringBuilder.toString());
                        dropLocation = new LatLng(currentLatitude, currentLongitude);
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

                        }


                    } else {
                        Log.e("call else ", "both else");

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
                        if (listPos > 0) {
                            origin = checkPointsList.get(listPos - 1).getLat() + "," + checkPointsList.get(listPos - 1).getLongi();
                        } else {
                            origin = destination;
                        }
                        destination = currentLatitude + "," + currentLongitude;
                        mGoogleMap.addMarker(new MarkerOptions()
                                .position(dropLocation)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.drop_location))
                                .title("Marker in Sydney"));
//                        origin = pickupLocation.latitude + "," + pickupLocation.longitude;
//                        destination = dropLocation.latitude + "," + dropLocation.longitude;
//                        new GetDirection().execute();

                        textViewCheckpoints.setText("Checkpoints :" + checkPointsList.size());
                    }


                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void setPickupLocation() {

    }

    public void setDroupLocation() {

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
        hour = calendar.get(Calendar.HOUR);
        minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), EditLiftDaiFragment.this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        timePickerDialog.show();
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


    public void show() {

        final Dialog d = new Dialog(getActivity());
//        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dailog_seat);
        AppCompatTextView b1 = d.findViewById(R.id.button1);
//        Button b2 = d.findViewById(R.id.button2);
        final NumberPicker np = d.findViewById(R.id.numberPicker1);

        np.setMaxValue(5);
        np.setMinValue(1);
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(EditLiftDaiFragment.this);
        np.setValue(Integer.parseInt(seat));
        b1.setOnClickListener(v -> {
            textViewSelectSeat.setText(np.getValue() + " Seat");
            seat = String.valueOf(np.getValue());
            d.dismiss();
        });
//        b2.setOnClickListener(v -> d.dismiss());
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
    public void setFindRideData(FindLiftResponse findRideData,String action) {
        Log.e("setFindRideData", "" + new Gson().toJson(findRideData));
        showMessage(findRideData.getMessage());
        showDialogFindLift(findRideData,action);
        textViewSelectSeat.setText("Select Seat");
        textViewSelectDateTime.setText("Select Time");
    }

    @Override
    public void setVehicle(List<Datum> data,String action) {

            if (data.size() > 0) {
                layoutRideVehicle.setVisibility(View.VISIBLE);
                layoutRide.setVisibility(View.GONE);
                //etkm
                myVehicleListRideAdapter = new MyVehicleListRideAdapter(getContext(), data, EditLiftDaiFragment.this, etkm,vehicle_id);
                recyclerViewMyVehicle.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                recyclerViewMyVehicle.setAdapter(myVehicleListRideAdapter);
            } else {
                showMessage("No Vehicle find.Please Add Your Vehicle");
            }
    }

    @Override
    public void setCreateRideData(CreateLiftResponse createRideData,String ac) {
//        showMessage("Ride Request Send successfully");
        showDialogCreateLift(createRideData.getMessage(), createRideData.getSubMessage(),ac);
        layoutRideVehicle.setVisibility(View.GONE);
        layoutRide.setVisibility(View.VISIBLE);
        textViewSelectSeat.setText("Select Seat");
        textViewSelectDateTime.setText("Select Time");
        layoutDropLocation.setVisibility(View.VISIBLE);
        llchkpoint.setVisibility(View.VISIBLE);
        recyclerViewCheckpoints.setVisibility(View.GONE);
    }
    int vehicle_id;
    @Override
    public void getLiftDetail(EditVehicleData data) {
        Log.e("getLiftDetail", "" + new Gson().toJson(data));
        setStarLift(data);
        setEndLiftLoc(data);
        draw_check_point_from_api(data);
    }

    //  changes for offer
    @Override
    public void onclickVehicle(Datum s) {

        List<String> data = new ArrayList<>();
        String listString = "";
        JSONArray jsonArray = new JSONArray();
//        if (checkPointsList.size() > 1) {

        for (int i = 0; i < checkPointsList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
//                data.add(checkPointsList.get(i).getLat() + "," + checkPointsList.get(i).getLongi());
            try {
                jsonObject.put("LatLng", String.valueOf(checkPointsList.get(i).getLat()) + "," + String.valueOf(checkPointsList.get(i).getLongi()));
                jsonObject.put("country", checkPointsList.get(i).getCountry());
                jsonObject.put("state", checkPointsList.get(i).getState());
                jsonObject.put("city", checkPointsList.get(i).getCity());
                jsonObject.put("location", checkPointsList.get(i).getAddress());
                jsonObject.put("date", "");
                jsonObject.put("time", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
//            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                listString = String.join(", ", data);
            }
        }
//        presenter.createLift(sharedPreferences.getString(Constants.TOKEN, ""), s.getId().toString(), "paid", "0", seat, startPoint, endPoint, "{" + listString + "}", dateTime, liftTime);
       if(action.equalsIgnoreCase("add")){

           presenter.repeatoffercreateLift(sharedPreferences.getString(Constants.TOKEN, ""), s.getId().toString(), "paid", "0", seat, startPoint, endPoint, jsonArray.toString(), dateTime, liftTime, textkm.getText().toString(),etkm.getText().toString());

       }else {
           presenter.createLift(sharedPreferences.getString(Constants.TOKEN, ""), s.getId().toString(), "paid", "0", seat, startPoint, endPoint, jsonArray.toString(), dateTime , String.valueOf(liftdata.getLift().getId()),liftTime, textkm.getText().toString(),etkm.getText().toString());

       }
        // JAGNARAYAN
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

    class GetDirection extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {
            String stringUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=" + args[0] + "&destination=" + args[1] + "&key=" + getResources().getString(R.string.maps_api_key) + "&sensor=false";
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
                JSONObject steps = legs.getJSONObject(0);
                JSONObject distance = steps.getJSONObject("distance");
                distanceString = distance.getString("text");

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
                    //here is where it will draw the polyline in your map
                    Polyline line = mGoogleMap.addPolyline(new PolylineOptions()
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

                   /* builder.include(latLngOrigin);
                    builder.include(latLngDestination);*/

                LatLngBounds bounds = builder.build();
//                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                int padding = 250; // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
                mGoogleMap.moveCamera(cu);
            } catch (Exception e) {

            }
            textkm.setText("" + distanceString);
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


    private void showDialogFindLift(FindLiftResponse findRideData,String action) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(findRideData.getMessage());
        alertDialogBuilder.setMessage(findRideData.getSubMessage())
                .setCancelable(false);
        if (findRideData.getMatchesCount() != null && findRideData.getMatchesCount() > 0) {
            alertDialogBuilder.setPositiveButton("Check",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // jagnarayan
                            if(action.equalsIgnoreCase("add")){
                                ((HomeActivity)getActivity()).openride();
                                dismiss();
                            }else {
                                dismissDailog();
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
                                // jagnarayan
                                if(action.equalsIgnoreCase("add")){
                                    ((HomeActivity)getActivity()).openride();
                                    dismiss();
//                                    ((HomeActivity)getActivity()).onclick(2);

                                }else {
                                    dismissDailog();
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

    public void dismissDailog() {
        updateRecordListiner.done();
        dismiss();
    }

    private void showDialogCreateLift(String msg, String subMessage,String action) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(msg);
        alertDialogBuilder.setMessage(subMessage)
                .setCancelable(false);
        alertDialogBuilder.setNegativeButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            if(action.equalsIgnoreCase("add")){
                                ((HomeActivity)getActivity()).openride();
                                dismiss();

                            }else {
                                dismissDailog();
                            }
                            //  need for referesh data
                        } catch (Exception E) {

                        }
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


}
