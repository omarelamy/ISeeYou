package com.example.omarelaimy.iseeyou;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreateProfile extends Activity {

    private static final String TAG = "CreateProfileActivity";
    private static final String URL_FOR_CreateProfile = "https://icu.000webhostapp.com/create_profile.php";
    private NumberPicker AgePicker;
    private Spinner GenderSpinner;
    private ImageView ProfilePhoto;
    private View PatientSeparator,RelationSeparator,PhoneSeparator,ProductSeparator,DiseaseSeparator,AddressSeparator;
    private EditText PatientName,Relation,PhoneNumber,Address,ProductID,Diseases;
    private ImageButton btnClose;
    private Button btnCreateProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_profile);

        // Get All items needed from the layout
        GenderSpinner = (Spinner) findViewById(R.id.gender_spinner);
        AgePicker = (NumberPicker) findViewById(R.id.age_picker);
        ProfilePhoto = (ImageView) findViewById(R.id.profile_photo);

        //Getting the Separators
        PatientSeparator = (View) findViewById(R.id.patient_name_separator);
        RelationSeparator = (View) findViewById(R.id.relation_separator);
        PhoneSeparator = (View) findViewById(R.id.phone_number_separator);
        AddressSeparator = (View) findViewById(R.id.address_separator);
        ProductSeparator = (View) findViewById(R.id.productid_separator);
        DiseaseSeparator = (View) findViewById(R.id.disease_separator);


        //Getting the EditTexts
        PatientName = (EditText) findViewById(R.id.patient_name);
        Relation = (EditText) findViewById(R.id.relation);
        PhoneNumber = (EditText) findViewById(R.id.phone_number);
        Address = (EditText) findViewById(R.id.address);
        ProductID = (EditText) findViewById(R.id.product_id);
        Diseases = (EditText) findViewById(R.id.diseases);

        //Getting the Close Button
        btnClose = (ImageButton) findViewById(R.id.cancel_button);

        //Getting the Create Profile button
        btnCreateProfile = (Button) findViewById(R.id.create_profile2);

        //Change the color of the view when focused..
        ChangeSeparatorStatus(PatientName,PatientSeparator);
        ChangeSeparatorStatus(Relation,RelationSeparator);
        ChangeSeparatorStatus(PhoneNumber,PhoneSeparator);
        ChangeSeparatorStatus(Address,AddressSeparator);
        ChangeSeparatorStatus(ProductID,ProductSeparator);
        ChangeSeparatorStatus(Diseases,DiseaseSeparator);



        // Initializing a String Array for the Gender
        String[] Gender = new String[]{
                "Gender",
                "Male",
                "Female",
        };

        //clicking on the close button go to chooseprofile page

      btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateProfile.this, ChooseProfile.class));
                finish();
            }
        });

      btnCreateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Get the gender selection.
                String Gender = GenderSpinner.getSelectedItem().toString();
                //Get the Age picker value
                String Age  = "" + AgePicker.getValue();
                //Place the diseases separated by commas in an arraylist.
                List<String> DiseasesArray = Arrays.asList(Diseases.getText().toString().split(","));
                //Call CreateProfile Function to make the http request.
                CreateProfile(PatientName.getText().toString(),Relation.getText().toString(),PhoneNumber.getText().toString(),Address.getText().toString(),Gender,Age,ProductID.getText().toString(),DiseasesArray);
            }
        });

        final List<String> genderList = new ArrayList<>(Arrays.asList(Gender));

        // Initializing an ArrayAdapter with the spinner_item we created..
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,R.layout.spinner_item,genderList) {

            //Make sure "Gender" Option isn't selected
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position,convertView,parent);
                TextView text = (TextView) view;
                if(position == 0)
                {
                    // Set the hint text color
                    text.setTextColor(getResources().getColor(R.color.hintColor));
                }
                else
                {
                    text.setTextColor(Color.BLACK);
                }
                return view;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0)
                {
                    // Set the hint text color
                    tv.setTextColor(getResources().getColor(R.color.hintColor));
                }
                else
                {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        GenderSpinner.setAdapter(spinnerArrayAdapter);

        GenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if(position > 0 && selectedItemText == "Male")
                {
                    ProfilePhoto.setImageDrawable(getResources().getDrawable(R.drawable.male_profile));
                }
                else if(position > 0 && selectedItemText == "Female")
                {
                    ProfilePhoto.setImageDrawable(getResources().getDrawable(R.drawable.female_profile));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Get the widgets reference from XML layout
        AgePicker.setMaxValue(120);
        AgePicker.setMinValue(0);
        //Gets whether the selector wheel wraps when reaching the min/max value.
        AgePicker.setWrapSelectorWheel(true);

        //Set a value change listener for NumberPicker
        AgePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal)
            {

                //Age = "" + newVal;
                //Display the newly selected number from picker
            }
        });



    }

    //Function for making the http request to the server with the inputs on the android application
    private void CreateProfile (final String Patientname, final String Relation, final String Phonenumber,final String Address, final String Gender, final String Age, final String ProductID, final List<String> Diseases)
    {
        String Diseasescount = Integer.toString(Diseases.size());
        // Tag used to cancel the request
        String cancel_req_tag = "createprofile";

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_CreateProfile, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "CreateProfile Response: " + response);

                try
                {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error)
                    {
                       // String user = jObj.getJSONObject("user").getString("name");
                        Toast.makeText(getApplicationContext(), "Profile successfully Added!", Toast.LENGTH_SHORT).show();
                        // Launch ChooseProfile activity
                          Intent intent = new Intent(CreateProfile.this, ChooseProfile.class);
                          startActivity(intent);
                          finish();
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
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Create Profile Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                  params.put("patientname", Patientname);
                  params.put("relation", Relation);
                  params.put("phonenumber", Phonenumber);
                  params.put("address",Address);
                  params.put("gender", Gender);
                  params.put("age", Age);
                  params.put("productid", ProductID);
                 /* params.put("diseasescount", Integer.toString(Diseases.size()));
                  for (int i= 0; i<Diseases.size();i++)
                  {
                    params.put("disease"+i,Diseases.get(i));
                  }
                  */
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }
    private void ChangeSeparatorStatus(final EditText editText, final View view)
    {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    view.setBackgroundColor(getResources().getColor(R.color.SeparatorFocused));
                }
                else
                {
                    view.setBackgroundColor(getResources().getColor(R.color.SeparatorColor));
                }
            }
        });
    }


}