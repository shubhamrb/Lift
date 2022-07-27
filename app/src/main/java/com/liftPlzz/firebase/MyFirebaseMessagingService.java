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
import com.liftPlzz.utils.Constants;

import org.json.JSONObject;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private String CHANNEL_ID;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            /* if (*//* Check if data needs to be processed by long running job *//* true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }*/
            sendNotification(remoteMessage);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    // [START on_new_token]

    /**
     * Called if FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve
     * the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private void scheduleJob() {
        // [START dispatch_job]
       /* OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        WorkManager.getInstance().beginWith(work).enqueue();*/
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM registration token with any
     * server-side account maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {

    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */

    /*private void sendNotification(RemoteMessage messageBody) {
        try {
            Map<String, String> params = messageBody.getData();
            JSONObject object = new JSONObject(params);
            String type = object.getString("type");
            JSONObject jObject = new JSONObject(object.getString("details"));


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

            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 */
    /* Request code *//*, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            String channelId = getString(R.string.default_notification_channel_id);
            Uri defaultSoundUri;
            if (object.has("sound") && object.getString("sound").equals("default")){
                defaultSoundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + this.getPackageName() + "/" + R.raw.notification);
            }else {
                defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.logo_icon)
                            .setContentTitle(getString(R.string.app_name))
                            .setContentText(object.getString("body"))
                            .setAutoCancel(true)
                            .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                AudioAttributes attributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();
                channel.setDescription(object.getString("body"));
                channel.enableLights(true);
                channel.enableVibration(true);
                channel.setSound(defaultSoundUri, attributes);
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(0 *//* ID of notification *//*, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    private void sendNotification(RemoteMessage messageBody) {
        int NOTIFICATION_ID = 234;
        try {
            Map<String, String> params = messageBody.getData();
            JSONObject object = new JSONObject(params);
            String type = object.getString("type");
            JSONObject jObject = new JSONObject(object.getString("details"));

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.e("log : ", "if");
                CHANNEL_ID = "my_channel_01";
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

                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                Notification n = new Notification.Builder(this)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(object.getString("body"))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.logo_icon)
                        .build();
                NotificationManager notificationManager1 = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager1.notify(NOTIFICATION_ID, n);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}