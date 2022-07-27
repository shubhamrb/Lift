package com.liftPlzz.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.adapter.ReviewListAdapter;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.getVehicle.getReview.Datum;
import com.liftPlzz.model.getVehicle.getReview.Response;
import com.liftPlzz.presenter.ReviewsPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.ReviewsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewsFragment extends BaseFragment<ReviewsPresenter, ReviewsView> implements ReviewsView, ReviewListAdapter.ItemListener {

    SharedPreferences sharedPreferences;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    private String strToken;
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
    protected int createLayout() {
        return R.layout.fragment_reviews;
    }

    @Override
    protected void setPresenter() {
        presenter = new ReviewsPresenter();
    }


    @Override
    protected ReviewsView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        toolBarTitle.setText("All Reviews");

        spinnerArray = new ArrayList<>();
        spinnerArray.add("Select");
        adapter = new ArrayAdapter<String>(
                getActivity(), R.layout.custom_spinner, spinnerArray);

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
        presenter.getReview(sharedPreferences.getString(Constants.TOKEN, ""), sharedPreferences.getString(Constants.USER_ID, ""), filter_type.toLowerCase().replace(" ", "_"));
    }

    @OnClick({R.id.imageViewBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
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
                recyclerViewReview.setLayoutManager(new LinearLayoutManager(getContext()));

                List<Datum> list = new ArrayList<>();
                if (response.getData() != null && response.getData().size() > 0) {
                    list.addAll(response.getData());
                }
                recyclerViewReview.setAdapter(new ReviewListAdapter(getContext(), list, this));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccessLikeDislike(Response response) {
        if (response != null) {
            Toast.makeText(getActivity(), response.getMessage(), Toast.LENGTH_SHORT).show();
            getReviews();
        }
    }

    @Override
    public void onLikeClick(int review_id) {
        presenter.likeReview(sharedPreferences.getString(Constants.TOKEN, ""), review_id);
    }

    @Override
    public void onDislikeClick(int review_id) {
        presenter.dislikeReview(sharedPreferences.getString(Constants.TOKEN, ""), review_id);
    }
}
