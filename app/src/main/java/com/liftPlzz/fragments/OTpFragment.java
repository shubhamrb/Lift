package com.liftPlzz.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Telephony;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.liftPlzz.R;
import com.liftPlzz.SmsListener;
import com.liftPlzz.activity.HomeActivity;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.resendOtp.Response;
import com.liftPlzz.model.sendotp.SendOtpResponse;
import com.liftPlzz.presenter.OtpPresenter;
import com.liftPlzz.receiver.SmsBroadcastReceiver;
import com.liftPlzz.receiver.SmsReceiver;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.OtpReceivedInterface;
import com.liftPlzz.views.OtpView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;


public class OTpFragment extends BaseFragment<OtpPresenter, OtpView> implements OtpView, OtpReceivedInterface {

    SharedPreferences sharedPreferences;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @BindView(R.id.editTextFirstDigits)
    AppCompatEditText editTextFirstDigits;
    @BindView(R.id.editTextSecondsDigits)
    AppCompatEditText editTextSecondsDigits;
    @BindView(R.id.editTextThirdDigits)
    AppCompatEditText editTextThirdDigits;
    @BindView(R.id.editTextFourthDigits)
    AppCompatEditText editTextFourthDigits;
    @BindView(R.id.editTextFive)
    AppCompatEditText editTextFive;
    @BindView(R.id.editTextSix)
    AppCompatEditText editTextSix;
    @BindView(R.id.textViewResendOtp)
    AppCompatTextView textViewResendOtp;
    @BindView(R.id.reltive)
    RelativeLayout reltive;

    static String otp, mobileNo = "";
    private final static String country = "+91";
    static Integer otpData;

    static Integer newUser;
    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private FirebaseAuth firebaseAuth;
    SmsBroadcastReceiver mSmsBroadcastReceiver;


    public static void setOtpData(Integer otpData) {
        OTpFragment.otpData = otpData;
    }

    public static void setMobileNumber(String mobileNo) {
        OTpFragment.mobileNo = mobileNo;
    }

    public static void setNewUser(Integer newUser) {
        OTpFragment.newUser = newUser;
    }

    @Override
    protected int createLayout() {
        return R.layout.fragment_otp;
    }

    @Override
    protected void setPresenter() {
        presenter = new OtpPresenter();
    }


    @Override
    protected OtpView createView() {
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        reverseTimer(30, textViewResendOtp);
        firebaseAuth = FirebaseAuth.getInstance();
//        mSmsBroadcastReceiver = new SmsBroadcastReceiver();
//        mSmsBroadcastReceiver.setOnOtpListeners(this);
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
//        getActivity().getApplicationContext().registerReceiver(mSmsBroadcastReceiver, intentFilter);
        sendCode();
//        if (otpData != null) {
//            editTextFirstDigits.setText(String.valueOf(otpData.toString().charAt(0)));
//            editTextSecondsDigits.setText(String.valueOf(otpData.toString().charAt(1)));
//            editTextThirdDigits.setText(String.valueOf(otpData.toString().charAt(2)));
//            editTextFourthDigits.setText(String.valueOf(otpData.toString().charAt(3)));
//            otp = editTextFirstDigits.getText().toString() + editTextSecondsDigits.getText().toString() + editTextThirdDigits.getText().toString() + editTextFourthDigits.getText().toString();
//            if (otp.length() == 4) {
//                presenter.verifyOtp(otp, sharedPreferences.getString(Constants.MOBILE, ""));
//            }
//        }

        SmsReceiver.bindListener(messageText -> {
            editTextFirstDigits.setText(String.valueOf(messageText.charAt(0)));
            Log.d("OTP word 1", editTextFirstDigits.getText().toString());
        editTextSecondsDigits.setText(String.valueOf(messageText.charAt(1)));
            Log.d("OTP word 2", editTextSecondsDigits.getText().toString());
        editTextThirdDigits.setText(String.valueOf(messageText.charAt(2)));
            Log.d("OTP word 3", editTextThirdDigits.getText().toString());
        editTextFourthDigits.setText(String.valueOf(messageText.charAt(3)));
            Log.d("OTP word 4", editTextFourthDigits.getText().toString());
        editTextFive.setText(String.valueOf(messageText.charAt(4)));
            Log.d("OTP word 5", editTextFive.getText().toString());
        editTextSix.setText(String.valueOf(messageText.charAt(5)));
            Log.d("OTP word 6", editTextSix.getText().toString());
        });
        editTextFirstDigits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    editTextSecondsDigits.requestFocus();

                    otp = editTextFirstDigits.getText().toString() + editTextSecondsDigits.getText().toString()
                            + editTextThirdDigits.getText().toString() + editTextFourthDigits.getText().toString()
                            + editTextFive.getText().toString() + editTextSix.getText().toString();
                    if (otp.length() == 6) {
                        doVerifyCode();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextSecondsDigits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    editTextThirdDigits.requestFocus();
                    otp = editTextFirstDigits.getText().toString() + editTextSecondsDigits.getText().toString()
                            + editTextThirdDigits.getText().toString() + editTextFourthDigits.getText().toString()
                            + editTextFive.getText().toString() + editTextSix.getText().toString();
                    if (otp.length() == 6) {
                        doVerifyCode();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextThirdDigits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    editTextFourthDigits.requestFocus();
                    otp = editTextFirstDigits.getText().toString() + editTextSecondsDigits.getText().toString()
                            + editTextThirdDigits.getText().toString() + editTextFourthDigits.getText().toString()
                            + editTextFive.getText().toString() + editTextSix.getText().toString();
                    if (otp.length() == 6) {
                        doVerifyCode();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editTextFourthDigits.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    editTextFive.requestFocus();
                    otp = editTextFirstDigits.getText().toString() + editTextSecondsDigits.getText().toString()
                            + editTextThirdDigits.getText().toString() + editTextFourthDigits.getText().toString()
                            + editTextFive.getText().toString() + editTextSix.getText().toString();
                    if (otp.length() == 6) {
                        doVerifyCode();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        editTextFive.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    editTextSix.requestFocus();
                    otp = editTextFirstDigits.getText().toString() + editTextSecondsDigits.getText().toString()
                            + editTextThirdDigits.getText().toString() + editTextFourthDigits.getText().toString()
                            + editTextFive.getText().toString() + editTextSix.getText().toString();
                    if (otp.length() == 6) {
                        doVerifyCode();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        editTextSix.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    editTextFirstDigits.requestFocus();
                    otp = editTextFirstDigits.getText().toString() + editTextSecondsDigits.getText().toString()
                            + editTextThirdDigits.getText().toString() + editTextFourthDigits.getText().toString()
                            + editTextFive.getText().toString() + editTextSix.getText().toString();
                    if (otp.length() == 6) {
                        doVerifyCode();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void reverseTimer(int Seconds, final TextView tv) {

        new CountDownTimer(Seconds * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                if (getContext() != null) {
                    tv.setTextColor(getContext().getResources().getColor(R.color.colorBlack));
                    int seconds = (int) (millisUntilFinished / 1000);
                    int minutes = seconds / 60;
                    seconds = seconds % 60;
                    tv.setText(getContext().getResources().getString(R.string.resend_otp_in) + String.format("%02d", minutes)
                            + ":" + String.format("%02d", seconds));
                    tv.setEnabled(false);
                }
            }

            public void onFinish() {
                if (getContext() != null) {
                    tv.setText(getContext().getResources().getString(R.string.resend_otp));
                    tv.setEnabled(true);
                    tv.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
                }
            }
        }.start();
    }

    @Override
    public void setLoginData(SendOtpResponse response) {
        if (response.getNewUser() == 1) {
            //  sharedPreferences.edit().putBoolean(Constants.IS_LOGIN, true).apply();
            sharedPreferences.edit().putString(Constants.TOKEN, response.getToken()).apply();
//            sharedPreferences.edit().putString(Constants.NAME, response.getData().getName()).apply();
//            sharedPreferences.edit().putString(Constants.EMAIL, response.getData().getEmail()).apply();
            sharedPreferences.edit().putString(Constants.MOBILE, response.getData().getMobile()).apply();
            sharedPreferences.edit().putString(Constants.USER_ID, String.valueOf(response.getData().getId())).apply();
      /*  Intent intent = new Intent(getActivity(), HomeActivity.class);
        startActivity(intent);
        getActivity().finish();*/
            presenter.openCreateProfile();
        } else {

            sharedPreferences.edit().putBoolean(Constants.IS_LOGIN, true).apply();
            sharedPreferences.edit().putString(Constants.TOKEN, response.getToken()).apply();
            sharedPreferences.edit().putString(Constants.NAME, response.getData().getName()).apply();
            sharedPreferences.edit().putString(Constants.EMAIL, response.getData().getEmail()).apply();
            sharedPreferences.edit().putString(Constants.MOBILE, response.getData().getMobile()).apply();
            sharedPreferences.edit().putString(Constants.USER_ID, String.valueOf(response.getData().getId())).apply();
            Intent intent = new Intent(getActivity(), HomeActivity.class);
            startActivity(intent);
            getActivity().finish();

        }
    }

    @Override
    public void setResendData(Response response) {
        reverseTimer(30, textViewResendOtp);
    }

    @OnClick(R.id.textViewResendOtp)
    public void onViewClicked() {
//        presenter.ResendOtp(sharedPreferences.getString(Constants.MOBILE, ""));
        reverseTimer(30, textViewResendOtp);
        resendCode();
    }

    private void sendCode() {
        setUpVerificationCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                country + mobileNo,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                verificationCallbacks);
    }


    private void setUpVerificationCallbacks() {
        verificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Snackbar.make(reltive, "Invalid Credentials", Snackbar.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(reltive, "Limit Reached Try Again In a few Hours", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                phoneVerificationId = s;
                resendToken = forceResendingToken;
//                startSMSListener();
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }
        };
    }

    private void resendCode() {
        setUpVerificationCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                country + mobileNo,
                60,
                TimeUnit.SECONDS,
                getActivity(),
                verificationCallbacks,
                resendToken);
    }


    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Snackbar.make(reltive, "Successful", Snackbar.LENGTH_LONG).show();
                            String cred = task.getResult().getUser().getPhoneNumber();
                            Log.d("mobile no.", cred);
                            if (cred.isEmpty()){
                                Snackbar.make(reltive, "Mobile Number not found", Snackbar.LENGTH_LONG).show();
                            }
                            cred.replace("+91", "");
//                            checkUser();
//                            setLoginData();
                            presenter.sendOtp(mobileNo);
//                            presenter.sendOtp(cred);
                        } else {
                            if (task.getException() instanceof
                                    FirebaseAuthInvalidCredentialsException) {
                                Snackbar.make(reltive, "Invalid Credentials", Snackbar.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void doVerifyCode() {
//        String otp = etEnterotp.getText().toString();
        if (TextUtils.isEmpty(otp) || !TextUtils.isDigitsOnly(otp) || otp.length() != 6) {
            Toast.makeText(getActivity(), "Please enter valid OTP.", Toast.LENGTH_LONG).show();
            return;
        }

        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(phoneVerificationId, otp);
        signInWithPhoneAuthCredential(credential);

    }

    public void startSMSListener() {
        SmsRetrieverClient mClient = SmsRetriever.getClient(getActivity());
        Task<Void> mTask = mClient.startSmsRetriever();
        mTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                layoutInput.setVisibility(View.GONE);
//                layoutVerify.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "SMS Retriever starts", Toast.LENGTH_LONG).show();
            }
        });
        mTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onOtpReceived(String otp) {
        Toast.makeText(getActivity(), "Otp Received " + otp, Toast.LENGTH_LONG).show();
        Log.d("TAG", "onReceive: " + otp);
    }

    @Override
    public void onOtpTimeout() {
        Toast.makeText(getActivity(), "Time out, please resend", Toast.LENGTH_LONG).show();
    }
}
