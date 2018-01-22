package com.mobilerp.pathwaysstudio.mobilerp;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.mobilerp.pathwaysstudio.mobilerp.offline_mode.Insert;
import com.mobilerp.pathwaysstudio.mobilerp.offline_mode.OperationsLog;
import com.mobilerp.pathwaysstudio.mobilerp.offline_mode.SQLHandler;
import com.mobilerp.pathwaysstudio.mobilerp.offline_mode.Select;
import com.mobilerp.pathwaysstudio.mobilerp.offline_mode.Update;
import com.mobilerp.pathwaysstudio.mobilerp.online_mode.APIServer;
import com.mobilerp.pathwaysstudio.mobilerp.online_mode.URLs;
import com.mobilerp.pathwaysstudio.mobilerp.online_mode.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class StockUpdate extends Fragment implements View.OnClickListener {

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
    boolean isOfflineEnabled;
    OperationsLog log;

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
        // Offline Mode
        isOfflineEnabled = SettingsManager.getInstance(getContext()).getBoolean
                (getString(R.string.use_offline_mode));

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

        // -- INIT elements to display items
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

        // ----- END OF INIT -----

        if (isOfflineEnabled) {
            Toast.makeText(getContext(), getString(R
                    .string.offline_mode_enabled), Toast.LENGTH_LONG).show();
            log = OperationsLog.getInstance(getContext());
        } else {
            Toast.makeText(getContext(), getString(R
                    .string.offline_mode_disabled), Toast.LENGTH_LONG).show();
        }

        btnSave.setOnClickListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void findLastScannedProduct(String barcode) {
        tvBarcodeValue.setText(barcode);
        // -- FIND SCANNED PRODUCT ONLINE --
        if (!isOfflineEnabled) {
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
                                Toast.makeText(getContext(), R.string.srv_err_404_not_found, Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            NetworkResponse response = error.networkResponse;
                            if (response.statusCode == 404) {
                                Toast.makeText(getContext(), R.string.srv_err_404_not_found, Toast.LENGTH_LONG).show();
                                isNewProduct = true;
                                enableEntries();
                            } else {
                                apiServer.genericErrors(response.statusCode);
                            }
                        }
                    });
        } else {
            // -- FIND SCANNED PRODUCT OFFLINE --
            isNewProduct = false;
            SQLHandler db = SQLHandler.getInstance(getContext());
            if (db.isDatabaseOpen()) {
                Select select = new Select(getContext());
                select.setQuery("SELECT name, price FROM Product WHERE barcode='" + barcode + "'");
                if (select.execute()) {
                    if (select.results.getCount() > 0) {
                        Toast.makeText(getContext(), getString(R
                                .string.app_op_success), Toast.LENGTH_LONG).show();
                        etName.setText(select.results.getString(0));
                        etPrice.setText(String.valueOf(select.results.getFloat(1)));
                        enableEntries();
                    } else {
                        isNewProduct = true;
                        Toast.makeText(getContext(), getString(R.string.app_err_not_found), Toast
                                .LENGTH_LONG).show();
                        enableEntries();
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        JSONObject jsonObject = new JSONObject();
        jsonObject = prepareJSON();
        // -- FIND SCANNED PRODUCT ONLINE --
        if (!isOfflineEnabled) {
            if (isNewProduct) {
                // -- NEW PRODUCT SAVED TO SERVER --
                apiServer.getResponse(Request.Method.POST, URLs.BASE_URL + URLs.NEW_PRODUCT,
                        jsonObject, new VolleyCallback() {
                            @Override
                            public void onSuccessResponse(JSONObject result) {
                                cleanEntries();
                            }

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), R.string.srv_op_fail, Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                // -- PRODUCT UPDATED TO SERVER --
                apiServer.getResponse(Request.Method.PUT, URLs.BASE_URL + URLs
                                .UPDATE_PRODUCT + lastBarcode,
                        jsonObject, new VolleyCallback() {
                            @Override
                            public void onSuccessResponse(JSONObject result) {
                                cleanEntries();
                            }

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), R.string.srv_op_fail, Toast.LENGTH_LONG).show();
                            }
                        });
            }
        } else {
            // -- FIND PRODUCT OFFLINE --
            if (isNewProduct) {
                // -- NEW PRODUCT SAVED LOCALLY --
                // -- OPERATION ADDED TO LOGFILE --
                log.add(Request.Method.POST, URLs.NEW_PRODUCT, jsonObject);
                cleanEntries();
                Insert insert = new Insert(getContext());
                try {
                    String q = String.format(Locale.getDefault(), "INSERT INTO Product(barcode," +
                                    " name, " +
                                    "price, " +
                                    "units) " +
                                    "values('%s', '%s', %s, %s);", jsonObject.getString("barcode"),
                            jsonObject.getString("name"), jsonObject.getString("price"), jsonObject
                                    .getString("units"));
                    insert.setQuery(q);
                    insert.execute();
                    Log.d("SQL Query :: ", insert.getQuery());
                    Toast.makeText(getContext(), R.string.app_op_success, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Log.d("JSON_EXEC", e.getMessage());
                }
            } else {
                // -- PRODUCT UPDATED LOCALLY --
                // -- OPERATION SAVED TO LOG--
                // TODO: ADD UPDATE TO DB -- DONE??
                log.add(Request.Method.PUT, URLs
                        .UPDATE_PRODUCT + lastBarcode, jsonObject);
                cleanEntries();
                Update update = new Update(getContext());
                try {
                    String q = String.format(Locale.getDefault(), "UPDATE Product " +
                                    "SET name='%s', " +
                                    "price=%s, " +
                                    "units=%s " +
                                    "WHERE barcode='%s';",
                            jsonObject.getString("name"),
                            jsonObject.getString("price"),
                            jsonObject.getString("units"),
                            jsonObject.getString("barcode"));
                    update.setQuery(q);
                    update.execute();
                    Toast.makeText(getContext(), R.string.app_op_success, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    Toast.makeText(getContext(), R.string.app_op_fail, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private JSONObject prepareJSON() {
        JSONObject object = new JSONObject();
        try {
            if (isNewProduct) {
                object.put("barcode", lastBarcode);
                object.put("name", etName.getText());
                object.put("price", etPrice.getText());
                object.put("units", etTotal.getText());
            } else {
                object.put("price", etPrice.getText());
                object.put("units", etTotal.getText());
            }
            object.put("token", Calendar.getInstance().getTime().toString());
            return object;
        } catch (JSONException ex) {
            Log.d("JSON_ERROR", ex.toString());
        }
        return object;
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
        Toast.makeText(getContext(), R.string.srv_op_success, Toast.LENGTH_LONG).show();
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
