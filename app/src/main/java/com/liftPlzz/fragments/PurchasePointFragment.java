package com.liftPlzz.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.liftPlzz.R;
import com.liftPlzz.adapter.PaymentPackageAdapter;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.pointsRedemption.CardModel;
import com.liftPlzz.model.pointsRedemption.PointsModel;
import com.liftPlzz.model.recharge.RechargeHistory;
import com.liftPlzz.model.recharge.RechargeHistoryResponse;
import com.liftPlzz.presenter.PurchasePointPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.PurchasePointView;
import com.razorpay.Checkout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PurchasePointFragment extends BaseFragment<PurchasePointPresenter, PurchasePointView> implements PurchasePointView {

    SharedPreferences sharedPreferences;
    @BindView(R.id.recyclerViewpackage)
    RecyclerView recyclerViewpackage;
    @BindView(R.id.buttonRecharge)
    AppCompatButton buttonRecharge;
    @BindView(R.id.pointtext)
    TextView pointtext;
    @BindView(R.id.nametext)
    TextView nametext;
    @BindView(R.id.card_number)
    TextView card_number;
    @BindView(R.id.validity_text)
    TextView validity_text;
    String strToken = "";
    ArrayList<RechargeHistory> rechargeHistories;
    String description;
    String adaptoramount;

    private int IMAGE_TYPE = 0;
    private File fileTrans = null;
    private MultipartBody.Part transBody = null;
    private ImageView verified_img;

    @Override
    protected int createLayout() {
        return R.layout.fragment_payment_recharge_package;
    }

    @Override
    protected void setPresenter() {
        presenter = new PurchasePointPresenter();
    }


    @Override
    protected PurchasePointView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        strToken = sharedPreferences.getString(Constants.TOKEN, "");

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver, new IntentFilter("message_subject_intent"));

        rechargeHistories = new ArrayList<>();
        getPointsDetail();

        buttonRecharge.setOnClickListener(view -> showRechargeDialog());
    }

    private void getPointsDetail() {
        Constants.showLoader(getActivity());
        ApiService api = RetroClient.getApiService();
        Call<PointsModel> call = api.cardDetail(Constants.API_KEY, Constants.ANDROID, strToken);
        call.enqueue(new Callback<PointsModel>() {
            @Override
            public void onResponse(Call<PointsModel> call, Response<PointsModel> response) {
                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        CardModel cardModel = response.body().getCardModel();
                        if (cardModel != null) {
                            pointtext.setText(String.format(Locale.getDefault(), "%d", cardModel.getCurrent_point()));
                            nametext.setText(cardModel.getFull_name());
                            String card_no = cardModel.getCard_number();

                            if (card_number.length() != 16) {
                                card_number.setText("0000 **** **** 0000");
                            } else {
                                card_number.setText(card_no.substring(0, 4) + " **** **** " + card_no.substring(12));//2305 0000 0004 1111
                            }
                            String card_val = cardModel.getCard_expiry();
                            validity_text.setText(card_val.split("-")[1] + "/" + card_val.split("-")[0].substring(2));

                            getRechargeHistory();
                        }
                    } else {
                        Constants.hideLoader();
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PointsModel> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showRechargeDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.recharge_dialog);
        AppCompatButton buttonSubmit = dialog.findViewById(R.id.buttonSubmit);
        EditText editTextPoints = dialog.findViewById(R.id.editTextPoints);
        EditText editTextDescription = dialog.findViewById(R.id.editTextDescription);
        AppCompatTextView upload_img = dialog.findViewById(R.id.upload_img);
        verified_img = dialog.findViewById(R.id.verified_img);


        upload_img.setOnClickListener(view -> {
            ImagePicker.Companion.with(this).crop()                    //Crop image(Optional), Check Customization for more option
                    .compress(1024)            //Final image size will be less than 1 MB(Optional)
                    .maxResultSize(1080, 1080).start();
            IMAGE_TYPE = 0;
        });

        buttonSubmit.setOnClickListener(v -> {
            String amount = editTextPoints.getText().toString();
            String description = editTextDescription.getText().toString();

            if (amount.trim().equals("")) {
                Toast.makeText(getActivity(), "Please enter amount", Toast.LENGTH_SHORT).show();
                return;
            }
            if (description.trim().equals("")) {
                Toast.makeText(getActivity(), "Please enter description", Toast.LENGTH_SHORT).show();
                return;
            }

            if (transBody == null) {
                Toast.makeText(getActivity(), "Please upload the transaction image.", Toast.LENGTH_SHORT).show();
                return;
            }
            RequestBody api_key = RequestBody.create(MultipartBody.FORM, Constants.API_KEY);
            RequestBody device = RequestBody.create(MultipartBody.FORM, "android");
            RequestBody token = RequestBody.create(MultipartBody.FORM, strToken);
            RequestBody amountBody = RequestBody.create(MultipartBody.FORM, amount);
            RequestBody descriptionBody = RequestBody.create(MultipartBody.FORM, description);

//              rechargeFuelCard(editTextPoints.getText().toString().trim());
            presenter.rechargeRequest(api_key, device, token, transBody, amountBody, descriptionBody);

            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (IMAGE_TYPE == 0) {
                try {
                    fileTrans = new File(new URL(data.getDataString()).toURI());
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileTrans);
                    transBody = MultipartBody.Part.createFormData("transaction_image", fileTrans.getName(), requestFile);
                    verified_img.setImageTintList(ColorStateList.valueOf(getContext().getResources().getColor(R.color.quantum_googgreen)));
                } catch (URISyntaxException | MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void rechargeFuelCard(String points) {
        description = points + " points purchase.";
        Intent intent = new Intent("message_subject_intent");
        intent.putExtra("amountfromadaptor", points);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
        int amount = Math.round(Float.parseFloat(points) * 100);
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_sbtMx1SKiekIfR");
        JSONObject object = new JSONObject();
        try {
            object.put("name", "");
            object.put("description", description);
            object.put("theme.color", "");
            object.put("currency", "INR");
            object.put("amount", amount);
            object.put("prefill.contact", "");
            object.put("prefill.email", "");

            checkout.open(getActivity(), object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            adaptoramount = intent.getStringExtra("amountfromadaptor");
            Log.d("str", adaptoramount);
        }
    };


    private void getRechargeHistory() {
        ApiService api = RetroClient.getApiService();
        Call<RechargeHistoryResponse> call = api.rechargeWalletHistory(Constants.API_KEY, "android", strToken);
        call.enqueue(new Callback<RechargeHistoryResponse>() {
            @Override
            public void onResponse(Call<RechargeHistoryResponse> call, Response<RechargeHistoryResponse> response) {
                Constants.hideLoader();

                if (response.body() != null) {
                    if (response.code() == 200) {
                        rechargeHistories.clear();
                        rechargeHistories.addAll(response.body().getRechargeHistories());
                        PaymentPackageAdapter adapter = new PaymentPackageAdapter(getActivity(), rechargeHistories);
                        recyclerViewpackage.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerViewpackage.setAdapter(adapter);
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<RechargeHistoryResponse> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void onPaymentSuccess(String s) {
        Toast.makeText(getActivity(), "Payment Successful", Toast.LENGTH_SHORT).show();
        Constants.showLoader(getActivity());
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, "https://charpair.com/api/add-money", new com.android.volley.Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                Constants.hideLoader();
                Log.d("addmoney", response);
                getPointsDetail();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constants.hideLoader();
                Log.d("addmoneyerror", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", Constants.API_KEY);
                params.put("client", Constants.ANDROID);
                params.put("token", strToken);
                params.put("amount", adaptoramount);
                params.put("source", description);
                params.put("destination", "wallet_points");
                params.put("transaction_id", s);
                params.put("remarks", "online payment gateway used");
                params.put("status", "success");

                Log.d("last description", description);
                //   params.put("token", "064ywr3Ht5LPpFPF73J0foCAdvw3ylSDXJys8IqATQ2wyvwimen827FAPA5I");
                return params;
            }
        };
        queue.add(sr);

    }

    public void onPaymentError(int i, String s) {
//        Toast.makeText(this, "error" + s, Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "Payment Failed", Toast.LENGTH_SHORT).show();
        Constants.showLoader(getActivity());
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, "https://charpair.com/api/add-money", new com.android.volley.Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                Constants.hideLoader();
                Log.d("addmoney", response);
                getPointsDetail();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Constants.hideLoader();
                Log.d("addmoneyerror", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", Constants.API_KEY);
                params.put("client", Constants.ANDROID);
                params.put("token", strToken);
                params.put("amount", adaptoramount);
                params.put("source", description);
                params.put("destination", "wallet_points");
                params.put("transaction_id", s);
                params.put("remarks", "online payment gateway used");
                params.put("status", "failed");

                Log.d("last description", description);
                //   params.put("token", "064ywr3Ht5LPpFPF73J0foCAdvw3ylSDXJys8IqATQ2wyvwimen827FAPA5I");
                return params;
            }
        };
        queue.add(sr);
    }

    @Override
    public void rechargeRequestSubmit(String message) {
        new AlertDialog.Builder(getActivity()).setTitle("Recharge Request.").setMessage(message).setPositiveButton("OK", (dialog, whichButton) -> {

            try {
                rechargeHistories.clear();
                getPointsDetail();
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        }).show();
    }
}
