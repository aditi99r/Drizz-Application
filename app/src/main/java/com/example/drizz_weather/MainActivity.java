 package com.example.drizz_weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

 public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    ImageView imageView;
    TextView temp_yt, time, longitude, latitude, humidity, sunrise, sunset, pressure, wind, country_yt, city_yt, max_temp, min_temp, feels;

     String Location_Provider = LocationManager.GPS_PROVIDER;
     static final int REQUEST_LOCATION = 1;
     LocationManager mLocationManager;
     LocationListener mLocationListner;
     LocationManager locationManager;
     String lat, lon;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText=findViewById(R.id.editTextTextPersonName);
        button=findViewById(R.id.button);
        country_yt=findViewById(R.id.country);
        city_yt=findViewById(R.id.city);
        temp_yt=findViewById(R.id.temp);
        imageView=findViewById(R.id.imageView);
         time = findViewById(R.id.time);

         longitude = findViewById(R.id.longitude);
         latitude = findViewById(R.id.latitude);
         humidity = findViewById(R.id.humidity);
         sunrise = findViewById(R.id.sunrise);
         sunset = findViewById(R.id.sunset);
         pressure = findViewById(R.id.pressure);
         wind = findViewById(R.id.wind);
         max_temp = findViewById(R.id.temp_max);
         min_temp = findViewById(R.id.min_temp);
         feels = findViewById(R.id.feels);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = editText.getText().toString();
                if(city==null){
                    getLocation();
                    String url="https://api.openweathermap.org/data/2.5/weather?lat="+ lat +"&lon="+ lon +"&appid=286216a25c5c8cf3d4e189ace4dbab69";
                }
                else{
                    String url="https://api.openweathermap.org/data/2.5/weather?q="+ city +"&appid=286216a25c5c8cf3d4e189ace4dbab69";
                    findWeather(url);
                }
            }
        });

    }

     public void getLocation() {
         if (ActivityCompat.checkSelfPermission(
                 MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                 MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
             ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
         } else {
             Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
             if (locationGPS != null) {
                 double lati = ((Location) locationGPS).getLatitude();
                 double longi = locationGPS.getLongitude();
                 lat = String.valueOf(lati);
                 lon = String.valueOf(longi);
             } else {
                 Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
             }
         }
     }



    public void findWeather(String url){
//        String city = editText.getText().toString();
//        String url="https://api.openweathermap.org/data/2.5/weather?q="+ city +"&appid=286216a25c5c8cf3d4e189ace4dbab69";

        StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject= new JSONObject(response);

                    JSONObject object1= jsonObject.getJSONObject("sys");
                    String country_find=object1.getString("country");
                    country_yt.setText(country_find);
                    String city_find=jsonObject.getString("name");
                    city_yt.setText(city_find);

                    JSONObject object2= jsonObject.getJSONObject("main");
                    String temp_find=object2.getString("temp");
                    //int cTemp= (int) (Integer.parseInt(temp_find)-273.15);
                    temp_yt.setText(temp_find+"°C");

                    JSONArray jsonArray= jsonObject.getJSONArray("weather");
                    JSONObject jsonObject1=jsonArray.getJSONObject(0);
                    String img= jsonObject1.getString("icon");

                    Picasso.get().load("http://openweathermap.org/img/wn/"+img+"@2x.png").into(imageView);

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat std = new SimpleDateFormat("HH:mm a \nE, MMM dd yyyy");
                    String date = std.format(calendar.getTime());
                    time.setText(date);

                    JSONObject object3 = jsonObject.getJSONObject("coord");
                    double lat_find = object3.getDouble("lat");
                    latitude.setText(lat_find+"°  N");

                    JSONObject object4 = jsonObject.getJSONObject("coord");
                    double long_find = object4.getDouble("lon");
                    longitude.setText(long_find+"°  E");

                    //find humidity
                    JSONObject object5 = jsonObject.getJSONObject("main");
                    int humidity_find = object5.getInt("humidity");
                    humidity.setText(humidity_find+"  %");

                    //find sunrise
                    JSONObject object6 = jsonObject.getJSONObject("sys");
                    String sunrise_find = object6.getString("sunrise");
                    sunrise.setText(sunrise_find+"A.M.");

                    //find sunrise
                    JSONObject object7 = jsonObject.getJSONObject("sys");
                    String sunset_find = object7.getString("sunset");
//                    long s = sunset_find / 1000;
//                    long m = s / 60;
//                    long h = m / 60;
//                    String snst=String.format("02%d:%02d:%02d",h,m,s);
                    sunset.setText(sunset_find+"P.M.");

                    //find pressure
                    JSONObject object8 = jsonObject.getJSONObject("main");
                    String pressure_find = object8.getString("pressure");
                    pressure.setText(pressure_find+" hPa");

                    //find wind speed
                    JSONObject object9 = jsonObject.getJSONObject("wind");
                    String wind_find = object9.getString("speed");
                    wind.setText(wind_find+"  km/h");

                    //find min temperature
                    JSONObject object10 = jsonObject.getJSONObject("main");
                    double mintemp = object10.getDouble("temp_min");
                    //mintemp -= 273.15;
                    min_temp.setText("Min Temp\n"+mintemp+"°C");

                    //find max temperature
                    JSONObject object11 = jsonObject.getJSONObject("main");
                    double maxtemp = object11.getDouble("temp_max");
                    //maxtemp -= 273.15;
                    max_temp.setText("Max Temp\n"+maxtemp+"°C");

                    //find feels
                    JSONObject object12 = jsonObject.getJSONObject("main");
                    double feels_find = object12.getDouble("feels_like");
                    //feels_find -= 273.15;
                    feels.setText(feels_find+"°C");



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }
}