package com.liftPlzz.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.liftPlzz.R;

public class Constants {
    public static final String SHARED_PREF_NAME = "App";
    public static final String SHARED_PREF_INTRO = "App_intro";
    public static final String IS_LOGIN = "isLogin";
    public static final String TOKEN = "token";

    public static final String USER_ID = "user_id";
    public static final String IMAGE = "image";
    public static final String PARTNER = "patner";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String SURNAME = "surname";
    public static final String MOBILE = "mobile";
    public static final String IMAGE_PATH = "path";
    public static final String IS_ADMIN = "admin";
    public static final String IS_INTRO = "isIntroCompleted";
    public static final String IS_RIDE_ENDED = "isRideEnded";
    public static final String API_KEY = "070b92d28adc166b3a6c63c2d44535d2f62a3e24";
    public static final String ANDROID = "android";

    public static ProgressDialog dialog;
    public static final String IS_DRIVER = "IS_DRIVER";
    public static final String LIFT_ID = "LIFT_ID";
    public static final String LIFT_OBJ = "LIFT_obj";
    public static final String SEATS_NO = "seats_no";
    public static final String VEHICLE_TYPE = "vehicle_type";
    public static final String SUB_CATEGORY_ID = "sub_category_id";
    public static final String IS_FIND_LIFT = "find_lift";

    public static String NOTIFICATION_TYPE = "";
    public static boolean isLiftOnGoing = false;
    public static final String APP_UPDATE_MODEL = "APP_UPDATE_MODEL";


    public static void showLoader(Context context) {
        //hideKeyBoard();
        if (dialog == null && context != null) {
            try {
                dialog = new ProgressDialog(context);
                dialog.setMessage("Please wait...");
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (dialog != null)
            dialog.show();
    }

    public static void hideLoader() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public static void showMessage(Context context, String msg, View view) {
//        hideKeyBoard(context);
        if (view != null) {
            Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
            View view1 = snackbar.getView();
            view1.setBackgroundResource(R.color.colorAccent);
            TextView textView = (TextView) view1.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(context.getResources().getColor(R.color.colorWhite));
            snackbar.show();
        }
    }

    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }

//    @UiThread
//    public static void hideKeyBoard(Context context) {
//        View view = context.getcu();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//
//        }
//    }
}
