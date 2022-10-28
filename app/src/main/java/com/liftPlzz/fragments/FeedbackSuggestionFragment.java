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

import com.liftPlzz.R;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.presenter.FeedbackSuggestionPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.FeedbackSuggestionView;

import butterknife.BindView;
import butterknife.OnClick;

public class FeedbackSuggestionFragment extends BaseFragment<FeedbackSuggestionPresenter, FeedbackSuggestionView> implements FeedbackSuggestionView {

    SharedPreferences sharedPreferences;
    String strToken = "";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;

    @BindView(R.id.et_name)
    AppCompatEditText et_name;
    @BindView(R.id.et_email)
    AppCompatEditText et_email;
    @BindView(R.id.et_number)
    AppCompatEditText et_number;
    @BindView(R.id.et_feedback)
    AppCompatEditText et_feedback;

    @BindView(R.id.btn_submit)
    AppCompatButton btn_submit;

    private String type;

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
    }

    private void submitForm() {
        String name = et_name.getText().toString();
        String email = et_email.getText().toString();
        String number = et_number.getText().toString();
        String feedback = et_feedback.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter your name.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter your email.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (number.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter your number.", Toast.LENGTH_SHORT).show();
            return;
        }
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
        et_name.setText("");
        et_email.setText("");
        et_number.setText("");
        et_feedback.setText("");
        Toast.makeText(getActivity(), type + " submitted.", Toast.LENGTH_SHORT).show();
    }
}
