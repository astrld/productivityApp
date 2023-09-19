package com.example.workoutapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;

public class main extends AppCompatActivity{
    private DBHandler dbHandler;
    public static boolean hasDB = false;
    private TextView displayName;

    private ImageView home, calendar, graph, profile, setting;

    private LinearLayout bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        setStatusBarColor(R.color.beige);
        dbHandler = Database.getDBHandler();
        displayName = findViewById(R.id.displayName);

        home = findViewById(R.id.home);
        calendar = findViewById(R.id.calendar);
        graph = findViewById(R.id.graph);
        profile = findViewById(R.id.profile);
        setting = findViewById(R.id.setting);

        home.setMinimumWidth(home.getHeight());
        calendar.setMinimumWidth(calendar.getHeight());
        graph.setMinimumWidth(graph.getHeight());
        profile.setMinimumWidth(profile.getHeight());
        setting.setMinimumWidth(setting.getHeight());

        bottomNav = findViewById(R.id.bottomNav);
        double width = bottomNav.getWidth() - 20;
        // make all icons equidistant from each other and they should cover the entire linear layout
        double x = width/5;
        home.setX((float) (x/2));
        calendar.setX((float) (x/2 + x));
        graph.setX((float) (x/2 + 2*x));
        profile.setX((float) (x/2 + 3*x));
        setting.setX((float) (x/2 + 4*x));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                displayName.setText("Hello, " + dbHandler.getFirstName() + " " + dbHandler.getLastName());
                System.out.println("Width of bottomNav: " + bottomNav.getWidth());
                System.out.println("Height of bottomNav: " + bottomNav.getHeight());
                System.out.println("Width of home: " + home.getWidth());
                System.out.println("Height of home: " + home.getHeight());
            }
        }, 100);
    }

    private void setStatusBarColor(@ColorRes int colorRes) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(colorRes));
        }
    }
}
