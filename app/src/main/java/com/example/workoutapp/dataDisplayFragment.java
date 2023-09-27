package com.example.workoutapp;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class dataDisplayFragment extends Fragment {

    private TextView dataDisplayTextview;
    public static TextView staticDataDisplayTextview;

    public static String data;

    public static BarChart barChart;

    public static int blue4color;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data_display, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataDisplayTextview = view.findViewById(R.id.dataDisplayTextview);
        staticDataDisplayTextview = dataDisplayTextview;
        dataDisplayTextview.setText(data);

        // Initialize your BarChart
        BarChart barChart = view.findViewById(R.id.barChart);
        dataDisplayFragment.barChart = barChart;
        blue4color = ContextCompat.getColor(getContext(), R.color.blue4);

        // Create a list of BarEntry objects representing your data
    }

    public static void updateDataDisplay(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        if(days == 7){
            // first day of week
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        } else if(days == 30){
            // first day of month
            cal.set(Calendar.DAY_OF_MONTH, 1);
        }
        int chest = 0;
        int back = 0;
        int abs = 0;
        int shoulders = 0;
        int triceps = 0;
        int biceps = 0;
        int legs = 0;
        int cardio = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        for(int i = 0; i < days; i++) {
            String dateStr = sdf.format(cal.getTime());
             chest += Database.getDBHandler().getChest(dateStr);
             back += Database.getDBHandler().getBack(dateStr);
             abs += Database.getDBHandler().getAbs(dateStr);
             shoulders += Database.getDBHandler().getShoulders(dateStr);
             triceps += Database.getDBHandler().getTriceps(dateStr);
             biceps += Database.getDBHandler().getBiceps(dateStr);
             legs += Database.getDBHandler().getLegs(dateStr);
             cardio += Database.getDBHandler().getCardio(dateStr);
            cal.add(Calendar.DATE, 1);
        }
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, chest / 60));
        entries.add(new BarEntry(1, back / 60));
        entries.add(new BarEntry(2, abs / 60));
        entries.add(new BarEntry(3, shoulders / 60));
        entries.add(new BarEntry(4, triceps / 60));
        entries.add(new BarEntry(5, biceps / 60));
        entries.add(new BarEntry(6, legs / 60));
        entries.add(new BarEntry(7, cardio / 60));
        BarDataSet dataSet = new BarDataSet(entries, "");
        dataSet.setColor(blue4color);
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.setDrawValueAboveBar(true);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter());
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(entries.size());
        YAxis yAxisLeft = barChart.getAxisLeft();
        YAxis yAxisRight = barChart.getAxisRight();
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setAxisMaximum(120f * days);
        yAxisLeft.setValueFormatter(new MyYAxisValueFormatter());
        yAxisRight.setEnabled(false);
        barChart.setOnChartValueSelectedListener(new com.github.mikephil.charting.listener.OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, Highlight h) {
                double hoursRounded = Math.round(e.getY() / 60.0 * 100.0) / 100.0;
                String text = "Mins: " + (int) e.getY() + "\nHours: " + hoursRounded;
                staticDataDisplayTextview.setText(text);
            }
            @Override
            public void onNothingSelected() {
                staticDataDisplayTextview.setText("");
            }
        });
        barChart.invalidate();
    }

    public static class MyXAxisValueFormatter extends ValueFormatter {
        private final String[] labels = {
                "Chest", "Back", "Abs", "Shoulders", "Triceps", "Biceps", "Legs", "Cardio"
        };

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            int index = (int) value;
            if (index >= 0 && index < labels.length) {
                return labels[index];
            }
            return "";
        }
    }

    public static class MyYAxisValueFormatter extends ValueFormatter {
        // Implement this formatter to display Y-axis values as minutes
        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            // Convert value (in minutes) to a formatted string, e.g., "30 min"
            return String.format("%d min", (int) value);
        }
    }
}
