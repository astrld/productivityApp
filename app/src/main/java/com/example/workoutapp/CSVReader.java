package com.example.workoutapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class CSVReader {
    public static void loadCSVFile(
            @NotNull Context context, @NotNull String fileName, @NotNull SQLiteDatabase database) {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader =
                null;
        try {
            inputStream = context.getAssets().open(fileName);
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);

            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                String date = values[0];
                int weight = Integer.parseInt(values[1]);
                int chest = Integer.parseInt(values[2]);
                int back = Integer.parseInt(values[3]);
                int abs = Integer.parseInt(values[4]);
                int shoulders = Integer.parseInt(values[5]);
                int triceps = Integer.parseInt(values[6]);
                int biceps = Integer.parseInt(values[7]);
                int legs = Integer.parseInt(values[8]);
                int cardio = Integer.parseInt(values[9]);
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHandler.DATA_DATE_COL,date);
                contentValues.put(DBHandler.DATA_WEIGHT_COL, weight);
                contentValues.put(DBHandler.DATA_CHEST_COL, chest);
                contentValues.put(DBHandler.DATA_BACK_COL, back);
                contentValues.put(DBHandler.DATA_ABS_COL, abs);
                contentValues.put(DBHandler.DATA_SHOULDERS_COL, shoulders);
                contentValues.put(DBHandler.DATA_TRICEPS_COL, triceps);
                contentValues.put(DBHandler.DATA_BICEPS_COL, biceps);
                contentValues.put(DBHandler.DATA_LEGS_COL, legs);
                contentValues.put(DBHandler.DATA_CARDIO_COL, cardio);

                database.insert(
                        DBHandler.DATA_TABLE_NAME,
                        null,
                        contentValues);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (reader != null) reader.close();
                if (inputStreamReader != null) inputStreamReader.close();
                if (inputStream != null) inputStream.close();
                if (database != null) database.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String[]> loadCSVFile(@NotNull Context context, @NotNull String fileName) {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader =
                null;
        ArrayList<String[]> lines = new ArrayList<>();
        try {
            inputStream = context.getAssets().open(fileName);
            inputStreamReader = new InputStreamReader(inputStream);
            reader = new BufferedReader(inputStreamReader);

            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                lines.add(values);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (reader != null) reader.close();
                if (inputStreamReader != null) inputStreamReader.close();
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return lines;
    }
}