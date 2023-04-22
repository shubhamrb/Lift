package com.liftPlzz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.model.on_going.LiftUsers;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LiftPartnerAdapter extends RecyclerView.Adapter<LiftPartnerAdapter.ViewHolder> {


    private Context context;
    private List<LiftUsers> list;
    private OnButtonClick onButtonClick;


    public LiftPartnerAdapter(Context context, List<LiftUsers> list, OnButtonClick onButtonClick) {
        this.context = context;
        this.list = list;
        this.onButtonClick = onButtonClick;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public LiftPartnerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_lift_view, parent, false);
        return new LiftPartnerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final LiftPartnerAdapter.ViewHolder holder, final int position) {
        holder.leftId.setText("Lift ID : " + list.get(position).getUser_lift_id());
        holder.userName.setText("Name : " + list.get(position).getName());
        holder.userNumber.setText("Mobile Number : " + list.get(position).getMobile());

        if (list.get(position).getLiftee_joined() == 0) {
            holder.endBtn.setVisibility(View.GONE);
            holder.joinBtn.setVisibility(View.VISIBLE);
        } else {
            holder.joinBtn.setVisibility(View.GONE);
            holder.endBtn.setVisibility(View.VISIBLE);
        }

        holder.joinBtn.setOnClickListener(v -> {
            onButtonClick.onJoinClick(list.get(position));
        });

        holder.endBtn.setOnClickListener(v -> {
            onButtonClick.onEndClick(list.get(position));
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.leftId)
        TextView leftId;
        @BindView(R.id.userName)
        TextView userName;
        @BindView(R.id.userNumber)
        TextView userNumber;
        @BindView(R.id.endBtn)
        Button endBtn;
        @BindView(R.id.joinBtn)
        Button joinBtn;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnButtonClick {
        void onJoinClick(LiftUsers user);

        void onEndClick(LiftUsers user);
    }
}