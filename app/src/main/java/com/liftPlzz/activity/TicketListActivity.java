package com.liftPlzz.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.adapter.TicketListAdapter;
import com.liftPlzz.api.ApiService;
import com.liftPlzz.api.RetroClient;
import com.liftPlzz.model.getTicketList.ResponseTicketList;
import com.liftPlzz.model.getTicketList.TicketListData;
import com.liftPlzz.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketListActivity extends AppCompatActivity implements TicketListAdapter.ItemListener {

    TicketListAdapter ticketListAdapter;
    SharedPreferences sharedPreferences;
    @BindView(R.id.rel_driver)
    LinearLayout relativeLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imageViewBack)
    ImageView imageViewBack;
    @BindView(R.id.toolBarTitle)
    AppCompatTextView toolBarTitle;
    @BindView(R.id.rvTicketList)
    RecyclerView rvTicketList;
    @BindView(R.id.btn_add)
    TextView floatingButtonAdd;
    @BindView(R.id.rl_no_data)
    RelativeLayout rl_no_data;
    private String strToken = "", vehicleType;
    private ArrayList<TicketListData> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_ticket_list);
        ButterKnife.bind(this);
        toolBarTitle.setText(getResources().getString(R.string.ticket_list));
        sharedPreferences = getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        strToken = sharedPreferences.getString(Constants.TOKEN, "");
        ticketListAdapter = new TicketListAdapter(this, arrayList, TicketListActivity.this);
        rvTicketList.setLayoutManager(new LinearLayoutManager(this));
        rvTicketList.setAdapter(ticketListAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTicketList();
    }

    public void getTicketList() {
        Constants.showLoader(this);
        ApiService api = RetroClient.getApiService();
        Call<ResponseTicketList> call = api.getTicketList(Constants.API_KEY, getResources().getString(R.string.android), strToken);
        call.enqueue(new Callback<ResponseTicketList>() {
            @Override
            public void onResponse(Call<ResponseTicketList> call, Response<ResponseTicketList> response) {
                Constants.hideLoader();
                if (response.body() != null) {
                    if (response.body().getStatus() && response.body().getData() != null) {
                        arrayList.clear();
                        arrayList.addAll(response.body().getData());
                        if (arrayList.size() > 0) {
                            ticketListAdapter.notifyDataSetChanged();
                            rl_no_data.setVisibility(View.GONE);
                            rvTicketList.setVisibility(View.VISIBLE);
                        } else {
                            rl_no_data.setVisibility(View.VISIBLE);
                            rvTicketList.setVisibility(View.GONE);
                        }

                    } else {
                        rl_no_data.setVisibility(View.VISIBLE);
                        rvTicketList.setVisibility(View.GONE);
                        Constants.showMessage(TicketListActivity.this, response.body().getMessage(), relativeLayout);
                    }
                } else {
                    Constants.showMessage(TicketListActivity.this, getResources().getString(R.string.something_went_wrong), relativeLayout);
                }
            }


            @Override
            public void onFailure(Call<ResponseTicketList> call, Throwable throwable) {
                Constants.hideLoader();
                Constants.showMessage(TicketListActivity.this, throwable.getMessage(), relativeLayout);
            }
        });

    }

    @Override
    public void onItemClick(int id) {
        Intent intent = new Intent(TicketListActivity.this, DriverProfileActivity.class);
        intent.putExtra(Constants.USER_ID, id);
        startActivity(intent);
    }


    @OnClick({R.id.imageViewBack, R.id.btn_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                onBackPressed();
                break;

            case R.id.btn_add:
                Intent intent = new Intent(TicketListActivity.this, CreateTicketActivity.class);
                startActivity(intent);
                break;
        }
    }
}

