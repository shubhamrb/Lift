package com.liftPlzz.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.liftPlzz.R;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.model.createTicket.ResponseCreateTicket;
import com.liftPlzz.model.getTicketCategory.ResponseTicketCategory;
import com.liftPlzz.utils.Constants;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateTicketActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.et_email)
    AppCompatEditText editEmail;
    @BindView(R.id.et_subject)
    AppCompatEditText editSubject;
    @BindView(R.id.et_description)
    AppCompatEditText editDescription;
    @BindView(R.id.spinner_category)
    AppCompatSpinner spinnerCategory;
    @BindView(R.id.btn_create)
    AppCompatButton btnCreateTicket;
    private String strEmail = "", strSubject = "", strDescription = "", strCategory = "", strToken = "";
    SharedPreferences sharedPreferences;
    //    private List<TicketCategoryData> categoryDataList = new ArrayList<>();
    private ArrayList<String> arrayListCategory = new ArrayList<>();
    ArrayAdapter<String> adapterCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ticket);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        toolBarTitle.setText(getResources().getString(R.string.create_ticket));
        getTicketCategory();
    }

    @OnClick({R.id.imageViewBack, R.id.btn_create})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                onBackPressed();
                break;

            case R.id.btn_create:
                strEmail = editEmail.getText().toString();
                strSubject = editSubject.getText().toString();
                strCategory = spinnerCategory.getSelectedItem().toString();
                strDescription = editDescription.getText().toString();
                if (strEmail.equalsIgnoreCase("")) {
                    Toast.makeText(this, getResources().getString(R.string.please_enter_email), Toast.LENGTH_SHORT).show();
                } else if (strSubject.equalsIgnoreCase("")) {
                    Toast.makeText(this, getResources().getString(R.string.please_enter_subject), Toast.LENGTH_SHORT).show();
                } else if (strCategory.equalsIgnoreCase("")) {
                    Toast.makeText(this, getResources().getString(R.string.please_enter_category), Toast.LENGTH_SHORT).show();
                } else if (strDescription.equalsIgnoreCase("")) {
                    Toast.makeText(this, getResources().getString(R.string.please_enter_description), Toast.LENGTH_SHORT).show();
                } else {
                    createTicketApi(strEmail, strSubject, strCategory, strDescription);
                }
                break;

        }
    }


    /**
     * This method is used to create ticket
     *
     * @param strEmail
     * @param strSubject
     * @param strCategory
     * @param strDescription
     */
    public void createTicketApi(String strEmail, String strSubject, String strCategory, String strDescription) {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<ResponseCreateTicket> call = api.createTicket(strEmail, strSubject, strCategory, strDescription, Constants.API_KEY, getResources().getString(R.string.android), strToken);
        call.enqueue(new Callback<ResponseCreateTicket>() {
            @Override
            public void onResponse(Call<ResponseCreateTicket> call, Response<ResponseCreateTicket> response) {
                Constants.hideLoader();
                if (response.code() == 200) {
                    try {
                        String message = response.body().getMessage();
                        if (response.body().getStatus()) {
                            Toast.makeText(CreateTicketActivity.this, message, Toast.LENGTH_SHORT).show();
                            editEmail.setText("");
                            editSubject.setText("");
                            editDescription.setText("");
//                            editCategory.setText("");
                            finish();
                        } else {
                            Toast.makeText(CreateTicketActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.optString("message");
                        Toast.makeText(CreateTicketActivity.this, message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseCreateTicket> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(CreateTicketActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void getTicketCategory() {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<ResponseTicketCategory> call = api.getTicketCategory();
        call.enqueue(new Callback<ResponseTicketCategory>() {
            @Override
            public void onResponse(Call<ResponseTicketCategory> call, Response<ResponseTicketCategory> response) {
                Constants.hideLoader();
                if (response.code() == 200) {
                    try {
                        String message = response.body().getMessage();
                        if (response.body().getStatus()) {
                            arrayListCategory.clear();
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                arrayListCategory.add(response.body().getData().get(i).getCategory());
                            }
                            adapterCategory = new ArrayAdapter<>(CreateTicketActivity.this, android.R.layout.simple_spinner_item, arrayListCategory);
                            adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCategory.setAdapter(adapterCategory);
//                            Toast.makeText(CreateTicketActivity.this, message, Toast.LENGTH_SHORT).show();
//                            categoryDataList.clear();
//                            categoryDataList.addAll(response.body().getData());
                        } else {
                            Toast.makeText(CreateTicketActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.optString("message");
                        Toast.makeText(CreateTicketActivity.this, message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(Call<ResponseTicketCategory> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(CreateTicketActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}

