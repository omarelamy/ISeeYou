package com.example.omarelaimy.iseeyou;

import android.widget.LinearLayout;

/**
 * Created by Omar on 7/12/2017.
 */

public class NotificationClass {
    private String Title;
    private String Body;
    private int Type;
    private String Date;
    private String Phonenumber;
    private LinearLayout NotificationLayout;

    public void SetNotificationInfo(String title,String body,int type,String date,String phonenumber,LinearLayout layout)
    {
        this.Title = title;
        this.Body  = body;
        this.Type  = type;
        this.Date  = date;
        this.Phonenumber = phonenumber;
        this.NotificationLayout = layout;
    }

    public LinearLayout getMainLayout()
    {
        return this.NotificationLayout;
    }

    public String getPhonenumber() { return this.Phonenumber; }


}
