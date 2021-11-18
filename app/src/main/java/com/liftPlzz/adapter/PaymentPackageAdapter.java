package com.liftPlzz.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import com.liftPlzz.R;
import com.liftPlzz.activity.PaymentPackage;
import com.liftPlzz.model.PaymentPackageModel;
import com.liftPlzz.model.createProfile.User;
import com.razorpay.Checkout;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PaymentPackageAdapter extends RecyclerView.Adapter<PaymentPackageAdapter.MyViewHolder>  {

    private ArrayList<PaymentPackageModel> listsnew;
    Context context;
    static User user ;

    public PaymentPackageAdapter(Context context, ArrayList<PaymentPackageModel> listsnew) {
        this.context = context;
        this.listsnew = listsnew;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.paymentpackage_layout, parent, false);
        return new MyViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.amount.setText("₹"+listsnew.get(position).getAmount());
        holder.points.setText(listsnew.get(position).getPoints());
        holder.description.setText(listsnew.get(position).getDescription());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(view.getContext(),"click on item: "+listsnew.get(position).getDescription(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent("message_subject_intent");
                intent.putExtra("amountfromadaptor" , listsnew.get(position).getAmount());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                // rounding off the amount.
                int amount = Math.round(Float.parseFloat(listsnew.get(position).getAmount()) * 100);
                // initialize Razorpay account.
                Checkout checkout = new Checkout();
                // set your id as below
                checkout.setKeyID("rzp_test_sbtMx1SKiekIfR");
                // set image
//        checkout.setImage(R.drawable.gfgimage);
                // initialize json object
                JSONObject object = new JSONObject();
                try {
                    // to put name
                    object.put("name", "");
                    // put description
                    object.put("description", listsnew.get(position).getDescription());
                    // to set theme color
                    object.put("theme.color", "");
                    // put the currency
                    object.put("currency", "INR");
                    // put amount
                    object.put("amount", amount);
                    // put mobile number
                    object.put("prefill.contact", "");
                    // put email
                    object.put("prefill.email", "");

                    // open razorpay to checkout activity
                    checkout.open((Activity) context, object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return listsnew.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView amount;
        TextView points;
        TextView description;
        RelativeLayout relativeLayout;
        public MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            amount = (TextView) itemView.findViewById(R.id.amounttext);
            points = (TextView) itemView.findViewById(R.id.pointstext);
            description = (TextView) itemView.findViewById(R.id.descriptiontext);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeid);
        }
    }
}
