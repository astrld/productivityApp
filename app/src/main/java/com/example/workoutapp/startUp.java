package com.example.workoutapp;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class startUp extends AppCompatActivity {
    private Button getStartedButton, backButton, nextButton;
    private Vibrator vibrator;
    private ImageView[] surveyQ = new ImageView[5];

    private RelativeLayout getStartedLayout, surveyLayout;
    private int surveryQ = 0;
    private boolean survey = false;

    private TextView startUpName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startup_xml);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        startUpName = findViewById(R.id.startUpName);
        String name = "fit-track";
        startUpName.setText("");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for(int i = 0; i< name.length(); i++){
                    final int finalI = i;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startUpName.setText(startUpName.getText() + "" + name.charAt(finalI));
                        }
                    }, 100*i);
                }
            }
        }, 100);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switchToGetStarted();
            }
        }, 1000);
    }

    private void setStatusBarColor(@ColorRes int colorRes) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(colorRes));
        }
    }

    private void switchToGetStarted() {
        survey = false;
        setContentView(R.layout.get_started);
        setStatusBarColor(R.color.black);
        getStartedLayout = findViewById(R.id.getStartedLayout);
        getStartedButton = findViewById(R.id.btnGetStarted);


        getStartedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TranslateAnimation animate = new TranslateAnimation(0, -getStartedLayout.getWidth(), 0, 0);
                animate.setDuration(200);
                animate.setFillAfter(true);
                getStartedLayout.startAnimation(animate);
                codeForSurvey();
            }
        });
    }

    private void codeForSurvey(){
        setContentView(R.layout.survey);
        setStatusBarColor(R.color.beige);
        survey = true;
        surveyQ[0] = findViewById(R.id.progress1);
        surveyQ[1] = findViewById(R.id.progress2);
        surveyQ[2] = findViewById(R.id.progress3);
        surveyQ[3] = findViewById(R.id.progress4);
        surveyQ[4] = findViewById(R.id.progress5);
        backButton = findViewById(R.id.backButton);

        surveyLayout = findViewById(R.id.surveyLayout);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(surveryQ == 0){
                    TranslateAnimation animate = new TranslateAnimation(0, surveyLayout.getWidth(), 0, 0);
                    animate.setDuration(200);
                    animate.setFillAfter(true);
                    surveyLayout.startAnimation(animate);
                    switchToGetStarted();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TranslateAnimation animate = new TranslateAnimation(-getStartedLayout.getWidth(), 0, 0, 0);
                            animate.setDuration(200);
                            animate.setFillAfter(true);
                            getStartedLayout.startAnimation(animate);
                        }
                    }, 1);
                } else {
                    surveryQ--;
                    updateProgress();
                }
            }
        });
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(surveryQ == 4){
                    // todo
                } else {
                    surveryQ++;
                    updateProgress();
                }
            }
        });
        updateProgress();
        // after 1s
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TranslateAnimation animate = new TranslateAnimation(surveyLayout.getWidth(), 0, 0, 0);
                animate.setDuration(200);
                animate.setFillAfter(true);
                surveyLayout.startAnimation(animate);
            }
        }, 1);
    }

    private void updateProgress(){
        for(int i = 0; i < 5; i++){
            if(i <= surveryQ){
                surveyQ[i].setImageResource(R.drawable.greenrect);
            }
            else{
                surveyQ[i].setImageResource(R.drawable.beigerect);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(survey){
            if(surveryQ == 0){
                switchToGetStarted();
            } else {
                surveryQ--;
                updateProgress();
            }
        } else {
            super.onBackPressed();
        }
    }


}