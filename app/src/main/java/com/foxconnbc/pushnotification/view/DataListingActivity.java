package com.foxconnbc.pushnotification.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.foxconnbc.pushnotification.R;
import com.foxconnbc.pushnotification.controller.NotificationsAdapter;
import com.foxconnbc.pushnotification.model.Notification;
import com.foxconnbc.pushnotification.utils.DBUtils;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.ArrayList;

public class DataListingActivity extends AppCompatActivity {
    ListView lstView = null;
    NotificationsAdapter adapter = null;
    DBUtils oDBUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.WARN);

        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .setAutoPromptLocation(true).init();

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
        oDBUtils = new DBUtils(this);
        lstView = (ListView) findViewById(R.id.lstList);
        adapter = new NotificationsAdapter(this);

        lstView.setAdapter(adapter);

        new HttpPostTask(DataListingActivity.this).execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void llenarDatos(ArrayList<Notification> coachesList) {
        adapter.clear();
        for (Notification notification : coachesList) {
            adapter.add(notification);
        }
        adapter.notifyDataSetChanged();
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

                    oDBUtils.insertNotification(additionalData.getString("ID"), message, additionalData.getString("TimeStamp"));
                }

            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    private class HttpPostTask extends AsyncTask<Void, Integer, ArrayList<Notification>> {
        ProgressDialog pd;

        public HttpPostTask(Activity myActivity){
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
            return oDBUtils.getAllNotifications();
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
