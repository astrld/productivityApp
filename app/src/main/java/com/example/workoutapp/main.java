package com.example.workoutapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class main extends AppCompatActivity{
    private DBHandler dbHandler;
    private TextView displayName;

    public static String currentTab = "home";

    private int currentView = 1;
    private BottomNavigationView bottomNavigationView;

    private homeFragment HomeFragment = new homeFragment();

    private calendarFragment CalendarFragment = new calendarFragment();

    private graphFragment GraphFragment = new graphFragment();

    private settingsFragment SettingsFragment = new settingsFragment();

    private static final int HOME_FRAGMENT = R.id.homeMenu;

    private Vibrator vibrator;

    public static Context getContext() {
        return getContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setStatusBarColor(R.color.beige);
        dbHandler = Database.getDBHandler();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Workout Channel";
            String description = "Channel for workout notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("workout_channel", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel); // Use getSystemService here
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.container, HomeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                vibrator.vibrate(5);
                if(item.getItemId() == R.id.homeMenu){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, HomeFragment).commit();
                    currentTab = "home";
                } else if(item.getItemId() == R.id.calendarMenu){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, CalendarFragment).commit();
                    currentTab = "calendar";
                } else if(item.getItemId() == R.id.graphMenu){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, GraphFragment).commit();
                    currentTab = "graph";
                } else if(item.getItemId() == R.id.settingsMenu){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, SettingsFragment).commit();
                    currentTab = "settings";
                }
                return true;
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
