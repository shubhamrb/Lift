package com.liftPlzz.adapter;

/**
 * Created by MyInnos on 01-02-2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.model.UserInfo.Review;
import com.liftPlzz.model.getReview.Datum;
import com.liftPlzz.utils.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {


    private Context context;
    public ItemListener itemListener;
    List<Datum> verifiedLists;
    List<Review> driverReviewList;


    public ReviewListAdapter(Context context, List<Datum> verifiedLists, ItemListener itemListener) {
        this.context = context;
        this.verifiedLists = verifiedLists;
        this.itemListener = itemListener;
    }

    public ReviewListAdapter(Context context, ItemListener itemListener, List<Review> verifiedLists) {
        this.context = context;
        this.driverReviewList = verifiedLists;
        this.itemListener = itemListener;
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (verifiedLists != null && verifiedLists.size() > 0) {
            size = verifiedLists.size();
        } else if (driverReviewList != null && driverReviewList.size() > 0) {
            size = driverReviewList.size();
        }
        return size;

//        return verifiedLists.size();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_review_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (verifiedLists != null && verifiedLists.size() > 0) {
            //user self review
            holder.ratingBarReview.setRating(verifiedLists.get(position).getRating());
            holder.textViewUserName.setText(verifiedLists.get(position).getFromUser());
            holder.textViewReview.setText(verifiedLists.get(position).getFeedback());
            holder.textViewTime.setText(verifiedLists.get(position).getReviewDate());
            if (verifiedLists.get(position).getUserImage() != null && !verifiedLists.get(position).getUserImage().isEmpty()) {
                Picasso.get().load(verifiedLists.get(position).getUserImage()).transform(new CircleTransform()).into(holder.imageViewProfileImage);
            } else {
                Picasso.get().load(R.drawable.images_no_found).transform(new CircleTransform()).into(holder.imageViewProfileImage);
            }
        } else {
            //driver review
            holder.ratingBarReview.setRating(driverReviewList.get(position).getRating());
            holder.textViewUserName.setText(driverReviewList.get(position).getFromUser());
            holder.textViewReview.setText(driverReviewList.get(position).getFeedback());
            holder.textViewTime.setText(driverReviewList.get(position).getReviewDate());
            if (driverReviewList.get(position).getUserImage() != null && !driverReviewList.get(position).getUserImage().isEmpty()) {
                Picasso.get().load(driverReviewList.get(position).getUserImage()).transform(new CircleTransform()).into(holder.imageViewProfileImage);
            } else {
                Picasso.get().load(R.drawable.images_no_found).transform(new CircleTransform()).into(holder.imageViewProfileImage);
            }
        }
       /* holder.ratingBarReview.setRating(verifiedLists.get(position).getRating());
        holder.textViewUserName.setText(verifiedLists.get(position).getFromUser());
        holder.textViewReview.setText(verifiedLists.get(position).getFeedback());
        holder.textViewTime.setText(verifiedLists.get(position).getReviewDate());
        if (verifiedLists.get(position).getUserImage() != null && !verifiedLists.get(position).getUserImage().isEmpty()) {
            Picasso.get().load(verifiedLists.get(position).getUserImage()).transform(new CircleTransform()).into(holder.imageViewProfileImage);
        } else {
            Picasso.get().load(R.drawable.images_no_found).transform(new CircleTransform()).into(holder.imageViewProfileImage);
        }*/
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageViewConactImage)
        AppCompatImageView imageViewProfileImage;
        @BindView(R.id.textViewUserName)
        AppCompatTextView textViewUserName;
        @BindView(R.id.textViewTime)
        AppCompatTextView textViewTime;
        @BindView(R.id.textViewReview)
        AppCompatTextView textViewReview;
        @BindView(R.id.ratingBarReview)
        AppCompatRatingBar ratingBarReview;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public interface ItemListener {
        void onclick(int s);
    }
}