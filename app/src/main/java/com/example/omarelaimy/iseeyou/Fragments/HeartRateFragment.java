
package com.example.omarelaimy.iseeyou.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.omarelaimy.iseeyou.AppSingleton;
import com.example.omarelaimy.iseeyou.ChooseProfile;
import com.example.omarelaimy.iseeyou.Config;
import com.example.omarelaimy.iseeyou.Patient;
import com.example.omarelaimy.iseeyou.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class HeartRateFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String patientparameter = "patientid";
    private static final String patientnameparameter = "patientname";
    private static final String HEART_RATE_URL =  "https://icu.000webhostapp.com/heartrate.php";
    private static final String TAG = "HeartRateFragment";
    //public Patient patient = new Patient();
    private SwipeRefreshLayout swipeLayout;
    private String PatientID;
    private String Patientname;
    private TextView tv;
    private TextView PatientName;
    private OnFragmentInteractionListener mListener;
    private ProgressDialog progress;

    public HeartRateFragment() {
        // Required empty public constructor
    }

    public static HeartRateFragment newHearRateInstance(String patientid,String patientname) {
        HeartRateFragment fragment = new HeartRateFragment();
        Bundle args = new Bundle();
        args.putString(patientparameter, patientid);
        args.putString(patientnameparameter,patientname);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            PatientID = getArguments().getString("patientid");
            Patientname = getArguments().getString("patientname");
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        if(container == null) {
            return null;
        }
        View  view  = inflater.inflate(R.layout.fragment_heart_rate, container, false);
        tv = (TextView) view.findViewById(R.id.current_heart_rate);
        //Get the swipetorefresh layout.
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_orange_dark));

       swipeLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeLayout.setRefreshing(true);
                                        getCurrentHeartRate();
                                    }
                                }
        );

        return view;

    }

    @Override
    public void onRefresh()
    {
        //Setting the refresh layout to true.
        swipeLayout.setRefreshing(true);
       //Call getting the current heart rate function.
        getCurrentHeartRate();
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void getCurrentHeartRate()
    {
        //progress = ProgressDialog.show(getActivity(), "Getting the patient's heart rate",
          //      "Please wait...", true);
        // Tag used to cancel the request
        String cancel_req_tag = "PatientHeartrate";
        StringRequest strReq = new StringRequest(Request.Method.POST, HEART_RATE_URL, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
               // progress.dismiss();
                Log.d(TAG, "Current Heart Rate Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error)
                    {
                        Patient patient = new Patient();
                        String TimeStamp   = jObj.getJSONObject("result").getString("heart_timestamp");
                        String Reading = jObj.getJSONObject("result").getString("current_heart_rate");
                        patient.SetCurrentHeartRate(Double.parseDouble(Reading),TimeStamp);
                        tv.setText(patient.GetCurrentHeartRate());
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
                swipeLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "Get HeartRate Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("patientid", PatientID);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
    }

}


