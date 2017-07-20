package com.example.omarelaimy.iseeyou;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hp on 09-Jul-17.
 */

public class Notifications extends AppCompatActivity {

    private LinearLayout mainView;
    private static final String TAG = "NotificationsActivity";
    private TextView noNotifications;
    private ImageButton backnotifications;
    private ArrayList<NotificationClass> CaregiverNotifications = new ArrayList<>();
    private ArrayList<LinearLayout> Separators = new ArrayList<>();
    private static final String URL_FOR_GETNOTIFICATIONS ="https://icu.000webhostapp.com/getnotifications.php";

    private ProgressDialog progress;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_page);
        mainView = (LinearLayout) findViewById(R.id.mainvew_notifications);
        noNotifications = (TextView) findViewById(R.id.no_notifications);
        backnotifications = (ImageButton) findViewById(R.id.back_notifications);
        progress = ProgressDialog.show(this, "Getting your notifications",
                "Please wait...", true);



        backnotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        GetCurrentNotifications();
    }

    //Handle the back press.
    @Override
    public void onBackPressed()
    {
        finish();
    }
    //Function that gets the notifications for this day for the caregiver.
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
                       String NotificationID = "";
                        String NotificationTitle = "";
                        String NotificationBody = "";
                        String NotificationType = "";
                        String NotficationDate = "";
                        String NotificationPhone = "";

                        for (int i = 0; i < result.length(); i++)
                        {
                            NotificationClass notification = new NotificationClass();
                            JSONObject NotificationData = result.getJSONObject(i);
                            //Get the response from the server.
                            NotificationID = NotificationData.getString(Config.NOTIFICATIONID);
                            NotificationTitle = NotificationData.getString(Config.NOTIFICATION_TITILE);
                            NotificationBody = NotificationData.getString(Config.NOTIFICATION_BODY);
                            NotificationType = NotificationData.getString(Config.NOTIFICATION_TYPE);
                            NotficationDate = NotificationData.getString(Config.NOTIFICATION_DATE);
                            NotificationPhone = NotificationData.getString(Config.NOTIFICATION_PHONE);
                            LinearLayout newNotification = CreateNotificationLayout(NotificationTitle, NotificationBody, NotificationType, NotficationDate);
                            noNotifications.setVisibility(View.GONE);
                            //Add the notification information to the layout(front-end).
                            mainView.addView(newNotification);
                            //Add the notification information to the notification object to access it later.
                            notification.SetNotificationInfo(NotificationID,NotificationTitle,NotificationBody,Integer.parseInt(NotificationType),NotficationDate,NotificationPhone,newNotification);
                            //Add the notification object to the array of objects Caregivernotifications.
                            CaregiverNotifications.add(notification);
                            LinearLayout separator = CreateSeparator();
                            Separators.add(separator);
                            mainView.addView(separator);
                        }

                        //Caregiver notifications are accessible here. Get all info needed.
                        for (int j = 0; j<CaregiverNotifications.size(); j++)
                        {
                            ImageView call_iv = GetCallIcon(j);
                            ImageView message_iv = GetMessageIcon(j);
                            //ImageView mark_as_read_iv = GetMarkAsRead(j);
                            //Event listeners for each icon from the notification
                            CallIconListener(call_iv,j);
                            MessageIconListener(message_iv,j);
                            //MarkAsReadListener(mark_as_read_iv);
                            LayoutSwipeListener(CaregiverNotifications.get(j).getMainLayout(),j);
                            DeleteIconListner(GetDeleteIcon(j),j);
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


    //Layout creation functions
    public LinearLayout CreateNotificationLayout(String title, String msg, String type, String date)
    {
        TextView tv_title = new TextView(this);
        TextView tv_msg = new TextView(this);
        ImageView iv_notification = CreateNotificationImage(type);
        tv_title.setText(title);
        tv_msg.setText(msg);
        tv_title.setTextColor(getResources().getColor(R.color.welcomescreen));
        tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv_msg.setTextColor(getResources().getColor(R.color.welcomescreen));
        tv_msg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

        //Main layout for single notification
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20,0,0,20);
        layout.setLayoutParams(layoutParams);
        //Layout for the text Views of NotificationClass title and body
        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams1.weight = 4;
        layoutParams1.leftMargin = 10;
        layoutParams1.topMargin = 10;
        textLayout.setLayoutParams(layoutParams1);

        //Create a layout for the date along with the action buttons.
        LinearLayout iconsdate_layout = new LinearLayout(this);
        LinearLayout.LayoutParams iconslayout_Params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iconslayout_Params.setMargins(0,0,0,0);
        iconsdate_layout.setOrientation(LinearLayout.HORIZONTAL);
        iconsdate_layout.setLayoutParams(iconslayout_Params);

        //Add the call icon and message icon to the relative layout.
        //Call icon.
        ImageView call_iv = new ImageView(this);
        LinearLayout.LayoutParams call_Params = new LinearLayout.LayoutParams(135, 135);
        call_Params.setMargins(0,30,0,0);
        call_iv.setLayoutParams(call_Params);
        call_iv.setPadding(35,0,35,0);
        call_iv.setImageResource(R.drawable.call);

        //Message icon.
        ImageView message_iv = new ImageView(this);
        LinearLayout.LayoutParams message_Params = new LinearLayout.LayoutParams(135, 135);
        message_Params.setMargins(0,30,0,0);
        message_iv.setLayoutParams(message_Params);
        message_iv.setPadding(35,0,35,0);
        message_iv.setImageResource(R.drawable.message);

        //Date textview
        TextView date_tv = new TextView(this);
        LinearLayout.LayoutParams date_Params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        date_Params.setMargins(0,70,0,0);
        date_tv.setLayoutParams(date_Params);
        date_tv.setText(date);
        date_tv.setTextColor(getResources().getColor(R.color.welcomescreen));

        iconsdate_layout.addView(call_iv);
        iconsdate_layout.addView(message_iv);
        iconsdate_layout.addView(date_tv);

        textLayout.addView(tv_title);
        textLayout.addView(tv_msg);
        textLayout.addView(iconsdate_layout);

        //Layout for delete icon
        LinearLayout deletelayout = new LinearLayout(this);
        LinearLayout.LayoutParams deletelayoutparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        deletelayoutparams.leftMargin = 20;
        deletelayout.setLayoutParams(deletelayoutparams);
        deletelayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        ImageView delete_iv = new ImageView(this);
        LinearLayout.LayoutParams deleteimgparams = new LinearLayout.LayoutParams(50,50);
        deleteimgparams.gravity = Gravity.CENTER;
        delete_iv.setImageResource(R.drawable.bindelete);
        delete_iv.setColorFilter(getResources().getColor(android.R.color.holo_red_dark));
        delete_iv.setLayoutParams(deleteimgparams);
        deletelayout.addView(delete_iv);
        deletelayout.setVisibility(View.GONE);



        //Delete TextView and setting its layout params
       /* TextView deletemessage = new TextView(this);
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textLayoutParams.gravity = Gravity.CENTER;
        deletemessage.setLayoutParams(textLayoutParams);
        deletemessage.setText("Delete");
        deletemessage.setTypeface(null,Typeface.ITALIC);
        deletemessage.setTextColor(getResources().getColor(android.R.color.white));
        deletemessage.setGravity(Gravity.CENTER);*/

        layout.addView(iv_notification);
        layout.addView(textLayout);
        layout.addView(deletelayout);

        //layout.addView(delete_iv);
        //layout.addView(deletelayout);
        return layout;

    }

    public ImageView CreateNotificationImage(String notficationType) {
        ImageView iv = new ImageView(this);
        int src = 0;
        if (notficationType == "1") {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(210, 210);
            src = R.drawable.heartrate;
            layoutParams.weight = 1;
            iv.setImageResource(src);

            layoutParams.setMargins(0, 0, 0, 0);
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            iv.setPadding(0, 0, 0, 0);
            iv.setLayoutParams(layoutParams);
        } else if (notficationType == "2") {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(210, 210);
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
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
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

    public LinearLayout CreateSeparator()
    {
        LinearLayout separator = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(android.app.ActionBar.LayoutParams.MATCH_PARENT, 2);
        layoutParams1.setMargins(20, 20, 20, 20);
        separator.setLayoutParams(layoutParams1);
        separator.setBackgroundColor(getResources().getColor(R.color.SeparatorColor));
        return separator;

    }

    //End layout creation functions

    //Getting Children from layouts to access their event listeners.
    public ImageView GetCallIcon(int idx)
    {
        LinearLayout notificationLayout = CaregiverNotifications.get(idx).getMainLayout();
        LinearLayout textLayout = (LinearLayout)notificationLayout.getChildAt(1);
        LinearLayout icons_layout = (LinearLayout) textLayout.getChildAt(2);
        ImageView call_iv = (ImageView) icons_layout.getChildAt(0);
        return call_iv;
    }

    public ImageView GetMessageIcon(int idx)
    {
        LinearLayout notificationLayout = CaregiverNotifications.get(idx).getMainLayout();
        LinearLayout textLayout = (LinearLayout)notificationLayout.getChildAt(1);
        LinearLayout icons_layout = (LinearLayout) textLayout.getChildAt(2);
        ImageView message_iv = (ImageView) icons_layout.getChildAt(1);
        return message_iv;
    }
   /* public ImageView GetMarkAsRead(int idx)
    {
        LinearLayout notificationLayout = CaregiverNotifications.get(idx).getMainLayout();
        LinearLayout textLayout = (LinearLayout)notificationLayout.getChildAt(1);
        LinearLayout icons_layout = (LinearLayout) textLayout.getChildAt(2);
        ImageView mark_as_read_iv = (ImageView) icons_layout.getChildAt(2);
        return mark_as_read_iv;
    }*/
    public LinearLayout GetDeleteLayout(int idx)
    {
        LinearLayout notificationLayout = CaregiverNotifications.get(idx).getMainLayout();
        LinearLayout deletelayout = (LinearLayout) notificationLayout.getChildAt(2);
        return deletelayout;
    }

    public ImageView GetDeleteIcon(int idx)
    {
        LinearLayout notificationLayout = CaregiverNotifications.get(idx).getMainLayout();
        LinearLayout deletelayout = (LinearLayout) notificationLayout.getChildAt(2);
        ImageView deleteicon = (ImageView) deletelayout.getChildAt(0);
        return deleteicon;
    }
    //End Getting children from layouts.

    //Event listeners for the icons in the notifications
    public void CallIconListener(ImageView iv,final int idx)
    {
        iv.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
               // Toast.makeText(getApplicationContext(),"Hello from Call Icon", Toast.LENGTH_LONG).show();
                String phone = CaregiverNotifications.get(idx).getPhonenumber();
                int type = CaregiverNotifications.get(idx).GetType();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                if (type == 1)
                {
                    intent.setData(Uri.parse("tel:"+"911"));
                }
                else
                {
                    intent.setData(Uri.parse("tel:"+phone));
                }
                startActivity(intent);
            }
        });
    }

   public void DeleteIconListner(ImageView iv, final int idx)
   {
       iv.setOnClickListener(new View.OnClickListener()
       {
           public void onClick(View v)
           {
               //Delete notification from database
               final String Notficationid = CaregiverNotifications.get(idx).GetNotificationID();

               // Tag used to cancel the request
               String cancel_req_tag = "deletenotification";
               StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_GETNOTIFICATIONS, new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                       Log.d(TAG, "Delete Notification Response: " + response);
                       try {
                           JSONObject jObj = new JSONObject(response);
                           boolean error = jObj.getBoolean("error");
                           if (!error)
                           {
                               Toast.makeText(getApplicationContext(), "Notification deleted successfully", Toast.LENGTH_LONG).show();
                               //remove notification layout from the view
                               //Delete notification from view\
                               LinearLayout deletelayout = CaregiverNotifications.get(idx).getMainLayout();
                               mainView.removeView(deletelayout);
                               LinearLayout deleteseparator = Separators.get(idx);
                               mainView.removeView(deleteseparator);

                           }
                           else
                           {
                               String errorMsg = jObj.getString("error_msg");
                               Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                           }
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               }, new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       Log.e(TAG, "Delete Notification Error: " + error.getMessage());
                       Toast.makeText(getApplicationContext(),
                               error.getMessage(), Toast.LENGTH_LONG).show();
                   }
               }) {
                   @Override
                   protected Map<String, String> getParams() {
                       // Posting params to get slot url
                       Map<String, String> params = new HashMap<String, String>();
                       //Parameters for the slot of a given product.
                       params.put("notificationid", Notficationid);
                       return params;
                   }
               };
               // Adding request to request queue
               AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);


           }
       });
   }

    public void MessageIconListener(ImageView iv,final int idx)
    {
        iv.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                //TODO Implement the message dialog, see if we can get the number of the patient and put it in the dialog message.
               // Toast.makeText(getApplicationContext(),"Hello from Message Icon", Toast.LENGTH_LONG).show();
                String phone = CaregiverNotifications.get(idx).getPhonenumber();
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"+phone));
                sendIntent.putExtra("sms_body", "Hello from Message Icon");
                startActivity(sendIntent);

            }
        });
    }
    public void MarkAsReadListener(ImageView iv)
    {
        iv.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                //TODO Implement the notification deletion from layout, as well as from db. It must be deleted from db with id i think.
                Toast.makeText(getApplicationContext(),"Hello from Mark as read Icon", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void LayoutSwipeListener(LinearLayout layout,int idx)
    {
        final LinearLayout deletelayout = GetDeleteLayout(idx);
        layout.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            public void onSwipeTop() {
                //Toast.makeText(getApplicationContext(), "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                deletelayout.setVisibility(View.GONE);
              //  Toast.makeText(getApplicationContext(), "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                deletelayout.setVisibility(View.VISIBLE);
              //  Toast.makeText(getApplicationContext(), "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
             //   Toast.makeText(getApplicationContext(), "bottom", Toast.LENGTH_SHORT).show();
            }

        });
    }

}
