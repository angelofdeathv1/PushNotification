package com.foxconnbc.pushnotification.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.foxconnbc.pushnotification.R;
import com.foxconnbc.pushnotification.controller.NotificationsAdapter;
import com.foxconnbc.pushnotification.controller.NotificationsHelper;
import com.foxconnbc.pushnotification.model.Notification;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.ArrayList;

public class DataListingActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static Context oContext;
    ListView lstView = null;
    NotificationsAdapter adapter = null;
    NotificationsHelper oHelper = null;
    SwipeRefreshLayout swipeRefreshLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.oContext = this;
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.WARN);
        OneSignal.enableNotificationsWhenActive(false);

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
                Toast.makeText(getContext(), "Loading..", Toast.LENGTH_SHORT);
                new HttpPostTask(DataListingActivity.this).execute();
            }
        });

        lstView = (ListView) findViewById(R.id.lstList);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        oHelper = new NotificationsHelper(this);
        adapter = new NotificationsAdapter(this);

        lstView.setAdapter(adapter);
        lstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                deleteNotification(position);
                return true;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        new HttpPostTask(DataListingActivity.this).execute();
                                    }
                                }
        );
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
        int id = item.getItemId();
        if (id == R.id.action_settings_delete) {

                oHelper.deleteAllNotifications();
                new HttpPostTask(DataListingActivity.this).execute();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        new HttpPostTask(DataListingActivity.this).execute();
    }

    @Override
    protected void onResume() {
        new HttpPostTask(DataListingActivity.this).execute();
        super.onResume();
    }

    @Override
    protected void onPause() {
        oHelper.close();
        super.onPause();
    }

    public void populateNotifications(ArrayList<Notification> arrLNotifications) {
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
                    new HttpPostTask(DataListingActivity.this).execute();
                }

            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public static Context getContext() {
        return oContext;
    }

    private class HttpPostTask extends AsyncTask<Void, Integer, ArrayList<Notification>> {
        ProgressDialog pd;

        public HttpPostTask(Activity myActivity) {
            pd = new ProgressDialog(myActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeRefreshLayout.setRefreshing(true);
            pd.setMessage("loading");
            pd.show();
        }

        @Override
        protected ArrayList<Notification> doInBackground(Void... params) {
            return oHelper.getAllNotifications();
        }

        @Override
        protected void onPostExecute(ArrayList<Notification> response) {
            populateNotifications(response);
            if (pd.isShowing()) {
                pd.dismiss();
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
