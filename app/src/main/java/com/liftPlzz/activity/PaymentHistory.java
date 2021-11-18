package com.liftPlzz.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class PaymentHistory extends AppCompatActivity {

    ArrayList<PaymentHistoryModel> paymentHistoryModels;
    private String strToken;
    SharedPreferences sharedPreferences;
    ImageView imageView;
    TextView nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        Log.d("tokenq", strToken);
        imageView = findViewById(R.id.imageViewBackhistory);
        nodata = findViewById(R.id.nodata);
        Log.d("tokenn", strToken);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentHistory.this, HomeActivity.class);
                startActivity(intent);
            }
        });

       paymentHistoryModels = new ArrayList<>();
        getPaymentHistory();
    }

    private void getPaymentHistory(){
        Constants.showLoader(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest sr = new StringRequest(Request.Method.POST, "https://charpair.com/api/account-history", new com.android.volley.Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {
                Constants.hideLoader();
                Log.d("history", response);
                try {
                    JSONObject jObject = new JSONObject(response);
                    JSONArray packagedata = jObject.getJSONArray("data");
                    for (int i = 0;i<packagedata.length();i++){
                        JSONObject dataobj = packagedata.getJSONObject(i);
                        String amount = dataobj.getString("amount");
                        String status = dataobj.getString("status");
                        String source = dataobj.getString("source");
                        String datetime = dataobj.getString("created_at");
                        String type = dataobj.getString("type");

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        SimpleDateFormat output = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");

                        Date date = formatter.parse(datetime);
                        String formattedDate = output.format(date);
                        Log.d("formattedDate", formattedDate);

                        if (packagedata.length()==0){
                            nodata.setVisibility(View.VISIBLE);
                        }else{
                            nodata.setVisibility(View.INVISIBLE);
                        }

                        if (status.equals("success")){
                            paymentHistoryModels.add(new PaymentHistoryModel("Payment Successfull",amount,"By "+source,formattedDate,type));
                        }else{
                            paymentHistoryModels.add(new PaymentHistoryModel(status,amount,"By "+source,formattedDate,type));
                        }

                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                        PaymentHistoryAdatper adapter = new PaymentHistoryAdatper(paymentHistoryModels);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(adapter);

                        Log.d("amount", amount);
                        Log.d("poi", status);
                        Log.d("descccc", source);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("api_key", Constants.API_KEY);
                params.put("client", Constants.ANDROID);
                params.put("token", strToken);
//                params.put("token", "064ywr3Ht5LPpFPF73J0foCAdvw3ylSDXJys8IqATQ2wyvwimen827FAPA5I");
//                params.put("api_key", "070b92d28adc166b3a6c63c2d44535d2f62a3e24");
//                params.put("client", "android");
//                params.put("token", "NRy4MvEaDj5O04r8S6GGSZAJ7T5tv1QvS969rtgyYe7qdyKv8q6wjWBozH5I");
//                params.put("request_id", "57");

                return params;
            }
            };
        queue.add(sr);
    }
}