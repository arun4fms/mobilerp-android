package com.mobilerp.pathwaysstudio.mobilerp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
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
public class SalesFragment extends Fragment {

    Context context;
    boolean isNewProduct;
    DecoratedBarcodeView barcodeView;
    BeepManager beepManager;
    String lastBarcode;
    CameraSettings settings;
    APIServer apiServer;
    TextView tvName, tvAmount, tvSale, tvPrice;
    Button btnAddSale, btnEndSale;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(R.string.new_sale);

        // Camera settings
        settings = new CameraSettings();
        settings.setFocusMode(CameraSettings.FocusMode.MACRO);

        //Barcode settings
        barcodeView = (DecoratedBarcodeView) getView().findViewById(R.id.barcodePreview);
        barcodeView.getBarcodeView().setCameraSettings(settings);
        barcodeView.decodeContinuous(callback);

        beepManager = new BeepManager(getActivity());

        //Server init
        apiServer = new APIServer(context);

        // Init elements to display items
        tvPrice = (TextView) getView().findViewById(R.id.tvPriceValue);
        tvAmount = (TextView) getView().findViewById(R.id.tvAmountValue);
        tvName = (TextView) getView().findViewById(R.id.tvName);
        tvSale = (TextView) getView().findViewById(R.id.tvTotalSaleValue);

        btnAddSale = (Button) getView().findViewById(R.id.addProduct);
        btnEndSale= (Button) getView().findViewById(R.id.endSale);

        btnAddSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

        btnEndSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endSale();
            }
        });

        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sales, container, false);
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

    private void addProduct(){
        Toast.makeText(context, "Add Product", Toast.LENGTH_LONG).show();
    }

    private void endSale(){
        Toast.makeText(context, "End Sale", Toast.LENGTH_LONG).show();
    }

    private void findLastScannedProduct(String barcode) {
        apiServer.getResponse(Request.Method.GET, APIServer.BASE_URL + APIServer.FIND_PRODUCT + barcode, null, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                isNewProduct = false;
                try {
                    JSONArray _itms = result.getJSONArray("mobilerp");
                    JSONObject _itm = _itms.getJSONObject(0);
                    tvName.setText(_itm.getString("name"));
                    tvPrice.setText(_itm.getString("price"));
                } catch (JSONException e) {
                    Toast.makeText(context, R.string._404_not_found, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                apiServer.genericErrors(error.networkResponse.statusCode);
            }
        });
    }


    private void cleanEntries(){
        Toast.makeText(context, R.string.success, Toast.LENGTH_LONG).show();
        tvName.setText("");
        tvPrice.setText("");
        tvSale.setText("");
        tvAmount.setText("");
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

    public SalesFragment() {
        // Required empty public constructor
    }
}
