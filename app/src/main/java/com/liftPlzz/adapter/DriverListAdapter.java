package com.liftPlzz.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.liftPlzz.R;
import com.liftPlzz.model.ridebyvehicletypemodel.DriverData;
import com.liftPlzz.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class DriverListAdapter extends RecyclerView.Adapter<DriverListAdapter.ViewHolder> {


    private Context context;
    private ArrayList<DriverData> arrayList;
    private ItemListener itemListener;
    private boolean isFind = true;
    private final SharedPreferences sharedPreferences;

    public DriverListAdapter(Context context, ArrayList<DriverData> arrayList, ItemListener itemListener, boolean isFind) {
        this.context = context;
        this.arrayList = arrayList;
        this.itemListener = itemListener;
        this.isFind = isFind;
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

    }

    @Override
    public int getItemCount() {
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
        holder.tvName.setText("" + driverData.getName());
        holder.tv_time.setText(driverData.getLfit_time());

        if (driverData.getStart_location() == null)
            holder.from.setText("From: --");
        else
            holder.from.setText("From: " + driverData.getStart_location());
        if (driverData.getEnd_location() == null)
            holder.to.setText("To: --");
        else
            holder.to.setText("To: " + driverData.getEnd_location());

        if (driverData.getTotal_km() == null)
            holder.total_km.setText("Total km: --");
        else
            holder.total_km.setText("Total km: " + driverData.getTotal_km());
        holder.tv_date.setText(driverData.getLiftDate());
        holder.tv_rate.setText("" + driverData.getRating() + " " + "(" + driverData.getTotal_review() + ")");

        if (isFind) {
            holder.price_per_seat.setText("" + driverData.getPrice_per_seat());
            holder.tvRatePerKm.setText("Point per km: " + driverData.getRatePerKm() + "/km");
            holder.textViewSeats.setText(" " + driverData.getPaidSeats());
            holder.tvRatePerKm.setVisibility(View.VISIBLE);
            holder.textViewSeats.setVisibility(View.VISIBLE);
            holder.img_filled_seat.setVisibility(View.VISIBLE);
            holder.tv_na.setVisibility(View.GONE);
            holder.textVacantSeats.setText(" " + driverData.getVacant_seats());

            if (driverData.getProfile_percentage() != null) {
                holder.profile_percantage.setText("" + driverData.getProfile_percentage().getPercantage() + "%");
                switch (driverData.getProfile_percentage().getColor()) {
                    case "red":
                        holder.profile_percantage.setTextColor(context.getResources().getColor(R.color.colorRed, null));
                        break;
                    case "green":
                        holder.profile_percantage.setTextColor(context.getResources().getColor(R.color.quantum_googgreen, null));
                        break;
                    case "orange":
                        holder.profile_percantage.setTextColor(context.getResources().getColor(R.color.quantum_orange, null));
                        break;
                }
                holder.profile_percantage.setVisibility(View.VISIBLE);
            } else {
                holder.profile_percantage.setVisibility(View.GONE);
            }

            if (driverData.getVehicle_percentage() != null) {
                holder.vehicle_percantage.setText("" + driverData.getVehicle_percentage().getPercantage() + "%");
                switch (driverData.getVehicle_percentage().getColor()) {
                    case "red":
                        holder.vehicle_percantage.setTextColor(context.getResources().getColor(R.color.colorRed, null));
                        break;
                    case "green":
                        holder.vehicle_percantage.setTextColor(context.getResources().getColor(R.color.quantum_googgreen, null));
                        break;
                    case "orange":
                        holder.vehicle_percantage.setTextColor(context.getResources().getColor(R.color.quantum_orange, null));
                        break;
                }
                holder.vehicle_percantage.setVisibility(View.VISIBLE);
            } else {
                holder.vehicle_percantage.setVisibility(View.GONE);
            }

        } else {
            if (driverData.getProfile_percentage() != null) {
                holder.profile_percantage.setText("" + driverData.getProfile_percentage().getPercantage() + "%");
                holder.profile_percantage.setVisibility(View.VISIBLE);
            } else {
                holder.profile_percantage.setVisibility(View.GONE);
            }
            holder.vehicle_percantage.setVisibility(View.GONE);
            holder.price_per_seat.setVisibility(View.GONE);
            holder.tvRatePerKm.setVisibility(View.GONE);
            holder.textViewSeats.setVisibility(View.GONE);
            holder.img_filled_seat.setVisibility(View.GONE);
            holder.img_vacant_seat.setVisibility(View.GONE);

            holder.textVacantSeats.setText("Looking for : " + driverData.getVacant_seats() + " seat");
            if (driverData.getUserId().equals(sharedPreferences.getString(Constants.USER_ID, ""))) {
                holder.tv_na.setText("Point: " + driverData.getTotal_point());
                holder.tv_na.setVisibility(View.VISIBLE);
            } else {
                holder.tv_na.setVisibility(View.GONE);
            }
        }


        holder.btnSendRequest.setVisibility(View.VISIBLE);
        if (driverData.getRequestAlreadySend() == 0) {
            holder.btnSendRequest.setText(context.getResources().getString(R.string.send_request));
            holder.request_status.setVisibility(View.GONE);
        } else {
            holder.btnSendRequest.setText(context.getResources().getString(R.string.cancel_request));
            holder.request_status.setVisibility(View.VISIBLE);

            if (driverData.getRequest_status() == 0) {
                holder.request_status.setText("Request pending");
            } else if (driverData.getRequest_status() == 1) {
                holder.request_status.setText("Request accepted");
            } else if (driverData.getRequest_status() == 2) {
                holder.request_status.setText("Request rejected");
            } else if (driverData.getRequest_status() == 3) {
                holder.request_status.setText("Request canceled");
                holder.btnSendRequest.setVisibility(View.GONE);
            }
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
        CircleImageView imgDriver;
        @BindView(R.id.tv_name)
        AppCompatTextView tvName;
        @BindView(R.id.tv_time)
        AppCompatTextView tv_time;
        @BindView(R.id.tv_date)
        AppCompatTextView tv_date;
        @BindView(R.id.tv_rate_per_km)
        AppCompatTextView tvRatePerKm;
        @BindView(R.id.tv_rate)
        AppCompatTextView tv_rate;
        @BindView(R.id.tv_na)
        AppCompatTextView tv_na;
        @BindView(R.id.btn_send_request)
        AppCompatButton btnSendRequest;
        @BindView(R.id.relative_item)
        RelativeLayout relItem;

        @BindView(R.id.from)
        AppCompatTextView from;
        @BindView(R.id.to)
        AppCompatTextView to;
        @BindView(R.id.textViewSeats)
        AppCompatTextView textViewSeats;
        @BindView(R.id.total_km)
        AppCompatTextView total_km;
        @BindView(R.id.price_per_seat)
        AppCompatTextView price_per_seat;
        @BindView(R.id.request_status)
        AppCompatTextView request_status;

        @BindView(R.id.textVacantSeats)
        AppCompatTextView textVacantSeats;

        @BindView(R.id.profile_percantage)
        AppCompatTextView profile_percantage;
        @BindView(R.id.vehicle_percantage)
        AppCompatTextView vehicle_percantage;


        @BindView(R.id.img_filled_seat)
        ImageView img_filled_seat;
        @BindView(R.id.img_vacant_seat)
        ImageView img_vacant_seat;


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