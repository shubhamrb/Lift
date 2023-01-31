package com.liftPlzz.fragments;


import static android.app.Activity.RESULT_OK;
import static com.liftPlzz.utils.Validation.isEmailValid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.liftPlzz.R;
import com.liftPlzz.activity.HomeActivity;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.createProfile.Response;
import com.liftPlzz.model.createProfile.User;
import com.liftPlzz.presenter.CreateProfilePresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.CreateProfileView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateProfileFragment extends BaseFragment<CreateProfilePresenter, CreateProfileView> implements CreateProfileView {


    private static final int RC_SIGN_IN = 4;
    SharedPreferences sharedPreferences;
    @BindView(R.id.imageViewBackContact)
    ImageView imageViewBackContact;

    @BindView(R.id.editTextName)
    AppCompatEditText editTextName;
    @BindView(R.id.editTextDesignation)
    AppCompatEditText editTextDesignation;
    @BindView(R.id.editTextMobile)
    AppCompatTextView editTextMobile;
    @BindView(R.id.editTextEmail)
    AppCompatEditText editTextEmail;
    @BindView(R.id.editTextAboutUser)
    AppCompatEditText editTextAboutUser;
    @BindView(R.id.editTextReferralCode)
    AppCompatEditText editTextReferralCode;

    @BindView(R.id.buttonUpdate)
    AppCompatButton buttonUpdate;

    @BindView(R.id.imageViewAddImage)
    AppCompatImageView imageViewAddImage;

    @BindView(R.id.imageViewMainDetails)
    AppCompatImageView imageViewMainDetails;

    private CallbackManager mCallbackManager;
    static User user;
    String email;
    GoogleSignInClient mGoogleSignInClient;
    boolean isImage = false;
    File file;
    SignInButton signInButton;
    private String referral_id = null;

    public static void setUser(User user) {
        CreateProfileFragment.user = user;
    }

    @Override
    protected int createLayout() {
        return R.layout.fragment_create_profile;
    }

    @Override
    protected void setPresenter() {
        presenter = new CreateProfilePresenter();
    }


    @Override
    protected CreateProfileView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            referral_id = bundle.getString("referral_id");
            editTextReferralCode.setText(referral_id);
//            Toast.makeText(getActivity(), referral_id, Toast.LENGTH_LONG).show();
        }
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        mCallbackManager = CallbackManager.Factory.create();
        editTextMobile.setText(sharedPreferences.getString(Constants.MOBILE, ""));
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);


        signInButton = getActivity().findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        LoginButton loginButton = (LoginButton) getActivity().findViewById(R.id.login_button);
//        loginButton.setReadPermissions("email", "public_profile", "user_friends");

        loginButton.setFragment(this);
        loginButton.setReadPermissions("email", "public_profile");

//         LoginManager.getInstance().logInWithReadPermissions(CreateProfileFragment.this, Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());

                                        // Application code
                                        try {

                                            email = object.getString("email");
                                            editTextEmail.setText(email);
                                            // loginButton.setVisibility(View.GONE);
                                            Log.d("Shreks email", "" + email);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "email");
                        request.setParameters(parameters);
                        request.executeAsync();
                        Profile profile = Profile.getCurrentProfile();
                        Log.d("Shreks ", "" + profile.getName() + " " + profile.getId() + " " + profile.getProfilePictureUri(600, 600));
                        Uri imagePath = profile.getProfilePictureUri(600, 800);
                        editTextName.setText(profile.getName());

                        if (imagePath != null) {
                            imageViewAddImage.setVisibility(View.GONE);
                            Picasso.get().load(imagePath).into(imageViewMainDetails);
                        } else {
                            imageViewAddImage.setVisibility(View.VISIBLE);
                        }
                        // Get User Name
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.d("CreateProfileFragment", "cancel");

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.d("CreateProfileFragment", exception.getMessage());
                    }
                });
        printKeyHash(getActivity());
    }

    public static String printKeyHash(Context context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }


    @OnClick({R.id.imageViewBackContact, R.id.buttonUpdate, R.id.imageViewAddImage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewAddImage:
                ImagePicker.Companion.with(this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
                break;
            case R.id.imageViewBackContact:
                getActivity().onBackPressed();
                break;
            case R.id.buttonUpdate:
                if (editTextName.getText().toString().isEmpty()) {
                    showMessage("Please enter name");
                } /*else if (editTextDesignation.getText().toString().isEmpty()) {
                    showMessage("Please enter designation");
                }*/ else if (editTextEmail.getText().toString().isEmpty()) {
                    showMessage("Please enter email");
                } else if (!isEmailValid(editTextEmail.getText().toString())) {
                    showMessage("Please enter valid email");
                } else if (editTextMobile.getText().toString().isEmpty()) {
                    showMessage("Please enter mobile number");
                } /*else if (editTextAboutUser.getText().toString().isEmpty()) {
                    showMessage("Please enter About Yourself");
                } */ else {
                    if (isImage) {
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                        RequestBody api_key = RequestBody.create(okhttp3.MultipartBody.FORM, Constants.API_KEY);
                        RequestBody device = RequestBody.create(okhttp3.MultipartBody.FORM, "android");
                        RequestBody token = RequestBody.create(okhttp3.MultipartBody.FORM, sharedPreferences.getString(Constants.TOKEN, ""));
                        RequestBody name = RequestBody.create(okhttp3.MultipartBody.FORM, editTextName.getText().toString());
                        RequestBody designation = RequestBody.create(okhttp3.MultipartBody.FORM, "" + editTextDesignation.getText().toString());
                        RequestBody email = RequestBody.create(okhttp3.MultipartBody.FORM, editTextEmail.getText().toString());
                        RequestBody mobile = RequestBody.create(okhttp3.MultipartBody.FORM, editTextMobile.getText().toString());
                        RequestBody aboutUs = RequestBody.create(okhttp3.MultipartBody.FORM, "" + editTextAboutUser.getText().toString());
                        RequestBody refferal_id = RequestBody.create(okhttp3.MultipartBody.FORM, "" + editTextReferralCode.getText().toString());
                        presenter.updateProfile(api_key, device, token, name, designation, email, mobile, aboutUs, body, refferal_id);
                    } else {
                        presenter.updateProfile(sharedPreferences.getString(Constants.TOKEN, ""), editTextName.getText().toString(), editTextDesignation.getText().toString(), editTextEmail.getText().toString(), editTextMobile.getText().toString(), editTextAboutUser.getText().toString(), editTextReferralCode.getText().toString());
                    }
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        if (resultCode == RESULT_OK) {
//            Log.e("hdhdhd", "onActivityResult: " + data.getData().toString());
            Uri fileurl = data.getData();
            Picasso.get().load(fileurl).into(imageViewMainDetails);

            file = null;
            try {
                file = new File(new URL(data.getDataString()).toURI());
                isImage = true;
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            //File file= =ImagePicker.getFile(data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                editTextEmail.setText(account.getEmail());
                editTextName.setText(account.getDisplayName());

                if (account.getPhotoUrl() != null) {
                    imageViewAddImage.setVisibility(View.GONE);
                    Picasso.get().load(account.getPhotoUrl()).into(imageViewMainDetails);
                } else {
                    imageViewAddImage.setVisibility(View.VISIBLE);
                }
                signInButton.setVisibility(View.GONE);
            }
            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("mmm", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    @Override
    public void setProfileData(Response response) {
        sharedPreferences.edit().putBoolean(Constants.IS_LOGIN, true).apply();
        sharedPreferences.edit().putString(Constants.NAME, response.getUser().getName()).apply();
        sharedPreferences.edit().putString(Constants.EMAIL, response.getUser().getEmail()).apply();
        sharedPreferences.edit().putString(Constants.MOBILE, response.getUser().getMobile()).apply();
        sharedPreferences.edit().putString(Constants.USER_ID, String.valueOf(response.getUser().getId())).apply();
        sharedPreferences.edit().putString(Constants.IMAGE, String.valueOf(response.getUser().getImage())).apply();
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.putExtra("referral_id", referral_id);
        startActivity(intent);
        getActivity().finish();
    }
}
