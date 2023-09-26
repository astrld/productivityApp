package com.example.workoutapp;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

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

    public static void updateDataDisplay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String dateStr = sdf.format(cal.getTime());
        int chest = Database.getDBHandler().getChest(dateStr);
        int back = Database.getDBHandler().getBack(dateStr);
        int abs = Database.getDBHandler().getAbs(dateStr);
        int shoulders = Database.getDBHandler().getShoulders(dateStr);
        int triceps = Database.getDBHandler().getTriceps(dateStr);
        int biceps = Database.getDBHandler().getBiceps(dateStr);
        int legs = Database.getDBHandler().getLegs(dateStr);
        int cardio = Database.getDBHandler().getCardio(dateStr);
        System.out.println("Chest: " + chest + "\n" + "Back: " + back + "\n" + "Abs: " + abs + "\n" + "Shoulders: " + shoulders + "\n" + "Triceps: " + triceps + "\n" + "Biceps: " + biceps + "\n" + "Legs: " + legs + "\n" + "Cardio: " + cardio + "\n");
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, chest / 60)); // Convert seconds to minutes
        entries.add(new BarEntry(1, back / 60));
        entries.add(new BarEntry(2, abs / 60));
        entries.add(new BarEntry(3, shoulders / 60));
        entries.add(new BarEntry(4, triceps / 60));
        entries.add(new BarEntry(5, biceps / 60));
        entries.add(new BarEntry(6, legs / 60));
        entries.add(new BarEntry(7, cardio / 60));
        // Add entries for other muscle groups

        // Create a BarDataSet with your entries
        BarDataSet dataSet = new BarDataSet(entries, "");
        // make R.color.blue2 the color of the bars
        dataSet.setColor(blue4color); // Customize the color

        // Create a BarData object and set the BarDataSet
        BarData barData = new BarData(dataSet);

        // Set the data to your BarChart
        barChart.setData(barData);

        // Customize the appearance of the BarChart
        barChart.getDescription().setEnabled(false);
        barChart.setDrawValueAboveBar(true);

        // Customize the X-axis (muscle groups)
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter()); // Implement this formatter
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(entries.size());

        // Customize the Y-axis (minutes)
        YAxis yAxisLeft = barChart.getAxisLeft();
        YAxis yAxisRight = barChart.getAxisRight();
        yAxisLeft.setAxisMinimum(0f); // Start from 0
        yAxisLeft.setAxisMaximum(120f); // 2 hours (adjust as needed)
        yAxisRight.setAxisMinimum(0f);
        yAxisRight.setAxisMaximum(120f);
        yAxisLeft.setValueFormatter(new MyYAxisValueFormatter()); // Implement this formatter
        yAxisRight.setValueFormatter(new MyYAxisValueFormatter());

        // Refresh the chart
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
