package com.example.workoutapp;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class stopwatchFragment extends Fragment{

    private ImageView pausePlayButton;
    private ImageView stopButton;

    private TextView timer;
    private TextView muscleGroup;

    private boolean running = false;

    private RecyclerView recyclerView;

    private ZoomCenterLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stopwatch, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pausePlayButton = getView().findViewById(R.id.playpause);
        stopButton = getView().findViewById(R.id.stop);
        if(Database.dbHandler.ifStopwatchExists()){
            Database.dbHandler.addStopwatchData("0", "Chest", "0");
        } else {
            Database.dbHandler.updateStopwatchData("0", "Chest", "0");
        }
        timer = getView().findViewById(R.id.stopwatchTextview);
        recyclerView = getView().findViewById(R.id.recyclerView);

        layoutManager = new ZoomCenterLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        muscleGroup = getView().findViewById(R.id.muscleGroup);

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
        recyclerView.setAdapter(adapter);
        int itemSpacing = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = itemSpacing / 2;
                outRect.right = itemSpacing / 2;
            }
        });
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                int center = layoutManager.getWidth() / 2;
//                View closestChild = null;
//                int closestDistance = Integer.MAX_VALUE;
//                for (int i = 0; i < layoutManager.getChildCount(); i++) {
//                    View child = layoutManager.getChildAt(i);
//                    if(child != null) {
//                        int childCenter = (layoutManager.getDecoratedRight(child) + layoutManager.getDecoratedLeft(child)) / 2;
//                        int distance = Math.abs(center - childCenter);
//                        if (distance < closestDistance) {
//                            closestDistance = distance;
//                            closestChild = child;
//                        }
//                    }
//                }
//                int position = recyclerView.getChildLayoutPosition(closestChild);
//                updateMuscleGroupText(position);
//                if(userScrolled){
//                    running = false;
//                    pausePlayButton.setImageResource(R.drawable.play);
//                    addData();
//                    seconds = 0;
//                    timer.setText("00:00:00");
//                    Database.dbHandler.updateStopwatchData("0", "Chest", "0");
//                }
//            }
//        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState){
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    int position = layoutManager.findFirstVisibleItemPosition() + 1;
                    updateMuscleGroupText(position);
                    if(userScrolled){
                        running = false;
                        pausePlayButton.setImageResource(R.drawable.play);
                        addData();
                        seconds = 0;
                        timer.setText("00:00:00");
                        Database.dbHandler.updateStopwatchData("0", "Chest", "0");
                    }
                }
            }
        });

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        scrollToPosition(2);
        stopwatch();
        pausePlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(running){
                    pausePlayButton.setImageResource(R.drawable.play);
                    running = false;
                } else {
                    pausePlayButton.setImageResource(R.drawable.pause);
                    running = true;
                }
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = false;
                pausePlayButton.setImageResource(R.drawable.play);
                addData();
                seconds = 0;
                timer.setText("00:00:00");
                Database.dbHandler.updateStopwatchData("0", "Chest", "0");
            }
        });
        if(Database.dbHandler.getIfStopWatchExists()){
            seconds = Database.getDBHandler().getStopWatchSeconds();
            if(seconds > 0) {
                String muscleGroupText = Database.getDBHandler().getStopWatchMuscleGroup();
                muscleGroup.setText(muscleGroupText);
                System.out.println("MUSCLE GROUP IS" + muscleGroupText);
                switch (muscleGroupText) {
                    case "Chest":
                        scrollToPosition(2);
                        break;
                    case "Abs":
                        scrollToPosition(3);
                        break;
                    case "Back":
                        scrollToPosition(4);
                        break;
                    case "Shoulders":
                        scrollToPosition(5);
                        break;
                    case "Triceps":
                        scrollToPosition(6);
                        break;
                    case "Biceps":
                        scrollToPosition(7);
                        break;
                    case "Legs":
                        scrollToPosition(8);
                        break;
                    case "Cardio":
                        scrollToPosition(9);
                        break;
                }
                long closingTime = Database.getDBHandler().getStopWatchClosingTime();
                long currentTime = System.currentTimeMillis();
                long difference = currentTime - closingTime;
                seconds = seconds + (int) (difference / 1000);
                running = true;
                pausePlayButton.setImageResource(R.drawable.pause);
            }
        }
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
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
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


    private void updateMuscleGroupText(int position) {
        switch (position) {
            case 1:
                muscleGroup.setText("Chest");
                break;
            case 2:
                muscleGroup.setText("Abs");
                break;
            case 3:
                muscleGroup.setText("Back");
                break;
            case 4:
                muscleGroup.setText("Shoulders");
                break;
            case 5:
                muscleGroup.setText("Triceps");
                break;
            case 6:
                muscleGroup.setText("Biceps");
                break;
            case 7:
                muscleGroup.setText("Legs");
                break;
            case 8:
                muscleGroup.setText("Cardio");
                break;
        }
    }

    private int seconds = 0;

    private boolean stopWatchExists = false;
    private void stopwatch(){
        if(stopWatchExists){
            return;
        }
        final Handler handler = new Handler();
        stopWatchExists = true;
        handler.post(new Runnable() {
            @Override
            public void run() {
                int secs = seconds;
                int hours = secs / 3600;
                secs = secs % 3600;
                int minutes = secs / 60;
                secs = secs % 60;
                if(running){
                    seconds++;
                    long currentTime = System.currentTimeMillis();
                    Database.dbHandler.updateStopwatchData(String.valueOf(seconds), muscleGroup.getText().toString(), String.valueOf(currentTime) );
                }
                String time = String.format("%02d:%02d:%02d", hours, minutes, secs);
                timer.setText(time);
                handler.postDelayed(this, 1000);
            }
        });
    }
    private boolean userScrolled = true;
    private void scrollToPosition(int position) {
        recyclerView.smoothScrollToPosition(position);
        userScrolled = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                userScrolled = true;
            }
        }, 1000);
    }

    private void addData(){
        if(seconds == 0){
            return;
        }
        String muscleGroupText = muscleGroup.getText().toString();
        int chest = 0; int abs = 0; int back = 0; int shoulders = 0; int triceps = 0; int biceps = 0; int legs = 0; int cardio = 0;
        switch (muscleGroupText){
            case "Chest":
                chest = seconds;
                break;
            case "Abs":
                abs = seconds;
                break;
            case "Back":
                back = seconds;
                break;
            case "Shoulders":
                shoulders = seconds;
                break;
            case "Triceps":
                triceps = seconds;
                break;
            case "Biceps":
                biceps = seconds;
                break;
            case "Legs":
                legs = seconds;
                break;
            case "Cardio":
                cardio = seconds;
                break;
        }
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = sdf.format(date);
        Database.dbHandler.ifDayExists(dateString,0,chest, back, abs, shoulders, triceps, biceps, legs, cardio);
    }

}
