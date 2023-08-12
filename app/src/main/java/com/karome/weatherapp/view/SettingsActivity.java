package com.karome.weatherapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.karome.weatherapp.R;
import com.karome.weatherapp.view.SecondActivity;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";

    private Button saveBtn;
    private EditText editSettlement;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private final String WEATHER_APP = "Weather";
    private final String SETTLEMENT = "Settlement";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Log.d(TAG, "Getting SharedPreferences");
        settings = getSharedPreferences(WEATHER_APP, MODE_PRIVATE);
        editSettlement = findViewById(R.id.editSettlement);

        saveBtn = findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(listener);
    }
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String savedSettlement = editSettlement.getText().toString();
            if (savedSettlement.equals("")) {
                Snackbar.make(view, R.string.snackbar_warning, Snackbar.LENGTH_SHORT).show();
            } else {
                editor = settings.edit();
                editor.putString(SETTLEMENT, savedSettlement);
                editor.apply();

                startActivity(new Intent(getApplicationContext(), SecondActivity.class));
            }
        }
    };
}