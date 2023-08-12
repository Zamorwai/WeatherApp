package com.karome.weatherapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.karome.weatherapp.JsonAdapter;
import com.karome.weatherapp.R;
import com.karome.weatherapp.Weather;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class SecondActivity extends AppCompatActivity implements Runnable{
    private static final String TAG = "SecondActivity";

    private TextView settlementName;
    private TextView temperature;
    private TextView weatherDescription;
    private TextView feelsLikeTemp;
    private Button changeSettlement;
    private RecyclerView recyclerView;
    private SharedPreferences settings;
    private final String WEATHER_APP = "Weather";
    private final String SETTLEMENT = "Settlement";
    private final String CURRENT_WEATHER_SERVER_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    private final String WEEK_FORECAST_SERVER_URL = "https://api.openweathermap.org/data/2.5/forecast?q=";
    private final String API_KEY = "&appid=key";
    private final String EXTRA_OPTIONS = "&units=metric&lang=";
    private String dailyWeatherForecastRequest;
    private String weekWeatherForecastRequest;
    private String savedSettlement;
    private Handler handler;
    private JSONObject jsonObject;

    List<Weather> weatherList = new ArrayList<>();

    private JsonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        temperature = findViewById(R.id.temperature);
        temperature.setText(R.string.getting_temp_from_server);

        weatherDescription = findViewById(R.id.weatherDescription);
        weatherDescription.setText(R.string.getting_weather_description);

        feelsLikeTemp = findViewById(R.id.feelsLikeTemp);
        feelsLikeTemp.setText(R.string.getting_temp_from_server);

        settlementName = findViewById(R.id.settlementName);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new JsonAdapter(this, weatherList);
        recyclerView.setAdapter(adapter);

        settings = getSharedPreferences(WEATHER_APP, MODE_PRIVATE);
        savedSettlement = settings.getString(SETTLEMENT, "NoSettlement");

        settlementName.setText(savedSettlement);

        handler = new Handler();
        new Thread(this).start();

        changeSettlement = findViewById(R.id.changeSettlement);
        changeSettlement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "changeSettlement.setOnClickListener: start SettingsActivity");
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }
        });
    }
    @Override
    public void run() {
        String lang = EXTRA_OPTIONS + Locale.getDefault().getLanguage();
        Log.d(TAG, "Runnable run(): device language " + lang);
        dailyWeatherForecastRequest = CURRENT_WEATHER_SERVER_URL + savedSettlement + API_KEY + lang;

        weekWeatherForecastRequest = WEEK_FORECAST_SERVER_URL + savedSettlement + API_KEY + lang;

        loadDailyWeatherForecast();
        loadWeeklyWeatherForecast(weatherList);
    }
    private void loadDailyWeatherForecast() {
        try {
            Log.d(TAG, "getDailyWeatherForecast(): trying to connect with daily weather server via URL");
            URL url = new URL(dailyWeatherForecastRequest);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = reader.readLine()) != null)
                buffer.append(line).append("\n");
            String serverResponse = buffer.toString();

            jsonObject = new JSONObject(serverResponse);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        temperature.setText(jsonObject.getJSONObject("main").getDouble("temp") + "°C");
                        weatherDescription.setText(jsonObject.getJSONArray("weather").getJSONObject(0).getString("description").toUpperCase());
                        feelsLikeTemp.setText(jsonObject.getJSONObject("main").getDouble("feels_like") + "°C");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Exception");
            e.printStackTrace();
        }
    }

    private void loadWeeklyWeatherForecast(List<Weather> weatherList) {
        try {
            Log.d(TAG, "getWeekWeatherForecast(): trying to connect with week weather server via URL");
            URL url = new URL(weekWeatherForecastRequest);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line).append("\n");
            } String serverResponse = buffer.toString();

            JSONObject jsonObject = new JSONObject(serverResponse);
            JSONArray listArray = jsonObject.getJSONArray("list");

            for (int i = 0; i < listArray.length(); i++) {
                JSONObject listItem = listArray.getJSONObject(i);

                JSONObject mainObject = listItem.getJSONObject("main");
                Log.i(TAG, "getWeekWeatherForecast(): getting temperature from json response");
                double temp = mainObject.getDouble("temp");

                JSONArray weatherArray = listItem.getJSONArray("weather");
                JSONObject weatherObject = weatherArray.getJSONObject(0);

                Log.i(TAG, "getWeekWeatherForecast(): getting weather description from json response");
                String typeOfWeather = weatherObject.getString("description");

                Log.i(TAG, "getWeekWeatherForecast(): getting dt_txt (date) from json response");
                String date = listItem.getString("dt_txt");

                Weather weather = new Weather(temp, typeOfWeather, date);
                weatherList.add(weather);
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "getWeekWeatherForecast(): successful getting data from json");
                    adapter.notifyDataSetChanged();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}