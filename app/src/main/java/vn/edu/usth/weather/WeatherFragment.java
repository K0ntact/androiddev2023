package vn.edu.usth.weather;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;

public class WeatherFragment extends Fragment {
    private WeatherApi weatherApi;
    private String jsonStr;
    private final CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherApi = new WeatherApi();
        weatherApi.setApiKey("795ed0794c1c44948ae70157231109");
        Thread thread = new Thread(() -> {
            try {
                jsonStr = weatherApi.currentReq("Hanoi", false);
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        RelativeLayout relativeLayout = view.findViewById(R.id.relativeLayout);

        String cityName;
        String tempC;
        String weatherCondition;
        String weatherImageUrl;

        try {
            // Wait for the request to finish.
            latch.await();

            JSONObject jsonObj = new JSONObject(jsonStr);
            cityName = jsonObj.getJSONObject("location").getString("name");
            tempC = jsonObj.getJSONObject("current").getString("temp_c");
            weatherCondition = jsonObj.getJSONObject("current").getJSONObject("condition").getString("text");
            weatherImageUrl = jsonObj.getJSONObject("current").getJSONObject("condition").getString("icon");
            weatherImageUrl = weatherImageUrl.replace("64x64", "128x128");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        TextView tempView = view.findViewById(R.id.tempView);
        tempView.setText(tempC);

        TextView weatherConditionView = view.findViewById(R.id.weatherDetailsView);
        weatherConditionView.setText(weatherCondition);

        ImageView imageView3 = view.findViewById(R.id.imageView3);
        Picasso.get().load("https:" + weatherImageUrl).into(imageView3);

        TextView weatherCityNameView = view.findViewById(R.id.weatherCityNameView);
        weatherCityNameView.setText(cityName);

        return view;
    }
}
