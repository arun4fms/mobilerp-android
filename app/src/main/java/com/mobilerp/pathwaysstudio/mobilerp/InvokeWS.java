package com.mobilerp.pathwaysstudio.mobilerp;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mloki on 01/03/2017.
 */

/**
 * This class invokes all the call in the WebService
 */
public class InvokeWS {

    /**
     * Base URL
     * TODO: Find a way to make this configurable, preferebly autoconfig
     */
    String baseURL = "http://192.168.1.70:5000/mobilerp/api/v1.0/";

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
    final String checkLogin = "checkLogin/";

    /**
     * Client used by all request.
     */
    AsyncHttpClient client;

    final String WAIT_STRING = "Espere un momento...";

    /**
     * Finds a product in the Web Service
     */
    public void findProduct(String barcode, RequestResponse rR){
        final RequestResult  rs = new RequestResult();
        final RequestResponse requestResponse = rR;
        rs.setStringResult(WAIT_STRING);
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
                    requestResponse.onResponseReceived(rs);
                }catch (JSONException e){
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
        requestResponse.onResponseReceived(rs);
    }

    // List all the products in the WebService
    public void listProducts(RequestResponse rR){
        final RequestResult  rs = new RequestResult();
        final RequestResponse requestResponse = rR;
        rs.setStringResult(WAIT_STRING);
        client = new AsyncHttpClient();
        client.setBasicAuth("carlo","123");
        client.get(baseURL + listProducts, new AsyncHttpResponseHandler
                () {
            @Override
            public void onSuccess(int responseCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
                try{
                    rs.setJsonResult(new JSONObject(str));
                    rs.setStringResult(rs.getJsonResult().toString());
                    requestResponse.onResponseReceived(rs);
                }catch (JSONException e){
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
        requestResponse.onResponseReceived(rs);
    }

    // List all the products in the WebService
    public void checkLogin(String user, String pass, RequestResponse rR){
        final RequestResult  rs = new RequestResult();
        final RequestResponse requestResponse = rR;
        rs.setStringResult(WAIT_STRING);
        client = new AsyncHttpClient();
        JSONObject loginData = new JSONObject();
        //loginData.keys()
        client.get(baseURL + checkLogin, new AsyncHttpResponseHandler
                () {
            @Override
            public void onSuccess(int responseCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);
                try{
                    rs.setJsonResult(new JSONObject(str));
                    rs.setStringResult(rs.getJsonResult().toString());
                    requestResponse.onResponseReceived(rs);
                }catch (JSONException e){
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
        requestResponse.onResponseReceived(rs);
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
