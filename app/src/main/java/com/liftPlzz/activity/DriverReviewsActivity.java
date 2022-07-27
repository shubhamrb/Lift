package com.liftPlzz.activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.adapter.ReviewListAdapter;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.model.getVehicle.getReview.Datum;
import com.liftPlzz.model.getVehicle.getReview.GetReviewMainResponse;
import com.liftPlzz.model.getVehicle.getReview.Response;
import com.liftPlzz.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

public class DriverReviewsActivity extends AppCompatActivity implements ReviewListAdapter.ItemListener {

    SharedPreferences sharedPreferences;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    private String strToken, user_id;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    @BindView(R.id.recyclerViewReview)
    RecyclerView recyclerViewReview;
    @BindView(R.id.totalRating)
    RatingBar totalRating;
    @BindView(R.id.textTotalRating)
    AppCompatTextView textTotalRating;
    @BindView(R.id.textTotalOne)
    AppCompatTextView textTotalOne;
    @BindView(R.id.textTotalTwo)
    AppCompatTextView textTotalTwo;
    @BindView(R.id.textTotalThree)
    AppCompatTextView textTotalThree;
    @BindView(R.id.textTotalFour)
    AppCompatTextView textTotalFour;
    @BindView(R.id.textTotalFive)
    AppCompatTextView textTotalFive;

    @BindView(R.id.progressOne)
    ProgressBar progressOne;
    @BindView(R.id.progressTwo)
    ProgressBar progressTwo;
    @BindView(R.id.progressThree)
    ProgressBar progressThree;
    @BindView(R.id.progressFour)
    ProgressBar progressFour;
    @BindView(R.id.progressFive)
    ProgressBar progressFive;
    @BindView(R.id.filter_spinner)
    Spinner filter_spinner;

    String filter_type = "";
    private List<String> spinnerArray;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reviews);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        user_id = getIntent().getStringExtra(Constants.USER_ID);
        ButterKnife.bind(this);
        toolBarTitle.setText("All Reviews");
        spinnerArray = new ArrayList<>();
        spinnerArray.add("Select");
        adapter = new ArrayAdapter<String>(
                this, R.layout.custom_spinner, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filter_spinner.setAdapter(adapter);

        filter_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filter_type = spinnerArray.get(i).toLowerCase().replace(" ", "_");
                if (i != 0) {
                    getReviews();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        getReviews();
    }


    private void getReviews() {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<GetReviewMainResponse> call = api.get_review(Constants.API_KEY, "android", strToken, user_id, filter_type);
        call.enqueue(new Callback<GetReviewMainResponse>() {
            @Override
            public void onResponse(Call<GetReviewMainResponse> call, retrofit2.Response<GetReviewMainResponse> response) {
                Constants.hideLoader();

                if (response.body() != null) {
                    if (response.code() == 200) {
                        setReviewData(response.body().getResponse());
                    } else {
                        Toast.makeText(DriverReviewsActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
            }


            @Override
            public void onFailure(Call<GetReviewMainResponse> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(DriverReviewsActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.imageViewBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                onBackPressed();
                break;
        }
    }

    public void setReviewData(Response response) {
        try {
            if (response != null && response.getStatus()) {
                if (response.getOther_details() != null) {
                    totalRating.setRating(Float.parseFloat(response.getOther_details().getRating()));
                    textTotalRating.setText(response.getOther_details().getRating() + " rating and " + response.getOther_details().getTotal_review() + " reviews");

                    if (response.getOther_details().getTotal_review()>0){
                        progressOne.setProgress((response.getOther_details().getDetail().getOne_() * 100) / response.getOther_details().getTotal_review());
                        progressTwo.setProgress((response.getOther_details().getDetail().getTwo_() * 100) / response.getOther_details().getTotal_review());
                        progressThree.setProgress((response.getOther_details().getDetail().getThree_() * 100) / response.getOther_details().getTotal_review());
                        progressFour.setProgress((response.getOther_details().getDetail().getFour_() * 100) / response.getOther_details().getTotal_review());
                        progressFive.setProgress((response.getOther_details().getDetail().getFive_() * 100) / response.getOther_details().getTotal_review());
                    }

                    textTotalOne.setText(response.getOther_details().getDetail().getOne_() + "");
                    textTotalTwo.setText(response.getOther_details().getDetail().getTwo_() + "");
                    textTotalThree.setText(response.getOther_details().getDetail().getThree_() + "");
                    textTotalFour.setText(response.getOther_details().getDetail().getFour_() + "");
                    textTotalFive.setText(response.getOther_details().getDetail().getFive_() + "");
                }

                spinnerArray.clear();
                spinnerArray.add("Select");
                if (response.getFilter_type() != null && response.getFilter_type().size() > 0) {
                    spinnerArray.addAll(response.getFilter_type());
                }
                adapter.notifyDataSetChanged();
                recyclerViewReview.setLayoutManager(new LinearLayoutManager(this));

                List<Datum> list = new ArrayList<>();
                if (response.getData() != null && response.getData().size() > 0) {
                    list.addAll(response.getData());
                }
                recyclerViewReview.setAdapter(new ReviewListAdapter(this, list, this));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLikeClick(int review_id) {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<GetReviewMainResponse> call = api.like_review(Constants.API_KEY, "android", strToken, review_id);
        call.enqueue(new Callback<GetReviewMainResponse>() {
            @Override
            public void onResponse(Call<GetReviewMainResponse> call, retrofit2.Response<GetReviewMainResponse> response) {
                Constants.hideLoader();

                if (response.body() != null) {
                    if (response.code() == 200) {
                        onSuccessLikeDislike(response.body().getResponse());
                    } else {
                        //  view.hideLoader();
                        Toast.makeText(DriverReviewsActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
            }


            @Override
            public void onFailure(Call<GetReviewMainResponse> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(DriverReviewsActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDislikeClick(int review_id) {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<GetReviewMainResponse> call = api.dislike_review(Constants.API_KEY, "android", strToken, review_id);
        call.enqueue(new Callback<GetReviewMainResponse>() {
            @Override
            public void onResponse(Call<GetReviewMainResponse> call, retrofit2.Response<GetReviewMainResponse> response) {
                Constants.hideLoader();

                if (response.body() != null) {
                    if (response.code() == 200) {
                        onSuccessLikeDislike(response.body().getResponse());
                    } else {
                        //  view.hideLoader();
                        Toast.makeText(DriverReviewsActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
            }


            @Override
            public void onFailure(Call<GetReviewMainResponse> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(DriverReviewsActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onSuccessLikeDislike(Response response) {
        if (response != null) {
            Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            getReviews();
        }
    }
}
