package com.foxconnbc.pushnotification.utils;

import com.foxconnbc.pushnotification.model.Notification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by te-arambulaa on 3/30/2016.
 */
public class ParserUtils {
    private static final String C_URL = "https://dl.dropboxusercontent.com/u/12412048/nfl_coaches.json";
    private static final String C_TAG_ID = "en";
    private static final String C_TAG_COACHES = "notifications";
    private static final String C_TAG_NAME = "id";
    private static final String C_TAG_TEAM = "contents";

    private JSONArray arrJSONCoaches = null;
    private JSONParser oJSONParser;
    private JSONObject oJSON;

    private ArrayList<Notification> arrLNotifications;

    private String id;
    private String name;
    private String sTeam;

    public ArrayList<Notification> getCoaches() throws JSONException, NullPointerException {
        oJSONParser = new JSONParser();
        arrLNotifications = new ArrayList<Notification>();

        oJSON = oJSONParser.getJSONFromUrl(C_URL);
        if (oJSON==null) {
            return arrLNotifications;
        }
        arrJSONCoaches = oJSON.getJSONArray(C_TAG_COACHES);

        for (int i = 0; i < arrJSONCoaches.length(); i++) {
            JSONObject jsonObject = arrJSONCoaches.getJSONObject(i);
            id = jsonObject.getString(C_TAG_ID);
            name = jsonObject.getString(C_TAG_NAME);
            sTeam = jsonObject.getString(C_TAG_TEAM);
            arrLNotifications.add(new Notification(id, name, sTeam));
        }

        return arrLNotifications;
    }
}
