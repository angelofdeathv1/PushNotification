package com.foxconnbc.pushnotification.controller;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.foxconnbc.pushnotification.R;
import com.foxconnbc.pushnotification.model.Coach;

/**
 * Created by te-arambulaa on 3/30/2016.
 */
public class CoachesAdapter extends ArrayAdapter<Coach> {
    public CoachesAdapter(Context context) {
        super(context, R.layout.row_list, R.id.txtRowID);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View oView = super.getView(position, convertView, parent);

        TextView txtID = (TextView) oView.findViewById(R.id.txtRowID);
        TextView txtName = (TextView) oView.findViewById(R.id.txtRowName);
        TextView txtTeam = (TextView) oView.findViewById(R.id.txtRowTeam);

        Coach oCoachJr = getItem(position);

        txtID.setText(String.valueOf(oCoachJr.getnID()));
        txtName.setText(oCoachJr.getsName());
        txtTeam.setText(oCoachJr.getsTeam());

        return oView;
    }
}