package com.liftPlzz.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.liftPlzz.R;
import com.liftPlzz.adapter.VehicleSubCategoryAdapter;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.createVehicle.Response;
import com.liftPlzz.model.getVehicle.Datum;
import com.liftPlzz.model.vehiclesubcategory.SubCategoryResponse;
import com.liftPlzz.presenter.CarPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.CarView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * A simple {@link Fragment} subclass.
 */
public class CarFragment extends BaseFragment<CarPresenter, CarView> implements CarView, DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    @BindView(R.id.editTextVehicleModel)
    AppCompatEditText editTextVehicleModel;
    @BindView(R.id.editTextVehicleRegNo)
    AppCompatEditText editTextVehicleRegNo;
    @BindView(R.id.ed_rate_per)
    AppCompatEditText edRatePerKm;
    @BindView(R.id.editTextVehicleInsuranceDate)
    AppCompatTextView editTextVehicleInsuranceDate;
    @BindView(R.id.checkBoxVehicleMakeDefault)
    AppCompatCheckBox checkBoxVehicleMakeDefault;
    @BindView(R.id.layoutSave)
    LinearLayout layoutSave;
    @BindView(R.id.textViewSeat1)
    AppCompatTextView textViewSeat1;
    @BindView(R.id.textViewSeat2)
    AppCompatTextView textViewSeat2;
    @BindView(R.id.textViewSeat3)
    AppCompatTextView textViewSeat3;
    @BindView(R.id.textViewSeat4)
    AppCompatTextView textViewSeat4;
    @BindView(R.id.textViewSeat5)
    AppCompatTextView textViewSeat5;
    @BindView(R.id.textViewSeat6)
    AppCompatTextView textViewSeat6;
    @BindView(R.id.textViewSeat7)
    AppCompatTextView textViewSeat7;

    @BindView(R.id.imageViewFrontImage)
    AppCompatImageView imageViewFrontImage;
    @BindView(R.id.imageViewBackImage)
    AppCompatImageView imageViewBackImage;
    @BindView(R.id.imageViewRcImage)
    AppCompatImageView imageViewRcImage;
    @BindView(R.id.spinner_category)
    AppCompatSpinner spinnerCategory;


    SharedPreferences sharedPreferences;
    Calendar calendar;
    int day, month, year;
    int myday, myMonth, myYear;
    String seat;
    int VehicleType, vehicleSubCategoryId, vehicleSubCategoryEdit;
    File fileFront = null;
    File fileBack = null;
    File fileRC = null;
    public static Datum vehicleData;
    public static boolean isEdit = false;
    private String strToken = "";
    VehicleSubCategoryAdapter vehicleSubCategoryAdapter;

    public static void setVehicleData(Datum vehicleData) {
        CarFragment.vehicleData = vehicleData;
    }

    public static void setIsEdit(boolean isEdit) {
        CarFragment.isEdit = isEdit;
    }

    @Override
    protected int createLayout() {
        return R.layout.fragment_car;
    }

    @Override
    protected void setPresenter() {
        presenter = new CarPresenter();
    }


    @Override
    protected CarView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        seat = "1";
        textViewSeat1.setSelected(true);
        textViewSeat2.setSelected(false);
        textViewSeat3.setSelected(false);
        textViewSeat4.setSelected(false);
        textViewSeat5.setSelected(false);
        textViewSeat6.setSelected(false);
        textViewSeat7.setSelected(false);
        presenter.getSubCategory(Constants.API_KEY, getResources().getString(R.string.android),
                strToken, getResources().getString(R.string.four_wheeler));
        spinnerCategory.setOnItemSelectedListener(this);
        InputFilter[] editFilters = editTextVehicleRegNo.getFilters();
        InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
        System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
        newFilters[editFilters.length] = new InputFilter.AllCaps();
        editTextVehicleRegNo.setFilters(newFilters);
        if (isEdit) {
            editTextVehicleModel.setText(vehicleData.getModel());
            edRatePerKm.setText("" + vehicleData.getRatePerKm());
            editTextVehicleRegNo.setText(vehicleData.getRegistrationNo());

            editTextVehicleInsuranceDate.setText(vehicleData.getInsurance_date());
            vehicleSubCategoryEdit = vehicleData.getVehicleSubcategoryId();
            if (vehicleData.getVehicleImageFront() != null) {
                Picasso.get().load(vehicleData.getVehicleImageFront()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        imageViewFrontImage.setImageBitmap(bitmap);
                        fileFront = null;
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }

            if (vehicleData.getVehicleImageBack() != null) {
                Picasso.get().load(vehicleData.getVehicleImageBack()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        imageViewBackImage.setImageBitmap(bitmap);
                        fileBack = null;
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }

            if (vehicleData.getRcImage() != null) {
                Picasso.get().load(vehicleData.getRcImage()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        imageViewRcImage.setImageBitmap(bitmap);
                        fileRC = null;
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }
            if (vehicleData.getIsDefault() != null) {
                if (vehicleData.getIsDefault() == 1) {
                    checkBoxVehicleMakeDefault.setChecked(true);
                } else {
                    checkBoxVehicleMakeDefault.setChecked(false);
                }
            }
        }
    }

    @OnClick({R.id.imageViewBackImage, R.id.imageViewFrontImage, R.id.imageViewRcImage, R.id.layoutSave, R.id.editTextVehicleInsuranceDate, R.id.textViewSeat1, R.id.textViewSeat2, R.id.textViewSeat3, R.id.textViewSeat4, R.id.textViewSeat5, R.id.textViewSeat6, R.id.textViewSeat7})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layoutSave:
                if (editTextVehicleModel.getText().toString().isEmpty()) {
                    showMsg("Please enter vehicle model");
                } else if (editTextVehicleRegNo.getText().toString().isEmpty()) {
                    showMsg("Please enter vehicle registration number");
                } else if (editTextVehicleInsuranceDate.getText().toString().isEmpty()) {
                    showMsg("Please select vehicle Insurance Date ");
                } else if (edRatePerKm.getText().toString().isEmpty()) {
                    showMsg(getResources().getString(R.string.please_enter_rate_per_km));
                }
               /* else if (!isEdit && fileFront == null) {
                    showMsg("Please select vehicle Front Image");
                } else if (!isEdit && fileBack == null) {
                    showMsg(getResources().getString(R.string.please_select_vehicle_back_image));
                } else if (!isEdit && fileRC == null) {
                    showMsg(getResources().getString(R.string.please_select_vehicle_rc_image));
                } */
                else {
                    MultipartBody.Part vehicle_image_frontBody = null;
                    MultipartBody.Part vehicle_image_backBody = null;
                    MultipartBody.Part rc_imageBody = null;
                    if (fileFront != null) {
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileFront);
                        vehicle_image_frontBody = fileFront != null ? MultipartBody.Part.createFormData("vehicle_image_front", fileFront.getName(), requestFile) : null;
                    }

                    if (fileBack != null) {
                        RequestBody requestFileVehicle_image_back = RequestBody.create(MediaType.parse("multipart/form-data"), fileBack);
                        vehicle_image_backBody = fileBack != null ? MultipartBody.Part.createFormData("vehicle_image_back", fileBack.getName(), requestFileVehicle_image_back) : null;
                    }

                    if (fileRC != null) {
                        RequestBody requestFileRc_image = RequestBody.create(MediaType.parse("multipart/form-data"), fileRC);
                        rc_imageBody = fileRC != null ? MultipartBody.Part.createFormData("rc_image", fileRC.getName(), requestFileRc_image) : null;
                    }

                    RequestBody api_key = RequestBody.create(okhttp3.MultipartBody.FORM, Constants.API_KEY);
                    RequestBody device = RequestBody.create(okhttp3.MultipartBody.FORM, getResources().getString(R.string.android));
                    RequestBody types = RequestBody.create(okhttp3.MultipartBody.FORM, getResources().getString(R.string.four_wheeler));
                    RequestBody token = RequestBody.create(okhttp3.MultipartBody.FORM, strToken);
                    RequestBody model = RequestBody.create(okhttp3.MultipartBody.FORM, editTextVehicleModel.getText().toString());
                    RequestBody RegNo = RequestBody.create(okhttp3.MultipartBody.FORM, editTextVehicleRegNo.getText().toString());
                    RequestBody vehicleSubCategory = RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(vehicleSubCategoryId));
                    RequestBody insuranceDate = RequestBody.create(okhttp3.MultipartBody.FORM, editTextVehicleInsuranceDate.getText().toString());
                    RequestBody ratePerKm = RequestBody.create(okhttp3.MultipartBody.FORM, edRatePerKm.getText().toString());
                    RequestBody seats = RequestBody.create(okhttp3.MultipartBody.FORM, seat);
                    RequestBody is_default = RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(checkBoxVehicleMakeDefault.isChecked() ? 1 : 0));
                    if (isEdit) {
                        RequestBody vehicle_id = RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(vehicleData.getId()));
                        presenter.updateVehicle(api_key, device, token, vehicle_id, types, model, RegNo,
                                insuranceDate, ratePerKm, seats, is_default, vehicleSubCategory, vehicle_image_frontBody, vehicle_image_backBody, rc_imageBody);

                    } else {
                        presenter.createVehicle(api_key, device, token, types, model, RegNo,
                                insuranceDate, ratePerKm, seats, is_default, vehicleSubCategory, vehicle_image_frontBody, vehicle_image_backBody, rc_imageBody);
                    }
                }
                break;
            case R.id.editTextVehicleInsuranceDate:
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_InputMethod, CarFragment.this, year, month, day);
                datePickerDialog.show();
                break;
            case R.id.imageViewFrontImage:
                ImagePicker.Companion.with(this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
                VehicleType = 1;
                break;
            case R.id.imageViewBackImage:
                ImagePicker.Companion.with(this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
                VehicleType = 2;
                break;
            case R.id.imageViewRcImage:
                ImagePicker.Companion.with(this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
                VehicleType = 3;
                break;
            case R.id.textViewSeat1:
                seat = "1";
                textViewSeat1.setSelected(true);
                textViewSeat2.setSelected(false);
                textViewSeat3.setSelected(false);
                textViewSeat4.setSelected(false);
                textViewSeat5.setSelected(false);
                textViewSeat6.setSelected(false);
                textViewSeat7.setSelected(false);
                break;
            case R.id.textViewSeat2:
                seat = "2";
                textViewSeat1.setSelected(false);
                textViewSeat2.setSelected(true);
                textViewSeat3.setSelected(false);
                textViewSeat4.setSelected(false);
                textViewSeat5.setSelected(false);
                textViewSeat6.setSelected(false);
                textViewSeat7.setSelected(false);
                break;
            case R.id.textViewSeat3:
                seat = "3";
                textViewSeat1.setSelected(false);
                textViewSeat2.setSelected(false);
                textViewSeat3.setSelected(true);
                textViewSeat4.setSelected(false);
                textViewSeat5.setSelected(false);
                textViewSeat6.setSelected(false);
                textViewSeat7.setSelected(false);
                break;
            case R.id.textViewSeat4:
                seat = "4";
                textViewSeat1.setSelected(false);
                textViewSeat2.setSelected(false);
                textViewSeat3.setSelected(false);
                textViewSeat4.setSelected(true);
                textViewSeat5.setSelected(false);
                textViewSeat6.setSelected(false);
                textViewSeat7.setSelected(false);
                break;
            case R.id.textViewSeat5:
                seat = "5";
                textViewSeat1.setSelected(false);
                textViewSeat2.setSelected(false);
                textViewSeat3.setSelected(false);
                textViewSeat4.setSelected(false);
                textViewSeat5.setSelected(true);
                textViewSeat6.setSelected(false);
                textViewSeat7.setSelected(false);
                break;
            case R.id.textViewSeat6:
                seat = "6";
                textViewSeat1.setSelected(false);
                textViewSeat2.setSelected(false);
                textViewSeat3.setSelected(false);
                textViewSeat4.setSelected(false);
                textViewSeat5.setSelected(false);
                textViewSeat6.setSelected(true);
                textViewSeat7.setSelected(false);
                break;
            case R.id.textViewSeat7:
                seat = "7";
                textViewSeat1.setSelected(false);
                textViewSeat2.setSelected(false);
                textViewSeat3.setSelected(false);
                textViewSeat4.setSelected(false);
                textViewSeat5.setSelected(false);
                textViewSeat6.setSelected(false);
                textViewSeat7.setSelected(true);
                break;
        }
    }

    private File savebitmap(String filename, Bitmap bitmap) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;

        File file = new File(extStorageDirectory, filename + ".png");
        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("file", "" + file);
        return file;

    }

    @Override
    public void createVehicle(Response response) {
        getActivity().onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Log.e("hdhdhd", "onActivityResult: " + data.getData().toString());
            if (VehicleType == 1) {
                try {
                    Picasso.get().load(data.getData()).into(imageViewFrontImage);
                    fileFront = new File(new URL(data.getDataString()).toURI());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (VehicleType == 2) {
                try {
                    Picasso.get().load(data.getData()).into(imageViewBackImage);
                    fileBack = new File(new URL(data.getDataString()).toURI());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Picasso.get().load(data.getData()).into(imageViewRcImage);
                    fileRC = new File(new URL(data.getDataString()).toURI());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //File file= =ImagePicker.getFile(data);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        myYear = year;
        myday = dayOfMonth;
        myMonth = month;
        Calendar myCalender = Calendar.getInstance();
        myCalender.set(myYear, myMonth, myday);
        editTextVehicleInsuranceDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(myCalender.getTime()));
    }

    @Override
    public void getSubCategory(SubCategoryResponse response) {
        vehicleSubCategoryAdapter = new VehicleSubCategoryAdapter(getActivity(), response.getData());
        spinnerCategory.setAdapter(vehicleSubCategoryAdapter);
        if (isEdit) {
            for (int i = 0; i < response.getData().size(); i++) {
                if (response.getData().get(i).getId().equals(vehicleSubCategoryEdit)) {
                    spinnerCategory.setSelection(i);
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        vehicleSubCategoryId = (Integer) vehicleSubCategoryAdapter.getItem(position);
//        vehicleSubCategoryId = selectedId;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
