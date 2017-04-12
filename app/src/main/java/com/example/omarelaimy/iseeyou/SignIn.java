package com.example.omarelaimy.iseeyou;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



/**
 * Created by Omar on 3/15/2017.
 */

public class SignIn extends AppCompatActivity {
    private TextView btnSignUp;
    private Button btnSignIn;
    private View EmailSeparator,PasswordSeparator;
    private EditText Email,Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
           getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        changeStatusBarColor();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        //Get the sign up button to navigate to the sign up page//
        btnSignUp = (TextView) findViewById(R.id.signup1);

        //Get the sign in button
        btnSignIn = (Button) findViewById(R.id.sign_in);

        //Get the separator lines
        EmailSeparator =  (View) findViewById(R.id.email_separator);
        PasswordSeparator = (View) findViewById(R.id.password_separator);

        //Get the Email and password texts
        Email = (EditText) findViewById(R.id.email_text);
        Password = (EditText) findViewById(R.id.password_text);


        ChangeSeparatorStatus(Email,EmailSeparator);
        ChangeSeparatorStatus(Password,PasswordSeparator);

        //If Sign up is pressed, go to sign up page
     btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSignUp();
               // startActivity(new Intent(SignIn.this, CreateProfile.class));
               // finish();
            }
        });

    //If Sign in is pressed, go to choose profile page
    btnSignIn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(SignIn.this,ChooseProfile.class));
            finish();
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
    //Launch SignUp Page
    private void launchSignUp()
    {
        startActivity(new Intent(SignIn.this, SignUp.class));
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