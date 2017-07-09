package com.example.omarelaimy.iseeyou;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Omar on 7/9/2017.
 */

public class NotificationSlotPillService extends Service {
    private static final String TAG = "NotificationService";
    private static final String URL_FOR_NOTIFICATIONS = "https://icu.000webhostapp.com/slotpillsnotifications.php";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle extras = intent.getExtras();
        String caregiverid = extras.getString("caregiverid");
        //Call the function that checks for notification in php.
        GetNotifications(caregiverid);
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }

    public void GetNotifications(final String caregiverid)
    {
        // Tag used to cancel the request
        String cancel_req_tag = "Get Notifications Caregiver";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_NOTIFICATIONS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Get Notifications Caregiver Response : " + response);
                //try
                //{
                // JSONObject jObj = new JSONObject(response);
                //boolean error = jObj.getBoolean("error");
                //if (!error)
                //{
                //   Toast.makeText(getApplicationContext(), "You are now logged out!", Toast.LENGTH_LONG).show();
                //}
                // else
                //{
                //  Toast.makeText(getApplicationContext(), "Unknown error occured while logging out!..Please try again.", Toast.LENGTH_LONG).show();
                //}
                //}
                //catch (JSONException e)
                //{
                //  e.printStackTrace();
                //}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Get Caregiver Notifications" + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to get slot url
                Map<String, String> params = new HashMap<String, String>();
                //Parameters for the slot of a given product.
                params.put("caregiverid",caregiverid);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);


    }
}
