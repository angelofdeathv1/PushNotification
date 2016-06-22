package com.foxconnbc.pushnotification.controller;

import com.foxconnbc.pushnotification.view.DataListingActivity;
import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationPayload;

/**
 * Created by te-arambulaa on 6/21/2016.
 */
public class NotificationExtenderBareBonesExample extends NotificationExtenderService {
    NotificationsHelper oHelper = new NotificationsHelper(DataListingActivity.getContext());

    @Override
    protected boolean onNotificationProcessing(OSNotificationPayload notification) {
        /*OverrideSettings overrideSettings = new OverrideSettings();
        overrideSettings.extender = new NotificationCompat.Extender() {
            @Override
            public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                // Sets the background notification color to Green on Android 5.0+ devices.
                return builder.setColor(new BigInteger("FF00FF00", 16).intValue());
            }
        };*/
        try {
            if (notification.additionalData != null) {
                oHelper.insertNotification(notification.message, notification.additionalData.getString("TimeStamp"));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
/*
        OSNotificationDisplayedResult result = displayNotification(overrideSettings);
        Log.d("OneSignalExample", "Notification displayed with id: " + result.notificationId);*/

        return false;
    }
}