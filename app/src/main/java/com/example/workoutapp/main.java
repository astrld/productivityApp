package com.example.workoutapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class main extends AppCompatActivity{
    private DBHandler dbHandler;
    private TextView displayName;

    private int currentView = 1;
    private BottomNavigationView bottomNavigationView;

    private homeFragment HomeFragment = new homeFragment();

    private calendarFragment CalendarFragment = new calendarFragment();

    private graphFragment GraphFragment = new graphFragment();

    private profileFragment ProfileFragment = new profileFragment();

    private settingsFragment SettingsFragment = new settingsFragment();

    private static final int HOME_FRAGMENT = R.id.homeMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setStatusBarColor(R.color.beige);
        dbHandler = Database.getDBHandler();
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, HomeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // should get it from @menu/bottom_navigation_menu
                if(item.getItemId() == R.id.homeMenu){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, HomeFragment).commit();
                    return true;
                } else if(item.getItemId() == R.id.calendarMenu){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, CalendarFragment).commit();
                    return true;
                } else if(item.getItemId() == R.id.graphMenu){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, GraphFragment).commit();
                    return true;
                } else if(item.getItemId() == R.id.profileMenu){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, ProfileFragment).commit();
                    return true;
                } else if(item.getItemId() == R.id.settingsMenu){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, SettingsFragment).commit();
                    return true;
                }
                return false;
            }
        });

    }

    private void setStatusBarColor(@ColorRes int colorRes) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(colorRes));
        }
    }


}
