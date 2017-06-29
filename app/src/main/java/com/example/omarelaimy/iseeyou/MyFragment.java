package com.example.omarelaimy.iseeyou;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.omarelaimy.iseeyou.R;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

import static com.example.omarelaimy.iseeyou.ChooseProfile.ChooseProfileCtx;

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

        tv.setText(ChooseProfileCtx.CaregiverPatients.get(pos).GetName());

        tv.setTextColor(ContextCompat.getColor(getContext(),R.color.textcolor));
        ImageView iv = (ImageView) fragmentLL.findViewById(R.id.pagerImg);
        final String PatientName = ChooseProfileCtx.PatientNames[pos];
        final String PatientGender = ChooseProfileCtx.PatientGender[pos];
        final String PatientID = ChooseProfileCtx.PatientIDs[pos];
        final String ProductID =  ChooseProfileCtx.PatientProductIDs[pos];
        iv.setLayoutParams(layoutParams);
       // iv.setImageResource(ChooseProfile.ChooseProfileCtx.coverUrl[pos]);
        //Check if the user has a photo
        if (!ChooseProfileCtx.PatientImageCheck[pos])
          iv.setImageResource(ChooseProfileCtx.coverUrl[pos]);
        else
            iv.setImageBitmap(ChooseProfileCtx.PatientImage[pos]);

        iv.setPadding(15, 15, 15, 15);
        final Bitmap bmp =  ((BitmapDrawable)iv.getDrawable()).getBitmap();
        BitmapClass.img = bmp;
        iv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Call the Navigation Bar Activity and send the needed parameters
                //Convert the image to bytearray.
               // ByteArrayOutputStream stream = new ByteArrayOutputStream();
               // bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                //byte[] byteArray = stream.toByteArray();
                Config.img = bmp;
                Intent intent;
                intent = new Intent(ChooseProfileCtx, NavigationMainActivity.class);
                //Send parameters to the CreateProfile Activity
                Bundle extras = new Bundle();
                extras.putString("patient_name",PatientName);
                extras.putString("patient_id",PatientID);
                extras.putString("patient_gender", PatientGender);
                extras.putString("productid",ProductID);
                extras.putString("caregiver_name",ChooseProfileCtx.Caregiver_name);
                extras.putString("caregiver_email",ChooseProfileCtx.Caregiver_email);
                extras.putString("caregiver_id", ChooseProfileCtx.Caregiver_ID);
               // ByteArrayOutputStream stream = new ByteArrayOutputStream();
                //bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                //byte[] byteArray = stream.toByteArray();
                intent.putExtras(extras);
                startActivity(intent);
                ChooseProfileCtx.finish();
            }
        });


        MyLinearLayout root = (MyLinearLayout) fragmentLL.findViewById(R.id.root);
        float scale   = this.getArguments().getFloat("scale");
        root.setScaleBoth(scale);

        return fragmentLL;
    }

}