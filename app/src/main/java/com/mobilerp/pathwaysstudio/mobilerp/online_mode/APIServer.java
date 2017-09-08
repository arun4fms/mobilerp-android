package com.mobilerp.pathwaysstudio.mobilerp.online_mode;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mobilerp.pathwaysstudio.mobilerp.R;
import com.mobilerp.pathwaysstudio.mobilerp.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eligio Becerra on 15/06/2017.
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

public class APIServer {


    private static final User USER = User.getInstance();
    private static final URLs URL = URLs.getInstance();

    Context context;
    private VolleySingleton queue;

    public APIServer(Context ctx) {
        context = ctx;
    }

    public void getResponse(int method, String url, JSONObject jsonValues, final
    VolleyCallback callback) {

        queue = VolleySingleton.getInstance(context);

        JsonObjectRequest request = new JsonObjectRequest(method, url, jsonValues, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccessResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    callback.onErrorResponse(error);
                }
            }
        })
                //set headers
        {
            @Override
            public Map<String, String> getHeaders() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", USER.getAuthString());
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void genericErrors(int errorCode) {
        if (errorCode == 401)
            Toast.makeText(context, R.string._401_access_denied, Toast.LENGTH_LONG).show();
        if (errorCode == 500)
            Toast.makeText(context, R.string._500_server_error, Toast.LENGTH_LONG).show();
        if (errorCode == 404)
            Toast.makeText(context, R.string._404_not_found, Toast.LENGTH_LONG).show();
    }

    public void getResponse(boolean no_auth, int method, String url, JSONObject jsonValues, final
    VolleyCallback callback) {
        queue = VolleySingleton.getInstance(context);

        JsonObjectRequest request = new JsonObjectRequest(method, url, jsonValues, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.onSuccessResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    callback.onErrorResponse(error);
                }
            }
        });
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
}
