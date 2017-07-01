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
    private ImageView BtnaddPill;
    private PopupWindow mPopupWindow;
    private RelativeLayout mRelativeLayout,CircularPillsLayout,CapsulePillsLayout;
    private RelativeLayout currentPillsLayout, newPillsLayout;
    private EditText Circular_edit,Capsule_edit;
    private NumberPicker CircularPillsPicker,CapsulePillsPicker;
    private TextView newPillsTxt;
    private boolean pillhighlight1 = false;
    private boolean pillhighlight2 = false;
    private int PillType,PillCount,LastPillType;
    private String MedicineName;
    private int addedPillsNum = 0;
    private int pillsImgMargin = 0;
    private int closeImgMargin = 0;
    private  ArrayList<Pill> newPills = new ArrayList<>();
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

        //Set the SlotName to the text from the intent.
        tv_SlotName.setText(SlotDay + " " + SlotDayTime);
        tv_ProductID.setText(ProductID);
        //Current Pills Layout
        currentPillsLayout = (RelativeLayout) findViewById(R.id.view_current_pillslayout);
        //New Pills layout
        newPillsTxt = (TextView) findViewById(R.id.defalut_newpills_txt);
        newPillsLayout = (RelativeLayout) findViewById(R.id.view_new_pillslayout);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Call the set time on both time dialogs.
        setTime(setFromTime,1);
        setTime(setToTime,2);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.edit_slot_layout);


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
                mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);

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
                if(!pillhighlight1 && !pillhighlight2)
                    mPopupWindow.dismiss();

            if((pillhighlight1 && Circular_edit.getText().toString().matches("")) ||(pillhighlight2 && Capsule_edit.getText().toString().matches(""))  )
            {
                Toast.makeText(getApplicationContext(),"Please, Set the name of the pill", Toast.LENGTH_LONG).show();
                return;
            }

            if(pillhighlight1)
            {
                Pill newpill = new Pill();
                MedicineName =  Circular_edit.getText().toString();
                PillCount = CircularPillsPicker.getValue();
                if(LastPillType == 1) //increase margin between two circular pills
                {
                    pillsImgMargin += 70;
                }

                String FullText="";
                if (MedicineName.length() <= 7)
                    FullText = MedicineName + " (" + PillCount + ")";
                else
                {
                    String partialMedicineName  = MedicineName.substring(0, Math.min(MedicineName.length(), 7));
                    FullText = partialMedicineName + "..." +  " (" + PillCount + ")";
                }
                ImageView iv = AddnewImage(newPillsLayout,R.drawable.circular_pill,70,70,pillsImgMargin,10,RelativeLayout.ALIGN_PARENT_LEFT,R.color.textcolor);
                TextView tv = AddnewText(newPillsLayout,150,wrap_content,pillsImgMargin,80,5,RelativeLayout.ALIGN_PARENT_LEFT,FullText,10,2);
                pillsImgMargin += 95;
                closeImgMargin = pillsImgMargin + 4;
                newPillsTxt.setVisibility(View.GONE);
                ImageView closeBtn = AddnewImage(newPillsLayout,R.drawable.x,30,30,closeImgMargin,10,RelativeLayout.ALIGN_PARENT_LEFT,R.color.whitecolor);
                newpill.SetPillInfo(MedicineName,1,PillCount,addedPillsNum-1, iv,closeBtn,tv );
                newPills.add(newpill);
            }

            if(pillhighlight2)
            {
                Pill newpill = new Pill();
                MedicineName = Capsule_edit.getText().toString();
                PillCount = CapsulePillsPicker.getValue();
                if(LastPillType == 1) //incrase margin between circular and capsule pills
                {
                    pillsImgMargin += 50;
                }
                String FullText="";
                if (MedicineName.length() <= 7)
                    FullText = MedicineName + " (" + PillCount + ")";
                else
                {
                    String partialMedicineName  = MedicineName.substring(0, Math.min(MedicineName.length(), 7));
                    FullText = partialMedicineName + "..." +  " (" + PillCount + ")";
                }
                ImageView iv = AddnewImage(newPillsLayout,R.drawable.capsule_pill,70,70,pillsImgMargin,10,RelativeLayout.ALIGN_PARENT_LEFT,R.color.textcolor);
                TextView tv = AddnewText(newPillsLayout,150,wrap_content,pillsImgMargin,80,5,RelativeLayout.ALIGN_PARENT_LEFT,FullText,10,2);
                pillsImgMargin += 95;
                closeImgMargin = pillsImgMargin;
                newPillsTxt.setVisibility(View.GONE);
                ImageView closeBtn = AddnewImage(newPillsLayout,R.drawable.x,30,30,closeImgMargin,10,RelativeLayout.ALIGN_PARENT_LEFT,R.color.whitecolor);
                newpill.SetPillInfo(MedicineName,2,PillCount,addedPillsNum-1, iv,closeBtn,tv );
                newPills.add(newpill);
            }

                //NEW PILLS ARRAY IS ACCESSIBLE HERE.

                addedPillsNum += 1;
                LastPillType = PillType;
                PillType = 0;
                mPopupWindow.dismiss();
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
                        PillType = 1;
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

    //Add new image dynamically to a given layout
   public ImageView AddnewImage(RelativeLayout mainLayout, int src, int width, int height,int marginLeft, int margins,int align,int mycolor)
    {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        ImageView iv_pill = new ImageView(EditSlot.this);
        iv_pill.setImageResource(src);
        iv_pill.setColorFilter(ContextCompat.getColor(getApplicationContext(),mycolor));
        params.setMargins(marginLeft, margins, margins, margins);
        params.addRule(align);
        iv_pill.setLayoutParams(params);
        mainLayout.addView(iv_pill);
        return iv_pill;
    }

    //Add new text dynamically to a given layout
    public TextView AddnewText(RelativeLayout mainLayout,int width, int height, int marginLeft, int marginTop, int margins, int align,
                                             String text, int fontSize, int padding) {

        TextView tv = new TextView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);

        params.setMargins(marginLeft, marginTop, margins, margins);

        tv.setLayoutParams(params);

        tv.setText(text);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize);
        tv.setTextColor(Color.parseColor("#000000"));
        // textView1.setBackgroundColor(0xff66ff66); // hex color 0xAARRGGBB
        tv.setPadding(padding, padding, padding, padding);
        tv.setLayoutParams(params);
        mainLayout.addView(tv);
        return tv;
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