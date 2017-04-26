package com.example.omarelaimy.iseeyou;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Omar on 3/15/2017.
 */

public class SignUp extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private static final String URL_FOR_REGISTRATION = "https://icu.000webhostapp.com/register.php";
    ProgressDialog progressDialog;
    private TextView btnAlreadyuser;
    private EditText Name,Email,Password,ConfirmPass;
    private View NameSeparator,EmailSeparator,PasswordSeparator,ConfirmPassSeparator;
    private Button btnSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        changeStatusBarColor();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        //Get the already a user button to navigate to sign in page
        btnAlreadyuser = (TextView) findViewById(R.id.already_user);

        //Get the sign up button
        btnSignup = (Button) findViewById(R.id.signup2);

        //Get the Separators..
        NameSeparator = (View) findViewById(R.id.name_separator);
        EmailSeparator = (View) findViewById(R.id.email_separator);
        PasswordSeparator = (View) findViewById(R.id.password_separator);
        ConfirmPassSeparator = (View) findViewById(R.id.confirm_pass_separator);

        //Get the Edit Texts..
        Name = (EditText) findViewById(R.id.name_text);
        Email = (EditText) findViewById(R.id.email_text);
        Password = (EditText) findViewById(R.id.password_text);
        ConfirmPass = (EditText) findViewById(R.id.confirm_password);

        //Change the separators color when touching any of the edit texts.
        ChangeSeparatorColor(Name,NameSeparator);
        ChangeSeparatorColor(Email,EmailSeparator);
        ChangeSeparatorColor(Password,PasswordSeparator);
        ChangeSeparatorColor(ConfirmPass,ConfirmPassSeparator);

        btnAlreadyuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSignin();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Signup(Name.getText().toString(),Email.getText().toString(),Password.getText().toString(),ConfirmPass.getText().toString());
            }
        });


    }

    private void Signup (final String name, final String email, final String password, final String confirmpassword)
    {
        // Tag used to cancel the request
         String cancel_req_tag = "register";

       // progressDialog.setMessage("We're signing you up :)");

        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_FOR_REGISTRATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
              //  hideDialog();

                try
                {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error)
                    {
                        String user = jObj.getJSONObject("user").getString("name");
                        Toast.makeText(getApplicationContext(), "Welcome " + user +", You are successfully Added!", Toast.LENGTH_SHORT).show();
                        // Launch login activity
                        Intent intent = new Intent(SignUp.this, SignIn.class);
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
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("confirmpassword",confirmpassword);
                return params;
            }
        };
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void ChangeSeparatorColor(final EditText editText, final View view)
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


    private void launchSignin()
    {
        startActivity(new Intent(SignUp.this, SignIn.class));
        finish();
    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.NotificationBar));
        }
    }
}