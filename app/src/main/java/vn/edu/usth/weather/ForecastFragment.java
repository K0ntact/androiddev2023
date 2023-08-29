package vn.edu.usth.weather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ForecastFragment extends Fragment {
    private ImageView imgView1;
    private TextView textView1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_forecast, container, false);
        imgView1 = v.findViewById(R.id.imgView1);
        textView1 = v.findViewById(R.id.textView1);
        return v;
    }
}