package com.example.yudystriawan.projectpapb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

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
import java.util.Calendar;

class WeatherActivity extends AsyncTask<String, Void, String> {
    private ProgressDialog progressDialog;
    private Context context;
    private String webcontent;
    private HttpURLConnection httpConn;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private TextView text_tanggal, text_suhu, text_celcius, text_city;
    private WeatherIconView weatherIconView;


    public WeatherActivity(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context) ;
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

            double kelvin = Double.parseDouble(temp);
            double convert = (kelvin-273.15);
            convert = Math.round(convert);
            int celcius = (int)convert;

            text_tanggal = ((Activity)context).findViewById(R.id.tanggal);
            text_suhu = ((Activity)context).findViewById(R.id.suhu);
            text_celcius = ((Activity)context).findViewById(R.id.celcius);
            text_city = ((Activity)context).findViewById(R.id.city);

            calendar = Calendar.getInstance();
            dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            date = dateFormat.format(calendar.getTime());
            text_tanggal.setText(date);

            weatherIconView = ((Activity)context).findViewById(R.id.icon_weather);

            text_suhu.setText(String.valueOf(celcius));
            text_suhu.setTextSize(100);

            text_celcius.setText("\u2103");

            text_city.setText(city);

            if (desc.contains("thunderstrom")){
                weatherIconView.setIconResource(context.getString(R.string.wi_day_thunderstorm));
            }else if (desc.contains("rain")){
                weatherIconView.setIconResource(context.getString(R.string.wi_day_rain));
            }else if (desc.contains("cloud")){
                weatherIconView.setIconResource(context.getString(R.string.wi_day_cloudy));
            }else if (desc.contains("snow")){
                weatherIconView.setIconResource(context.getString(R.string.wi_day_snow));
            }else {
                weatherIconView.setIconResource(context.getString(R.string.wi_day_sunny));
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
