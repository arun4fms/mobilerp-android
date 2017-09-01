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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListItems.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListItems#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListItems extends Fragment implements Response.Listener<byte[]>, Response.ErrorListener {

    ListView itemList;
    ItemListAdapter itemListAdapter;
    ArrayList<ItemListModel> items;
    APIServer apiServer;
    URLs URL = URLs.getInstance();
    String endpoint;
    String reportURL;
    Button btnDownloadPDF;
    InputStreamVolleyRequest request;
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
            getActivity().setTitle(R.string.drug_list);
        }
        if (endpoint.equals("LISTDEPLETED")) {
            listDepleted();
            reportURL = URLs.BASE_URL + URLs.DEPLETED_REPORT;
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
                request = new InputStreamVolleyRequest(Request.Method.GET, reportURL, ListItems
                        .this, ListItems.this, null);
                RequestQueue rq = Volley.newRequestQueue(getContext(), new HurlStack());
                rq.add(request);
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

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(byte[] response) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            if (response != null) {

                //Read file name from headers
                String content = request.responseHeaders.get("Content-Disposition");
                StringTokenizer st = new StringTokenizer(content, "=");
                String[] arrTag = st.toArray();

                String filename = arrTag[1];
                filename = filename.replace(":", ".");
                Log.d("DEBUG::RESUME FILE NAME", filename);

                try {
                    long lenghtOfFile = response.length;

                    //covert reponse to input stream
                    InputStream input = new ByteArrayInputStream(response);
                    File path = Environment.getExternalStorageDirectory();
                    File file = new File(path, filename);
                    map.put("resume_path", file.toString());
                    BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(file));
                    byte data[] = new byte[1024];

                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        output.write(data, 0, count);
                    }

                    output.flush();

                    output.close();
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
            e.printStackTrace();
        }
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
