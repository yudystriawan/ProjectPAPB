package com.example.yudystriawan.projectpapb;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.yudystriawan.projectpapb.Adapter.FoodAdapter;

import com.example.yudystriawan.projectpapb.Data.Restoran;
import com.github.pwittchen.weathericonview.WeatherIconView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private RecyclerView recycleFoods;
    private RecyclerView.Adapter foodAdapter;
    private FusedLocationProviderClient mFusedLocationClient;
    protected static double latitude, longitude;
    protected static String jenisDatabase;
    private BottomNavigationView bottomNavigationView;
    ArrayList<Restoran> listRestSample = new ArrayList<Restoran>();
    private FirebaseFirestore db;
    Context mContext;
    private String weatherNow;
    private TextView text_tanggal, text_suhu, text_celcius, text_city, text_detail;
    private WeatherIconView weatherIconView;
    private Button btnTerpopuler, btnTerdekat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        recycleFoods = findViewById(R.id.recycle_view_foods);
        btnTerdekat = findViewById(R.id.btnTerdekat);
        btnTerpopuler = findViewById(R.id.btnTerpopuler);
        bottomNavigationView = findViewById(R.id.bottom_nav_bar);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        
        getLastKnowLocation();

        jenisDatabase = "DaftarMakananSample";
        readFB(jenisDatabase);

        btnTerdekat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listRestSample.clear();
                jenisDatabase = "DaftarMakananSample";
                readFB(jenisDatabase);
            }
        });

        btnTerpopuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listRestSample.clear();
                jenisDatabase = "DaftarMakanan";
                readFB(jenisDatabase);
            }
        });

//        getWeather("https://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid=28c444227fbea12e1d303822b43f327f");
        //getRecommend();



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_map:
                        Intent intent1 = new Intent(getBaseContext(), MapsNearbyActivity.class);
                        intent1.putExtra("originLat", latitude);
                        intent1.putExtra("originLon", longitude);
                        intent1.putExtra("data", (Serializable) listRestSample);
                        startActivity(intent1);
                        break;
                    default:
                        return false;

                }
                return false;
            }
        });

    }

    //^^^END OF ONCREATE


    public static double getLatitude() {
        return latitude;
    }

    public static double getLongitude() {
        return longitude;
    }

    private void getLastKnowLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                try {
                                    latitude =  location.getLatitude();
                                    longitude =  location.getLongitude();

                                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                    if (addresses.size() > 0) {
                                        getWeather(location.getLatitude(), location.getLongitude());
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else { // Show "no location" }
                            }
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
// If the permission is granted, get the location, otherwise, show a Toast
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    getLastKnowLocation();
                } else {
                    Toast.makeText(this,
                            "Permission denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void getWeather(double latitude, double longitude) {
        String url = "http://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid=3fd8da85e581b3ff8dfb191ea4454620";

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET,
                url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject main_object = response.getJSONObject("main");
                    JSONArray array = response.getJSONArray("weather");

                    JSONObject object = array.getJSONObject(0);

                    String temp = String.valueOf(Math.round((main_object.getDouble("temp")-273.15)));
                    String city = response.getString("name");
                    weatherNow = object.getString("main");
                    String detail = object.getString("description");

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE-MM-dd");
                    String formatted_date = sdf.format(calendar.getTime());

                    text_tanggal = findViewById(R.id.tanggal);
                    text_suhu = findViewById(R.id.suhu);
                    text_celcius = findViewById(R.id.celcius);
                    text_city = findViewById(R.id.city);
                    text_detail = findViewById(R.id.detail);
                    text_tanggal.setText(formatted_date);

                    weatherIconView = findViewById(R.id.icon_weather);

                    text_suhu.setText(String.valueOf(temp));
                    text_suhu.setTextSize(100);

                    text_celcius.setText("\u2103");

                    text_city.setText(city);

                    text_detail.setText(detail);

                    switch(weatherNow){
                        case "Thunderstorm":
                            weatherIconView.setIconResource(getString(R.string.wi_day_thunderstorm));
                            break;
                        case "Rain":
                            weatherIconView.setIconResource(getString(R.string.wi_day_rain));
                            break;
                        case "Clouds":
                            weatherIconView.setIconResource(getString(R.string.wi_day_cloudy));
                            break;
                        case "Clear":
                            weatherIconView.setIconResource(getString(R.string.wi_day_sunny));
                            break;
                        case "Haze":
                            weatherIconView.setIconResource(getString(R.string.wi_day_haze));
                            break;
                        default:
                            weatherIconView.setIconResource(getString(R.string.wi_day_sunny));
                            break;
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection";
                } else if (volleyError instanceof ServerError) {
                    message = "The location could not be found. Please try again";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time";
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jor);
    }
    //AMBIL DATA DARI FIREBASE
    int sizeData = 0;

    private void readFB(String database) {

        db = FirebaseFirestore.getInstance();

        db.collection(database)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        sizeData = queryDocumentSnapshots.size();
                        String id, name, phone, rating, type, weather, latitude, longitude;
                        //PERLU DITAMBAH TRY CATCH(?)
                        for (int i = 0; i < sizeData; i++) {
//                            if (queryDocumentSnapshots.getDocuments().get(i).get("Weather").toString().equalsIgnoreCase(weatherNow)) {
                                id = String.valueOf(i);
                                name = queryDocumentSnapshots.getDocuments().get(i).get("Name").toString();
                                phone = queryDocumentSnapshots.getDocuments().get(i).get("Phone").toString();
                                rating = queryDocumentSnapshots.getDocuments().get(i).get("Rating").toString();
                                type = queryDocumentSnapshots.getDocuments().get(i).get("Type").toString();
                                weather = queryDocumentSnapshots.getDocuments().get(i).get("Weather").toString();
                                latitude = queryDocumentSnapshots.getDocuments().get(i).get("Latitude").toString();
                                longitude = queryDocumentSnapshots.getDocuments().get(i).get("Longitude").toString();
                                listRestSample.add(new Restoran(id, name, phone, rating, type, weather, latitude, longitude));
//                            }
                        }
                        if (listRestSample.size() != 0){
                            recycleFoods.setHasFixedSize(true);
                            recycleFoods.setLayoutManager(new LinearLayoutManager(mContext));

                            foodAdapter = new FoodAdapter(LayoutInflater.from(mContext), listRestSample);
                            recycleFoods.setAdapter(foodAdapter);
                        } else {
                            Toast.makeText(getApplicationContext(), "Tidak ada resto yang sesuai", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

}

