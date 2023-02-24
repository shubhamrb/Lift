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
import com.liftPlzz.model.AccountDataModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder> {

    private Context context;
    List<AccountDataModel> list;

    public AccountsAdapter(Context context, List<AccountDataModel> list) {
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
        AccountDataModel account = list.get(position);

        if (account.getAccount_no() != null) {
            if (account.getAccount_no().length() > 4) {
                holder.tv_account_name.setText(account.getAccount_no().substring(account.getAccount_no().length() - 4) + " - " + account.getBank_name());
            } else {
                holder.tv_account_name.setText(account.getAccount_no() + " - " + account.getBank_name());
            }
        }

        holder.tv_account_status.setText(account.getStatus());

        if (account.getStatus_color() != null) {
            switch (account.getStatus_color()) {
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