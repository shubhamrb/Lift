package com.liftPlzz.adapter;

/**
 * Created by MyInnos on 01-02-2017.
 */

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.model.getVehicle.Datum;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyVehicleListAdapter extends RecyclerView.Adapter<MyVehicleListAdapter.ViewHolder> {

    private Context context;
    public ItemListener itemListener;
    List<Datum> verifiedLists;


    public MyVehicleListAdapter(Context context, List<Datum> verifiedLists, ItemListener itemListener) {
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_vehicle_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textViewName.setText(verifiedLists.get(position).getModel());
        holder.textViewNumber.setText(verifiedLists.get(position).getRegistrationNo());
        holder.tvRatePerKm.setText(context.getResources().getString(R.string.rate_per_km)
                + " : " + verifiedLists.get(position).getRatePerKm());

        holder.textViewPercent.setText(verifiedLists.get(position).getVehicle_percentage() + "%");

        switch (verifiedLists.get(position).getPercentage_color()) {
            case "red":
                holder.textViewPercent.setTextColor(context.getResources().getColor(R.color.colorRed));
                break;
            case "green":
                holder.textViewPercent.setTextColor(context.getResources().getColor(R.color.quantum_googgreen));
                break;
            case "yellow":
                holder.textViewPercent.setTextColor(context.getResources().getColor(R.color.quantum_yellow400));
                break;
        }

        if (verifiedLists.get(position).getIsDefault() == 1) {
            holder.textViewDeafult.setVisibility(View.VISIBLE);
        } else {
            holder.textViewDeafult.setVisibility(View.GONE);
        }
        if (verifiedLists.get(position).getVehicleImageFront() != null && !verifiedLists.get(position).getVehicleImageFront().isEmpty()) {
            Picasso.get().load(verifiedLists.get(position).getVehicleImageFront()).fit().centerCrop().into(holder.imageViewConactImage);
        } else if (verifiedLists.get(position).getVehicleImageBack() != null && !verifiedLists.get(position).getVehicleImageBack().isEmpty()) {
            Picasso.get().load(verifiedLists.get(position).getVehicleImageBack()).into(holder.imageViewConactImage);
        } else if (verifiedLists.get(position).getRcImage() != null && !verifiedLists.get(position).getRcImage().isEmpty()) {
            Picasso.get().load(verifiedLists.get(position).getRcImage()).into(holder.imageViewConactImage);
        } else {
            Picasso.get().load(R.drawable.images_no_found).into(holder.imageViewConactImage);
        }

        holder.imMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(context, holder.view);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setGravity(Gravity.END);
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(item -> {
                    if (item.toString().equalsIgnoreCase("delete")) {
                        if (itemListener != null) {
                            itemListener.onDeleteClick(verifiedLists.get(position).getId());
                        }
                    } else {
                        if (itemListener != null) {
                            itemListener.onEditClick(verifiedLists.get(position));
                        }
                    }
                    return true;
                });

                popup.show();

            }
        });

        holder.itemView.setOnClickListener(view -> {
            if (itemListener != null) {
                itemListener.onEditClick(verifiedLists.get(position));
            }
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewName)
        AppCompatTextView textViewName;
        @BindView(R.id.textViewNumber)
        AppCompatTextView textViewNumber;
        @BindView(R.id.textViewDeafult)
        AppCompatTextView textViewDeafult;
        @BindView(R.id.imMore)
        AppCompatImageView imMore;
        @BindView(R.id.imageViewConactImage)
        ImageView imageViewConactImage;
        @BindView(R.id.textViewSeats)
        AppCompatTextView textViewSeats;
        @BindView(R.id.tv_rate_per_km)
        AppCompatTextView tvRatePerKm;
        @BindView(R.id.textViewPercent)
        AppCompatTextView textViewPercent;
        @BindView(R.id.layoutClick)
        LinearLayout layoutClick;
        @BindView(R.id.view)
        View view;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

//            imageViewConactImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    itemListener.onDeleteClick(getAdapterPosition());
//                }
//            });
//            layoutClick.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    itemListener.onEditClick(verifiedLists.get(getAdapterPosition()));
//                }
//            });

        }
    }

    public interface ItemListener {
        void onclick(int s);

        void onDeleteClick(int s);

        void onEditClick(Datum s);
    }
}