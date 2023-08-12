package com.karome.weatherapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.karome.weatherapp.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button startButton;
    private Intent intent;
    private SharedPreferences settings;
    private final String WEATHER_APP = "Weather";
    private final String SETTLEMENT = "Settlement";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.get_started_button);
        settings = getSharedPreferences(WEATHER_APP, MODE_PRIVATE);
        startButton.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String choiceSettlement = settings.getString(SETTLEMENT, "NoSettlement");
            if (choiceSettlement.equals("NoSettlement")) {
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
            } else {
                intent = new Intent(getApplicationContext(), SecondActivity.class);
            } startActivity(intent);
        }
    };
}