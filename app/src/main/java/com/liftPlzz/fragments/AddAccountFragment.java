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
import com.liftPlzz.presenter.AddAccountPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.AddAccountView;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddAccountFragment extends BaseFragment<AddAccountPresenter, AddAccountView> implements AddAccountView {

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

    @BindView(R.id.upload_cheque)
    AppCompatTextView upload_cheque;

    @BindView(R.id.upload_pan)
    AppCompatTextView upload_pan;

    @BindView(R.id.verified_cheque)
    ImageView verified_cheque;

    @BindView(R.id.verified_pan)
    ImageView verified_pan;

    @BindView(R.id.editTextAccount)
    AppCompatEditText editTextAccount;

    @BindView(R.id.editTextBankName)
    AppCompatEditText editTextBankName;

    @BindView(R.id.editTextIfsc)
    AppCompatEditText editTextIfsc;

    @BindView(R.id.editTextName)
    AppCompatEditText editTextName;

    String strToken = "";
    private int IMAGE_TYPE = 1;
    private File fileCheque = null, filePan = null;
    private MultipartBody.Part chequeBody = null, panBody = null;

    @Override
    protected int createLayout() {
        return R.layout.fragment_add_account;
    }

    @Override
    protected void setPresenter() {
        presenter = new AddAccountPresenter();
    }

    @Override
    protected AddAccountView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        toolBarTitle.setText("Add Bank");
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        imageViewHome.setVisibility(View.VISIBLE);

    }

    @OnClick({R.id.imageViewBack, R.id.imageViewHome, R.id.buttonSubmit, R.id.upload_cheque, R.id.upload_pan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                getActivity().onBackPressed();
                break;
            case R.id.imageViewHome:
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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

                if (chequeBody == null) {
                    Toast.makeText(getActivity(), "Please upload your Cancelled Cheque.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (panBody == null) {
                    Toast.makeText(getActivity(), "Please upload your PAN.", Toast.LENGTH_SHORT).show();
                    return;
                }

                String bankName = editTextBankName.getText().toString();
                String account = editTextAccount.getText().toString();
                String ifsc = editTextIfsc.getText().toString();
                String name = editTextName.getText().toString();

                if (bankName.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter your bank name.", Toast.LENGTH_SHORT).show();
                    return;
                }

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
                RequestBody bankNameBody = RequestBody.create(MultipartBody.FORM, bankName);
                RequestBody accountBody = RequestBody.create(MultipartBody.FORM, account);
                RequestBody ifscBody = RequestBody.create(MultipartBody.FORM, ifsc);
                RequestBody nameBody = RequestBody.create(MultipartBody.FORM, name);

                presenter.addBankAccount(api_key, device, token, chequeBody, panBody, bankNameBody, accountBody, ifscBody, nameBody);
                break;
        }
    }

    @Override
    public void onSubmit(String message) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Account Details Added.")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, whichButton) -> {
                    dialog.dismiss();
                    getActivity().onBackPressed();
                }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (IMAGE_TYPE == 2) {
                try {
                    fileCheque = new File(new URL(data.getDataString()).toURI());
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileCheque);
                    chequeBody = MultipartBody.Part.createFormData("cheque_img", fileCheque.getName(), requestFile);
                    verified_cheque.setImageTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.quantum_googgreen)));
                } catch (URISyntaxException | MalformedURLException e) {
                    e.printStackTrace();
                }
            } else if (IMAGE_TYPE == 3) {
                try {
                    filePan = new File(new URL(data.getDataString()).toURI());
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), filePan);
                    panBody = MultipartBody.Part.createFormData("pan_img", filePan.getName(), requestFile);
                    verified_pan.setImageTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.quantum_googgreen)));
                } catch (URISyntaxException | MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
