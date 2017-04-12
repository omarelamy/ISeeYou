
package com.example.omarelaimy.iseeyou;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

public class ChooseProfile extends FragmentActivity {
    public final static int LOOPS = 1000;
    public Button CreateProfile;
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
        super.onCreate(savedInstanceState);

        setContentView(R.layout.viewpager);

        CreateProfile = (Button) findViewById(R.id.create_profile);
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
        CreateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseProfile.this, CreateProfile.class));
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