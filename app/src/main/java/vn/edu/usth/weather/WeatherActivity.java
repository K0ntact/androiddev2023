package vn.edu.usth.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class WeatherActivity extends AppCompatActivity {

//    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WeatherFragment detailForecast = new WeatherFragment();
        getSupportFragmentManager().beginTransaction().add(
                R.id.detailWeatherFragmentContainerView, detailForecast).commit();

        ForecastFragment dailyForecast = new ForecastFragment();
        getSupportFragmentManager().beginTransaction().add(
                R.id.dailyForecastFragmentContainerView, dailyForecast).commit();

        Log.i("WeatherActivity", "onCreate() called");
        setContentView(R.layout.activity_weather);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("WeatherActivity", "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("WeatherActivity", "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("WeatherActivity", "onPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("WeatherActivity", "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("WeatherActivity", "onDestroy() called");
    }
}