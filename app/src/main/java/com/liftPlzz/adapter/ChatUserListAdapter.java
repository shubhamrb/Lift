package com.liftPlzz.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.liftPlzz.R;
import com.liftPlzz.model.chatuser.ChatUser;
import com.liftPlzz.model.ridebyvehicletypemodel.DriverData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatUserListAdapter extends RecyclerView.Adapter<ChatUserListAdapter.ViewHolder> {


    private Context context;
    private List<ChatUser> arrayList;
    private ItemListener itemListener;


    public ChatUserListAdapter(Context context, List<ChatUser> arrayList, ItemListener itemListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.itemListener = itemListener;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_user_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ChatUser driverData = arrayList.get(position);
        holder.tvName.setText(driverData.getName());
        holder.tvMobile.setText(driverData.getMobile());


        try {
            Glide.with(context).load(driverData.getImage())
                    .error(R.drawable.logo_icon)
                    .placeholder(R.drawable.logo_icon).into(holder.imgDriver);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_driver)
        AppCompatImageView imgDriver;
        @BindView(R.id.tv_name)
        AppCompatTextView tvName;
        @BindView(R.id.tv_mob)
        AppCompatTextView tvMobile;
        @BindView(R.id.relative_item)
        RelativeLayout relItem;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("call this","call here");
                    itemListener.onSendButtonClick(arrayList.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface ItemListener {
        void onSendButtonClick(ChatUser chatUser);

        void onUserImageClick(int id);
    }
}