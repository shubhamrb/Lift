package com.liftPlzz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.model.getsetting.Datum;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingOptionAdapter extends RecyclerView.Adapter<SettingOptionAdapter.ViewHolder> {


    public ItemListener itemListener;
    List<Datum> settingList;
    private final Context context;


    public SettingOptionAdapter(Context context, List<Datum> settingList, ItemListener itemListener) {
        this.context = context;
        this.settingList = settingList;
        this.itemListener = itemListener;
    }

    @Override
    public int getItemCount() {
        return settingList.size();

    }

    @Override
    public SettingOptionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_setting, parent, false);
        return new SettingOptionAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final SettingOptionAdapter.ViewHolder holder, final int position) {
        Datum datum = settingList.get(position);
        holder.tvContent.setText(datum.getType());
        if (datum.getOptionType().equalsIgnoreCase(context.getResources().getString(R.string.toggle))) {
            holder.toggleButton.setVisibility(View.VISIBLE);
            holder.imgnext.setVisibility(View.GONE);
            if (datum.getSelectedValue().equalsIgnoreCase("1")) {
                holder.toggleButton.setChecked(true);
            } else if (datum.getSelectedValue().equalsIgnoreCase("0")) {
                holder.toggleButton.setChecked(false);
            }
        } else {
            holder.toggleButton.setVisibility(View.GONE);
            holder.imgnext.setVisibility(View.VISIBLE);
        }

        holder.toggleButton.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked){
                itemListener.onToggleClick(datum.getId(), 1);
            }else {
                itemListener.onToggleClick(datum.getId(), 0);
            }
        });

        /*holder.toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datum.getSelectedValue().equalsIgnoreCase("") || datum.getSelectedValue().equalsIgnoreCase("0")) {
                    itemListener.onToggleClick(datum.getId(), 1);
                } else {
                    itemListener.onToggleClick(datum.getId(), 0);
                }
            }
        });*/

        holder.itemRow.setOnClickListener(v -> {
            if (datum.getOptionType().equalsIgnoreCase(context.getResources().getString(R.string.select_option))) {
                itemListener.onSelectOption(position, datum);
            } else if (datum.getOptionType().equalsIgnoreCase("action")) {
                itemListener.onSelectAction(position, datum);
            } else if (datum.getOptionType().equalsIgnoreCase("other_screen")) {
                itemListener.onOtherScreen(position, datum);
            } else if (datum.getOptionType().equalsIgnoreCase("input")) {
                itemListener.onSelectOption(position, datum);
            }
        });
    }


    public interface ItemListener {
        void onToggleClick(int settingId, int inputValue);

        void onSelectOption(int position, Datum data);

        void onSelectAction(int position, Datum data);

        void onOtherScreen(int position, Datum data);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imgtoggle)
        SwitchCompat toggleButton;
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