package com.foxconnbc.pushnotification.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.foxconnbc.pushnotification.R;
import com.foxconnbc.pushnotification.model.Notification;

/**
 * Created by te-arambulaa on 3/30/2016.
 */
public class NotificationsAdapter extends ArrayAdapter<Notification> {
    public NotificationsAdapter(Context context) {
        super(context, R.layout.row_list, R.id.txtRowMessage);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View oView = super.getView(position, convertView, parent);

        TextView txtName = (TextView) oView.findViewById(R.id.txtRowMessage);
        TextView txtTeam = (TextView) oView.findViewById(R.id.txtRowDate);

        Notification oNotificationJr = getItem(position);

        txtName.setText(oNotificationJr.getsContent());
        txtTeam.setText(oNotificationJr.getsTimeStamp());

        return oView;
    }
}