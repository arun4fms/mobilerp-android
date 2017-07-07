package com.mobilerp.pathwaysstudio.mobilerp;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminFragment extends Fragment {

    User user = User.getInstance();
    TabHost host;
    ListView pharmacyList;
    ListView salesList;
    OptionListAdapter pharmacyListAdapter;
    OptionListAdapter salesListAdapter;


    public AdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        user.setName("carlo");
        user.setPass("123");
        user.setIsLoginIn(true);

        tabsSetup();

        // Pharmacy list setup
        pharmacyList = (ListView) getView().findViewById(R.id.lvPharmacyOptions);
        pharmacyListAdapter = new OptionListAdapter(getContext(), pharmacyList());
        pharmacyList.setAdapter(pharmacyListAdapter);
        pharmacyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(getContext(), BarcodeScanner.class);
                        intent.putExtra("ENDPOINT", pharmacyListAdapter.getItem(position)
                                .getEndpoint());
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(getContext(), DisplayItems.class);
                        intent.putExtra("ENDPOINT", pharmacyListAdapter.getItem(position)
                                .getEndpoint());
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(getContext(), DisplayItems.class);
                        intent.putExtra("ENDPOINT", pharmacyListAdapter.getItem(position)
                                .getEndpoint());
                        startActivity(intent);
                        break;
                    default:
                        Toast.makeText(getContext(), "DAYUM " + position,
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        // Sales list setup
        salesList = (ListView) getView().findViewById(R.id.lvSalesOptions);
        salesListAdapter = new OptionListAdapter(getContext(), salesList());
        salesList.setAdapter(salesListAdapter);

    }

    private ArrayList<OptionListModel> pharmacyList() {
        ArrayList<OptionListModel> models = new ArrayList<OptionListModel>();
        //models.add(new OptionListModel("Acciones"));
        models.add(new OptionListModel(R.mipmap.ic_launcher, this.getResources().getString(R
                .string.stock_input), "UPDATESTOCK"));
        models.add(new OptionListModel(R.mipmap.ic_launcher, this.getResources().getString(R.string.drug_list), "LISTPRODUCTS"));
        models.add(new OptionListModel(R.mipmap.ic_launcher, this.getResources().getString(R.string.depleted_stock), "LISTDEPLETED"));

        return models;
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // inflate the menu, this adds items to th action bar if present
//        getMenuInflater().inflate(R.menu.menu_admin, menu);
//        return true;
//    }

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void tabsSetup() {
        host = (TabHost) getView().findViewById(R.id.tabHost);
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
