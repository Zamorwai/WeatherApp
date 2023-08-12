package com.karome.weatherapp;

public class Weather {
    private Double temperature;
    private String weatherDescription;
    private String date;

    public Weather(Double temperature, String weatherDescription, String date) {
        this.temperature = temperature;
        this.weatherDescription = weatherDescription;
        this.date = date;
    }

    public Double getTemperature() {
        return temperature;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public String getDate() {
        return date;
    }
}
