package com.liftPlzz.adapter;

/**
 * Created by MyInnos on 01-02-2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.model.upcomingLift.Lift;
import com.liftPlzz.model.videos.upcomingLift.VideosModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

    private Context context;
    public ItemListener itemListener;
    List<VideosModel> verifiedLists;

    public VideosAdapter(Context context, List<VideosModel> verifiedLists, ItemListener itemListener) {
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
                .inflate(R.layout.row_videos_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final VideosModel videos = verifiedLists.get(position);
        holder.textViewTitle.setText(videos.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videos.getUrl() != null) {
                    itemListener.onItemClick(videos.getUrl());
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.no_request_found), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewTitle)
        AppCompatTextView textViewTitle;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ItemListener {
        void onItemClick(String url);
    }
}