package com.example.omarelaimy.iseeyou;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.omarelaimy.iseeyou.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;


public class ChooseProfile extends FragmentActivity {
    private String Caregiver_email = "";
    private String Caregiver_name = "";
    private String Caregiver_ID = "";
    private static final String CHOOSE_PROFILE_URL =  "https://icu.000webhostapp.com/chooseprofile.php";
    private static final String TAG = "ChooseProfileActivity";
    public final static int LOOPS = 1;
    public Button CreateProfileBtn;
    public static int FIRST_PAGE; // = count * LOOPS / 2;
    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.7f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
    public MyPagerAdapter adapter;
    public ViewPager pager;
    /*Array of Patients (Patient Class)*/
    public ArrayList<Patient> CaregiverPatients = new ArrayList<>();
    /*** variables for the View */
    public int coverUrl[];
    public static int count;

    public static ChooseProfile ChooseProfileCtx;

    public static int currentPage = 0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Get email passed by signin activity
        Bundle extras   = getIntent().getExtras();
        Caregiver_name  = extras.getString("caregiver_name");
        Caregiver_email = extras.getString("caregiver_email");
        Caregiver_ID    = extras.getString("caregiver_id");
        super.onCreate(savedInstanceState);

        //Call the function to get the patients' registered to a certain caregiver.
        getCaregiverPatients(new CallBack(){
            @Override
            public void onSuccess(ArrayList<Patient> CaregiverPatients) {

                coverUrl = new int[CaregiverPatients.size()];
                for (int i = 0 ; i < coverUrl.length;i++)
                {
                    if (CaregiverPatients.get(i).GetGender() == "0")
                        coverUrl[i] = R.drawable.male_profile;
                    else
                        coverUrl[i] = R.drawable.female_profile;
                }
                ChooseProfileCtx = ChooseProfile.this;
                pager = (ViewPager) findViewById(R.id.myviewpager);
                count = coverUrl.length;
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int pageMargin = 0;
                pageMargin = (int) ((metrics.widthPixels / 4) *2);
                pager.setPageMargin(-pageMargin);
                try {
                    adapter = new MyPagerAdapter(ChooseProfile.this, ChooseProfile.this.getSupportFragmentManager());
                    pager.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    FIRST_PAGE = count * LOOPS / 2;

                    pager.setOnPageChangeListener((ViewPager.OnPageChangeListener) adapter);
                    // Set current item to the middle page so we can fling to both
                    // directions left and right
                    pager.setCurrentItem(FIRST_PAGE); // FIRST_PAGE
                    // pager.setFocusableInTouchMode(true);
                    pager.setOffscreenPageLimit(3);
                    // Set margin for pages as a negative number, so a part of next and
                    // previous pages will be showed


                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String msg) {
                // Do Stuff
            }
        },Caregiver_ID);

        //getCaregiverPatients(Caregiver_ID);

        //Set the view after loading.
        setContentView(R.layout.viewpager);


        CreateProfileBtn = (Button) findViewById(R.id.create_profile);
        //coverUrl = new int[] { R.drawable.male_profile, R.drawable.male_profile,
          //      R.drawable.male_profile, R.drawable.female_profile};



        //If Create Profile button is pressed, go to Create Profile page
        CreateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseProfile.this, CreateProfile.class);
                //Send parameters to the CreateProfile Activity
                Bundle extras = new Bundle();
                extras.putString("caregiver_name",Caregiver_name);
                extras.putString("caregiver_email",Caregiver_email);
                extras.putString("caregiver_id", Caregiver_ID);
                intent.putExtras(extras);

                startActivity(intent);
                finish();

            }
        });



    }

    public interface CallBack {
        void onSuccess(ArrayList<Patient> CaregiverPatients);

        void onFail(String msg);
    }

    //Function for converting the Base64format string to bitmap image
    public Bitmap getImageBitmap(String EncodedString)
    {
        try{
            byte [] encodeByte= Base64.decode(EncodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public void getCaregiverPatients(final CallBack onCallBack, String CaregiverID)
    {
        // Tag used to cancel the request
        String cancel_req_tag = "CaregiverPatients";
        StringRequest strReq = new StringRequest(Request.Method.POST, CHOOSE_PROFILE_URL, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Choose Profile Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error)
                    {
                        String Name   = "";
                        String Gender = "";
                        String Image  = "";
                        String Relation  = "";

                        JSONArray result = jObj.getJSONArray(Config.JSON_ARRAY);
                        int length = result.length();
                            //Loop on the array of patients. response from the server and save in the Patients Array.
                            for( int i = 0; i < result.length();i++)
                            {
                                Patient patient = new Patient();
                                JSONObject PatientData = result.getJSONObject(i);
                                Name = PatientData.getString(Config.KEY_NAME);
                                Gender = PatientData.getString(Config.KEY_GENDER);
                                Image  = PatientData.getString(Config.KEY_IMAGE);
                                Relation = PatientData.getString(Config.KEY_RELATION);
                                patient.SetPatientInfo(Name,Gender,Image,Relation);
                                CaregiverPatients.add(patient);
                            }
                            onCallBack.onSuccess(CaregiverPatients);
                        Log.d(TAG, "Caregiver Patients\n" + "Name: " + Name + "\nGender: " + Gender);
                    }
                    else
                    {
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    onCallBack.onFail(e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "Choose Profile Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("caregiver_id", Caregiver_ID);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq,cancel_req_tag);
    }
}