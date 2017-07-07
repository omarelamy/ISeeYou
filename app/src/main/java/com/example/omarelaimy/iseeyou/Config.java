package com.example.omarelaimy.iseeyou;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
    public static final String KEY_PRODUCTID = "productid";
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

    //Tags used for the pill retrieval
    public static final String KEY_Pillname = "medicinename";
    public static final String KEY_PillType = "medicinetype";
    public static final String KEY_PillCount= "medicinecount";
    public static final String KEY_SlotFromtime = "slotfromtime";
    public static final String KEY_SlotTotime = "slottotime";

    //Tags used for pill insertion
    public static final String Key_PillInsertError = "error";
    public static final String Key_PillInsertErrormsg = "error_msg";
    public static final String Key_PillInsertName = "pillname";
    public static final String Key_PillInsertCount = "pillcount";

    //Tags used for inventory of patient
    public static final String Key_InventoryPillName = "medicinename";
    public static final String Key_InventoryPillType = "medicinetype";
    public static final String Key_InventoryPillCount = "medicinecount";

    //Tags used for monitoring info of slot.
    public static final String KEY_SLOTID = "slotid";
    public static final String KEY_TAKEN = "taken";
    public static final String KEY_TIMEOUT = "timeout";

    //Tags used for sending the notifications.
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";
    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String SHARED_PREF = "ah_firebase";


}
