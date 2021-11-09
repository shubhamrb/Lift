package com.liftPlzz.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.model.ResponseChatSuggestion;

import java.util.ArrayList;

public class ChatSuggestionAdapter extends RecyclerView.Adapter<ChatSuggestionAdapter.RecyclerViewHolder> {
  
    private ArrayList<String> arrayList;
    private Context mcontext;
    public ItemListener itemListener;
  
    public ChatSuggestionAdapter(ArrayList<String> recyclerDataArrayList, Context mcontext,ItemListener itemListener) {
        this.arrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
        this.itemListener = itemListener;
    }
  
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_suggestion, parent, false);
        return new RecyclerViewHolder(view);
    }
  
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        String itemText = arrayList.get(position);
        holder.tvSuggestion.setText(itemText);
        holder.cvSuggestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = arrayList.get(position);
                if (itemListener!=null){
                    itemListener.onItemClickSuggestion(text);
                }
            }
        });
    }
  
    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return arrayList.size();
    }
  
    // View Holder Class to handle Recycler View.
     class RecyclerViewHolder extends RecyclerView.ViewHolder {
  
        private TextView tvSuggestion;
        private CardView cvSuggestion;
  
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSuggestion = itemView.findViewById(R.id.tv_suggestion);
            cvSuggestion = itemView.findViewById(R.id.cv_suggestion);
        }
    }

    public interface ItemListener {
        void onItemClickSuggestion(String copyItem);
    }
}