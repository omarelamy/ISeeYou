package com.example.omarelaimy.iseeyou;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import com.example.omarelaimy.iseeyou.R;


public class ChooseProfile extends FragmentActivity {
    private String Caregiver_email = "";
    private String Caregiver_name = "";
    public final static int LOOPS = 1000;
    public Button CreateProfileBtn;
    public static int FIRST_PAGE; // = count * LOOPS / 2;
    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.7f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
    public MyPagerAdapter adapter;
    public ViewPager pager;
    /*** variables for the View */
    public int coverUrl[];
    public static int count;

    public static ChooseProfile ChooseProfileCtx;

    public static int currentPage = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Get email passed by signin activity
        Bundle extras = getIntent().getExtras();
        Caregiver_name = extras.getString("caregiver_name");
        Caregiver_email = extras.getString("caregiver_email");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.viewpager);

        CreateProfileBtn = (Button) findViewById(R.id.create_profile);
        coverUrl = new int[] { R.drawable.male_profile, R.drawable.male_profile,
                R.drawable.male_profile, R.drawable.female_profile};

        ChooseProfileCtx = this;
        pager = (ViewPager) findViewById(R.id.myviewpager);
        count = coverUrl.length;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int pageMargin = 0;
        pageMargin = (int) ((metrics.widthPixels / 4) *2);
        pager.setPageMargin(-pageMargin);


        //If Create Profile button is pressed, go to sign up page
        CreateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseProfile.this, CreateProfile.class);
                //Send parameters to the ChooseProfile Activity
                Bundle extras = new Bundle();
                extras.putString("caregiver_name",Caregiver_name);
                extras.putString("caregiver_email",Caregiver_email);
                intent.putExtras(extras);

                startActivity(intent);
                finish();

            }
        });
        try {
            adapter = new MyPagerAdapter(this, this.getSupportFragmentManager());
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

}