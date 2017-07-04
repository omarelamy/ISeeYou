package com.example.omarelaimy.iseeyou.Fragments;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.omarelaimy.iseeyou.Patient;
import com.example.omarelaimy.iseeyou.R;

import org.w3c.dom.Text;


public class InventoryFragment extends Fragment {
    private static final String patientparameter = "patientid";

    private String PatientID;


    private OnFragmentInteractionListener mListener;

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
        View  mainview  =  inflater.inflate(R.layout.fragment_inventory, container, false);
        LinearLayout mainlayout = (LinearLayout) mainview.findViewById(R.id.mainview_inventory);

        LinearLayout pillLayout = CreatePillLayout();

        mainlayout.addView(pillLayout);

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

    public  LinearLayout CreatePillLayout()
    {
        //main layout for each pill
        LinearLayout PillLayout = new LinearLayout(getActivity());
        PillLayout.setOrientation(LinearLayout.HORIZONTAL);

        //Children of the pill layout
        LinearLayout pillIndicator = new LinearLayout(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(45, 250);
        layoutParams.setMargins(20, 10, 20, 10);
        pillIndicator.setLayoutParams(layoutParams);
        pillIndicator.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        pillIndicator.setPadding(0,0,0,0);

        //Layout for pill Description
        LinearLayout pillInfo = new LinearLayout(getActivity());
        pillInfo.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT);
        layoutParams2.setMargins(80, 10, 20, 10);
        pillInfo.setLayoutParams(layoutParams2);
        //TextView for pillinfo
        TextView tv_pillname = new TextView(getActivity());
        TextView tv_pillcount = new TextView(getActivity());
        //Setting the info for the pill name.
        tv_pillname.setText("Pill Name 1");
        tv_pillname.setTextColor(getResources().getColor(R.color.welcomescreen));
        //tv_pillname.setTypeface(null, Typeface.BOLD);
        tv_pillname.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        tv_pillcount.setText("56 pills left");
        tv_pillcount.setTypeface(null,Typeface.ITALIC);
        tv_pillcount.setTextColor(getResources().getColor(R.color.greycolor));
        tv_pillcount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        pillInfo.addView(tv_pillname);
        pillInfo.addView(tv_pillcount);

        //ImageView for the icon
        int src = R.drawable.circular_pill;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, 80);
        ImageView iv_pilltype = new ImageView(getActivity());
        iv_pilltype.setImageResource(src);
        iv_pilltype.setColorFilter(getResources().getColor(R.color.welcomescreen));
        params.setMargins(100, 0, 0, 0);
        iv_pilltype.setPadding(0,0,0,0);
        iv_pilltype.setLayoutParams(params);

        PillLayout.addView(pillIndicator);
        PillLayout.addView(pillInfo);
        PillLayout.addView(iv_pilltype);
        return PillLayout;
    }
}
