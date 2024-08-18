package com.example.lab7;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    AppBroadcastReceiver receiver;
    IntentFilter time_filter = new IntentFilter(Intent.ACTION_TIME_TICK);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setupFragments(savedInstanceState);

        receiver = new AppBroadcastReceiver(this);
        registerReceiver(receiver, time_filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    /**
     * This function adds the weather alert fragment but only if it is not already running
     */
    private void checkWeatherAlert(){
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // check if the weather alert fragment is already running or not
            WeatherAlertFragment check = (WeatherAlertFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_weather_alert);
            // only actually start the fragment if nothing was found
            if(check == null) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragment_weather_alert, WeatherAlertFragment.class, null)
                        .commit();
            }
        }
    }

    /**
     * Set up all the fragments if they are not already setup
     * @param savedInstanceState - A bundle representing the current savedInstanceState,
     *                           will only be null once
     */
    private void setupFragments(Bundle savedInstanceState){
        checkWeatherAlert();
        if(savedInstanceState == null){
            Bundle bundle = new Bundle();
            Intent callingIntent = getIntent();
            String city = callingIntent.getStringExtra("city");
            if(city != null){
                bundle.putString("city", city);
            }else {
                bundle.putString("city","Victoria,ca");
            }
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_city_list, CityListFragment.class, null)
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_current_weather, CurrentWeatherFragment.class, bundle)
                    .commit();
        }
    }
}
