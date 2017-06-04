package com.example.thehanged.habittracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.thehanged.habittracker.data.habitContract.habitEntry;

/**
 * Database helper for habits app. Manages database creation and version management.
 */
public class habitDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = habitDbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "shelter.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link habitDbHelper}.
     *
     * @param context of the app
     */
    public habitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the habits table
        String SQL_CREATE_habitS_TABLE = "CREATE TABLE " + habitEntry.TABLE_NAME + " ("
                + habitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + habitEntry.COLUMN_habit_NAME + " TEXT NOT NULL, "
                + habitEntry.COLUMN_place + " TEXT, "
                + habitEntry.COLUMN_feedback + " INTEGER NOT NULL, "
                + habitEntry.COLUMN_times + " INTEGER NOT NULL DEFAULT 0);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_habitS_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}