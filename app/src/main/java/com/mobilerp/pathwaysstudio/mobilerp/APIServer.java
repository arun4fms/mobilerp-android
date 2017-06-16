package com.mobilerp.pathwaysstudio.mobilerp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

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

    //public static final String BASE_URL = "http://192.168.1.70:5000/";
    public static final String BASE_URL = "http://192.168.0.104:5000/";
    public static final String LOGIN = "mobilerp/api/v1.0/user/checkLogin/";
    public static final String LIST_PRODUCTS = "mobilerp/api/v1.0/listProducts/";
    public static final String LIST_DEPLETED = "mobilerp/api/v1.0/listDepletedProducts/";

    final static User USER = User.getInstance();
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
                    if (response.statusCode == 401)
                        Toast.makeText(context, R.string._401_access_denied, Toast.LENGTH_LONG).show();
                    if (response.statusCode == 500)
                        Toast.makeText(context, R.string._500_server_error, Toast.LENGTH_LONG).show();
                }
            }
        })
                //set headers
        {
            @Override
            public Map<String, String> getHeaders() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", USER.getAuthString());
                Log.d("PARAMS", params.toString());
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
}
