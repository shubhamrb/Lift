package com.liftPlzz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.model.ridehistorymodel.Data;

import java.util.List;


public class RideHistoryAdapter extends RecyclerView.Adapter<RideHistoryAdapter.ViewHolder> {


    private Context context;
    List<Data> arrayList;
    private ItemListener itemListener;
    private String type;

    public RideHistoryAdapter(Context context, List<Data> arrayList, ItemListener itemListener, String type) {
        this.context = context;
        this.arrayList = arrayList;
        this.itemListener = itemListener;
        this.type = type;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ride_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Data matchingRideResponse = arrayList.get(position);
        if (type.equals("from")) {
            holder.tv_start_point.setText(matchingRideResponse.getStart_location());
            holder.img_marker.setImageResource(R.drawable.pic_location);
        } else {
            holder.tv_start_point.setText(matchingRideResponse.getEnd_location());
            holder.img_marker.setImageResource(R.drawable.drop_location);
        }

        holder.itemView.setOnClickListener(v -> {
            if (itemListener != null) {
                itemListener.onCategoryClick(matchingRideResponse);
            }
        });

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tv_start_point;
        ImageView img_marker;

        ViewHolder(View itemView) {
            super(itemView);
            tv_start_point = itemView.findViewById(R.id.tv_start_point);
            img_marker = itemView.findViewById(R.id.img_marker);
        }
    }

    public interface ItemListener {
        void onCategoryClick(Data matchingRideResponse);

    }

}
