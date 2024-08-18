package com.example.lab7;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {

    // constants
    private final String KEY = "&APPID=badf2a1f50fcde5ad57084e98723a1e8";
    private final String DOMAIN = "https://api.openweathermap.org/data/2.5/weather?q=";
    private final double ADJUST_TEMP = -273.15;

    // string representing api city queryStringParameter
    private String city;
    // city list array for the adapter
    private String[] city_list;
    // request queue and string for the volley api call
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;


    // constructor for the city list adapter
    public CityListAdapter(String[] city_list){this.city_list = city_list;}


    // OnClickListener object to store the onCLickListener Instance
    private static OnClickListener onClickListener;
    // interface for the OnClickListener
    public interface OnClickListener { void onClick(int position, View model);}
    // function to set the OnClickListener for the adapter
    public void setOnClickListener(OnClickListener onClickListener) {this.onClickListener = onClickListener;}


    // ViewHolder inner class
    public static class ViewHolder extends RecyclerView.ViewHolder{
        // Views that need populating
        TextView city;
        TextView temp;
        TextView high;
        TextView low;
        ImageView img;

        public ViewHolder(@NonNull View cityView) {
            super(cityView);
            city = cityView.findViewById(R.id.text_view_city_list_name);
            temp = cityView.findViewById(R.id.text_view_city_list_temp);
            high = cityView.findViewById(R.id.text_view_city_list_temp_high);
            low = cityView.findViewById(R.id.text_view_city_list_temp_low);
            img = cityView.findViewById(R.id.image_view_city_list_weather);
            itemView.setOnClickListener(view -> {
                if (onClickListener != null) {
                    onClickListener.onClick(getAdapterPosition(), itemView);
                }
            });
        }
    }

    @NonNull
    @Override
    public CityListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_list_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityListAdapter.ViewHolder holder, int position) {
        // set up the api call url
        city = city_list[position];
        String url = DOMAIN + city + KEY;

        // the api call
        mRequestQueue = Volley.newRequestQueue(holder.itemView.getContext());
        mStringRequest = new StringRequest(Request.Method.GET, url, response -> {
            try {
                pullData(holder, response);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            // log Errors
            Log.d("Error", "API error" + error);
        });
        mRequestQueue.add(mStringRequest);
    }

    @Override
    public int getItemCount() {
        return city_list.length;
    }

    /**
     * Function separated for readability purposes. Take a response and add all the needed
     * data from the response into the appropriate holder view
     * @param holder - the ViewHolder for the adapter
     * @param response - the response from the api call
     * @throws JSONException - for when pulling null data
     */
    private void pullData(ViewHolder holder, String response) throws JSONException {
        // pull the city name from the api call
        String cityName = (String) new JSONObject(response).get("name");
        // pull the icon data from the api call
        String icon = (String) new JSONObject(response).getJSONArray("weather")
                .getJSONObject(0).get("icon");
        // add the data to the holder
        holder.city.setText(cityName);
        holder.img.setImageResource(determineIcon(icon));
        // pull all 3 temperature calls
        pullTemp(holder, response);
    }

    /**
     * Function separated for readability purposes. Pull only the temperature data to keep things
     * together
     * @param holder - the ViewHolder for the adapter
     * @param response - the response from the api call
     * @throws JSONException - for when pulling null data
     */
    private void pullTemp(ViewHolder holder, String response) throws JSONException {
        // create a decimal formatter for the proper format for the temperature strings
        DecimalFormat decimal = new DecimalFormat("0.0");
        // pull all the temperature data from the api call
        Number temp =  (Number) new JSONObject(response).getJSONObject("main")
                .get("temp");
        Number high =  (Number) new JSONObject(response).getJSONObject("main")
                .get("temp_max");
        Number low =  (Number) new JSONObject(response).getJSONObject("main")
                .get("temp_min");
        // convert the kelvin temperatures to celsius
        double final_temp = temp.doubleValue() + ADJUST_TEMP;
        double final_high = high.doubleValue() + ADJUST_TEMP;
        double final_low = low.doubleValue() + ADJUST_TEMP;
        // set the temperature strings in the ViewHolder
        holder.temp.setText(decimal.format(final_temp));
        holder.high.setText(decimal.format(final_high));
        holder.low.setText(decimal.format(final_low));
    }

    /**
     * Function that determines the image that will used for the view
     * @param icon - a string representing the icon number provided by the api
     * @return - an int representing the drawable id for the image being used
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
