package com.liftPlzz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

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
            if (datum.getSelectedValue().equalsIgnoreCase("1")) {
                holder.toggleButton.setChecked(true);
            } else if (datum.getSelectedValue().equalsIgnoreCase("0")) {
                holder.toggleButton.setChecked(false);
            }

        } else {
            holder.toggleButton.setVisibility(View.GONE);
        }
        holder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    itemListener.onToggleClick(datum.getId(), 1);
                } else {
                    itemListener.onToggleClick(datum.getId(), 0);
                }

            }
        });
//        holder.ratingBarReview.setRating(settingList.get(position).getRating());
    }


    public interface ItemListener {
        void onToggleClick(int settingId, int inputValue);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.toggle_button)
        AppCompatToggleButton toggleButton;
        @BindView(R.id.tv_setting_content)
        AppCompatTextView tvContent;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}