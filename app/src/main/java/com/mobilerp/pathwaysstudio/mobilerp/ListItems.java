package com.mobilerp.pathwaysstudio.mobilerp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListItems.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListItems#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListItems extends Fragment {

    ListView itemList;
    ItemListAdapter itemListAdapter;
    ArrayList<ItemListModel> items;
    APIServer apiServer;
    URLs URL = URLs.getInstance();
    String endpoint;
    private OnFragmentInteractionListener mListener;


    public ListItems() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListItems.
     */
    // TODO: Rename and change types and number of parameters
    public static ListItems newInstance(String endpoint) {
        ListItems fragment = new ListItems();
        Bundle args = new Bundle();
        args.putString("ENDPOINT", endpoint);
        fragment.setArguments(args);
        return fragment;
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
                    itemList = (ListView) getActivity().findViewById(R.id.itemList);
                    itemListAdapter = new ItemListAdapter(getContext(), items, R.layout.item_row);
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

    public void listDepleted() {
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
                    itemList = (ListView) getActivity().findViewById(R.id.itemList);
                    itemListAdapter = new ItemListAdapter(getContext(), items, R.layout.item_row);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            endpoint = getArguments().getString("ENDPOINT");

        apiServer = new APIServer(getContext());
        items = new ArrayList<>();

        if (endpoint.equals("LISTPRODUCTS")) {
            listProducts();
            getActivity().setTitle(R.string.drug_list);
        }
        if (endpoint.equals("LISTDEPLETED")) {
            listDepleted();
            getActivity().setTitle(R.string.depleted_stock);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_items, container, false);
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

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
