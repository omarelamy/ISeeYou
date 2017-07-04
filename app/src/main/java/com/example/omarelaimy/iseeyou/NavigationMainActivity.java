package com.example.omarelaimy.iseeyou;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//import com.example.omarelaimy.iseeyou.navigationdrawer.R;

import com.example.omarelaimy.iseeyou.Fragments.AppointmentsFragment;
import com.example.omarelaimy.iseeyou.Fragments.HeartRateFragment;
import com.example.omarelaimy.iseeyou.Fragments.InventoryFragment;
import com.example.omarelaimy.iseeyou.Fragments.NotificationsFragment;
import com.example.omarelaimy.iseeyou.Fragments.PillboxFragment;
import com.example.omarelaimy.iseeyou.Fragments.ProfileFragment;

import static com.example.omarelaimy.iseeyou.Fragments.HeartRateFragment.newHearRateInstance;
import static com.example.omarelaimy.iseeyou.Fragments.InventoryFragment.newInventoryInstance;
import static com.example.omarelaimy.iseeyou.Fragments.PillboxFragment.newPillBoxInstance;


/**
 * Created by Omar on 6/24/2017.
 */

public class NavigationMainActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView iv_patientImage;
    private ImageView EditIcon;
    private Button EditButton;
    private TextView tv_patientName;
    private Toolbar toolbar;
   // private FloatingActionButton fab;

    //Patient's info
    private String Patient_ID = "";
    private String Patient_ProductID = "";
    private String Patient_Name = "";
    private String Patient_Gender = "";
    private Bitmap Patient_Image;
    //Caregiver info
    private String Caregiver_name;
    private String Caregiver_email;
    private String Caregiver_ID;

    public static String CURRENT_TAG = Config.TAG_PROFILE;

    // index to identify current nav menu item
    public static int navItemIndex = 5;



    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load profile fragment when user presses back key
    private boolean shouldLoadProfileFragOnBackPress = true;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //get Patient's name, image and id
        Intent intent = getIntent();
        Bundle extras   = intent.getExtras();

        Patient_Name  = extras.getString("patient_name");
        Patient_ID = extras.getString("patient_id");
        Patient_Gender    = extras.getString("patient_gender");
        Caregiver_email =  extras.getString("caregiver_email");
        Caregiver_ID =  extras.getString("caregiver_id");
        Caregiver_name = extras.getString("caregiver_name");
        Patient_ProductID = extras.getString("productid");
        Patient_Image = Config.img;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigationbar_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        EditIcon = (ImageView) findViewById(R.id.nav_edit_icon);
        EditButton = (Button) findViewById(R.id.nav_edit_button);
        //fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);

        tv_patientName = (TextView) navHeader.findViewById(R.id.patient_name);
        iv_patientImage = (ImageView) navHeader.findViewById(R.id.patient_img);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

     /*   fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 5;
            CURRENT_TAG = Config.TAG_PROFILE;
           loadProfileFragment();
        }

    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        //Set the Patient Name.
        tv_patientName.setText(Patient_Name);
        //Set the Patient's Profile Image.
        iv_patientImage.setImageBitmap(Patient_Image);
        //Show dot next to notifications label.
        navigationView.getMenu().getItem(4).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadProfileFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
          //  toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
  Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getProfileFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
       // toggleFab();

        //Closing drawer on item click
          drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();

    }

    private Fragment getProfileFragment() {
        switch (navItemIndex) {
            case 0:
                //  inventory
                InventoryFragment inventoryFragment = newInventoryInstance(Patient_ID);
                return inventoryFragment;

            case 1:
                // pillbox
                PillboxFragment pillboxFragment = newPillBoxInstance(Patient_ID,Patient_ProductID);
                return pillboxFragment;

            case 2:
                // heartrate
                HeartRateFragment heartRateFragment = newHearRateInstance(Patient_ID,Patient_Name);
                //Bundle bundle = new Bundle();
                //bundle.putString("patientid",Patient_ID);
                //heartRateFragment.setArguments(bundle);
                return heartRateFragment;

            case 3:
                // appointments
                AppointmentsFragment appointmentsFragment = new AppointmentsFragment();
                return appointmentsFragment;

            case 4:
                // notifications
                NotificationsFragment notificationsFragment = new NotificationsFragment();
                return notificationsFragment;

            case 5:
                // profile
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;

        //    case 6:
                // switch profile
        //        SwitchProfileFragment switchProfileFragment = new SwitchProfileFragment();
        //        return switchProfileFragment;

            //case 7:
                // logout
            //    LogoutFragment logoutFragment = new LogoutFragment();
            //    return logoutFragment;
              //  LogoutMessage();
            default:
                return new ProfileFragment();
        }
    }

    private void setToolbarTitle() {
        //If Selected item is Heart rate monitor or my notifications. hide the edit icons.
        if (navItemIndex == 2 || navItemIndex == 4)
        {
            EditIcon.setVisibility(View.GONE);
            EditButton.setVisibility(View.GONE);
        }
        else
        {
            EditIcon.setVisibility(View.VISIBLE);
            EditButton.setVisibility(View.VISIBLE);
        }
        if(navItemIndex == 7 || navItemIndex == 6)
            return;
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    public void setNavMenuItemThemeColors(int color){
        //Setting default colors for menu item Text and Icon
        int navDefaultTextColor = Color.parseColor("#202020");
        int navDefaultIconColor = Color.parseColor("#737373");

        //Defining ColorStateList for menu item Text
        ColorStateList navMenuTextList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_pressed}
                },
                new int[] {
                        color,
                        navDefaultTextColor,
                        navDefaultTextColor,
                        navDefaultTextColor,
                        navDefaultTextColor
                }
        );

        //Defining ColorStateList for menu item Icon
        ColorStateList navMenuIconList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{android.R.attr.state_enabled},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_pressed}
                },
                new int[] {
                        color,
                        navDefaultIconColor,
                        navDefaultIconColor,
                        navDefaultIconColor,
                        navDefaultIconColor
                }
        );

        navigationView.setItemTextColor(navMenuTextList);
        navigationView.setItemIconTintList(navMenuIconList);
    }
    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
        setNavMenuItemThemeColors(getResources().getColor(R.color.colorAccent));
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_inventory:
                        navItemIndex = 0;
                        CURRENT_TAG = Config.TAG_INVENTORY;
                        break;
                    case R.id.nav_pillbox:
                        navItemIndex = 1;
                        CURRENT_TAG = Config.TAG_PILLBOX;
                        break;
                    case R.id.nav_heartrate:
                        navItemIndex = 2;
                        CURRENT_TAG = Config.TAG_HEARTRATE;
                        break;
                    case R.id.nav_appointments:
                        navItemIndex = 3;
                        CURRENT_TAG = Config.TAG_APPOINTMENTS;
                        break;
                    case R.id.nav_notifications:
                        navItemIndex = 4;
                        CURRENT_TAG = Config.TAG_NOTIFICATIONS;
                        break;
                    case R.id.nav_profile:
                        navItemIndex = 5;
                        CURRENT_TAG = Config.TAG_PROFILE;
                        break;
                        // launch new intent instead of loading fragment

            //           startActivity(new Intent(NavigationMainActivity.this, SignIn.class));
             //           drawer.closeDrawers();
             //           return true;
                    case R.id.nav_switchpatient:
                        navItemIndex = 6;
                        CURRENT_TAG = Config.TAG_SWITCHPROFILE;
                       break;
                        // launch new intent instead of loading fragment

              //          startActivity(new Intent(NavigationMainActivity.this, SignIn.class));
            //           drawer.closeDrawers();
            //           return true;
                   case R.id.nav_logout:
                        navItemIndex = 7;
                       CURRENT_TAG = Config.TAG_LOGOUT;
                        break;
                    default:
                        navItemIndex = 5;

                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                if(navItemIndex == 7)
                {
                    LogoutMessage();
                }
                else if(navItemIndex == 6)
                {
                    SwitchProfile();
                }
                loadProfileFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadProfileFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 5) {
                navItemIndex = 5;
                CURRENT_TAG = Config.TAG_PROFILE;
                loadProfileFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is notifications, load the menu created for notifications
        //if (navItemIndex == 3)
        //{
          //  getMenuInflater().inflate(R.menu.main, menu);
        //}
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    // show or hide the fab
 /*   private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }*/

 public void LogoutMessage()
 {
     new AlertDialog.Builder(this)
             .setTitle("Logout")
             .setMessage("Are you sure you want to logout?")
             .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     // Launch login activity
                     Intent intent = new Intent(NavigationMainActivity.this, SignIn.class);
                     startActivity(intent);
                     finish();
                 }
             }).setNegativeButton("No", null).show();

 }
 public void SwitchProfile()
 {
     new AlertDialog.Builder(this)
             .setTitle("Switch Profile")
             .setMessage("Are you sure you want to switch a patient's profile?")
             .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     //Launch Chooseprofile Activity
                     Bundle extras = new Bundle();
                     Intent intent = new Intent(NavigationMainActivity.this,ChooseProfile.class);
                     extras.putString("caregiver_name",Caregiver_name);
                     extras.putString("caregiver_email",Caregiver_email);
                     extras.putString("caregiver_id",Caregiver_ID);
                     intent.putExtras(extras);
                     startActivity(intent);
                     finish();
                 }
             }).setNegativeButton("No", null).show();
 }
}
