
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HeartRateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HeartRateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeartRateFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String patientparameter = "patientid";
    private static final String HEART_RATE_URL =  "https://icu.000webhostapp.com/heartrate.php";
    private static final String TAG = "HeartRateFragment";


    //private static final String ARG_PARAM2 = "param2";
    private SwipeRefreshLayout swipeLayout;
    private String PatientID;
    //ana 7asa en el static final eli fo2 dh mlosh lazma

    private OnFragmentInteractionListener mListener;

    public HeartRateFragment() {
        // Required empty public constructor
    }




    public static HeartRateFragment newInstance(String patientid) {
        HeartRateFragment fragment = new HeartRateFragment();
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
        View  view  = inflater.inflate(R.layout.fragment_heart_rate, container, false);

        //maynf3sh lazem fel oncreateview 3shan homa dool el gowael fragment_heart_rate
        //Get the Textviews for the heart rate and the average heart rate
        TextView PatientCurrentHeartRate  = (TextView) view.findViewById(R.id.current_heart_rate);
        PatientCurrentHeartRate.setText("kkdkd");
        TextView PatientAverageHeartRate  = (TextView) view.findViewById(R.id.average_heart_rate);
        //((TextView) getView().findViewById(R.id.current_heart_rate)).setText("90");
        //Get the swipetorefresh layout.
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_orange_dark));

        getCurrentHeartRate(PatientID,PatientCurrentHeartRate,PatientAverageHeartRate);

        return inflater.inflate(R.layout.fragment_heart_rate, container, false);
        //ahh wait ana mgm3a estnaa :D
    }

    @Override
    public void onRefresh()
    {
        //TODO call the function for retrieving the php results.
        //new myTask().execute();
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

    public void getCurrentHeartRate(String patientid, final TextView currentheartrate, TextView averageheartrate)
    {
        // Tag used to cancel the request
        String cancel_req_tag = "PatientHeartrate";
        StringRequest strReq = new StringRequest(Request.Method.POST, HEART_RATE_URL, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Current Heart Rate Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error)
                    {
                        //Get the timestamp and the reading of the patient.
                        String TimeStamp   = jObj.getJSONObject("result").getString("heart_timestamp");
                        String Reading = jObj.getJSONObject("result").getString("current_heart_rate");
                        currentheartrate.setText(Reading);
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
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "Choose Profile Error: " + error.getMessage());
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
