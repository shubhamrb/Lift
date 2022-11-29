package com.liftPlzz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.model.FeedbackModelResponse;

import java.util.ArrayList;
import java.util.List;


public class FeedbackSuggestionsAdapter extends RecyclerView.Adapter<FeedbackSuggestionsAdapter.ViewHolder> {


    private Context context;
    List<FeedbackModelResponse> arrayList;

    public FeedbackSuggestionsAdapter(Context context) {
        this.context = context;
        this.arrayList = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ride_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        FeedbackModelResponse matchingRideResponse = arrayList.get(position);
        holder.tv_message.setText(matchingRideResponse.getMessage());
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView tv_message;

        ViewHolder(View itemView) {
            super(itemView);
            tv_message = itemView.findViewById(R.id.tv_message);
        }
    }
}
