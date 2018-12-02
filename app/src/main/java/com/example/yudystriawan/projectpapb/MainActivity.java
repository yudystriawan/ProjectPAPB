package com.example.yudystriawan.projectpapb;

import android.Manifest;
import android.content.Context;
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

import com.example.yudystriawan.projectpapb.Data.Restoran;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private RecyclerView recycleFoods;
    private RecyclerView.Adapter foodAdapter;
    private FusedLocationProviderClient mFusedLocationClient;
    protected static double latitude, longitude;
    private BottomNavigationView bottomNavigationView;
    ArrayList<Restoran> listRestSample = new ArrayList<Restoran>();
    private FirebaseFirestore db;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

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
        //getRecommend();
        readFB();
    }
    //^^^END OF ONCREATE

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

//    private void getRecommend() {
//        ArrayList<Food> foods = new ArrayList<Food>();
//
//        foods.add(new Food("Bakso", R.drawable.ic_baseline_fastfood_24px, R.drawable.ic_baseline_directions_24px));
//        foods.add(new Food("Soto", R.drawable.ic_baseline_fastfood_24px, R.drawable.ic_baseline_directions_24px));
//        foods.add(new Food("Sate", R.drawable.ic_baseline_fastfood_24px, R.drawable.ic_baseline_directions_24px));
//        foods.add(new Food("AAAA", R.drawable.ic_baseline_fastfood_24px, R.drawable.ic_baseline_directions_24px));
//        foods.add(new Food("BBB", R.drawable.ic_baseline_fastfood_24px, R.drawable.ic_baseline_directions_24px));
//        foods.add(new Food("CCCC", R.drawable.ic_baseline_fastfood_24px, R.drawable.ic_baseline_directions_24px));
//        foods.add(new Food("DDDD", R.drawable.ic_baseline_fastfood_24px, R.drawable.ic_baseline_directions_24px));
//
//        recycleFoods.setHasFixedSize(true);
//        recycleFoods.setLayoutManager(new LinearLayoutManager(this));
//
//        foodAdapter = new FoodAdapter(LayoutInflater.from(this), foods);
//        recycleFoods.setAdapter(foodAdapter);
//    }

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

    //AMBIL DATA DARI FIREBASE
    int sizeData = 0;
    private void readFB() {

        db = FirebaseFirestore.getInstance();

        db.collection("DaftarMakananSample")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        sizeData = queryDocumentSnapshots.size();
                        String id, name, phone, rating, type, weather, latitude, longitude;
                        //PERLU DITAMBAH TRY CATCH(?)
                        for (int i = 0; i < sizeData; i++) {

                            id = queryDocumentSnapshots.getDocuments().get(i).get("Id").toString();
                            name = queryDocumentSnapshots.getDocuments().get(i).get("Name").toString();
                            phone = queryDocumentSnapshots.getDocuments().get(i).get("Phone").toString();
                            rating = queryDocumentSnapshots.getDocuments().get(i).get("Rating").toString();
                            type = queryDocumentSnapshots.getDocuments().get(i).get("Type").toString();
                            weather = queryDocumentSnapshots.getDocuments().get(i).get("Weather").toString();
                            latitude = queryDocumentSnapshots.getDocuments().get(i).get("Latitude").toString();
                            longitude = queryDocumentSnapshots.getDocuments().get(i).get("Longitude").toString();
                            listRestSample.add(new Restoran(id, name, phone, rating, type, weather, latitude, longitude));
                        }
                        recycleFoods.setHasFixedSize(true);
                        recycleFoods.setLayoutManager(new LinearLayoutManager(mContext));

                        foodAdapter = new FoodAdapter(LayoutInflater.from(mContext), listRestSample);
                        recycleFoods.setAdapter(foodAdapter);
                    }
                });

    }
}

