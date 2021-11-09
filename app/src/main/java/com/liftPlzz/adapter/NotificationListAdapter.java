package com.liftPlzz.adapter;

/**
 * Created by MyInnos on 01-02-2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.liftPlzz.R;
import com.liftPlzz.model.getNotification.NotificationData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {


    private Context context;
    public ItemListener itemListener;
    List<NotificationData> notificationList;


    public NotificationListAdapter(Context context, List<NotificationData> dataList/*, List<MenuItem> , ItemListener itemListener*/) {
        this.context = context;
        this.notificationList = dataList;
        this.itemListener = itemListener;
    }

    @Override
    public int getItemCount() {
       /* if (notificationList == null)
            return 0;
        return notificationList.size();*/
        return notificationList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_notification_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        NotificationData notificationData = notificationList.get(position);
        holder.tvNotificationText.setText(notificationData.getTitle());
        holder.tvNotificationText.setText(notificationData.getDescription());
        holder.tvNotificationTime.setText(notificationData.getCreatedAt());

        try {
            Glide.with(context).load(notificationData.getImage())
                    .error(R.drawable.logo_icon)
                    .placeholder(R.drawable.logo_icon).into(holder.imageIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_icon)
        AppCompatImageView imageIcon;
        @BindView(R.id.tv_notification_text)
        AppCompatTextView tvNotificationText;
        @BindView(R.id.tv_notification_title)
        AppCompatTextView tvTitle;
        @BindView(R.id.tv_notification_time)
        AppCompatTextView tvNotificationTime;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public interface ItemListener {


        void onclick(int s);
    }
}