package com.liftPlzz.adapter;

/**
 * Created by MyInnos on 01-02-2017.
 */

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.liftPlzz.R;
import com.liftPlzz.model.partnerdetails.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class BlockListAdapter extends RecyclerView.Adapter<BlockListAdapter.ViewHolder> {


    private Context context;
    public ItemListener itemListener;
    List<User> blockList;


    public BlockListAdapter(Context context, List<User> dataList, ItemListener itemListener) {
        this.context = context;
        this.blockList = dataList;
        this.itemListener = itemListener;
    }

    @Override
    public int getItemCount() {
        return blockList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_block_user_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        User user = blockList.get(position);
        holder.textViewName.setText(user.getName());
        try {
            Glide.with(context).load(user.getImage())
                    .error(R.drawable.logo_icon)
                    .placeholder(R.drawable.logo_icon).into(holder.imageViewConactImage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user.isIs_block()) {
            holder.btn_block.setText("Unblock");
            holder.btn_block.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorPrimary)));
        } else {
            holder.btn_block.setText("Block");
            holder.btn_block.setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.colorRed)));
        }
        holder.btn_block.setOnClickListener(view -> {
            itemListener.onclick(user.getId());
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewName)
        AppCompatTextView textViewName;
        @BindView(R.id.btn_block)
        AppCompatTextView btn_block;
        @BindView(R.id.imageViewConactImage)
        CircleImageView imageViewConactImage;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ItemListener {
        void onclick(Integer user_id);
    }
}