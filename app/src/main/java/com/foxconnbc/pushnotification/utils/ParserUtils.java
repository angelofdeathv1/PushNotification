package com.foxconnbc.pushnotification.utils;

import com.foxconnbc.pushnotification.model.Coach;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by te-arambulaa on 3/30/2016.
 */
public class ParserUtils {
    private static final String C_URL = "https://dl.dropboxusercontent.com/u/12412048/nfl_coaches.json";
    private static final String C_TAG_ID = "id";
    private static final String C_TAG_COACHES = "coaches";
    private static final String C_TAG_NAME = "name";
    private static final String C_TAG_TEAM = "team";

    private JSONArray arrJSONCoaches = null;
    private JSONParser oJSONParser;
    private JSONObject oJSON;

    private ArrayList<Coach> arrLCoaches;

    private int id;
    private String name;
    private String sTeam;

    public ArrayList<Coach> getCoaches() throws JSONException, NullPointerException {
        oJSONParser = new JSONParser();
        arrLCoaches = new ArrayList<Coach>();
        oJSON = oJSONParser.getJSONFromUrl(C_URL);
        arrJSONCoaches = oJSON.getJSONArray(C_TAG_COACHES);

        for (int i = 0; i < arrJSONCoaches.length(); i++) {
            JSONObject jsonObject = arrJSONCoaches.getJSONObject(i);
            id = Integer.parseInt(jsonObject.getString(C_TAG_ID));
            name = jsonObject.getString(C_TAG_NAME);
            sTeam = jsonObject.getString(C_TAG_TEAM);
            arrLCoaches.add(new Coach(id, name, sTeam));
        }

        return arrLCoaches;
    }
}
