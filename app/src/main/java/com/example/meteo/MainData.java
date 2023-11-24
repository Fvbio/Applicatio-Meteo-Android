// MainData.java
package com.example.meteo;

import com.google.gson.annotations.SerializedName;

public class MainData {
    @SerializedName("temp")
    private double temp;


    @SerializedName("temp_min")
    private double temp_min;

    @SerializedName("temp_max")
    private double temp_max;
    @SerializedName("pressure")
    private double pressure;
    @SerializedName("humidity")
    private double humidity;

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public double getTemp() {return temp;}


}
