package com.example.lab7;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CityListFragment extends Fragment {
    // broadcast receiver and manager for the tick and custom broadcast
    AppBroadcastReceiver receiver;
    LocalBroadcastManager manager;

    // constant custom broadcast string
    private final String CITY_CLICKED = "com.example.lab7.BROADCAST_CITY_CLICKED";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // set up the recycler view and adapter
        String[] list = getResources().getStringArray(R.array.cities);
        RecyclerView city_list = view.findViewById(R.id.recycler_view_city_list);
        CityListAdapter adapter = new CityListAdapter(list);
        city_list.setAdapter(adapter);

        // set up the broadcast manager
        receiver = new AppBroadcastReceiver(getActivity());
        manager = LocalBroadcastManager.getInstance(view.getContext());
        IntentFilter custom_filter = new IntentFilter(CITY_CLICKED);
        manager.registerReceiver(receiver, custom_filter);

        // onclick listener for each recycler view
        adapter.setOnClickListener((position, model) -> {
            // send the city name on click via broadcast
            Intent intent = new Intent(CITY_CLICKED);
            intent.putExtra("city", list[position] );
            if(manager != null){
                manager.sendBroadcast(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.unregisterReceiver(receiver);
    }
}