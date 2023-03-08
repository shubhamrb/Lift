package com.liftPlzz;

public interface SmsListener {
    public void messageReceived(String messageText);

    public void onError(String messageText);
}
