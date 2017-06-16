package com.mobilerp.pathwaysstudio.mobilerp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.android.volley.Request;

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
            String url = APIServer.BASE_URL + APIServer.LIST_PRODUCTS;
            apiServer.getResponse(Request.Method.GET, url, null, new VolleyCallback() {
                @Override
                public void onSuccessResponse(JSONObject result) {
                    try {
                        JSONArray _itms = result.getJSONArray("mobilerp");
                        for (int i = 0; i < _itms.length(); i++) {
                            JSONObject _itm = _itms.getJSONObject(i);
                            //name, price, total
                            items.add(new ItemListModel(_itm.getString("name"), _itm.getDouble("price"), _itm.getInt("units")));
                        }
                        itemList = (ListView) findViewById(R.id.itemList);
                        itemListAdapter = new ItemListAdapter(context, items);
                        itemList.setAdapter(itemListAdapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void setItemListContent() {
        //Set up List

    }

//    private ArrayList<ItemListModel> putItemsInList(){
//        ArrayList<ItemListModel> model = new ArrayList<>();
//
//    }

}
