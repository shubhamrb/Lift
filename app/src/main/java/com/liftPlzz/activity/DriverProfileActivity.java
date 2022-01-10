package com.liftPlzz.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.liftPlzz.R;
import com.liftPlzz.adapter.ReviewListAdapter;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.model.UserInfo.Review;
import com.liftPlzz.model.UserInfo.User;
import com.liftPlzz.model.UserInfo.UserInfoModel;
import com.liftPlzz.utils.Constants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverProfileActivity extends AppCompatActivity implements ReviewListAdapter.ItemListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.iv_user_image)
    AppCompatImageView ivUserImage;
    @BindView(R.id.tv_driver_name)
    AppCompatTextView tvDriverName;
    @BindView(R.id.tv_total_ride)
    AppCompatTextView tvTotalRide;
    @BindView(R.id.tv_as_rider)
    AppCompatTextView tvAsRider;
    @BindView(R.id.tv_as_seeker)
    AppCompatTextView tvAsSeeker;
    @BindView(R.id.tv_call)
    AppCompatTextView tvCall;
    @BindView(R.id.tv_chat)
    AppCompatTextView tvChat;
    @BindView(R.id.tv_driver_otp)
    AppCompatTextView tvDriverOtp;
    @BindView(R.id.tv_mobile)
    AppCompatTextView tvMobile;
    @BindView(R.id.tv_email)
    AppCompatTextView tvEmail;
    @BindView(R.id.driver_rating)
    AppCompatRatingBar ratingBar;
    @BindView(R.id.tv_rating_count)
    AppCompatTextView tvRatingCount;
    @BindView(R.id.recyclerReview)
    RecyclerView recyclerReview;
    private int userId;
    private User user;
    private String mobileNo = "";
    private ReviewListAdapter reviewListAdapter;
    private List<Review> reviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_driver_profile);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            userId = getIntent().getIntExtra(Constants.USER_ID, -1);
        }
        toolBarTitle.setText(getResources().getString(R.string.profile));

        getUserDetailsApi();
        reviewList = new ArrayList<>();
        recyclerReview.setLayoutManager(new LinearLayoutManager(this));
        reviewListAdapter = new ReviewListAdapter(this, DriverProfileActivity.this, reviewList);
        recyclerReview.setAdapter(reviewListAdapter);
    }


    public void getUserDetailsApi() {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<UserInfoModel> call = api.getUserDetails(Constants.API_KEY, getResources().getString(R.string.android), userId);
        call.enqueue(new Callback<UserInfoModel>() {
            @Override
            public void onResponse(Call<UserInfoModel> call, Response<UserInfoModel> response) {
                Constants.hideLoader();
                if (response.code() == 200) {
                    try {
                        String message = response.body().getResponse().getMessage();
                        if (response.body().getResponse().getStatus()) {
                            user = response.body().getResponse().getUser();
                            setUserData(user);
//                            Toast.makeText(DriverProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DriverProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.optString("message");
                        Toast.makeText(DriverProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(Call<UserInfoModel> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(DriverProfileActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void setUserData(User userData) {
        try {
            Glide.with(this).load(userData.getImage()).into(ivUserImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        reviewList.clear();
        reviewList.addAll(userData.getReviews());
        reviewListAdapter.notifyDataSetChanged();
        tvDriverName.setText(userData.getName());
//        tvDriverOtp.setText(getResources().getString(R.string.share_code) + "\n " + userData.getShareCode());
        if (userData.getSettings().getProfilePublicly() != null) {
            if (userData.getSettings().getProfilePublicly() == 0) {
                tvEmail.setVisibility(View.GONE);
                tvMobile.setVisibility(View.GONE);
            } else {
                tvEmail.setVisibility(View.VISIBLE);
                tvMobile.setVisibility(View.VISIBLE);
            }
        }
        tvEmail.setText(userData.getEmail());
        mobileNo = userData.getMobile();
        tvMobile.setText(mobileNo);

        if (userData.getSettings().getCall() == 1) {
            tvCall.setVisibility(View.VISIBLE);
        } else {
            tvCall.setVisibility(View.GONE);
        }
        if (userData.getSettings().getChat() == 1) {
            tvChat.setVisibility(View.VISIBLE);
        } else {
            tvChat.setVisibility(View.GONE);
        }
        ratingBar.setRating(Float.parseFloat(userData.getRating()));
        tvRatingCount.setText("" + userData.getTotalReview());
        tvAsRider.setText("" + userData.getLiftGiver());
        tvAsSeeker.setText("" + userData.getLiftTaker());
        tvTotalRide.setText("" + userData.getTotalLift());
    }

    @OnClick({R.id.imageViewBack, R.id.tv_call, R.id.tv_chat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                onBackPressed();
                break;

            case R.id.tv_call:
                if (!mobileNo.equalsIgnoreCase("")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", mobileNo, null));
                    startActivity(intent);
                }
                break;

            case R.id.tv_chat:
//                Toast.makeText(DriverProfileActivity.this, "Coming soon....", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DriverProfileActivity.this, ChatActivity.class);
                intent.putExtra(Constants.USER_ID, String.valueOf(userId));
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onclick(int s) {

    }
}