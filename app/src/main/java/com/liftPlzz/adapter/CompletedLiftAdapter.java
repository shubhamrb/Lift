package com.liftPlzz.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.dialog.EditLiftDaiFragment;
import com.liftPlzz.model.completedLift.CompleteLiftData;
import com.liftPlzz.model.upcomingLift.Lift;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompletedLiftAdapter extends RecyclerView.Adapter<CompletedLiftAdapter.ViewHolder> {

    private Context context;
    public ItemListener itemListener;
    List<CompleteLiftData> verifiedLists;

    EditLiftDaiFragment.UpdateRecordListiner listinerUpdate;

    public CompletedLiftAdapter(Context context, List<CompleteLiftData> verifiedLists, EditLiftDaiFragment.UpdateRecordListiner listinerUpdate, ItemListener itemListener) {
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
        holder.textDistancekm.setText("Distance : " + lift.getTotalDistance() + " km");
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

        @BindView(R.id.imMore)
        ImageView imMore;
        @BindView(R.id.view)
        View view;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            textRepet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Lift lif = new Lift();
                    lif.setId(verifiedLists.get(getAdapterPosition()).getId());
                    lif.setTitle(verifiedLists.get(getAdapterPosition()).getTitle());
                    lif.setUserId(verifiedLists.get(getAdapterPosition()).getUserId());
                    lif.setVehicleId(verifiedLists.get(getAdapterPosition()).getVehicleId());
                    lif.setLiftType(verifiedLists.get(getAdapterPosition()).getLiftType());
                    lif.setLiftType(verifiedLists.get(getAdapterPosition()).getLiftType());
                    lif.setFreeSeats(verifiedLists.get(getAdapterPosition()).getFreeSeats());
                    lif.setPaidSeats(verifiedLists.get(getAdapterPosition()).getPaidSeats());
                    lif.setLiftDate(verifiedLists.get(getAdapterPosition()).getLiftDate());
                    lif.setStatus(verifiedLists.get(getAdapterPosition()).getStatus());
                    lif.setIsBooked(verifiedLists.get(getAdapterPosition()).getIsBooked());
                    lif.setTotalRequest(verifiedLists.get(getAdapterPosition()).getTotalRequest());
                    lif.setCreatedAt(verifiedLists.get(getAdapterPosition()).getCreatedAt());
                    lif.setUpdatedAt(verifiedLists.get(getAdapterPosition()).getUpdatedAt());
                    lif.setStartLatlong(verifiedLists.get(getAdapterPosition()).getStartLatlong());
                    lif.setStartLocation(verifiedLists.get(getAdapterPosition()).getStartLocation());
                    lif.setEndLatlong(verifiedLists.get(getAdapterPosition()).getEndLatlong());
                    lif.setEndLocation(verifiedLists.get(getAdapterPosition()).getEndLocation());
                    lif.setTotalDistance(verifiedLists.get(getAdapterPosition()).getTotalDistance());
//                    lif.seten(verifiedLists.get(getAdapterPosition()).getTotalDistance());
//        lif.setStart_time(model.get());
//        lif.setSameroutevehicle(model.get());
//        lif.setFindMatch(model.ge());

                    itemListener.onAddClick(lif, listinerUpdate, "add");
                }
            });

            imMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context, view);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                    popup.setGravity(Gravity.END);
                    Menu menu = popup.getMenu();
                    menu.removeItem(R.id.edit);
                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.toString().equalsIgnoreCase("delete")) {
                                itemListener.onDeleteClick(verifiedLists.get(getAdapterPosition()).getId());
                            }
                            return true;
                        }
                    });

                    popup.show();
                }
            });
        }
    }

    public interface ItemListener {
        void onMatchClick(CompleteLiftData lift);
        void onAddClick(Lift completeLiftData, EditLiftDaiFragment.UpdateRecordListiner listinerUpdate, String edit);

        void onDeleteClick(int Id);
    }
}