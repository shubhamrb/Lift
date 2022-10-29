package com.liftPlzz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {


    public ItemListener itemListener;
    Map<String, String> titlesMap;
    private final Context context;


    public SettingAdapter(Context context, Map<String, String> titlesMap, ItemListener itemListener) {
        this.context = context;
        this.titlesMap = titlesMap;
        this.itemListener = itemListener;
    }

    @Override
    public int getItemCount() {
        return titlesMap.size();

    }

    @Override
    public SettingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_setting, parent, false);
        return new SettingAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final SettingAdapter.ViewHolder holder, final int position) {
        String title = titlesMap.get("" + (position + 1));
        holder.tvContent.setText(title);
        holder.itemView.setOnClickListener(view -> {
            itemListener.onSelectOption(title, (position + 1));
        });
    }


    public interface ItemListener {
        void onSelectOption(String title, int id);
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