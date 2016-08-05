package com.example.marko.myholidays;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        mMap.getUiSettings().setCompassEnabled(true);//Bussola
        mMap.getUiSettings().setZoomControlsEnabled(true);//Zoom button + -
        mMap.getUiSettings().setRotateGesturesEnabled(true);

        DB holidayDatabase = new DB(this);
        List<Holiday> holidayList = holidayDatabase.getAll();

        this.setAllPins(holidayList);
    }

    public void setAllPins(List<Holiday> list){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        double latAvg = 0;
        double lonAvg = 0;
        double maxLat = 0;
        double maxLon = 0;

        for (Holiday h : list){
            try{
                addresses = geocoder.getFromLocationName(h.getPlace(), 5);
                if(addresses.size() > 0){
                    double latitude = addresses.get(0).getLatitude();
                    double longitude = addresses.get(0).getLongitude();

                    LatLng latLng = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(latLng).title(h.getTitle()));



                    latAvg += latitude;
                    lonAvg += longitude;

                    double lat = (latitude < 0) ? latitude * (-1) : latitude;
                    double lon = (longitude < 0) ? longitude * (-1) : longitude;

                    if(maxLat < lat)
                        maxLat = lat;
                    if(maxLon < lon)
                        maxLon = lon;
                }
            }catch(IOException e) {
                e.printStackTrace();
            }
        }

        if(list.size() != 0) {
            latAvg /= list.size();
            lonAvg /= list.size();
        }
        LatLng latLng = new LatLng(latAvg, lonAvg);
        System.out.println("Max lat: " + maxLat);
        System.out.println("Max lon: " + maxLon);
        System.out.println(latAvg);
        System.out.println(lonAvg);

        int zoom = 4;
        if(latAvg != 0) {
            Double n = maxLat % latAvg;
            zoom = n.intValue();
        }

        System.out.println(zoom);


        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(zoom)
                .bearing(0)
                .tilt(0)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
