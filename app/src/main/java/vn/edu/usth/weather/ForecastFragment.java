package vn.edu.usth.weather;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ForecastFragment extends Fragment {
    private WeatherApi weatherApi;
    private String jsonStr;
    private final List<String> dates = new ArrayList<>();
    private final List<String> maxTempCs = new ArrayList<>();
    private final List<String> minTempCs = new ArrayList<>();
    private final List<String> weatherConditions = new ArrayList<>();
    private final List<String> weatherImageUrls = new ArrayList<>();
    private final CountDownLatch latch = new CountDownLatch(1);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherApi = new WeatherApi();
        weatherApi.setApiKey("795ed0794c1c44948ae70157231109");
        Thread thread = new Thread(() -> {
            try {
                jsonStr = weatherApi.forecastReq("Hanoi", 7, false);
                System.out.println(jsonStr);
                latch.countDown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);
        GridLayout gridLayout = view.findViewById(R.id.gridLayout);

        try {
            // Wait for the request to finish.
            latch.await();

            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray forecastdays = jsonObj.getJSONObject("forecast").getJSONArray("forecastday");

            for (int i = 0; i < forecastdays.length(); i++) {
                JSONObject dayMetadata = (JSONObject) forecastdays.get(i);
                JSONObject forecastday = ((JSONObject) forecastdays.get(i)).getJSONObject("day");
                JSONObject condition = forecastday.getJSONObject("condition");

                System.out.println(dayMetadata.getString("date"));
                dates.add(dayMetadata.getString("date"));
                maxTempCs.add(forecastday.getString("maxtemp_c"));
                minTempCs.add(forecastday.getString("mintemp_c"));
                weatherConditions.add(condition.getString("text"));
                weatherImageUrls.add(condition.getString("icon"));
                System.out.println(weatherImageUrls.get(i));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for(int row = 0; row < 7; row++) {
            TextView textView = addTextView(dates.get(row), row);
            gridLayout.addView(textView);

            ImageView imageView = addImageView("https:" + weatherImageUrls.get(row).replace("64x64", "128x128"), row);
            gridLayout.addView(imageView);

            LinearLayout linearLayout = addLinearLayout(row);
            gridLayout.addView(linearLayout);
        }
        return view;
    }

    //Create a textView.
    private TextView addTextView(String text, int row) {
        TextView textView = new TextView(getActivity());
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);

        GridLayout.LayoutParams textParam = new GridLayout.LayoutParams();
        textParam.width = GridLayout.LayoutParams.WRAP_CONTENT;
        textParam.height = dpToPx(48);
        textParam.rowSpec = GridLayout.spec(row);
        textParam.columnSpec = GridLayout.spec(0);
        textParam.setMargins(
                dpToPx(20),
                dpToPx(10),
                dpToPx(20),
                dpToPx(10)
        );
        textView.setLayoutParams(textParam);

        return textView;
    }

    // Create an ImageView.
    private ImageView addImageView(String imgURL, int row) {
        ImageView imageView = new ImageView(getActivity());
        Picasso.get().load(imgURL).into(imageView);

        GridLayout.LayoutParams imageParam = new GridLayout.LayoutParams();
        imageParam.width = GridLayout.LayoutParams.WRAP_CONTENT;
        imageParam.height = dpToPx(48);
        imageParam.rowSpec = GridLayout.spec(row);
        imageParam.columnSpec = GridLayout.spec(1);
        imageParam.setMargins(
                dpToPx(20),
                dpToPx(10),
                dpToPx(20),
                dpToPx(10)
        );
        imageView.setLayoutParams(imageParam);
        return imageView;
    }

    // Create a LinearLayout.
    private LinearLayout addLinearLayout(int row){
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(
                dpToPx(20),
                dpToPx(10),
                dpToPx(20),
                dpToPx(0)
        );

        GridLayout.LayoutParams linearParam = new GridLayout.LayoutParams();
        linearParam.width = GridLayout.LayoutParams.WRAP_CONTENT;
        linearParam.height = dpToPx(48);
        linearParam.rowSpec = GridLayout.spec(row);
        linearParam.columnSpec = GridLayout.spec(2);
        linearLayout.setLayoutParams(linearParam);

        TextView weather = new TextView(getActivity());
        weather.setText(weatherConditions.get(row));
        linearLayout.addView(weather);

        TextView temperature = new TextView(getActivity());
        temperature.setText(minTempCs.get(row) + "°C - " + maxTempCs.get(row) + "°C");
        linearLayout.addView(temperature);

        return linearLayout;
    }

    public int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

}
