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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TabHost;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    User user = User.getInstance();

    TabHost host;
    ListView listView;
    CustomListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        /*if (!user.getIsLoginIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }*/

        host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        // tab 1
        TabHost.TabSpec spec = host.newTabSpec("Farmacia");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Farmacia");
        host.addTab(spec);

        // tab 2
        spec = host.newTabSpec("Ventas");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Ventas");
        host.addTab(spec);

        // tab 3
        spec = host.newTabSpec("Pacientes");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Pacientes");
        host.addTab(spec);

        //listView = (ListView) findViewById(R.id.lvPharmOptions);


        //listAdapter = new CustomListAdapter(this, generateData());

        //listView.setAdapter(listAdapter);
    }

    private ArrayList<CustomListModel> generateData() {
        ArrayList<CustomListModel> models = new ArrayList<CustomListModel>();
        models.add(new CustomListModel("Acciones"));
        models.add(new CustomListModel(R.drawable.medicine, "Lista de medicinas"));
        models.add(new CustomListModel(R.drawable.money, "Corte del dia"));
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
}
