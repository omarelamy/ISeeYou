package com.example.omarelaimy.iseeyou;
import android.app.Notification;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp on 09-Jul-17.
 */

public class Notifications extends AppCompatActivity {

    private LinearLayout mainView;
    private static final String TAG = "NotificationsActivity";
    private TextView noNOtifications;

    private static final String URL_FOR_GETNOTIFICATIONS ="https://icu.000webhostapp.com/getnotifications.php";
    private ProgressDialog progress;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_page);
        mainView = (LinearLayout) findViewById(R.id.mainvew_notifications);
        noNOtifications = (TextView) findViewById(R.id.notifications);
        progress = ProgressDialog.show(this, "Getting your notifications_page",
                "Please wait...", true);


    }

    public void GetCurrentNotifications() {
        // Tag used to cancel the request

        String cancel_req_tag = "getcurrentnotifications";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_GETNOTIFICATIONS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                Log.d(TAG, "Get Pills Slot Response: " + response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONArray result = jObj.getJSONArray(Config.JSON_ARRAY);
                        //CREATE THE LINEAR LAYOUT FOR THE ARRAY RETURNED of Pills.
                        String MessageTitle = "";
                        String MessageBody = "";
                        String MessageType = "";
                        String MessageDate = "";

                        for (int i = 0; i < result.length(); i++)
                        {
                            JSONObject PillData = result.getJSONObject(i);
                            MessageTitle = PillData.getString(Config.KEY_Pillname);
                            MessageBody = PillData.getString(Config.KEY_PillType);
                            MessageType = PillData.getString(Config.KEY_PillCount);
                            MessageDate = PillData.getString(Config.KEY_PillCount);
                           LinearLayout newNotification = CreateNotificationLayout(MessageTitle, MessageBody);
                            mainView.addView(newNotification);
                /*            LinearLayout separator = CreateSeparator();
                            Separators.add(separator);
                            currentpill.SetPillInfo(CurrPillname,Integer.parseInt(CurrPillType),Integer.parseInt(CurrPillCount), i, newpillayout);
                            currentPills.add(currentpill);
                            mainLayout.addView(newpillayout);
                            mainLayout.addView(separator);
                            tv_emptyslot.setVisibility(View.GONE);*/

                        }


                    } else {

                        noNOtifications.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Get Notifications Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to get slot url
                Map<String, String> params = new HashMap<String, String>();
                //Parameters for the slot of a given product.
                params.put("caregiverid", Config.CAREGIVERID);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);

    }

    public LinearLayout CreateNotificationLayout(String title, String msg)
    {
        TextView tv_title = new TextView(this);
        TextView tv_msg = new TextView(this);
        tv_title.setText(title);
        tv_msg.setText(msg);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(tv_title);
        layout.addView(tv_msg);
        return layout;

    }


    }
