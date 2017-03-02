package com.mobilerp.pathwaysstudio.mobilerp;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mloki on 01/03/2017.
 */

public class InvokeWS {

    String baseURL = "http://192.168.1.70:5000/mobilerp/api/v1.0/";
    String findProduct = "/findProduct/";
    AsyncHttpClient client;

    // Finds a product in the Web Service
    public RequestResult findProduct(String barcode){
        final RequestResult  rs = new RequestResult();
        rs.setStringResult("Lol not finished");
        client = new AsyncHttpClient();
        client.setBasicAuth("carlo","123");
        client.get(baseURL + findProduct + barcode, new AsyncHttpResponseHandler
                () {
            @Override
            public void onSuccess(int responseCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
                try{
                    rs.setJsonResult(new JSONObject(str));
                    rs.setStringResult(rs.getJsonResult().toString());
                }catch (JSONException e){
                    rs.setStringResult(e.toString());
                }
            }
            @Override
            public void onFailure(int responseCode, Header[] headers, byte[] responseBody,
                                  Throwable throwable) {
                rs.setCodeResult(responseCode);
                rs.setStringResult(CheckWSError(responseCode));
            }
        });
        return rs;
    }

    private String CheckWSError(int responseCode){
        if (responseCode == 404){
            return "Elemento no encontrado";
        }
        if (responseCode == 401){
            return  "Acceso Denegado";
        }
        if (responseCode == 500){
            return "Error del servidor";
        }
        return "Error desconocido del servidor" + Integer.toString(responseCode);
    }
}
