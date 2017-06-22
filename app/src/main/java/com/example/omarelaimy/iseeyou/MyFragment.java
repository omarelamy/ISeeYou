package com.example.omarelaimy.iseeyou;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.omarelaimy.iseeyou.R;

public class MyFragment extends Fragment {
    public static Fragment newInstance(ChooseProfile context, int pos, float scale) {
        Bundle b = new Bundle();
        b.putInt("pos", pos);
        b.putFloat("scale", scale);

        return Fragment.instantiate(context, MyFragment.class.getName(), b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(400,400);
        LinearLayout fragmentLL  = (LinearLayout) inflater.inflate(R.layout.choose_profile, container, false);
        int pos   = this.getArguments().getInt("pos");
        TextView tv  = (TextView) fragmentLL.findViewById(R.id.profile_name);

        tv.setText(ChooseProfile.ChooseProfileCtx.CaregiverPatients.get(pos).GetName());

        tv.setTextColor(ContextCompat.getColor(getContext(),R.color.textcolor));
        ImageView iv = (ImageView) fragmentLL.findViewById(R.id.pagerImg);

        iv.setLayoutParams(layoutParams);
        //Check if the user has a photo
        if (!ChooseProfile.ChooseProfileCtx.PatientImageCheck[pos])
            iv.setImageResource(ChooseProfile.ChooseProfileCtx.coverUrl[pos]);
        else
            iv.setImageBitmap(ChooseProfile.ChooseProfileCtx.PatientImage[pos]);
        //iv.setImageResource(ChooseProfile.ChooseProfileCtx.coverUrl[pos]);
        iv.setPadding(15, 15, 15, 15);


        MyLinearLayout root = (MyLinearLayout) fragmentLL.findViewById(R.id.root);
        float scale   = this.getArguments().getFloat("scale");
        root.setScaleBoth(scale);

        return fragmentLL;
    }
}