package com.karome.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class JsonAdapter extends RecyclerView.Adapter<JsonAdapter.ViewHolder>{
    private final LayoutInflater inflater;
    private final List<Weather> weatherList;

    public JsonAdapter(Context context, List<Weather> weatherList) {
        this.inflater = LayoutInflater.from(context);
        this.weatherList = weatherList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Weather weather = weatherList.get(position);
        holder.date.setText(weather.getDate());
        holder.temp.setText(weather.getTemperature().toString() + "°C");
        holder.weather_description.setText(weather.getWeatherDescription().toUpperCase());
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView date, temp, weather_description;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            temp = itemView.findViewById(R.id.temp);
            weather_description = itemView.findViewById(R.id.weather_description);
        }
    }
}
