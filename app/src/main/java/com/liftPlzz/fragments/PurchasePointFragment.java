package com.liftPlzz.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
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
    String strToken = "";
    ArrayList<RechargeHistory> rechargeHistories;
    String description;
    String adaptoramount;

    private int IMAGE_TYPE = 0;
    private File fileTrans = null;
    private MultipartBody.Part transBody = null;
    private ImageView verified_img;

    private boolean upiShow, barcodeShow, bankShow;
    private ActivityResultLauncher<String[]> somePermissionResultLauncher;

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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        somePermissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), (Map<String, Boolean> isGranted) -> {
            boolean granted = true;
            for (Map.Entry<String, Boolean> x : isGranted.entrySet()) {
                if (!x.getValue()) granted = false;
            }
            if (granted) {
                //download
                new DownloadsImage().execute("https://charpair.com/charpair_barcode.pdf");
            } else {
//                Toast.makeText(getContext(), "Permission Denied.", Toast.LENGTH_SHORT).show();
            }

        });
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
                            pointtext.setText(String.format(Locale.getDefault(), "%d Pair Points", cardModel.getCurrent_point()));
                            nametext.setText(cardModel.getFull_name());
                            String card_no = cardModel.getCard_number();

                            if (card_number.length() != 16) {
                                card_number.setText("0000 **** **** 0000");
                            } else {
                                card_number.setText(card_no.substring(0, 4) + " **** **** " + card_no.substring(12));//2305 0000 0004 1111
                            }
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
        Window window = dialog.getWindow();
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        AppCompatButton buttonSubmit = dialog.findViewById(R.id.buttonSubmit);
        EditText editTextPoints = dialog.findViewById(R.id.editTextPoints);
        EditText editTextDescription = dialog.findViewById(R.id.editTextDescription);
        AppCompatTextView upload_img = dialog.findViewById(R.id.upload_img);
        verified_img = dialog.findViewById(R.id.verified_img);

        RelativeLayout btn_upi = dialog.findViewById(R.id.btn_upi);
        LinearLayout ll_upi = dialog.findViewById(R.id.ll_upi);

        RelativeLayout btn_barcode = dialog.findViewById(R.id.btn_barcode);
        LinearLayout ll_barcode = dialog.findViewById(R.id.ll_barcode);

        RelativeLayout btn_bank = dialog.findViewById(R.id.btn_bank);
        LinearLayout ll_bank = dialog.findViewById(R.id.ll_bank);
        ImageView copy_upi = dialog.findViewById(R.id.copy_upi);
        ImageView copy_account = dialog.findViewById(R.id.copy_account);
        ImageView copy_ifsc = dialog.findViewById(R.id.copy_ifsc);
        TextView btn_download = dialog.findViewById(R.id.btn_download);
        ImageView btn_close = dialog.findViewById(R.id.btn_close);


        btn_close.setOnClickListener(view -> {
            dialog.dismiss();
        });

        btn_upi.setOnClickListener(view -> {
            if (upiShow) {
                ll_upi.setVisibility(View.GONE);
            } else {
                ll_upi.setVisibility(View.VISIBLE);
            }
            ll_barcode.setVisibility(View.GONE);
            ll_bank.setVisibility(View.GONE);
            upiShow = !upiShow;
            barcodeShow = false;
            bankShow = false;
        });

        btn_barcode.setOnClickListener(view -> {
            if (barcodeShow) {
                ll_barcode.setVisibility(View.GONE);
            } else {
                ll_barcode.setVisibility(View.VISIBLE);
            }
            ll_upi.setVisibility(View.GONE);
            ll_bank.setVisibility(View.GONE);
            barcodeShow = !barcodeShow;
            upiShow = false;
            bankShow = false;
        });

        btn_bank.setOnClickListener(view -> {
            if (bankShow) {
                ll_bank.setVisibility(View.GONE);
            } else {
                ll_bank.setVisibility(View.VISIBLE);
            }
            ll_upi.setVisibility(View.GONE);
            ll_barcode.setVisibility(View.GONE);
            bankShow = !bankShow;
            upiShow = false;
            barcodeShow = false;
        });

        copy_upi.setOnClickListener(view -> {
            copyText("upi", "aavis94246@barodampay");
        });
        copy_account.setOnClickListener(view -> {
            copyText("account", "45760200000469");
        });
        copy_ifsc.setOnClickListener(view -> {
            copyText("ifsc", "BARB0SANRAI");
        });

        btn_download.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                somePermissionResultLauncher.launch(new
                        String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE});
            } else {
                showLoader();
                new DownloadsImage().execute("https://charpair.com/charpair_barcode.jpeg");
            }

        });

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

    class DownloadsImage extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            URL url;
            try {
                url = new URL(strings[0]);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //Creates app specific folder
                if (!path.exists()) {
                    path.mkdirs();
                }
                File imageFile = new File(path, "aavi" + System.currentTimeMillis() + ".jpeg");
                FileOutputStream fos = new FileOutputStream(imageFile);
                InputStream is = c.getInputStream();
                byte[] buffer = new byte[4096];
                int len1;

                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                try {
                    fos.flush();
                    fos.close();
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideLoader();
            Toast.makeText(getContext(), "Downloaded", Toast.LENGTH_LONG).show();
        }
    }

    private void copyText(String type, String text) {
        try {
            ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(type, text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getActivity(), type + " copied.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
