package com.example.omarelaimy.iseeyou.Fragments;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.GetChars;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import static com.example.omarelaimy.iseeyou.EditSlot.setNumberPickerTextColor;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.omarelaimy.iseeyou.AppSingleton;
import com.example.omarelaimy.iseeyou.Config;
import com.example.omarelaimy.iseeyou.EditSlot;
import com.example.omarelaimy.iseeyou.Patient;
import com.example.omarelaimy.iseeyou.Pill;
import com.example.omarelaimy.iseeyou.Pill_Inventory;
import com.example.omarelaimy.iseeyou.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.example.omarelaimy.iseeyou.EditSlot.setNumberPickerTextColor;
import static com.example.omarelaimy.iseeyou.R.id.wrap_content;


public class InventoryFragment extends Fragment {
    private static final String patientparameter = "patientid";
    private String PatientID;
    private ArrayList<Pill_Inventory> PatientInventory = new ArrayList<>();
    private ArrayList<LinearLayout> Separators = new ArrayList<>();
    private  ProgressDialog progress;
    private View mainview;
    private LinearLayout mainlayout;
    private TextView tv_welcome;
    private boolean pillhighlight1 = false;
    private boolean pillhighlight2 = false;
    private String InsertPillName;
    private int InsertPillType;
    private int InsertPillCount;
    private PopupWindow mPopupWindow;
    private RelativeLayout CircularPillsLayout,CapsulePillsLayout;
    private NumberPicker CircularPillsPicker,CapsulePillsPicker;
    private EditText Circular_edit,Capsule_edit;
    private Button DoneAddPill;
    private int PillType=0;
    private static final String TAG = "InventoryActivity";
    private static final String URL_FOR_GetInventory ="https://icu.000webhostapp.com/getinventory.php";
    private static final String URL_FOR_EditInventory="https://icu.000webhostapp.com/editinventory.php";
    private OnFragmentInteractionListener mListener;
    private Button btn_edit;
    private ImageButton btn_add;
    private int editclicked = 0;
    private int deletecheck = 0;

    public InventoryFragment() {
        // Required empty public constructor
    }


    public static InventoryFragment newInventoryInstance(String patientid) {
        InventoryFragment fragment = new InventoryFragment();
        Bundle args = new Bundle();
        args.putString(patientparameter, patientid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            PatientID = getArguments().getString(patientparameter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
         mainview  =  inflater.inflate(R.layout.fragment_inventory, container, false);
        mainlayout = (LinearLayout) mainview.findViewById(R.id.mainview_inventory);

        progress = ProgressDialog.show(getActivity(), "Getting your pills",
                "Please wait...", true);

        //Get the empty welcome text.
        tv_welcome = (TextView) mainview.findViewById(R.id.empty_pillstext);

        //Call the function to retrieve the current pills of the patient in the inventory.
        GetPatientInventory();


        //Get the Edit button for the navbar
        btn_edit = (Button) ((AppCompatActivity) getActivity()).findViewById(R.id.nav_edit_button);
        //Get the + button for adding a new pill
        btn_add = (ImageButton)  mainview.findViewById(R.id.add_inventory_pill);


        //Click listener on the edit in the navbar
        EditNavClickListener(btn_edit);
        //Click listener on the plus at the end of the page.
        AddPillListener(btn_add);

        return mainview;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
       // if (context instanceof OnFragmentInteractionListener) {
         //   mListener = (OnFragmentInteractionListener) context;
        //} else {
          //  throw new RuntimeException(context.toString()
            //        + " must implement OnFragmentInteractionListener");
        //}
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction(Uri uri);
    }

    //Function that listens to the plus button
    public void AddPillListener(ImageView button)
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.add_pill,null);
                pillhighlight1 = false;
                pillhighlight2 = false;
                // Initialize a new instance of popup window
                mPopupWindow = new PopupWindow(
                        customView,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
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
                CircularPillsPicker.setMaxValue(100);
                CapsulePillsPicker.setMinValue(1);
                CapsulePillsPicker.setMaxValue(100);

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
                mPopupWindow.showAtLocation(mainview, Gravity.CENTER,0,0);

            }
        });
    }

    //Event handler for pressing the DONE button in add_pill menu
    public void DoneAddPillListener (Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!pillhighlight1 && !pillhighlight2) {
                    mPopupWindow.dismiss();
                    return;
                }

                if ((pillhighlight1 && Circular_edit.getText().toString().matches("")) || (pillhighlight2 && Capsule_edit.getText().toString().matches(""))) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please, Set the name of the pill", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    progress = ProgressDialog.show(getActivity(), "Adding your pill",
                            "Please wait...", true);
                    //Call the database to add the new pill
                    InsertPillInventory();
                }

                mPopupWindow.dismiss();
            }
        });
    }
public void AddPillClickListener(ImageButton addbtn)
{
    addbtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            ShowEditLabels();
        }
    });
}
    public void EditNavClickListener(Button edit)
    {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                editclicked++;
               ShowEditLabels();
            }
        });
    }

    //Delete Pill Message.
    public void DeletePillMessage(final int idx)
    {
        new AlertDialog.Builder(getActivity())
                .setTitle("Delete Pill")
                .setMessage("Are you sure you want to delete the pill completely from  your inventory?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Call function of database to delete from database.
                        DeletePillInventory(idx);
                    }
                }).setNegativeButton("No", null).show();

    }

    //Edit Pill Message
    public void EditPillMessage(final int idx)
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Refill");
        alertDialog.setMessage("Enter your refill pills");

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String count  = "";
                        count = input.getText().toString();
                        if (!count.equals("")) {
                            if (count.equals(String.valueOf(0)))
                            {
                                Toast.makeText(getActivity().getApplicationContext(),
                                        "You cannot refill the inventory pill with zero pills", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                            }
                            else
                            {
                                    EditPillInventory(idx,Integer.parseInt(count));
                            }
                        }
                    }
                });
        alertDialog.show();
    }

    public void EditPillListener(TextView tv, final int idx)
    {
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                EditPillMessage(idx);
            }
        });
    }
    public void DeletePillListener(ImageView iv,final int idx)
    {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DeletePillMessage(idx);

            }
        });

    }

    public ImageView GetDeleteImage(int idx)
    {
        LinearLayout pilllayout = PatientInventory.get(idx).GetMainLayout();
        ImageView delete = (ImageView) pilllayout.getChildAt(3);
        return delete;
    }
    public TextView GetEditLabel(int idx)
    {
        LinearLayout pilllayout = PatientInventory.get(idx).GetMainLayout();
        LinearLayout pillinfo = (LinearLayout) pilllayout.getChildAt(1);
        LinearLayout pillcount = (LinearLayout) pillinfo.getChildAt(1);
        TextView edit = (TextView) pillcount.getChildAt(1);
        return edit;
    }

    public void SetPillsLeftText(int idx,int count)
    {
        LinearLayout pilllayout = PatientInventory.get(idx).GetMainLayout();
        LinearLayout pillinfo = (LinearLayout) pilllayout.getChildAt(1);
        LinearLayout pillcount = (LinearLayout) pillinfo.getChildAt(1);
        TextView pillsleft = (TextView) pillcount.getChildAt(0);
        if(count == 1)
            pillsleft.setText(Integer.toString(count) + " pill left");
        else
            pillsleft.setText(Integer.toString(count) + " pills left");
    }

    public void SetPillIndicator(int idx,int count)
    {
        LinearLayout pilllayout = PatientInventory.get(idx).GetMainLayout();
        LinearLayout pillindicator = (LinearLayout) pilllayout.getChildAt(0);

        if (count == 0)
            pillindicator.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        else if (count > 0 && count<=5)
            pillindicator.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        else
            pillindicator.setBackgroundColor(getResources().getColor(R.color.colorAccent));
    }
    public void ShowEditLabels()
    {

        for (int i = 0; i<PatientInventory.size(); i++)
        {
            TextView edit = GetEditLabel(i);
            ImageView delete = GetDeleteImage(i);
            if(editclicked%2 != 0)
            {
                edit.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
            }
            else
            {
                edit.setVisibility(View.GONE);
                delete.setVisibility(View.GONE);
            }
        }
    }


    //Insert the Pill into the patient's inventory.
    public void InsertPillInventory()
    {
        if(pillhighlight1)
        {
            InsertPillName =  Circular_edit.getText().toString();
            InsertPillCount = CircularPillsPicker.getValue();
            InsertPillType = 1;
        }

        else if(pillhighlight2)
        {
            InsertPillName = Capsule_edit.getText().toString();
            InsertPillCount = CapsulePillsPicker.getValue();
            InsertPillType  = 2;
        }

        // Tag used to cancel the request
        String cancel_req_tag = "insertpillinventory";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_EditInventory, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Insert Pill Inventory Response: " + response);
                try
                {
                    progress.dismiss();
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                        //Error is false, pill is inserted successfully.
                        if (!error)
                        {
                            String errorMsg = jObj.getString("error_msg");
                            //No errors happened for this pill, show the toast and maybe do anything you want.
                            Toast.makeText(getActivity().getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();

                            //Insert the new layout to the linearlayout.
                            Pill_Inventory pill = new Pill_Inventory();
                            LinearLayout newpilllayout = CreatePillLayout(InsertPillName,InsertPillCount,InsertPillType);
                            LinearLayout separator = CreateSeparator();
                            Separators.add(separator);
                            mainlayout.addView(newpilllayout);
                            mainlayout.addView(separator);
                            pill.SetPillInventory(InsertPillName,InsertPillType,InsertPillCount,newpilllayout);
                            PatientInventory.add(pill);
                            tv_welcome.setVisibility(View.GONE);

                            int idx = PatientInventory.size()-1;
                            ImageView newdeleteimage = GetDeleteImage(idx);
                            TextView neweditpill = GetEditLabel(idx);
                            if (editclicked % 2 != 0)
                            {
                                newdeleteimage.setVisibility(View.VISIBLE);
                                neweditpill.setVisibility(View.VISIBLE);
                            }

                            //Patient inventory is accessible here
                            for (int i = 0; i<PatientInventory.size();i++)
                            {
                                ImageView deletepill = GetDeleteImage(i);
                                TextView editpill = GetEditLabel(i);
                                DeletePillListener(deletepill,i);
                                EditPillListener(editpill,i);
                            }
                        }
                        else
                        {
                            String errorMsg = jObj.getString("error_msg");

                            Toast.makeText(getActivity().getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
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
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                //Parameters for the patient
                params.put("patientid",PatientID);
                params.put("pillname",InsertPillName);
                params.put("pilltype",String.valueOf(InsertPillType));
                params.put("pillcount",String.valueOf(InsertPillCount));
                return params;
            }

        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }
    public void GetPatientInventory()
    {
        // Tag used to cancel the request
        String cancel_req_tag = "getpatientinventory";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_GetInventory, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                Log.d(TAG, "Get Inventory Response: " + response);
                try
                {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error)
                    {
                        JSONArray result = jObj.getJSONArray(Config.JSON_ARRAY);
                        //CREATE THE LINEAR LAYOUT FOR THE ARRAY RETURNED of Pills.
                        String Pillname = "";
                        String Pillcount = "";
                        String Pilltype= "";

                        //Loop on all pills and put them in currentpills array as well as the layout.
                        for( int i = 0; i < result.length();i++)
                        {
                            Pill_Inventory pill = new Pill_Inventory();
                            JSONObject PillData = result.getJSONObject(i);
                            Pillname = PillData.getString(Config.Key_InventoryPillName);
                            Pillcount = PillData.getString(Config.Key_InventoryPillCount);
                            Pilltype = PillData.getString(Config.Key_InventoryPillType);
                            LinearLayout newpilllayout = CreatePillLayout(Pillname,Integer.parseInt(Pillcount),Integer.parseInt(Pilltype));
                            LinearLayout separator = CreateSeparator();
                            Separators.add(separator);
                            mainlayout.addView(newpilllayout);
                            mainlayout.addView(separator);
                            pill.SetPillInventory(Pillname,Integer.parseInt(Pilltype),Integer.parseInt(Pillcount),newpilllayout);
                            PatientInventory.add(pill);
                        }

                        //Patient inventory is accessible here
                        for (int i = 0; i<PatientInventory.size();i++)
                        {
                           ImageView deletepill = GetDeleteImage(i);
                           TextView editpill = GetEditLabel(i);
                            DeletePillListener(deletepill,i);
                            EditPillListener(editpill,i);
                        }
                    }
                    else
                    {
                        //No pills returned, set the visibility of the text to visible to tell the user.
                        tv_welcome.setVisibility(View.VISIBLE);
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
                Log.e(TAG, "Get Inventory Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to get patientinventory url
                Map<String, String> params = new HashMap<String, String>();
                //Parameters for the patient.
                params.put("patientid",PatientID);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);

    }


    public void EditPillInventory(final int idx,final int count)
    {
        //Get the params to be sent to the database.
        final String PillName = PatientInventory.get(idx).GetPillName();

        // Tag used to cancel the request
        String cancel_req_tag = "updatepillinventory";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_EditInventory, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Update Pill count Inventory Response: " + response);
                try
                {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error)
                    {
                        //Update done
                        String errorMsg = jObj.getString("error_msg");
                        //Calculate the new count
                        int finalcount = PatientInventory.get(idx).GetPillCount() + count;
                        SetPillIndicator(idx,finalcount);
                        //Set it to the finalcount
                        PatientInventory.get(idx).SetPillCount(finalcount);
                        //Set the text message to the new updated count.
                        SetPillsLeftText(idx,finalcount);

                        Toast.makeText(getActivity().getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity().getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
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
                Log.e(TAG, "Update Pills count Inventory Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to get slot url
                Map<String, String> params = new HashMap<String, String>();
                //Parameters for the slot of a given product.
                params.put("patientid",PatientID);
                params.put("pillname",PillName);
                params.put("pillcount",String.valueOf(count));
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);

    }
    public void DeletePillInventory(final int idx)
    {

        final String PillName = PatientInventory.get(idx).GetPillName();

        // Tag used to cancel the request
        String cancel_req_tag = "Delete Inventory Pill";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_EditInventory, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Delete Inventory Pill Response: " + response);
                try
                {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error)
                    {
                        deletecheck++;
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity().getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                        //Remove from the view.
                        mainlayout.removeView(PatientInventory.get(idx).GetMainLayout());
                        mainlayout.removeView(Separators.get(idx));
                        if (deletecheck == PatientInventory.size())
                        {
                            tv_welcome.setVisibility(View.VISIBLE);

                        }
                    }
                    else
                    {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getActivity().getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
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
                Log.e(TAG, "Delete Inventory Pill Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to get slot url
                Map<String, String> params = new HashMap<String, String>();
                //Parameters for the slot of a given product.
                params.put("patientid",PatientID);
                params.put("pillname",PillName);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);

    }

    public  LinearLayout CreatePillLayout(String PillName, int PillCount, int PillType)
    {
        //main layout for each pill
        LinearLayout PillLayout = new LinearLayout(getActivity());
        PillLayout.setOrientation(LinearLayout.HORIZONTAL);

        //Children of the pill layout2
        LinearLayout pillIndicator = new LinearLayout(getActivity());
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(46, 300);
        layoutParams1.setMargins(5, 10, 20, 10);
        //layoutParams1.weight = 1;
        pillIndicator.setLayoutParams(layoutParams1);

        //Check for the remaining pills.
        //0 set background to red.
        //1-5 set background to orange.
        //>5 set it to green
        if (PillCount == 0)
        pillIndicator.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
        else if (PillCount > 0 && PillCount<=5)
        pillIndicator.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        else
        pillIndicator.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        pillIndicator.setPadding(0,0,0,0);


        //Layout for pill Description
        LinearLayout pillInfo = new LinearLayout(getActivity());
        pillInfo.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT);
        layoutParams2.setMargins(30, 10, 20, 10);
        layoutParams2.weight = 6;
        pillInfo.setLayoutParams(layoutParams2);
        //TextView for pillinfo
        TextView tv_pillname = new TextView(getActivity());
        TextView tv_pillcount = new TextView(getActivity());
        TextView edit_button  = new TextView(getActivity());
        //Setting the info for the pill name.
        tv_pillname.setText(PillName);
        tv_pillname.setTextColor(getResources().getColor(R.color.welcomescreen));
        tv_pillname.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        //Create a layout for the text of pills left along with edit button
        LinearLayout pilldescription = new LinearLayout(getActivity());
        LinearLayout.LayoutParams pilldescription_params = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT);
        pilldescription.setOrientation(LinearLayout.HORIZONTAL);
        pilldescription_params.setMargins(20,0,0,70);
        pilldescription.setPadding(0,0,0,0);
        pilldescription.setLayoutParams(pilldescription_params);
        //Text for viewing the pills left.
        if(PillCount == 1)
            tv_pillcount.setText(Integer.toString(PillCount) + " pill left");
        else
            tv_pillcount.setText(Integer.toString(PillCount) + " pills left");
        tv_pillcount.setTypeface(null,Typeface.ITALIC);
        tv_pillcount.setTextColor(getResources().getColor(R.color.greycolor));
        tv_pillcount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

        //Button for edit.
        edit_button.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        edit_button.setText("edit");
        edit_button.setAllCaps(false);
        edit_button.setPaintFlags(edit_button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        edit_button.setTextColor(getResources().getColor(R.color.welcomescreen));
        edit_button.setTypeface(null,Typeface.ITALIC);
        edit_button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        edit_button.setPadding(20,0,20,0);
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
        layoutParams4.setMargins(20, 0, 0,0);
        int src1 = R.drawable.x;
        ImageView deletebtn = new ImageView(getActivity());
        deletebtn.setImageResource(src1);
        deletebtn.setColorFilter(getResources().getColor(R.color.welcomescreen));
        deletebtn.setPadding(0,0,0,0);
        deletebtn.setVisibility(View.GONE);

        PillLayout.addView(pillIndicator);
        PillLayout.addView(pillInfo);
        PillLayout.addView(iv_pilltype);
        PillLayout.addView(deletebtn,layoutParams4);
        return PillLayout;
    }

    public LinearLayout CreateSeparator()
    {
        LinearLayout separator = new LinearLayout(getActivity());
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, 2);
        layoutParams1.setMargins(20, 20, 20, 20);
        separator.setLayoutParams(layoutParams1);
        separator.setBackgroundColor(getResources().getColor(R.color.SeparatorColor));
        return separator;

    }

    public ImageView CreatePillView(int PillType)
    {
        ImageView iv = new ImageView(getActivity());
        int src = 0;
        if(PillType == 1)
        {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(90,90);
            src = R.drawable.circular_pill;
            layoutParams.weight = 1;
            iv.setImageResource(src);
            iv.setColorFilter(getResources().getColor(R.color.welcomescreen));
            layoutParams.setMargins(0, 60, 0, 0);
            iv.setPadding(0,0,0,0);
            iv.setLayoutParams(layoutParams);
        }
       else if(PillType == 2)
        {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100,100);
            src = R.drawable.capsule_pill;
            layoutParams.weight = 1;
            iv.setImageResource(src);
            iv.setColorFilter(getResources().getColor(R.color.welcomescreen));
            layoutParams.setMargins(0, 60, 0, 0);
            iv.setPadding(0,0,0,0);
            iv.setLayoutParams(layoutParams);

        }
        return iv;

    }


//Popup window functions
//Function that listens for the selected ImageView whether the circular pill or capsule pill.
public void PillImageListener(final ImageView iv,final EditText et,final RelativeLayout mylayout, final int pillno,final ImageView other_iv, final EditText other_et, final RelativeLayout otherlayout) {

    iv.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {

            if (pillno == 1) {
                if (pillhighlight1) //remove highlight
                {
                    pillhighlight1 = false;
                    PillType = 0;
                    iv.setColorFilter(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.welcomescreen));
                    et.setText("");
                    et.setVisibility(View.GONE);
                    mylayout.setVisibility(View.GONE);
                } else  //add hilight to pill1 and remove from pill2
                {
                    pillhighlight1 = true;
                    PillType = 1;
                    iv.setColorFilter(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorAccent));
                    et.setVisibility(View.VISIBLE);
                    mylayout.setVisibility(View.VISIBLE);
                    pillhighlight2 = false;
                    //disable other pill
                    other_iv.setColorFilter(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.welcomescreen));
                    other_et.setText("");
                    other_et.setVisibility(View.GONE);
                    otherlayout.setVisibility(View.GONE);
                }
            }

            if (pillno == 2) {
                if (pillhighlight2) //remove highlight
                {
                    pillhighlight2 = false;
                    PillType = 0;
                    iv.setColorFilter(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.welcomescreen));
                    et.setText("");
                    et.setVisibility(View.GONE);
                    mylayout.setVisibility(View.GONE);
                } else //add hilight to pill1 and remove from pill2
                {
                    pillhighlight2 = true;
                    PillType = 2;
                    iv.setColorFilter(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorAccent));
                    et.setVisibility(View.VISIBLE);
                    mylayout.setVisibility(View.VISIBLE);
                    pillhighlight1 = false;
                    //remove highlight of other view
                    other_iv.setColorFilter(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.welcomescreen));
                    other_et.setText("");
                    other_et.setVisibility(View.GONE);
                    otherlayout.setVisibility(View.GONE);
                }
            }
        }
    });
}

}