package com.example.omarelaimy.iseeyou.Fragments;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.omarelaimy.iseeyou.AppSingleton;
import com.example.omarelaimy.iseeyou.Config;
import com.example.omarelaimy.iseeyou.EditSlot;
import com.example.omarelaimy.iseeyou.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class PillboxFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String Patientid = "patientid";
    private static final String Productid = "productid";
    private static final String URL_FOR_GETBOX_MONITOR = "https://icu.000webhostapp.com/getslotdates.php";
    private static final String TAG = "PillBoxActivity";
    private String PatientID;
    private String ProductID;
    private String MinDate;
    private ProgressDialog progress;
    private SwipeRefreshLayout swipeLayout;
    private ImageView SatMor,SatAft,SatEve,SunMor,SunAft,SunEve,MonMor,MonAft,MonEve,TueMor,TueAft,TueEve;
    private ImageView WedMor,WedAft,WedEve,ThuMor,ThuAft,ThuEve,FriMor,FriAft,FriEve;
    private Button btn_edit;
    private OnFragmentInteractionListener mListener;

    public PillboxFragment() {
        // Required empty public constructor
    }


    public static PillboxFragment newPillBoxInstance(String patientid, String productid) {
        PillboxFragment fragment = new PillboxFragment();
        Bundle args = new Bundle();
        args.putString(Patientid, patientid);
        args.putString(Productid, productid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            PatientID = getArguments().getString(Patientid);
            ProductID = getArguments().getString(Productid);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pillbox, container, false);
        //Get all the Imageviews for the slots.
        SatMor = (ImageView) view.findViewById(R.id.sat_morning_slot);
        SatAft = (ImageView) view.findViewById(R.id.sat_afternoon_slot);
        SatEve = (ImageView) view.findViewById(R.id.sat_evening_slot);
        SunMor = (ImageView) view.findViewById(R.id.sun_morning_slot);
        SunAft = (ImageView) view.findViewById(R.id.sun_afternoon_slot);
        SunEve = (ImageView) view.findViewById(R.id.sun_evening_slot);
        MonMor = (ImageView) view.findViewById(R.id.mon_morning_slot);
        MonAft = (ImageView) view.findViewById(R.id.mon_afternoon_slot);
        MonEve = (ImageView) view.findViewById(R.id.mon_evening_slot);
        TueMor = (ImageView) view.findViewById(R.id.tue_morning_slot);
        TueAft = (ImageView) view.findViewById(R.id.tue_afternoon_slot);
        TueEve = (ImageView) view.findViewById(R.id.tue_evening_slot);
        WedMor = (ImageView) view.findViewById(R.id.wed_morning_slot);
        WedAft = (ImageView) view.findViewById(R.id.wed_afternoon_slot);
        WedEve = (ImageView) view.findViewById(R.id.wed_evening_slot);
        ThuMor = (ImageView) view.findViewById(R.id.thu_morning_slot);
        ThuAft = (ImageView) view.findViewById(R.id.thu_afternoon_slot);
        ThuEve = (ImageView) view.findViewById(R.id.thu_evening_slot);
        FriMor = (ImageView) view.findViewById(R.id.fri_morning_slot);
        FriAft = (ImageView) view.findViewById(R.id.fri_afternoon_slot);
        FriEve = (ImageView) view.findViewById(R.id.fri_evening_slot);

        SlotClickListener(SatMor,"Saturday Morning");
        SlotClickListener(SatAft,"Saturday Afternoon");
        SlotClickListener(SatEve,"Saturday Evening");
        SlotClickListener(SunMor,"Sunday Morning");
        SlotClickListener(SunAft,"Sunday Afternoon");
        SlotClickListener(SunEve,"Sunday Evening");
        SlotClickListener(MonMor,"Monday Morning");
        SlotClickListener(MonAft,"Monday Afternoon");
        SlotClickListener(MonEve,"Monday Evening");
        SlotClickListener(TueMor,"Tuesday Morning");
        SlotClickListener(TueAft,"Tuesday Afternoon");
        SlotClickListener(TueEve,"Tuesday Evening");
        SlotClickListener(WedMor,"Wednesday Morning");
        SlotClickListener(WedAft,"Wednesday Afternoon");
        SlotClickListener(WedEve,"Wednesday Evening");
        SlotClickListener(ThuMor,"Thursday Morning");
        SlotClickListener(ThuAft,"Thursday Afternoon");
        SlotClickListener(ThuEve,"Thursday Evening");
        SlotClickListener(FriMor,"Friday Morning");
        SlotClickListener(FriAft,"Friday Afternoon");
        SlotClickListener(FriEve,"Friday Evening");
        //Get the edit button.
        btn_edit = (Button) ((AppCompatActivity) getActivity()).findViewById(R.id.nav_edit_button);

        //Click listener on the edit button.
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Pillbox Fragment", Toast.LENGTH_SHORT).show();

            }
        });

        progress = ProgressDialog.show(getActivity(), "Syncing your pillbox",
                "Please wait...", true);


        //Get the swipetorefresh layout.
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_pillbox_refresh_layout);
        swipeLayout.setOnRefreshListener(this);

        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_orange_dark));




        //Get the minimum date from the current day we're in.
        String mindate = GetMinDate();
        MinDate =  mindate;
        //Call the function of the db to get the slots from the min date till now.
        GetSlotsBeforeDate(mindate);
       return view;
    }

    @Override
    public void onRefresh()
    {
        //Setting the refresh layout to true.
        swipeLayout.setRefreshing(true);
        //Call the function of the db to get the slots from the min date till now.
        GetSlotsBeforeDate(MinDate);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                {
                    getActivity().finish();
                }
                return false;
            }
        });
    }


    public void SlotClickListener(ImageView iv, final String SlotName)
    {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] SlotNameSplitted = SlotName.split("\\s+");
                Intent intent = new Intent(getActivity(), EditSlot.class);
                //Send parameters to the EditSlot Activity
                Bundle extras = new Bundle();
                //Send the patient id and the product id.
                extras.putString("patientid",PatientID);
                extras.putString("productid",ProductID);
                extras.putString("SlotDay",SlotNameSplitted[0]);
                extras.putString("SlotDayTime",SlotNameSplitted[1]);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void GetSlotsBeforeDate(final String mindate)
    {
        // Tag used to cancel the request
        String cancel_req_tag = "getdatesbeforeslot";
        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_GETBOX_MONITOR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Get Dates Slots Response: " + response);
                try
                {
                    swipeLayout.setRefreshing(false);
                    progress.dismiss();
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error)
                    {
                        JSONArray result = jObj.getJSONArray(Config.JSON_ARRAY);

                        //Loop on all pills and put them in currentpills array as well as the layout.
                        for( int i = 0; i < result.length();i++)
                        {
                            String SlotID = "";
                            String Taken  = "";
                            String Timeout= "";
                            JSONObject SlotData = result.getJSONObject(i);
                            SlotID = SlotData.getString(Config.KEY_SLOTID);
                            Taken =  SlotData.getString(Config.KEY_TAKEN);
                            Timeout = SlotData.getString(Config.KEY_TIMEOUT);

                            ChangeSlotStatus(SlotID,Taken,Timeout);
                        }
                    }
                    else
                    {
                        //Error occured.
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
                Log.e(TAG, "Get Slots dates Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to get patientinventory url
                Map<String, String> params = new HashMap<String, String>();
                //Parameters for the patient.
                params.put("productid",ProductID);
                params.put("minimumdate",mindate);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }



    //Function that gets the min date from the current day we're in.
    public String GetMinDate()
    {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String mindate = "";
        switch (day)
        {
            case Calendar.SATURDAY:
                SimpleDateFormat saturday = new SimpleDateFormat("yyyy-MM-dd"); // you can specify your format here...
                mindate = saturday.format(new Date(calendar.getTimeInMillis()));
                break;

            case Calendar.SUNDAY:
                calendar.add(Calendar.DATE, -1); // I just want date before 90 days. you can give that you want.
                SimpleDateFormat sunday = new SimpleDateFormat("yyyy-MM-dd"); // you can specify your format here...
                mindate = sunday.format(new Date(calendar.getTimeInMillis()));
                break;

            case Calendar.MONDAY:
                calendar.add(Calendar.DATE, -2); // I just want date before 90 days. you can give that you want.
                SimpleDateFormat monday = new SimpleDateFormat("yyyy-MM-dd"); // you can specify your format here...
                mindate = monday.format(new Date(calendar.getTimeInMillis()));
                break;
            // Current day is Monday

            case Calendar.TUESDAY:
                calendar.add(Calendar.DATE, -3); // I just want date before 90 days. you can give that you want.
                SimpleDateFormat tuesday = new SimpleDateFormat("yyyy-MM-dd"); // you can specify your format here...
                mindate = tuesday.format(new Date(calendar.getTimeInMillis()));
                break;

            case Calendar.WEDNESDAY:
                calendar.add(Calendar.DATE, -4); // I just want date before 90 days. you can give that you want.
                SimpleDateFormat wednesday = new SimpleDateFormat("yyyy-MM-dd"); // you can specify your format here...
                mindate = wednesday.format(new Date(calendar.getTimeInMillis()));
                break;

            case Calendar.THURSDAY:
                calendar.add(Calendar.DATE, -5); // I just want date before 90 days. you can give that you want.
                SimpleDateFormat thursday = new SimpleDateFormat("yyyy-MM-dd"); // you can specify your format here...
                mindate = thursday.format(new Date(calendar.getTimeInMillis()));
                break;

            case Calendar.FRIDAY:
                calendar.add(Calendar.DATE, -6);
                SimpleDateFormat friday = new SimpleDateFormat("yyyy-MM-dd"); // you can specify your format here...
                mindate = friday.format(new Date(calendar.getTimeInMillis()));
                break;
        }
        return mindate;
    }
    public void ChangeSlotStatus(String SlotID, String Taken, String Timeout)
    {
        switch (SlotID)
        {
            case "0":
                //Case Slot is taken mark it as greyed out.(Taken)
                if (Taken == "1")
                    SunMor.setImageDrawable(getResources().getDrawable(R.drawable.morninginactive));
                //Case Slot is not taken and timeout occured. Red border on the slot should happend.
                else if (Taken == "0" && Timeout == "1")
                    SunMor.setImageDrawable(getResources().getDrawable(R.drawable.morningactivealert));
                break;

            case "1":
                if (Taken == "1")
                    MonMor.setImageDrawable(getResources().getDrawable(R.drawable.morninginactive));
                    //Case Slot is not taken and timeout occured. Red border on the slot should happend.
                else if (Taken == "0" && Timeout == "1")
                    MonMor.setImageDrawable(getResources().getDrawable(R.drawable.morningactivealert));
                break;
            case "2":
                if (Taken == "1")
                    TueMor.setImageDrawable(getResources().getDrawable(R.drawable.morninginactive));
                else if (Taken == "0" && Timeout == "1")
                    TueMor.setImageDrawable(getResources().getDrawable(R.drawable.morningactivealert));
                break;
            case "3":
                if (Taken == "1")
                    WedMor.setImageDrawable(getResources().getDrawable(R.drawable.morninginactive));
                else if (Taken == "0" && Timeout == "1")
                    WedMor.setImageDrawable(getResources().getDrawable(R.drawable.morningactivealert));
                break;
            case "4":
                if (Taken == "1")
                    ThuMor.setImageDrawable(getResources().getDrawable(R.drawable.morninginactive));
                else if (Taken == "0" && Timeout == "1")
                    ThuMor.setImageDrawable(getResources().getDrawable(R.drawable.morningactivealert));
                break;
            case "5":
                if (Taken == "1")
                    FriMor.setImageDrawable(getResources().getDrawable(R.drawable.morninginactive));
                    //Case Slot is not taken and timeout occured. Red border on the slot should happend.
                else if (Taken == "0" && Timeout == "1")
                    FriMor.setImageDrawable(getResources().getDrawable(R.drawable.morningactivealert));
                break;
            case "6":
                if (Taken == "1")
                    SatMor.setImageDrawable(getResources().getDrawable(R.drawable.morninginactive));
                    //Case Slot is not taken and timeout occured. Red border on the slot should happend.
                else if (Taken == "0" && Timeout == "1")
                    SatMor.setImageDrawable(getResources().getDrawable(R.drawable.morningactivealert));
                break;
            case "7":
                if (Taken == "1")
                    SunAft.setImageDrawable(getResources().getDrawable(R.drawable.afternooninactive));
                    //Case Slot is not taken and timeout occured. Red border on the slot should happend.
                else if (Taken == "0" && Timeout == "1")
                    SunAft.setImageDrawable(getResources().getDrawable(R.drawable.afternoonactivealert));
                break;
            case "8":
                if (Taken == "1")
                    MonAft.setImageDrawable(getResources().getDrawable(R.drawable.afternooninactive));
                    //Case Slot is not taken and timeout occured. Red border on the slot should happend.
                else if (Taken == "0" && Timeout == "1")
                    MonAft.setImageDrawable(getResources().getDrawable(R.drawable.afternoonactivealert));
                break;
            case "9":
                if (Taken == "1")
                    TueAft.setImageDrawable(getResources().getDrawable(R.drawable.afternooninactive));
                    //Case Slot is not taken and timeout occured. Red border on the slot should happend.
                else if (Taken == "0" && Timeout == "1")
                    TueAft.setImageDrawable(getResources().getDrawable(R.drawable.afternoonactivealert));
                break;
            case "10":
                if (Taken == "1")
                    WedAft.setImageDrawable(getResources().getDrawable(R.drawable.afternooninactive));
                    //Case Slot is not taken and timeout occured. Red border on the slot should happend.
                else if (Taken == "0" && Timeout == "1")
                    WedAft.setImageDrawable(getResources().getDrawable(R.drawable.afternoonactivealert));
                break;
            case "11":
                if (Taken == "1")
                    ThuAft.setImageDrawable(getResources().getDrawable(R.drawable.afternooninactive));
                    //Case Slot is not taken and timeout occured. Red border on the slot should happend.
                else if (Taken == "0" && Timeout == "1")
                    ThuAft.setImageDrawable(getResources().getDrawable(R.drawable.afternoonactivealert));
                break;
            case "12":
                if (Taken == "1")
                    FriAft.setImageDrawable(getResources().getDrawable(R.drawable.afternooninactive));
                    //Case Slot is not taken and timeout occured. Red border on the slot should happend.
                else if (Taken == "0" && Timeout == "1")
                    FriAft.setImageDrawable(getResources().getDrawable(R.drawable.afternoonactivealert));
                break;
            case "13":
                if (Taken == "1")
                    SatAft.setImageDrawable(getResources().getDrawable(R.drawable.afternooninactive));
                    //Case Slot is not taken and timeout occured. Red border on the slot should happend.
                else if (Taken == "0" && Timeout == "1")
                    SatAft.setImageDrawable(getResources().getDrawable(R.drawable.afternoonactivealert));
                break;
            case "14":
                if (Taken == "1")
                    SunEve.setImageDrawable(getResources().getDrawable(R.drawable.eveninginactive));
                    //Case Slot is not taken and timeout occured. Red border on the slot should happend.
                else if (Taken == "0" && Timeout == "1")
                    SunEve.setImageDrawable(getResources().getDrawable(R.drawable.eveningactivealert));
                break;
            case "15":
                if (Taken == "1")
                    MonEve.setImageDrawable(getResources().getDrawable(R.drawable.eveninginactive));
                    //Case Slot is not taken and timeout occured. Red border on the slot should happend.
                else if (Taken == "0" && Timeout == "1")
                    MonEve.setImageDrawable(getResources().getDrawable(R.drawable.eveningactivealert));
                break;
            case "16":
                if (Taken == "1")
                    TueEve.setImageDrawable(getResources().getDrawable(R.drawable.eveninginactive));
                    //Case Slot is not taken and timeout occured. Red border on the slot should happend.
                else if (Taken == "0" && Timeout == "1")
                    SunMor.setImageDrawable(getResources().getDrawable(R.drawable.redborder));
                break;
            case "17":
                if (Taken == "1")
                    WedEve.setImageDrawable(getResources().getDrawable(R.drawable.eveninginactive));
                    //Case Slot is not taken and timeout occured. Red border on the slot should happend.
                else if (Taken == "0" && Timeout == "1")
                    WedEve.setImageDrawable(getResources().getDrawable(R.drawable.eveningactivealert));
                break;
            case "18":
                if (Taken == "1")
                    ThuEve.setImageDrawable(getResources().getDrawable(R.drawable.eveninginactive));
                    //Case Slot is not taken and timeout occured. Red border on the slot should happend.
                else if (Taken == "0" && Timeout == "1")
                    ThuEve.setImageDrawable(getResources().getDrawable(R.drawable.eveningactivealert));
                break;
            case "19":
                if (Taken == "1")
                    FriEve.setImageDrawable(getResources().getDrawable(R.drawable.eveninginactive));
                    //Case Slot is not taken and timeout occured. Red border on the slot should happend.
                else if (Taken == "0" && Timeout == "1")
                    FriEve.setImageDrawable(getResources().getDrawable(R.drawable.eveningactivealert));
                break;
            case "20":
                if (Taken == "1")
                    SatEve.setImageDrawable(getResources().getDrawable(R.drawable.eveninginactive));
                    //Case Slot is not taken and timeout occured. Red border on the slot should happend.
                else if (Taken == "0" && Timeout == "1")
                    SatEve.setImageDrawable(getResources().getDrawable(R.drawable.eveningactivealert));
                break;
        }
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
