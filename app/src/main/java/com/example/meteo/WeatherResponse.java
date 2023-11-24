package com.example.meteo;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class WeatherResponse {
    @SerializedName("main")
    private MainData main;

    @SerializedName("weather")
    private List<Weather> weather;
    @SerializedName("name")
    private String name;

    public String getCityName() {
        return name;
    }

    @SerializedName("wind")
    private Wind wind;


    public MainData getMain() {return main;}

    public Wind getWind() { return wind; }

    public List<Weather> getWeather() {
        return weather;
    }

}

