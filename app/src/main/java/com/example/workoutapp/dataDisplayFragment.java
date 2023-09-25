package com.example.workoutapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class dataDisplayFragment extends Fragment {

    private TextView dataDisplayTextview;
    public static TextView staticDataDisplayTextview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data_display, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataDisplayTextview = view.findViewById(R.id.dataDisplayTextview);
        staticDataDisplayTextview = dataDisplayTextview;
    }

    public static void updateDataDisplay(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(date);
        int chest = Database.getDBHandler().getChest(strDate);
        int back = Database.getDBHandler().getBack(strDate);
        int abs = Database.getDBHandler().getAbs(strDate);
        int shoulders = Database.getDBHandler().getShoulders(strDate);
        int triceps = Database.getDBHandler().getTriceps(strDate);
        int biceps = Database.getDBHandler().getBiceps(strDate);
        int legs = Database.getDBHandler().getLegs(strDate);
        int cardio = Database.getDBHandler().getCardio(strDate);
        String data = "Chest: " + chest + "\nBack: " + back + "\nAbs: " + abs + "\nShoulders: " + shoulders + "\nTriceps: " + triceps + "\nBiceps: " + biceps + "\nLegs: " + legs + "\nCardio: " + cardio;
        staticDataDisplayTextview.setText(data);
    }
}
