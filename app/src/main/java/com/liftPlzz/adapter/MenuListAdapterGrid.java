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


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftPlzz.R;
import com.liftPlzz.model.MenuItem;


public class MenuListAdapterGrid extends RecyclerView.Adapter<MenuListAdapterGrid.ViewHolder> {


    private Context context;
    public ItemListener itemListener;
    List<MenuItem> verifiedLists;


    public MenuListAdapterGrid(Context context, List<MenuItem> verifiedLists, ItemListener itemListener) {
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
                .inflate(R.layout.row_user_item_grid, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textViewName.setText(verifiedLists.get(position).getTitle());
        holder.imageViewConactImage.setImageResource(verifiedLists.get(position).getIcon());
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textViewName)
        AppCompatTextView textViewName;
        @BindView(R.id.imageViewConactImage)
        AppCompatImageView imageViewConactImage;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onclick(verifiedLists.get(getAdapterPosition()).getId());
                }
            });

        }
    }

    public interface ItemListener {


        void onclick(int s);
    }
}