package com.example.omarelaimy.iseeyou;

import android.graphics.Bitmap;

/**
 * Created by Omar on 6/20/2017.
 */

public class Config {
    //Attributes for Choose Profile Page
    public static final String KEY_NAME = "name";
    public static final String KEY_ID = "patientid";
    public static final String KEY_GENDER = "Gender";
    public static final String KEY_IMAGE = "Image";
    public static final String KEY_RELATION = "Relation";
    public static final String JSON_ARRAY = "result";
    //End Attributes for Choose Profile Page

    // tags used to attach the fragments for navigation bar
    public static final String TAG_APPOINTMENTS = "Appointments";
    public static final String TAG_HEARTRATE = "Heart Rate Monitor";
    public static final String TAG_INVENTORY = "My Inventory";
    public static final String TAG_LOGOUT = "Logout";
    public static final String TAG_NOTIFICATIONS = "Notifications";
    public static final String TAG_PILLBOX = "Pillbox";
    public static final String TAG_SWITCHPROFILE = "Switch Profile";
    public static final String TAG_PROFILE = "Profile";
    public static Bitmap img;
    //Tags used for the Heart rate Fragment
    public static final String TAG_CURRENTHEARTRATE = "current_heart_rate";

}
