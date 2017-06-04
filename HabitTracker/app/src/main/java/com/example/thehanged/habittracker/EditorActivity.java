package com.example.thehanged.habittracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.thehanged.habittracker.data.habitContract.habitEntry;
import com.example.thehanged.habittracker.data.habitDbHelper;

/**
 * Allows user to create a new habit or edit
 */
public class EditorActivity extends AppCompatActivity {

    /**
     * EditText field to enter the habit's name
     */
    private EditText mNameEditText;

    /**
     * EditText field to enter the habit's place
     */
    private EditText mPlaceEditText;

    /**
     * EditText field to enter the habit's times
     */
    private EditText mTimesEditText;

    /**
     * EditText field to enter the habit's feedback
     */
    private Spinner mFeedbackSpinner;

    /**
     * Feedback of the habit. The possible valid values are in the habitContract.java file:
     * {@link habitEntry#normal}, {@link habitEntry#bad}, or
     * {@link habitEntry#beautiful}.
     */
    private int mFeedback = habitEntry.normal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_habit_name);
        mPlaceEditText = (EditText) findViewById(R.id.edit_habit_place);
        mTimesEditText = (EditText) findViewById(R.id.edit_habit_times);
        mFeedbackSpinner = (Spinner) findViewById(R.id.spinner_feedback);

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the feedback of habit.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter feedbackSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_feedback_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        feedbackSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mFeedbackSpinner.setAdapter(feedbackSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mFeedbackSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.bad))) {
                        mFeedback = habitEntry.bad;
                    } else if (selection.equals(getString(R.string.beautiful))) {
                        mFeedback = habitEntry.beautiful;
                    } else {
                        mFeedback = habitEntry.normal;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mFeedback = habitEntry.normal;
            }
        });
    }

    /**
     * Get user input from editor and save new habit into database.
     */
    private void insertHabit() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String placeString = mPlaceEditText.getText().toString().trim();
        String timesString = mTimesEditText.getText().toString().trim();
        int times = Integer.parseInt(timesString);

        // Create database helper
        habitDbHelper mDbHelper = new habitDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(habitEntry.COLUMN_habit_NAME, nameString);
        values.put(habitEntry.COLUMN_place, placeString);
        values.put(habitEntry.COLUMN_feedback, mFeedback);
        values.put(habitEntry.COLUMN_times, times);

        // Insert a new row for habit in the database, returning the ID of that new row.
        long newRowId = db.insert(habitEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving habit", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "habit saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save habit to database
                insertHabit();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
