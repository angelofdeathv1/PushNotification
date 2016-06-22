package com.foxconnbc.pushnotification.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.foxconnbc.pushnotification.R;
import com.foxconnbc.pushnotification.controller.NotificationsAdapter;
import com.foxconnbc.pushnotification.controller.NotificationsHelper;
import com.foxconnbc.pushnotification.model.Notification;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import java.util.ArrayList;

public class DataListingActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static Context oContext;
    Toolbar toolbar = null;
    ListView lstView = null;
    FloatingActionButton fab = null;
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

        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_data_listing);

        oHelper = new NotificationsHelper(this);
        adapter = new NotificationsAdapter(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lstView = (ListView) findViewById(R.id.lstList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        setSupportActionBar(toolbar);
        lstView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteNotifications();
            }
        });

        lstView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                deleteNotification(position);
                return true;
            }
        });

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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings_delete:
                deleteNotifications();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        oHelper.closeDatabase();
        super.onPause();
    }

    protected void deleteNotifications(){
        AlertDialog alertDialog = new AlertDialog.Builder(DataListingActivity.this).create();
        alertDialog.setTitle("Warning");
        alertDialog.setMessage("Delete all local messages?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        oHelper.deleteAllNotifications();
                        new HttpPostTask(DataListingActivity.this).execute();
                    }
                });
        alertDialog.show();
    }
    protected void populateNotifications(ArrayList<Notification> arrLNotifications) {
        adapter.clear();
        for (Notification notification : arrLNotifications) {
            adapter.add(notification);
        }
        adapter.notifyDataSetChanged();
    }

    protected void deleteNotification(int position) {
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
