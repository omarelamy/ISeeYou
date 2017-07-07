package com.example.omarelaimy.iseeyou.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.omarelaimy.iseeyou.ChooseProfile;
import com.example.omarelaimy.iseeyou.CreateProfile;
import com.example.omarelaimy.iseeyou.EditSlot;
import com.example.omarelaimy.iseeyou.R;
import com.example.omarelaimy.iseeyou.SignIn;


public class PillboxFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String Patientid = "patientid";
    private static final String Productid = "productid";

    private String PatientID;
    private String ProductID;
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



        btn_edit = (Button) ((AppCompatActivity) getActivity()).findViewById(R.id.nav_edit_button);


        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Pillbox Fragment", Toast.LENGTH_SHORT).show();

            }
        });


        //TODO: Implement the functionality of slots on click listeners.
       return view;
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
       /* if (context instanceof OnFragmentInteractionListener) {
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
        void onFragmentInteraction(Uri uri);
    }
}
