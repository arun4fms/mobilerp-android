package com.mobilerp.pathwaysstudio.mobilerp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.mobilerp.pathwaysstudio.mobilerp.online_mode.APIServer;
import com.mobilerp.pathwaysstudio.mobilerp.online_mode.URLs;
import com.mobilerp.pathwaysstudio.mobilerp.online_mode.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FinishSell.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FinishSell#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FinishSell extends Fragment {

    final Fragment me = this;
    // Objects
    TextView tvTotalSale;
    ArrayList<SalesItem> items;
    ItemListAdapter itemListAdapter;
    ListView lvSalesItems;
    ArrayList<ItemListModel> itemsListModel;
    Button btnEndSale;
    private OnFragmentInteractionListener mListener;

    public FinishSell() {
        // Required empty public constructor
    }

    public static FinishSell newInstance(ArrayList<SalesItem> _items) {
        FinishSell fragment = new FinishSell();
        Bundle args = new Bundle();
        args.putParcelableArrayList("SalesData", _items);
        fragment.setArguments(args);
        return fragment;
    }

    private void initUI() {
        getActivity().setTitle(R.string.finish_sale);
        tvTotalSale = (TextView) getView().findViewById(R.id.totalSale);
        lvSalesItems = (ListView)getView().findViewById(R.id.itemSalesList);
        btnEndSale = (Button) getView().findViewById(R.id.finish_sale);
        itemsListModel = new ArrayList<>();

        btnEndSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIServer apiServer = new APIServer(getContext());
                URLs URL = URLs.getInstance();
                JSONObject data = new JSONObject();
                try {
                    JSONArray barcode = new JSONArray();
                    JSONArray units = new JSONArray();
                    for (int i = 0; i < items.size(); i++) {
                        barcode.put(i, items.get(i).barcode);
                        units.put(i, items.get(i).amount);
                    }
                    data.put("barcode", barcode);
                    data.put("units", units);
                    Log.d("DATA", data.toString());
                    apiServer.getResponse(Request.Method.POST, URLs.BASE_URL + URLs.MAKE_SALE,
                            data, new VolleyCallback() {
                                @Override
                                public void onSuccessResponse(JSONObject result) {
                                    Toast.makeText(getContext(), R.string.srv_op_success, Toast
                                            .LENGTH_LONG).show();
                                    getActivity().setTitle(R.string.manager);
                                    getActivity().getSupportFragmentManager()
                                            .beginTransaction()
                                            .remove(me)
                                            .commit();
                                }

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), R.string.srv_op_fail, Toast
                                            .LENGTH_LONG).show();
                                }
                            });
                }catch (JSONException ignored){

                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            items = getArguments().getParcelableArrayList("SalesData");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finish_sell, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();
        if (items != null) {
            double total_sale = 0.0;
            for (int i = 0; i < items.size(); i++) {
                itemsListModel.add(new ItemListModel(items.get(i).name, items.get(i).price, items
                                .get(i).amount));
                total_sale += (items.get(i).price * items
                        .get(i).amount);
            }
            itemListAdapter = new ItemListAdapter(getContext(), itemsListModel, R.layout.item_sales_row);
            lvSalesItems.setAdapter(itemListAdapter);
            tvTotalSale.setText(getString(R.string.total_sale) + " : " + String.valueOf
                    (total_sale));
        } else {
            tvTotalSale.setText(R.string.data_fail);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
