package com.example.omarelaimy.iseeyou;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CreateProfile extends Activity {

    private NumberPicker AgePicker;
    private Spinner GenderSpinner;
    private ImageView ProfilePhoto;
    private View PatientSeparator,RelationSeparator,PhoneSeparator,ProductSeparator,PasswordSeparator,ConfirmPassSeparator;
    private EditText PatientName,Relation,PhoneNumber,ProductID,Password,ConfirmPass;
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
        ProductSeparator = (View) findViewById(R.id.productid_separator);
        PasswordSeparator = (View) findViewById(R.id.password_separator);
        ConfirmPassSeparator = (View) findViewById(R.id.confirm_pass_separator);

        //Getting the EditTexts
        PatientName = (EditText) findViewById(R.id.patient_name);
        Relation = (EditText) findViewById(R.id.relation);
        PhoneNumber = (EditText) findViewById(R.id.phone_number);
        ProductID = (EditText) findViewById(R.id.product_id);
        Password = (EditText) findViewById(R.id.password);
        ConfirmPass = (EditText) findViewById(R.id.confirm_password);


        //Change the color of the view when focused..
        ChangeSeparatorStatus(PatientName,PatientSeparator);
        ChangeSeparatorStatus(Relation,RelationSeparator);
        ChangeSeparatorStatus(PhoneNumber,PhoneSeparator);
        ChangeSeparatorStatus(ProductID,ProductSeparator);
        ChangeSeparatorStatus(Password,PasswordSeparator);
        ChangeSeparatorStatus(ConfirmPass,ConfirmPassSeparator);



        // Initializing a String Array for the Gender
        String[] Gender = new String[]{
                "Gender",
                "Male",
                "Female",
        };

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
                    ProfilePhoto.setImageDrawable(getResources().getDrawable(R.drawable.heartrate));
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
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
            }
        });



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