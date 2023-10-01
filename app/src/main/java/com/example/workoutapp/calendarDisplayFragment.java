package com.example.workoutapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class calendarDisplayFragment extends Fragment {

    private Spinner spinner;

    private Button day1, day2, day3, day4, day5;

    public static Date date;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar_display, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = view.findViewById(R.id.spinnerDatePeriod);
        Adapter adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, getResources().getStringArray(R.array.spinner_array));
        spinner.setAdapter((SpinnerAdapter) adapter);
        ((ArrayAdapter<?>) adapter).setDropDownViewResource(R.layout.spinner_item);
        spinner.setPopupBackgroundDrawable(getResources().getDrawable(R.color.blue2));
        day1 = view.findViewById(R.id.btnBefore2);
        day2 = view.findViewById(R.id.btnBefore1);
        day3 = view.findViewById(R.id.btnCurrent);
        day4 = view.findViewById(R.id.btnAfter1);
        day5 = view.findViewById(R.id.btnAfter2);
        Calendar cal = Calendar.getInstance();
        date = cal.getTime();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Calendar cal = Calendar.getInstance();
                Date tdy = cal.getTime();
                date = tdy;
                dateChanged(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Calendar cal = Calendar.getInstance();
                Date tdy = cal.getTime();
                date = tdy;
                dateChanged(0);
            }
        });

        day1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateChanged(-2);
            }
        });
        day2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateChanged(-1);
            }
        });
        day3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateChanged(0);
            }
        });

        day4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateChanged(1);
            }
        });

        day5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateChanged(2);
            }
        });

        updateData();
    }

    private void updateCalendar(String text, Date date){
        int x = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        switch (text){
            case "Days":
                x = Calendar.DATE;
                break;
            case "Weeks":
                cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                x = Calendar.WEEK_OF_MONTH;
                break;
            case "Months":
                sdf = new SimpleDateFormat("MMM");
                cal.set(Calendar.DAY_OF_MONTH, 1);
                x = Calendar.MONTH;
                break;
        }
        cal.add(x, -2);
        day1.setText(sdf.format(cal.getTime()));
        cal.add(x, 1);
        day2.setText(sdf.format(cal.getTime()));
        cal.add(x, 1);
        day3.setText(sdf.format(cal.getTime()));
        cal.add(x, 1);
        day4.setText(sdf.format(cal.getTime()));
        cal.add(x, 1);
        day5.setText(sdf.format(cal.getTime()));
    }

    private void dateChanged(int x){
        String text = spinner.getSelectedItem().toString();
        switch (text){
            case "Days":
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.DATE, x);
                updateCalendar(text, cal.getTime());
                date = cal.getTime();
                break;
            case "Weeks":
                cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.WEEK_OF_MONTH, x);
                updateCalendar(text, cal.getTime());
                date = cal.getTime();
                break;
            case "Months":
                cal = Calendar.getInstance();
                cal.setTime(date);
                cal.add(Calendar.MONTH, x);
                updateCalendar(text, cal.getTime());
                date = cal.getTime();
                break;
        }
        updateData();
    }

    private void updateData(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String strDate = formatter.format(cal.getTime());
        Spinner spinner = getView().findViewById(R.id.spinnerDatePeriod);
        String text = spinner.getSelectedItem().toString();
        int x = 0;
        switch (text) {
            case "Days":
                x = 1;
                break;
            case "Weeks":
                x = 7;
                break;
            case "Months":
                x = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                break;
        }
        dataDisplayFragment.updateDataDisplay(date,x);
//        int finalX = x;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                dataDisplayFragment.updateDataDisplay(date, finalX);
//            }
//        }).start();
    }

}
