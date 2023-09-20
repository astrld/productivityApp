package com.example.workoutapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

public class main extends AppCompatActivity{
    private DBHandler dbHandler;
    private TextView displayName;

    private FragmentContainerView bottomNav;

    private int currentView = 1;

    private ImageView home, calendar, graph, profile, setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        setStatusBarColor(R.color.beige);
        dbHandler = Database.getDBHandler();
        displayName = findViewById(R.id.displayName);
        bottomNav = findViewById(R.id.fragment_container_view);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                displayName.setText("Hello, " + dbHandler.getFirstName() + " " + dbHandler.getLastName());
                home = bottomNav.findViewById(R.id.home);
                calendar = bottomNav.findViewById(R.id.calendar);
                graph = bottomNav.findViewById(R.id.graph);
                profile = bottomNav.findViewById(R.id.profile);
                setting = bottomNav.findViewById(R.id.setting);
                home.setColorFilter(getResources().getColor(R.color.beige2));
                calendar.setColorFilter(getResources().getColor(R.color.black));
                graph.setColorFilter(getResources().getColor(R.color.black));
                profile.setColorFilter(getResources().getColor(R.color.black));
                setting.setColorFilter(getResources().getColor(R.color.black));
                home.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switchToHome();
                    }
                });
                calendar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switchToCalendar();
                    }
                });
                graph.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switchToGraph();
                    }
                });
                profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switchToProfile();
                    }
                });
                setting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        switchToSetting();
                    }
                });
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

    private void updateBottomNav(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(currentView == 1){
                    bottomNav = findViewById(R.id.fragment_container_view);
                }else if(currentView == 2){
                    bottomNav = findViewById(R.id.fragment_container_view1);
                }
                else if(currentView == 3){
                    bottomNav = findViewById(R.id.fragment_container_view2);
                }
                else if(currentView == 4){
                    bottomNav = findViewById(R.id.fragment_container_view3);
                }
                else if(currentView == 5){
                    bottomNav = findViewById(R.id.fragment_container_view4);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        home = bottomNav.findViewById(R.id.home);
                        calendar = bottomNav.findViewById(R.id.calendar);
                        graph = bottomNav.findViewById(R.id.graph);
                        profile = bottomNav.findViewById(R.id.profile);
                        setting = bottomNav.findViewById(R.id.setting);
                        home.setColorFilter(getResources().getColor(R.color.black));
                        calendar.setColorFilter(getResources().getColor(R.color.black));
                        graph.setColorFilter(getResources().getColor(R.color.black));
                        profile.setColorFilter(getResources().getColor(R.color.black));
                        setting.setColorFilter(getResources().getColor(R.color.black));
                        if(currentView == 1){
                            home.setColorFilter(getResources().getColor(R.color.beige2));
                        }else if(currentView == 2){
                            calendar.setColorFilter(getResources().getColor(R.color.beige2));
                        }
                        else if(currentView == 3){
                            graph.setColorFilter(getResources().getColor(R.color.beige2));
                        }
                        else if(currentView == 4){
                            profile.setColorFilter(getResources().getColor(R.color.beige2));
                        }
                        else if(currentView == 5){
                            setting.setColorFilter(getResources().getColor(R.color.beige2));
                        }
                        home.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                switchToHome();
                            }
                        });
                        calendar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                switchToCalendar();
                            }
                        });
                        graph.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                switchToGraph();
                            }
                        });
                        profile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                switchToProfile();
                            }
                        });
                        setting.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                switchToSetting();
                            }
                        });
                    }
                }, 10);
            }
        }, 10);
    }
    private void switchToHome(){
        if(currentView != 1) {
            setContentView(R.layout.home);
            currentView = 1;
            updateBottomNav();
        }
    }

    private void switchToCalendar(){
        if(currentView != 2){
            setContentView(R.layout.calendar);
            currentView = 2;
            updateBottomNav();
        }
    }

    private void switchToGraph(){
        if(currentView != 3){
            setContentView(R.layout.graph);
            currentView = 3;
            updateBottomNav();
        }
    }

    private void switchToProfile(){
        if(currentView != 4){
            setContentView(R.layout.profile);
            currentView = 4;
            updateBottomNav();
        }
    }

    private void switchToSetting(){
        if(currentView != 5){
            setContentView(R.layout.settings);
            currentView = 5;
            updateBottomNav();
        }
    }

}
