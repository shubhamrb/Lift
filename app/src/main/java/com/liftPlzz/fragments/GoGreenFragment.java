package com.liftPlzz.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.JsonObject;
import com.liftPlzz.R;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.ReferralDataResponse;
import com.liftPlzz.presenter.GoGreenPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.GoGreenView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class GoGreenFragment extends BaseFragment<GoGreenPresenter, GoGreenView> implements GoGreenView {

    SharedPreferences sharedPreferences;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;

    @BindView(R.id.imageViewHome)
    ImageView imageViewHome;

    @BindView(R.id.buttonSubmit)
    AppCompatButton buttonSubmit;

    @BindView(R.id.upload_aadhar_front)
    AppCompatTextView upload_aadhar_front;

    @BindView(R.id.upload_aadhar_back)
    AppCompatTextView upload_aadhar_back;

    @BindView(R.id.upload_cheque)
    AppCompatTextView upload_cheque;

    @BindView(R.id.upload_pan)
    AppCompatTextView upload_pan;

    @BindView(R.id.verified_aadhar_front)
    ImageView verified_aadhar_front;

    @BindView(R.id.verified_aadhar_back)
    ImageView verified_aadhar_back;

    @BindView(R.id.verified_cheque)
    ImageView verified_cheque;

    @BindView(R.id.verified_pan)
    ImageView verified_pan;

    @BindView(R.id.editTextAccount)
    AppCompatEditText editTextAccount;

    @BindView(R.id.editTextIfsc)
    AppCompatEditText editTextIfsc;

    @BindView(R.id.editTextName)
    AppCompatEditText editTextName;
    String strToken = "";
    private int IMAGE_TYPE = 1;
    private ReferralDataResponse.ReferralModel referralData;
    private File fileAadharFront = null, fileAadharBack = null, fileCheque = null, filePan = null;
    private MultipartBody.Part aadharFrontBody = null, aadharBackBody = null, chequeBody = null, panBody = null;

    @Override
    protected int createLayout() {
        return R.layout.fragment_go_green;
    }

    @Override
    protected void setPresenter() {
        presenter = new GoGreenPresenter();
    }

    @Override
    protected GoGreenView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        toolBarTitle.setText("Go Green Partner");
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        imageViewHome.setVisibility(View.VISIBLE);
        loadData();

    }

    private void loadData() {
        presenter.getData(strToken);
    }

    @OnClick({R.id.imageViewBack, R.id.imageViewHome, R.id.buttonSubmit, R.id.upload_aadhar_front, R.id.upload_aadhar_back,R.id.upload_cheque, R.id.upload_pan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                getActivity().onBackPressed();
                break;
            case R.id.imageViewHome:
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case R.id.upload_aadhar_front:
                ImagePicker.Companion.with(this).crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080).start();
                IMAGE_TYPE = 0;
                break;
            case R.id.upload_aadhar_back:
                ImagePicker.Companion.with(this).crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080).start();
                IMAGE_TYPE = 1;
                break;
            case R.id.upload_cheque:
                ImagePicker.Companion.with(this).crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080).start();
                IMAGE_TYPE = 2;
                break;
            case R.id.upload_pan:
                ImagePicker.Companion.with(this).crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080).start();
                IMAGE_TYPE = 3;
                break;
            case R.id.buttonSubmit:

                if (aadharFrontBody == null) {
                    Toast.makeText(getActivity(), "Please upload your aadhar's front image.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (aadharBackBody == null) {
                    Toast.makeText(getActivity(), "Please upload your aadhar's back image.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (chequeBody == null) {
                    Toast.makeText(getActivity(), "Please upload your Cancelled Cheque.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (panBody == null) {
                    Toast.makeText(getActivity(), "Please upload your PAN.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String account = editTextAccount.getText().toString();
                String ifsc = editTextIfsc.getText().toString();
                String name = editTextName.getText().toString();

                if (account.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter your account number.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ifsc.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter your IFSC.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (name.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter your name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestBody api_key = RequestBody.create(MultipartBody.FORM, Constants.API_KEY);
                RequestBody device = RequestBody.create(MultipartBody.FORM, "android");
                RequestBody token = RequestBody.create(MultipartBody.FORM, strToken);
                RequestBody accountBody = RequestBody.create(MultipartBody.FORM, account);
                RequestBody ifscBody = RequestBody.create(MultipartBody.FORM, ifsc);
                RequestBody nameBody = RequestBody.create(MultipartBody.FORM, name);

                presenter.applyGoGreen(api_key, device, token, aadharFrontBody, aadharBackBody, chequeBody, panBody, accountBody, ifscBody, nameBody);
                break;
        }
    }


    @Override
    public void setData(JsonObject jsonObject) {
        Log.e("Response : ", jsonObject.toString());
        JsonObject dataObject = jsonObject.get("data").getAsJsonObject();

        String aadharFront = dataObject.get("aadhar").getAsString();
        /*String aadharBack = dataObject.get("aadhar_back").getAsString();
        String cheque = dataObject.get("cancel_cheque").getAsString();*/
        String pan = dataObject.get("pan").getAsString();
        String account = dataObject.get("account").getAsString();
        String ifsc = dataObject.get("ifsc").getAsString();
        String name = dataObject.get("name").getAsString();
        if (account != null) {
            editTextAccount.setText(account);
        }
        if (ifsc != null) {
            editTextIfsc.setText(ifsc);
        }
        if (name != null) {
            editTextName.setText(name);
        }

        if (aadharFront != null && !aadharFront.isEmpty()) {
            verified_aadhar_front.setImageTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.quantum_googgreen)));
        }

       /* if (aadharBack != null && !aadharBack.isEmpty()) {
            verified_aadhar_back.setImageTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.quantum_googgreen)));
        }

        if (cheque != null && !cheque.isEmpty()) {
            verified_aadhar_front.setImageTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.quantum_googgreen)));
        }*/

        if (pan != null && !pan.isEmpty()) {
            verified_pan.setImageTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.quantum_googgreen)));
        }
    }

    @Override
    public void onSubmit(String message) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Go Green Partner")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, whichButton) -> {
                    dialog.dismiss();
                }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (IMAGE_TYPE == 0) {
                try {
                    fileAadharFront = new File(new URL(data.getDataString()).toURI());
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileAadharFront);
                    aadharFrontBody = MultipartBody.Part.createFormData("aadhar", fileAadharFront.getName(), requestFile);
                    verified_aadhar_front.setImageTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.quantum_googgreen)));
                } catch (URISyntaxException | MalformedURLException e) {
                    e.printStackTrace();
                }
            } else if (IMAGE_TYPE == 1) {
                try {
                    fileAadharBack = new File(new URL(data.getDataString()).toURI());
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileAadharBack);
                    aadharBackBody = MultipartBody.Part.createFormData("aadhar_back", fileAadharBack.getName(), requestFile);
                    verified_aadhar_back.setImageTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.quantum_googgreen)));
                } catch (URISyntaxException | MalformedURLException e) {
                    e.printStackTrace();
                }
            } else if (IMAGE_TYPE == 2) {
                try {
                    fileCheque = new File(new URL(data.getDataString()).toURI());
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileCheque);
                    chequeBody = MultipartBody.Part.createFormData("cancel_cheque", fileCheque.getName(), requestFile);
                    verified_cheque.setImageTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.quantum_googgreen)));
                } catch (URISyntaxException | MalformedURLException e) {
                    e.printStackTrace();
                }
            } else if (IMAGE_TYPE == 3) {
                try {
                    filePan = new File(new URL(data.getDataString()).toURI());
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), filePan);
                    panBody = MultipartBody.Part.createFormData("pan", filePan.getName(), requestFile);
                    verified_pan.setImageTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.quantum_googgreen)));
                } catch (URISyntaxException | MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
