package com.example.omarelaimy.iseeyou;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaExtractor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import java.lang.reflect.Field;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.omarelaimy.iseeyou.Fragments.PillboxFragment;
import static com.example.omarelaimy.iseeyou.Fragments.PillboxFragment.newPillBoxInstance;
import static com.example.omarelaimy.iseeyou.R.id.imageView;
import static com.example.omarelaimy.iseeyou.R.id.wrap_content;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.view.ViewGroup.LayoutParams;

/**
 * Created by Omar on 3/15/2017.
 */

public class EditSlot extends AppCompatActivity {
    private String SlotDay,SlotDayTime,PatientID,ProductID;
    private TextView tv_ProductID,tv_SlotName;
    private ImageButton closeBtn;
    private Button setFromTime,setToTime,DoneAddPill;
    private Button BtnUpdateCurrent,BtnAddNew;
    private ImageView BtnaddPill;
    private PopupWindow mPopupWindow;
    private LinearLayout mLinearLayout;
    private RelativeLayout CircularPillsLayout,CapsulePillsLayout;
    private LinearLayout currentPillsLayout, newPillsLayout;
    private EditText Circular_edit,Capsule_edit;
    private NumberPicker CircularPillsPicker,CapsulePillsPicker;
    private TextView newPillsTxt;
    private boolean pillhighlight1 = false;
    private boolean pillhighlight2 = false;
    private int PillType,PillCount;
    private String MedicineName;
    private int addedPillsNum = 0;
    private  ArrayList<Pill> newPills = new ArrayList<>();
    private List<Integer> toremove = new ArrayList<>();
    private ArrayList<LinearLayout> SinglePillLayout = new ArrayList<>();
    private static final String TAG = "EditSlotActivity";
    private static final String URL_FOR_EditSlot="";  /*"https://icu.000webhostapp.com/login.php"*/;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_slot);
        //Get the productid along with the slot name, morning,afternoon...etc.
        tv_ProductID = (TextView) findViewById(R.id.slot_productid);
        tv_SlotName = (TextView) findViewById(R.id.slot_slotname);
        closeBtn = (ImageButton) findViewById(R.id.slot_cancel_button);
        setFromTime = (Button) findViewById(R.id.from_time);
        setToTime = (Button) findViewById(R.id.to_time);
        //Get the strings from the intent.
        Bundle extras   = getIntent().getExtras();
        PatientID = extras.getString("patientid");
        ProductID = extras.getString("productid");
        SlotDay  = extras.getString("SlotDay");
        SlotDayTime = extras.getString("SlotDayTime");


        //Get the button.
        BtnaddPill = (ImageView) findViewById(R.id.add_pill);
        BtnAddNew = (Button) findViewById(R.id.btn_add_pills);
       // BtnUpdateCurrent = (Button) findViewById(R.id.btn_update_pills);
        //Set the SlotName to the text from the intent.
        tv_SlotName.setText(SlotDay + " " + SlotDayTime);
        tv_ProductID.setText(ProductID);
        //Current Pills Layout
        currentPillsLayout = (LinearLayout) findViewById(R.id.view_current_pillslayout);
        //New Pills layout
        newPillsTxt = (TextView) findViewById(R.id.defalut_newpills_txt);
        newPillsLayout = (LinearLayout) findViewById(R.id.view_new_pillslayout);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Call the set time on both time dialogs.
        setTime(setFromTime,1);
        setTime(setToTime,2);

        mLinearLayout = (LinearLayout) findViewById(R.id.edit_slot_layout);
        //Call the function of loading the popup window.
        AddPillListener(BtnaddPill);
    }


    //Event handler for pressing Add button in the edit_slot page
    public void AddPillListener(ImageView button)
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.add_pill,null);
                pillhighlight1 = false;
                pillhighlight2 = false;
                // Initialize a new instance of popup window
                mPopupWindow = new PopupWindow(
                        customView,
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT
                );

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }


                //Get the layouts for the pills count.
                CircularPillsLayout = (RelativeLayout) customView.findViewById(R.id.circular_pillscount_layout);
                CapsulePillsLayout =  (RelativeLayout) customView.findViewById(R.id.capsule_pillscount_layout);

                //Get the pickers for the pills count
                CircularPillsPicker = (NumberPicker) customView.findViewById(R.id.circular_count);
                CapsulePillsPicker = (NumberPicker) customView.findViewById(R.id.capsule_count);

                //Set the min and max values for the pill
                CircularPillsPicker.setMinValue(1);
                CircularPillsPicker.setMaxValue(10);
                CapsulePillsPicker.setMinValue(1);
                CapsulePillsPicker.setMaxValue(10);

                //Set the color for the text in the wheel
                setNumberPickerTextColor(CircularPillsPicker, Color.parseColor("#181c2c"));
                setNumberPickerTextColor(CapsulePillsPicker, Color.parseColor("#181c2c"));

                //Set the wrap selector wheel
                CircularPillsPicker.setWrapSelectorWheel(true);
                CapsulePillsPicker.setWrapSelectorWheel(true);

                // Get a reference for the custom view close button
                ImageButton closeButton = (ImageButton) customView.findViewById(R.id.pill_cancel_button);
                // Get the Image views of the pills shapes.
                ImageView Circular_Pill = (ImageView) customView.findViewById(R.id.circular_pillimage);
                ImageView Capsule_Pill = (ImageView) customView.findViewById(R.id.capsule_pillimage);

                //Get the Edit texts
                Circular_edit = (EditText) customView.findViewById(R.id.circular_pillid);
                Capsule_edit  = (EditText) customView.findViewById(R.id.capsule_pillid);

                //Call the functions on the image listeners.
                PillImageListener(Circular_Pill,Circular_edit,CircularPillsLayout,1,Capsule_Pill,Capsule_edit,CapsulePillsLayout);
                PillImageListener(Capsule_Pill, Capsule_edit,CapsulePillsLayout,2,Circular_Pill,Circular_edit,CircularPillsLayout);

                //Get the done Add pill button
                DoneAddPill = (Button) customView.findViewById(R.id.done_add_pill);

                //Call the function on done pill
                DoneAddPillListener(DoneAddPill);

                // Set a click listener for the popup window close button
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                    }
                });
                // Finally, show the popup window at the center location of root relative layout
                mPopupWindow.setFocusable(true);
                mPopupWindow.update();
                mPopupWindow.showAtLocation(mLinearLayout, Gravity.CENTER,0,0);

            }
        });
    }

    //Event handler for pressing the DONE button in add_pill menu
    public void DoneAddPillListener (Button button)
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!pillhighlight1 && !pillhighlight2) {
                    mPopupWindow.dismiss();
                    return;
                }

                if((pillhighlight1 && Circular_edit.getText().toString().matches("")) ||(pillhighlight2 && Capsule_edit.getText().toString().matches(""))  )
                {
                    Toast.makeText(getApplicationContext(),"Please, Set the name of the pill", Toast.LENGTH_LONG).show();
                    return;
                }

                Pill newpill = new Pill();
                ImageView iv = new ImageView(EditSlot.this);
                TextView tv = new TextView(EditSlot.this);
                String FullText="";

                if(pillhighlight1)
                {
                    MedicineName =  Circular_edit.getText().toString();
                    PillCount = CircularPillsPicker.getValue();
                    iv = AddnewImage(newPillsLayout, R.drawable.circular_pill, 50, 50, 0 ,0, R.color.textcolor);
                   }

                if(pillhighlight2)
                {
                    MedicineName = Capsule_edit.getText().toString();
                    PillCount = CapsulePillsPicker.getValue();
                   iv = AddnewImage(newPillsLayout,R.drawable.capsule_pill,50,50,0,0,R.color.textcolor);
                }
                //Common code for both pills
                //Setting the medicine name displayed
                if (MedicineName.length() <= 6)
                    FullText = MedicineName + " (" + PillCount + ")";
                else
                {
                    String partialMedicineName  = MedicineName.substring(0, Math.min(MedicineName.length(), 6));
                    FullText = partialMedicineName + "..." +  " (" + PillCount + ")";
                }

                tv = AddnewText(newPillsLayout,150,30,0,FullText,10,2);
                newPillsTxt.setVisibility(View.GONE);
                ImageView closeBtn = AddnewImage(newPillsLayout,R.drawable.x,20,20,0,0,R.color.whitecolor);
                newpill.SetPillInfo(MedicineName,PillType,PillCount,addedPillsNum,iv,closeBtn,tv);
                newPills.add(newpill);
                LinearLayout mylayout = CreateSinglePillView(iv,tv,closeBtn);
                SinglePillLayout.add(mylayout);
                newPillsLayout.addView(mylayout);
                //NEW PILLS ARRAY IS ACCESSIBLE HERE.
                for (int i = 0; i<newPills.size();i++)
                {
                    //Call the onclick listener for each close image for all the added pills.
                    ClosePillImageListener(newPills.get(i).GetCloseimg(),i);
                }
                addedPillsNum += 1;
                PillType = 0;
                mPopupWindow.dismiss();
            }
        });

    }

    public void ClosePillImageListener(final ImageView iv,final int idx)
    {
        iv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                //Remove the layout corresponding to the clicked (x).
                newPillsLayout.removeView(SinglePillLayout.get(idx));
                newPillsLayout.removeView(iv);
                toremove.add(idx);
                //newPills.remove(idx);
                //SinglePillLayout.remove(idx);
                if(newPills.size() == toremove.size() )
                    newPillsTxt.setVisibility(View.VISIBLE);
            }
        });
    }
    //Function that listens for the selected ImageView whether the circular pill or capsule pill.
    public void PillImageListener(final ImageView iv,final EditText et,final RelativeLayout mylayout, final int pillno,final ImageView other_iv, final EditText other_et, final RelativeLayout otherlayout)
    {

        iv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(pillno == 1)
                {
                    if(pillhighlight1) //remove highlight
                    {
                        pillhighlight1 = false;
                        PillType = 0;
                        iv.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.welcomescreen));
                        et.setText("");
                        et.setVisibility(View.GONE);
                        mylayout.setVisibility(View.GONE);
                    }
                    else  //add hilight to pill1 and remove from pill2
                    {
                        pillhighlight1 = true;
                        PillType = 1;
                        iv.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent));
                        et.setVisibility(View.VISIBLE);
                        mylayout.setVisibility(View.VISIBLE);
                        pillhighlight2 = false;
                        //disable other pill
                        other_iv.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.welcomescreen));
                        other_et.setText("");
                        other_et.setVisibility(View.GONE);
                        otherlayout.setVisibility(View.GONE);
                    }
                }

                if(pillno == 2)
                {
                    if(pillhighlight2) //remove highlight
                    {
                        pillhighlight2 = false;
                        PillType = 0;
                        iv.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.welcomescreen));
                        et.setText("");
                        et.setVisibility(View.GONE);
                        mylayout.setVisibility(View.GONE);
                    }
                    else //add hilight to pill1 and remove from pill2
                    {
                        pillhighlight2 = true;
                        PillType = 2;
                        iv.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent));
                        et.setVisibility(View.VISIBLE);
                        mylayout.setVisibility(View.VISIBLE);
                        pillhighlight1 = false;
                        //remove highlight of other view
                        other_iv.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.welcomescreen));
                        other_et.setText("");
                        other_et.setVisibility(View.GONE);
                        otherlayout.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    public void AddSlotPillsListener(Button button)
    {
      button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //TODO Remove indecies marketed at arraylist remove from newPills and SinglePillsLayout
                //toremove.add(idx);

                //Check if both times are not set.
             /*     if (setFromTime.getText() == "Set From Time" && setToTime.getText() == "Set To Time")
                    Toast.makeText(getApplicationContext(),"Please set the From time and the To time of the slot.", Toast.LENGTH_LONG).show();

                    //Check if From Time is not set
                else if (setFromTime.getText() == "Set From Time")
                    Toast.makeText(getApplicationContext(),"Please set the From time of the slot.", Toast.LENGTH_LONG).show();

                    //Check if To Time is not set
                else if (setToTime.getText() == "Set To Time")
                    Toast.makeText(getApplicationContext(),"Please set the To time of the slot.", Toast.LENGTH_LONG).show();

                else
                {
                    //Check if newpills is size is empty.
                    //The function misses the update query fro the slotTime.
                    if (newPills.size() == 0)
                        EditSlotTimeMessage();
                    //It's safe to insert into the db with new pills
                    InsertPills();
                }*/
            }

        });

    }

    //Add new image dynamically to a given layout
    public ImageView AddnewImage(LinearLayout mainLayout, int src, int width, int height, int leftmargin, int margins,int mycolor)
    {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        ImageView iv_pill = new ImageView(EditSlot.this);
        iv_pill.setImageResource(src);
        iv_pill.setColorFilter(ContextCompat.getColor(getApplicationContext(),mycolor));
        params.setMargins(leftmargin, margins, margins, margins);
       iv_pill.setPadding(0,0,0,0);
        iv_pill.setLayoutParams(params);
        return iv_pill;
    }

    //Add new text dynamically to a given layout
    public TextView AddnewText(LinearLayout mainLayout,int width, int height, int margins,
                               String text, int fontSize, int padding) {

        TextView tv = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins(0, 0, 0, 0);
        tv.setLayoutParams(params);
        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        tv.setTextColor(Color.parseColor("#000000"));
        // textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
       tv.setPadding(0, 0, 0, 0);
        tv.setLayoutParams(params);
        return tv;
    }

    public LinearLayout CreateSinglePillView(ImageView iv,TextView tv, ImageView deletebtn)
    {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 150);
        layoutParams.setMargins(50, 10, 10, 0);
        LinearLayout layout = new LinearLayout(EditSlot.this);
        layout.setLayoutParams(layoutParams);
        layout.setPadding(0,0,0,0);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(deletebtn);
        layout.addView(iv);
        layout.addView(tv);
        return layout;
    }

    public void InsertPills()
    {
        Toast.makeText(getApplicationContext(), "The Product ID is " + ProductID, Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "The Patient ID is " + PatientID, Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "The Slot Day is " + SlotDay, Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "The Slot Day Time is " + SlotDayTime, Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "The Set From Time is " + setFromTime.getText().toString(), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "The Set To Time is " + setToTime.getText().toString() , Toast.LENGTH_LONG).show();


        // Tag used to cancel the request
        /*String cancel_req_tag = "createprofile";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_EditSlot, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Edit Slot Response: " + response);
                try
                {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error)
                    {
                        //String patient = jObj.getJSONObject("patient").getString("name");
                        //Toast.makeText(getApplicationContext(), "Profile for " + patient + " is successfully Added!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Edit slot Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                //Strings used to fill the pills names' and their counts.
                String PillsNames = "";
                String PillsCount = "";
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                //Parameters for the patient
                params.put("productid",ProductID);
                params.put("patientid",PatientID);
                params.put("day",SlotDay);
                params.put("daytime",SlotDayTime);
                params.put("fromtime",setFromTime.getText().toString());
                params.put("totime",setToTime.getText().toString());
                //Send the array of the pills names' and array of pills counts separated by commas.
                for (int i = 0 ;i<newPills.size();i++)
                {
                    if (i == (newPills.size()-1))
                    {
                        PillsNames += newPills.get(i).GetPillName();
                        PillsCount += String.valueOf(newPills.get(i).GetPillCount());
                    }
                    else
                    {
                        PillsNames += newPills.get(i).GetPillName() + ",";
                        PillsCount += String.valueOf(newPills.get(i).GetPillCount()) + ",";
                    }
                }
                params.put("pillsnames",PillsNames);
                params.put("pillscount",PillsCount);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
*/
    }


    //Slot Time Message.
    public void EditSlotTimeMessage()
    {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("There are no new pills to beadded. Do you want to edit the slot time only?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Call update query to update the current pills time.
                    }
                }).setNegativeButton("No", null).show();

    }


    public void setTime(Button button, final int id)
    {
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditSlot.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String selectedTime = "";
                        if (selectedHour < 10)
                            selectedTime = "0" + Integer.toString(selectedHour) + ":";
                        else
                            selectedTime = Integer.toString(selectedHour)+ ":";
                        if(selectedMinute < 10)
                            selectedTime += "0" + Integer.toString(selectedMinute);
                        else
                            selectedTime += Integer.toString(selectedMinute);
                        if (id == 1)
                            setFromTime.setText(selectedTime);
                        else
                            setToTime.setText(selectedTime);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
    }

    //Function that changes the color of the number picker.
    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color)
    {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText)child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                }
                catch(NoSuchFieldException e){
                    Log.d("NumberPickerTextColor", e.toString());
                }
                catch(IllegalAccessException e){
                    Log.d("NumberPickerTextColor", e.toString());
                }
                catch(IllegalArgumentException e){
                    Log.d("NumberPickerTextColor", e.toString());
                }
            }
        }
        return false;
    }
}