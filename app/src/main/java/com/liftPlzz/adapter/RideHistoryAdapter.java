package com.liftPlzz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.model.ridehistorymodel.Data;

import java.util.List;


public class RideHistoryAdapter extends RecyclerView.Adapter<RideHistoryAdapter.ViewHolder> {


    private Context context;
    List<Data> arrayList;
    private ItemListener itemListener;

    public RideHistoryAdapter(Context context, List<Data> arrayList, ItemListener itemListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.itemListener = itemListener;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ride_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Data matchingRideResponse = arrayList.get(position);
        holder.tv_start_point.setText(matchingRideResponse.getStart_location());
        holder.tv_end_point.setText(matchingRideResponse.getEnd_location());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListener != null) {
                    itemListener.onCategoryClick(matchingRideResponse);
                }
            }
        });

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tv_start_point;
        AppCompatTextView tv_end_point;

        ViewHolder(View itemView) {
            super(itemView);
            tv_start_point = itemView.findViewById(R.id.tv_start_point);
            tv_end_point = itemView.findViewById(R.id.tv_end_point);
        }
    }

    public interface ItemListener {
        void onCategoryClick(Data matchingRideResponse);

    }

}
