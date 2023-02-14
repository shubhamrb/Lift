package com.liftPlzz.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.liftPlzz.R;
import com.liftPlzz.adapter.AccountsAdapter;
import com.liftPlzz.adapter.RedeemRequestAdapter;
import com.liftPlzz.base.BaseFragment;
import com.liftPlzz.model.AccountDataModel;
import com.liftPlzz.model.RedeemRequestModel;
import com.liftPlzz.presenter.PointRedemptionPresenter;
import com.liftPlzz.utils.Constants;
import com.liftPlzz.views.PointRedemptionView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PointRedemptionFragment extends BaseFragment<PointRedemptionPresenter, PointRedemptionView> implements PointRedemptionView {

    SharedPreferences sharedPreferences;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;

    @BindView(R.id.imageViewHome)
    ImageView imageViewHome;

    @BindView(R.id.buttonAddAccount)
    AppCompatButton buttonAddAccount;

    @BindView(R.id.text_history)
    AppCompatTextView text_history;

    @BindView(R.id.layouthistory)
    LinearLayout layouthistory;

    @BindView(R.id.text_account)
    AppCompatTextView text_account;

    @BindView(R.id.layoutaccount)
    LinearLayout layoutaccount;

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    @BindView(R.id.text_points)
    AppCompatTextView text_points;

    @BindView(R.id.buttonRedeem)
    AppCompatButton buttonRedeem;

    @BindView(R.id.txt_no_data_msg)
    AppCompatTextView txt_no_data_msg;

    String strToken = "";
    private int CURRENT_TAB = 0;
    private List<AccountDataModel> accountsList;
    private List<RedeemRequestModel> requestList;
    private int total_banks;
    private int total_points;

    @Override
    protected int createLayout() {
        return R.layout.fragment_point_redemption;
    }

    @Override
    protected void setPresenter() {
        presenter = new PointRedemptionPresenter();
    }

    @Override
    protected PointRedemptionView createView() {
        return this;
    }

    @Override
    protected void bindData() {
        sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        toolBarTitle.setText("Points Redemption");
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        imageViewHome.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        if (CURRENT_TAB == 0) {
            loadAccounts();
        } else {
            loadHistory();
        }
    }

    private void loadHistory() {
        presenter.getHistory(strToken);
    }

    private void loadAccounts() {
        presenter.getAccounts(strToken);
    }

    @OnClick({R.id.imageViewBack, R.id.imageViewHome, R.id.buttonAddAccount,
            R.id.layouthistory, R.id.layoutaccount, R.id.buttonRedeem})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                getActivity().onBackPressed();
                break;
            case R.id.imageViewHome:
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case R.id.buttonAddAccount:
                presenter.openAddAccount();
                break;
            case R.id.layouthistory:
                CURRENT_TAB = 1;

                layouthistory.setBackground(getResources().getDrawable(R.drawable.rounded_bg_blue));
                layoutaccount.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                text_history.setTextColor(getResources().getColor(R.color.colorWhite));
                text_account.setTextColor(getResources().getColor(R.color.colorBlack));
                buttonAddAccount.setVisibility(View.GONE);
                //load history
                loadHistory();
                break;
            case R.id.layoutaccount:
                CURRENT_TAB = 0;
                layoutaccount.setBackground(getResources().getDrawable(R.drawable.rounded_bg_blue));
                layouthistory.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                text_history.setTextColor(getResources().getColor(R.color.colorBlack));
                text_account.setTextColor(getResources().getColor(R.color.colorWhite));
                buttonAddAccount.setVisibility(View.VISIBLE);
                //load accounts
                loadAccounts();
                break;
            case R.id.buttonRedeem:
                if (total_points <= 0) {
                    Toast.makeText(getActivity(), "You don't have enough points to redeem.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (accountsList == null || accountsList.size() == 0) {
                    Toast.makeText(getActivity(), "Please add your bank details.", Toast.LENGTH_LONG).show();
                    return;
                }
                showRequestDialog();
                break;
        }
    }

    public void showRequestDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.redemption_request_dialog);
        AppCompatButton buttonSubmit = dialog.findViewById(R.id.buttonSubmit);
        EditText editTextPoints = dialog.findViewById(R.id.editTextPoints);
        EditText editTextDescription = dialog.findViewById(R.id.editTextDescription);
        AppCompatSpinner spinner = dialog.findViewById(R.id.spinner_accounts);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < accountsList.size(); i++) {
            list.add(accountsList.get(i).getAccount_name());
        }
        ArrayAdapter adapterCategory = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, list);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterCategory);

        buttonSubmit.setOnClickListener(v -> {
            String points = editTextPoints.getText().toString();
            String description = editTextDescription.getText().toString();
            String account_id = String.valueOf(accountsList.get(spinner.getSelectedItemPosition()).getId());

            if (points.trim().equals("")) {
                Toast.makeText(getActivity(), "Please enter redeem points", Toast.LENGTH_SHORT).show();
                return;
            }
            /*if (Integer.parseInt(editTextPoints.getText().toString()) < 500) {
                Toast.makeText(getActivity(), "Min. 500 points are required.", Toast.LENGTH_SHORT).show();
                return;
            }*/
            if (description.trim().equals("")) {
                Toast.makeText(getActivity(), "Please enter the description.", Toast.LENGTH_SHORT).show();
                return;
            }

            presenter.redeemRequest(strToken, points, description, account_id);
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void setHistory(JsonObject jsonObject) {
        JsonArray dataObject = jsonObject.get("user").getAsJsonArray();

        Type accounts = new TypeToken<List<RedeemRequestModel>>() {
        }.getType();

        requestList = new Gson().fromJson(dataObject.toString(), accounts);


        if (requestList.size() > 0) {
            recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
            recycler_view.setAdapter(new RedeemRequestAdapter(getContext(), requestList));

            txt_no_data_msg.setVisibility(View.GONE);
            recycler_view.setVisibility(View.VISIBLE);
        } else {
            recycler_view.setVisibility(View.GONE);
            txt_no_data_msg.setVisibility(View.VISIBLE);
            txt_no_data_msg.setText("No Redemption Requests found.");
        }
    }

    @Override
    public void setAccounts(JsonObject jsonObject) {
        total_points = jsonObject.get("total_point").getAsInt();
        total_banks = jsonObject.get("total_bank").getAsInt();
        text_points.setText(total_points + " Points");
        JsonArray dataObject = jsonObject.get("bank_detail").getAsJsonArray();

        Type accounts = new TypeToken<List<AccountDataModel>>() {
        }.getType();
        accountsList = new Gson().fromJson(dataObject.toString(), accounts);

        if (accountsList.size() > 0) {
            recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
            recycler_view.setAdapter(new AccountsAdapter(getContext(), accountsList));

            txt_no_data_msg.setVisibility(View.GONE);
            recycler_view.setVisibility(View.VISIBLE);

            if (total_points > 0) {
                buttonRedeem.setVisibility(View.VISIBLE);
            } else {
                buttonRedeem.setVisibility(View.GONE);
            }
        } else {
            buttonRedeem.setVisibility(View.GONE);
            recycler_view.setVisibility(View.GONE);
            txt_no_data_msg.setVisibility(View.VISIBLE);
            txt_no_data_msg.setText("You don’t have any bank account, add new account for redemption.");
        }
    }

    @Override
    public void onSubmit(String message) {
        new AlertDialog.Builder(getActivity()).setTitle("Redemption Request").setMessage(message).setPositiveButton("OK", (dialog, whichButton) -> {
            loadData();
            dialog.dismiss();
        }).show();
    }
}
