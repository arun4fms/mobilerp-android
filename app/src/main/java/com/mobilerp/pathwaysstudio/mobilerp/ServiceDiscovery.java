package com.mobilerp.pathwaysstudio.mobilerp;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
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
    String LOG_TAG = "NetScan";
    String netPrefix;
    ArrayList<InetAddress> validHosts;
    int port;
    APIServer apiServer;

    public ServiceDiscovery(Context context) {

        this.context = context;
        this.netPrefix = "";
        this.validHosts = new ArrayList<>();
        this.port = 5000;
        apiServer = new APIServer(this.context);

        if (context != null) {

            WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo connectionInfo = wm.getConnectionInfo();
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
                    boolean reachable = inet.isReachable(1000);
                    if (reachable) {
                        Log.d(LOG_TAG, host + " => Result: reachable");
                        //validHosts.add(inet);
                        if (testPort(inet)) {
                            final String url = inet.toString() + ":" + String.valueOf(port);
                            Log.d(LOG_TAG, "Server found at " + url);
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