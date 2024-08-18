package com.example.lab7;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AppBroadcastReceiver extends BroadcastReceiver {
    // a string representing a city name
    private String city;
    // the context of the main activity for replacing fragments
    Context mainActivity;

    // constructor for AppBroadcast class, used for setting the mainActivity context
    public AppBroadcastReceiver(Context mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_TIME_TICK.equals(action)) {
            // update both fragments on every tick
            ((AppCompatActivity) mainActivity).getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_city_list, CityListFragment.class, null)
                    .commit();
            ((AppCompatActivity)mainActivity).getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_current_weather, CurrentWeatherFragment.class, tickBundle())
                    .commit();

            // keep this in for the demo just to make things easier
            System.out.println("************************Tick****************");

        } else if ("com.example.lab7.BROADCAST_CITY_CLICKED".equals(action)) {
            // create a new bundle with clicked cities api endpoint and then replace the fragment
            Bundle bundle = new Bundle();
            bundle.putString("city", intent.getStringExtra("city"));
            ((AppCompatActivity) mainActivity).getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_current_weather, CurrentWeatherFragment.class, bundle)
                    .commit();
        }
    }

    /**
     * Function separated for readability. During tick I needed to set the city that was being sent
     * into the CurrentWeatherFragment
     * @return - a bundle with the information of the current city the user is on
     */
    private Bundle tickBundle(){
        // for some reason on every tick the city value was being deleted and the only way I
        // could get the current city to stay on tick was to assign the variable after the tick
        TextView txt = ((AppCompatActivity) mainActivity).findViewById(R.id.text_view_current_weather_title);
        // create a new bundle to send into the CurrentWeatherFragment with the current city info
        Bundle bundle = new Bundle();
        setCity((String)txt.getText());
        bundle.putString("city", city);
        return bundle;
    }

    /**
     * Convert a shortened City name back to its usable string for the api
     * @param value - a usable string in the weather api
     */
    private void setCity(String value) {
        switch (value) {
            case ("Victoria"):
                city = "Victoria,ca";
                break;
            case ("Tokyo"):
                city = "Tokyo,jp";
                break;
            case ("Paris"):
                city = "Paris,fr";
                break;
            case ("London"):
                city = "London,gb";
                break;
            case ("Berlin"):
                city = "Berlin,de";
                break;
            case ("Beijing"):
                city = "Beijing,cn";
                break;
            case ("Toronto"):
                city = "Toronto,ca";
                break;
            case ("Mumbai"):
                city = "Mumbai,in";
                break;
            case ("Rome"):
                city = "Rome,it";
                break;
            case ("Cairo"):
                city = "Cairo,eg";
                break;
        }
    }
}
