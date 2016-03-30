package com.foxconnbc.pushnotification.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by te-arambulaa on 3/30/2016.
 */
public class JSONParser {
    public JSONParser() {

    }

    public JSONObject getJSONFromUrl(String url) {
        try {
            HttpRequest req;
            req = new HttpRequest(url);
            String sResponse = req.preparePost().sendAndReadString();
            return new JSONObject(sResponse);
        } catch (MalformedURLException e) {
            Log.e("URL Exception", "Error parsing data" + e.getMessage());
        } catch (IOException e) {
            Log.e("IO Exception", e.getMessage());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data" + e.getMessage());
        } catch (Exception e) {
            Log.e("Exception", "Error parsing data" + e.getMessage());
        }
        return null;
    }
}
