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
    private TextView displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        setStatusBarColor(R.color.beige);
        dbHandler = Database.getDBHandler();
        displayName = findViewById(R.id.displayName);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                displayName.setText("Hello, " + dbHandler.getFirstName() + " " + dbHandler.getLastName());
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
