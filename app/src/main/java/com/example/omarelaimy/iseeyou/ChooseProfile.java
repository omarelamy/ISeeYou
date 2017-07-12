package com.example.omarelaimy.iseeyou;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.omarelaimy.iseeyou.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;


public class ChooseProfile extends FragmentActivity {
    public String Caregiver_email = "";
    public String Caregiver_name = "";
    public String Caregiver_ID = "";
    private static final String CHOOSE_PROFILE_URL =  "https://icu.000webhostapp.com/chooseprofile.php";
    private static final String TAG = "ChooseProfileActivity";
    public final static int LOOPS = 1;
    public Button CreateProfileBtn;
    public ImageView btn_Notifications;
    public static int FIRST_PAGE; // = count * LOOPS / 2;
    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.7f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
    public MyPagerAdapter adapter;
    public ViewPager pager;
    public ProgressDialog progress;
    /*Array of Patients (Patient Class)*/
    public ArrayList<Patient> CaregiverPatients = new ArrayList<>();
   public Bitmap PatientImage[];
    /*** variables for the View */
    public int coverUrl[];
    //save parameters here to send to navigation bar
    public String PatientNames[];
    public String PatientIDs[];
    public String PatientGender[];
    public String PatientProductIDs[];
    public boolean PatientImageCheck[];

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
                PatientImageCheck = new boolean[CaregiverPatients.size()];
                PatientImage = new Bitmap[CaregiverPatients.size()];
                PatientNames = new String[CaregiverPatients.size()];
                PatientIDs = new String[CaregiverPatients.size()];
                PatientProductIDs =  new String[CaregiverPatients.size()];
                //Add PatientProducts
                PatientGender = new String[CaregiverPatients.size()];
                for (int i = 0 ; i < coverUrl.length;i++)
                {
                    //set patient names,ids and gender
                    PatientNames[i] = CaregiverPatients.get(i).GetName();
                    PatientIDs[i] = CaregiverPatients.get(i).GetID();
                    PatientGender[i] = CaregiverPatients.get(i).GetGender();
                    PatientProductIDs[i] = CaregiverPatients.get(i).GetProductID();
                   if (CaregiverPatients.get(i).GetGender() == "0")
                        coverUrl[i] = R.drawable.male_profile;
                   else
                       coverUrl[i] = R.drawable.female_profile;
                    //Check for Image attribute if null or not, make sure if comparison is RIGHT.
                  if (CaregiverPatients.get(i).GetImagePath().isEmpty())
                   {
                        //User didn't enter a custom image, mark that.
                        PatientImageCheck[i] = false;
                   }
                  else
                   {
                       PatientImageCheck[i] = true;
                       String ImagePath = CaregiverPatients.get(i).GetImagePath();
                       PatientImage[i] = getImageBitmap(ImagePath);
                   }
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
                Log.d("PatientInfoRetrieval",msg);
            }
        });


        //Set the view after loading.
        setContentView(R.layout.viewpager);


        CreateProfileBtn = (Button) findViewById(R.id.create_profile);
       btn_Notifications = (ImageView) findViewById(R.id.iv_notifications);



       CheckNotifications(btn_Notifications);
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
    //If notification icon is pressed, go to Notifications Page
    public void CheckNotifications(ImageView iv) {

        iv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ChooseProfile.this, Notifications.class);
                startActivity(intent);
            }
        });
    }



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
    public interface CallBack {
        void onSuccess(ArrayList<Patient> CaregiverPatients);
        void onFail(String msg);
    }

    public void getCaregiverPatients(final CallBack onCallBack)
    {
        progress = ProgressDialog.show(this, "Loading your Patients",
                "Please wait...", true);
        // Tag used to cancel the request
        String cancel_req_tag = "CaregiverPatients";
        StringRequest strReq = new StringRequest(Request.Method.POST, CHOOSE_PROFILE_URL, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                Log.d(TAG, "Choose Profile Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    //Check if there are notifications for today.
                    boolean hasnotifications = jObj.getBoolean("hasnotifications");
                    btn_Notifications.setVisibility(View.VISIBLE);
                    if (hasnotifications)
                    {
                        btn_Notifications.setColorFilter(getResources().getColor(R.color.colorAccent));
                    }

                    if (!error)
                    {
                        String Name   = "";
                        String ID = "";
                        String Gender = "";
                        String ImagePath  = "";
                        String Relation  = "";
                        String ProductID = "";

                        JSONArray result = jObj.getJSONArray(Config.JSON_ARRAY);
                            //Loop on the array of patients. response from the server and save in the Patients Array.
                            for( int i = 0; i < result.length();i++)
                            {
                                Patient patient = new Patient();
                                JSONObject PatientData = result.getJSONObject(i);
                                Name = PatientData.getString(Config.KEY_NAME);
                                ID = PatientData.getString(Config.KEY_ID);
                                Gender = PatientData.getString(Config.KEY_GENDER);
                                ImagePath  = PatientData.getString(Config.KEY_IMAGE);
                                Relation = PatientData.getString(Config.KEY_RELATION);
                                ProductID = PatientData.getString(Config.KEY_PRODUCTID);
                                patient.SetPatientInfo(Name,ID,Gender,ImagePath,Relation,ProductID);
                                CaregiverPatients.add(patient);
                            }
                            onCallBack.onSuccess(CaregiverPatients);
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