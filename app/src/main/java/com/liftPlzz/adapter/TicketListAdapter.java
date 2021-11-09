package com.liftPlzz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.model.getTicketList.TicketListData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TicketListAdapter extends RecyclerView.Adapter<TicketListAdapter.ViewHolder> {


    private Context context;
    private ArrayList<TicketListData> arrayList;
    private ItemListener itemListener;


    public TicketListAdapter(Context context, ArrayList<TicketListData> arrayList, ItemListener itemListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.itemListener = itemListener;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public TicketListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ticket_list, parent, false);
        return new TicketListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final TicketListAdapter.ViewHolder holder, final int position) {
        TicketListData ticketListData = arrayList.get(position);
        holder.tvSubject.setText(ticketListData.getSubject());
        holder.tvDescription.setText(ticketListData.getDescription());
        holder.tvStatus.setText(ticketListData.getCurrentStatus());
        holder.tvTicketId.setText("" + ticketListData.getId());
        holder.tvDate.setText(ticketListData.getCreatedAt());

        holder.cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListener != null) {
//                    itemListener.onItemClick(ticketListData.getId());
                }
            }
        });

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_subject)
        AppCompatTextView tvSubject;
        @BindView(R.id.tv_description)
        AppCompatTextView tvDescription;
        @BindView(R.id.tv_date)
        AppCompatTextView tvDate;
        @BindView(R.id.tv_status)
        AppCompatTextView tvStatus;
        @BindView(R.id.tv_ticket_id)
        AppCompatTextView tvTicketId;
        @BindView(R.id.card_item)
        CardView cardItem;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ItemListener {
        //        void onSendButtonClick(ticketListData ticketListData);
        void onItemClick(int id);
    }
}