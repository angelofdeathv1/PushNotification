package com.foxconnbc.pushnotification.utils;

/**
 * Created by te-arambulaa on 3/30/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.foxconnbc.pushnotification.model.Notification;

import java.util.ArrayList;

public class DBUtils extends SQLiteOpenHelper {

    public static final String C_NOTIFICATIONS = "Notifications";
    public static final String C_NOTIFICATION_ID = "_id";
    public static final String C_NOTIFICATION_CONTENT = "_content";
    public static final String C_NOTIFICATION_TIMESTAMP = "_timestamp";
    private static final String CREATE_TABLE_NOTIFICATIONS_QUERY = "create table " + C_NOTIFICATIONS + " (_id text primary key, _content text,_timestamp text)";
    private static final String DATABASE_NAME = "Notifications.db";
    private static final int DATABASE_VERSION = 1;
    public Notification test = new Notification();

    public DBUtils(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTIFICATIONS_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + C_NOTIFICATIONS);
        onCreate(db);
    }

    public boolean insertNotification  (String id, String content, String timestamp)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(C_NOTIFICATION_ID, id);
        contentValues.put(C_NOTIFICATION_CONTENT, content);
        contentValues.put(C_NOTIFICATION_TIMESTAMP, timestamp);
        db.insert(C_NOTIFICATIONS, null, contentValues);
        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+C_NOTIFICATIONS, null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, C_NOTIFICATIONS);
        return numRows;
    }

    public ArrayList<Notification> getAllNotifications()
    {
        ArrayList<Notification> arrLNotifications = new ArrayList<Notification>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+C_NOTIFICATIONS, null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Notification oNotification=new Notification();
            oNotification.setsID(res.getString(res.getColumnIndex(C_NOTIFICATION_ID)));
            oNotification.setsContent(res.getString(res.getColumnIndex(C_NOTIFICATION_CONTENT)));
            oNotification.setsTimeStamp(res.getString(res.getColumnIndex(C_NOTIFICATION_TIMESTAMP)));
            arrLNotifications.add(oNotification);
            res.moveToNext();
        }

        return arrLNotifications;
    }
}