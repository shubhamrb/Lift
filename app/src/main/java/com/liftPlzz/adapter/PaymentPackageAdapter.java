package com.liftPlzz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.model.recharge.RechargeHistory;
import com.liftPlzz.utils.MonthConverter;

import java.util.ArrayList;

public class PaymentPackageAdapter extends RecyclerView.Adapter<PaymentPackageAdapter.MyViewHolder> {

    private ArrayList<RechargeHistory> listsnew;
    Context context;

    public PaymentPackageAdapter(Context context, ArrayList<RechargeHistory> listsnew) {
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
        holder.remarktext.setText(listsnew.get(position).getRemarks());
        if (listsnew.get(position).getType().equals("credit")) {
            holder.amount.setText("+" + listsnew.get(position).getAmount());
            holder.amount.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.indicator.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        } else {
            holder.amount.setText("-" + listsnew.get(position).getAmount());
            holder.amount.setTextColor(context.getResources().getColor(R.color.colorRed));
            holder.indicator.setBackgroundColor(context.getResources().getColor(R.color.colorRed));
        }

        try {
            String[] dateArr = listsnew.get(position).getCreated_at().split("T");
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
        TextView remarktext;
        CardView indicator;

        public MyViewHolder(View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.amountext);
            daytext = itemView.findViewById(R.id.daytext);
            monthtext = itemView.findViewById(R.id.monthtext);
            remarktext = itemView.findViewById(R.id.remarktext);
            indicator = itemView.findViewById(R.id.indicator);
        }
    }
}
