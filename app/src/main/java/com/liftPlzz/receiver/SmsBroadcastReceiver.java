package com.liftPlzz.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import com.liftPlzz.views.OtpReceivedInterface;

/**
 * Created on : May 21, 2019
 * Author     : AndroidWave
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsBroadcastReceiver";
    OtpReceivedInterface otpReceiveInterface = null;

    public void setOnOtpListeners(OtpReceivedInterface otpReceiveInterface) {
        this.otpReceiveInterface = otpReceiveInterface;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
//      Bundle extras = intent.getExtras();
//      Status mStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
//      switch (mStatus.getStatusCode()) {
//        case CommonStatusCodes.SUCCESS:
//          // Get SMS message contents'
//          String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
//          Log.d(TAG, "onReceive: failure "+message);
//          if (otpReceiveInterface != null) {
//            String otp = message.replace("<#> Your otp code is : ", "");
//            otpReceiveInterface.onOtpReceived(otp);
//          }
//          break;
//        case CommonStatusCodes.TIMEOUT:
//          // Waiting for SMS timed out (5 minutes)
//          Log.d(TAG, "onReceive: failure");
//          if (otpReceiveInterface != null) {
//            otpReceiveInterface.onOtpTimeout();
//          }
//          break;
//      }
//    }

            // this function is trigged when each time a new SMS is received on device.

            Bundle data = intent.getExtras();

            Object[] pdus = new Object[0];
            if (data != null) {
                pdus = (Object[]) data.get("pdus"); // the pdus key will contain the newly received SMS
            }

            if (pdus != null) {
                for (Object pdu : pdus) { // loop through and pick up the SMS of interest
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);

                    // your custom logic to filter and extract the OTP from relevant SMS - with regex or any other way.

                    if (otpReceiveInterface != null)
//                        mListener.onOTPReceived("Extracted OTP");
                        otpReceiveInterface.onOtpReceived("Recie" + smsMessage);
                    break;
                }
            }
        }

    }
}
