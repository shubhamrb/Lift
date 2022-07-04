package com.liftPlzz.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.PagerAdapter;

import com.liftPlzz.R;
import com.liftPlzz.model.SocialImage;
import com.liftPlzz.model.getVehicle.Datum;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class ViewPagerAdapter extends PagerAdapter {

    // Context object
    Context context;

    // Array of images
    List<SocialImage> images;

    // Layout Inflater
    LayoutInflater mLayoutInflater;
    public ItemListener itemListener;
    public int type = 0;

    // Viewpager Constructor
    public ViewPagerAdapter(Context context, List<SocialImage> images, ItemListener it, int type) {
        this.context = context;
        this.images = images;
        this.type = type;
        this.itemListener = it;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // return the number of images
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((RelativeLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        // inflating the item.xml
        View itemView = mLayoutInflater.inflate(R.layout.row_image_item, container, false);

        // referencing the image view from the item.xml file
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewMainDetails);
        ImageView img = (ImageView) itemView.findViewById(R.id.img);
        ImageView addImg = (ImageView) itemView.findViewById(R.id.addImg);
        if (type == 1) {
            img.setVisibility(View.GONE);
            addImg.setVisibility(View.GONE);
        } else {
            addImg.setVisibility(View.VISIBLE);

            if (images.get(position).getImageId() != -1) {
                img.setVisibility(View.VISIBLE);
            } else {
                img.setVisibility(View.GONE);
            }
        }
        if (images.size() > 0) {
            Picasso.get().load(images.get(position).getImage()).into(imageView);
        } else {
            Picasso.get().load(R.drawable.images_no_found).into(imageView);
        }
        addImg.setOnClickListener(v -> itemListener.onAddImage());

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to delete this banner?")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (images.get(position).getImageId() != null) {
                                    itemListener.onDeleteClick(images.get(position).getImageId());
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(context, "Cant delete this item", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.dismiss();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Alert");
                alert.show();
            }
        });
        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((RelativeLayout) object);
    }

    public interface ItemListener {
        void onclick(int s);

        void onDeleteClick(int s);

        void onEditClick(Datum s);

        void onAddImage();
    }
}
