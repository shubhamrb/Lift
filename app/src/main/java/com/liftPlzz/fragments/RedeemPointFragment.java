package com.liftPlzz.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Environment;
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
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.liftPlzz.R;
import com.liftPlzz.adapter.FuelCardRechargeHistoryAdapter;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.pointsRedemption.CardModel;
import com.liftPlzz.model.pointsRedemption.PointsModel;
import com.liftPlzz.model.recharge.RechargeFuelCardHistory;
import com.liftPlzz.model.recharge.RechargeFuelCardHistoryResponse;
import com.liftPlzz.presenter.RedeemPointPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.RedeemPointView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
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
public class RedeemPointFragment extends BaseFragment<RedeemPointPresenter, RedeemPointView> implements RedeemPointView {

    SharedPreferences sharedPreferences;
    @BindView(R.id.buttonRecharge)
    AppCompatButton btnRecharge;
    @BindView(R.id.buttonRedemption)
    AppCompatButton buttonRedemption;
    @BindView(R.id.buttonRequest)
    AppCompatButton btnRequest;
    @BindView(R.id.pointtext)
    TextView pointtext;
    @BindView(R.id.nametext)
    TextView nametext;
    @BindView(R.id.card_number)
    TextView card_number;
    @BindView(R.id.recyclerViewpackage)
    RecyclerView recyclerViewpackage;
    private String strToken;
    private CardModel cardModel;
    private ArrayList<RechargeFuelCardHistory> rechargeFuelCardHistories;
    private int IMAGE_TYPE = 0;
    private File fileTrans = null;
    private MultipartBody.Part transBody = null;
    private ImageView verified_img;

    private boolean upiShow, barcodeShow, bankShow;
    private ActivityResultLauncher<String[]> somePermissionResultLauncher;

    @Override
    protected int createLayout() {
        return R.layout.fragment_point_redeem;
    }

    @Override
    protected void setPresenter() {
        presenter = new RedeemPointPresenter();
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
    protected RedeemPointView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        strToken = sharedPreferences.getString(Constants.TOKEN, "");

        btnRecharge.setOnClickListener(view -> {
            showRechargeDialog();
        });

        btnRequest.setOnClickListener(view -> {
            showRequestDialog();
        });
        buttonRedemption.setOnClickListener(view -> {
            presenter.openPointRedemption();
        });

        rechargeFuelCardHistories = new ArrayList<>();
        getPointsDetail();
    }

    private void getRechargeHistory() {
        ApiService api = RetroClient.getApiService();
        Call<RechargeFuelCardHistoryResponse> call = api.rechargeFuelCardHistory(Constants.API_KEY, "android", strToken);
        call.enqueue(new Callback<RechargeFuelCardHistoryResponse>() {
            @Override
            public void onResponse(Call<RechargeFuelCardHistoryResponse> call, Response<RechargeFuelCardHistoryResponse> response) {
                Constants.hideLoader();

                if (response.body() != null) {
                    if (response.code() == 200) {
                        rechargeFuelCardHistories.clear();
                        rechargeFuelCardHistories.addAll(response.body().getRechargeFuelCardHistories());
                        FuelCardRechargeHistoryAdapter adapter = new FuelCardRechargeHistoryAdapter(getActivity(), rechargeFuelCardHistories);
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
            public void onFailure(Call<RechargeFuelCardHistoryResponse> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
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
                        cardModel = response.body().getCardModel();
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

    /**
     * Show Recharge dialog
     */
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
                new DownloadsImage().execute("https://charpair.com/charpair_barcode.pdf");
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
                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS); //Creates app specific folder
                if (!path.exists()) {
                    path.mkdirs();
                }
                File imageFile = new File(path, "aavi" + System.currentTimeMillis() + ".pdf");
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
    public void rechargeRequestSubmit(String message) {
        new AlertDialog.Builder(getActivity()).setTitle("Recharge Request.").setMessage(message).setPositiveButton("OK", (dialog, whichButton) -> {

            try {
                rechargeFuelCardHistories.clear();
                getPointsDetail();
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        }).show();
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

    /**
     * Show request dialog
     */
    public void showRequestDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.request_dialog);
        AppCompatButton buttonSubmit = dialog.findViewById(R.id.buttonSubmit);
        EditText editTextName = dialog.findViewById(R.id.editTextName);
        EditText editTextAddress = dialog.findViewById(R.id.editTextAddress);
        EditText editTextPoints = dialog.findViewById(R.id.editTextPoints);

        if (cardModel != null) {
            try {
                editTextName.setText(cardModel.getFull_name());
                editTextPoints.setText("" + cardModel.getCurrent_point());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        buttonSubmit.setOnClickListener(v -> {
            if (editTextName.getText().toString().trim().equals("")) {
                Toast.makeText(getActivity(), "Please enter your full name.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (editTextAddress.getText().toString().trim().equals("")) {
                Toast.makeText(getActivity(), "Please enter your full address.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (editTextPoints.getText().toString().trim().equals("")) {
                Toast.makeText(getActivity(), "Please enter redeem points", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Integer.parseInt(editTextPoints.getText().toString()) < 500) {
                Toast.makeText(getActivity(), "Min. 500 points are required.", Toast.LENGTH_SHORT).show();
                return;
            }
            requestNewCard(strToken, editTextName.getText().toString().trim(), editTextAddress.getText().toString().trim(), editTextPoints.getText().toString().trim());
            dialog.dismiss();
        });
        dialog.show();
    }

    private void rechargeFuelCard(String token, String points) {
        Constants.showLoader(getActivity());
        ApiService api = RetroClient.getApiService();
        Call<PointsModel> call = api.rechargeFuelCard(Constants.API_KEY, Constants.ANDROID, token, points);
        call.enqueue(new Callback<PointsModel>() {
            @Override
            public void onResponse(Call<PointsModel> call, Response<PointsModel> response) {
                Constants.hideLoader();
                if (response.body() != null) {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    getPointsDetail();
                }
            }

            @Override
            public void onFailure(Call<PointsModel> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestNewCard(String token, String name, String address, String points) {
        Constants.showLoader(getActivity());
        ApiService api = RetroClient.getApiService();
        Call<PointsModel> call = api.requestNewCard(Constants.API_KEY, Constants.ANDROID, token, name, address, points);
        call.enqueue(new Callback<PointsModel>() {
            @Override
            public void onResponse(Call<PointsModel> call, Response<PointsModel> response) {
                Constants.hideLoader();
                if (response.body() != null) {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    getPointsDetail();
                }
            }

            @Override
            public void onFailure(Call<PointsModel> call, Throwable throwable) {
                Constants.hideLoader();
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
