package com.mobilerp.pathwaysstudio.mobilerp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
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

public class BarcodeScanner extends AppCompatActivity {

    final Context context = this;
    boolean isNewProduct;
    DecoratedBarcodeView barcodeView;
    BeepManager beepManager;
    String lastBarcode;
    CameraSettings settings;
    APIServer apiServer;
    URLs URL = URLs.getInstance();
    TextView tvBarcode, tvBarcodeValue, tvPrice, tvTotal;
    EditText etName, etPrice, etTotal;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Camera settings
        settings = new CameraSettings();
        settings.setFocusMode(CameraSettings.FocusMode.MACRO);

        //Barcode settings
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcodePreview);
        barcodeView.getBarcodeView().setCameraSettings(settings);
        barcodeView.decodeContinuous(callback);

        beepManager = new BeepManager(this);

        //Server init
        apiServer = new APIServer(context);

        // Init elements to display items
        tvBarcode = (TextView) findViewById(R.id.tvBarcode);
        tvBarcodeValue = (TextView) findViewById(R.id.tvBarcodeValue);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvTotal = (TextView) findViewById(R.id.tvTotal);

        etName = (EditText) findViewById(R.id.etName);
        etPrice = (EditText) findViewById(R.id.etPrice);
        etTotal = (EditText) findViewById(R.id.etTotal);

        tvBarcode.setText(R.string.item_barcode);
        tvPrice.setText(R.string.item_price);
        tvTotal.setText(R.string.item_total);

        etName.setEnabled(false);
        etPrice.setEnabled(false);
        etTotal.setEnabled(false);
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
                    Toast.makeText(context, R.string._404_not_found, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response.statusCode == 404) {
                    Toast.makeText(context, R.string._404_not_found, Toast.LENGTH_LONG).show();
                    isNewProduct = true;
                    enableEntries();
                } else {
                    apiServer.genericErrors(response.statusCode);
                }
            }
        });
    }

    private void enableEntries(){
        etPrice.setEnabled(true);
        etTotal.setEnabled(true);
        if (isNewProduct) {
            etName.setText(R.string.new_item);
            etName.setEnabled(true);
        }
    }

    public void saveProduct(View view) {
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
                                Toast.makeText(context, R.string.fail, Toast.LENGTH_LONG).show();
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
                                Toast.makeText(context, R.string.fail, Toast.LENGTH_LONG).show();
                            }
                        });
            }catch (JSONException ex){
                ex.printStackTrace();
            }
        }

    }

    private void cleanEntries(){
        Toast.makeText(context, R.string.success, Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }


}