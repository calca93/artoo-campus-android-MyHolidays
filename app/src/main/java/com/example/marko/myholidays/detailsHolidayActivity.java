package com.example.marko.myholidays;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class detailsHolidayActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    DB holidayDatabase;
    Holiday holiday;
    int globalID;


    @Override
    protected void onResume(){
        super.onResume();
        holiday = holidayDatabase.getHoliday(globalID);

        TextView title = (TextView) findViewById(R.id.detailTitle);
        TextView place = (TextView) findViewById(R.id.detailPlace);
        TextView date = (TextView) findViewById(R.id.detailDate);
        ImageView image = (ImageView) findViewById(R.id.imageView);

        title.setText(holiday.getTitle());
        place.setText(holiday.getPlace());
        date.setText(holiday.getDate());
        //image.setBitmap
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_holiday);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        final Intent intentId = getIntent();
        final int id = intentId.getIntExtra("id", 0);
        globalID = id;
        holidayDatabase = new DB(this);

        //-------------- FAB BUTTON ADD NEW HOLIDAY
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_edit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newEdit = new Intent(getApplicationContext(), NewEditActivity.class);
                newEdit.putExtra("id",id);
                startActivity(newEdit);
            }
        });

        holiday = holidayDatabase.getHoliday(id);

        TextView title = (TextView) findViewById(R.id.detailTitle);
        TextView place = (TextView) findViewById(R.id.detailPlace);
        TextView date = (TextView) findViewById(R.id.detailDate);
        ImageView image = (ImageView) findViewById(R.id.imageView);

        title.setText(holiday.getTitle());
        place.setText(holiday.getPlace());
        date.setText(holiday.getDate());
        //image.setBitmap
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setPin(holiday);
    }

    public void setPin(Holiday h){

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(h.getPlace(), 5);
            if (addresses.size() > 0) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();

                LatLng latLng = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(latLng).title(h.getTitle()));

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(9)
                        .bearing(0)
                        .tilt(0)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
