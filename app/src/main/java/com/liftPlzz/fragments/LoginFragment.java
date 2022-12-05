package com.liftPlzz.fragments;


import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.liftPlzz.R;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.sendotp.SendOtpResponse;
import com.liftPlzz.presenter.LoginPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.LoginView;

import butterknife.BindView;
import butterknife.OnClick;


public class LoginFragment extends BaseFragment<LoginPresenter, LoginView> implements LoginView, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

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
    private String referral_id = null;
    private ActivityResultLauncher<IntentSenderRequest> someActivityResultLauncher;

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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
            try {
                if (result.getData() != null) {
                    Credential credential = result.getData().getParcelableExtra(Credential.EXTRA_KEY);
                    if (credential != null) {

                        String mobNumber = credential.getId();
                        String newString = mobNumber.replace("+91", "0");

                        if (!newString.isEmpty()) {
                            if (newString.length() > 10) {
                                editTextMobileNumber.setText(newString.substring(newString.length() - 10));
                            } else {
                                editTextMobileNumber.setText(newString);
                            }
                            hideKeyBoard();
                        }

                    } else {
//                        Toast.makeText(getActivity(), "err", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        Bundle bundle = getArguments();
        if (bundle != null) {
            referral_id = bundle.getString("referral_id");
        }

        editTextMobileNumber.setOnFocusChangeListener((view, isFocused) -> {
            if (isFocused) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_STATE}, 1001);
                    return;
                }
                phoneSelection();
            }
        });

        editTextMobileNumber.clearFocus();
    }

    private void phoneSelection() {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(getActivity()).addApi(Auth.CREDENTIALS_API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        googleApiClient.connect();
        HintRequest hintRequest = new HintRequest.Builder().setPhoneNumberIdentifierSupported(true).build();
        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest);
        IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(intent.getIntentSender()).build();
        someActivityResultLauncher.launch(intentSenderRequest);
    }

    @OnClick({R.id.imageViewNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewNext:
                if (editTextMobileNumber.getText().toString().trim().isEmpty()) {
                    showMessage("Please enter Mobile Number");
                } else if (editTextMobileNumber.getText().length() < 10) {
                    showMessage("Please enter Valid Mobile Number");
                } else {
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
        presenter.openOTP(referral_id);
    }

    public void otpScreen(String mobileNo) {
        OTpFragment.setMobileNumber(mobileNo);
        presenter.openOTP(referral_id);
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
}