package com.mobilerp.pathwaysstudio.mobilerp;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {


    public Settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = getContext();
        SharedPreferences sharedPrefs = context.getSharedPreferences(getString(R.string
                .preferences_file), Context.MODE_PRIVATE);
        String serverAddress = sharedPrefs.getString(getString(R.string.server_addr), null);
        Boolean useOfflineMode = sharedPrefs.getBoolean(getString(R.string.use_offline_mode),
                false);
    }
}
