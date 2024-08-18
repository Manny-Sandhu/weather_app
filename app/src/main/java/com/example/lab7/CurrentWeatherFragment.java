package com.example.lab7;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CurrentWeatherFragment extends Fragment {

    // constants
    private final String KEY = "&APPID=badf2a1f50fcde5ad57084e98723a1e8";
    private final String DOMAIN = "https://api.openweathermap.org/data/2.5/forecast?q=";
    private final int STRING_START = 0;
    private final int STRING_OFFSET = 3;

    // String representing the city key
    private String city;

    // api variables
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // setup the City TextView
        setupCity(view);
        // setup the url for the api call
        String url = DOMAIN + city + KEY;
        mRequestQueue = Volley.newRequestQueue(view.getContext());
        mStringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                // setup the recycler view for the fragment
                JSONArray data = new JSONObject(response).getJSONArray("list");
                RecyclerView current_weather = view.findViewById(R.id.recycler_view_current_weather);
                CurrentWeatherAdapter adapter = new CurrentWeatherAdapter(data);
                current_weather.setAdapter(adapter);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            // do something if it fails
            Log.d("ERROR", "API call ERROR: " + error);
        });

        mRequestQueue.add(mStringRequest);
    }

    /**
     * Function separated for readability. Setup the City TextView and determine the correct city to show
     * @param view
     */
    private void setupCity(View view){
        // if there was a bundle sent use the city that was in the bundle
        // otherwise default to Victoria
        Bundle bundle = getArguments();
        if (bundle != null) {
            city = bundle.getString("city");
        } else {
            city = "Victoria,ca";
        }
        // Remove the country code from the string and add it to the tile TextView
        String city_name = city.substring(STRING_START, city.length() - STRING_OFFSET);
        TextView title = view.findViewById(R.id.text_view_current_weather_title);
        title.setText(city_name);
    }

}

