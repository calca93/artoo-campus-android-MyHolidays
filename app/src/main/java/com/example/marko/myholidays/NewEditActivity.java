package com.example.marko.myholidays;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class NewEditActivity extends AppCompatActivity {

    DB holidayDatabase;
    Holiday holiday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_edit);

        //-------------- FAB BUTTON ADD NEW HOLIDAY
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextTitle = (EditText) findViewById(R.id.editTextTitle);
                String editTitle = editTextTitle.getText().toString();

                EditText editTextPlace = (EditText) findViewById(R.id.editTextPlace);
                String editPlace = editTextPlace.getText().toString();

                Spinner spinnerMonth = (Spinner) findViewById(R.id.spinnerMonth);
                Spinner spinnerYear = (Spinner) findViewById(R.id.spinnerYear);

                Log.v("title", editTextPlace.getText().toString());

                //CHECK FIELDS
                if(editTitle.isEmpty() || editPlace.isEmpty() || editPlace == "" || editTitle == ""){
                    Toast.makeText(getApplicationContext(), "Some field are empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = getIntent();
                int id = intent.getIntExtra("id", 0);
                holidayDatabase = new DB(getApplicationContext());

                String date = spinnerMonth.getSelectedItem().toString() + " " + spinnerYear.getSelectedItem().toString();

                //SAVE DATA
                //RETURN TO DETAIL OR LIST
                if(id == 0) {
                    Holiday newHoliday = new Holiday(editTitle, editPlace, date, "");
                    holidayDatabase.addHoliday(newHoliday);

                    Toast.makeText(getApplicationContext(), "Holiday created", Toast.LENGTH_SHORT).show();

                    onBackPressed();
                }else{
                    Holiday editedHoliday = new Holiday(id, editTitle, editPlace, date, "");
                    holidayDatabase.updateHoliday(editedHoliday);

                    Toast.makeText(getApplicationContext(), "Holiday updated", Toast.LENGTH_SHORT).show();

                    onBackPressed();
                }
            }
        });

        DB holidayDatabase;

        EditText title = (EditText) findViewById(R.id.editTextTitle);
        EditText place = (EditText) findViewById(R.id.editTextPlace);
        Spinner spinnerMonth = (Spinner) findViewById(R.id.spinnerMonth);
        Spinner spinnerYear = (Spinner) findViewById(R.id.spinnerYear);

        String[] arrayMonth = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(this,
                R.layout.spinner_element, arrayMonth);
        spinnerMonth.setAdapter(adapterMonth);

        String[] arrayYear = new String[50];
        Calendar c = Calendar.getInstance();
        Integer thisYear = c.get(Calendar.YEAR);
        for(int i=0 ; i < 50 ; i++){
            Integer v = thisYear - i;
            arrayYear[i] = v.toString();
        }
        ArrayAdapter<String> adapterYear = new ArrayAdapter<String>(this,
                R.layout.spinner_element, arrayYear);
        spinnerYear.setAdapter(adapterYear);


        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        if(id == 0){
            setTitle("New Holiday");
        }else{
            setTitle("Edit Holiday");
            holidayDatabase = new DB(this);

            Holiday holiday = holidayDatabase.getHoliday(id);

            title.setText(holiday.getTitle());
            place.setText(holiday.getPlace());

            String[] date = holiday.getDate().split(" ");
            int spinnerYearPosition = adapterYear.getPosition(date[1]);
            int spinnerMonthPosition = adapterMonth.getPosition(date[0]);

            spinnerYear.setSelection(spinnerYearPosition);
            spinnerMonth.setSelection(spinnerMonthPosition);

        }
    }


}
