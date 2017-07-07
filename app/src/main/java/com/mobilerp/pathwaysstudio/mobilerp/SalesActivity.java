package com.mobilerp.pathwaysstudio.mobilerp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;
import java.util.List;

public class SalesActivity extends AppCompatActivity {

    final Context context = this;
    DecoratedBarcodeView barcodeView;
    BeepManager beepManager;
    String lastBarcode;
    CameraSettings settings;
    APIServer apiServer;
    TextView tvPrice, tvTotalSale;
    EditText etName, etPrice, etAmountItems;
    ArrayList<SaleItem> items;
    Double totalSale = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
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
        items = new ArrayList<>();

        // Init elements to display items
        tvPrice = (TextView) findViewById(R.id.tvPrice);

        etName = (EditText) findViewById(R.id.etName);
        etPrice = (EditText) findViewById(R.id.etPrice);

        etAmountItems = (EditText)findViewById(R.id.etAmountItems);
        tvTotalSale= (TextView)findViewById(R.id.tvTotalSale);

        tvPrice.setText(R.string.item_price);

        etName.setEnabled(false);
        etPrice.setEnabled(false);
    }

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

    private void findLastScannedProduct(final String barcode) {
        apiServer.getResponse(Request.Method.GET, APIServer.BASE_URL + APIServer.FIND_PRODUCT + barcode, null, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    JSONArray _itms = result.getJSONArray("mobilerp");
                    JSONObject _itm = _itms.getJSONObject(0);
                    etName.setText(_itm.getString("name"));
                    etPrice.setText(_itm.getString("price"));
                    etAmountItems.setText("1");
                    etAmountItems.requestFocus();
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
                } else {
                    apiServer.genericErrors(response.statusCode);
                }
            }
        });
    }

    class SaleItem{
        String barcode;
        int amount;

        public SaleItem(String barcode, int amount){
            this.barcode = barcode;
            this.amount = amount;
        }
    }
}
