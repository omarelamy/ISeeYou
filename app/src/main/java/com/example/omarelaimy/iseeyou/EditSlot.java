package com.example.omarelaimy.iseeyou;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.omarelaimy.iseeyou.Fragments.PillboxFragment;
import static com.example.omarelaimy.iseeyou.Fragments.PillboxFragment.newPillBoxInstance;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;



/**
 * Created by Omar on 3/15/2017.
 */

public class EditSlot extends AppCompatActivity {
    private String SlotDay,SlotDayTime,PatientID,ProductID;
    private TextView tv_ProductID,tv_SlotName;
    private ImageButton closeBtn;
    private Button setFromTime,setToTime;
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

        //Set the SlotName to the text from the intent.
        tv_SlotName.setText(SlotDay + " " + SlotDayTime);
        tv_ProductID.setText(ProductID);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Call the set time on both time dialogs.
        setTime(setFromTime,1);
        setTime(setToTime,2);


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



}