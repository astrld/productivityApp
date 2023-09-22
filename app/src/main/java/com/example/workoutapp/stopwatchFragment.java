package com.example.workoutapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class stopwatchFragment extends Fragment{

    private ImageView startButton;
    private ImageView pauseButton;
    private ImageView stopButton;

    private TextView timer;

    private boolean running = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stopwatch, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startButton = getView().findViewById(R.id.play);
        pauseButton = getView().findViewById(R.id.pause);
        stopButton = getView().findViewById(R.id.stop);
        timer = getView().findViewById(R.id.stopwatchTextview);
        Database.dbHandler.addDataToStopwatch(seconds + "", false);
        startTimer();

        seconds = Database.dbHandler.getStopwatchTime();
        if(seconds != 0){
            if (!Database.dbHandler.getStopwatchRunning()) {
                running = true;
            }
        }
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = true;
                Database.dbHandler.updateStopwatch(String.valueOf(Database.dbHandler.getStopwatchTime()), true);
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Database.dbHandler.updateStopwatch("0", false);
                running = false;
                seconds = 0;
                timer.setText("00:00:00");
            }
        });
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = false;
            }
        });
    }

    private int seconds = 0;
    private void startTimer() {
        System.out.println("Starting timer");
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("reaching here1");
                if(running){
                    System.out.println("reaching here");
                    seconds = Database.dbHandler.getStopwatchTime();
                    seconds++;
                    int hours = seconds / 3600;
                    int minutes = (seconds % 3600) / 60;
                    int secs = seconds % 60;
                    String time = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, secs);
                    timer.setText(time);
                    handler.postDelayed(this, 1000);
                    Database.dbHandler.updateStopwatch(String.valueOf(seconds), true);
                }
            }
        });
    }
}
