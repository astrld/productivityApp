package com.example.workoutapp;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class startUp extends AppCompatActivity {
    private Button getStartedButton, backButton, nextButton, button1, button2;
    private Vibrator vibrator;
    private ImageView[] surveyQ = new ImageView[4];

    private RelativeLayout getStartedLayout, surveyLayout;
    private int surveyQNum = 0;
    private boolean survey = false;

    private boolean height = false;

    private boolean metric = false;

    private TextView startUpName, questionText,messageText, textView1, textView2;
    private EditText editText1, editText2, editText3, editText4;
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
                    }, 50*i);
                }
            }
        }, 200);
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
        backButton = findViewById(R.id.backButton);

        surveyLayout = findViewById(R.id.surveyLayout);
        questionText = findViewById(R.id.questionText);
        messageText = findViewById(R.id.messageText);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        textView1 = findViewById(R.id.textview1);
        textView2 = findViewById(R.id.textview2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(metric && !editText3.getText().toString().equals("")){
                    int cm = Integer.parseInt(editText3.getText().toString());
                    int feet = (int) (cm/30.48);
                    int inches = (int) ((cm - feet*30.48)/2.54);
                    editText3.setText(feet + "");
                    editText4.setText(inches + "");
                }
                metric = false;
                button1.setBackground(getResources().getDrawable(R.drawable.button2));
                button2.setBackground(getResources().getDrawable(R.drawable.button3));
                if(height){
                    textView1.setText("ft");
                    textView2.setText("in");
                    editText3.setX(editText1.getX() +  editText1.getWidth()/30);
                    editText4.setX(editText1.getX() + editText1.getWidth() - editText4.getWidth() - editText1.getWidth()/30);
                    button1.setX(editText3.getX() + editText3.getWidth() - button1.getWidth());
                    button2.setX(editText4.getX());
                    editText3.setVisibility(View.VISIBLE);
                    editText4.setVisibility(View.VISIBLE);
                    textView1.setVisibility(View.VISIBLE);
                    textView2.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textView1.setX(editText3.getX() + editText3.getWidth() - textView1.getWidth());
                            textView2.setX(editText4.getX() + editText4.getWidth() - textView2.getWidth());
                        }
                    }, 1);
                }
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!metric && !editText3.getText().toString().equals("") && !editText4.getText().toString().equals("")){
                    int feet = Integer.parseInt(editText3.getText().toString());
                    int inches = Integer.parseInt(editText4.getText().toString());
                    int cm = (int) (feet*30.48 + inches*2.54);
                    System.out.println(cm);
                    editText3.setText(cm + "");
                    editText4.setText("");
                }
                metric = true;
                textView1.setText(""); textView2.setText("");
                textView2.setVisibility(View.INVISIBLE);
                editText4.setVisibility(View.INVISIBLE);
                button1.setBackground(getResources().getDrawable(R.drawable.button3));
                button2.setBackground(getResources().getDrawable(R.drawable.button2));
                if(height){
                    editText3.setVisibility(View.VISIBLE);
                    textView1.setVisibility(View.VISIBLE);
                    editText3.setX(editText1.getX() + editText1.getWidth()/2 - editText4.getWidth()/2);
                    button1.setX(editText3.getX());
                    button2.setX(editText3.getX() + editText3.getWidth() - button2.getWidth());
                    textView1.setText("cm");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textView1.setX(editText3.getX() + editText3.getWidth() - textView1.getWidth() + 15);
                        }
                    }, 1);
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(surveyQNum == 0){
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
                    surveyQNum--;
                    updateProgress();
                }
            }
        });
        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(surveyQNum == 4){
                    // todo
                } else {
                    surveyQNum++;
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
        questionText.setText(""); messageText.setText(""); editText1.setText(""); editText2.setText(""); editText3.setText(""); editText4.setText(""); textView1.setText(""); textView2.setText("");
        editText1.setVisibility(View.INVISIBLE); editText2.setVisibility(View.INVISIBLE); editText3.setVisibility(View.INVISIBLE); editText4.setVisibility(View.INVISIBLE); textView1.setVisibility(View.INVISIBLE); textView2.setVisibility(View.INVISIBLE); button1.setVisibility(View.INVISIBLE); button2.setVisibility(View.INVISIBLE);
        for(int i = 0; i < 4; i++){
            if(i <= surveyQNum){
                surveyQ[i].setImageResource(R.drawable.greenrect);
            }
            else{
                surveyQ[i].setImageResource(R.drawable.beigerect);
            }
        }
        if(surveyQNum == 0){
            questionText.setText("What's your name?");
            messageText.setText("We'll use this to personalize your experience.");
            editText1.setVisibility(View.VISIBLE);
            editText2.setVisibility(View.VISIBLE);
            editText1.setHint("First Name");
            editText2.setHint("Last Name");
        } else if (surveyQNum == 1){
            height = true;
            button1.setBackground(getResources().getDrawable(R.drawable.button2));
            button2.setBackground(getResources().getDrawable(R.drawable.button3));
            button1.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);
            button1.setText("Ft/In");
            button2.setText("Cm");
            questionText.setText("What's your height?");
            messageText.setText("");

            editText3.setX(editText1.getX() +  editText1.getWidth()/30);
            editText4.setX(editText1.getX() + editText1.getWidth() - editText4.getWidth() - editText1.getWidth()/30);
            button1.setX(editText3.getX() + editText3.getWidth() - button1.getWidth());
            button2.setX(editText4.getX());
            editText3.setVisibility(View.VISIBLE);
            editText4.setVisibility(View.VISIBLE);
            textView1.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            textView1.setText("ft");
            textView2.setText("in");
            metric = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    textView1.setX(editText3.getX() + editText3.getWidth() - textView1.getWidth());
                    textView2.setX(editText4.getX() + editText4.getWidth() - textView2.getWidth());
                }
            }, 1);
        } else if (surveyQNum == 2){
            height = false;
            questionText.setText("What's your weight?");
            messageText.setText("Great, almost done!");
            editText3.setVisibility(View.VISIBLE);
            editText3.setX(editText1.getX() + editText1.getWidth()/2 - editText4.getWidth()/2);
        } else if (surveyQNum == 3){
            height = false;
            questionText.setText("What's your goal weight?");
            messageText.setText("");
            editText3.setVisibility(View.VISIBLE);
            editText3.setX(editText1.getX() + editText1.getWidth()/2 - editText4.getWidth()/2);
        }
    }

    @Override
    public void onBackPressed() {
        if(survey){
            if(surveyQNum == 0){
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
                surveyQNum--;
                updateProgress();
            }
        } else {
            super.onBackPressed();
        }
    }


}