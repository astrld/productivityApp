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

public class stopwatchFragment extends Fragment{

    private ImageView startButton;
    private ImageView pauseButton;
    private ImageView stopButton;

    private TextView timer;
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
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });
    }

    private void startTimer() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();
                String time = timeFormat.format(date);
                timer.setText(time);
                handler.postDelayed(this, 1000);
            }
        });
    }
}
