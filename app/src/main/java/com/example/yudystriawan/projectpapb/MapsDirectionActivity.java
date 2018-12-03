package com.example.yudystriawan.projectpapb;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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

import java.util.ArrayList;


public class MapsDirectionActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    MarkerOptions origin, destination;
    private static LatLng locationDest;
    private static LatLng locationNow;
    private Button btnDirect;
    double originLat, originLon, destinationLat, destinationLon;
    String destName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_direction);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent direction= getIntent();
        if (direction.getExtras() != null){
            originLat = direction.getExtras().getDouble("OriginLat");
            originLon = direction.getExtras().getDouble("OriginLon");
            destinationLat = direction.getExtras().getDouble("DestLat");
            destinationLon = direction.getExtras().getDouble("DestLon");
            destName = direction.getExtras().getString("DestName");
            locationNow = new LatLng(originLat,originLon);
            locationDest = new LatLng(destinationLat, destinationLon);
        }

        btnDirect = findViewById(R.id.btnDirect);

        btnDirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGoogleMap(destinationLat, destinationLon);
            }
        });
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
        origin = new MarkerOptions().position(locationNow).title("Your Here");
        mMap.addMarker(origin);
//        destination = new MarkerOptions().position(locationDest).title(destName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        Marker destination = mMap.addMarker(new MarkerOptions().position(locationDest).title(destName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        destination.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationNow, 14));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 1000, null);

    }

    public void OpenGoogleMap(double latitude, double longitude){
        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude+","+ longitude + "&mode=d");

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");

        // Attempt to start an activity that can handle the Intent
        startActivity(mapIntent);
    }
}
