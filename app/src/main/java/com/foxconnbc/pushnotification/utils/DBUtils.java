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
    private static final String DATABASE_NAME = "Notifications.db";
    private static final int DATABASE_VERSION = 2;

    private static final String CREATE_TABLE_NOTIFICATIONS_QUERY = "create table " + C_NOTIFICATIONS
            + "("+ C_NOTIFICATION_ID+" integer primary key autoincrement,"
            +C_NOTIFICATION_CONTENT+" text not null,"
            +C_NOTIFICATION_TIMESTAMP+" text);";


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

}