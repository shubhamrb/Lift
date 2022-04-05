package com.liftPlzz.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.liftPlzz.R;
import com.liftPlzz.adapter.ReviewListAdapter;
import com.liftPlzz.adapter.ViewPagerAdapter;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.SocialImage;
import com.liftPlzz.model.createProfile.Response;
import com.liftPlzz.model.createProfile.User;
import com.liftPlzz.model.getVehicle.getReview.Datum;
import com.liftPlzz.presenter.ProfilePresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.ProfileView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment<ProfilePresenter, ProfileView> implements ProfileView, ReviewListAdapter.ItemListener,ViewPagerAdapter.ItemListener {

    SharedPreferences sharedPreferences;
    @BindView(R.id.imageViewBackContact)
    ImageView imageViewBackContact;
    @BindView(R.id.layoutAbout)
    LinearLayout layoutAbout;
    @BindView(R.id.textViewAbout)
    AppCompatTextView textViewAbout;

    @BindView(R.id.layoutAddPoint)
    LinearLayout layoutAddPoint;
    @BindView(R.id.AddPointtextview)
    AppCompatTextView AddPointtextview;

    @BindView(R.id.layoutReview)
    LinearLayout layoutReview;
    @BindView(R.id.imgback)
    ImageView imgback;
    @BindView(R.id.textViewReview)
    AppCompatTextView textViewReview;
    @BindView(R.id.recyclerViewReview)
    RecyclerView recyclerViewReview;
    @BindView(R.id.scrollViewAbout)
    ScrollView scrollViewAbout;
    @BindView(R.id.textViewMobile)
    AppCompatTextView textViewMobile;
    @BindView(R.id.textViewEmail)
    AppCompatTextView textViewEmail;
    @BindView(R.id.textViewReviewCount)
    AppCompatTextView textViewReviewCount;
    @BindView(R.id.profilePercentTxt)
    AppCompatTextView profilePercentTxt;
    @BindView(R.id.textViewRating)
    AppCompatTextView textViewRating;
    @BindView(R.id.textViewAboutUser)
    AppCompatTextView textViewAboutUser;
    @BindView(R.id.textViewTotalLift)
    AppCompatTextView textViewTotalLift;
    @BindView(R.id.textViewGiverLift)
    AppCompatTextView textViewGiverLift;
    @BindView(R.id.textViewTakerLift)
    AppCompatTextView textViewTakerLift;
    @BindView(R.id.editTextName)
    AppCompatTextView editTextName;
    @BindView(R.id.tv_shareCode)
    AppCompatTextView tvShareCode;
    @BindView(R.id.imageViewEdit)
    AppCompatImageView imageViewEdit;
    User userData;
    @BindView(R.id.viewPagerMain)
    ViewPager viewPagerMain;
    ViewPagerAdapter mViewPagerAdapter;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    List<SocialImage> imageslist;
    private String strToken;

    @Override
    protected int createLayout() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void setPresenter() {
        presenter = new ProfilePresenter();
    }


    @Override
    protected ProfileView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        presenter.getProfile(sharedPreferences.getString(Constants.TOKEN, ""));
        presenter.getReview(sharedPreferences.getString(Constants.TOKEN, ""));
        layoutAbout.setSelected(true);
        textViewAbout.setSelected(true);
        layoutReview.setSelected(false);
        textViewReview.setSelected(false);
        scrollViewAbout.setVisibility(View.VISIBLE);
        recyclerViewReview.setVisibility(View.GONE);


    }

    @OnClick({R.id.imageViewBackContact,R.id.imgback, R.id.layoutAbout, R.id.layoutReview, R.id.imageViewEdit, R.id.layoutAddPoint, R.id.textViewReviewCount})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBackContact:
                getActivity().onBackPressed();
                break;
            case R.id.imageViewEdit:
                if (userData != null) {
                    UpdateProfileFragment.setUser(userData);
                }
                presenter.openUpdateProfile();

                break;
            case R.id.imgback:
                layoutAbout.setSelected(true);
                textViewAbout.setSelected(true);
                layoutReview.setSelected(false);
                textViewReview.setSelected(false);
                layoutAddPoint.setSelected(false);
                scrollViewAbout.setVisibility(View.VISIBLE);
                recyclerViewReview.setVisibility(View.GONE);
                imgback.setVisibility(View.GONE);

                break;

            case R.id.layoutAddPoint:
                layoutAddPoint.setSelected(true);
                AddPointtextview.setSelected(true);
                layoutAbout.setSelected(false);
                layoutReview.setSelected(false);
                Log.d("tok", strToken);
                getpoints();

//                Intent intent = new Intent(getActivity(), PaymentPackage.class);
//                startActivity(intent);
                break;

            case R.id.textViewReviewCount:
                layoutAbout.setSelected(false);
                textViewAbout.setSelected(false);
                layoutReview.setSelected(true);
                textViewReview.setSelected(true);
                layoutAddPoint.setSelected(false);
                scrollViewAbout.setVisibility(View.GONE);
                recyclerViewReview.setVisibility(View.VISIBLE);
                imgback.setVisibility(View.VISIBLE);

                break;
        }
    }

    @Override
    public void setProfileData(Response response) {
        if (response.getUser() != null) {
            userData = response.getUser();
            User user = response.getUser();
            editTextName.setText(user.getName());
            if (!user.getAboutMe().isEmpty()) {
                textViewAboutUser.setText(user.getAboutMe());
            }
            profilePercentTxt.setText(user.getProfile_percentage()+" %");
            textViewMobile.setText(user.getMobile());
            textViewEmail.setText(user.getEmail());
            tvShareCode.setText("" + user.getShareCode());
            textViewRating.setText(user.getRating() + "/5");
            textViewReviewCount.setText("(" + user.getTotalReview() + " Reviews)");
            textViewTotalLift.setText(String.valueOf(user.getTotalLift()));
            textViewTakerLift.setText(String.valueOf(user.getLiftTaker()));
            textViewGiverLift.setText(String.valueOf(user.getLiftGiver()));
            imageslist = new ArrayList<>();
            if (user.getImage() != null && !user.getImage().isEmpty()) {
                SocialImage socialImage = new SocialImage();
                socialImage.setImage(user.getImage());
                imageslist.add(socialImage);
            }
            if (user.getSocialImages().size() > 0) {
                imageslist.addAll(user.getSocialImages());
            }

            mViewPagerAdapter = new ViewPagerAdapter(getActivity(), imageslist,ProfileFragment.this);
            // Adding the Adapter to the ViewPager
            viewPagerMain.setAdapter(mViewPagerAdapter);
            indicator.setViewPager(viewPagerMain);
        }
    }

    @Override
    public void setProfileImageData(String message) {
        presenter.getProfile(sharedPreferences.getString(Constants.TOKEN, ""));
    }

    @Override
    public void setReviewData(List<Datum> data) {
        recyclerViewReview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewReview.setAdapter(new ReviewListAdapter(getContext(), data, ProfileFragment.this));
    }

    @Override
    public void onclick(int s) {

    }

    @Override
    public void onDeleteClick(int s) {
        presenter.delete_imag(sharedPreferences.getString(Constants.TOKEN, ""),""+s);

    }

    @Override
    public void onEditClick(com.liftPlzz.model.getVehicle.Datum s) {

    }

    private void getpoints() {
        Constants.showLoader(getContext());
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest sr = new StringRequest(Request.Method.POST, "https://charpair.com/api/my-balance", new com.android.volley.Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                Constants.hideLoader();
                Log.d("getpointsresponse", response);

                try {
                    JSONObject jObject = new JSONObject(response);
                    String points = jObject.getString("points");
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                    // ...Irrelevant code for customizing the buttons and title
                    LayoutInflater inflater = getLayoutInflater();
                    dialogBuilder.setTitle("Available Points");
                    dialogBuilder.setCancelable(true);
                    dialogBuilder.setPositiveButton("Ok",(dialogInterface, i) -> {
                    });
                    View dialogView= inflater.inflate(R.layout.user_points_layout, null);
                    dialogBuilder.setView(dialogView);
                    TextView pointstext = dialogView.findViewById(R.id.pointstextviewetxt);
                    pointstext.setText(points);
                    AlertDialog alertDialog =  dialogBuilder.create();
                    alertDialog.show();
                    dialogBuilder.setPositiveButton("Close",(dialogInterface, i) -> {
                        alertDialog.hide();
                    });

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
//                params.put("token", "064ywr3Ht5LPpFPF73J0foCAdvw3ylSDXJys8IqATQ2wyvwimen827FAPA5I");
//                params.put("api_key", "070b92d28adc166b3a6c63c2d44535d2f62a3e24");
//                params.put("client", "android");
//                params.put("token", "NRy4MvEaDj5O04r8S6GGSZAJ7T5tv1QvS969rtgyYe7qdyKv8q6wjWBozH5I");
//                params.put("request_id", "57");

                return params;
            }
        };
        queue.add(sr);

    }

}
