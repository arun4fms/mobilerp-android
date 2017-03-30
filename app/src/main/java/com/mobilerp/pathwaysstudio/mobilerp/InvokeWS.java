/**
 * Created by Eligio Becerra on 01/03/2017.
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

package com.mobilerp.pathwaysstudio.mobilerp;

import android.content.res.Resources;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * This class invokes all the call in the WebService
 */
public class InvokeWS {

    /**
     * Find product endpoint
     */
    final String findProduct = "findProduct/";
    /**
     * List all product enpoint
     */
    final String listProducts = "listProducts/";
    /**
     * Check Login enpoint
     */
    final String checkLogin = "user/checkLogin/";
    final String WAIT_STRING = "Espere un momento...";
    /**
     * Base URL
     * TODO: Find a way to make this configurable, preferebly autoconfig
     */
    String baseURL = "http://192.168.1.75:5000/mobilerp/api/v1.0/";
    //String baseURL = "http://192.168.0.108:5000/mobilerp/api/v1.0/";
    /**
     * Client used by all request.
     */
    AsyncHttpClient client;

    /**
     * Finds a product in the Web Service
     */
    public void findProduct(String barcode, RequestResponse rR) {
        final RequestResult rs = new RequestResult();
        final RequestResponse requestResponse = rR;
        rs.setStringResult(WAIT_STRING);
        client = new AsyncHttpClient();
        client.setBasicAuth("carlo", "123");
        client.get(baseURL + findProduct + barcode, new AsyncHttpResponseHandler
                () {

            @Override
            public void onSuccess(int responseCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
                try {
                    rs.setJsonResult(new JSONObject(str));
                    rs.setStringResult(rs.getJsonResult().toString());
                    requestResponse.onResponseReceived(rs);
                } catch (JSONException e) {
                    rs.setStringResult(e.toString());
                    requestResponse.onResponseReceived(rs);
                }
            }

            @Override
            public void onFailure(int responseCode, Header[] headers, byte[] responseBody,
                                  Throwable throwable) {
                rs.setCodeResult(responseCode);
                rs.setStringResult(CheckWSError(responseCode));
                requestResponse.onResponseReceived(rs);
            }
        });
    }

    // List all the products in the Database
    public void listProducts(RequestResponse rR) {
        final RequestResult rs = new RequestResult();
        final RequestResponse requestResponse = rR;
        rs.setStringResult(WAIT_STRING);
        client = new AsyncHttpClient();
        client.setBasicAuth("carlo", "123");
        client.get(baseURL + listProducts, new AsyncHttpResponseHandler
                () {
            @Override
            public void onSuccess(int responseCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
                try {
                    rs.setJsonResult(new JSONObject(str));
                    rs.setStringResult(rs.getJsonResult().toString());
                    requestResponse.onResponseReceived(rs);
                } catch (JSONException e) {
                    rs.setStringResult(e.toString());
                    requestResponse.onResponseReceived(rs);
                }
            }

            @Override
            public void onFailure(int responseCode, Header[] headers, byte[] responseBody,
                                  Throwable throwable) {
                rs.setCodeResult(responseCode);
                rs.setStringResult(CheckWSError(responseCode));
                requestResponse.onResponseReceived(rs);
            }
        });
    }

    // List all the products in the WebService
    public void checkLogin(final User user, RequestResponse rR) {
        final RequestResult rs = new RequestResult();
        final RequestResponse requestResponse = rR;
        JSONObject params = new JSONObject();
        rs.setStringResult(WAIT_STRING);
        client = new AsyncHttpClient();
        StringEntity entity = null;
        try {
            entity = new StringEntity("empty");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            params.put("user", user.getName());
            params.put("pass", user.getPass());
            entity = new StringEntity(params.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.get(null, baseURL + checkLogin, entity, "application/json", new
                AsyncHttpResponseHandler
                () {

                    @Override
            public void onSuccess(int responseCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
                try {
                    rs.setJsonResult(new JSONObject(str));
                    rs.setStringResult(rs.getJsonResult().toString());
                    rs.setCodeResult(responseCode);
                    if (responseCode == 200)
                        user.setIsLoginIn(true);
                    requestResponse.onResponseReceived(rs);
                } catch (JSONException e) {
                    rs.setStringResult(e.toString());
                    rs.setCodeResult(responseCode);
                    requestResponse.onResponseReceived(rs);
                }
            }

            @Override
            public void onFailure(int responseCode, Header[] headers, byte[] responseBody,
                                  Throwable throwable) {
                rs.setCodeResult(responseCode);
                rs.setStringResult(CheckWSError(responseCode));
                requestResponse.onResponseReceived(rs);
            }
        });
    }

    private String CheckWSError(int responseCode) {
        if (responseCode == 404) {
            return Resources.getSystem().getString(R.string._404_not_found);
        }
        if (responseCode == 401) {
            return Resources.getSystem().getString(R.string._401_access_denied);
        }
        if (responseCode == 500) {
            return Resources.getSystem().getString(R.string._500_server_error);
        }
        return Resources.getSystem().getString(R.string.unknown_server_error) + Integer.toString(responseCode);
    }
}
