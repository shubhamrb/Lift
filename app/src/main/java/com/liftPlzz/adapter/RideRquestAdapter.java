package com.liftPlzz.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.liftPlzz.R;
import com.liftPlzz.activity.DriverProfileActivity;
import com.liftPlzz.model.riderequestmodel.RideRequestData;
import com.liftPlzz.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RideRquestAdapter extends RecyclerView.Adapter<RideRquestAdapter.ViewHolder> {


    private Context context;
    private ArrayList<RideRequestData> arrayList;
    public ItemListener itemListener;


    public RideRquestAdapter(Context context, ArrayList<RideRequestData> arrayList, ItemListener itemListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.itemListener = itemListener;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
//        return 10;
    }

    @Override
    public RideRquestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ride_request, parent, false);
        return new RideRquestAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RideRquestAdapter.ViewHolder holder, final int position) {
        RideRequestData requestData = arrayList.get(position);
        holder.tvMobile.setText(requestData.getMobile());
        holder.tvName.setText(requestData.getName());
        holder.tvSeats.setText(context.getResources().getString(R.string.seats) + " :" + requestData.getSeats());
        holder.textRateparkm.setText("Rate per km : " + requestData.getRate_per_km()+"/km");

        if (requestData.getProfile_percentage() == null)
            holder.profile_percantage.setText("Profile: --");
        else
            holder.profile_percantage.setText("Profile: " + requestData.getProfile_percentage());

        if (requestData.getLocation().getStart_city() == null)
            holder.from.setText("From: --");
        else
            holder.from.setText("From: " + requestData.getLocation().getStart_city());
        if (requestData.getLocation().getEnd_city() == null)
            holder.to.setText("To: --");
        else
            holder.to.setText("To: " + requestData.getLocation().getEnd_city());
        if (requestData.getTotal_point() == null)
            holder.total_point.setText("Total Points: --");
        else
            holder.total_point.setText("Total Points: " + requestData.getTotal_point());

        if (requestData.getTotal_km() == null)
            holder.total_km.setText("Total km: --");
        else
            holder.total_km.setText("Total km: " + requestData.getTotal_km());

        if (requestData.getStatus() == 0) {
            holder.linearBtn.setVisibility(View.VISIBLE);
            holder.lblStatus.setVisibility(View.GONE);
        } else if (requestData.getStatus() == 1) {
            holder.linearBtn.setVisibility(View.GONE);
            holder.lblStatus.setVisibility(View.VISIBLE);
            holder.lblStatus.setText(context.getResources().getString(R.string.accepted));
        } else {
            holder.linearBtn.setVisibility(View.GONE);
            holder.lblStatus.setVisibility(View.VISIBLE);
            holder.lblStatus.setText(context.getResources().getString(R.string.rejected));
        }

        try {
            Glide.with(context).load(requestData.getImage()).into(holder.imgDriver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListener != null) {
                    itemListener.onAcceptClick(position, requestData);
                }

            }
        });

        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListener != null) {
                    itemListener.onRejectClick(position, requestData);
                }
            }
        });

        holder.relItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DriverProfileActivity.class);
                intent.putExtra(Constants.USER_ID, requestData.getUserId());
                context.startActivity(intent);
            }
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_driver)
        AppCompatImageView imgDriver;
        @BindView(R.id.tv_name)
        AppCompatTextView tvName;
        @BindView(R.id.tv_mobile_no)
        AppCompatTextView tvMobile;
        @BindView(R.id.tv_seats)
        AppCompatTextView tvSeats;

        @BindView(R.id.textRateparkm)
        AppCompatTextView textRateparkm;

        @BindView(R.id.btn_accept)
        AppCompatButton btnAccept;
        @BindView(R.id.btn_reject)
        AppCompatButton btnReject;
        @BindView(R.id.lbl_status)
        AppCompatTextView lblStatus;
        @BindView(R.id.linear_btn)
        LinearLayout linearBtn;
        @BindView(R.id.rel_item_request)
        RelativeLayout relItem;

        @BindView(R.id.profile_percantage)
        AppCompatTextView profile_percantage;
        @BindView(R.id.from)
        AppCompatTextView from;
        @BindView(R.id.to)
        AppCompatTextView to;
        @BindView(R.id.total_point)
        AppCompatTextView total_point;
        @BindView(R.id.total_km)
        AppCompatTextView total_km;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public interface ItemListener {
        void onAcceptClick(int position, RideRequestData rideRequestData);

        void onRejectClick(int position, RideRequestData rideRequestData);
    }
}