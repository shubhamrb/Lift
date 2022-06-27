package com.liftPlzz.presenter;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.liftPlzz.R;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseActivity;
import com.liftPlzz.base.BasePresenter;
import com.liftPlzz.model.createProfile.CreateProfileMainResponse;
import com.liftPlzz.model.getsetting.SettingModel;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.UpdateProfileView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfilePresenter extends BasePresenter<UpdateProfileView> {


    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }


    /**
     * Update Input value with String format
     */
    public void updateSetting(String token, int settingId, String inputValue) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<SettingModel> call = api.updateUserSetting(Constants.API_KEY, Constants.ANDROID, token, settingId, inputValue);
        call.enqueue(new Callback<SettingModel>() {
            @Override
            public void onResponse(Call<SettingModel> call, Response<SettingModel> response) {
                view.hideLoader();
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        view.setUpdateSetting(response.body().getStatus());
                    } else {
                        view.showMessage(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<SettingModel> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage(throwable.getMessage());
            }
        });
    }

    public void updateProfile(String token, String name,
                              String deg, String email,
                              String gender,
                              String mobile,String aboutme,
                              String sos) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<CreateProfileMainResponse> call = api.edit_profile(Constants.API_KEY, "android", token, name, email,gender, mobile, deg, aboutme,sos);
        call.enqueue(new Callback<CreateProfileMainResponse>() {
            @Override
            public void onResponse(Call<CreateProfileMainResponse> call, Response<CreateProfileMainResponse> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.body().getResponse().getStatus()) {
                        view.setProfileData(response.body().getResponse());
                    } else {
                        //  view.hideLoader();
                        view.showMessage(response.body().getResponse().getMessage());
                    }


                }
            }


            @Override
            public void onFailure(Call<CreateProfileMainResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage(throwable.getMessage());
            }
        });
    }

//    private void updateprofile(){
//        Constants.showLoader(getContext());
//        RequestQueue queue = Volley.newRequestQueue(getContext());
//        StringRequest sr = new StringRequest(Request.Method.POST, "https://charpair.com/api/my-balance", new com.android.volley.Response.Listener<String>() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onResponse(String response) {
//                Constants.hideLoader();
//                Log.d("getpointsresponse", response);
//
//                try {
//                    JSONObject jObject = new JSONObject(response);
//                    String points = jObject.getString("points");
//                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
//                    // ...Irrelevant code for customizing the buttons and title
//                    LayoutInflater inflater = getLayoutInflater();
//                    dialogBuilder.setTitle("Available Points");
//                    dialogBuilder.setCancelable(true);
//                    dialogBuilder.setPositiveButton("Ok",(dialogInterface, i) -> {
//                    });
//                    View dialogView= inflater.inflate(R.layout.user_points_layout, null);
//                    dialogBuilder.setView(dialogView);
//                    TextView pointstext = dialogView.findViewById(R.id.pointstextviewetxt);
//                    pointstext.setText(points);
//                    AlertDialog alertDialog =  dialogBuilder.create();
//                    alertDialog.show();
//                    dialogBuilder.setPositiveButton("Close",(dialogInterface, i) -> {
//                        alertDialog.hide();
//                    });
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("api_key", Constants.API_KEY);
//                params.put("client", Constants.ANDROID);
//                params.put("token", strToken);
////                params.put("token", "064ywr3Ht5LPpFPF73J0foCAdvw3ylSDXJys8IqATQ2wyvwimen827FAPA5I");
////                params.put("api_key", "070b92d28adc166b3a6c63c2d44535d2f62a3e24");
////                params.put("client", "android");
////                params.put("token", "NRy4MvEaDj5O04r8S6GGSZAJ7T5tv1QvS969rtgyYe7qdyKv8q6wjWBozH5I");
////                params.put("request_id", "57");
//
//                return params;
//            }
//        };
//        queue.add(sr);
//
//    }


    public void uploadImage(RequestBody api_key, RequestBody device, RequestBody token, MultipartBody.Part email) {
        view.showLoader();
        ApiService api = RetroClient.getApiService();
        Call<CreateProfileMainResponse> call = api.add_social_image(api_key, device, token, email);
        call.enqueue(new Callback<CreateProfileMainResponse>() {
            @Override
            public void onResponse(Call<CreateProfileMainResponse> call, Response<CreateProfileMainResponse> response) {
                view.hideLoader();

                if (response.body() != null) {
                    if (response.body().getResponse().getStatus()) {
                        view.setImageData(response.body().getResponse());
                    } else {
                        //  view.hideLoader();
                        view.showMessage(response.body().getResponse().getMessage());
                    }


                }
            }

            @Override
            public void onFailure(Call<CreateProfileMainResponse> call, Throwable throwable) {
                view.hideLoader();
                view.showMessage(throwable.getMessage());
            }
        });
    }

    public void openUpdateProfile() {
        navigator.openUpdateProfileFragment(BaseActivity.PerformFragment.REPLACE);
    }
}
