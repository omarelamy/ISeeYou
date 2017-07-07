package com.example.omarelaimy.iseeyou;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import java.lang.reflect.Field;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import android.app.ProgressDialog;
import android.util.Log;
import android.widget.TimePicker;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Omar on 3/15/2017.
 */

public class EditSlot extends AppCompatActivity {
    private static final String TAG = "EditSlotActivity";
    private static final String URL_FOR_EditSlot="https://icu.000webhostapp.com/editslot.php";
    private static final String URL_FOR_GetSlot ="https://icu.000webhostapp.com/getslot.php";
    private String PatientID;
    private String SlotDay,SlotDayTime,ProductID;
    private int insertPillType;
    //ArrayLists
    private ArrayList<Pill> currentPills = new ArrayList<>();
    private List<Integer> currentpillstoremove = new ArrayList<>();
    private ArrayList<LinearLayout> Separators = new ArrayList<>();

    private  ProgressDialog progress;

    //Views for the tool bar
    private TextView tv_ProductID,tv_SlotName;
    private ImageButton closeBtn;

    //Views for the mainlayout
    private TextView tv_emptyslot;
    private Button btn_edit;
    private ImageButton btn_add;
    private Button setFromTime,setToTime;
    private LinearLayout mainLayout;

    //Check Variables
    private boolean SetFromTimeClicked = false;
    private boolean SetToTimeClicked = false;
    private int editclicked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_slot);
        //Get the toolbar Views
        tv_ProductID = (TextView) findViewById(R.id.slot_productid);
        tv_SlotName = (TextView) findViewById(R.id.slot_slotname);
        closeBtn = (ImageButton) findViewById(R.id.slot_cancel_button);
        btn_edit = (Button)  findViewById(R.id.slot_edit_button);
        //Get Views for setting the time
        setFromTime = (Button) findViewById(R.id.from_time);
        setToTime = (Button) findViewById(R.id.to_time);
        //Get Views for the main layout
        mainLayout = (LinearLayout) findViewById(R.id.mainvew_editslot);
        tv_emptyslot = (TextView) findViewById(R.id.empty_slot);
        btn_add = (ImageButton)  findViewById(R.id.add_pill);

        //Get the strings from the intent.
        Bundle extras   = getIntent().getExtras();
        PatientID = extras.getString("patientid");
        ProductID = extras.getString("productid");
        SlotDay  = extras.getString("SlotDay");
        SlotDayTime = extras.getString("SlotDayTime");

        progress = ProgressDialog.show(this, "Getting your pills",
                "Please wait...", true);

        //Call the function that retrieves the product's pills for this slot.
        GetSlotPills();

        //Click listener on the edit in the toolbar
        EditToolBarClickListener(btn_edit);
        //Click listener on the plus at the end of the page.
        AddPillListener(btn_add);

        //Set the SlotName to the text from the intent.
        tv_SlotName.setText(SlotDay + " " + SlotDayTime);
        tv_ProductID.setText(ProductID);
        //Closing EditSlot Page
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

    /*************Event Listeners*************/
    //Event handler for pressing Add button in the edit_slot page
    public void AddPillListener(ImageView button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Alert Dialogue to add new pill
                //User adds the name and the count

                AddPillMessage();
            }
            });
    }

    public void EditToolBarClickListener(Button edit) {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editclicked++;
                ShowEditLabels();
            }
        });
    }

    public void EditPillListener(TextView tv, final int idx) {
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                UpdatePillCountMessage(idx);
            }
        });
    }

    public void DeletePillListener(ImageView iv,final int idx) {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DeletePillMessage(idx);
            }
        });

    }

    /***************Database Functions*****************/

    //Function that retrieves the pills from the database for the given slot and the product.
   public void GetSlotPills() {
       // Tag used to cancel the request

        String cancel_req_tag = "getslotpills";
         StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_GetSlot, new Response.Listener<String>() {
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
                    String CurrPillname = "";
                    String CurrPillCount = "";
                    String CurrPillType = "";
                    String FromTime = "";
                    String ToTime = "";
                    JSONObject initialobj = result.getJSONObject(0);
                    FromTime = initialobj.getString(Config.KEY_SlotFromtime);
                    ToTime = initialobj.getString(Config.KEY_SlotTotime);
                    //Loop on all pills and put them in currentpills array as well as the layout.
                    for (int i = 0; i < result.length(); i++)
                    {
                        Pill currentpill = new Pill();
                        JSONObject PillData = result.getJSONObject(i);
                        CurrPillname = PillData.getString(Config.KEY_Pillname);
                        CurrPillType = PillData.getString(Config.KEY_PillType);
                        CurrPillCount = PillData.getString(Config.KEY_PillCount);
                        LinearLayout newpillayout = CreatePillLayout(CurrPillname, Integer.parseInt(CurrPillCount), Integer.parseInt(CurrPillType));
                        LinearLayout separator = CreateSeparator();
                        Separators.add(separator);
                        currentpill.SetPillInfo(CurrPillname,Integer.parseInt(CurrPillType),Integer.parseInt(CurrPillCount), i, newpillayout);
                        currentPills.add(currentpill);
                        mainLayout.addView(newpillayout);
                        mainLayout.addView(separator);
                        tv_emptyslot.setVisibility(View.GONE);
                        SetToTimeClicked = true;
                        SetFromTimeClicked = true;
                        setFromTime.setText(FromTime);
                        setToTime.setText(ToTime);
                    }

                    //Currentpills array is ACCESSIBLE here.
                    for (int i = 0; i < currentPills.size(); i++)
                    {
                        //Call the onclick listener for each close image for all the added pills.
                        ImageView deleteimg = GetDeleteImage(i);
                        DeletePillListener(deleteimg, i);
                        TextView editpill = GetEditLabel(i);
                        EditPillListener(editpill,i);

                    }
                            } else {
                                Toast.makeText(getApplicationContext(), "Welcome to your new slot!", Toast.LENGTH_LONG).show();
                                //No pills returned, set the visibility of the text to visible to tell the user.
                                tv_emptyslot.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Get Pills slot Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Posting params to get slot url
                        Map<String, String> params = new HashMap<String, String>();
                        //Parameters for the slot of a given product.
                        params.put("productid", ProductID);
                        params.put("slotday", SlotDay);
                        params.put("slotdaytime", SlotDayTime);
                        params.put("patientid", PatientID);
                        return params;
                    }
                };
                // Adding request to request queue
                AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
            }

    //Function that updates the time of the slot with no pills added.
   public void UpdateSlotTime() {
                // Tag used to cancel the request
                String cancel_req_tag = "updateslottime";
                StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_EditSlot, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Update Slot Time Response: " + response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");
                            if (!error) {
                                String error_msg = jObj.getString("error_msg");
                                //Update done
                                Toast.makeText(getApplicationContext(), error_msg, Toast.LENGTH_LONG).show();
                            } else {
                                String errorMsg = jObj.getString("error_msg");
                                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                                setFromTime.setText("Set From Time");
                                setToTime.setText("Set To Time");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Update Slot Time Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Posting params to get slot url
                        Map<String, String> params = new HashMap<String, String>();
                        //Parameters for the slot of a given product.
                        params.put("productid", ProductID);
                        params.put("day", SlotDay);
                        params.put("daytime", SlotDayTime);
                        params.put("fromtime", setFromTime.getText().toString());
                        params.put("totime", setToTime.getText().toString());
                        return params;
                    }
                };
                // Adding request to request queue
                AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);


            }

    //Function that updates the pill count of the selected pill.
   public void UpdatePillCount(final int idx, final int count) {
      final String PillName = currentPills.get(idx).GetPillName();

                // Tag used to cancel the request
                String cancel_req_tag = "updatepillcount";
                StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_EditSlot, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Update Pill count Response: " + response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");
                            if (!error) {
                                currentPills.get(idx).SetCount(count);
                                //Update done
                                Toast.makeText(getApplicationContext(), "Pill " + PillName + " is updated successfully", Toast.LENGTH_LONG).show();
                                //View New count in the layout
                                SetPillsLeftText(idx,count);
                                SetPillIndicator(idx,count);
                            } else {
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
                        Log.e(TAG, "Update Pills slot Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Posting params to get slot url
                        Map<String, String> params = new HashMap<String, String>();
                        //Parameters for the slot of a given product.
                        params.put("productid", ProductID);
                        params.put("day", SlotDay);
                        params.put("daytime", SlotDayTime);
                        params.put("pillname", PillName);
                        params.put("pillcount", String.valueOf(count));
                        params.put("patientid", PatientID);
                        return params;
                    }
                };
                // Adding request to request queue
                AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
            }

    //Function that deletes the selected pill from the database.
   public void DeletePill(int idx) {
                final String PillName = currentPills.get(idx).GetPillName();

                // Tag used to cancel the request
                String cancel_req_tag = "deleteslotpill";
                StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_EditSlot, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Delete Pill Slot Response: " + response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");
                            if (!error) {
                                Toast.makeText(getApplicationContext(), "Pill " + PillName + " is deleted successfully", Toast.LENGTH_LONG).show();
                            } else {
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
                        Log.e(TAG, "Delete Pill slot Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Posting params to get slot url
                        Map<String, String> params = new HashMap<String, String>();
                        //Parameters for the slot of a given product.
                        params.put("productid", ProductID);
                        params.put("day", SlotDay);
                        params.put("daytime", SlotDayTime);
                        params.put("pillname", PillName);
                        return params;
                    }
                };
                // Adding request to request queue
                AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
            }

    //Insert the Pill into the patient's inventory.
    public void InsertPill(final String insertPillName, final String insertPillCount) {

        // Tag used to cancel the request
        // Tag used to cancel the request
        String cancel_req_tag = "insertpills";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_EditSlot, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Insert Pill Slot Response: " + response);
                try
                {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    //Error is false, pill is inserted successfully.
                    if (!error) {
                       insertPillType = Integer.parseInt(jObj.getString("pilltype"));

                       // insertPillType  = Integer.parseInt(jObj.getJSONObject("result").getString("pilltype"));
                        Pill newpill = new Pill();
                        int idx = currentPills.size(); //The index after adding the pill later
                        LinearLayout newpillayout = CreatePillLayout(insertPillName,Integer.parseInt(insertPillCount),insertPillType);
                        LinearLayout separator = CreateSeparator();
                        newpill.SetPillInfo(insertPillName,insertPillType,Integer.parseInt(insertPillCount),idx,newpillayout);
                        currentPills.add(newpill);
                        Separators.add(separator);
                        mainLayout.addView(newpillayout);
                        mainLayout.addView(separator);
                        tv_emptyslot.setVisibility(View.GONE);
                        ImageView newdeleteimage = GetDeleteImage(idx);
                        TextView neweditpill = GetEditLabel(idx);
                        if (editclicked % 2 != 0)
                        {
                            newdeleteimage.setVisibility(View.VISIBLE);
                            neweditpill.setVisibility(View.VISIBLE);
                        }

                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                        for (int i = 0; i<currentPills.size();i++)
                        {
                            ImageView deletepill = GetDeleteImage(i);
                            TextView editpill = GetEditLabel(i);
                            DeletePillListener(deletepill,i);
                            EditPillListener(editpill,i);
                        }

                    } else {
                        //Errors happened, show the error_message and you can mark the errored pills as red.
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
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
                Log.e(TAG, "Add Pill Inventory Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                //Parameters for the patient
                params.put("productid",ProductID);
                params.put("patientid",PatientID);
                params.put("day",SlotDay);
                params.put("daytime",SlotDayTime);
                params.put("fromtime",setFromTime.getText().toString());
                params.put("totime",setToTime.getText().toString());
                params.put("pillname",insertPillName);
                params.put("pillcount",insertPillCount);
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    /////////////////////////////Dialog Messages////////////////////////////////////////////////

    //Update pill count message at pressing edit button
    public void UpdatePillCountMessage(final int idx) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Change Number of pills");
                alertDialog.setMessage("Enter your new pills count");

                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);

                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String count = "";
                                count = input.getText().toString();
                                int intCount = Integer.parseInt(count);
                                if (!count.equals("")) {
                                    if (count.equals(String.valueOf(0))) {
                                        Toast.makeText(getApplicationContext(),
                                                "You cannot update the slot pill with 0, remove it instead.", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    } else if (intCount == currentPills.get(idx).GetPillCount()) {
                                        Toast.makeText(getApplicationContext(),
                                                "The number of the pills is the same as before.", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    } else {
                                        //Valid new count(different than the displayed one).
                                        //Call the function to update the pillcount of the db.
                                        UpdatePillCount(idx, Integer.parseInt(count));
                                    }
                                }
                            }
                        });

                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }

    //Insert PillName and Count to add new pill to the slot
    public void AddPillMessage() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Add New Pill");
        alertDialog.setMessage("Enter the new pill name and the number of pills you want to add.");
        LinearLayout layout = new LinearLayout(EditSlot.this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText et_pillname = new EditText(this);
        et_pillname.setInputType(InputType.TYPE_CLASS_TEXT);
        et_pillname.setHint("Pill Name");

        final EditText et_pillcount = new EditText(this);
        et_pillcount.setInputType(InputType.TYPE_CLASS_NUMBER);
        et_pillcount.setHint("Number of Pills");

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp2.topMargin = 10;

        layout.setLayoutParams(lp1);
        et_pillname.setLayoutParams(lp2);
        et_pillcount.setLayoutParams(lp2);

        layout.addView(et_pillname);
        layout.addView(et_pillcount);

        alertDialog.setView(layout);


        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String pillname = "";
                        String pillcount = "";

                        pillname = et_pillname.getText().toString();
                        pillcount = et_pillcount.getText().toString();
                       //Check if pillname is empty
                        if (!pillname.equals("")) {
                            if (!pillcount.equals("")) {
                                if (pillcount.equals(String.valueOf(0))) {
                                    Toast.makeText(getApplicationContext(),
                                            "You cannot update the slot pill with 0, remove it instead.", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                } else {
                                    //Valid new name and count
                                    //Check if time is set or not
                                    if (!SetFromTimeClicked && !SetToTimeClicked)
                                        Toast.makeText(getApplicationContext(),"Please set the From time and the To time of the slot.", Toast.LENGTH_LONG).show();

                                        //Check if From Time is not set
                                    else if (!SetFromTimeClicked)
                                        Toast.makeText(getApplicationContext(),"Please set the From time of the slot.", Toast.LENGTH_LONG).show();

                                        //Check if To Time is not set
                                    else if (!SetToTimeClicked)
                                        Toast.makeText(getApplicationContext(),"Please set the To time of the slot.", Toast.LENGTH_LONG).show();
                                    else
                                    //Call the function to add the pill in the database
                                    InsertPill(pillname,pillcount);
                                }
                            }
                        }
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

            //Slot Time Message.
    public void EditSlotTimeMessage() {
                new AlertDialog.Builder(this)
                        .setTitle("Edit Slot Time")
                        .setMessage("There are no new pills to be added. Do you want to edit the slot time only?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Call update query to update the current pills time.
                                UpdateSlotTime();
                            }
                        }).setNegativeButton("No", null).show();

            }

            //Delete Pill Message.
    public void DeletePillMessage(final int idx) {
                new AlertDialog.Builder(this)
                        .setTitle("Delete Pill")
                        .setMessage("Are you sure you want to delete the pill completely from this slot?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                //Call function of database to delete from database.
                                DeletePill(idx);
                                //Remove from layout
                                mainLayout.removeView(currentPills.get(idx).GetPillLayout());
                                mainLayout.removeView(Separators.get(idx));

                                currentpillstoremove.add(idx);
                                if (currentPills.size() == currentpillstoremove.size())
                                    tv_emptyslot.setVisibility(View.VISIBLE);

                            }
                        }).setNegativeButton("No", null).show();

            }

    //////////////////////View Functions/////////////////////////////////////////
    public LinearLayout CreatePillLayout(String PillName, int PillCount, int PillType) {
                //main layout for each pill
                LinearLayout PillLayout = new LinearLayout(EditSlot.this);
                PillLayout.setOrientation(LinearLayout.HORIZONTAL);

                //Children of the pill layout2
                LinearLayout pillIndicator = new LinearLayout(EditSlot.this);
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(46, 300);
                layoutParams1.setMargins(5, 10, 20, 10);
                //layoutParams1.weight = 1;
                pillIndicator.setLayoutParams(layoutParams1);

                pillIndicator.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                pillIndicator.setPadding(0, 0, 0, 0);


                //Layout for pill Description
                LinearLayout pillInfo = new LinearLayout(EditSlot.this);
                pillInfo.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(android.app.ActionBar.LayoutParams.WRAP_CONTENT, android.app.ActionBar.LayoutParams.MATCH_PARENT);
                layoutParams2.setMargins(30, 10, 20, 10);
                layoutParams2.weight = 6;
                pillInfo.setLayoutParams(layoutParams2);
                //TextView for pillinfo
                TextView tv_pillname = new TextView(EditSlot.this);
                TextView tv_pillcount = new TextView(EditSlot.this);
                TextView edit_button = new TextView(EditSlot.this);
                //Setting the info for the pill name.
                tv_pillname.setText(PillName);
                tv_pillname.setTextColor(getResources().getColor(R.color.welcomescreen));
                tv_pillname.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

                //Create a layout for the text of pills left along with edit button
                LinearLayout pilldescription = new LinearLayout(EditSlot.this);
                LinearLayout.LayoutParams pilldescription_params = new LinearLayout.LayoutParams(android.app.ActionBar.LayoutParams.WRAP_CONTENT, android.app.ActionBar.LayoutParams.WRAP_CONTENT);
                pilldescription.setOrientation(LinearLayout.HORIZONTAL);
                pilldescription_params.setMargins(20, 0, 0, 70);
                pilldescription.setPadding(0, 0, 0, 0);
                pilldescription.setLayoutParams(pilldescription_params);
                //Text for viewing the pills left.
                if(PillCount == 1)
                    tv_pillcount.setText(Integer.toString(PillCount) + " pill");
                else
                    tv_pillcount.setText(Integer.toString(PillCount) + " pills");
                tv_pillcount.setTypeface(null, Typeface.ITALIC);
                tv_pillcount.setTextColor(getResources().getColor(R.color.greycolor));
                tv_pillcount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

                //Button for edit.
                edit_button.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                edit_button.setText("edit");
                edit_button.setAllCaps(false);
                edit_button.setPaintFlags(edit_button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                edit_button.setTextColor(getResources().getColor(R.color.welcomescreen));
                edit_button.setTypeface(null, Typeface.ITALIC);
                edit_button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                edit_button.setPadding(20, 0, 20, 0);
                edit_button.setVisibility(View.GONE);

                pilldescription.addView(tv_pillcount);
                pilldescription.addView(edit_button);

                pillInfo.addView(tv_pillname);
                pillInfo.addView(pilldescription);

                //ImageView for the icon
                ImageView iv_pilltype = CreatePillView(PillType);

                //Img for the X button
                LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(25, 25);
                layoutParams4.weight = 1;
                layoutParams4.setMargins(20, 0, 0, 0);
                int src1 = R.drawable.x;
                ImageView deletebtn = new ImageView(EditSlot.this);
                deletebtn.setImageResource(src1);
                deletebtn.setColorFilter(getResources().getColor(R.color.welcomescreen));
                deletebtn.setPadding(0, 0, 0, 0);
                deletebtn.setVisibility(View.GONE);

                PillLayout.addView(pillIndicator);
                PillLayout.addView(pillInfo);
                PillLayout.addView(iv_pilltype);
                PillLayout.addView(deletebtn, layoutParams4);
                return PillLayout;
            }

    public ImageView CreatePillView(int PillType) {
                ImageView iv = new ImageView(EditSlot.this);
                int src = 0;
                if (PillType == 1) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(90, 90);
                    src = R.drawable.circular_pill;
                    layoutParams.weight = 1;
                    iv.setImageResource(src);
                    iv.setColorFilter(getResources().getColor(R.color.welcomescreen));
                    layoutParams.setMargins(0, 60, 0, 0);
                    iv.setPadding(0, 0, 0, 0);
                    iv.setLayoutParams(layoutParams);
                } else if (PillType == 2) {
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                    src = R.drawable.capsule_pill;
                    layoutParams.weight = 1;
                    iv.setImageResource(src);
                    iv.setColorFilter(getResources().getColor(R.color.welcomescreen));
                    layoutParams.setMargins(0, 60, 0, 0);
                    iv.setPadding(0, 0, 0, 0);
                    iv.setLayoutParams(layoutParams);

                }
                return iv;

            }

    public void SetPillIndicator(int idx,int count) {
        LinearLayout pilllayout = currentPills.get(idx).GetPillLayout();
        LinearLayout pillindicator = (LinearLayout) pilllayout.getChildAt(0);
        pillindicator.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }

    public LinearLayout CreateSeparator() {
                LinearLayout separator = new LinearLayout(EditSlot.this);
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(android.app.ActionBar.LayoutParams.MATCH_PARENT, 2);
                layoutParams1.setMargins(20, 20, 20, 20);
                separator.setLayoutParams(layoutParams1);
                separator.setBackgroundColor(getResources().getColor(R.color.SeparatorColor));
                return separator;

            }

    public ImageView GetDeleteImage(int idx) {
        LinearLayout pilllayout = currentPills.get(idx).GetPillLayout();
        ImageView delete = (ImageView) pilllayout.getChildAt(3);
        return delete;
    }

    public void SetPillsLeftText(int idx,int count) {
        LinearLayout pilllayout = currentPills.get(idx).GetPillLayout();
        LinearLayout pillinfo = (LinearLayout) pilllayout.getChildAt(1);
        LinearLayout pillcount = (LinearLayout) pillinfo.getChildAt(1);
        TextView pillsleft = (TextView) pillcount.getChildAt(0);
        if(count == 1)
            pillsleft.setText(Integer.toString(count) + " pill left");
        else
            pillsleft.setText(Integer.toString(count) + " pills left");
    }


    //////////////////////////////////////////////////////////////////////////////
    public void ShowEditLabels() {

        for (int i = 0; i < currentPills.size(); i++) {
            TextView edit = GetEditLabel(i);
            ImageView delete = GetDeleteImage(i);
            if (editclicked % 2 != 0) {
                edit.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
            } else {
                edit.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
            }
        }
    }

    public TextView GetEditLabel(int idx) {
        LinearLayout pilllayout = currentPills.get(idx).GetPillLayout();
        LinearLayout pillinfo = (LinearLayout) pilllayout.getChildAt(1);
        LinearLayout pillcount = (LinearLayout) pillinfo.getChildAt(1);
        TextView edit = (TextView) pillcount.getChildAt(1);
        return edit;
    }

///////////////////////////////Time Functions////////////////////////////////////
     public void setTime(Button button, final int id) {
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
                        selectedTime = Integer.toString(selectedHour) + ":";
                    if (selectedMinute < 10)
                        selectedTime += "0" + Integer.toString(selectedMinute);
                    else
                        selectedTime += Integer.toString(selectedMinute);
                    if (id == 1) {
                        setFromTime.setText(selectedTime);
                        SetFromTimeClicked = true;
                    } else {
                        setToTime.setText(selectedTime);
                        SetToTimeClicked = true;
                    }

                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        }
    });
}

    //Function that changes the color of the number picker.
    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        final int count = numberPicker.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                } catch (NoSuchFieldException e) {
                    Log.d("NumberPickerTextColor", e.toString());
                } catch (IllegalAccessException e) {
                    Log.d("NumberPickerTextColor", e.toString());
                } catch (IllegalArgumentException e) {
                    Log.d("NumberPickerTextColor", e.toString());
                }
            }
        }
        return false;
    }

 }
