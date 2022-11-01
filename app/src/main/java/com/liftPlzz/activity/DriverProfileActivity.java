package com.liftPlzz.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.liftPlzz.R;
import com.liftPlzz.adapter.ReviewListAdapter;
import com.liftPlzz.adapter.ViewPagerAdapter;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.model.SocialImage;
import com.liftPlzz.model.UserInfo.Review;
import com.liftPlzz.model.UserInfo.User;
import com.liftPlzz.model.UserInfo.UserInfoModel;
import com.liftPlzz.model.chatuser.ChatUser;
import com.liftPlzz.model.getVehicle.Datum;
import com.liftPlzz.model.upcomingLift.Lift;
import com.liftPlzz.utils.AgeCalculator;
import com.liftPlzz.utils.Constants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverProfileActivity extends AppCompatActivity implements ReviewListAdapter.ItemListener, ViewPagerAdapter.ItemListener {

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
    @BindView(R.id.layoutProfile)
    RelativeLayout layoutProfile;


    @BindView(R.id.tv_profile_percentage)
    AppCompatTextView tv_profile_percentage;
    @BindView(R.id.tv_designaton)
    AppCompatTextView tv_designaton;
    @BindView(R.id.tv_department)
    AppCompatTextView tv_department;
    @BindView(R.id.tv_company)
    AppCompatTextView tv_company;
    @BindView(R.id.textViewAboutUser)
    AppCompatTextView textViewAboutUser;
    @BindView(R.id.tv_age)
    AppCompatTextView tv_age;
    @BindView(R.id.viewPagerMain)
    ViewPager viewPagerMain;
    ViewPagerAdapter mViewPagerAdapter;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.linear_rating)
    LinearLayout linear_rating;
    List<com.liftPlzz.model.SocialImage> imageslist;


    private int userId;
    private int liftId;
    private Lift lift;
    private User user;
    private String mobileNo = "";
    private ReviewListAdapter reviewListAdapter;
    private List<Review> reviewList;
    private String strToken = "";
    SharedPreferences sharedPreferences;
    private Boolean IsDriver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_driver_profile);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        ButterKnife.bind(this);
        if (getIntent() != null) {
            userId = getIntent().getIntExtra(Constants.USER_ID, -1);
            liftId = getIntent().getIntExtra(Constants.LIFT_ID, -1);
            IsDriver = getIntent().getBooleanExtra(Constants.IS_DRIVER, false);
        }
        imageslist = new ArrayList<>();
        toolBarTitle.setText(getResources().getString(R.string.profile));

        if (IsDriver) {
            getDriverDetailsApi();
        } else {
            getUserDetailsApi();
        }

        reviewList = new ArrayList<>();
        recyclerReview.setLayoutManager(new LinearLayoutManager(this));
        reviewListAdapter = new ReviewListAdapter(this, DriverProfileActivity.this, reviewList);
        recyclerReview.setAdapter(reviewListAdapter);

        mViewPagerAdapter = new ViewPagerAdapter(this, imageslist, this, 1);
        viewPagerMain.setAdapter(mViewPagerAdapter);
        indicator.setViewPager(viewPagerMain);

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

    public void getDriverDetailsApi() {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();

        Call<UserInfoModel> call = api.getDriverDetails(Constants.API_KEY, getResources().getString(R.string.android), strToken, liftId);
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
        layoutProfile.setVisibility(View.VISIBLE);
        try {
            Glide.with(this).load(userData.getImage()).into(ivUserImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvDriverName.setText(userData.getName());
        tv_profile_percentage.setText("Profile: " + userData.getProfile_percentage().getPercantage() + "%");

        switch (userData.getProfile_percentage().getColor()) {
            case "red":
                tv_profile_percentage.setTextColor(getResources().getColor(R.color.colorRed, null));
                break;
            case "green":
                tv_profile_percentage.setTextColor(getResources().getColor(R.color.quantum_googgreen, null));
                break;
            case "orange":
                tv_profile_percentage.setTextColor(getResources().getColor(R.color.quantum_orange, null));
                break;
        }

        if (userData.getIs_dob_public() != null && userData.getIs_dob_public() != 0 && userData.getDob() != null) {
            int age = new AgeCalculator().getAge(userData.getDob());
            tv_age.setText("Age " + age + " Years old");
            tv_age.setVisibility(View.VISIBLE);
        } else {
            tv_age.setVisibility(View.INVISIBLE);
        }

        tv_designaton.setText(userData.getDesignation());
        tv_department.setText(userData.getDepartment());
        tv_company.setText(userData.getCompany());
        textViewAboutUser.setText(userData.getAbout());

        if (userData.getIs_contact_public() != null && userData.getIs_contact_public() == 0) {
            tvMobile.setVisibility(View.GONE);
        } else {
            tvMobile.setVisibility(View.VISIBLE);
        }
        if (userData.getIs_email_public() != null && userData.getIs_email_public() == 0) {
            tvEmail.setVisibility(View.GONE);
        } else {
            tvEmail.setVisibility(View.VISIBLE);
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
        for (int i = 0; i < userData.getSocialImages().size(); i++) {
            SocialImage obj = new SocialImage();
            obj.setImage(userData.getSocialImages().get(i).getImage());
            obj.setImageId(userData.getSocialImages().get(i).getImageId());
            imageslist.add(obj);
        }
        mViewPagerAdapter.notifyDataSetChanged();

        if (userData.getReviews() != null) {
            if (reviewList != null) {
                reviewList.clear();
            } else {
                reviewList = new ArrayList<>();
            }
            reviewList.addAll(userData.getReviews());
            reviewListAdapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.imageViewBack, R.id.tv_call, R.id.tv_chat, R.id.linear_rating})
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
                Intent intent = new Intent(DriverProfileActivity.this, ChatActivity.class);
                intent.putExtra(Constants.USER_ID, String.valueOf(userId));
                ChatUser chatUser = new ChatUser();
                chatUser.setId(userId);
                chatUser.setName(user.getName());
                chatUser.setMobile(user.getMobile());
                chatUser.setImage(user.getImage());
                intent.putExtra("charuser", new Gson().toJson(chatUser));
                startActivity(intent);
                break;
            case R.id.linear_rating:
//                if (user != null && user.getReviews().size() > 0) {
                Intent reviewIntent = new Intent(DriverProfileActivity.this, DriverReviewsActivity.class);
                reviewIntent.putExtra(Constants.USER_ID, String.valueOf(userId));
                startActivity(reviewIntent);
//                }
                break;
        }
    }

    @Override
    public void onclick(int s) {

    }

    @Override
    public void onDeleteClick(int s) {

    }

    @Override
    public void onEditClick(Datum s) {

    }

    @Override
    public void onAddImage() {

    }

    @Override
    public void onLikeClick(int s) {

    }

    @Override
    public void onDislikeClick(int s) {

    }
}