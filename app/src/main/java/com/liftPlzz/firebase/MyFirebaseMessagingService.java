package com.liftPlzz.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.liftPlzz.R;
import com.liftPlzz.activity.RideRequestActivity;
import com.liftPlzz.activity.SplashActivity;
import com.liftPlzz.utils.Constants;

import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private String CHANNEL_ID;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            sendNotification(remoteMessage);
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }

    private void sendNotification(RemoteMessage messageBody) {

        int NOTIFICATION_ID = 234;
        try {
            Map<String, String> params = messageBody.getData();
            JSONObject object = new JSONObject(params);
            String type = object.getString("type");
            JSONObject jObject = new JSONObject(object.getString("details"));

            try {
                Intent myIntent = new Intent("FBR-IMAGE");
                myIntent.putExtra("title", object.getString("body"));
                myIntent.putExtra("details", jObject.toString());
                this.sendBroadcast(myIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.e("log : ", "if");
                CHANNEL_ID = "notification";
                CharSequence name = "notification";
                String Description = "This is my channel";
                int importance = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    importance = NotificationManager.IMPORTANCE_HIGH;
                }
                Uri defaultSoundUri;
                if (object.has("sound") && !object.getString("sound").equals("default")) {
                    defaultSoundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName() + "/" + R.raw.notification);
                } else {
                    defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                }
                AudioAttributes attributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();
                NotificationChannel mChannel;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                    mChannel.setDescription(Description);
                    mChannel.enableLights(true);
                    mChannel.setLightColor(Color.WHITE);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    mChannel.setShowBadge(false);
                    mChannel.setSound(defaultSoundUri, attributes);
                    notificationManager.createNotificationChannel(mChannel);
                }

                Intent intent = null;
                if (type.equalsIgnoreCase("cancel-invitation") || type.equalsIgnoreCase("send-invitation")) {
                    String liftId = "", subCategoryId = "";
                    if (jObject.has("LIFT_ID")) {
                        liftId = jObject.getString("LIFT_ID");
                    }
                    if (jObject.has("LIFT_ID")) {
                        subCategoryId = jObject.getString("SUB_CATEGORY_ID");
                    }
//                String vehicleType = jObject.getString("VEHICLE_TYPE");
                    intent = new Intent(this, RideRequestActivity.class);
                    if (!liftId.equals(""))
                        intent.putExtra(Constants.LIFT_ID, Integer.parseInt(liftId));
                    if (!subCategoryId.equals(""))
                        intent.putExtra(Constants.SUB_CATEGORY_ID, Integer.parseInt(subCategoryId));
                    intent.putExtra(Constants.PARTNER, false);

                } else if (type.equalsIgnoreCase("invitation-status-update")) {
                    String liftId = jObject.getString("LIFT_ID");
                    intent = new Intent(this, RideRequestActivity.class);
                    if (!liftId.equals(""))
                        intent.putExtra(Constants.LIFT_ID, Integer.parseInt(liftId));
                    intent.putExtra(Constants.PARTNER, false);

                } else if (type.equalsIgnoreCase("ride-end")) {
                    intent = new Intent(this, SplashActivity.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.logo_icon)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(object.getString("body"))
                        .setAutoCancel(true)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
                builder.setContentIntent(pendingIntent);
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            } else {

                Intent intent = null;
                if (type.equalsIgnoreCase("cancel-invitation") || type.equalsIgnoreCase("send-invitation")) {
                    String liftId = "", subCategoryId = "";
                    if (jObject.has("LIFT_ID")) {
                        liftId = jObject.getString("LIFT_ID");
                    }
                    if (jObject.has("LIFT_ID")) {
                        subCategoryId = jObject.getString("SUB_CATEGORY_ID");
                    }
//                String vehicleType = jObject.getString("VEHICLE_TYPE");
                    intent = new Intent(this, RideRequestActivity.class);
                    if (!liftId.equals(""))
                        intent.putExtra(Constants.LIFT_ID, Integer.parseInt(liftId));
                    if (!subCategoryId.equals(""))
                        intent.putExtra(Constants.SUB_CATEGORY_ID, Integer.parseInt(subCategoryId));
                    intent.putExtra(Constants.PARTNER, false);

                } else if (type.equalsIgnoreCase("invitation-status-update")) {
                    String liftId = jObject.getString("LIFT_ID");
                    intent = new Intent(this, RideRequestActivity.class);
                    if (!liftId.equals(""))
                        intent.putExtra(Constants.LIFT_ID, Integer.parseInt(liftId));
                    intent.putExtra(Constants.PARTNER, false);

                } else if (type.equalsIgnoreCase("ride-end")) {
                    intent = new Intent(this, SplashActivity.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri;
                if (object.has("sound") && !object.getString("sound").equals("default")) {
                    defaultSoundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName() + "/" + R.raw.notification);
                } else {
                    defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                }

                Notification n = new Notification.Builder(this)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(object.getString("body"))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.logo_icon)
                        .setSound(defaultSoundUri)
                        .build();
                NotificationManager notificationManager1 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager1.notify(NOTIFICATION_ID, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}