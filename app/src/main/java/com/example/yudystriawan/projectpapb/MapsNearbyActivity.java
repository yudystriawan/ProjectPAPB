package com.example.yudystriawan.projectpapb;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.yudystriawan.projectpapb.Data.Restoran;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.net.URL;
import java.util.ArrayList;


public class MapsNearbyActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private GoogleMap mMap;
    MarkerOptions origin, destination;
    private static LatLng locationDest;
    private static LatLng locationNow;
    double originLat, originLon, destinationLat, destinationLon;
    String destName;
    ArrayList<Restoran> listRestSample = new ArrayList<Restoran>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent i = getIntent();
        if (i.getExtras() != null) {
            originLat = i.getExtras().getDouble("originLat");
            originLon = i.getExtras().getDouble("originLon");
            listRestSample = (ArrayList<Restoran>) i.getSerializableExtra("data");
            locationNow = new LatLng(originLat, originLon);
        }
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
//        origin = new MarkerOptions().position(locationNow).title("Your Here");
//        mMap.addMarker(origin);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
        mMap.setMyLocationEnabled(true);
        for (int i = 0; i < listRestSample.size(); i++){
            destinationLat = Double.valueOf(listRestSample.get(i).getLatitude());
            destinationLon = Double.valueOf(listRestSample.get(i).getLongitude());
            destName = listRestSample.get(i).getName();
            locationDest = new LatLng(destinationLat, destinationLon);
            destination = new MarkerOptions().position(locationDest).title(destName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            mMap.addMarker(destination);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationNow, 14));

    }

}
