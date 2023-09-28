package com.example.workoutapp;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class graphFragment extends Fragment {
    private LineChart weightLineChart, muscleLineChart;
    public static LineChart staticMuscleLineChart;

    private graphDisplayFragment GraphDisplayFragment = new graphDisplayFragment();

    public static int blue4color;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graph, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        weightLineChart = view.findViewById(R.id.WeightlineChart);
        muscleLineChart = view.findViewById(R.id.muscleGroupLineChart);
        staticMuscleLineChart = muscleLineChart;
        blue4color = getResources().getColor(R.color.blue4);
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.graphButtonsFrameLayout, GraphDisplayFragment);
        fragmentTransaction.commit();

    }

    public static void updateMuscleGroupGraph(Date date, String period, int x){
        Date startDate = null;
        int days =x;
        staticMuscleLineChart.clear();

        // Create entries for each muscle group
        List<Entry> chestEntries = new ArrayList<>();
        List<Entry> backEntries = new ArrayList<>();
        List<Entry> absEntries = new ArrayList<>();
        List<Entry> shouldersEntries = new ArrayList<>();
        List<Entry> tricepsEntries = new ArrayList<>();
        List<Entry> bicepsEntries = new ArrayList<>();
        List<Entry> legsEntries = new ArrayList<>();
        List<Entry> cardioEntries = new ArrayList<>();

        if(period.equals("Weeks")){
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            startDate = cal.getTime();
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            startDate = cal.getTime();
        }
        for (int i = 0; i < days; i++) {
            // Calculate the date for this entry
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            cal.add(Calendar.DATE, i);
            Date entryDate = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            String formattedDate = sdf.format(entryDate);
            Log.d("date!!", formattedDate);
            // Fetch data for each muscle group for this date
            int chestValue = Database.getDBHandler().getChest(formattedDate)/60;
            int backValue = Database.getDBHandler().getBack(formattedDate)/60;
            int absValue = Database.getDBHandler().getAbs(formattedDate)/60;
            int shouldersValue = Database.getDBHandler().getShoulders(formattedDate)/60;
            int tricepsValue = Database.getDBHandler().getTriceps(formattedDate)/60;
            int bicepsValue = Database.getDBHandler().getBiceps(formattedDate)/60;
            int legsValue = Database.getDBHandler().getLegs(formattedDate)/60;
            int cardioValue = Database.getDBHandler().getCardio(formattedDate)/60;

            if (chestValue > 0) {
                chestEntries.add(new Entry(i, chestValue));
            }
            if (backValue > 0) {
                backEntries.add(new Entry(i, backValue));
            }
            if (absValue > 0) {
                absEntries.add(new Entry(i, absValue));
            }
            if (shouldersValue > 0) {
                shouldersEntries.add(new Entry(i, shouldersValue));
            }
            if (tricepsValue > 0) {
                tricepsEntries.add(new Entry(i, tricepsValue));
            }
            if (bicepsValue > 0) {
                bicepsEntries.add(new Entry(i, bicepsValue));
            }
            if (legsValue > 0) {
                legsEntries.add(new Entry(i, legsValue));
            }
            if (cardioValue > 0) {
                cardioEntries.add(new Entry(i, cardioValue));
            }
        }
        int chestColor = Color.parseColor("#FF0000");
        int backColor = Color.parseColor("#FFA500");
        int absColor = Color.parseColor("#FFFF00");
        int shouldersColor = Color.parseColor("#008000");
        int tricepsColor = Color.parseColor("#0000FF");
        int bicepsColor = Color.parseColor("#4B0082");
        int legsColor = Color.parseColor("#EE82EE");
        int cardioColor = Color.parseColor("#FFC0CB");

        LineDataSet chestDataSet = createLineDataSet(cardioColor, chestEntries, "Chest No Trend");
        LineDataSet chestTrendlineDataSet = createTrendlineDataSet(chestColor,chestEntries);
        chestTrendlineDataSet.setLabel("Chest Trend1");
        LineDataSet chestTrendlineDataSet1 = createTrendlineDataSet1(backColor,chestEntries);
        chestTrendlineDataSet1.setLabel("Chest Trend2");
//        LineDataSet backTrendlineDataSet = createTrendlineDataSet(backColor,backEntries);
//        backTrendlineDataSet.setLabel("Back");
//        LineDataSet absTrendlineDataSet = createTrendlineDataSet(absColor,absEntries);
//        absTrendlineDataSet.setLabel("Abs");
//        LineDataSet shouldersTrendlineDataSet = createTrendlineDataSet(shouldersColor,shouldersEntries);
//        shouldersTrendlineDataSet.setLabel("Shoulders");
//        LineDataSet tricepsTrendlineDataSet = createTrendlineDataSet(tricepsColor,tricepsEntries);
//        tricepsTrendlineDataSet.setLabel("Triceps");
//        LineDataSet bicepsTrendlineDataSet = createTrendlineDataSet(bicepsColor,bicepsEntries);
//        bicepsTrendlineDataSet.setLabel("Biceps");
//        LineDataSet legsTrendlineDataSet = createTrendlineDataSet(legsColor,legsEntries);
//        legsTrendlineDataSet.setLabel("Legs");
//        LineDataSet cardioTrendlineDataSet = createTrendlineDataSet(cardioColor,cardioEntries);
//        cardioTrendlineDataSet.setLabel("Cardio");

        LineData lineData = new LineData();
        lineData.addDataSet(chestTrendlineDataSet);
        lineData.addDataSet(chestTrendlineDataSet1);
        lineData.addDataSet(chestDataSet);
//        lineData.addDataSet(backTrendlineDataSet);
//        lineData.addDataSet(absTrendlineDataSet);
//        lineData.addDataSet(shouldersTrendlineDataSet);
//        lineData.addDataSet(tricepsTrendlineDataSet);
//        lineData.addDataSet(bicepsTrendlineDataSet);
//        lineData.addDataSet(legsTrendlineDataSet);
//        lineData.addDataSet(cardioTrendlineDataSet);

        staticMuscleLineChart.setData(lineData);
        staticMuscleLineChart.getDescription().setEnabled(false);

        staticMuscleLineChart.invalidate();
    }

    private static LineDataSet createTrendlineDataSet(int color, List<Entry>... muscleEntries) {
        WeightedObservedPoints obs = new WeightedObservedPoints();
        float maxX = 0f;
        for (List<Entry> entries : muscleEntries) {
            for (Entry entry : entries) {
                obs.add(entry.getX(), entry.getY());
                if (entry.getX() > maxX) {
                    maxX = entry.getX();
                }
            }
        }

        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(2);
        double[] coeff = fitter.fit(obs.toList());
        List<Entry> trendlineEntries = new ArrayList<>();
        for (float x = 0; x <= maxX; x += 1) {
            float y = (float) (coeff[0] + coeff[1] * x + coeff[2] * x * x);
            trendlineEntries.add(new Entry(x, y));
        }

        LineDataSet trendlineDataSet = new LineDataSet(trendlineEntries, "Trendline");
        trendlineDataSet.setColor(color);
        trendlineDataSet.setLineWidth(2f);
        trendlineDataSet.setDrawCircles(false);
        trendlineDataSet.setDrawValues(false);
        return trendlineDataSet;
    }

    private static LineDataSet createTrendlineDataSet1(int color, List<Entry>... muscleEntries) {
        WeightedObservedPoints obs = new WeightedObservedPoints();
        for (List<Entry> entries : muscleEntries) {
            for (Entry entry : entries) {
                obs.add(entry.getX(), entry.getY());
            }
        }
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(1);
        double[] coeff = fitter.fit(obs.toList());
        List<Entry> trendlineEntries = new ArrayList<>();
        for (float x = 0; x <= muscleEntries[0].size(); x += 1) {
            float y = (float) (coeff[0] + coeff[1] * x);
            trendlineEntries.add(new Entry(x, y));
        }
        LineDataSet trendlineDataSet = new LineDataSet(trendlineEntries, "Trendline");
        trendlineDataSet.setColor(color);
        trendlineDataSet.setLineWidth(2f);
        trendlineDataSet.setDrawCircles(false);
        trendlineDataSet.setDrawValues(false);
        return trendlineDataSet;
    }

    private static LineDataSet createLineDataSet(int color, List<Entry> entries, String label) {
        LineDataSet dataSet = new LineDataSet(entries, label);
        dataSet.setDrawCircles(true);
        dataSet.setCircleRadius(4f);
        dataSet.setColor(color);
        dataSet.setLineWidth(2f);
        dataSet.setDrawValues(true);
        return dataSet;
    }


}
