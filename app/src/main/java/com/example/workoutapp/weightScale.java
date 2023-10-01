package com.example.workoutapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.marcinmoskala.arcseekbar.ArcSeekBar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class weightScale extends Fragment {

    private ArcSeekBar weightArcSeekBar;

    private AppCompatButton minusButton, plusButton;

    private TextView weightTextView;

    private Handler decreaseHandler = new Handler();
    private Runnable decreaseRunnable;
    private boolean decreaseActive = false;

    private double decreaseAmount = 0.1;

    private int ran = 0;

    private Handler increaseHandler = new Handler();
    private Runnable increaseRunnable;
    private boolean increaseActive = false;
    private double increaseAmount = 0.1;
    private int ran2 = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weight_scale, container, false);
    }

    private String weightUnit = "lbs";
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        weightArcSeekBar = view.findViewById(R.id.weightScale);
        minusButton = view.findViewById(R.id.weightDecreaseButton);
        plusButton = view.findViewById(R.id.weightIncreaseButton);
        weightTextView = view.findViewById(R.id.weightDisplay);
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        date = cal.getTime();
        weightArcSeekBar.setProgress((int) Double.parseDouble(Database.getDBHandler().getCurrentWeight(date)));
        weightUnit = Database.getDBHandler().getWeightUnits();
        weightTextView.setText(Database.getDBHandler().getCurrentWeight(date) + " " + weightUnit);
        weightArcSeekBar.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    weightTextView.setText(weightArcSeekBar.getProgress() +  " " + weightUnit);
                case MotionEvent.ACTION_UP:
                    updateTodaysWeight(weightArcSeekBar.getProgress());
            }
            return false;
        });

        minusButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        decreaseActive = true;
                        decreaseHandler.postDelayed(decreaseRunnable, 0);
                        break;
                    case MotionEvent.ACTION_UP:
                        decreaseActive = false;
                        ran = 0;
                        decreaseAmount = 0.1;
                        decreaseHandler.removeCallbacks(decreaseRunnable);
                        break;
                }
                return true;
            }
        });

        decreaseRunnable = new Runnable() {
            @Override
            public void run() {
                if (decreaseActive) {
                    decreaseWeight(decreaseAmount); // Long press (2 seconds or more)
                    decreaseHandler.postDelayed(this, 100); // Decrease every 100 milliseconds
                    ran++;
                    if(ran == 20){
                        decreaseAmount = .5;
                    } else if(ran == 40){
                        decreaseAmount = 1;
                    }
                }
            }
        };

        plusButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    increaseActive = true;
                    increaseHandler.postDelayed(increaseRunnable, 0);
                    break;
                case MotionEvent.ACTION_UP:
                    increaseActive = false;
                    ran2 = 0;
                    increaseAmount = 0.1;
                    increaseHandler.removeCallbacks(increaseRunnable);
                    break;
            }
            return true;
        });

        increaseRunnable = new Runnable() {
            @Override
            public void run() {
                if (increaseActive) {
                    increaseWeight(increaseAmount);
                    increaseHandler.postDelayed(this, 100);
                    ran2++;
                    if(ran2 == 20){
                        increaseAmount = .5;
                    } else if(ran2 == 40){
                        increaseAmount = 1;
                    }
                }
            }
        };
    }

    private void decreaseWeight(double amount) {
        String currentWeightString = weightTextView.getText().toString();
        if(currentWeightString.equals("") || currentWeightString.startsWith("0")){
            return;
        }
        int indexOfSpace = currentWeightString.indexOf(" ");
        double currentWeight = Double.parseDouble(currentWeightString.substring(0, indexOfSpace));
        double newWeight = currentWeight - amount;
        double roundedWeight = Math.round(newWeight * 10) / 10.0;
        if (newWeight < 0) {
            newWeight = 0;
        }
        weightArcSeekBar.setProgress((int) newWeight);
        weightTextView.setText(roundedWeight +  " " + weightUnit);
        updateTodaysWeight(roundedWeight);
    }

    private void increaseWeight(double amount) {
        String currentWeightString = weightTextView.getText().toString();
        if(currentWeightString.equals("") || currentWeightString.startsWith("0")){
            return;
        }
        int indexOfSpace = currentWeightString.indexOf(" ");
        double currentWeight = Double.parseDouble(currentWeightString.substring(0, indexOfSpace));
        double newWeight = currentWeight + amount;
        double roundedWeight = Math.round(newWeight * 10) / 10.0;
        if (newWeight < 0) {
            newWeight = 0;
        }
        weightArcSeekBar.setProgress((int) newWeight);
        weightTextView.setText(roundedWeight +  " " + weightUnit);
        updateTodaysWeight(roundedWeight);
    }

    private void updateTodaysWeight(double weight) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        date = cal.getTime();
        java.text.SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = sdf.format(date);
        Database.dbHandler.ifDayExists(dateString,weight,0,0,0,0,0,0,0,0);
    }
}
