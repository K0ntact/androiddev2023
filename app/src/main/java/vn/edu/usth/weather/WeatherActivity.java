package vn.edu.usth.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

public class WeatherActivity extends AppCompatActivity {

//    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("WeatherActivity", "onCreate() called");
        setContentView(R.layout.activity_weather);

        ViewPager2 viewPager = findViewById(R.id.viewPager);
        FragmentStateAdapter adapter = new WnFAdapter(this);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.cityTabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab unused) {
                viewPager.setCurrentItem(tabLayout.getSelectedTabPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab unused) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab unused) {
            }
        }
        );
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