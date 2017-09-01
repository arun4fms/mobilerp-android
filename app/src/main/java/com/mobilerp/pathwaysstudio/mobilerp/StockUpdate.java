package com.mobilerp.pathwaysstudio.mobilerp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StockUpdate extends Fragment {

    boolean isNewProduct;
    DecoratedBarcodeView barcodeView;
    BeepManager beepManager;
    String lastBarcode;
    CameraSettings settings;
    APIServer apiServer;
    URLs URL = URLs.getInstance();
    TextView tvBarcode, tvBarcodeValue, tvPrice, tvTotal;
    EditText etName, etPrice, etTotal;
    Button btnSave;
    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastBarcode)) {
                return;
            }

            lastBarcode = result.getText();
            barcodeView.setStatusText(lastBarcode);
            beepManager.playBeepSoundAndVibrate();
            findLastScannedProduct(result.getText());

        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };


    public StockUpdate() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stock_update, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstance) {
        // Camera settings
        settings = new CameraSettings();
        settings.setFocusMode(CameraSettings.FocusMode.MACRO);

        //Barcode settings
        barcodeView = (DecoratedBarcodeView) getView().findViewById(R.id.barcodePreview);
        barcodeView.getBarcodeView().setCameraSettings(settings);
        barcodeView.decodeContinuous(callback);

        beepManager = new BeepManager(getActivity());

        //Server init
        apiServer = new APIServer(getContext());

        // Init elements to display items
        tvBarcode = (TextView) getView().findViewById(R.id.tvBarcode);
        tvBarcodeValue = (TextView) getView().findViewById(R.id.tvBarcodeValue);
        tvPrice = (TextView) getView().findViewById(R.id.tvPrice);
        tvTotal = (TextView) getView().findViewById(R.id.tvTotal);

        etName = (EditText) getView().findViewById(R.id.etName);
        etPrice = (EditText) getView().findViewById(R.id.etPrice);
        etTotal = (EditText) getView().findViewById(R.id.etTotal);

        btnSave = (Button) getView().findViewById(R.id.btnSave);

        tvBarcode.setText(R.string.item_barcode);
        tvPrice.setText(R.string.item_price);
        tvTotal.setText(R.string.item_total);

        etName.setEnabled(false);
        etPrice.setEnabled(false);
        etTotal.setEnabled(false);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                if (isNewProduct) {
                    try {
                        jsonObject.put("barcode", lastBarcode);
                        jsonObject.put("name", etName.getText());
                        jsonObject.put("price", etPrice.getText());
                        jsonObject.put("units", etTotal.getText());
                        apiServer.getResponse(Request.Method.POST, URLs.BASE_URL + URLs.NEW_PRODUCT,
                                jsonObject, new VolleyCallback() {
                                    @Override
                                    public void onSuccessResponse(JSONObject result) {
                                        cleanEntries();
                                    }

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getContext(), R.string.fail, Toast.LENGTH_LONG).show();
                                    }
                                });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        jsonObject.put("price", etPrice.getText());
                        jsonObject.put("units", etTotal.getText());
                        apiServer.getResponse(Request.Method.PUT, URLs.BASE_URL + URLs
                                        .UPDATE_PRODUCT + lastBarcode,
                                jsonObject, new VolleyCallback() {
                                    @Override
                                    public void onSuccessResponse(JSONObject result) {
                                        cleanEntries();
                                    }

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(getContext(), R.string.fail, Toast.LENGTH_LONG).show();
                                    }
                                });
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void findLastScannedProduct(String barcode) {
        tvBarcodeValue.setText(barcode);
        apiServer.getResponse(Request.Method.GET, URLs.BASE_URL + URLs.FIND_PRODUCT + barcode,
                null, new VolleyCallback() {
                    @Override
                    public void onSuccessResponse(JSONObject result) {
                        isNewProduct = false;
                        try {
                            JSONArray _itms = result.getJSONArray("mobilerp");
                            JSONObject _itm = _itms.getJSONObject(0);
                            etName.setText(_itm.getString("name"));
                            etPrice.setText(_itm.getString("price"));
                            enableEntries();
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), R.string._404_not_found, Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (response.statusCode == 404) {
                            Toast.makeText(getContext(), R.string._404_not_found, Toast.LENGTH_LONG).show();
                            isNewProduct = true;
                            enableEntries();
                        } else {
                            apiServer.genericErrors(response.statusCode);
                        }
                    }
                });
    }

    private void enableEntries() {
        etPrice.setEnabled(true);
        etTotal.setEnabled(true);
        if (isNewProduct) {
            etName.setText(R.string.new_item);
            etName.setEnabled(true);
        }
    }

    private void cleanEntries() {
        Toast.makeText(getContext(), R.string.success, Toast.LENGTH_LONG).show();
        etName.setText("");
        etPrice.setText("");
        etTotal.setText("");
        tvBarcodeValue.setText("");
        etName.setEnabled(false);
        etPrice.setEnabled(false);
        etTotal.setEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
//    }

}
