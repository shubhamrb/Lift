package com.liftPlzz.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.model.PaymentHistoryModel;

import java.util.ArrayList;

public class PaymentHistoryAdatper extends RecyclerView.Adapter<PaymentHistoryAdatper.MyViewHolder>  {

    private ArrayList<PaymentHistoryModel> listsnew;

    public PaymentHistoryAdatper(ArrayList<PaymentHistoryModel> listsnew){
        this.listsnew = listsnew;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.paymanet_history_view, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        String type = listsnew.get(position).getType();
        if (type.equals("credit")){
            holder.amount.setText("+"+listsnew.get(position).getAmount());
            holder.amount.setTextColor(Color.parseColor("#00FF00"));
        }else {
            holder.amount.setText(listsnew.get(position).getAmount());
            holder.amount.setTextColor(Color.parseColor("#FF0000"));
        }
        // set the data in items
        holder.status.setText(listsnew.get(position).getSstatus());
        holder.source.setText(listsnew.get(position).getSource());
        holder.datetime.setText(listsnew.get(position).getDatetime());
    }


    @Override
    public int getItemCount() {
        return listsnew.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView status;// init the item view's
        TextView amount;// init the item view's
        TextView source;// init the item view's
        TextView datetime;// init the item view's
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            status = (TextView) itemView.findViewById(R.id.historysuccessfail);
            amount = (TextView) itemView.findViewById(R.id.historypayment);
            source = (TextView) itemView.findViewById(R.id.historysource);
            datetime = (TextView) itemView.findViewById(R.id.datetimeformat);

        }
    }
}
