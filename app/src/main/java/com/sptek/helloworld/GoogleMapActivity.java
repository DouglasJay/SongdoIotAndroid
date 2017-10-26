package com.sptek.helloworld;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class GoogleMapActivity extends AppCompatActivity  {
    SupportMapFragment mapFragment;
    GoogleMap map;
    class Loc {
        String title; double latitude, longitude;
        Loc(String title, double latitude, double longitude) {
            this.title = title; this.latitude = latitude; this.longitude = longitude;
        }
    }
    ArrayList<Loc> locList = new ArrayList<Loc>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);

        locList.add(new Loc("파리 개선문", 48.873975, 2.295038));
        locList.add(new Loc("그리스 산토리니", 36.400502, 25.461484));
        locList.add(new Loc("이태리 산마르코 대성당", 45.435322, 12.339422));
        locList.add(new Loc("제주도 중문색달해변", 33.245049, 126.411761));
        locList.add(new Loc("부산 송정 해수욕장", 35.178506, 129.199799));

        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                for (int i = 0; i < locList.size(); i++) {
                    MarkerOptions marker = new MarkerOptions();
                    marker.position(new LatLng(locList.get(i).latitude,locList.get(i).longitude));
                    marker.title(locList.get(i).title);
                    map.addMarker(marker);
                }
            }
        });
        try {
            MapsInitializer.initialize(this);
        } catch(Exception e) { e.printStackTrace(); }
        startLocationService();
    }
    class GPSListener implements android.location.LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            String message = "Current location - lat:"+location.getLatitude()+
                    ", long:"+location.getLongitude();
            Toast.makeText(GoogleMapActivity.this, message, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}
    }
    private void startLocationService() {
        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        try {
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                String message = "Last knwon location - lat:" + location.getLatitude() +
                        ", long:" + location.getLongitude();
                Toast.makeText(GoogleMapActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        } catch (SecurityException e) { e.printStackTrace(); }
        GPSListener gpsListener = new GPSListener();
        long time = 10000; float dist = 0;
        try {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, time, dist, gpsListener);
        } catch (SecurityException e) { e.printStackTrace(); }
    }

}
