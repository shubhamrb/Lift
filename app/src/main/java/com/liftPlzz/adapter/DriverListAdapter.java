package com.liftPlzz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.liftPlzz.R;
import com.liftPlzz.model.ridebyvehicletypemodel.DriverData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DriverListAdapter extends RecyclerView.Adapter<DriverListAdapter.ViewHolder> {


    private Context context;
    private ArrayList<DriverData> arrayList;
    private ItemListener itemListener;


    public DriverListAdapter(Context context, ArrayList<DriverData> arrayList, ItemListener itemListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.itemListener = itemListener;
    }

    @Override
    public int getItemCount() {
//        if (arrayList == null)
//            return 0;
        return arrayList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_driver_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        DriverData driverData = arrayList.get(position);
        holder.tvName.setText(driverData.getName());
        holder.tvMobile.setText(driverData.getMobile());
        holder.tvRatePerKm.setText("" + driverData.getRatePerKm());
        if (driverData.getRequestAlreadySend() == 0) {
            holder.btnSendRequest.setText(context.getResources().getString(R.string.send_request));
        } else {
            holder.btnSendRequest.setText(context.getResources().getString(R.string.cancel_request));
        }

        try {
            Glide.with(context).load(driverData.getImage())
                    .error(R.drawable.logo_icon)
                    .placeholder(R.drawable.logo_icon).into(holder.imgDriver);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.relItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListener != null) {
                    itemListener.onUserImageClick(driverData.getUserId());
                }
            }
        });

        holder.btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListener != null) {
                    itemListener.onSendButtonClick(driverData);
                }
            }
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_driver)
        AppCompatImageView imgDriver;
        @BindView(R.id.tv_name)
        AppCompatTextView tvName;
        @BindView(R.id.tv_mob)
        AppCompatTextView tvMobile;
        @BindView(R.id.tv_rate_per_km)
        AppCompatTextView tvRatePerKm;
        @BindView(R.id.btn_send_request)
        AppCompatButton btnSendRequest;
        @BindView(R.id.relative_item)
        RelativeLayout relItem;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ItemListener {
        void onSendButtonClick(DriverData driverData);

        void onUserImageClick(int id);
    }
}