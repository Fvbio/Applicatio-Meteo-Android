package com.example.meteo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.meteo.ApiService;
import com.example.meteo.ApiClient;
import com.example.meteo.R;
import com.example.meteo.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTemperature;
    private TextView textViewVent;
    private TextView textViewVentDirection;
    private TextView textViewTempMin;
    private TextView textViewTempMax;
    private TextView textViewPressure;
    private TextView textViewHumidity;
    private TextView textViewCity;

    private final String DEFAULT_CITY = "Paris";

    private Switch temperatureSwitch;  // Déclarez le Switch ici

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTemperature = findViewById(R.id.textViewTemperature);
        textViewVent = findViewById(R.id.textViewVent);
        textViewVentDirection = findViewById(R.id.textViewVentDirection);
        textViewTempMin = findViewById(R.id.textViewTempMin);
        textViewTempMax = findViewById(R.id.textViewTempMax);
        textViewPressure = findViewById(R.id.textViewPressure);
        textViewHumidity = findViewById(R.id.textViewHumidity);
        textViewCity = findViewById(R.id.textViewCity);
        temperatureSwitch = findViewById(R.id.temperatureSwitch);

        final ApiService apiService = ApiClient.createService();

        makeWeatherApiCall(apiService, DEFAULT_CITY, "metric");

        SearchView searchView = findViewById(R.id.searchView);

        temperatureSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String unit = isChecked ? "metric" : "imperial";
                makeWeatherApiCall(apiService, DEFAULT_CITY, unit);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                makeWeatherApiCall(apiService, query, "metric");
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void makeWeatherApiCall(ApiService apiService, String city, String unit) {
        Call<WeatherResponse> call = apiService.getCurrentWeather(city, "d0060546f0e76bb2f532096b8a62e825", unit);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    updateWeatherInformation(weatherResponse, temperatureSwitch.isChecked());
                } else {
                    Log.e("API_CALL_ERROR", "Erreur de réponse de l'API: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("API_CALL_FAILURE", "Échec de la requête API", t);
            }
        });
    }

    private void updateWeatherInformation(WeatherResponse weatherResponse, boolean isMetric) {
        double temperature = weatherResponse.getMain().getTemp();
        double tempMin = weatherResponse.getMain().getTemp_min();
        double tempMax = weatherResponse.getMain().getTemp_max();
        double pressure = weatherResponse.getMain().getPressure();
        double humidity = weatherResponse.getMain().getHumidity();
        String city = weatherResponse.getCityName();
        double windSpeed = weatherResponse.getWind().getSpeed();
        int windDeg = weatherResponse.getWind().getDeg();

        int roundedTemperature = (int) Math.round(temperature);
        String temperatureUnit = isMetric ? "°C" : "°F";

        textViewTemperature.setText(String.valueOf(roundedTemperature + temperatureUnit));
        textViewVent.setText(String.valueOf(Math.round(windSpeed) + " km/h"));
        textViewVentDirection.setText(String.valueOf(Math.round(windDeg) + "°"));
        textViewTempMin.setText(String.valueOf("MIN : " + (int) Math.round(tempMin) + temperatureUnit));
        textViewTempMax.setText(String.valueOf("MAX : " + (int) Math.round(tempMax) + temperatureUnit));
        textViewPressure.setText(String.valueOf(Math.round(pressure) + " hPa"));
        textViewHumidity.setText(String.valueOf(Math.round(humidity) + " %"));
        textViewCity.setText(String.valueOf(city));
    }
}
