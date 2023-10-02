package com.example.workoutapp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TimePicker;

import java.util.Calendar;

public class settingsFragment extends Fragment {

    private RadioGroup heightRadioGroup, weightRadioGroup;

    private static final int MY_PERMISSIONS_REQUEST_SCHEDULE_EXACT_ALARM = 123;
    private static final String SCHEDULE_EXACT_ALARM = "android.permission.SCHEDULE_EXACT_ALARM";


    private RadioButton heightRadioCM, heightRadioIN, weightRadioKG, weightRadioLB;

    private EditText heightCm, heightFt, heightIn, weight;

    private Switch notifcationSwitch;

    private TimePicker notificationTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        heightRadioGroup = getView().findViewById(R.id.radio_group_height);
        weightRadioGroup = getView().findViewById(R.id.radio_group_goal_weight);

        heightRadioCM = getView().findViewById(R.id.radio_cm);
        heightRadioIN = getView().findViewById(R.id.radio_feet_inches);

        weightRadioKG = getView().findViewById(R.id.radio_kg);
        weightRadioLB = getView().findViewById(R.id.radio_lb);

        heightCm = getView().findViewById(R.id.edit_text_height_cm);
        heightFt = getView().findViewById(R.id.edit_text_height_feet);
        heightIn = getView().findViewById(R.id.edit_text_height_inches);

        weight = getView().findViewById(R.id.edit_text_goal_weight);

        notifcationSwitch = getView().findViewById(R.id.notification_switch);
        notificationTime = getView().findViewById(R.id.notification_time_picker);

        boolean heightMetric = Database.getDBHandler().getHeightUnits().equals("cm");
        boolean weightMetric = Database.getDBHandler().getWeightUnits().equals("kg");
        if(heightMetric){
            heightRadioCM.setChecked(true);
            heightCm.setText(Database.getDBHandler().getHeight());
            heightFt.setVisibility(View.GONE);
            heightIn.setVisibility(View.GONE);
            heightFt.setText("");
            heightIn.setText("");
        } else {
            heightRadioIN.setChecked(true);
            int inches = Integer.parseInt(Database.getDBHandler().getHeight());
            heightFt.setText(String.valueOf(inches / 12));
            heightIn.setText(String.valueOf(inches % 12));
            heightCm.setVisibility(View.GONE);
            heightFt.setVisibility(View.VISIBLE);
            heightIn.setVisibility(View.VISIBLE);
            heightCm.setText("");
        }
        if(weightMetric){
            weightRadioKG.setChecked(true);
            weight.setText(Database.getDBHandler().getGoalWeight());
        } else {
            weightRadioLB.setChecked(true);
            weight.setText(Database.getDBHandler().getGoalWeight());
        }

        // set notification time
        String oldTime = Database.dbHandler.getReminderTime();
        String[] timeSplit = oldTime.split(":");
        notificationTime.setHour(Integer.parseInt(timeSplit[0]));
        notificationTime.setMinute(Integer.parseInt(timeSplit[1]));
        if(Database.dbHandler.getReminderEnabled()){
            notifcationSwitch.setChecked(true);
        } else {
            notifcationSwitch.setChecked(false);
        }

        heightRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId == R.id.radio_cm){
                heightCm.setVisibility(View.VISIBLE);
                heightFt.setVisibility(View.GONE);
                heightIn.setVisibility(View.GONE);
                if(!heightFt.getText().toString().equals("")){
                    int inches = Integer.parseInt(heightFt.getText().toString()) * 12;
                    if(!heightIn.getText().toString().equals("")){
                        inches += Integer.parseInt(heightIn.getText().toString());
                    }
                    int cm = (int) ((inches * 2.54) + .5);
                    heightCm.setText(String.valueOf(cm));
                }
                heightFt.setText("");
                heightIn.setText("");
            } else {
                heightCm.setVisibility(View.GONE);
                heightFt.setVisibility(View.VISIBLE);
                heightIn.setVisibility(View.VISIBLE);
                if(!heightCm.getText().toString().equals("")){
                    int cm = Integer.parseInt(heightCm.getText().toString());
                    int inches = (int) ((cm / 2.54) + .5);
                    heightFt.setText(String.valueOf(inches / 12));
                    heightIn.setText(String.valueOf(inches % 12));
                }
                heightCm.setText("");
            }
        });

        weightRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if(checkedId == R.id.radio_kg){
                if(!weight.getText().toString().equals("")){
                    int lb = Integer.parseInt(weight.getText().toString());
                    int kg = (int) ((lb / 2.205) + .5);
                    weight.setText(String.valueOf(kg));
                }
            } else {
                if(!weight.getText().toString().equals("")){
                    int kg = Integer.parseInt(weight.getText().toString());
                    int lb = (int) ((kg * 2.205) + .5);
                    weight.setText(String.valueOf(lb));
                }
            }
        });

        // when height ft and inches are changed update database
        heightFt.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                if(!heightFt.getText().toString().equals("") && !heightIn.getText().toString().equals("")){
                    int inches = Integer.parseInt(heightFt.getText().toString()) * 12;
                    inches += Integer.parseInt(heightIn.getText().toString());
                    Database.getDBHandler().updateData(Database.getDBHandler().getFirstName(), Database.dbHandler.getLastName(), String.valueOf(inches), "false", Database.getDBHandler().getWeight(), Database.getDBHandler().getWeightMetric(), Database.getDBHandler().getGoalWeight(), Database.getDBHandler().getGoalWeightMetric());
                }
            }
        });
        heightIn.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                if(!heightFt.getText().toString().equals("") && !heightIn.getText().toString().equals("")){
                    int inches = Integer.parseInt(heightFt.getText().toString()) * 12;
                    inches += Integer.parseInt(heightIn.getText().toString());
                    Database.getDBHandler().updateData(Database.getDBHandler().getFirstName(), Database.dbHandler.getLastName(), String.valueOf(inches), "false", Database.getDBHandler().getWeight(), Database.getDBHandler().getWeightMetric(), Database.getDBHandler().getGoalWeight(), Database.getDBHandler().getGoalWeightMetric());
                }
            }
        });

        heightCm.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                if(!heightCm.getText().toString().equals("")){
                    int cm = Integer.parseInt(heightCm.getText().toString());
                    Database.getDBHandler().updateData(Database.getDBHandler().getFirstName(), Database.dbHandler.getLastName(), String.valueOf(cm), "true", Database.getDBHandler().getWeight(), Database.getDBHandler().getWeightMetric(), Database.getDBHandler().getGoalWeight(), Database.getDBHandler().getGoalWeightMetric());
                }
            }
        });

        weight.setOnEditorActionListener((v, actionId, event) -> {
            if(!weight.getText().toString().equals("")){
                if(weightRadioGroup.getCheckedRadioButtonId() == R.id.radio_kg){
                    Database.getDBHandler().updateData(Database.getDBHandler().getFirstName(), Database.dbHandler.getLastName(), Database.getDBHandler().getHeight(), Database.getDBHandler().getHeightMetric(), Database.getDBHandler().getWeight(), Database.getDBHandler().getWeightMetric(),weight.getText().toString(), "true");
                } else {
                    Database.getDBHandler().updateData(Database.getDBHandler().getFirstName(), Database.dbHandler.getLastName(), Database.getDBHandler().getHeight(), Database.getDBHandler().getHeightMetric(), Database.getDBHandler().getWeight(), Database.getDBHandler().getWeightMetric(), weight.getText().toString(), "false");
                }
            }
            return false;
        });

        notificationTime.setOnTimeChangedListener((view1, hourOfDay, minute) -> {
            String time = String.format("%02d:%02d", hourOfDay, minute);
            System.out.println(time);
            String bool = "false";
            if(notifcationSwitch.isChecked()){
                bool = "true";
            }
            Database.dbHandler.updateReminderData(bool,time);
            scheduleNotification();
        });

        notifcationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String bool = "false";
            if(isChecked){
                bool = "true";
                scheduleNotification();
            } else {
                cancelNotificationAlarm();
            }
            Database.dbHandler.updateReminderData(bool,Database.dbHandler.getReminderTime());
        });

    }
    private void cancelNotificationAlarm() {
        Intent intent = new Intent(getContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getContext(),
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        // Cancel the alarm
        alarmManager.cancel(pendingIntent);

        // Remove any existing notifications
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    private void scheduleNotification() {
        int hour = notificationTime.getHour();
        int minute = notificationTime.getMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        Intent intent = new Intent(getContext(), NotificationReceiver.class);
        intent.putExtra("message", "Log in your workout today!");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getContext(),
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            } else {
            }
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }








}
