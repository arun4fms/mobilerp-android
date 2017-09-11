package com.mobilerp.pathwaysstudio.mobilerp;


import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilerp.pathwaysstudio.mobilerp.offline_mode.SQLHandler;
import com.mobilerp.pathwaysstudio.mobilerp.online_mode.DownloadFileFromURL;
import com.mobilerp.pathwaysstudio.mobilerp.online_mode.FileDownloadListener;
import com.mobilerp.pathwaysstudio.mobilerp.online_mode.ServiceDiscovery;
import com.mobilerp.pathwaysstudio.mobilerp.online_mode.URLs;


/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {

    EditText etServerAddr;
    Button btnScanServer, btnBackupDB;
    CheckBox cbOfflineMode;
    AppBarLayout ablMainBar;
    SettingsManager manager;


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
        manager = SettingsManager.getInstance(context);

        String serverAddress = manager.getString(getString(R.string.server_addr));
        Boolean useOfflineMode = manager.getBoolean(getString(R.string.use_offline_mode));

        ablMainBar = (AppBarLayout) getView().findViewById(R.id.ablMainBar);
        etServerAddr = (EditText) getView().findViewById(R.id.etServerAddr);
        etServerAddr.setText(serverAddress);

        etServerAddr.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || event.getKeyCode() == KeyEvent.KEYCODE_ENTER ||
                        event.getKeyCode() == KeyEvent.KEYCODE_NUMPAD_ENTER) {
                    Context context = getContext();
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context
                            .INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    manager.saveString(getString(R.string.server_addr), etServerAddr.getText().toString());

                    Toast.makeText(getContext(), R.string.server_addr_updated, Toast
                            .LENGTH_LONG).show();
                }
                return false;
            }
        });

        btnScanServer = (Button) getView().findViewById(R.id.btnScanServer);
        btnScanServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();

                Toast.makeText(context, R.string.searching_server, Toast.LENGTH_LONG).show();
                ServiceDiscovery ds = new ServiceDiscovery(context);
                ds.doScan();
                String serverAddress = manager.getString(getString(R.string.server_addr));
                etServerAddr.setText(serverAddress);

            }
        });

        btnBackupDB = (Button) getView().findViewById(R.id.btnBackupDB);
        btnBackupDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Download db started", Toast.LENGTH_LONG).show();
                DownloadFileFromURL fileDownloader = new DownloadFileFromURL(new FileDownloadListener() {
                    @Override
                    public void onFileDownloaded() {
                        Toast.makeText(getContext(), R.string.download_finished, Toast.LENGTH_LONG).show();
                    }
                });
                fileDownloader.execute(URLs.BASE_URL + URLs.DB_BACKUP, getString(R.string.database_name));
                if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    Log.d("DOWN_ERR", "SD \n" + Environment.getExternalStorageState());
                    return;
                }
            }
        });

        cbOfflineMode = (CheckBox) getView().findViewById(R.id.cbOfflineMode);
        cbOfflineMode.setChecked(useOfflineMode);
        Toast.makeText(getContext(), (useOfflineMode) ? getString(R.string.offline_mode_enabled) : getString(R.string.offline_mode_disabled), Toast.LENGTH_LONG).show();
        cbOfflineMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();
                SettingsManager manager = SettingsManager.getInstance(context);
                Boolean useOfflineMode;
                useOfflineMode = cbOfflineMode.isChecked();
                if (useOfflineMode) {
                    SQLHandler handler = SQLHandler.getInstance(context);
                    if (handler.isDatabaseOpen())
                        Toast.makeText(context, (useOfflineMode) ? R.string.offline_mode_enabled : R.string.offline_mode_disabled, Toast.LENGTH_LONG).show();
                    else {
                        Toast.makeText(context, R.string.no_db_file, Toast.LENGTH_LONG).show();
                        useOfflineMode = false;
                        cbOfflineMode.setChecked(useOfflineMode);
                    }
                } else {
                    Toast.makeText(context, R.string.offline_mode_disabled, Toast.LENGTH_LONG).show();
                }
                manager.saveBoolean(getString(R.string.use_offline_mode), useOfflineMode);
            }
        });
    }
}
