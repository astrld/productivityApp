package com.example.workoutapp;

public class Database {
    public static DBHandler dbHandler;

    public static void setDBHandler(DBHandler dbHandler) {
        Database.dbHandler = dbHandler;
    }

    public static DBHandler getDBHandler() {
        return Database.dbHandler;
    }
}
