package com.liftPlzz.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.liftPlzz.R;
import com.liftPlzz.model.SocialImage;
import com.liftPlzz.model.getVehicle.Datum;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class VehiclePagerAdapter extends PagerAdapter {

    Context context;
    List<Datum> vehicleList;
    LayoutInflater mLayoutInflater;

    public VehiclePagerAdapter(Context context, List<Datum> vehicle) {
        this.context = context;
        this.vehicleList = vehicle;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return vehicleList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.vehicle_view_pager, container, false);
        ImageView imageView = itemView.findViewById(R.id.vehicleImage);
        TextView vehicleName = itemView.findViewById(R.id.vehicleName);
        TextView vehicleNumber = itemView.findViewById(R.id.vehicleNumber);

        if (vehicleList.get(position).getVehicleImageFront() != null && !vehicleList.get(position).getVehicleImageFront().isEmpty()) {
            Picasso.get().load(vehicleList.get(position).getVehicleImageFront()).into(imageView);
        } else if (vehicleList.get(position).getVehicleImageBack() != null && !vehicleList.get(position).getVehicleImageBack().isEmpty()) {
            Picasso.get().load(vehicleList.get(position).getVehicleImageBack()).into(imageView);
        } else if (vehicleList.get(position).getRcImage() != null && !vehicleList.get(position).getRcImage().isEmpty()) {
            Picasso.get().load(vehicleList.get(position).getRcImage()).into(imageView);
        } else {
            Picasso.get().load(R.drawable.images_no_found).into(imageView);
        }

        vehicleName.setText("Model : " + vehicleList.get(position).getModel());
        vehicleNumber.setText("Reg Number : " + vehicleList.get(position).getRegistrationNo());

        Objects.requireNonNull(container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
