package com.example.workoutapp;

import android.os.Bundle;
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

public class calendarDisplayFragment extends Fragment {

    private Spinner spinner;

    private Button day1, day2, day3, day4, day5;

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

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = adapterView.getItemAtPosition(i).toString();
                updateCalendar(text);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });
    }

    private void updateCalendar(String text){
        int x = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d");
        Calendar cal = Calendar.getInstance();
        switch (text){
            case "Days":
                x = Calendar.DATE;
                break;
            case "Weeks":
                x = Calendar.WEEK_OF_MONTH;
                break;
            case "Months":
                sdf = new SimpleDateFormat("MMM");
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

}
