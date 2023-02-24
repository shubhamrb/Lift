package com.liftPlzz.adapter;

/**
 * Created by MyInnos on 01-02-2017.
 */

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.model.RedeemRequestModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RedeemRequestAdapter extends RecyclerView.Adapter<RedeemRequestAdapter.ViewHolder> {

    private Context context;
    List<RedeemRequestModel> list;

    public RedeemRequestAdapter(Context context, List<RedeemRequestModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_bank_account, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        RedeemRequestModel requestModel = list.get(position);

        holder.tv_account_name.setText(requestModel.getPoint() + " points\n" + requestModel.getComment());

        holder.tv_account_status.setText(requestModel.getStatus());

        if (requestModel.getStatus_color() != null) {
            switch (requestModel.getStatus_color()) {
                case "green":
                    holder.tv_account_status.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.quantum_googgreen)));
                    break;
                case "orange":
                    holder.tv_account_status.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.quantum_orange)));
                    break;
                case "red":
                    holder.tv_account_status.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.colorRed)));
                    break;
            }
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_account_name)
        AppCompatTextView tv_account_name;
        @BindView(R.id.tv_account_status)
        AppCompatButton tv_account_status;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}