package com.example.omarelaimy.iseeyou;

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

import org.w3c.dom.Text;

/**
 * Created by Omar on 3/15/2017.
 */

public class SignUp extends AppCompatActivity {
    private TextView btnAlreadyuser;
    private EditText Name,Email,Password,ConfirmPass;
    private View NameSeparator,EmailSeparator,PasswordSeparator,ConfirmPassSeparator;
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

        btnAlreadyuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSignin();
            }
        });


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

    }

    private void ChangeSeparatorColor(final EditText editText, final View view)
    {
        editText.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                view.setBackgroundColor(getResources().getColor(R.color.SeparatorFocused));
                return false;
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