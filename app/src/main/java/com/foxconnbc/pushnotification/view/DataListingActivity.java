package com.foxconnbc.pushnotification.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.foxconnbc.pushnotification.R;
import com.foxconnbc.pushnotification.controller.NotificationsAdapter;
import com.foxconnbc.pushnotification.controller.NotificationsHelper;
import com.foxconnbc.pushnotification.model.Notification;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.ArrayList;

public class DataListingActivity extends AppCompatActivity {
    ListView lstView = null;
    NotificationsAdapter adapter = null;
    NotificationsHelper oHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.WARN);

        OneSignal.startInit(this)
                .setAutoPromptLocation(true)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .init();

        setContentView(R.layout.activity_data_listing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HttpPostTask(DataListingActivity.this).execute();
            }
        });

        lstView = (ListView) findViewById(R.id.lstList);
        adapter = new NotificationsAdapter(this);

        lstView.setAdapter(adapter);

        oHelper = new NotificationsHelper(this);

        lstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                deleteNotification(position);
                return true;
            }
        });

        new HttpPostTask(DataListingActivity.this).execute();

    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    @Override
    protected void onPause() {
        oHelper.close();
        super.onPause();
    }

    public void llenarDatos(ArrayList<Notification> arrLNotifications) {
        adapter.clear();
        for (Notification notification : arrLNotifications) {
            adapter.add(notification);
        }
        adapter.notifyDataSetChanged();
    }

    public void deleteNotification(int position) {

        if (adapter.getCount() > 0) {
            Notification oNotification = adapter.getItem(position);
            oHelper.deleteNotification(oNotification);
            adapter.remove(oNotification);
        }
    }

    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        @Override
        public void notificationOpened(String message, JSONObject additionalData, boolean isActive) {
            try {
                if (additionalData != null) {
                    if (additionalData.has("actionSelected")) {
                        Log.d("OneSignalExample", "OneSignal notification button with id " + additionalData.getString("actionSelected") + " pressed");
                    }

                    Log.d("OneSignalExample", "Full additionalData:\n" + additionalData.toString());

                    oHelper.insertNotification(message, additionalData.getString("TimeStamp"));
                    //llenarDatos(oHelper.getAllNotifications());
                    new HttpPostTask(DataListingActivity.this).execute();
                }

            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    private class HttpPostTask extends AsyncTask<Void, Integer, ArrayList<Notification>> {
        ProgressDialog pd;

        public HttpPostTask(Activity myActivity) {
            pd = new ProgressDialog(myActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setMessage("loading");
            pd.show();
        }

        @Override
        protected ArrayList<Notification> doInBackground(Void... params) {
            return oHelper.getAllNotifications();
        }

        @Override
        protected void onPostExecute(ArrayList<Notification> response) {
            llenarDatos(response);
            if (pd.isShowing()) {
                pd.dismiss();
            }
        }
    }
}
