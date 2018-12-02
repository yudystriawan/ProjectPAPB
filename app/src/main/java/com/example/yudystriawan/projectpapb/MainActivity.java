package com.example.yudystriawan.projectpapb;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yudystriawan.projectpapb.Adapter.FoodAdapter;
import com.example.yudystriawan.projectpapb.Data.Food;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private RecyclerView recycleFoods;
    private RecyclerView.Adapter foodAdapter;
    private FusedLocationProviderClient mFusedLocationClient;
    protected static double latitude, longitude;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycleFoods = findViewById(R.id.recycle_view_foods);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        bottomNavigationView = findViewById(R.id.bottom_nav_bar);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_map:
                        Intent intent1 = new Intent(getBaseContext(), MapsActivity.class);
                        intent1.putExtra("LATITUDE", latitude);
                        intent1.putExtra("LONGITUDE", longitude);
                        startActivity(intent1);
                        break;
                    default:
                        return false;

                }
                return false;
            }
        });

        getLastKnowLocation();

        getWeather("https://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid=28c444227fbea12e1d303822b43f327f");
        getRecommend();
    }

    private void getLastKnowLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            });
        }
    }

    private void getRecommend() {
        ArrayList<Food> foods = new ArrayList<Food>();

        foods.add(new Food("Bakso", R.drawable.ic_baseline_fastfood_24px, R.drawable.ic_baseline_directions_24px));
        foods.add(new Food("Soto", R.drawable.ic_baseline_fastfood_24px, R.drawable.ic_baseline_directions_24px));
        foods.add(new Food("Sate", R.drawable.ic_baseline_fastfood_24px, R.drawable.ic_baseline_directions_24px));
        foods.add(new Food("AAAA", R.drawable.ic_baseline_fastfood_24px, R.drawable.ic_baseline_directions_24px));
        foods.add(new Food("BBB", R.drawable.ic_baseline_fastfood_24px, R.drawable.ic_baseline_directions_24px));
        foods.add(new Food("CCCC", R.drawable.ic_baseline_fastfood_24px, R.drawable.ic_baseline_directions_24px));
        foods.add(new Food("DDDD", R.drawable.ic_baseline_fastfood_24px, R.drawable.ic_baseline_directions_24px));

        recycleFoods.setHasFixedSize(true);
        recycleFoods.setLayoutManager(new LinearLayoutManager(this));

        foodAdapter = new FoodAdapter(LayoutInflater.from(this), foods);
        recycleFoods.setAdapter(foodAdapter);
    }

    private void getWeather(String weatherLink) {
        ConnectivityManager connMgr=(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // Check for network connections
        if (networkInfo != null && networkInfo.isConnected()) {
            // Create background thread to connect and get data
            new WeatherActivity(this).execute(weatherLink);
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


}

