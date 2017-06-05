package com.example.thehanged.habittracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.thehanged.habittracker.data.habitContract.habitEntry;
import com.example.thehanged.habittracker.data.habitDbHelper;

/**
 * Displays list of habits that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {

    /**
     * Database helper that will provide us access to the database
     */
    private habitDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new habitDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */

    public Cursor readAllHabits() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                habitEntry._ID,
                habitEntry.COLUMN_habit_NAME,
                habitEntry.COLUMN_place,
                habitEntry.COLUMN_feedback,
                habitEntry.COLUMN_times};
        // Perform a query on the pets table
        Cursor cursor = db.query(
                habitEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        return cursor;
    }

    private void displayDatabaseInfo() {


        Cursor cursor = readAllHabits();

        TextView displayView = (TextView) findViewById(R.id.text_view_habit);

        try {
            // Create a header in the Text View that looks like this:
            //
            // The habits table contains <number of rows in Cursor> habits.
            // _id - name - place - feedback - times
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.setText("The habits table contains " + cursor.getCount() + " habits.\n\n");
            displayView.append(habitEntry._ID + " - " +
                    habitEntry.COLUMN_habit_NAME + " - " +
                    habitEntry.COLUMN_place + " - " +
                    habitEntry.COLUMN_feedback + " - " +
                    habitEntry.COLUMN_times + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(habitEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(habitEntry.COLUMN_habit_NAME);
            int placeColumnIndex = cursor.getColumnIndex(habitEntry.COLUMN_place);
            int feedbackColumnIndex = cursor.getColumnIndex(habitEntry.COLUMN_feedback);
            int timesColumnIndex = cursor.getColumnIndex(habitEntry.COLUMN_times);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentPlace = cursor.getString(placeColumnIndex);
                int currentFeedback = cursor.getInt(feedbackColumnIndex);
                int currentTimes = cursor.getInt(timesColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPlace + " - " +
                        currentFeedback + " - " +
                        currentTimes));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    /**
     * Helper method to insert hardcoded habit data into the database. For debugging purposes only.
     */
    private void inserthabit() {
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and football's habit attributes are the values.
        ContentValues values = new ContentValues();
        values.put(habitEntry.COLUMN_habit_NAME, "Football");
        values.put(habitEntry.COLUMN_place, "club");
        values.put(habitEntry.COLUMN_feedback, habitEntry.beautiful);
        values.put(habitEntry.COLUMN_times, 1);

        // Insert a new row for football in the database, returning the ID of that new row.
        // The first argument for db.insert() is the habits table name.
        // The second argument provides the name of a column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for football.
        long newRowId = db.insert(habitEntry.TABLE_NAME, null, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert default data" menu option
            case R.id.action_insert_default_data:
                inserthabit();
                displayDatabaseInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
