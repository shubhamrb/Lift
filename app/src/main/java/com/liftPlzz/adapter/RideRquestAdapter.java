package com.liftPlzz.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.liftPlzz.R;
import com.liftPlzz.activity.DriverProfileActivity;
import com.liftPlzz.model.riderequestmodel.RideRequestData;
import com.liftPlzz.utils.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class RideRquestAdapter extends RecyclerView.Adapter<RideRquestAdapter.ViewHolder> {


    private final SharedPreferences sharedPreferences;
    private Context context;
    private ArrayList<RideRequestData> arrayList;
    public ItemListener itemListener;
    public boolean isLifter;


    public RideRquestAdapter(Context context, ArrayList<RideRequestData> arrayList, ItemListener itemListener, boolean isLifter) {
        this.context = context;
        this.arrayList = arrayList;
        this.itemListener = itemListener;
        this.isLifter = isLifter;
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
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

        if (requestData.getIs_contact_public() == 0) {
            holder.tvMobile.setVisibility(View.GONE);
        } else {
            holder.tvMobile.setText(requestData.getMobile());
            holder.tvMobile.setVisibility(View.VISIBLE);
        }
        holder.tvName.setText(requestData.getName());
        holder.tvSeats.setText(context.getResources().getString(R.string.seats) + " :" + requestData.getSeats());

        if (requestData.getProfile_percentage() == null)
            holder.profile_percantage.setText("Profile: --");
        else {
            holder.profile_percantage.setText("Profile: " + requestData.getProfile_percentage().getPercantage() + "%");
            switch (requestData.getProfile_percentage().getColor()) {
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
        }
        if (requestData.getVehicle_percentage() == null)
            holder.vehicle_percantage.setText("Vehicle: --");
        else {
            holder.vehicle_percantage.setText("Vehicle: " + requestData.getVehicle_percentage().getPercantage() + "%");
            switch (requestData.getVehicle_percentage().getColor()) {
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
        }

        if (requestData.getLocation().getStart_city() == null)
            holder.from.setText("From: --");
        else
            holder.from.setText("From: " + requestData.getLocation().getStart_city());
        if (requestData.getLocation().getEnd_city() == null)
            holder.to.setText("To: --");
        else
            holder.to.setText("To: " + requestData.getLocation().getEnd_city());


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

        if (requestData.getTotal_point() == null)
            holder.total_point.setText("Total Points: --");
        else
            holder.total_point.setText("Total Points: " + requestData.getTotal_point());
        if (isLifter) {
            holder.textRateparkm.setVisibility(View.GONE);
            holder.total_point.setVisibility(View.VISIBLE);
        } else {
            holder.textRateparkm.setText("Rate per km : " + requestData.getRate_per_km() + "/km");
            holder.textRateparkm.setVisibility(View.VISIBLE);

            if (requestData.getUserId().equals(sharedPreferences.getString(Constants.USER_ID, ""))) {
                holder.total_point.setVisibility(View.VISIBLE);
            } else {
                holder.total_point.setVisibility(View.GONE);
            }
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

        holder.iv_menu.setVisibility(View.GONE);
        /*holder.iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.view);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setGravity(Gravity.END);
                Menu menu = popup.getMenu();
                menu.getItem(1).setTitle("Cancel Lift Request");
                menu.removeItem(R.id.edit);
                popup.setOnMenuItemClickListener(item -> {
                    if (item.toString().equalsIgnoreCase("Cancel Lift Request")) {
//                        itemListener.onDeleteClick(requestData.getRequest_id(), lift_id);
                    }
                    return true;
                });

                popup.show();
            }
        });*/
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
        @BindView(R.id.vehicle_percantage)
        AppCompatTextView vehicle_percantage;

        @BindView(R.id.from)
        AppCompatTextView from;
        @BindView(R.id.to)
        AppCompatTextView to;
        @BindView(R.id.total_point)
        AppCompatTextView total_point;
        @BindView(R.id.total_km)
        AppCompatTextView total_km;

        @BindView(R.id.iv_menu)
        AppCompatImageView iv_menu;
        @BindView(R.id.view)
        View view;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public interface ItemListener {
        void onAcceptClick(int position, RideRequestData rideRequestData);
        void onDeleteClick(Integer id, int lift_id);
        void onRejectClick(int position, RideRequestData rideRequestData);
    }
}