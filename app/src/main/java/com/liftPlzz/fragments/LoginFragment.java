package com.liftPlzz.fragments;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.liftPlzz.R;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.sendotp.SendOtpResponse;
import com.liftPlzz.presenter.LoginPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.LoginView;

import java.util.List;

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
                String phoneNumber = Identity.getSignInClient(getActivity()).getPhoneNumberFromIntent(result.getData());
                if (!phoneNumber.isEmpty()) {
                    editTextMobileNumber.setText(phoneNumber.substring(phoneNumber.length() - 10));
                }
            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(getActivity(), "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                SubscriptionManager subManager = (SubscriptionManager) getActivity().getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                try {
                    List<SubscriptionInfo> subInfoList = subManager.getActiveSubscriptionInfoList();
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.item_phone_number);

                    for (int i = 0; i < subInfoList.size(); i++) {
                        arrayAdapter.add(subInfoList.get(i).getNumber());
                    }
                    if (!subInfoList.isEmpty()) {
                        new AlertDialog.Builder(getActivity()).setTitle("Continue with").setAdapter(arrayAdapter, (dialogInterface, i) -> {
                            String number = subInfoList.get(i).getNumber();

                            if (number.length() > 10) {
                                number = number.substring(number.length() - 10);
                            }
                            editTextMobileNumber.setText(number);
                        }).setPositiveButton("NONE OF THE ABOVE", null).setNegativeButton("", null).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

//                phoneSelection();
            }
        });
    }

    private void phoneSelection() {
        GetPhoneNumberHintIntentRequest request = GetPhoneNumberHintIntentRequest.builder().build();
        Identity.getSignInClient(getActivity()).getPhoneNumberHintIntent(request).addOnFailureListener(e -> {
            Log.e("Error : ", e.getMessage());
        }).addOnSuccessListener(pendingIntent -> {
            IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(pendingIntent.getIntentSender()).build();
            someActivityResultLauncher.launch(intentSenderRequest);
        });
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


}