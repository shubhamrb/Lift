package com.liftPlzz.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.model.getsetting.Datum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {


    public ItemListener itemListener;
    List<Datum> settingList;
    private final Context context;


    public SettingAdapter(Context context, List<Datum> settingList, ItemListener itemListener) {
        this.context = context;
        this.settingList = settingList;
        this.itemListener = itemListener;
    }

    @Override
    public int getItemCount() {
        return settingList.size();

    }

    @Override
    public SettingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_setting, parent, false);
        return new SettingAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final SettingAdapter.ViewHolder holder, final int position) {
        Datum datum = settingList.get(position);
        holder.tvContent.setText(datum.getType());
        if (datum.getOptionType().equalsIgnoreCase(context.getResources().getString(R.string.toggle))) {
            holder.toggleButton.setVisibility(View.VISIBLE);
            holder.imgnext.setVisibility(View.GONE);
            if (datum.getSelectedValue().equalsIgnoreCase("1")) {
                holder.toggleButton.setImageResource(R.drawable.on);
            } else if (datum.getSelectedValue().equalsIgnoreCase("0")) {
                holder.toggleButton.setImageResource(R.drawable.off);
            }
        } else {
            holder.toggleButton.setVisibility(View.GONE);
            holder.imgnext.setVisibility(View.VISIBLE);
        }

        holder.toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datum.getSelectedValue().equalsIgnoreCase("0")) {
                    itemListener.onToggleClick(datum.getId(), 1);
                } else {
                    itemListener.onToggleClick(datum.getId(), 0);
                }
            }
        });

        holder.itemRow.setOnClickListener(v -> {
            if(datum.getOptionType().equalsIgnoreCase(context.getResources().getString(R.string.select_option))){
                itemListener.onSelectOption(position,datum);
            }
        });
//        holder.ratingBarReview.setRating(settingList.get(position).getRating());
    }


    public interface ItemListener {
        void onToggleClick(int settingId, int inputValue);
        void onSelectOption(int position ,Datum data);
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgtoggle)
        ImageView toggleButton;
        @BindView(R.id.imgnext)
        ImageView imgnext;
        @BindView(R.id.tv_setting_content)
        AppCompatTextView tvContent;
        @BindView(R.id.itemRow)
        RelativeLayout itemRow;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}