package com.example.omarelaimy.iseeyou.Fragments;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.GetChars;
import android.text.Layout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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

        for(int i = 0; i < 5; i++)
        {
            String name = "Pill Name " +Integer.toString(i);
            LinearLayout pillLayout = CreatePillLayout(name,56,(i%2 +1));
            LinearLayout separator = CreateSeparator();
            mainlayout.addView(pillLayout);
            mainlayout.addView(separator);
        }


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

    public  LinearLayout CreatePillLayout(String PillName, int PillCount, int PillType)
    {
        //main layout for each pill
        LinearLayout PillLayout = new LinearLayout(getActivity());
        PillLayout.setOrientation(LinearLayout.HORIZONTAL);


        //Children of the pill layout
        LinearLayout pillIndicator = new LinearLayout(getActivity());
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(45, 250);
        layoutParams1.setMargins(5, 10, 20, 10);
        layoutParams1.weight = 1;
        pillIndicator.setLayoutParams(layoutParams1);
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
        //Setting the info for the pill name.
        tv_pillname.setText(PillName);
        tv_pillname.setTextColor(getResources().getColor(R.color.welcomescreen));
        //tv_pillname.setTypeface(null, Typeface.BOLD);
        tv_pillname.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        tv_pillcount.setText(Integer.toString(PillCount) + " pills left");
        tv_pillcount.setTypeface(null,Typeface.ITALIC);
        tv_pillcount.setTextColor(getResources().getColor(R.color.greycolor));
        tv_pillcount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        pillInfo.addView(tv_pillname);
        pillInfo.addView(tv_pillcount);


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
        separator.setBackgroundColor(getResources().getColor(R.color.greycolor));
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
}
