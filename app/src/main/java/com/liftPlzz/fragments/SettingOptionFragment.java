package com.liftPlzz.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.liftPlzz.R;
import com.liftPlzz.activity.ChatActivity;
import com.liftPlzz.activity.TicketListActivity;
import com.liftPlzz.adapter.SettingOptionAdapter;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.chatuser.ChatUser;
import com.liftPlzz.model.createProfile.User;
import com.liftPlzz.model.getsetting.Datum;
import com.liftPlzz.model.getsetting.SettingModel;
import com.liftPlzz.presenter.SettingPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.SettingView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingOptionFragment extends BaseFragment<SettingPresenter, SettingView> implements SettingView, SettingOptionAdapter.ItemListener {

    SharedPreferences sharedPreferences;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    @BindView(R.id.recyclerViewSetting)
    RecyclerView recyclerViewSetting;
    @BindView(R.id.imageViewHome)
    ImageView imageViewHome;
    String strToken = "";
    String professionSelection = "";
    String vehicleSelection = "";
    String pointPerKmSelection = "";
    String sosSelection = "";
    private User userData;
    private int id;

    @Override
    protected int createLayout() {
        return R.layout.setting_fragment;
    }

    @Override
    protected void setPresenter() {
        presenter = new SettingPresenter();
    }

    @Override
    protected SettingView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        imageViewHome.setVisibility(View.VISIBLE);
        Bundle bundle = getArguments();

        if (bundle != null) {
            String title = bundle.getString("title");
            id = bundle.getInt("id");

            toolBarTitle.setText(title);
            strToken = sharedPreferences.getString(Constants.TOKEN, "");

            presenter.getSettingList(strToken);
        }
    }

    private void setSettingsOption(List<Datum> settingData) {
        /*Datum d1 = new Datum();
        d1.setId(settingData.size() + 1);
        d1.setOptionType("");
        d1.setType("Account Setting");
        d1.setOptionType("action");

        Datum d2 = new Datum();
        d2.setId(settingData.size() + 2);
        d2.setOptionType("");
        d2.setType("Block User");
        d2.setOptionType("action");

        Datum d3 = new Datum();
        d3.setId(settingData.size() + 3);
        d3.setOptionType("");
        d3.setType("Reset to default");
        d3.setOptionType("action");
        settingData.add(d1);
        settingData.add(d2);
        settingData.add(d3);*/

        recyclerViewSetting.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSetting.setAdapter(new SettingOptionAdapter(getContext(), settingData, this));

    }

    @OnClick({R.id.imageViewBack, R.id.imageViewHome})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                getActivity().onBackPressed();
                break;
            case R.id.imageViewHome:
                ((AppCompatActivity) getActivity()).getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
        }
    }

    @Override
    public void setSettings(SettingModel model) {
        List<Datum> setData = new ArrayList<>();
        for (Datum datum : model.getData()) {
            if (datum.getSetting_title_id() == id) {
                setData.add(datum);
            }
        }
        setSettingsOption(setData);
    }

    @Override
    public void setProfileData(com.liftPlzz.model.createProfile.Response response) {
        userData = response.getUser();
        if (userData != null) {
            UpdateProfileFragment.setUser(userData);
        }
        presenter.openUpdateProfile();
    }

    @Override
    public void onSuccessReset(JsonObject jsonObject) {
        showMessage(jsonObject.get("message").getAsString());
    }

    @Override
    public void onToggleClick(int settingId, int inputValue) {
        updateSetting(strToken, settingId, inputValue);
    }

    @Override
    public void onSelectOption(int position, Datum data) {
        if (data.getShortCode().equals("gender_preference")) {
            showLiftWhomDialog(data);
        } else if (data.getShortCode().equals("age_preference")) {
            showAgeDialog(data);
        } else if (data.getShortCode().equals("professional_preference")) {
            showProfessionalDialog(data);
        } else if (data.getShortCode().equals("vehicle_preference")) {
            showVehicleDialog(data);
        } else if (data.getShortCode().equals("point_per_km")) {
            showPointKmDialog(data);
        } else if (data.getShortCode().equals("sos")) {
            showSosDialog(data);
        }
    }

    @Override
    public void onSelectAction(int position, Datum data) {
        if (data.getType().trim().equals("Account Setting")) {
            presenter.getProfile(strToken);
        } else if (data.getType().trim().equals("Reset to default")) {
            //reset setting
            new AlertDialog.Builder(getActivity())
                    .setTitle("Reset to default")
                    .setMessage("Do you really want to reset to default?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            presenter.reset(strToken);
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }

    @Override
    public void onOtherScreen(int position, Datum data) {
        if (data.getShortCode().equals("rating_preference")) {
            showRatingDialog(data);
        } else if (data.getShortCode().equals("block_user")) {
            presenter.openBlock();
        } else if (data.getShortCode().equals("edit_profile")) {
            presenter.getProfile(strToken);
        } else if (data.getShortCode().equals("suspend_account")) {
            //
        } else if (data.getShortCode().equals("faq")) {
            presenter.openFaq();
        } else if (data.getShortCode().equals("how_to_use_charpair")) {
            presenter.openHowToUse();
        } else if (data.getShortCode().equals("report_problem_issue")) {
            startActivity(new Intent(getActivity(), TicketListActivity.class));
        } else if (data.getShortCode().equals("contact_with_us")) {
            showCCDialog();
        } else if (data.getShortCode().equals("customer_support")) {
            ChatUser user = new ChatUser();
            user.setId(0);
            user.setName("CharPair Support");
            user.setMobile("9876543210");
            user.setImage("https://charpair.com/website/assets/img/Char_Pair_logo.jpg");

            Intent intent = new Intent(getActivity(), ChatActivity.class);
            intent.putExtra(Constants.USER_ID, String.valueOf(user.getId()));
            intent.putExtra("charuser", new Gson().toJson(user));
            startActivity(intent);
        }
    }

    public void showCCDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customer_care_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        AppCompatTextView tv_number = dialog.findViewById(R.id.tv_number);
        AppCompatTextView tv_email = dialog.findViewById(R.id.tv_email);
        tv_number.setText("9876543210");
        tv_email.setText("support@charpair.com ");
        dialog.show();
    }

    /**
     * Show SOS dialog
     */
    public void showSosDialog(Datum data) {
        sosSelection = "";
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_professional_dialog);
        TextView cancelBtn = dialog.findViewById(R.id.cancelBtn);
        TextView okayBtn = dialog.findViewById(R.id.okayBtn);
        TextView titleTxt = dialog.findViewById(R.id.titleTxt);
        EditText otherEdit = dialog.findViewById(R.id.otherEdit);
        AppCompatSpinner professionalSpinner = dialog.findViewById(R.id.professionalSpinner);
        otherEdit.setHint("SOS");
        professionalSpinner.setVisibility(View.GONE);
        otherEdit.setVisibility(View.VISIBLE);
        if (data.getSelectedValue() != null) {
            otherEdit.setText(data.getSelectedValue());
        }

        titleTxt.setText(data.getType());

        cancelBtn.setOnClickListener(v -> {
            dialog.dismiss();
        });

        okayBtn.setOnClickListener(v -> {
            sosSelection = otherEdit.getText().toString();
            if (!sosSelection.isEmpty()) {
                updateSetting(strToken, data.getId(), sosSelection);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * Show Point Per KM dialog
     */
    public void showPointKmDialog(Datum data) {
        pointPerKmSelection = "";
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_professional_dialog);
        TextView cancelBtn = dialog.findViewById(R.id.cancelBtn);
        TextView okayBtn = dialog.findViewById(R.id.okayBtn);
        TextView titleTxt = dialog.findViewById(R.id.titleTxt);
        AppCompatSpinner professionalSpinner = dialog.findViewById(R.id.professionalSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, data.getOption_value());
        professionalSpinner.setAdapter(adapter);
        if (data.getSelectedValue() != null) {
            for (int i = 0; i < data.getOption_value().size(); i++) {
                if (data.getSelectedValue().equals(data.getOption_value().get(i))) {
                    professionalSpinner.setSelection(i);
                    break;
                } else {
                    if (data.getSelectedValue().equals("0")) {
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
                pointPerKmSelection = data.getOption_value().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        titleTxt.setText(data.getType());

        cancelBtn.setOnClickListener(v -> {
            dialog.dismiss();
        });

        okayBtn.setOnClickListener(v -> {
            if (pointPerKmSelection != null && !pointPerKmSelection.isEmpty()) {
                updateSetting(strToken, data.getId(), pointPerKmSelection);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * Show Lift whom dialog
     */
    public void showLiftWhomDialog(Datum data) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_whom_dialog);

        RadioGroup genderRadioGroup = dialog.findViewById(R.id.genderRadioGroup);
        RadioButton maleRadio = dialog.findViewById(R.id.maleRadio);
        RadioButton femaleRadio = dialog.findViewById(R.id.femaleRadio);
        RadioButton anyRadio = dialog.findViewById(R.id.anyRadio);
        TextView cancelBtn = dialog.findViewById(R.id.cancelBtn);
        TextView okayBtn = dialog.findViewById(R.id.okayBtn);
        TextView titleTxt = dialog.findViewById(R.id.titleTxt);
        titleTxt.setText(data.getType());

        if (data.getSelectedValue() != null) {
            if (!data.getSelectedValue().equals("0")) {
                if (data.getSelectedValue().equals("Male")) {
                    maleRadio.setChecked(true);
                } else if (data.getSelectedValue().equals("Female")) {
                    maleRadio.setChecked(true);
                } else {
                    anyRadio.setChecked(true);
                }
            }
        }

        cancelBtn.setOnClickListener(v -> {
            dialog.dismiss();
        });

        okayBtn.setOnClickListener(v -> {
            if (genderRadioGroup.getCheckedRadioButtonId() == -1) {
                Toast.makeText(getContext(), "Please select one of them.", Toast.LENGTH_LONG).show();
            } else {
                String type = "";
                if (genderRadioGroup.getCheckedRadioButtonId() == R.id.maleRadio) {
                    type = "Male";
                } else if (genderRadioGroup.getCheckedRadioButtonId() == R.id.femaleRadio) {
                    type = "Female";
                } else if (genderRadioGroup.getCheckedRadioButtonId() == R.id.anyRadio) {
                    type = "Any";
                }
                dialog.dismiss();
                updateSetting(strToken, data.getId(), type.trim());
            }
        });
        dialog.show();
    }

    /**
     * Show Age dialog
     */
    public void showAgeDialog(Datum data) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_age_dialog);

        TextView cancelBtn = dialog.findViewById(R.id.cancelBtn);
        TextView okayBtn = dialog.findViewById(R.id.okayBtn);
        TextView titleTxt = dialog.findViewById(R.id.titleTxt);
        EditText mini = dialog.findViewById(R.id.mini);
        EditText maxi = dialog.findViewById(R.id.maxi);
        titleTxt.setText(data.getType());

        if (data.getSelectedValue() != null) {
            if (!data.getSelectedValue().equals("0")) {
                try {
                    JSONObject object = new JSONObject(data.getSelectedValue());
                    mini.setText(object.getString("min"));
                    maxi.setText(object.getString("max"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        cancelBtn.setOnClickListener(v -> {
            dialog.dismiss();
        });

        okayBtn.setOnClickListener(v -> {
            if (!mini.getText().toString().equals("") && !maxi.getText().toString().equals("")) {
                if (Integer.parseInt(mini.getText().toString()) != 0 && Integer.parseInt(maxi.getText().toString()) != 0) {
                    if (Integer.parseInt(mini.getText().toString()) < Integer.parseInt(maxi.getText().toString())) {
                        dialog.dismiss();
                        JSONObject object = new JSONObject();
                        try {
                            object.accumulate("min", mini.getText().toString());
                            object.accumulate("max", maxi.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        updateSetting(strToken, data.getId(), object);
                    } else {
                        Toast.makeText(getContext(), "Please enter correct format", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Please enter correct format", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Please enter minimum and maximum values", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    /**
     * Show Rating dialog
     */
    public void showRatingDialog(Datum data) {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_rating_dialog);
        TextView cancelBtn = dialog.findViewById(R.id.cancelBtn);
        TextView okayBtn = dialog.findViewById(R.id.okayBtn);
        TextView titleTxt = dialog.findViewById(R.id.titleTxt);
        RatingBar ratingBr = dialog.findViewById(R.id.ratingBr);

        if (data != null && data.getSelectedValue() != null) {
            titleTxt.setText(data.getType());
            if (!data.getSelectedValue().equals("") && !data.getSelectedValue().equals("0")) {
                ratingBr.setRating(Float.parseFloat(data.getSelectedValue()));
            }
        }

        cancelBtn.setOnClickListener(v -> {
            dialog.dismiss();
        });

        okayBtn.setOnClickListener(v -> {
            dialog.dismiss();
            if (data != null)
                updateSetting(strToken, data.getId(), "" + ratingBr.getRating());
        });
        dialog.show();
    }

    /**
     * Show Vehicle Status dialog
     */
    public void showVehicleDialog(Datum data) {
        vehicleSelection = "";
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_professional_dialog);
        TextView cancelBtn = dialog.findViewById(R.id.cancelBtn);
        TextView okayBtn = dialog.findViewById(R.id.okayBtn);
        TextView titleTxt = dialog.findViewById(R.id.titleTxt);
        AppCompatSpinner professionalSpinner = dialog.findViewById(R.id.professionalSpinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, data.getOption_value());
        professionalSpinner.setAdapter(adapter);
        if (data.getSelectedValue() != null) {
            for (int i = 0; i < data.getOption_value().size(); i++) {
                if (data.getSelectedValue().equals(data.getOption_value().get(i))) {
                    professionalSpinner.setSelection(i);
                    break;
                } else {
                    if (data.getSelectedValue().equals("0")) {
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
                vehicleSelection = data.getOption_value().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        titleTxt.setText(data.getType());

        cancelBtn.setOnClickListener(v -> {
            dialog.dismiss();
        });

        okayBtn.setOnClickListener(v -> {
            if (vehicleSelection != null && !vehicleSelection.isEmpty()) {
                updateSetting(strToken, data.getId(), vehicleSelection);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * Show Professional Status dialog
     */
    public void showProfessionalDialog(Datum data) {
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
        if (data.getSelectedValue() != null) {
            for (int i = 0; i < list.size(); i++) {
                if (data.getSelectedValue().equals(list.get(i))) {
                    professionalSpinner.setSelection(i);
                    break;
                } else {
                    if (data.getSelectedValue().equals("0")) {
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
                        otherEdit.setText(data.getSelectedValue());
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

        titleTxt.setText(data.getType());

        cancelBtn.setOnClickListener(v -> {
            dialog.dismiss();
        });

        okayBtn.setOnClickListener(v -> {

            if (professionalSpinner.getSelectedItemPosition() != 0) {
                if (professionSelection.equals("Others")) {
                    if (otherEdit.getText().toString().trim().equals("")) {
                        Toast.makeText(getContext(), "Please fill above details", Toast.LENGTH_SHORT).show();
                    } else {
                        updateSetting(strToken, data.getId(), otherEdit.getText().toString().trim());
                        dialog.dismiss();
                    }
                } else {
                    updateSetting(strToken, data.getId(), professionSelection);
                    dialog.dismiss();
                }
            } else {
                Toast.makeText(getContext(), "Please select one of them", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    /**
     * Update Toggle setting API
     */
    public void updateSetting(String token, int settingId, int inputValue) {
        Constants.showLoader(getActivity());
        ApiService api = RetroClient.getApiService();
        Call<SettingModel> call = api.updateUserSetting(Constants.API_KEY, Constants.ANDROID, token, settingId, inputValue);
        call.enqueue(new Callback<SettingModel>() {
            @Override
            public void onResponse(Call<SettingModel> call, Response<SettingModel> response) {
                Constants.hideLoader();
                if (response.body() != null) {
                    if (response.body().getStatus()) {
//                        view.setSettings(response.body().getData());
                        presenter.getSettingList(strToken);
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SettingModel> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Update Input value with String format
     */
    public void updateSetting(String token, int settingId, String inputValue) {
        Constants.showLoader(getActivity());
        ApiService api = RetroClient.getApiService();
        Call<SettingModel> call = api.updateUserSetting(Constants.API_KEY, Constants.ANDROID, token, settingId, inputValue);
        call.enqueue(new Callback<SettingModel>() {
            @Override
            public void onResponse(Call<SettingModel> call, Response<SettingModel> response) {
                Constants.hideLoader();
                if (response.body() != null) {
                    if (response.body().getStatus()) {
//                        view.setSettings(response.body().getData());
                        presenter.getSettingList(strToken);
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SettingModel> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Update Input value with JSON Object format
     */
    public void updateSetting(String token, int settingId, JSONObject inputValue) {
        Constants.showLoader(getActivity());
        ApiService api = RetroClient.getApiService();
        Call<SettingModel> call = api.updateUserSetting(Constants.API_KEY, Constants.ANDROID, token, settingId, inputValue);
        call.enqueue(new Callback<SettingModel>() {
            @Override
            public void onResponse(Call<SettingModel> call, Response<SettingModel> response) {
                Constants.hideLoader();
                if (response.body() != null) {
                    if (response.body().getStatus()) {
//                        view.setSettings(response.body().getData());
                        presenter.getSettingList(strToken);
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<SettingModel> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
