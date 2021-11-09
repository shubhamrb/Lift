package com.liftPlzz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.liftPlzz.R;
import com.liftPlzz.model.matchingridemodel.MatchingRideResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MatchingRideAdapter extends RecyclerView.Adapter<MatchingRideAdapter.ViewHolder> {


    private Context context;
    ArrayList<MatchingRideResponse> arrayList;
    private ItemListener itemListener;


    public MatchingRideAdapter(Context context, ArrayList<MatchingRideResponse> arrayList, ItemListener itemListener) {
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
                .inflate(R.layout.item_matching_ride_by_category, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        MatchingRideResponse matchingRideResponse = arrayList.get(position);
        holder.tvName.setText(matchingRideResponse.getCategory());
        holder.tvType.setText(matchingRideResponse.getType());
        holder.tvTotalRide.setText("" + matchingRideResponse.getTotalRides());
        try {
            Glide.with(context).load(matchingRideResponse.getImage()).into(holder.imgType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.relItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListener != null) {
                    itemListener.onCategoryClick(matchingRideResponse);
                }
            }
        });

    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_vehicle_type)
        AppCompatImageView imgType;
        @BindView(R.id.tv_category_name)
        AppCompatTextView tvName;
        @BindView(R.id.tv_type)
        AppCompatTextView tvType;
        @BindView(R.id.tv_total_ride)
        AppCompatTextView tvTotalRide;
        @BindView(R.id.rel_matching)
        RelativeLayout relItem;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public interface ItemListener {
        void onCategoryClick(MatchingRideResponse matchingRideResponse);

    }

}
