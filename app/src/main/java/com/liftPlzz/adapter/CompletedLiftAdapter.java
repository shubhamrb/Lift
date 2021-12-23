package com.liftPlzz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.model.completedLift.CompleteLiftData;
import com.liftPlzz.model.upcomingLift.Lift;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompletedLiftAdapter extends RecyclerView.Adapter<CompletedLiftAdapter.ViewHolder> {

    private Context context;
    public ItemListener itemListener;
    List<CompleteLiftData> verifiedLists;


    public CompletedLiftAdapter(Context context, List<CompleteLiftData> verifiedLists,ItemListener itemListener) {
        this.context = context;
        this.verifiedLists = verifiedLists;
        this.itemListener = itemListener;
    }

    @Override
    public int getItemCount() {
        if (verifiedLists == null)
            return 0;
        return verifiedLists.size();
    }

    @Override
    public CompletedLiftAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_completed_lift, parent, false);
        return new CompletedLiftAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final CompleteLiftData lift = verifiedLists.get(position);
        holder.tvLiftType.setText(lift.getLiftType());
        holder.textViewTitle.setText(lift.getTitle());
        holder.textViewRideId.setText(context.getString(R.string.lift_id) + lift.getId());
        holder.textViewSeats.setText(context.getString(R.string.seat) + lift.getPaidSeats());
        holder.textViewDateTime.setText(lift.getLiftDate());
        holder.textDistancekm.setText("Distance : "+lift.getTotalDistance()+" km");
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewTitle)
        AppCompatTextView textViewTitle;
        @BindView(R.id.tv_lift_type)
        AppCompatTextView tvLiftType;
        @BindView(R.id.textDistancekm)
        AppCompatTextView textDistancekm;
        @BindView(R.id.textViewRideId)
        AppCompatTextView textViewRideId;
        @BindView(R.id.textViewSeats)
        AppCompatTextView textViewSeats;
        @BindView(R.id.textViewDateTime)
        AppCompatTextView textViewDateTime;

        @BindView(R.id.textRepet)
        AppCompatTextView textRepet;

        @BindView(R.id.linear_item)
        LinearLayout linearItem;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            textRepet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onMatchClick(verifiedLists.get(getAdapterPosition()));
                }
            });

        }
    }

    public interface ItemListener {
        void onMatchClick(CompleteLiftData lift);
    }
}