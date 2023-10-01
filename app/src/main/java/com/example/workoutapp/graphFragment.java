package com.example.workoutapp;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class graphFragment extends Fragment {
    private LineChart weightLineChart, muscleLineChart;
    public static LineChart staticMuscleLineChart, staticWeightLineChart;

    private graphDisplayFragment GraphDisplayFragment = new graphDisplayFragment();

    public static int blue4color, blue3color, blue2color, blue1color;

    private RelativeLayout graphFragmentRelativeLayout;

    private LinearLayout graphDisplayName;

    private RecyclerView linegraphrecyclerview;
    private static RecyclerView staticLinegraphrecyclerview;

    private ZoomCenterLayoutManager layoutManager;

    private static ZoomCenterLayoutManager staticLayoutManager;

    private static Date date;

    private static String period;

    private static int x;

    private boolean userScrolling = false;

    private int closestChildPosition = 1;


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
        staticWeightLineChart = weightLineChart;
        graphDisplayName = view.findViewById(R.id.graphLL);
        graphFragmentRelativeLayout = view.findViewById(R.id.graphFragmentRelativeLayout);
        blue4color = getResources().getColor(R.color.blue4);
        blue3color = getResources().getColor(R.color.blue3);
        blue2color = getResources().getColor(R.color.blue2);
        blue1color = getResources().getColor(R.color.blue1);
        linegraphrecyclerview = view.findViewById(R.id.linegraphrecyclerview);
        staticLinegraphrecyclerview = linegraphrecyclerview;
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.graphButtonsFrameLayout, GraphDisplayFragment);
        fragmentTransaction.commit();

        layoutManager = new ZoomCenterLayoutManager(getContext());
        staticLayoutManager = layoutManager;
        linegraphrecyclerview.setLayoutManager(layoutManager);
        int[] iconResIds = {
                R.color.beige2,
                R.drawable.chesticon,
                R.drawable.absicon,
                R.drawable.backicon,
                R.drawable.shouldericon,
                R.drawable.tricepicon,
                R.drawable.bicepsicon,
                R.drawable.legsicon,
                R.drawable.cardioicon,
                R.color.beige2
        };
        MyAdapter adapter = new MyAdapter(iconResIds);
        linegraphrecyclerview.setAdapter(adapter);
        int itemSpacing = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        linegraphrecyclerview.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = itemSpacing / 2;
                outRect.right = itemSpacing / 2;
            }
        });
        linegraphrecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int position = layoutManager.findFirstVisibleItemPosition() + 1;
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            updateMuscleGroupGraph(position);
//                        }
//                    }).start();
                    updateMuscleGroupGraph(position);
                }
            }
        });
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(linegraphrecyclerview);
        scrollToPosition(2);

        weightLineChart.setNoDataTextColor(blue4color);
        muscleLineChart.setNoDataTextColor(blue4color);
        weightLineChart.setNoDataText("Loading...");
        muscleLineChart.setNoDataText("Loading...");
    }

    public static void updateGraphs(Date date, String period, int x){
        graphFragment.date= date;
        graphFragment.period = period;
        graphFragment.x = x;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                updateWeightAndBmiGraph();
//                int position =  staticLayoutManager.findFirstVisibleItemPosition() + 1;
//                updateMuscleGroupGraph(position);
//            }
//        }).start();
        updateWeightAndBmiGraph();
        int position =  staticLayoutManager.findFirstVisibleItemPosition() + 1;
        updateMuscleGroupGraph(position);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private int[] iconResIds;

        public MyAdapter(int[] iconResIds) {
            this.iconResIds = iconResIds;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
            int iconResId = iconResIds[position];
            holder.iconImageView.setImageResource(iconResId);
        }

        @Override
        public int getItemCount() {
            return iconResIds.length;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView iconImageView;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                iconImageView = itemView.findViewById(R.id.iconImageView);
            }
        }
    }

    public class ZoomCenterLayoutManager extends LinearLayoutManager {

        private final float mShrinkAmount = .4f;
        private final float mShrinkDistance = .6f;

        public ZoomCenterLayoutManager(Context context) {
            super(context);
            setOrientation(LinearLayoutManager.HORIZONTAL);

        }

        @Override
        public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
            int scrolled = super.scrollHorizontallyBy(dx, recycler, state);
            float midpoint = getWidth() / 2.0f;
            float d0 = 0.0f;
            float d1 = mShrinkDistance * midpoint;
            float s0 = 1.0f;
            float s1 = 1.0f - mShrinkAmount;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child != null) {
                    float childMidpoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2.0f;
                    float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
                    float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
                    if (!Float.isNaN(scale)) {
                        child.setScaleX(scale);
                        child.setScaleY(scale);
                    }
                }
            }
            return scrolled;
        }
    }

    private void scrollToPosition(int position) {
        linegraphrecyclerview.smoothScrollToPosition(position);
    }

    public static void updateWeightAndBmiGraph() {
        Date startDate = null;
        int days = x;
        staticWeightLineChart.clear();
        List<Entry> weightEntries = new ArrayList<>();
        List<Entry> bmiEntries = new ArrayList<>();
        if (period.equals("Weeks")) {
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
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            cal.add(Calendar.DATE, i);
            Date entryDate = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            String formattedDate = sdf.format(entryDate);
            double weightValue = Double.parseDouble(Database.getDBHandler().getWeightGivenDate(entryDate));
            double bmiValue = Database.getDBHandler().getBMI(weightValue);
            if (weightValue > 0) {
                weightEntries.add(new Entry((float) i, (float) weightValue));
                bmiEntries.add(new Entry((float) i, (float) bmiValue));
            }
        }
        LineDataSet weightDataSet = createLineDataSet(blue4color, weightEntries, "Weight");
        LineDataSet bmiDataSet = createLineDataSet(blue1color, bmiEntries, "BMI");
        LineData lineData = new LineData();
        lineData.addDataSet(weightDataSet);
        lineData.addDataSet(bmiDataSet);
        staticWeightLineChart.setData(lineData);
        staticWeightLineChart.getDescription().setEnabled(false);
        staticWeightLineChart.setDrawMarkers(false);
        staticWeightLineChart.invalidate();
    }

    public static void updateMuscleGroupGraph(int position) {
        Date startDate = null;
        int days =x;
        staticMuscleLineChart.clear();
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
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            cal.add(Calendar.DATE, i);
            Date entryDate = cal.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            String formattedDate = sdf.format(entryDate);
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
        LineDataSet dataSet = null;
        switch (position) {
            case 1:
                dataSet = createLineDataSet(blue4color, chestEntries, "Chest");
                break;
            case 2:
                dataSet = createLineDataSet(blue4color, absEntries, "Abs");
                break;
            case 3:
                dataSet = createLineDataSet(blue4color, backEntries, "Back");
                break;
            case 4:
                dataSet = createLineDataSet(blue4color, shouldersEntries, "Shoulders");
                break;
            case 5:
                dataSet = createLineDataSet(blue4color, tricepsEntries, "Triceps");
                break;
            case 6:
                dataSet = createLineDataSet(blue4color, bicepsEntries, "Biceps");
                break;
            case 7:
                dataSet = createLineDataSet(blue4color, legsEntries, "Legs");
                break;
            case 8:
                dataSet = createLineDataSet(blue4color, cardioEntries, "Cardio");
                break;
        }
        LineData lineData = new LineData();
        lineData.addDataSet(dataSet);

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
