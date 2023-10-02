package com.example.workoutapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DBHandler extends SQLiteOpenHelper{

    private static final String DB_Name = "userData";

    private static final int DB_VERSION = 10; // highest has been 5

    private static final String TABLE_NAME = "startData";

    private static final String STOPWATCH_TABLE_NAME = "stopwatchData";

    private static final String FIRST_NAME_COL = "firstName";

    private static final String LAST_NAME_COL = "lastName";

    private static final String HEIGHT_COL = "heightCol";

    private static final String HEIGHT_METRIC_COL = "heightMetricCol";

    private static final String WEIGHT_COL = "weightCol";

    private static final String WEIGHT_METRIC_COL = "weightMetricCol";

    private static final String GOAL_WEIGHT_COL = "goalWeightCol";

    private static final String GOAL_WEIGHT_METRIC_COL = "goalWeightMetricCol";

    private static final String STOPWATCH_SECONDS_COL = "stopwatchSecondsCol";
    private static final String STOPWATCH_MUSCLE_GROUP_COL = "stopwatchMuscleGroupCol";
    private static final String STOPWATCH_CLOSING_TIME_COL = "stopwatchClosingTimeCol";

    public static final String DATA_TABLE_NAME = "dataTable";
    public static final String DATA_DATE_COL = "dataDateCol";
    public static final String DATA_WEIGHT_COL = "dataWeightCol";

    public static final String DATA_CHEST_COL = "dataChestCol";
    public static final String DATA_BACK_COL = "dataBackCol";
    public static final String DATA_ABS_COL = "dataAbsCol";
    public static final String DATA_SHOULDERS_COL = "dataShouldersCol";
    public static final String DATA_TRICEPS_COL = "dataTricepsCol";
    public static final String DATA_BICEPS_COL = "dataBicepsCol";
    public static final String DATA_LEGS_COL = "dataLegsCol";
    public static final String DATA_CARDIO_COL = "dataCardioCol";

    public static final String REMINDER_TABLE_NAME = "reminderTable";
    public static final String REMINDER_ENABLED_COL = "reminderEnabledCol";
    public static final String REMINDER_TIME_COL = "reminderTimeCol";




    public DBHandler(Context context) {
        super(context, DB_Name, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String startData = "CREATE TABLE " + TABLE_NAME + " ("
                + FIRST_NAME_COL + " TEXT,"
                + LAST_NAME_COL + " TEXT,"
                + HEIGHT_COL + " TEXT,"
                + HEIGHT_METRIC_COL + " TEXT,"
                + WEIGHT_COL + " TEXT,"
                + WEIGHT_METRIC_COL + " TEXT,"
                + GOAL_WEIGHT_COL + " TEXT,"
                + GOAL_WEIGHT_METRIC_COL + " TEXT)";
        db.execSQL(startData);

        String stopwatchQuery = "CREATE TABLE " + STOPWATCH_TABLE_NAME + " ("
                + STOPWATCH_SECONDS_COL + " TEXT,"
                + STOPWATCH_MUSCLE_GROUP_COL + " TEXT,"
                + STOPWATCH_CLOSING_TIME_COL + " TEXT)";
        db.execSQL(stopwatchQuery);

        String data = "CREATE TABLE " + DATA_TABLE_NAME + " ("
                + DATA_DATE_COL + " TEXT,"
                + DATA_WEIGHT_COL + " INTEGER,"
                + DATA_CHEST_COL + " INTEGER,"
                + DATA_BACK_COL + " INTEGER,"
                + DATA_ABS_COL + " INTEGER,"
                + DATA_SHOULDERS_COL + " INTEGER,"
                + DATA_TRICEPS_COL + " INTEGER,"
                + DATA_BICEPS_COL + " INTEGER,"
                + DATA_LEGS_COL + " INTEGER,"
                + DATA_CARDIO_COL + " INTEGER)";
        db.execSQL(data);

        String reminderEnabled = "CREATE TABLE " + REMINDER_TABLE_NAME + " ("
                + REMINDER_ENABLED_COL + " TEXT,"
                + REMINDER_TIME_COL + " TEXT)";
        db.execSQL(reminderEnabled);
    }

    public void addReminderData(String enabled, String time){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(REMINDER_ENABLED_COL, enabled);
        values.put(REMINDER_TIME_COL, time);

        db.insert(REMINDER_TABLE_NAME, null, values);
        db.close();
    }

    public void updateReminderData(String enabled, String time){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(REMINDER_ENABLED_COL, enabled);
        values.put(REMINDER_TIME_COL, time);

        db.update(REMINDER_TABLE_NAME, values, null, null);
        db.close();
    }

    public boolean checkIfReminderExists(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + REMINDER_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public String getReminderTime(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + REMINDER_TIME_COL + " FROM " + REMINDER_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String time = "";
        if (cursor.moveToFirst()) {
            time = cursor.getString(0);
        }
        cursor.close();
        return time;
    }

    public boolean getReminderEnabled(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + REMINDER_ENABLED_COL + " FROM " + REMINDER_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String enabled = "";
        if (cursor.moveToFirst()) {
            enabled = cursor.getString(0);
        }
        cursor.close();
        if(enabled.equals("true")){
            return true;
        }
        return false;
    }

    public void addDataToStart(String firstName, String lastName, String height, String heightMetric, String weight, String weightMetric, String goalWeight, String goalWeightMetric) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FIRST_NAME_COL, firstName);
        values.put(LAST_NAME_COL, lastName);
        values.put(HEIGHT_COL, height);
        values.put(HEIGHT_METRIC_COL, heightMetric);
        values.put(WEIGHT_COL, weight);
        values.put(WEIGHT_METRIC_COL, weightMetric);
        values.put(GOAL_WEIGHT_COL, goalWeight);
        values.put(GOAL_WEIGHT_METRIC_COL, goalWeightMetric);

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    public void updateData(String firstName, String lastName, String height, String heightMetric, String weight, String weightMetric, String goalWeight, String goalWeightMetric) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(FIRST_NAME_COL, firstName);
        values.put(LAST_NAME_COL, lastName);
        values.put(HEIGHT_COL, height);
        values.put(HEIGHT_METRIC_COL, heightMetric);
        values.put(WEIGHT_COL, weight);
        values.put(WEIGHT_METRIC_COL, weightMetric);
        values.put(GOAL_WEIGHT_COL, goalWeight);
        values.put(GOAL_WEIGHT_METRIC_COL, goalWeightMetric);

        db.update(TABLE_NAME, values, null, null);
        db.close();
    }

    public void addStopwatchData(String seconds, String muscleGroup, String closingTime){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(STOPWATCH_SECONDS_COL, seconds);
        values.put(STOPWATCH_MUSCLE_GROUP_COL, muscleGroup);
        values.put(STOPWATCH_CLOSING_TIME_COL, closingTime);

        db.insert(STOPWATCH_TABLE_NAME, null, values);
        db.close();
    }

    public boolean ifStopwatchExists(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + STOPWATCH_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void updateStopwatchData(String seconds, String muscleGroup, String closingTime){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(STOPWATCH_SECONDS_COL, seconds);
        values.put(STOPWATCH_MUSCLE_GROUP_COL, muscleGroup);
        values.put(STOPWATCH_CLOSING_TIME_COL, closingTime);

        db.update(STOPWATCH_TABLE_NAME, values, null, null);
        db.close();
    }

    public void ifDayExists(String date, double weight, int chest, int back, int abs, int shoulders, int triceps, int biceps, int legs, int cardio){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DATA_TABLE_NAME + " WHERE " + DATA_DATE_COL + " = '" + date + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            newDay(date, weight, chest, back, abs, shoulders, triceps, biceps, legs, cardio);
        } else {
            updateDay(date, weight, chest, back, abs, shoulders, triceps, biceps, legs, cardio);
        }
        cursor.close();
    }

    public void newDay(String date, double weight, int chest, int back, int abs, int shoulders, int triceps, int biceps, int legs, int cardio){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DATA_DATE_COL, date);
        values.put(DATA_WEIGHT_COL, weight);
        values.put(DATA_CHEST_COL, chest);
        values.put(DATA_BACK_COL, back);
        values.put(DATA_ABS_COL, abs);
        values.put(DATA_SHOULDERS_COL, shoulders);
        values.put(DATA_TRICEPS_COL, triceps);
        values.put(DATA_BICEPS_COL, biceps);
        values.put(DATA_LEGS_COL, legs);
        values.put(DATA_CARDIO_COL, cardio);

        db.insert(DATA_TABLE_NAME, null, values);
        System.out.println("Date: " + date + "\n Weight" + weight + "\n Chest: " + chest + "\n Back: " + back + "\n Abs: " + abs + "\n Shoulders: " + shoulders + "\n Triceps: " + triceps + "\n Biceps: " + biceps + "\n Legs: " + legs + "\n Cardio: " + cardio + "\n");
        db.close();
    }

    public void updateDay(String date, double weight, int chest, int back, int abs, int shoulders, int triceps, int biceps, int legs, int cardio){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (chest == 0){
            chest = getChest(date);
        } else {
            chest += getChest(date);
        }
        if (back == 0){
            back = getBack(date);
        } else {
            back += getBack(date);
        }
        if (abs == 0){
            abs = getAbs(date);
        } else {
            abs += getAbs(date);
        }
        if (shoulders == 0){
            shoulders = getShoulders(date);
        } else {
            shoulders += getShoulders(date);
        }
        if (triceps == 0){
            triceps = getTriceps(date);
        } else {
            triceps += getTriceps(date);
        }
        if (biceps == 0){
            biceps = getBiceps(date);
        } else {
            biceps += getBiceps(date);
        }
        if (legs == 0){
            legs = getLegs(date);
        } else {
            legs += getLegs(date);
        }
        if (cardio == 0){
            cardio = getCardio(date);
        } else {
            cardio += getCardio(date);
        }
        values.put(DATA_DATE_COL, date);
        values.put(DATA_WEIGHT_COL, weight);
        values.put(DATA_CHEST_COL, chest);
        values.put(DATA_BACK_COL, back);
        values.put(DATA_ABS_COL, abs);
        values.put(DATA_SHOULDERS_COL, shoulders);
        values.put(DATA_TRICEPS_COL, triceps);
        values.put(DATA_BICEPS_COL, biceps);
        values.put(DATA_LEGS_COL, legs);
        values.put(DATA_CARDIO_COL, cardio);
        db.update(DATA_TABLE_NAME, values, DATA_DATE_COL + " = ?", new String[]{date});
        System.out.println("UPDATEDDD Date: " + date + "\n Weight" + weight + "\n Chest: " + chest + "\n Back: " + back + "\n Abs: " + abs + "\n Shoulders: " + shoulders + "\n Triceps: " + triceps + "\n Biceps: " + biceps + "\n Legs: " + legs + "\n Cardio: " + cardio + "\n");
        db.close();
    }

    public int getChest(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + DATA_CHEST_COL + " FROM " + DATA_TABLE_NAME + " WHERE " + DATA_DATE_COL + " = '" + date + "'";
        Cursor cursor = db.rawQuery(query, null);
        int chest = 0;
        if (cursor.moveToFirst()) {
            chest = Integer.parseInt(cursor.getString(0));
        }
        cursor.close();
        return chest;
    }

    public int getBack(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + DATA_BACK_COL + " FROM " + DATA_TABLE_NAME + " WHERE " + DATA_DATE_COL + " = '" + date + "'";
        Cursor cursor = db.rawQuery(query, null);
        int back = 0;
        if (cursor.moveToFirst()) {
            back = Integer.parseInt(cursor.getString(0));
        }
        cursor.close();
        return back;
    }

    public int getAbs(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + DATA_ABS_COL + " FROM " + DATA_TABLE_NAME + " WHERE " + DATA_DATE_COL + " = '" + date + "'";
        Cursor cursor = db.rawQuery(query, null);
        int abs = 0;
        if (cursor.moveToFirst()) {
            abs = Integer.parseInt(cursor.getString(0));
        }
        cursor.close();
        return abs;
    }

    public int getShoulders(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + DATA_SHOULDERS_COL + " FROM " + DATA_TABLE_NAME + " WHERE " + DATA_DATE_COL + " = '" + date + "'";
        Cursor cursor = db.rawQuery(query, null);
        int shoulders = 0;
        if (cursor.moveToFirst()) {
            shoulders = Integer.parseInt(cursor.getString(0));
        }
        cursor.close();
        return shoulders;
    }

    public int getTriceps(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + DATA_TRICEPS_COL + " FROM " + DATA_TABLE_NAME + " WHERE " + DATA_DATE_COL + " = '" + date + "'";
        Cursor cursor = db.rawQuery(query, null);
        int triceps = 0;
        if (cursor.moveToFirst()) {
            triceps = Integer.parseInt(cursor.getString(0));
        }
        cursor.close();
        return triceps;
    }

    public int getBiceps(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + DATA_BICEPS_COL + " FROM " + DATA_TABLE_NAME + " WHERE " + DATA_DATE_COL + " = '" + date + "'";
        Cursor cursor = db.rawQuery(query, null);
        int biceps = 0;
        if (cursor.moveToFirst()) {
            biceps = Integer.parseInt(cursor.getString(0));
        }
        cursor.close();
        return biceps;
    }

    public int getLegs(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + DATA_LEGS_COL + " FROM " + DATA_TABLE_NAME + " WHERE " + DATA_DATE_COL + " = '" + date + "'";
        Cursor cursor = db.rawQuery(query, null);
        int legs = 0;
        if (cursor.moveToFirst()) {
            legs = Integer.parseInt(cursor.getString(0));
        }
        cursor.close();
        return legs;
    }

    public int getCardio(String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + DATA_CARDIO_COL + " FROM " + DATA_TABLE_NAME + " WHERE " + DATA_DATE_COL + " = '" + date + "'";
        Cursor cursor = db.rawQuery(query, null);
        int cardio = 0;
        if (cursor.moveToFirst()) {
            cardio = Integer.parseInt(cursor.getString(0));
        }
        cursor.close();
        return cardio;
    }

    public int getStopWatchSeconds(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + STOPWATCH_SECONDS_COL + " FROM " + STOPWATCH_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        int seconds = 0;
        if (cursor.moveToFirst()) {
            seconds = Integer.parseInt(cursor.getString(0));
        }
        cursor.close();
        return seconds;
    }

    public String getStopWatchMuscleGroup(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + STOPWATCH_MUSCLE_GROUP_COL + " FROM " + STOPWATCH_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String muscleGroup = "";
        if (cursor.moveToFirst()) {
            muscleGroup = cursor.getString(0);
        }
        cursor.close();
        return muscleGroup;
    }

    public long getStopWatchClosingTime(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + STOPWATCH_CLOSING_TIME_COL + " FROM " + STOPWATCH_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        long closingTime = 0;
        if (cursor.moveToFirst()) {
            closingTime = Long.parseLong(cursor.getString(0));
        }
        cursor.close();
        return closingTime;
    }

    public boolean getIfStopWatchExists(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + STOPWATCH_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }



    public String getFirstName() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + FIRST_NAME_COL + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String firstName = "";
        if (cursor.moveToFirst()) {
            firstName = cursor.getString(0);
        }
        cursor.close();
        return firstName;
    }

    public String getLastName() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + LAST_NAME_COL + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String lastName = "";
        if (cursor.moveToFirst()) {
            lastName = cursor.getString(0);
        }
        cursor.close();
        return lastName;
    }

    public String getHeight() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + HEIGHT_COL + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String height = "";
        if (cursor.moveToFirst()) {
            height = cursor.getString(0);
        }
        cursor.close();
        return height;
    }

    public String getHeightMetric() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + HEIGHT_METRIC_COL + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String heightMetric = "";
        if (cursor.moveToFirst()) {
            heightMetric = cursor.getString(0);
        }
        cursor.close();
        return heightMetric;
    }

    public String getWeight() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + WEIGHT_COL + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String weight = "";
        if (cursor.moveToFirst()) {
            weight = cursor.getString(0);
        }
        cursor.close();
        return weight;
    }

    public String getCurrentWeight(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = sdf.format(date);

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + DATA_WEIGHT_COL + " FROM " + DATA_TABLE_NAME + " WHERE " + DATA_DATE_COL + " <= ? ORDER BY " + DATA_DATE_COL + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{dateString});

        String weight = "";
        double weightDouble = 0;

        if (cursor.moveToFirst()) {
            weight = cursor.getString(0);
            weightDouble = Double.parseDouble(weight);
        }

        while (weightDouble == 0) {
            if (cursor.moveToNext()) {
                weight = cursor.getString(0);
                weightDouble = Double.parseDouble(weight);
            } else {
                // Get it from the start data
                weight = getWeight();
                weightDouble = Double.parseDouble(weight);
            }
        }

        weight = String.valueOf(weightDouble);
        cursor.close();
        return weight;
    }


    public String getWeightGivenDate(Date date){
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = sdf.format(date);
        String query = "SELECT " + DATA_WEIGHT_COL + " FROM " + DATA_TABLE_NAME + " WHERE " + DATA_DATE_COL + " = '" + dateString + "'";
        Cursor cursor = db.rawQuery(query, null);
        String weight = "";
        if (cursor.moveToFirst()) {
            weight = cursor.getString(0);
        }
        cursor.close();
        if(weight.equals("") || weight.equals("0") || weight.equals("0.0") || weight.equals("0.00")){
            weight = getCurrentWeight(date);
        }
        return weight;
    }

    public String getWeightMetric() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + WEIGHT_METRIC_COL + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String weightMetric = "";
        if (cursor.moveToFirst()) {
            weightMetric = cursor.getString(0);
        }
        cursor.close();
        return weightMetric;
    }

    public String getWeightUnits(){
        boolean metric = Boolean.parseBoolean(getWeightMetric());
        if(metric){
            return "kg";
        } else {
            return "lbs";
        }
    }

    public String getHeightUnits(){
        boolean metric = Boolean.parseBoolean(getHeightMetric());
        if(metric){
            return "cm";
        } else {
            return "in";
        }
    }

    public double getBMI(double weight){
        boolean weightmetric = Boolean.parseBoolean(getWeightMetric());
        boolean heightmetric = Boolean.parseBoolean(getHeightMetric());
        double bmi = 0;
        double weightMetric = 0;
        double heightMetric = 0;
        double height = Double.parseDouble(getHeight());
        if(weightmetric){
            weightMetric = weight;
        } else {
            weightMetric = weight * .453592;
        }
        if(heightmetric){
            heightMetric = height * .01;
        } else {
            heightMetric = height * 0.0254;
        }
        bmi = weightMetric / (heightMetric * heightMetric);
        return bmi;
    }

    public String getGoalWeight() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + GOAL_WEIGHT_COL + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String goalWeight = "";
        if (cursor.moveToFirst()) {
            goalWeight = cursor.getString(0);
        }
        cursor.close();
        return goalWeight;
    }

    public String getGoalWeightMetric() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + GOAL_WEIGHT_METRIC_COL + " FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String goalWeightMetric = "";
        if (cursor.moveToFirst()) {
            goalWeightMetric = cursor.getString(0);
        }
        cursor.close();
        return goalWeightMetric;
    }


    public boolean checkEmpty() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public boolean allFilled(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            if(!cursor.getString(0).equals("") && !cursor.getString(1).equals("") && !cursor.getString(2).equals("") && !cursor.getString(3).equals("") && !cursor.getString(4).equals("") && !cursor.getString(5).equals("") && !cursor.getString(6).equals("") && !cursor.getString(7).equals("")){
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}