package com.mobilerp.pathwaysstudio.mobilerp;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


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
    String reportURL, reportName;
    Button btnDownloadPDF;
    int count;

    private OnFragmentInteractionListener mListener;

    public ListItems() {
        // Required empty public constructor
    }

    public static ListItems newInstance(String endpoint) {
        ListItems fragment = new ListItems();
        Bundle args = new Bundle();
        args.putString("ENDPOINT", endpoint);
        fragment.setArguments(args);
        return fragment;
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
            reportURL = URLs.BASE_URL + URLs.SALES_REPORT;
            reportName = getString(R.string.sales_report_filename);
            getActivity().setTitle(R.string.drug_list);
        }
        if (endpoint.equals("LISTDEPLETED")) {
            listDepleted();
            reportURL = URLs.BASE_URL + URLs.DEPLETED_REPORT;
            reportName = getString(R.string.depleted_report_filename);
            getActivity().setTitle(R.string.depleted_stock);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnDownloadPDF = (Button) getView().findViewById(R.id.btn_download_pdf);
        btnDownloadPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Download started", Toast.LENGTH_LONG).show();
                SimpleDateFormat date = new SimpleDateFormat("dd-MM-yy");
                Date now = new Date();
                DownloadFileFromURL fileDownloader = new DownloadFileFromURL(new FileDownloadListener() {
                    @Override
                    public void onFileDownloaded() {
                        Toast.makeText(getContext(), R.string.download_finished, Toast.LENGTH_LONG).show();
                    }
                });
                fileDownloader.execute(reportURL, reportName + date.format(now) + ".pdf");
                if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    Log.d("DOWN_ERR", "SD \n" + Environment.getExternalStorageState());
                    return;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_items, container, false);
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
                    items.add(new ItemListModel("title_"));
                    JSONArray _itms = result.getJSONArray("mobilerp");
                    for (int i = 0; i < _itms.length(); i++) {
                        JSONObject _itm = _itms.getJSONObject(i);
                        //name, price, total
                        items.add(new ItemListModel(_itm.getString("name"), _itm.getString("date")));
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
