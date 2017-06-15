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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    final Context context = this;
    User user = User.getInstance();
    TabHost host;
    ListView pharmacyList;
    ListView salesList;
    OptionListAdapter pharmacyListAdapter;
    OptionListAdapter salesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        /*if (!user.getIsLoginIn()) {
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
        }*/

        user.setName("carlo");
        user.setName("123");
        user.setIsLoginIn(true);

        tabsSetup();

        // Pharmacy list setup
        pharmacyList = (ListView) findViewById(R.id.lvPharmacyOptions);
        pharmacyListAdapter = new OptionListAdapter(this, pharmacyList());
        pharmacyList.setAdapter(pharmacyListAdapter);
        pharmacyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "HURR " + position,
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Intent intent = new Intent(context, DisplayItems.class);
                        intent.putExtra("ENDPOINT", pharmacyListAdapter.getItem(position)
                                .getEndpoint());
                        startActivity(intent);
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "DERP " + position,
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "DAYUM " + position,
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        // Sales list setup
        salesList = (ListView) findViewById(R.id.lvSalesOptions);
        salesListAdapter = new OptionListAdapter(this, salesList());
        salesList.setAdapter(salesListAdapter);

    }

    private ArrayList<OptionListModel> pharmacyList() {
        ArrayList<OptionListModel> models = new ArrayList<OptionListModel>();
        //models.add(new OptionListModel("Acciones"));
        models.add(new OptionListModel(R.mipmap.ic_launcher, this.getResources().getString(R
                .string.stock_input), ""));
        models.add(new OptionListModel(R.mipmap.ic_launcher, this.getResources().getString(R.string.drug_list), "LISTPRODUCTS"));
        models.add(new OptionListModel(R.mipmap.ic_launcher, this.getResources().getString(R.string.depleted_stock), ""));

        return models;
    }

    private ArrayList<OptionListModel> salesList() {
        ArrayList<OptionListModel> models = new ArrayList<OptionListModel>();
        //models.add(new OptionListModel("Acciones"));
        models.add(new OptionListModel(R.mipmap.ic_launcher, this.getResources().getString(R.string.today_sales), ""));
        models.add(new OptionListModel(R.mipmap.ic_launcher, this.getResources().getString(R.string.month_sales), ""));
        models.add(new OptionListModel(R.mipmap.ic_launcher, this.getResources().getString(R
                .string.most_sold_products), ""));
        models.add(new OptionListModel(R.mipmap.ic_launcher, this.getResources().getString(R.string.least_sold_products), ""));
        models.add(new OptionListModel(R.mipmap.ic_launcher, this.getResources().getString(R.string.custom_statement), ""));

        return models;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu, this adds items to th action bar if present
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void tabsSetup() {
        host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        // tab 1
        TabHost.TabSpec spec = host.newTabSpec(this.getResources().getString(R.string.pharmacy));
        spec.setContent(R.id.pharmacy_tab);
        spec.setIndicator(this.getResources().getString(R.string.pharmacy));
        host.addTab(spec);

        // tab 2
        spec = host.newTabSpec(this.getResources().getString(R.string.sales));
        spec.setContent(R.id.sales_tab);
        spec.setIndicator(this.getResources().getString(R.string.sales));
        host.addTab(spec);

        // tab 3
        spec = host.newTabSpec(this.getResources().getString(R.string.patients));
        spec.setContent(R.id.px_tab);
        spec.setIndicator(this.getResources().getString(R.string.patients));
        host.addTab(spec);
    }
}
