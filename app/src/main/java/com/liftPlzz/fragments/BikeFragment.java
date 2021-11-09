package com.liftPlzz.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.liftPlzz.R;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.createVehicle.Response;
import com.liftPlzz.model.getVehicle.Datum;
import com.liftPlzz.presenter.BikePresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.BikeView;
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
public class BikeFragment extends BaseFragment<BikePresenter, BikeView> implements BikeView, DatePickerDialog.OnDateSetListener {


    @BindView(R.id.editTextVehicleModel)
    AppCompatEditText editTextVehicleModel;
    @BindView(R.id.editTextVehicleRegNo)
    AppCompatEditText editTextVehicleRegNo;
    @BindView(R.id.editTextVehicleInsuranceDate)
    AppCompatTextView editTextVehicleInsuranceDate;

    @BindView(R.id.ed_rate_per_km)
    AppCompatEditText edRatePerKm;
    @BindView(R.id.checkBoxVehicleMakeDefault)
    AppCompatCheckBox checkBoxVehicleMakeDefault;
    @BindView(R.id.layoutSave)
    LinearLayout layoutSave;
    SharedPreferences sharedPreferences;
    Calendar calendar;
    int day, month, year;
    int myday, myMonth, myYear;
    String seat = "1";
    int VehicleType;
    File fileFront = null;
    File fileBack = null;
    File fileRC = null;

    @BindView(R.id.imageViewFrontImage)
    AppCompatImageView imageViewFrontImage;
    @BindView(R.id.imageViewBackImage)
    AppCompatImageView imageViewBackImage;
    @BindView(R.id.imageViewRcImage)
    AppCompatImageView imageViewRcImage;
    public static Datum vehicleData;
    public static boolean isEdit = false;

    public static void setVehicleData(Datum vehicleData) {
        BikeFragment.vehicleData = vehicleData;
    }

    public static void setIsEdit(boolean isEdit) {
        BikeFragment.isEdit = isEdit;
    }

    @Override
    protected int createLayout() {
        return R.layout.fragment_bike;
    }

    @Override
    protected void setPresenter() {
        presenter = new BikePresenter();
    }


    @Override
    protected BikeView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        if (isEdit) {
            editTextVehicleModel.setText(vehicleData.getModel());
            editTextVehicleRegNo.setText(vehicleData.getRegistrationNo());
            editTextVehicleInsuranceDate.setText(vehicleData.getInsurance_date());
            edRatePerKm.setText("" + vehicleData.getRatePerKm());
            if (vehicleData.getVehicleImageFront() != null) {
                Picasso.get().load(vehicleData.getVehicleImageFront()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        imageViewFrontImage.setImageBitmap(bitmap);
                        fileFront = savebitmap("FrontImage" + System.currentTimeMillis(), bitmap);
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
                        fileBack = savebitmap("BackImage" + System.currentTimeMillis(), bitmap);
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
                        fileRC = savebitmap("RcImage" + System.currentTimeMillis(), bitmap);
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

    @OnClick({R.id.imageViewBackImage, R.id.imageViewFrontImage, R.id.imageViewRcImage, R.id.layoutSave, R.id.editTextVehicleInsuranceDate})
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
                } else if (fileFront == null) {
                    showMsg("Please select vehicle Front Image");
                } else if (fileBack == null) {
                    showMsg("Please select vehicle Back Image");
                } else if (fileRC == null) {
                    showMsg("Please select vehicle RC Image");
                } else {
                    //rc_image vehicle_image_back vehicle_image_front
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileFront);
                    MultipartBody.Part vehicle_image_frontBody = MultipartBody.Part.createFormData("vehicle_image_front", fileFront.getName(), requestFile);
                    RequestBody requestFileVehicle_image_back = RequestBody.create(MediaType.parse("multipart/form-data"), fileBack);
                    MultipartBody.Part vehicle_image_backBody = MultipartBody.Part.createFormData("vehicle_image_back", fileBack.getName(), requestFileVehicle_image_back);
                    RequestBody requestFileRc_image = RequestBody.create(MediaType.parse("multipart/form-data"), fileFront);
                    MultipartBody.Part rc_imageBody = MultipartBody.Part.createFormData("rc_image", fileFront.getName(), requestFileRc_image);
                    RequestBody api_key = RequestBody.create(okhttp3.MultipartBody.FORM, Constants.API_KEY);
                    RequestBody device = RequestBody.create(okhttp3.MultipartBody.FORM, "android");
                    RequestBody types = RequestBody.create(okhttp3.MultipartBody.FORM, "two_wheeler");
                    RequestBody token = RequestBody.create(okhttp3.MultipartBody.FORM, sharedPreferences.getString(Constants.TOKEN, ""));
                    RequestBody model = RequestBody.create(okhttp3.MultipartBody.FORM, editTextVehicleModel.getText().toString());
                    RequestBody RegNo = RequestBody.create(okhttp3.MultipartBody.FORM, editTextVehicleRegNo.getText().toString());
                    RequestBody vehicleSubCategory = RequestBody.create(okhttp3.MultipartBody.FORM, "");
                    RequestBody insuranceDate = RequestBody.create(okhttp3.MultipartBody.FORM, editTextVehicleInsuranceDate.getText().toString());
                    RequestBody seats = RequestBody.create(okhttp3.MultipartBody.FORM, seat);
                    RequestBody ratePerKm = RequestBody.create(okhttp3.MultipartBody.FORM, edRatePerKm.getText().toString());
                    RequestBody is_default = RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(checkBoxVehicleMakeDefault.isChecked() ? 1 : 0));

                    if (isEdit) {
                        RequestBody vehicle_id = RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(vehicleData.getId()));
                        presenter.updateVehicle(api_key, device, token, vehicle_id, types, model, RegNo,
                                ratePerKm,insuranceDate, seats, is_default, vehicleSubCategory, vehicle_image_frontBody, vehicle_image_backBody, rc_imageBody);

                    } else {
                        presenter.createVehicle(api_key, device, token, types, model, RegNo,
                                ratePerKm, insuranceDate, seats, is_default, vehicleSubCategory, vehicle_image_frontBody, vehicle_image_backBody, rc_imageBody);
                    }
                }
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
                        .compress(1024)
                        //Final image size will be less than 1 MB(Optional)
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
            case R.id.editTextVehicleInsuranceDate:
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), BikeFragment.this, year, month, day);
                datePickerDialog.show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Log.e("hdhdhd", "onActivityResult: " + data.getData().toString());
            if (VehicleType == 1) {
                try {
                    fileFront = new File(new URL(data.getDataString()).toURI());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Picasso.get().load(data.getData()).into(imageViewFrontImage);
            } else if (VehicleType == 2) {
                Picasso.get().load(data.getData()).into(imageViewBackImage);
                try {
                    fileBack = new File(new URL(data.getDataString()).toURI());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } else {
                Picasso.get().load(data.getData()).into(imageViewRcImage);
                try {
                    fileRC = new File(new URL(data.getDataString()).toURI());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            //File file= =ImagePicker.getFile(data);
        }
    }

    @Override
    public void createVehicle(Response response) {
        getActivity().onBackPressed();
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
}
