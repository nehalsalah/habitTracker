package com.example.thehanged.habittracker.data;


import android.provider.BaseColumns;

/**
 * API Contract for the habits tracker app.
 */
public final class habitContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private habitContract() {
    }

    /**
     * Inner class that defines constant values for the habits database table.
     * Each entry in the table represents a single habit.
     */
    public static final class habitEntry implements BaseColumns {

        /**
         * Name of database table for habits
         */
        public final static String TABLE_NAME = "habits";

        /**
         * Unique ID number for the habit (only for use in the database table).
         * <p>
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the habit.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_habit_NAME = "name";

        /**
         * place of the habit.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_place = "place";

        /**
         * feedback for habit.
         * <p>
         * The only possible values are {@link #normal}, {@link #bad},
         * or {@link #beautiful}.
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_feedback = "feedback";

        /**
         * habits times per day.
         * <p>
         * Type: INTEGER
         */
        public final static String COLUMN_times = "times";

        /**
         * Possible values for feedback .
         */
        public static final int normal = 0;
        public static final int bad = 1;
        public static final int beautiful = 2;
    }

}

