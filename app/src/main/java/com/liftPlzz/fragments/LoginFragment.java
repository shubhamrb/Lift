package com.liftPlzz.fragments;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.liftPlzz.R;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.sendotp.SendOtpResponse;
import com.liftPlzz.presenter.LoginPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.LoginView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginFragment extends BaseFragment<LoginPresenter, LoginView> implements LoginView {

    @BindView(R.id.editTextMobileNumber)
    AppCompatEditText editTextMobileNumber;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    SharedPreferences sharedPreferences;
    @BindView(R.id.textViewForgotPassword)
    AppCompatTextView textViewForgotPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @BindView(R.id.imageViewNext)
    ImageView imageViewNext;
    @BindView(R.id.image)
    AppCompatImageView image;
    @BindView(R.id.frame)
    FrameLayout frame;
    @BindView(R.id.textViewMain)
    AppCompatTextView textViewMain;
    @BindView(R.id.layoutLine1)
    ImageView layoutLine1;
    @BindView(R.id.layoutEditText)
    LinearLayout layoutEditText;
    @BindView(R.id.textViewNext)
    AppCompatTextView textViewNext;

    @Override
    protected int createLayout() {
        return R.layout.fragment_login;
    }

    @Override
    protected void setPresenter() {
        presenter = new LoginPresenter();
    }


    @Override
    protected LoginView createView() {
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }


    @OnClick({R.id.textViewNext, R.id.imageViewNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textViewNext:
            case R.id.imageViewNext:
                if (editTextMobileNumber.getText().toString().trim().isEmpty()) {
                    showMessage("Please enter Mobile Number");
                } else if (editTextMobileNumber.getText().length() < 10) {
                    showMessage("Please enter Valid Mobile Number");
                } else {
//                    presenter.sendOtp(editTextMobileNumber.getText().toString());
                    otpScreen(editTextMobileNumber.getText().toString());
                }
                break;
        }
    }

    @Override
    public void setLoginData(SendOtpResponse response) {
        sharedPreferences.edit().putString(Constants.TOKEN, response.getToken()).apply();
        sharedPreferences.edit().putString(Constants.MOBILE, editTextMobileNumber.getText().toString()).apply();
        OTpFragment.setOtpData(response.getOtp());
        OTpFragment.setMobileNumber(response.getData().getMobile());
        OTpFragment.setNewUser(response.getNewUser());
        presenter.openOTP();
    }

    public void otpScreen(String mobileNo) {
//        sharedPreferences.edit().putString(Constants.TOKEN, response.getToken()).apply();
//        sharedPreferences.edit().putString(Constants.MOBILE, editTextMobileNumber.getText().toString()).apply();
//        OTpFragment.setOtpData(response.getOtp());
        OTpFragment.setMobileNumber(mobileNo);
//        OTpFragment.setNewUser(response.getNewUser());
        presenter.openOTP();
    }



}