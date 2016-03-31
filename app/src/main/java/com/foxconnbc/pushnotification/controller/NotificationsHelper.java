package com.foxconnbc.pushnotification.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.foxconnbc.pushnotification.model.Notification;
import com.foxconnbc.pushnotification.utils.DBUtils;

import java.util.ArrayList;

/**
 * Created by AngelArambula on 3/30/16.
 */
public class NotificationsHelper {
    private DBUtils dbHelper;
    private String[] USUARIOS_TABLE_COLUMNS = {DBUtils.C_NOTIFICATION_ID, DBUtils.C_NOTIFICATION_CONTENT,DBUtils.C_NOTIFICATION_TIMESTAMP};
    private SQLiteDatabase database;

    public NotificationsHelper(Context context) {
        dbHelper = new DBUtils(context);
    }

    public SQLiteDatabase getNotificationCon(){
        if (database==null || !database.isOpen()){
            open();
        }
        return database;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public Notification insertNotification  (String content, String timestamp)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBUtils.C_NOTIFICATION_CONTENT, content);
        contentValues.put(DBUtils.C_NOTIFICATION_TIMESTAMP, timestamp);

        database=getNotificationCon();

        long studId = database.insert(DBUtils.C_NOTIFICATIONS, null, contentValues);

        Cursor cursor = database.query(DBUtils.C_NOTIFICATIONS,
                USUARIOS_TABLE_COLUMNS, DBUtils.C_NOTIFICATION_ID + " = "
                        + studId, null, null, null, null);

        cursor.moveToFirst();

        Notification oNotification = parseNotification(cursor);

        cursor.close();

        return oNotification;
    }

    public void deleteNotification(Notification oNotification) {
        long id = oNotification.getnID();
        database=getNotificationCon();
        database.delete(DBUtils.C_NOTIFICATIONS, DBUtils.C_NOTIFICATION_ID
                + " = " + id, null);
    }



    public ArrayList<Notification> getAllNotifications() {
        ArrayList<Notification> arrLNotifications=new ArrayList<Notification>();
        database=getNotificationCon();
        Cursor cursor = database.query(DBUtils.C_NOTIFICATIONS,
                USUARIOS_TABLE_COLUMNS, null, null, null, null, DBUtils.C_NOTIFICATION_ID+ " DESC");

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Notification oNotification = parseNotification(cursor);
            arrLNotifications.add(oNotification);
            cursor.moveToNext();
        }

        cursor.close();
        return arrLNotifications;
    }

    private Notification parseNotification(Cursor cursor) {
        Notification oNotification = new Notification();
        oNotification.setnID((cursor.getInt(0)));
        oNotification.setsContent(cursor.getString(1));
        oNotification.setsTimeStamp(cursor.getString(2));
        return oNotification;
    }

}
