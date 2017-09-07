package com.mobilerp.pathwaysstudio.mobilerp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;

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
    String netPrefix;
    int port;
    APIServer apiServer;

    public ServiceDiscovery(Context context) {

        this.context = context;
        this.netPrefix = "";
        this.port = 5000;
        apiServer = new APIServer(this.context);
        prefsFile = context.getString(R.string.preferences_file);
        server_addr = context.getString(R.string.server_addr);


        if (context != null) {

            WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo connectionInfo = wm.getConnectionInfo();
            Log.d("NET_NAME", wm.getConnectionInfo().getSSID());
            Log.d("NET_NAME", String.valueOf(wm.getConnectionInfo().getNetworkId()));
            int ipAddress = connectionInfo.getIpAddress();
            String ipString = Formatter.formatIpAddress(ipAddress);
            netPrefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
        }
    }

    public void doScan() {
        Log.i(LOG_TAG, "Start scanning");

        ExecutorService executor = Executors.newFixedThreadPool(NB_THREADS);
        for (int dest = 0; dest < 255; dest++) {
            String host = netPrefix + dest;
            executor.execute(pingRunnable(host));
        }

        Log.i(LOG_TAG, "Waiting for executor to terminate...");
        executor.shutdown();
        try {
            executor.awaitTermination(60 * 1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ignored) {
        }

        Log.i(LOG_TAG, "Scan finished");

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
                            SharedPreferences sharedPrefs = context.getSharedPreferences(prefsFile, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPrefs.edit();
                            editor.putString(server_addr, _url);
                            editor.commit();
                            Log.d(LOG_TAG, "Wrote to prefs file" + _url);
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