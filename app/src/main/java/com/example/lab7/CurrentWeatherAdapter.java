package com.example.lab7;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import java.text.DecimalFormat;

public class CurrentWeatherAdapter extends RecyclerView.Adapter<CurrentWeatherAdapter.ViewHolder>{

    // constants
    private final double ADJUST_TEMP = -273.15;
    private final int DAY_STRING_START = 5;
    private final int DAY_STRING_END = 10;
    private final int TIME_STRING_START = 11;
    private final int TIME_STRING_END = 16;
    // the list that we are pulling data from
    private JSONArray forecast;

    // Current Weather ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView date;
        TextView temp;
        TextView condition;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.text_view_current_weather_item_date);
            temp = itemView.findViewById(R.id.text_view_current_weather_item_temp);
            condition = itemView.findViewById(R.id.text_view_current_weather_item_condition);
            img = itemView.findViewById(R.id.image_view_current_weather_item_image);
        }
    }

    // constructor for the current weather adapter
    public CurrentWeatherAdapter(JSONArray forecast){this.forecast = forecast;}

    @NonNull
    @Override
    public CurrentWeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.current_weather_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentWeatherAdapter.ViewHolder holder, int position) {
        try {
            pullData(holder, position);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return forecast.length();
    }

    /**
     * Function separated for readability. Pull the data out of the current position in the JSONArray
     * and add them to the appropriate ViewHolder
     * @param holder - the ViewHolder that all the data is going into
     * @param position - the position index of the JSONArray
     * @throws JSONException - if the object is null throw an exception
     */
    private void pullData(ViewHolder holder, int position) throws JSONException {
        // pull data from the json array to form the date the forecast is taken
        String full_date = forecast.getJSONObject(position).getString("dt_txt");
        String day = full_date.substring(DAY_STRING_START,DAY_STRING_END);
        String time = full_date.substring(TIME_STRING_START,TIME_STRING_END);
        String short_date = day + "\n" + time;

        // pull the current temperature for that time period and convert it to a double and then celsius
        DecimalFormat decimal = new DecimalFormat("0.0");
        Number convert_temp = (Number) forecast.getJSONObject(position).getJSONObject("main")
                .get("temp");
        double final_temp = convert_temp.doubleValue() + ADJUST_TEMP;

        // pull the icon string out of the current json object
        JSONArray weather = forecast.getJSONObject(position).getJSONArray("weather");
        String icon = weather.getJSONObject(0).getString("icon");

        // add all the pulled data into the proper view holder
        holder.temp.setText(decimal.format(final_temp));
        holder.date.setText(short_date);
        holder.condition.setText(weather.getJSONObject(0).getString("description"));
        holder.img.setImageResource(determineIcon(icon));
    }
    /**
     * Determine the proper drawable depending on the weather icon string provided
     * @param icon - the string repressing the icon that will be used
     * @return - an int representing the drawable id for the icon
     */
    private int determineIcon(String icon){
        switch(icon){
            case("01d"):
                return R.drawable.weather_01d;
            case("01n"):
                return R.drawable.weather_01n;
            case("02d"):
                return R.drawable.weather_02d;
            case("02n"):
                return R.drawable.weather_02n;
            case("03d"):
                return R.drawable.weather_03d;
            case("03n"):
                return R.drawable.weather_03n;
            case("04d"):
                return R.drawable.weather_04d;
            case("04n"):
                return R.drawable.weather_04n;
            case("09d"):
                return R.drawable.weather_09d;
            case("09n"):
                return R.drawable.weather_09n;
            case("10d"):
                return R.drawable.weather_10d;
            case("10n"):
                return R.drawable.weather_10n;
            case("11d"):
                return R.drawable.weather_11d;
            case("11n"):
                return R.drawable.weather_11n;
            case("13d"):
                return R.drawable.weather_13d;
            case("13n"):
                return R.drawable.weather_13n;
            case("50d"):
                return R.drawable.weather_50d;
            case("50n"):
                return R.drawable.weather_50n;
            default:
                return R.drawable.ic_launcher_background;
        }
    }
}
