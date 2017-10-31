package com.mobilerp.pathwaysstudio.mobilerp.offline_mode;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.mobilerp.pathwaysstudio.mobilerp.R;
import com.mobilerp.pathwaysstudio.mobilerp.SettingsManager;
import com.mobilerp.pathwaysstudio.mobilerp.online_mode.APIServer;
import com.mobilerp.pathwaysstudio.mobilerp.online_mode.URLs;
import com.mobilerp.pathwaysstudio.mobilerp.online_mode.VolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Eligio Becerra on 11/09/2017.
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

public class OperationsLog {

    public static OperationsLog instance = null;
    static Context context;
    public String filename;
    public boolean fileOpen;
    File SDCardRoot, file;
    FileOutputStream output;
    FileInputStream input;
    APIServer apiServer;

    protected OperationsLog() {
        filename = context.getString(R.string.logfile);
        SDCardRoot = Environment.getExternalStorageDirectory();
        SDCardRoot = new File(SDCardRoot.getAbsolutePath() + "/MobilERP");
        file = new File(SDCardRoot, filename);
    }

    public static OperationsLog getInstance(Context _context) {
        if (instance == null) {
            context = _context;
            instance = new OperationsLog();
        }
        return instance;
    }

    public boolean isFileOpen() {
        return fileOpen;
    }

    protected void openFile() {
        try {
            output = new FileOutputStream(file, true);
            fileOpen = true;
        } catch (FileNotFoundException e) {
            fileOpen = false;
        }
    }

    protected void closeFile() {
        fileOpen = false;
        try {
            output.flush();
            output.close();
        } catch (IOException e) {

        }
    }

    public void add(int method, String endPoint, JSONObject data) {
        openFile();
        if (fileOpen) {
            try {
                String finalString = String.valueOf(method) + "\t" + endPoint + "\t" + data.toString() + "\n";
                Log.d("Final Data", finalString);
                output.write(finalString.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, R.string.error_saving_operation, Toast.LENGTH_LONG).show();
        }
        closeFile();
    }

    public void pushOperations() {
        if (SettingsManager.getInstance(context).getBoolean(context.getString(R.string
                .use_offline_mode))) {
            Toast.makeText(context, R.string.disable_offline_mode_first, Toast.LENGTH_LONG).show();
            return;
        }
        ArrayList<String> operations = loadOperations();
        if (operations == null) {
            Toast.makeText(context, "Nothing done", Toast.LENGTH_LONG).show();
            return;
        }
        apiServer = new APIServer(context);
        for (String op : operations) {
            JSONObject object = new JSONObject();
            String[] _op = op.split("\t");
            try {
                apiServer.getResponse(Integer.valueOf(_op[0]), URLs.BASE_URL + _op[1], new JSONObject
                        (_op[2]), new
                        VolleyCallback() {
                            @Override
                            public void onSuccessResponse(JSONObject result) {
                                Toast.makeText(context, R.string.srv_op_success, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, R.string.srv_op_fail, Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Nullable
    private ArrayList<String> loadOperations() {
        try {
            ArrayList<String> operations = new ArrayList<>();
            InputStream stream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader input = new BufferedReader(reader);
            String line;
            while (true) {
                line = input.readLine();
                if (line != null)
                    operations.add(line);
                else break;
            }
            destroyFile();
            return operations;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void destroyFile() {
        if (file.exists())
            file.delete();
    }
}
