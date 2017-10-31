package com.mobilerp.pathwaysstudio.mobilerp.online_mode;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.mobilerp.pathwaysstudio.mobilerp.R;
import com.mobilerp.pathwaysstudio.mobilerp.SettingsManager;

import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Eligio Becerra on 08/07/2017.
 * Copyright (C) 2017 Eligio Becerra
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public class ServiceDiscovery {

    private static final int NB_THREADS = 10;
    final Context context;
    final String LOG_TAG = "NetScan";
    final String prefsFile;
    final String server_addr;
    final SettingsManager manager;
    String netPrefix;
    WifiManager wm;
    int port;
    APIServer apiServer;
    boolean serverFound;

    public ServiceDiscovery(Context context) {

        this.context = context;
        this.netPrefix = "";
        this.port = 5000;
        apiServer = new APIServer(this.context);
        prefsFile = context.getString(R.string.preferences_file);
        server_addr = context.getString(R.string.server_addr);
        manager = SettingsManager.getInstance(context);



        if (context != null) {

            wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo connectionInfo = wm.getConnectionInfo();
            Log.d("NET_NAME", wm.getConnectionInfo().getSSID());
            Log.d("NET_NAME", String.valueOf(wm.getConnectionInfo().getNetworkId()));
            int ipAddress = connectionInfo.getIpAddress();
            String ipString = Formatter.formatIpAddress(ipAddress);
            netPrefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
        }

    }

    public boolean isServerFound() {
        return serverFound;
    }

    public void doScan() {
        if (wm.getWifiState() == WifiManager.WIFI_STATE_DISABLED || wm.getWifiState() == WifiManager.WIFI_STATE_UNKNOWN) {
            Log.d("NET_NAME", "NOT CONNECTED");
            Toast.makeText(context, R.string.not_connected, Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(context, "Start scanning", Toast.LENGTH_LONG).show();

        ExecutorService executor = Executors.newFixedThreadPool(NB_THREADS);
        for (int dest = 0; dest < 255; dest++) {
            String host = netPrefix + dest;
            executor.execute(pingRunnable(host));
        }

        Toast.makeText(context, "Waiting for executor to terminate...", Toast.LENGTH_LONG).show();
        executor.shutdown();
        try {
            executor.awaitTermination(60 * 1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ignored) {
        }

        Toast.makeText(context, "Scan finished", Toast.LENGTH_LONG).show();
    }

    private Runnable pingRunnable(final String host) {
        return new Runnable() {
            public void run() {
                try {
                    InetAddress inet = InetAddress.getByName(host);
                    if (inet.isReachable(1000)) {
                        final String _url = "http:/" + (inet.toString() + ":" + String.valueOf
                                (port));
                        if (testPort(inet)) {
                            Log.d(LOG_TAG, "Successful connection to port 5000 @ " + _url);
                            URLs URL = URLs.getInstance();
                            URL.setBASE_URL(_url);
                            Log.d(LOG_TAG, "BASE_URL set to :: " + _url);

                            manager.saveString(server_addr, _url);
                            Log.d(LOG_TAG, "Wrote to prefs file :: " + _url);

                            //testServer(_url);
//                            ExecutorService executor = Executors.newFixedThreadPool(2);
//                            executor.execute(testServer(_url));
                        }
                    }
                } catch (UnknownHostException e) {
                    Log.e(LOG_TAG, "Not found", e);
                } catch (IOException e) {
                    Log.e(LOG_TAG, "IO Error", e);
                }
            }
        };
    }

    private void testServer(final String url) {
        Log.d(LOG_TAG, "Testing server response @ " + url);
                apiServer.getResponse(false, Request.Method.GET, url,
                        null, new VolleyCallback() {
                            @Override
                            public void onSuccessResponse(JSONObject result) {
                                Log.d(LOG_TAG, "VALID SERVER @ " + url);
                                URLs URL = URLs.getInstance();
                                URL.setBASE_URL(url);
                            }

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(LOG_TAG, "Why am I here? " + url);
                            }
                        });
    }

//    private Runnable testServer(final String url) {
//        return new Runnable() {
//            @Override;
//            public void run() {
//                Log.d(LOG_TAG, "Testing server response @ " + url);
//                apiServer.getResponse(false, Request.Method.GET, url,
//                        null, new VolleyCallback() {
//                            @Override
//                            public void onSuccessResponse(JSONObject result) {
//                                Log.d(LOG_TAG, "VALID SERVER @ " + url);
//                                URLs URL = URLs.getInstance();
//                                URL.setBASE_URL(url);
//                            }
//
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.d(LOG_TAG, "Why am I here? " + url);
//                            }
//                        });
//            }
//        };
//    }

    private boolean testPort(InetAddress ip) {
        try {
            SocketAddress address = new InetSocketAddress(ip, port);
            Socket socket = new Socket();
            int timeout = 2000;

            socket.connect(address, timeout);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}