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
import com.liftPlzz.model.partnerdetails.User;
import com.liftPlzz.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PartnerAdapter extends RecyclerView.Adapter<PartnerAdapter.ViewHolder> {


    private Context context;
    private ArrayList<User> arrayList;
    private boolean isPartner;


    public PartnerAdapter(Context context, ArrayList<User> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.isPartner = isPartner;
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
        holder.tvMobile.setText(requestData.getMobile());
        holder.tvName.setText(requestData.getName());
        holder.linearBtn.setVisibility(View.GONE);
        holder.lblStatus.setVisibility(View.GONE);
        holder.tvSeats.setText(context.getResources().getString(R.string.seats) + " : " + requestData.getSeats());
//        if (isPartner) {
//            holder.linearBtn.setVisibility(View.GONE);
//            holder.lblStatus.setVisibility(View.GONE);
//        } else {
//            if (requestData.getStatus() == 0) {
//                holder.linearBtn.setVisibility(View.VISIBLE);
//                holder.lblStatus.setVisibility(View.GONE);
//            } else if (requestData.getStatus() == 1) {
//                holder.linearBtn.setVisibility(View.GONE);
//                holder.lblStatus.setVisibility(View.VISIBLE);
//                holder.lblStatus.setText(context.getResources().getString(R.string.accepted));
//            } else {
//                holder.linearBtn.setVisibility(View.GONE);
//                holder.lblStatus.setVisibility(View.VISIBLE);
//                holder.lblStatus.setText(context.getResources().getString(R.string.rejected));
//            }
//        }


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
        AppCompatImageView imgDriver;
        @BindView(R.id.tv_name)
        AppCompatTextView tvName;
        @BindView(R.id.tv_mobile_no)
        AppCompatTextView tvMobile;
        @BindView(R.id.tv_seats)
        AppCompatTextView tvSeats;

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


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}