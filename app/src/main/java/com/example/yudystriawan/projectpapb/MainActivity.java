package com.example.yudystriawan.projectpapb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pwittchen.weathericonview.WeatherIconView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private TextView text_tanggal, text_suhu, text_celcius, text_city;
    private WeatherIconView weatherIconView;
    private ProgressDialog progressDialog;
    private HttpURLConnection httpConn;
    private String webcontent;
    private RecyclerView recycleFoods;
    private RecyclerView.Adapter foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_tanggal = findViewById(R.id.tanggal);
        text_suhu = findViewById(R.id.suhu);
        text_celcius = findViewById(R.id.celcius);
        text_city = findViewById(R.id.city);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        date = dateFormat.format(calendar.getTime());
        text_tanggal.setText(date);

        weatherIconView = findViewById(R.id.icon_weather);

        recycleFoods = findViewById(R.id.recycle_view_foods);

        getWeather("https://api.openweathermap.org/data/2.5/weather?q=japan,id&appid=28c444227fbea12e1d303822b43f327f");
        getRecommend();

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
            new GetWeatherData().execute(weatherLink);
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private class GetWeatherData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this) ;
            progressDialog.setMessage("Please Wait");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jo = new JSONObject(s);
                JSONObject main_object = jo.getJSONObject("main");
                JSONArray weather_object = jo.getJSONArray("weather");
                JSONObject object = weather_object.getJSONObject(0);

                String temp = String.valueOf(main_object.getDouble("temp"));
                String city = jo.getString("name");
                String desc = object.getString("description");
                String humidity = String.valueOf(main_object.getString("humidity"));
                String pressure = String.valueOf(main_object.getString("pressure"));

                double kelvin = Double.parseDouble(temp);
                double convert = (kelvin-273.15);
                convert = Math.round(convert);
                int celcius = (int)convert;

                text_suhu.setText(String.valueOf(celcius));
                text_suhu.setTextSize(100);

                text_celcius.setText("\u2103");

                text_city.setText(city);

                if (desc.contains("thunderstrom")){
                    weatherIconView.setIconResource(getString(R.string.wi_day_thunderstorm));
                }else if (desc.contains("rain")){
                    weatherIconView.setIconResource(getString(R.string.wi_day_rain));
                }else if (desc.contains("cloud")){
                    weatherIconView.setIconResource(getString(R.string.wi_day_cloudy));
                }else if (desc.contains("snow")){
                    weatherIconView.setIconResource(getString(R.string.wi_day_snow));
                }else {
                    weatherIconView.setIconResource(getString(R.string.wi_day_sunny));
                }

                httpConn.disconnect();
                progressDialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... strings) {
            String link = strings[0];
            InputStream in = null;
            try {
                StringBuilder sb = new StringBuilder();
                in = openHttpConnection(link);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
                String nextLine = "";
                while ((nextLine = reader.readLine()) != null) {
                    sb.append(nextLine);
                    webcontent = sb.toString();
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return webcontent;
        }

        private InputStream openHttpConnection(String url) {
            InputStream in = null;
            int resCode;

            try {
                URL link = new URL(url);

                httpConn = (HttpURLConnection) link.openConnection();
                httpConn.setAllowUserInteraction(false);
                httpConn.setInstanceFollowRedirects(true);
                httpConn.setReadTimeout(10000);
                httpConn.setConnectTimeout(15000);
                httpConn.setRequestMethod("GET");
                httpConn.connect();
                resCode = httpConn.getResponseCode();

                if (resCode == HttpURLConnection.HTTP_OK){
                    in = httpConn.getInputStream();
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return  in;
        }
    }
}
