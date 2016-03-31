package com.foxconnbc.pushnotification.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

/**
 * Created by te-arambulaa on 3/30/2016.
 */
public class JSONParser {
    public JSONParser() {

    }

    public JSONObject getJSONFromUrl(String url) {
        try {
            HttpRequest req=new HttpRequest(url);
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("app_id", "bd91e04b-2369-4338-902b-14ce63f29728");

            try {
               String  sResponse = req.preparePost().withData(parameters).sendAndReadString();

                return new JSONObject(sResponse);

            } catch (IOException e) {
                Log.e("IO Exception", e.getMessage());
            }
        } catch (MalformedURLException e) {
            Log.e("URL Exception", "Error parsing data" + e.getMessage());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data" + e.getMessage());
        } catch (Exception e) {
            Log.e("Exception", "Error parsing data" + e.getMessage());
        }
        return null;
    }
}
