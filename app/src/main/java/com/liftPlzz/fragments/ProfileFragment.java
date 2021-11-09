package com.liftPlzz.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.liftPlzz.R;
import com.liftPlzz.adapter.ReviewListAdapter;
import com.liftPlzz.adapter.ViewPagerAdapter;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.SocialImage;
import com.liftPlzz.model.createProfile.Response;
import com.liftPlzz.model.createProfile.User;
import com.liftPlzz.model.getReview.Datum;
import com.liftPlzz.presenter.ProfilePresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.ProfileView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BaseFragment<ProfilePresenter, ProfileView> implements ProfileView, ReviewListAdapter.ItemListener {


    SharedPreferences sharedPreferences;
    @BindView(R.id.imageViewBackContact)
    ImageView imageViewBackContact;
    @BindView(R.id.layoutAbout)
    LinearLayout layoutAbout;
    @BindView(R.id.textViewAbout)
    AppCompatTextView textViewAbout;
    @BindView(R.id.layoutReview)
    LinearLayout layoutReview;
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
        presenter.getProfile(sharedPreferences.getString(Constants.TOKEN, ""));
        presenter.getReview(sharedPreferences.getString(Constants.TOKEN, ""));
        layoutAbout.setSelected(true);
        textViewAbout.setSelected(true);
        layoutReview.setSelected(false);
        textViewReview.setSelected(false);
        scrollViewAbout.setVisibility(View.VISIBLE);
        recyclerViewReview.setVisibility(View.GONE);


    }

    @OnClick({R.id.imageViewBackContact, R.id.layoutAbout, R.id.layoutReview, R.id.imageViewEdit})
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
            case R.id.layoutAbout:
                layoutAbout.setSelected(true);
                textViewAbout.setSelected(true);
                layoutReview.setSelected(false);
                textViewReview.setSelected(false);
                scrollViewAbout.setVisibility(View.VISIBLE);
                recyclerViewReview.setVisibility(View.GONE);
                break;
            case R.id.layoutReview:
                layoutAbout.setSelected(false);
                textViewAbout.setSelected(false);
                layoutReview.setSelected(true);
                textViewReview.setSelected(true);
                scrollViewAbout.setVisibility(View.GONE);
                recyclerViewReview.setVisibility(View.VISIBLE);

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
            mViewPagerAdapter = new ViewPagerAdapter(getActivity(), imageslist);
            // Adding the Adapter to the ViewPager
            viewPagerMain.setAdapter(mViewPagerAdapter);
            indicator.setViewPager(viewPagerMain);
        }
    }

    @Override
    public void setReviewData(List<Datum> data) {
        recyclerViewReview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewReview.setAdapter(new ReviewListAdapter(getContext(), data, ProfileFragment.this));
    }

    @Override
    public void onclick(int s) {

    }
}
