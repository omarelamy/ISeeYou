package com.example.omarelaimy.iseeyou;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.provider.MediaStore;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreateProfile extends Activity {

    private static final String TAG = "CreateProfileActivity";
    private static final String URL_FOR_CreateProfile = "https://icu.000webhostapp.com/create_profile.php";
    private String Caregiver_name = "";
    private  String Caregiver_email = "";
    private String Caregiver_ID= "";
    private NumberPicker AgePicker;
    private Spinner GenderSpinner;
    private ImageView ProfilePhoto;
    private View PatientSeparator,RelationSeparator,PhoneSeparator,ProductSeparator,DiseaseSeparator,AddressSeparator;
    private EditText PatientName,Relation,PhoneNumber,Address,ProductID,Diseases;
    private ImageButton btnClose;
    private Button btnCreateProfile,btnUpload;
    ProgressDialog progressDialog;
    //Variables for the profile photo
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;

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

        //Getting the button for uploading the photo
        btnUpload = (Button) findViewById(R.id.buttonUpload);

        //Set the Progressdialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        //Change the color of the view when focused..
        ChangeSeparatorStatus(PatientName,PatientSeparator);
        ChangeSeparatorStatus(Relation,RelationSeparator);
        ChangeSeparatorStatus(PhoneNumber,PhoneSeparator);
        ChangeSeparatorStatus(Address,AddressSeparator);
        ChangeSeparatorStatus(ProductID,ProductSeparator);
        ChangeSeparatorStatus(Diseases,DiseaseSeparator);

        Bundle extras = getIntent().getExtras();
        Caregiver_name = extras.getString("caregiver_name");
        Caregiver_email = extras.getString("caregiver_email");
        Caregiver_ID    = extras.getString("caregiver_id");


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
                Intent intent = new Intent(CreateProfile.this, ChooseProfile.class);
                //Send parameters to the ChooseProfile Activity
                Bundle extras = new Bundle();
                extras.putString("caregiver_name",Caregiver_name);
                extras.putString("caregiver_email",Caregiver_email);
                extras.putString("caregiver_id",Caregiver_ID);
                intent.putExtras(extras);
                startActivity(intent);
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
                //Call CreateProfile Function to make the http request.

                CreateProfile(Caregiver_email,PatientName.getText().toString(),Relation.getText().toString(),PhoneNumber.getText().toString(),Address.getText().toString(),Gender,Age,ProductID.getText().toString(),Diseases.getText().toString());
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //Image upload button pressed. Call the function for choosing the image from the library
                showFileChooser();
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
                    //This check to make sure that the user didn't load any custom image, so if the bitmap attribute is not equal to null.
                    //This means the user uploaded a photo.
                    //Otherwise,set the image to the selected option.
                    if (bitmap == null)
                    ProfilePhoto.setImageDrawable(getResources().getDrawable(R.drawable.male_profile));
                }
                else if(position > 0 && selectedItemText == "Female")
                {
                    //This check to make sure that the user didn't load any custom image, so if the bitmap attribute is not equal to null.
                    //This means the user uploaded a photo.
                    //Otherwise,set the image to the selected option.
                    if (bitmap == null)
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

    ///Functions///
    //Function for showing the progress dialog
    private void showDialog()
    {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    //Function for picking an image from the photo gallery.
    private void showFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //Call the onActivity result to check for the image picking success
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Setting the Bitmap to ImageView
                ProfilePhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Function for converting the image to String base64 format
    public String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //Function for making the http request to the server with the inputs on the android application
    private void CreateProfile (final String Caregiver_email,final String Patientname, final String Relation, final String Phonenumber,final String Address, final String Gender, final String Age, final String ProductID, final String patient_diseases)
    {

        // Tag used to cancel the request
        String cancel_req_tag = "createprofile";

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_CreateProfile, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d(TAG, "Create Profile Response: " + response);
                try
                {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error)
                    {
                        String patient = jObj.getJSONObject("patient").getString("name");
                        Toast.makeText(getApplicationContext(), "Profile for " + patient + " is successfully Added!", Toast.LENGTH_SHORT).show();
                        // Launch ChooseProfile activity
                        Intent intent = new Intent(CreateProfile.this, ChooseProfile.class);
                        Bundle extras = new Bundle();
                        extras.putString("caregiver_name",Caregiver_name);
                        extras.putString("caregiver_email",Caregiver_email);
                        extras.putString("caregiver_id",Caregiver_ID);
                        intent.putExtras(extras);
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

                if (bitmap!=null)
                {
                    //Converting the Image to String
                    String image = getStringImage(bitmap);

                    //Putting The parameter for the image
                    params.put("image",image);
                }
                else
                {
                    params.put("image","");
                }

                params.put("caregiver_email",Caregiver_email);
                params.put("patientname", Patientname);
                params.put("relation", Relation);
                params.put("phonenumber", Phonenumber);
                params.put("address",Address);
                params.put("gender", Gender);
                params.put("age", Age);
                params.put("productid", ProductID);
                params.put("diseases",patient_diseases);
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