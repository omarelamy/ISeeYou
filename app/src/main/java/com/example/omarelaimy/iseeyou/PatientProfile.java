package com.example.omarelaimy.iseeyou;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
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
 * Created by hp on 25-Jun-17.
 */

public class PatientProfile extends FragmentActivity {
    String Patient_name = "";
    String Patient_id="";
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Bundle extras   = intent.getExtras();
        Patient_name  = extras.getString("patient_name");
        Patient_id    = extras.getString("patient_id");
        super.onCreate(savedInstanceState);

    }
    }
