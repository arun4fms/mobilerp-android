package com.mobilerp.pathwaysstudio.mobilerp;

/**
 * Created by Eligio Becerra on 24/08/2017.
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

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.HashMap;
import java.util.Map;

public class InputStreamVolleyRequest extends Request<byte[]> {

    private static final User USER = User.getInstance();
    private static final URLs URL = URLs.getInstance();
    private final Response.Listener<byte[]> mListener;
    //create a static map for directly accessing headers
    public Map<String, String> responseHeaders;
    private Map<String, String> mParams;

    public InputStreamVolleyRequest(int post, String mUrl, Response.Listener<byte[]> listener,
                                    Response.ErrorListener errorListener, HashMap<String, String> params) {
        super(post, mUrl, errorListener);
        // this request would never use cache.
        setShouldCache(false);
        mListener = listener;
        try {
            mParams = getParams();
        } catch (AuthFailureError error) {
            Log.d("AUTH ERROR", error.getMessage());
        }
    }

    @Override
    protected Map<String, String> getParams()
            throws com.android.volley.AuthFailureError {
        mParams = new HashMap<>();
        mParams.put("Authorization", USER.getAuthString());
        return mParams;
    }


    @Override
    protected void deliverResponse(byte[] response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {

        //Initialise local responseHeaders map with response headers received
        responseHeaders = response.headers;

        //Pass the response data here
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }
}