package th.ac.kku.charoenkitsupat.chanyanood.managementforsugarapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NavigationMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    // The total number of menu items
    private static final int MENU_ITEMS = 6;
    private boolean mToolBarNavigationListenerIsRegistered = false;
    // Contains the views in the NavigationView
    ActionBarDrawerToggle toggle;
    private ListView drawerList;
    private ArrayAdapter<String> adapter;
    private View headerView;
    LinearLayout headerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadFragment("LoginPage");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open
                , R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.inflateHeaderView(R.layout.nav_header_navigation_main);
        headerLayout = (LinearLayout) headerView.findViewById(R.id.header);
        initNavHeader();
        headerLayout.setOnClickListener(this);
    }

    public void initNavHeader(){
        String data = loadPreferencesUser();
        try {
            JSONArray jsonArray = new JSONArray(data);
            JSONObject jsonObject = jsonArray.getJSONObject(0);

            TextView name = (TextView) headerLayout.findViewById(R.id.name);
            TextView email = (TextView) headerLayout.findViewById(R.id.email);

            name.setText(jsonObject.getString("full_name"));
            email.setText(jsonObject.getString("email"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadPreferencesUser() {
        SharedPreferences preferences = getSharedPreferences("APP_PARAMS", Context.MODE_PRIVATE);
        String data = preferences.getString("user", "Not found");
        return data;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
        Log.d("naviagtion ", "onBack");
    }

    public void enableViews(boolean enable) {
        if(enable) {
            toggle.setDrawerIndicatorEnabled(false);// Remove hamburger
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);// Show back button
            // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
            // clicks are disabled i.e. the UP button will not work.
            // We need to add a listener, as in below, so DrawerToggle will forward
            // click events to this listener.
            if(!mToolBarNavigationListenerIsRegistered) {
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Doesn't have to be onBackPressed
                        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        onBackPressed();
                    }
                });
                mToolBarNavigationListenerIsRegistered = true;
            }
        } else {
            // Remove back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            // Show hamburger
            toggle.setDrawerIndicatorEnabled(true);
            // Remove the/any drawer toggle listener
            toggle.setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.header) {
            loadFragment("ProfilePage");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_farmer) {
            loadFragment("FarmerInfoPage");
        } else if (id == R.id.nav_survey_queue) {
            loadFragment("SurveyAndQueuePage");
        } else if (id == R.id.nav_zone) {
            loadFragment("ZonePage");
        } else if (id == R.id.nav_cost) {
            loadFragment("CostIntoPage");
        } else if (id == R.id.nav_logout) {
            loadFragment("LoginPage");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    public void loadFragment(String Fragment){
        FragmentTransaction loadFragment = getSupportFragmentManager().beginTransaction();
        switch (Fragment) {
            case "DashboardPage":
                loadFragment.replace(R.id.content_frame, new DashboardPage()).addToBackStack(null).commit();
                break;
            case "FarmerInfoPage":
                loadFragment.replace(R.id.content_frame, new FarmerInfoPage()).addToBackStack(null).commit();
                break;
            case "SurveyAndQueuePage":
                loadFragment.replace(R.id.content_frame, new SurveyAndQueuePage()).addToBackStack(null).commit();
                break;
            case "ZonePage":
                loadFragment.replace(R.id.content_frame, new ZonePage()).addToBackStack(null).commit();
                break;
            case "CostIntoPage":
                loadFragment.replace(R.id.content_frame, new CostInfoPage()).addToBackStack(null).commit();
                break;
            case "ProfilePage":
                loadFragment.replace(R.id.content_frame, new ProfilePage()).addToBackStack(null).commit();
                break;
            case "FarmerMoreDetail":
                loadFragment.replace(R.id.content_frame, new FarmerMoreDetail()).addToBackStack(null).commit();
                break;
            case "FarmerEditDetail":
                loadFragment.replace(R.id.content_frame, new FarmerEditDetail()).addToBackStack(null).commit();
                break;
            case "SurveyMoreDetail":
                loadFragment.replace(R.id.content_frame, new SurveyMoreDetail()).addToBackStack(null).commit();
                break;
            case "CompareHarvestCost":
                loadFragment.replace(R.id.content_frame, new CompareHarvestCost()).addToBackStack(null).commit();
                break;
            case "LoginPage":
                loadFragment.replace(R.id.content_frame, new LoginPage()).addToBackStack(null).commit();
                break;
        }
    }
}
