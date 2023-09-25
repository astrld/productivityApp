package com.example.workoutapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class calendarFragment extends Fragment {

    private calendarDisplayFragment CalendarDisplayFragment = new calendarDisplayFragment();

    private dataDisplayFragment DataDisplayFragment = new dataDisplayFragment();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.calendarContainer, CalendarDisplayFragment);
        fragmentTransaction.commit();

        FragmentTransaction fragmentTransaction2 = getChildFragmentManager().beginTransaction();
        fragmentTransaction2.replace(R.id.dataFrameContainer, DataDisplayFragment);
        fragmentTransaction2.commit();

    }
}
