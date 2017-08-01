package com.mobilerp.pathwaysstudio.mobilerp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DisplayItems extends AppCompatActivity {

    final Context context = this;
    ListView itemList;
    ItemListAdapter itemListAdapter;
    ArrayList<ItemListModel> items;
    APIServer apiServer;
    URLs URL = URLs.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        apiServer = new APIServer(context);
        String endpoint = getIntent().getStringExtra("ENDPOINT");
        items = new ArrayList<>();
        //Toast.makeText(this, R.string.wait_string + " " + endpoint, Toast.LENGTH_SHORT).show();

        if (endpoint.equals("LISTPRODUCTS")) {
           listProducts();
        }
        if (endpoint.equals("LISTDEPLETED")){
            listDepleted();
        }
    }

    private void listProducts() {
        String url = URLs.BASE_URL + URLs.LIST_PRODUCTS;
        apiServer.getResponse(Request.Method.GET, url, null, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    items.add(new ItemListModel("title"));
                    JSONArray _itms = result.getJSONArray("mobilerp");
                    for (int i = 0; i < _itms.length(); i++) {
                        JSONObject _itm = _itms.getJSONObject(i);
                        //name, price, total
                        items.add(new ItemListModel(_itm.getString("name"), _itm.getDouble("price"), _itm.getInt("units")));
                    }
                    itemList = (ListView) findViewById(R.id.itemList);
                    itemListAdapter = new ItemListAdapter(context, items, R.layout.item_row);
                    itemList.setAdapter(itemListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                apiServer.genericErrors(response.statusCode);
            }
        });
    }

    public void listDepleted(){
        String url = URLs.BASE_URL + URLs.LIST_DEPLETED;
        apiServer.getResponse(Request.Method.GET, url, null, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    items.add(new ItemListModel("title"));
                    JSONArray _itms = result.getJSONArray("mobilerp");
                    for (int i = 0; i < _itms.length(); i++) {
                        JSONObject _itm = _itms.getJSONObject(i);
                        //name, price, total
                        items.add(new ItemListModel(_itm.getString("name"), _itm.getDouble("price"), _itm.getInt("units")));
                    }
                    itemList = (ListView) findViewById(R.id.itemList);
                    itemListAdapter = new ItemListAdapter(context, items, R.layout.item_row);
                    itemList.setAdapter(itemListAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                apiServer.genericErrors(response.statusCode);
            }
        });
    }

//    private ArrayList<ItemListModel> putItemsInList(){
//        ArrayList<ItemListModel> model = new ArrayList<>();
//
//    }

}
