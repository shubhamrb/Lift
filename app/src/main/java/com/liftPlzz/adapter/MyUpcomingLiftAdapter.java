package com.liftPlzz.adapter;

/**
 * Created by MyInnos on 01-02-2017.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.liftPlzz.R;
import com.liftPlzz.activity.StartRideActivity;
import com.liftPlzz.dialog.EditLiftDaiFragment;
import com.liftPlzz.fragments.dailog.MoreOptionDailog;
import com.liftPlzz.model.upcomingLift.Lift;
import com.liftPlzz.utils.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyUpcomingLiftAdapter extends RecyclerView.Adapter<MyUpcomingLiftAdapter.ViewHolder> {

    private Context context;
    public ItemListener itemListener;
    List<Lift> verifiedLists;
    EditLiftDaiFragment.UpdateRecordListiner listinerUpdate;

    public MyUpcomingLiftAdapter(Context context, List<Lift> verifiedLists, ItemListener itemListener, EditLiftDaiFragment.UpdateRecordListiner listinerUpdate) {
        this.context = context;
        this.verifiedLists = verifiedLists;
        this.itemListener = itemListener;
        this.listinerUpdate = listinerUpdate;
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
                .inflate(R.layout.row_upcoming_lift_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Lift lift = verifiedLists.get(position);
        holder.matchFoundBtn.setText("" + lift.getFindMatch() + " " + context.getResources().getString(R.string.match_found));
        holder.requestBtn.setText("" + lift.getTotalRequest() + " " + context.getResources().getString(R.string.request));
        if (lift.getLiftType().equalsIgnoreCase(context.getResources().getString(R.string.find_lift))) {
            //find lift
            holder.llpoint.setVisibility(View.GONE);
            holder.tvLiftType.setText(lift.getLiftType());
//            holder.btnRequest.setText("" + lift.getFindMatch() + " " + context.getResources().getString(R.string.match_found));
        } else {
            //offer lift
            holder.llpoint.setVisibility(View.VISIBLE);
            holder.tvLiftType.setText(lift.getLiftType());
//            holder.btnRequest.setText("" + lift.getTotalRequest() + " " + context.getResources().getString(R.string.request));
            /*if (lift.getIsBooked() == 1) {
                holder.btnRequest.setVisibility(View.GONE);
            }*/
        }
        //todo if ride_start will be 1 it will show the ride start button
        if (lift.getRideStart() == 1) {
            holder.tvStartRide.setVisibility(View.VISIBLE);
            holder.btn_share_lift.setVisibility(View.VISIBLE);
            holder.btn_same_rute.setVisibility(View.VISIBLE);
            holder.btn_same_rute.setText(lift.getSameroutevehicle() + " Same Route");
        } else {
            holder.tvStartRide.setVisibility(View.GONE);
//            holder.btn_share_lift.setVisibility(View.GONE);
//            holder.btn_same_rute.setVisibility(View.GONE);
        }
        /*holder.tvStartRide.setVisibility(View.VISIBLE);
        holder.btn_share_lift.setVisibility(View.VISIBLE);*/
        //        if   "is_booked": 0,      then this button will be hide
//        if   "is_booked": 1,    then 'Partner details' button will show,
//        if   "is_booked": 0,      then   '0 match found' button will be same
//        if 'is_booked': 1,   then '0 match found' button will be hide and will show  'driver-profile
        if (lift.getIsBooked() == 0) {
            holder.partnerBtn.setVisibility(View.GONE);
        } else {
            holder.partnerBtn.setVisibility(View.VISIBLE);
        }
        holder.textViewTitle.setText(lift.getTitle());
        holder.textViewRideId.setText(context.getString(R.string.lift_id) + lift.getId());
        holder.textViewSeats.setText(context.getString(R.string.seat) + lift.getPaidSeats());
        holder.textViewDateTime.setText(lift.getLiftDate());


        //textRateparkm,textPoints
        holder.textRateparkm.setText("Rate per km : " + lift.getRate_per_km());
        holder.textPoints.setText("Total points : " + lift.getTotal_points());
        holder.textDistancekm.setText("Distance : " + lift.getTotalDistance() + " km");

        if (lift.getLiftType().equals(context.getResources().getString(R.string.find_lift))) {
            if (lift.getIs_user_start() == 1) {
                holder.tvStartRide.setText(context.getResources().getString(R.string.end_ride));
            } else {
                holder.tvStartRide.setText(context.getResources().getString(R.string.start_ride));
            }
        } else {
            if (lift.getIs_driver_start() == 1) {
                holder.tvStartRide.setText(context.getResources().getString(R.string.end_ride));
            } else {
                holder.tvStartRide.setText(context.getResources().getString(R.string.start_ride));
            }
        }


        holder.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListener != null) {
                    itemListener.onCancelClick(lift.getId());
                }
            }
        });


        if (lift.getFindMatch() == 0) {
            holder.matchFoundBtn.setVisibility(View.GONE);
        }
        holder.matchFoundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lift.getFindMatch() != null && lift.getFindMatch() != 0) {
                    itemListener.onMatchClick(lift);
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.no_matches), Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (lift.getTotalRequest() == 0) {
            holder.requestBtn.setVisibility(View.GONE);
        }

        holder.requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lift.getTotalRequest() != null && lift.getTotalRequest() != 0) {
                    itemListener.onRequestClick(lift);
                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.no_request_found), Toast.LENGTH_SHORT).show();
                }
                /*if (holder.btnRequest.getText().toString()
                        .equalsIgnoreCase(context.getResources().getString(R.string.driver_profile))) {
                    itemListener.onDriverProfile(lift);
                } else if (lift.getLiftType().equalsIgnoreCase(context.getResources().getString(R.string.find_lift))) {
                    if (lift.getFindMatch() != null && lift.getFindMatch() != 0) {
                        itemListener.onMatchClick(lift);
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.no_matches), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (lift.getTotalRequest() != null && lift.getTotalRequest() != 0) {
                        itemListener.onRequestClick(lift);
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.no_request_found), Toast.LENGTH_SHORT).show();
                    }
                }*/

            }
        });

        holder.partnerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListener != null) {
                    itemListener.onPartnetDetails(lift);
                }
            }
        });


        holder.tvStartRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StartRideActivity.class);
                intent.putExtra(Constants.LIFT_OBJ, lift);
                context.startActivity(intent);
            }
        });


    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textViewTitle)
        AppCompatTextView textViewTitle;
        @BindView(R.id.tv_lift_type)
        AppCompatTextView tvLiftType;

        @BindView(R.id.partnerBtn)
        AppCompatButton partnerBtn;

        @BindView(R.id.requestBtn)
        AppCompatButton requestBtn;

        @BindView(R.id.matchFoundBtn)
        AppCompatButton matchFoundBtn;

        @BindView(R.id.imMore)
        ImageView imMore;

        @BindView(R.id.textViewRideType)
        AppCompatTextView textViewRideType;
        @BindView(R.id.textViewRideId)
        AppCompatTextView textViewRideId;
        @BindView(R.id.textViewSeats)
        AppCompatTextView textViewSeats;
        @BindView(R.id.textViewDateTime)
        AppCompatTextView textViewDateTime;
        @BindView(R.id.textDistancekm)
        AppCompatTextView textDistancekm;
        @BindView(R.id.tv_cancel)
        AppCompatTextView tvCancel;

        @BindView(R.id.btn_start_ride)
        AppCompatTextView tvStartRide;
        @BindView(R.id.btn_share_lift)
        AppCompatTextView btn_share_lift;
        @BindView(R.id.btn_same_rute)
        AppCompatTextView btn_same_rute;
        @BindView(R.id.textPoints)
        AppCompatTextView textPoints;
        @BindView(R.id.textRateparkm)
        AppCompatTextView textRateparkm;
        @BindView(R.id.llpoint)
        LinearLayout llpoint;
        //  textRateparkm,textPoints

        @BindView(R.id.linear_item)
        LinearLayout linearItem;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btn_share_lift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Lift lift = verifiedLists.get(getAdapterPosition());
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String message = "";
                    if (lift.getLiftType().equalsIgnoreCase("Offer Lift")) {
                        message = "I am inviting you for this ride\n\n";
                    } else {
                        message = "I am looking for this ride\n\n";
                    }
                    String shareBody = message + lift.getTitle() + "\nDate: " + lift.getLiftDate() + " at " + lift.getStart_time() + "\nPoint " + lift.getRate_per_km() + "/km";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share Lift");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    context.startActivity(Intent.createChooser(sharingIntent, "LiftPlzz"));
                }
            });
            imMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* MoreOptionDailog sheet = new MoreOptionDailog();
                    sheet.listiner(new MoreOptionDailog.ItemSelectListiner() {
                        @Override
                        public void cancel() {
                            if (itemListener != null) {
                                itemListener.onCancelClick(verifiedLists.get(getAdapterPosition()).getId());
                            }
                        }

                        @Override
                        public void edit() {
                            EditLiftDaiFragment sheet = new EditLiftDaiFragment("edit");
                            sheet.setLift(verifiedLists.get(getAdapterPosition()), listinerUpdate, "edit");
                            sheet.setStyle(DialogFragment.STYLE_NORMAL, R.style. zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzMyTheme);
                            sheet.show(((FragmentActivity) context).getSupportFragmentManager().beginTransaction(), "dialog");
                        }
                    });
                    sheet.show(((FragmentActivity) context).getSupportFragmentManager().beginTransaction(), "HomeSlidingMenuFragment");
                    */

                    PopupMenu popup = new PopupMenu(context, imMore);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if (item.toString().equalsIgnoreCase("delete")){
                                itemListener.onCancelClick(verifiedLists.get(getAdapterPosition()).getId());

                            }else {
                                EditLiftDaiFragment sheet = new EditLiftDaiFragment("edit");
                                sheet.setLift(verifiedLists.get(getAdapterPosition()), listinerUpdate, "edit");
                                sheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.MyTheme);
                                sheet.show(((FragmentActivity) context).getSupportFragmentManager().beginTransaction(), "dialog");
                            }
                            return true;
                        }
                    });

                    popup.show();
                }
            });
        }
    }
    /*fun showDialogImage(postData: PostData?, imagePostion: Int?) {
        val ft: FragmentTransaction = childFragmentManager!!.beginTransaction()

        val newFragment: DialogFragment = PostImageDetailFragment(postData,imagePostion)
        newFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.MyThemeDailogZoomin)
        newFragment.show(ft, "dialog")
    }*/

    public interface ItemListener {
        void onMatchClick(Lift lift);

        void onRequestClick(Lift lift);

        void onPartnetDetails(Lift lift);

        void onDriverProfile(Lift lift);

        void onCancelClick(int Id);
    }
}