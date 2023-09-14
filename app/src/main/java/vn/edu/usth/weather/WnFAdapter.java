package vn.edu.usth.weather;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class WnFAdapter extends FragmentStateAdapter {
    public WnFAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new WeatherAndForecastFragment("Hanoi");
            case 1: return new WeatherAndForecastFragment("Paris");
            case 2: return new WeatherAndForecastFragment("Toulouse");
        }
        return new WeatherAndForecastFragment("Hanoi");
    }
}
