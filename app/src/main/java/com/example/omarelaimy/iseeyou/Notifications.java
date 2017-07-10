package com.example.omarelaimy.iseeyou;
import android.app.Notification;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
    private TextView noNotifications;

    private static final String URL_FOR_GETNOTIFICATIONS ="https://icu.000webhostapp.com/getnotifications.php";
    private ProgressDialog progress;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_page);
        mainView = (LinearLayout) findViewById(R.id.mainvew_notifications);
        noNotifications = (TextView) findViewById(R.id.no_notifications);
        progress = ProgressDialog.show(this, "Getting your notifications_page",
                "Please wait...", true);

        GetCurrentNotifications();
    }

    public void GetCurrentNotifications() {
        // Tag used to cancel the request

        String cancel_req_tag = "getcurrentnotifications";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_GETNOTIFICATIONS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                Log.d(TAG, "Get Current Notifications Response: " + response);
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
                            JSONObject NotificationData = result.getJSONObject(i);
                            MessageTitle = NotificationData.getString(Config.KEY_MSGTITLE);
                            MessageBody = NotificationData.getString(Config.KEY_MSGBODY);
                            MessageType = NotificationData.getString(Config.KEY_MSGTYPE);
                            MessageDate = NotificationData.getString(Config.KEY_MSGDATE);
                          LinearLayout newNotification = CreateNotificationLayout(MessageTitle, MessageBody, MessageType, MessageDate);
                            noNotifications.setVisibility(View.GONE);
                            mainView.addView(newNotification);
                            LinearLayout separator = CreateSeparator();
                            mainView.addView(separator);
                /*            Separators.add(separator);
                            currentpill.SetPillInfo(CurrPillname,Integer.parseInt(CurrPillType),Integer.parseInt(CurrPillCount), i, newpillayout);
                            currentPills.add(currentpill);
                            mainLayout.addView(newpillayout);
                            mainLayout.addView(separator);
                            tv_emptyslot.setVisibility(View.GONE);*/

                        }


                    } else {

                        noNotifications.setVisibility(View.VISIBLE);
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

    public LinearLayout CreateNotificationLayout(String title, String msg, String type, String date)
    {
        TextView tv_title = new TextView(this);
        TextView tv_msg = new TextView(this);
        ImageView iv_notification = CreateNotificationImage(type);
        tv_title.setText(title);
        tv_msg.setText(msg);
        tv_title.setTextColor(getResources().getColor(R.color.welcomescreen));
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        tv_msg.setTextColor(getResources().getColor(R.color.welcomescreen));
        tv_msg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

        //Main layout for single notification
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20,0,0,20);
        layout.setLayoutParams(layoutParams);
        //Layout for the text Views of Notification title and body
        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams1.weight = 4;
        layoutParams1.leftMargin = 15;
        textLayout.setLayoutParams(layoutParams1);
        textLayout.addView(tv_title);
        textLayout.addView(tv_msg);
     layout.addView(iv_notification);
       layout.addView(textLayout);
        return layout;

    }

    public ImageView CreateNotificationImage(String notficationType) {
        ImageView iv = new ImageView(this);
        int src = 0;
        if (notficationType == "1") {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(80, 80);
            src = R.drawable.heartrate;
            layoutParams.weight = 1;
            iv.setImageResource(src);

            //iv.setColorFilter(getResources().getColor(R.color.welcomescreen));
            layoutParams.setMargins(0, 0, 0, 0);
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            iv.setPadding(0, 0, 0, 0);
            iv.setLayoutParams(layoutParams);
        } else if (notficationType == "2") {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(80, 80);
            src = R.drawable.pillbox;
            layoutParams.weight = 1;
            iv.setImageResource(src);
            //iv.setColorFilter(getResources().getColor(R.color.welcomescreen));
            layoutParams.setMargins(0, 0, 0, 0);
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            iv.setPadding(0, 0, 0, 0);
            iv.setLayoutParams(layoutParams);
        }
            else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(50, 50);
            src = R.drawable.capsule_pill;
            layoutParams.weight = 1;
            iv.setImageResource(src);
            //iv.setColorFilter(getResources().getColor(R.color.welcomescreen));
            layoutParams.setMargins(0, 0, 0, 0);
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            iv.setPadding(0, 0, 0, 0);
            iv.setLayoutParams(layoutParams);
            }

        return iv;

    }

    public LinearLayout CreateSeparator() {
        LinearLayout separator = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(android.app.ActionBar.LayoutParams.MATCH_PARENT, 2);
        layoutParams1.setMargins(20, 20, 20, 20);
        separator.setLayoutParams(layoutParams1);
        separator.setBackgroundColor(getResources().getColor(R.color.SeparatorColor));
        return separator;

    }



}
