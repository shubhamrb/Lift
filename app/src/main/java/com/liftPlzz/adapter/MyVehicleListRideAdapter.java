package com.liftPlzz.adapter;

/**
 * Created by MyInnos on 01-02-2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.model.getVehicle.Datum;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyVehicleListRideAdapter extends RecyclerView.Adapter<MyVehicleListRideAdapter.ViewHolder> {

    private Context context;
    public ItemListener itemListener;
    public List<Datum> verifiedLists;
    public int selectionposition=0;
    private EditText etkm;
    int vehicle_id;


    public MyVehicleListRideAdapter(Context context, List<Datum> verifiedLists, ItemListener itemListener, EditText etkm,int v_id) {
        this.context = context;
        this.verifiedLists = verifiedLists;
        this.itemListener = itemListener;
        this.etkm=etkm;
        this.vehicle_id=v_id;
        if(this.vehicle_id>0) {
            selectionposition = get_pos_by_id(this.vehicle_id, this.verifiedLists);
        }

    }
    private int get_pos_by_id(int id,List<Datum> verifiedLists){
        for(int i=0;i<verifiedLists.size();i++){
            if(verifiedLists.get(i).getId()==vehicle_id){
                return i;
            }
        }
        return 0;
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
                .inflate(R.layout.row_vehicle_item_ride, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textViewName.setText(verifiedLists.get(position).getModel());
        holder.textViewNumber.setText(verifiedLists.get(position).getRegistrationNo());
        if (verifiedLists.get(position).getVehicleImageFront() != null && !verifiedLists.get(position).getVehicleImageFront().isEmpty()) {
            Picasso.get().load(verifiedLists.get(position).getVehicleImageFront()).into(holder.imageViewConactImage);
        } else if (verifiedLists.get(position).getVehicleImageBack() != null && !verifiedLists.get(position).getVehicleImageBack().isEmpty()) {
            Picasso.get().load(verifiedLists.get(position).getVehicleImageBack()).into(holder.imageViewConactImage);
        } else if (verifiedLists.get(position).getRcImage() != null && !verifiedLists.get(position).getRcImage().isEmpty()) {
            Picasso.get().load(verifiedLists.get(position).getRcImage()).into(holder.imageViewConactImage);
        } else {
            Picasso.get().load(R.drawable.images_no_found).into(holder.imageViewConactImage);
        }
        holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
        etkm.setText(""+verifiedLists.get(selectionposition).getRatePerKm());

        if(position==selectionposition)
           holder.itemView.setBackgroundColor(context.getResources().getColor(R.color.colorGrey));
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewName)
        AppCompatTextView textViewName;
        @BindView(R.id.textViewNumber)
        AppCompatTextView textViewNumber;
        @BindView(R.id.textViewDeafult)
        AppCompatTextView textViewDeafult;
        @BindView(R.id.imageViewConactImage)
        AppCompatImageView imageViewConactImage;

        View itemView;

        ViewHolder(View itemView) {
            super(itemView);
            this.itemView=itemView;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectionposition=getAdapterPosition();
                    etkm.setText(""+verifiedLists.get(getAdapterPosition()).getRatePerKm());
                    notifyDataSetChanged();
//                    itemListener.onclickVehicle(verifiedLists.get(getAdapterPosition()));
                }
            });

        }
    }

    public interface ItemListener {
        void onclickVehicle(Datum s);
    }
}