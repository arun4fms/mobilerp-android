package com.mobilerp.pathwaysstudio.mobilerp;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadFileFromURL extends AsyncTask<String, String, String> {

    private FileDownloadListener listener;

    public DownloadFileFromURL(FileDownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        listener.onFileDownloaded();
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        try {
            URL url = new URL(f_url[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Log.d("AUTH/HEADER", User.getInstance().getAuthString());
            connection.setRequestProperty("Authorization", User.getInstance().getAuthString());
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                connection.connect();
                InputStream input = new BufferedInputStream(connection.getInputStream(), 8192);
                File SDCardRoot = Environment.getExternalStorageDirectory();
                SDCardRoot = new File(SDCardRoot.getAbsolutePath() + "/MobilERP");
                SDCardRoot.mkdir();
                Log.d("CON/FILENAME", f_url[1]);
                File file = new File(SDCardRoot, f_url[1]);
                FileOutputStream output = new FileOutputStream(file);
                byte data[] = new byte[1024];

                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } else
                Log.d("CON/ERR", "Auth failed " + String.valueOf(responseCode));

        } catch (Exception e) {
            Log.d("Error: ", e.getMessage());
        }
        return null;
    }
}
