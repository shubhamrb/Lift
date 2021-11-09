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

import com.liftPlzz.R;
import com.liftPlzz.model.CheckPoints;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CheckPointsListAdapter extends RecyclerView.Adapter<CheckPointsListAdapter.ViewHolder> {


    private Context context;
    public ItemListener itemListener;
    List<CheckPoints> verifiedLists;


    public CheckPointsListAdapter(Context context, List<CheckPoints> verifiedLists, ItemListener itemListener) {
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
                .inflate(R.layout.row_dropoff_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.editTextDropLocation.setText(verifiedLists.get(position).getAddress());

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.editTextDropLocation)
        AppCompatTextView editTextDropLocation;
        @BindView(R.id.imageViewDelete)
        AppCompatImageView imageViewDelete;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            editTextDropLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onclick(getAdapterPosition());
                }
            });
            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onDeleteClick(verifiedLists.get(getAdapterPosition()));
                }
            });

        }
    }

    public interface ItemListener {
        void onclick(int s);
        void onDeleteClick(CheckPoints s);
    }
}