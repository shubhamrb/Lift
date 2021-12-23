package com.liftPlzz.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.liftPlzz.R;
import com.liftPlzz.adapter.PaymentHistoryAdatper;
import com.liftPlzz.adapter.PaymentPackageAdapter;
import com.liftPlzz.model.PaymentHistoryModel;
import com.liftPlzz.model.PaymentPackageModel;
import com.liftPlzz.utils.Constants;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentPackage extends AppCompatActivity implements PaymentResultListener {

    ArrayList<PaymentPackageModel> paymentPackageModels;
    ImageView imageView;
    private String strToken;
    SharedPreferences sharedPreferences;
    String amount;
    String description;
    String adaptoramount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_payment_recharge_package);
        imageView = findViewById(R.id.imageViewBackrecharge);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,new IntentFilter("message_subject_intent"));
//        str = getIntent().getStringExtra("amountfromadaptor");
////        Log.d("str", str);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentPackage.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        paymentPackageModels = new ArrayList<>();
        getPaymentpackge();

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            adaptoramount = intent.getStringExtra("amountfromadaptor");
            Log.d("str", adaptoramount);
//            Toast.makeText(PaymentPackage.this, name, Toast.LENGTH_SHORT).show();
        }
    };


    private void getPaymentpackge(){
        Constants.showLoader(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.GET, "https://charpair.com/api/recharge-package", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Constants.hideLoader();
                Log.d("package", response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    JSONArray packagedata = jObject.getJSONArray("data");
                    for (int i = 0;i<packagedata.length();i++){
                        JSONObject dataobj = packagedata.getJSONObject(i);
                        amount = dataobj.getString("amount");
                        String points = dataobj.getString("point");
                        description = dataobj.getString("description");
                        Log.d("amount", amount);
                        paymentPackageModels.add(new PaymentPackageModel(amount,"Points : You will recieve "+points+ " points","Details : "+description));

                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewpackage);
                        PaymentPackageAdapter adapter = new PaymentPackageAdapter(PaymentPackage.this,paymentPackageModels);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(adapter);

                        Log.d("amount", amount);
                        Log.d("poi", points);
                        Log.d("descccc", description);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(sr);
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "Payment Successfull", Toast.LENGTH_SHORT).show();
        Constants.showLoader(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "https://charpair.com/api/add-money", new com.android.volley.Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                Constants.hideLoader();
                Log.d("addmoney", response);
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

    @Override
    public void onPaymentError(int i, String s) {
//        Toast.makeText(this, "error" + s, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
    }
}