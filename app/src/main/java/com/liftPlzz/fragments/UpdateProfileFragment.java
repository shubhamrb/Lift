package com.liftPlzz.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
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
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.SocialImage;
import com.liftPlzz.model.createProfile.Response;
import com.liftPlzz.model.createProfile.User;
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
import java.util.ArrayList;
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
    @BindView(R.id.spinner_category)
    AppCompatSpinner spinner_category;
    @BindView(R.id.professionalTextView)
    AppCompatEditText professionalTextView;

    @BindView(R.id.otherTextView)
    AppCompatEditText otherTextView;
    @BindView(R.id.txtCMName)
    AppCompatEditText txtCMName;
    @BindView(R.id.editTextMobile)
    AppCompatTextView editTextMobile;
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
    @BindView(R.id.layoutProfessionalStatus)
    LinearLayout layoutProfessionalStatus;
    @BindView(R.id.layoutEditTextOther)
    LinearLayout layoutEditTextOther;

    @BindView(R.id.genderGroup)
    RadioGroup genderGroup;

    @BindView(R.id.toggle_email)
    AppCompatImageView toggle_email;
    @BindView(R.id.toggle_dob)
    AppCompatImageView toggle_dob;
    @BindView(R.id.toggle_mobile)
    AppCompatImageView toggle_mobile;

    ViewPagerAdapter mViewPagerAdapter;
    final Calendar myCalendar = Calendar.getInstance();
    String professionSelection = "";
    String professional = "";
    private int isEmailPrivate = 0;
    private int isDOBPrivate = 0;
    private int isMobilePrivate = 0;

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
            professionalTextView.setText(user.getDepartment());
            txtCMName.setText(user.getCompany());
            editTextEmail.setText(user.getEmail());
            txtDOB.setText(user.getDob());
            editTextMobile.setText(user.getMobile());
            professionalTextView.setText(user.getDepartment());
            editTextAboutUser.setText(user.getAboutMe());
            professional = user.getDesignation();
            if (!user.getGender().isEmpty()) {
                if (user.getGender().equals("Male")) {
                    genderGroup.check(R.id.maleRadio);
                } else if (user.getGender().equals("Female")) {
                    genderGroup.check(R.id.femaleRadio);
                } else if (user.getGender().equals("Not to Share")) {
                    genderGroup.check(R.id.otherRadio);
                }
            }

            mViewPagerAdapter = new ViewPagerAdapter(getActivity(), user.getSocialImages());
            // Adding the Adapter to the ViewPager
            viewPagerMain.setAdapter(mViewPagerAdapter);

            populateProfessionalDialog(professional);
        }
    }

    private int mYear, mMonth, mDay, mHour, mMinute;

    @OnClick({R.id.imageViewBackContact, R.id.buttonUpdate, R.id.imageViewAddImage, R.id.txtDOB
            , R.id.toggle_email, R.id.toggle_dob, R.id.toggle_mobile})
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
                        (view1, year, monthOfYear, dayOfMonth) -> txtDOB.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth), mYear, mMonth, mDay);
                datePickerDialog.show();

                break;
            case R.id.buttonUpdate:
                if (editTextName.getText().toString().isEmpty()) {
                    showMessage("Please enter name");
                } else /*if (editTextDesignation.getText().toString().isEmpty()) {
                    showMessage("Please enter designation");
                } else*/ if (editTextEmail.getText().toString().isEmpty()) {
                    showMessage("Please enter email");
                } else if (editTextMobile.getText().toString().isEmpty()) {
                    showMessage("Please enter mobile number");
                } /*else if (editsosnumber.getText().toString().isEmpty()) {
                    showMessage("Please enter Emergency number");
                } else if (editTextAboutUser.getText().toString().isEmpty()) {
                    showMessage("Please enter About Yourself");
                } */ /*else if (genderGroup.getCheckedRadioButtonId() == R.id.maleRadio ||
                        genderGroup.getCheckedRadioButtonId() == R.id.femaleRadio ||
                        genderGroup.getCheckedRadioButtonId() == R.id.otherRadio) {*/

                    String gender = "";
                    if (genderGroup.getCheckedRadioButtonId() == R.id.maleRadio) {
                        gender = "Male";
                    } else if (genderGroup.getCheckedRadioButtonId() == R.id.femaleRadio) {
                        gender = "Female";
                    } else if (genderGroup.getCheckedRadioButtonId() == R.id.otherRadio) {
                        gender = "Not to Share";
                    }

//                    if (spinner_category.getSelectedItemPosition() != 0) {
                        /*if (professionSelection.equals("Others") && otherTextView.getText().toString().trim().equals("")) {
                            Toast.makeText(getContext(), "Please fill above details", Toast.LENGTH_SHORT).show();
                        } else {*/
                            presenter.updateProfile(sharedPreferences.getString(Constants.TOKEN, ""),
                                    editTextName.getText().toString(),
                                    professionSelection,
                                    professionalTextView.getText().toString(),
                                    txtCMName.getText().toString(),
                                    txtDOB.getText().toString(),
                                    editTextEmail.getText().toString(),
                                    gender, editTextMobile.getText().toString(),
                                    editTextAboutUser.getText().toString(),
                                    editsosnumber.getText().toString(), isEmailPrivate, isDOBPrivate, isMobilePrivate);
//                        }
                    /*} else {
                        Toast.makeText(getContext(), "Please select one of them", Toast.LENGTH_SHORT).show();
                    }*/

               /* }
                else {
                    showMessage("Please Select Gender");
                }*/
                break;

            case R.id.toggle_email:
                if (getActivity() != null) {
                    if (isEmailPrivate == 0) {
                        isEmailPrivate = 1;
                        toggle_email.setImageDrawable(AppCompatResources.getDrawable(getActivity(), R.drawable.on));
                    } else {
                        isEmailPrivate = 0;
                        toggle_email.setImageDrawable(AppCompatResources.getDrawable(getActivity(), R.drawable.off));
                    }
                }
                break;

            case R.id.toggle_dob:
                if (getActivity() != null) {
                    if (isDOBPrivate == 0) {
                        isDOBPrivate = 1;
                        toggle_dob.setImageDrawable(AppCompatResources.getDrawable(getActivity(), R.drawable.on));
                    } else {
                        isDOBPrivate = 0;
                        toggle_dob.setImageDrawable(AppCompatResources.getDrawable(getActivity(), R.drawable.off));
                    }
                }
                break;

            case R.id.toggle_mobile:
                if (getActivity() != null) {
                    if (isMobilePrivate == 0) {
                        isMobilePrivate = 1;
                        toggle_mobile.setImageDrawable(AppCompatResources.getDrawable(getActivity(), R.drawable.on));
                    } else {
                        isMobilePrivate = 0;
                        toggle_mobile.setImageDrawable(AppCompatResources.getDrawable(getActivity(), R.drawable.off));
                    }
                }
                break;
        }
    }

    /**
     * Show Professional Status dialog
     */
    public void showProfessionalDialog(String professional) {
        professionSelection = "";
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_professional_dialog);
        TextView cancelBtn = dialog.findViewById(R.id.cancelBtn);
        TextView okayBtn = dialog.findViewById(R.id.okayBtn);
        TextView titleTxt = dialog.findViewById(R.id.titleTxt);
        EditText otherEdit = dialog.findViewById(R.id.otherEdit);
        AppCompatSpinner professionalSpinner = dialog.findViewById(R.id.professionalSpinner);
        ArrayList<String> list = new ArrayList<>();
        list.add("Select One");
        list.add("Sales");
        list.add("Marketing");
        list.add("HR");
        list.add("Account");
        list.add("Back Office");
        list.add("Govt. Employee");
        list.add("Self Employed");
        list.add("Production");
        list.add("Warehouse");
        list.add("Others");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, list);
        professionalSpinner.setAdapter(adapter);
        if (professional != null) {
            for (int i = 0; i < list.size(); i++) {
                if (professional.equals(list.get(i))) {
                    professionalSpinner.setSelection(i);
                    break;
                } else {
                    if (professional.equals("")) {
                        professionalSpinner.setSelection(0);
                    } else {
                        professionalSpinner.setSelection(i);
                    }
                }
            }
        }
        professionalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    if (list.get(position).equals("Others")) {
                        otherEdit.setVisibility(View.VISIBLE);
                        otherEdit.setText(professional);
                    } else {
                        otherEdit.setVisibility(View.GONE);
                    }
                    professionSelection = list.get(position);
                } else {
                    otherEdit.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cancelBtn.setOnClickListener(v -> {
            dialog.dismiss();
        });

        okayBtn.setOnClickListener(v -> {


        });
        dialog.show();
    }

    public void populateProfessionalDialog(String professional) {
        professionSelection = "";
        ArrayList<String> list = new ArrayList<>();
        list.add("Select One");
        list.add("Sales");
        list.add("Marketing");
        list.add("HR");
        list.add("Account");
        list.add("Back Office");
        list.add("Govt. Employee");
        list.add("Self Employed");
        list.add("Production");
        list.add("Warehouse");
        list.add("Others");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, list);
        spinner_category.setAdapter(adapter);
        if (professional != null) {
            for (int i = 0; i < list.size(); i++) {
                if (professional.equals(list.get(i))) {
                    spinner_category.setSelection(i);
                    break;
                } else {
                    if (professional.equals("")) {
                        spinner_category.setSelection(0);
                    } else {
                        spinner_category.setSelection(i);
                    }
                }
            }
        }
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    if (list.get(position).equals("Others")) {
                        layoutEditTextOther.setVisibility(View.VISIBLE);
                        otherTextView.setText(professional);
                    } else {
                        layoutEditTextOther.setVisibility(View.GONE);
                    }
                    professionSelection = list.get(position);
                } else {
                    layoutEditTextOther.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /*okayBtn.setOnClickListener(v -> {

            if (professionalSpinner.getSelectedItemPosition() != 0) {
                if (professionSelection.equals("Others")) {
                    if (otherEdit.getText().toString().trim().equals("")) {
                        Toast.makeText(getContext(), "Please fill above details", Toast.LENGTH_SHORT).show();
                    } else {
                        editTextDesignation.setText(otherEdit.getText().toString().trim());
                        dialog.dismiss();
                    }
                } else {
                    presenter.updateSetting(sharedPreferences.getString(Constants.TOKEN, ""), 8, professionSelection);
                    editTextDesignation.setText(professionSelection);
                    dialog.dismiss();
                }
            } else {
                Toast.makeText(getContext(), "Please select one of them", Toast.LENGTH_SHORT).show();
            }
        });*/
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
                    isEmailPrivate = userdata.getInt("is_email_public");
                    isDOBPrivate = userdata.getInt("is_dob_public");
                    isMobilePrivate = userdata.getInt("is_contact_public");
                    Log.d("sos", so1s);

                    if (getActivity() != null) {
                        if (isEmailPrivate == 1)
                            toggle_email.setImageDrawable(AppCompatResources.getDrawable(getActivity(), R.drawable.on));
                        else
                            toggle_email.setImageDrawable(AppCompatResources.getDrawable(getActivity(), R.drawable.off));

                        if (isDOBPrivate == 1)
                            toggle_dob.setImageDrawable(AppCompatResources.getDrawable(getActivity(), R.drawable.on));
                        else
                            toggle_dob.setImageDrawable(AppCompatResources.getDrawable(getActivity(), R.drawable.off));

                        if (isMobilePrivate == 1)
                            toggle_mobile.setImageDrawable(AppCompatResources.getDrawable(getActivity(), R.drawable.on));
                        else
                            toggle_mobile.setImageDrawable(AppCompatResources.getDrawable(getActivity(), R.drawable.off));
                    }

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
        sharedPreferences.edit().putString(Constants.NAME, String.valueOf(response.getUser().getName())).apply();
        sharedPreferences.edit().putString(Constants.EMAIL, String.valueOf(response.getUser().getEmail())).apply();
        sharedPreferences.edit().putString(Constants.IMAGE, String.valueOf(response.getUser().getImage())).apply();
        Objects.requireNonNull(getActivity()).onBackPressed();
    }

    @Override
    public void setUpdateSetting(Boolean status) {
        /*if (status) {
            String gender = "";
            if (genderGroup.getCheckedRadioButtonId() == R.id.maleRadio ||
                    genderGroup.getCheckedRadioButtonId() == R.id.femaleRadio ||
                    genderGroup.getCheckedRadioButtonId() == R.id.otherRadio) {
                if (genderGroup.getCheckedRadioButtonId() == R.id.maleRadio) {
                    gender = "Male";
                } else if (genderGroup.getCheckedRadioButtonId() == R.id.femaleRadio) {
                    gender = "Female";
                } else if (genderGroup.getCheckedRadioButtonId() == R.id.otherRadio) {
                    gender = "Not to Share";
                }
            }

            presenter.updateProfile(sharedPreferences.getString(Constants.TOKEN, ""),
                    editTextName.getText().toString(),
                    editTextDesignation.getText().toString(),
                    professionalTextView.getText().toString(),
                    txtCMName.getText().toString(),
                    txtDOB.getText().toString(),
                    editTextEmail.getText().toString(),
                    gender, editTextMobile.getText().toString(),
                    editTextAboutUser.getText().toString(),
                    editsosnumber.getText().toString(), isEmailPrivate, isDOBPrivate, isMobilePrivate);
        }*/
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
