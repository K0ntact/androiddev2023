package vn.edu.usth.weather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ForecastFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);

        GridLayout gridLayout = view.findViewById(R.id.gridLayout);

        for(int row = 0; row < 10; row++) {
            TextView textView = addTextView("Monday", row);
            gridLayout.addView(textView);

            ImageView imageView = addImageView("https://cdn.weatherapi.com/weather/128x128/day/116.png", row);
            gridLayout.addView(imageView);

            LinearLayout linearLayout = addLinearLayout(row);
            gridLayout.addView(linearLayout);
        }

        return view;

    }

    //Create a textView.
    public TextView addTextView(String text, int row) {
        TextView textView = new TextView(getActivity());
        textView.setText(text);
//        textView.setPadding(
//                dpToPx(20),
//                dpToPx(20),
//                dpToPx(20),
//                dpToPx(20)
//        );
        textView.setGravity(Gravity.CENTER);
//        textView.setBackgroundResource(R.drawable.hightlight_border);

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
    public ImageView addImageView(String imgURL, int row) {
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
    public LinearLayout addLinearLayout(int row){
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
        weather.setGravity(Gravity.CENTER);
        weather.setText("Rain");
        linearLayout.addView(weather);

        TextView temperature = new TextView(getActivity());
        temperature.setGravity(Gravity.CENTER);
        temperature.setText("20C - 30C");
        linearLayout.addView(temperature);

        return linearLayout;
    }

    public int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

}
