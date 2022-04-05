package com.liftPlzz.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.liftPlzz.R;
import com.liftPlzz.adapter.ViewPagerAdapter;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.SocialImage;
import com.liftPlzz.model.createProfile.Response;
import com.liftPlzz.model.createProfile.User;
import com.liftPlzz.model.getVehicle.Datum;
import com.liftPlzz.presenter.UpdateProfilePresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.UpdateProfileView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateProfileFragment extends BaseFragment<UpdateProfilePresenter, UpdateProfileView> implements UpdateProfileView {


    SharedPreferences sharedPreferences;
    @BindView(R.id.imageViewBackContact)
    ImageView imageViewBackContact;

    @BindView(R.id.editTextName)
    AppCompatEditText editTextName;
    @BindView(R.id.editTextDesignation)
    AppCompatEditText editTextDesignation;
    @BindView(R.id.editTextMobile)
    AppCompatEditText editTextMobile;
    @BindView(R.id.editTextEmail)
    AppCompatEditText editTextEmail;

    @BindView(R.id.editsosnumber)
    AppCompatEditText editsosnumber;

    @BindView(R.id.editTextAboutUser)
    AppCompatEditText editTextAboutUser;
    @BindView(R.id.txtDOB)
    AppCompatTextView txtDOB;

    @BindView(R.id.buttonUpdate)
    AppCompatButton buttonUpdate;

    @BindView(R.id.imageViewAddImage)
    AppCompatImageView imageViewAddImage;
    @BindView(R.id.viewPagerMain)
    ViewPager viewPagerMain;
    ViewPagerAdapter mViewPagerAdapter;
    final Calendar myCalendar = Calendar.getInstance();

    static User user;

    public static void setUser(User user) {
        UpdateProfileFragment.user = user;
    }

    @Override
    protected int createLayout() {
        return R.layout.fragment_update_profile;
    }

    @Override
    protected void setPresenter() {
        presenter = new UpdateProfilePresenter();
    }


    @Override
    protected UpdateProfileView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sosnumbers();

        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (user != null) {
            editTextName.setText(user.getName());
            editTextEmail.setText(user.getEmail());
            editTextMobile.setText(user.getMobile());
            editTextDesignation.setText(user.getDesignation());
            editTextAboutUser.setText(user.getAboutMe());
            mViewPagerAdapter = new ViewPagerAdapter(getActivity(), user.getSocialImages());

            // Adding the Adapter to the ViewPager
            viewPagerMain.setAdapter(mViewPagerAdapter);
        }
    }

    private int mYear, mMonth, mDay, mHour, mMinute;

    @OnClick({R.id.imageViewBackContact, R.id.buttonUpdate, R.id.imageViewAddImage, R.id.txtDOB})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewAddImage:
                ImagePicker.Companion.with(this)
                        .crop(1080, 700)                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
                break;
            case R.id.imageViewBackContact:
                getActivity().onBackPressed();
                break;
            case R.id.txtDOB:
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDOB.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

                break;
            case R.id.buttonUpdate:
                if (editTextName.getText().toString().isEmpty()) {
                    showMessage("Please enter name");
                } else if (editTextDesignation.getText().toString().isEmpty()) {
                    showMessage("Please enter designation");
                } else if (editTextEmail.getText().toString().isEmpty()) {
                    showMessage("Please enter email");
                } else if (editTextMobile.getText().toString().isEmpty()) {
                    showMessage("Please enter mobile number");
                } else if (editsosnumber.getText().toString().isEmpty()) {
                    showMessage("Please enter Emergency number");
                } else if (editTextAboutUser.getText().toString().isEmpty()) {
                    showMessage("Please enter About Yourself");
                } else {
                    presenter.updateProfile(sharedPreferences.getString(Constants.TOKEN, ""), editTextName.getText().toString(), editTextDesignation.getText().toString(), editTextEmail.getText().toString(), editTextMobile.getText().toString(), editTextAboutUser.getText().toString(), editsosnumber.getText().toString());

                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Log.e("hdhdhd", "onActivityResult: " + data.getData().toString());
            File file = null;
            try {
                file = new File(new URL(data.getDataString()).toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

            RequestBody api_key = RequestBody.create(okhttp3.MultipartBody.FORM, Constants.API_KEY);
            RequestBody device = RequestBody.create(okhttp3.MultipartBody.FORM, "android");
            RequestBody token = RequestBody.create(okhttp3.MultipartBody.FORM, sharedPreferences.getString(Constants.TOKEN, ""));
            presenter.uploadImage(api_key, device, token, body);

            //File file= =ImagePicker.getFile(data);
        }
    }

    private void sosnumbers() {
        Constants.showLoader(getContext());
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest sr = new StringRequest(Request.Method.POST, "https://charpair.com/api/get-profile", new com.android.volley.Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                Constants.hideLoader();
                Log.d("history", response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    JSONObject responsee = jObject.getJSONObject("response");
                    JSONObject userdata = responsee.getJSONObject("user");
                    String so1s = userdata.getString("sos");
                    Log.d("sos", so1s);

                    editsosnumber.setText(so1s);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", Constants.API_KEY);
                params.put("client", Constants.ANDROID);
                params.put("token", sharedPreferences.getString(Constants.TOKEN, ""));
                //   params.put("token", "064ywr3Ht5LPpFPF73J0foCAdvw3ylSDXJys8IqATQ2wyvwimen827FAPA5I");
                return params;
            }
        };
        queue.add(sr);

    }


    @Override
    public void setProfileData(Response response) {
        getActivity().onBackPressed();
    }

    @Override
    public void setImageData(Response response) {
        user = response.getUser();
        mViewPagerAdapter = new ViewPagerAdapter(getActivity(), user.getSocialImages());
        // Adding the Adapter to the ViewPager
        viewPagerMain.setAdapter(mViewPagerAdapter);
    }

    public class ViewPagerAdapter extends PagerAdapter {

        // Context object
        Context context;

        // Array of images
        List<SocialImage> images;

        // Layout Inflater
        LayoutInflater mLayoutInflater;

        // Viewpager Constructor
        public ViewPagerAdapter(Context context, List<SocialImage> images) {
            this.context = context;
            this.images = images;
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // return the number of images
            return images.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((RelativeLayout) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            // inflating the item.xml
            View itemView = mLayoutInflater.inflate(R.layout.row_image_item, container, false);

            // referencing the image view from the item.xml file
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewMainDetails);
            ImageView img = (ImageView) itemView.findViewById(R.id.img);
            img.setVisibility(View.GONE);
            if (images.size() > 0) {
                Picasso.get().load(images.get(position).getImage()).into(imageView);
            } else {
                Picasso.get().load(R.drawable.images_no_found).into(imageView);
            }
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Do you want to cancel this lift?")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (images.get(position).getImageId() != null) {
//                                        itemListener.onDeleteClick(images.get(position).getImageId());
                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(context, "Cant delete this item", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.dismiss();

                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Alert");
                    alert.show();
                }
            });
            // Adding the View
            Objects.requireNonNull(container).addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((RelativeLayout) object);
        }

    }

}
