package vn.edu.usth.weather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class WeatherAndForecastFragment extends Fragment {
    private final String cityName;
    private String forecastJson;
    private String currentJson;
    private final List<String> forecastDates;
    private final List<String> forecastMaxTempCs;
    private final List<String> forecastMinTempCs;
    private final List<String> forecastWeatherConditions;
    private final List<String> forecastWeatherImageUrls;
    private String currentTempC;
    private String currentWeatherCondition;
    private String lastUpdated;
    private String currentWeatherImageUrl;
    private final CountDownLatch latch;

    public WeatherAndForecastFragment(String cityName) {
        this.cityName = cityName;
        forecastDates = new ArrayList<>();
        forecastMaxTempCs = new ArrayList<>();
        forecastMinTempCs = new ArrayList<>();
        forecastWeatherConditions = new ArrayList<>();
        forecastWeatherImageUrls = new ArrayList<>();
        currentTempC = "";
        currentWeatherCondition = "";
        currentWeatherImageUrl = "";
        latch = new CountDownLatch(1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeatherApi weatherApi = new WeatherApi();
        weatherApi.setApiKey("795ed0794c1c44948ae70157231109");
        Thread thread = new Thread(() -> {
            try {
                currentJson = weatherApi.currentReq(cityName, false);
                forecastJson = weatherApi.forecastReq(cityName, 7, false);
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_and_forecast, container, false);
        GridLayout gridLayout = view.findViewById(R.id.gridLayout);
        return view;
    }

    // On resume, update the weather details and forecast with data from the API
    @Override
    public void onResume() {
        super.onResume();
        try {
            latch.await();
            View view = getView();
            assert view != null;
            GridLayout gridLayout = view.findViewById(R.id.gridLayout);
            updateCurrent(view, currentJson);
            updateForecast(gridLayout, forecastJson);

            gridLayout.removeView(view.findViewById(R.id.noDataPlaceholder));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void updateCurrent(View view,String currentString) {
        try {
            JSONObject currentJson = new JSONObject(currentString);
            currentTempC = currentJson.getJSONObject("current").getString("temp_c");
            currentWeatherCondition = currentJson.getJSONObject("current").getJSONObject("condition").getString("text");
            currentWeatherImageUrl = currentJson.getJSONObject("current").getJSONObject("condition").getString("icon");
            lastUpdated = currentJson.getJSONObject("current").getString("last_updated");
            currentWeatherImageUrl = currentWeatherImageUrl.replace("64x64", "128x128");
            System.out.println(currentWeatherImageUrl);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        TextView weatherCityNameView = view.findViewById(R.id.weatherCityNameView);
        weatherCityNameView.setText(cityName);

        TextView tempView = view.findViewById(R.id.tempView);
        tempView.setText(currentTempC + "°C");

        TextView weatherConditionView = view.findViewById(R.id.weatherDetailsView);
        weatherConditionView.setText(currentWeatherCondition);

        TextView lastUpdatedView = view.findViewById(R.id.lastUpdateView);
        lastUpdatedView.setText(getString(R.string.last_update) + lastUpdated);

        ImageView imageView = view.findViewById(R.id.imageView);
        Picasso.get().load("https:" + currentWeatherImageUrl).into(imageView);

    }

    private void updateForecast(GridLayout gridLayout, String forecastString) {
        try {
            JSONObject forecastJson = new JSONObject(forecastString);
            JSONArray forecastdays = forecastJson.getJSONObject("forecast").getJSONArray("forecastday");

            for (int i = 0; i < forecastdays.length(); i++) {
                JSONObject dayMetadata = (JSONObject) forecastdays.get(i);
                JSONObject forecastday = ((JSONObject) forecastdays.get(i)).getJSONObject("day");
                JSONObject condition = forecastday.getJSONObject("condition");

                System.out.println(dayMetadata.getString("date"));
                forecastDates.add(dayMetadata.getString("date"));
                forecastMaxTempCs.add(forecastday.getString("maxtemp_c"));
                forecastMinTempCs.add(forecastday.getString("mintemp_c"));
                forecastWeatherConditions.add(condition.getString("text"));
                forecastWeatherImageUrls.add(condition.getString("icon").replace("64x64", "128x128"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for(int i = 0; i < 7; i++) {
            TextView textView = new TextView(getActivity());
            textView.setText(dateToWeekday(forecastDates.get(i)));
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(16);
            textView.setSingleLine(true);
            GridLayout.LayoutParams textParam = new GridLayout.LayoutParams();
            textParam.width = GridLayout.LayoutParams.WRAP_CONTENT;
            textParam.height = dpToPx(48);
            textParam.rowSpec = GridLayout.spec(i);
            textParam.columnSpec = GridLayout.spec(0);
            textParam.setMargins(
                    dpToPx(10),
                    dpToPx(10),
                    dpToPx(10),
                    dpToPx(10)
            );
            textView.setLayoutParams(textParam);
            gridLayout.addView(textView);


            ImageView imageView = new ImageView(getActivity());
            System.out.println("https:" + forecastWeatherImageUrls.get(i));
            Picasso.get().load("https:" + forecastWeatherImageUrls.get(i)).into(imageView);
            GridLayout.LayoutParams imageParam = new GridLayout.LayoutParams();
            imageParam.width = GridLayout.LayoutParams.WRAP_CONTENT;
            imageParam.height = dpToPx(48);
            imageParam.rowSpec = GridLayout.spec(i);
            imageParam.columnSpec = GridLayout.spec(1);
            imageParam.setMargins(
                    dpToPx(10),
                    dpToPx(10),
                    dpToPx(20),
                    dpToPx(10)
            );
            imageView.setLayoutParams(imageParam);
            gridLayout.addView(imageView);

            LinearLayout linearLayout = new LinearLayout(getActivity());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setPadding(
                    dpToPx(10),
                    dpToPx(10),
                    dpToPx(10),
                    dpToPx(0)
            );
            GridLayout.LayoutParams linearParam = new GridLayout.LayoutParams();
            linearParam.width = GridLayout.LayoutParams.WRAP_CONTENT;
            linearParam.height = dpToPx(48);
            linearParam.rowSpec = GridLayout.spec(i);
            linearParam.columnSpec = GridLayout.spec(2);
            linearLayout.setLayoutParams(linearParam);
            TextView weather = new TextView(getActivity());
            weather.setText(forecastWeatherConditions.get(i));
            linearLayout.addView(weather);
            TextView temperature = new TextView(getActivity());
            temperature.setText(forecastMinTempCs.get(i) + "°C - " + forecastMaxTempCs.get(i) + "°C");
            linearLayout.addView(temperature);
            gridLayout.addView(linearLayout);
        }
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private String dateToWeekday(String dateStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date dateObj = format.parse(dateStr);
            SimpleDateFormat weekdayFormat = new SimpleDateFormat("EEEE");
            return weekdayFormat.format(dateObj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
