package com.liftPlzz.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.model.recharge.RechargeFuelCardHistory;
import com.liftPlzz.utils.MonthConverter;

import java.util.ArrayList;

public class FuelCardRechargeHistoryAdapter extends RecyclerView.Adapter<FuelCardRechargeHistoryAdapter.MyViewHolder> {

    private ArrayList<RechargeFuelCardHistory> listsnew;
    Context context;

    public FuelCardRechargeHistoryAdapter(Context context, ArrayList<RechargeFuelCardHistory> listsnew) {
        this.context = context;
        this.listsnew = listsnew;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.paymentpackage_layout, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.srctext.setText(listsnew.get(position).getRemarks() != null ? listsnew.get(position).getRemarks() : "");
        holder.remarktext.setText(listsnew.get(position).getStatus());
        holder.desttext.setVisibility(View.GONE);
        if (!listsnew.get(position).getStatus().equals("Pending")) {
            holder.amount.setText("" + listsnew.get(position).getPoint());
            holder.amount.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.indicator.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));
        } else {
            holder.amount.setText("" + listsnew.get(position).getPoint());
            holder.amount.setTextColor(context.getResources().getColor(R.color.quantum_orange));
            holder.indicator.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.quantum_orange)));
        }

        try {
            String[] dateArr = listsnew.get(position).getCreated_at().split(" ");
            String[] date = dateArr[0].split("-");

            holder.daytext.setText(date[2]);
            holder.monthtext.setText(new MonthConverter().doConvert(date[1]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return listsnew.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView daytext;
        TextView monthtext;
        TextView amount;
        TextView srctext;
        TextView remarktext;
        TextView desttext;
        CardView indicator;

        public MyViewHolder(View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amountext);
            daytext = itemView.findViewById(R.id.daytext);
            monthtext = itemView.findViewById(R.id.monthtext);
            srctext = itemView.findViewById(R.id.srctext);
            remarktext = itemView.findViewById(R.id.remarktext);
            desttext = itemView.findViewById(R.id.desttext);
            indicator = itemView.findViewById(R.id.indicator);
        }
    }
}
