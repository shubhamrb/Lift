package com.liftPlzz.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.adapter.FeedbackSuggestionsAdapter;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.FeedbackModelResponse;
import com.liftPlzz.presenter.FeedbackSuggestionPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.FeedbackSuggestionView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackSuggestionFragment extends BaseFragment<FeedbackSuggestionPresenter, FeedbackSuggestionView> implements FeedbackSuggestionView {

    SharedPreferences sharedPreferences;
    String strToken = "";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;

    @BindView(R.id.et_feedback)
    AppCompatEditText et_feedback;

    @BindView(R.id.btn_submit)
    AppCompatButton btn_submit;

    @BindView(R.id.recycler_data)
    RecyclerView recycler_data;

    private String type;
    private List<FeedbackModelResponse.FeedbackModel> arrayList;
    private FeedbackSuggestionsAdapter feedbackSuggestionsAdapter;

    @Override
    protected int createLayout() {
        return R.layout.fragment_feedback_suggestion;
    }

    @Override
    protected void setPresenter() {
        presenter = new FeedbackSuggestionPresenter();
    }

    @Override
    protected FeedbackSuggestionView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString("type");
            toolBarTitle.setText(type);
            et_feedback.setHint(type);
        }
        strToken = sharedPreferences.getString(Constants.TOKEN, "");

        btn_submit.setOnClickListener(view -> {
            submitForm();
        });

        arrayList = new ArrayList<>();
        feedbackSuggestionsAdapter = new FeedbackSuggestionsAdapter(getActivity());
        recycler_data.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_data.setAdapter(feedbackSuggestionsAdapter);

        getFeedbackSuggestionList();
    }

    private void getFeedbackSuggestionList() {
        ApiService api = RetroClient.getApiService();
        Call<FeedbackModelResponse> call;
        if (type.equalsIgnoreCase("feedback")) {
            call = api.getFeedbackList(Constants.API_KEY, getResources().getString(R.string.android), strToken);
        } else {
            call = api.getSuggestionList(Constants.API_KEY, getResources().getString(R.string.android), strToken);
        }
        call.enqueue(new Callback<FeedbackModelResponse>() {
            @Override
            public void onResponse(Call<FeedbackModelResponse> call, Response<FeedbackModelResponse> response) {
                Constants.hideLoader();
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        arrayList.clear();
                        arrayList.addAll(response.body().getData());
                        feedbackSuggestionsAdapter.setList(arrayList);
                    }
                }
            }

            @Override
            public void onFailure(Call<FeedbackModelResponse> call, Throwable throwable) {
                Constants.hideLoader();
//                Constants.showMessage(this, throwable.getMessage(), linearLayout);
            }
        });
    }

    private void submitForm() {
        String feedback = et_feedback.getText().toString();
        if (feedback.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter your " + type.toLowerCase() + ".", Toast.LENGTH_SHORT).show();
            return;
        }

        if (type.equalsIgnoreCase("feedback")) {
            presenter.getSubmitFeedback(strToken, feedback);
        } else {
            presenter.getSubmitSuggestion(strToken, feedback);
        }
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
    public void setSubmitData() {
        et_feedback.setText("");
        Toast.makeText(getActivity(), type + " submitted.", Toast.LENGTH_SHORT).show();
        arrayList.clear();
        hideKeyBoard();
        getFeedbackSuggestionList();
    }
}
