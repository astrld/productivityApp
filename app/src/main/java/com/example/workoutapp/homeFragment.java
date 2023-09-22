package com.example.workoutapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class homeFragment extends Fragment {

    private TextView displayName;

    private stopwatchFragment StopwatchFragment = new stopwatchFragment();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayName = getView().findViewById(R.id.displayName);
        displayName.setText("Hey, " + Database.getDBHandler().getFirstName());
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.stopwatchContainer, StopwatchFragment);
        fragmentTransaction.commit();
    }

}
