package com.mobilerp.pathwaysstudio.mobilerp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

public class DisplayItems extends AppCompatActivity {

    final Context context = this;
    ListView itemList;
    ItemListAdapter itemListAdapter;
    InvokeWS ws;
    RequestResult result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_items);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ws = new InvokeWS();
        String endpoint = getIntent().getStringExtra("ENDPOINT");

        Toast.makeText(this, R.string.wait_string, Toast.LENGTH_SHORT).show();

        if (endpoint == "LISTPRODUCTS")
            ws.listProducts(new RequestResponse() {
                @Override
                public void onResponseReceived(RequestResult result) {
                    if (result.getCodeResult() == 200) {
                        Toast.makeText(context, context.getResources().getString(R.string
                                .success), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        itemList = (ListView) findViewById(R.id.itemList);
    }

//    private ArrayList<ItemListModel> putItemsInList(){
//        ArrayList<ItemListModel> model = new ArrayList<>();
//
//    }

}
