package com.liftPlzz.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.liftPlzz.R;
import com.liftPlzz.activity.DriverProfileActivity;
import com.liftPlzz.model.partnerdetails.User;
import com.liftPlzz.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class PartnerAdapter extends RecyclerView.Adapter<PartnerAdapter.ViewHolder> {


    private final SharedPreferences sharedPreferences;
    private Context context;
    private ArrayList<User> arrayList;
    private boolean isLifter;


    public PartnerAdapter(Context context, ArrayList<User> arrayList,boolean isLifter) {
        this.context = context;
        this.arrayList = arrayList;
        this.isLifter = isLifter;
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
//        return 10;
    }

    @Override
    public PartnerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ride_request, parent, false);
        return new PartnerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final PartnerAdapter.ViewHolder holder, final int position) {
        User requestData = arrayList.get(position);
        if (requestData.getIs_contact_public()==0){
            holder.tvMobile.setVisibility(View.GONE);
        }else {
            holder.tvMobile.setText("Mobile: " + requestData.getMobile());
            holder.tvMobile.setVisibility(View.VISIBLE);
        }
        holder.tvName.setText(requestData.getName());

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
        holder.userType.setVisibility(View.VISIBLE);
        holder.userType.setText(requestData.getUserType());

        if (isLifter) {
            holder.total_point.setVisibility(View.VISIBLE);
            holder.total_km.setVisibility(View.VISIBLE);
        } else {
            if (requestData.getId() != null && String.valueOf(requestData.getId()).equals(sharedPreferences.getString(Constants.USER_ID, ""))) {
                holder.total_point.setVisibility(View.VISIBLE);
                holder.total_km.setVisibility(View.VISIBLE);
            } else {
                holder.total_point.setVisibility(View.GONE);
                holder.total_km.setVisibility(View.GONE);
            }
        }

        if (requestData.getUserType().equalsIgnoreCase("Lifter")) {
            if (requestData.getVehicle_percentage() == null) {
                holder.vehicle_percantage.setVisibility(View.INVISIBLE);
            } else {
                holder.vehicle_percantage.setText("Vehicle: " + requestData.getProfile_percentage());
                holder.vehicle_percantage.setVisibility(View.VISIBLE);
            }

            if (requestData.getRate_per_km() == null) {
                holder.textRateparkm.setVisibility(View.GONE);
            } else {
                holder.textRateparkm.setText("Rate per km: " + requestData.getRate_per_km() + "/km");
                holder.textRateparkm.setVisibility(View.VISIBLE);
            }
        } else {
            holder.textRateparkm.setVisibility(View.GONE);
            if (requestData.getRate_per_km() == null) {
                holder.vehicle_percantage.setVisibility(View.INVISIBLE);
            } else {
                holder.vehicle_percantage.setText("Point per km: " + requestData.getRate_per_km() + "/km");
                holder.vehicle_percantage.setVisibility(View.VISIBLE);
            }
        }

        holder.linearBtn.setVisibility(View.GONE);
        holder.lblStatus.setVisibility(View.GONE);
        holder.tvSeats.setText(context.getResources().getString(R.string.seats) + " : " + requestData.getSeats());
        try {
            Glide.with(context).load(requestData.getImage()).into(holder.imgDriver);
        } catch (Exception e) {
            e.printStackTrace();
        }


        holder.relItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DriverProfileActivity.class);
                intent.putExtra(Constants.USER_ID, requestData.getId());
                context.startActivity(intent);
            }
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_driver)
        CircleImageView imgDriver;
        @BindView(R.id.tv_name)
        AppCompatTextView tvName;
        @BindView(R.id.tv_mobile_no)
        AppCompatTextView tvMobile;
        @BindView(R.id.tv_seats)
        AppCompatTextView tvSeats;

        @BindView(R.id.userType)
        TextView userType;

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
        @BindView(R.id.vehicle_percantage)
        AppCompatTextView vehicle_percantage;
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
        @BindView(R.id.textRateparkm)
        AppCompatTextView textRateparkm;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}