package com.liftPlzz.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.liftPlzz.R;
import com.liftPlzz.adapter.MatchingRideAdapter;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.model.matchingridemodel.MatchingRideByCategoryResponse;
import com.liftPlzz.model.matchingridemodel.MatchingRideResponse;
import com.liftPlzz.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchingRideActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, MatchingRideAdapter.ItemListener {

    @BindView(R.id.recycler_matching)
    RecyclerView recyclerViewMatching;
    @BindView(R.id.tv_start_point)
    AppCompatTextView tvStartPoint;
    @BindView(R.id.tv_end_point)
    AppCompatTextView tvEndPoint;
    List<LatLng> pontos = new ArrayList<>();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.layout_matching)
    LinearLayout linearLayout;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFragment;
    String origin;
    String destination, strToken = "";
    private int liftId;
    private MatchingRideAdapter matchingRideAdapter;
    private ArrayList<MatchingRideResponse> arrayList = new ArrayList<>();
    SharedPreferences sharedPreferences;
    private boolean isFind = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_matching_ride);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            liftId = getIntent().getIntExtra(Constants.LIFT_ID, -1);
            isFind = getIntent().getBooleanExtra(Constants.IS_FIND_LIFT, true);

        }
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        toolBarTitle.setText(getResources().getString(R.string.driver_list));
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_matching);
        matchingRideAdapter = new MatchingRideAdapter(this, arrayList, MatchingRideActivity.this);
        recyclerViewMatching.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMatching.setAdapter(matchingRideAdapter);
        getMachingRide();
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
            String stringUrl = "https://maps.googleapis.com/maps/api/directions/json?origin=" + origin + "&destination=" + destination + "&key=" + getResources().getString(R.string.maps_api_key) + "&sensor=false";
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

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            for (int i = 0; i < pontos.size() - 1; i++) {
                LatLng src = pontos.get(i);
                LatLng dest = pontos.get(i + 1);
                try {
                    //here is where it will draw the polyline in your map
                    Polyline line = mGoogleMap.addPolyline(new PolylineOptions()
                            .add(new LatLng(src.latitude, src.longitude),
                                    new LatLng(dest.latitude, dest.longitude))
                            .width(7).color(Color.GREEN).geodesic(true));
                    mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                } catch (NullPointerException e) {
                    Log.e("Error", "NullPointerException onPostExecute: " + e.toString());
                } catch (Exception e2) {
                    Log.e("Error", "Exception onPostExecute: " + e2.toString());
                }

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

    public void getMachingRide() {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<MatchingRideByCategoryResponse> call = api.getMatchingRideByCategory(Constants.API_KEY, getResources().getString(R.string.android), strToken, liftId);
        call.enqueue(new Callback<MatchingRideByCategoryResponse>() {
            @Override
            public void onResponse(Call<MatchingRideByCategoryResponse> call, Response<MatchingRideByCategoryResponse> response) {
                Constants.hideLoader();
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        tvStartPoint.setText(response.body().getData().getAddress().getStartLocation());
                        tvEndPoint.setText(response.body().getData().getAddress().getEndLocation());
                        arrayList.clear();
                        arrayList.addAll(response.body().getData().getVehicleList());
                        matchingRideAdapter.notifyDataSetChanged();
                    } else {
                        Constants.showMessage(MatchingRideActivity.this, response.body().getMessage(), linearLayout);
                    }
                }
            }

            @Override
            public void onFailure(Call<MatchingRideByCategoryResponse> call, Throwable throwable) {
                Constants.hideLoader();
                Constants.showMessage(MatchingRideActivity.this, throwable.getMessage(), linearLayout);
            }
        });

    }

    @Override
    public void onCategoryClick(MatchingRideResponse matchingRideResponse) {
        Intent intent = new Intent(MatchingRideActivity.this, DriverListActivity.class);
        intent.putExtra(Constants.IS_FIND_LIFT, isFind);
        intent.putExtra(Constants.LIFT_ID, liftId);
        intent.putExtra(Constants.VEHICLE_TYPE, matchingRideResponse.getType());
        intent.putExtra(Constants.SUB_CATEGORY_ID, matchingRideResponse.getId());
        startActivity(intent);
    }


    @OnClick({R.id.imageViewBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                onBackPressed();
                break;
        }
    }
}