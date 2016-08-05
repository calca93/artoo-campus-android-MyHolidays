package com.example.marko.myholidays;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_INTERNET = 112;
    private static final int REQUEST_FINE_POSITION = 123;

    ListView listView;
    CustomAdapter customAdapter;
    ArrayList<Holiday> holidaysArray = new ArrayList<Holiday>();
    DB holidayDatabase;


    @Override
    protected void onResume(){
        super.onResume();
        holidayDatabase = new DB(this);

        holidaysArray = (ArrayList<Holiday>) holidayDatabase.getAll();

        listView = (ListView) findViewById(R.id.listView);
        customAdapter = new CustomAdapter(this, holidaysArray);
        listView.setAdapter(customAdapter);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ---------------- CHECK PERMISSIONS
        final boolean hasPermissionInternet = (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionInternet) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.INTERNET},
                    REQUEST_INTERNET);
        }
        boolean hasPermissionPosition = (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionPosition) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_FINE_POSITION);
        }

        //-------------- ACTION BAR ON TOOLBAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //-------------- FAB BUTTON ADD NEW HOLIDAY
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newEdit = new Intent(getApplicationContext(), NewEditActivity.class);
                startActivity(newEdit);
            }
        });


        holidayDatabase = new DB(this);

//        Holiday a = new Holiday("Cazzoni", "Pag", "October 2006", "");
//        Holiday b = new Holiday("Zozze", "Gallipoli", "July 2014", "");
//        Holiday c = new Holiday("Artoo", "Stella Maris", "June 2016", "");
//
//        holidayDatabase.addHoliday(a);
//        holidayDatabase.addHoliday(b);
//        holidayDatabase.addHoliday(c);

        holidaysArray = (ArrayList<Holiday>) holidayDatabase.getAll();

        listView = (ListView) findViewById(R.id.listView);
        customAdapter = new CustomAdapter(this, holidaysArray);
        listView.setAdapter(customAdapter);


        //---------------- EVENTI ONCLICK -------------------------

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), detailsHolidayActivity.class);
                i.putExtra("id", holidaysArray.get(position).getId());
                startActivity(i);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                alert(view, position);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_map) {
            Intent showMap = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(showMap);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void alert(View v, final int position){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setIcon(android.R.drawable.ic_dialog_alert);
        alert.setTitle("Do you really wanto to delete " + holidaysArray.get(position).getTitle() + " ?");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                holidayDatabase.deleteHoliday(holidaysArray.get(position).getId());
                holidaysArray.remove(holidaysArray.get(position));
                customAdapter.notifyDataSetChanged();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alert.show();
    }
}
