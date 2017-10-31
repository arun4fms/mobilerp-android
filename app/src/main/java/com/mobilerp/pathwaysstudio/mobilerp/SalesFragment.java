package com.mobilerp.pathwaysstudio.mobilerp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraSettings;
import com.mobilerp.pathwaysstudio.mobilerp.online_mode.APIServer;
import com.mobilerp.pathwaysstudio.mobilerp.online_mode.URLs;
import com.mobilerp.pathwaysstudio.mobilerp.online_mode.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SalesFragment extends Fragment {

    String lastBarcode;
    Double totalSale;
    boolean isNewProduct;

    APIServer apiServer;
    URLs URL = URLs.getInstance();
    ArrayList<SalesItem> items;

    Context context;
    DecoratedBarcodeView barcodeView;
    BeepManager beepManager;
    CameraSettings settings;

    TextView tvName, tvSale, tvPrice;
    EditText etAmount;
    Button btnAddSale, btnEndSale;


    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() == null || result.getText().equals(lastBarcode)) {
                return;
            }

            lastBarcode = result.getText();
            barcodeView.setStatusText(lastBarcode);
            beepManager.playBeepSoundAndVibrate();
            findLastScannedProduct();

        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {

        }
    };

    public SalesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();

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
        tvName = (TextView) getView().findViewById(R.id.tvName);
        tvSale = (TextView) getView().findViewById(R.id.tvTotalSaleValue);

        etAmount = (EditText) getView().findViewById(R.id.tvAmountValue);

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

        items = new ArrayList<>();
        totalSale = 0.0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sales, container, false);
    }

    private void addProduct(){
        int amount = Integer.parseInt(etAmount.getText().toString());
        Double price = Double.parseDouble(tvPrice.getText().toString());
        String name = tvName.getText().toString();
        items.add(new SalesItem(lastBarcode, amount, price, name));
        totalSale += (items.get(items.size() - 1).price * amount);
        tvSale.setText(totalSale.toString());
        Toast.makeText(context, tvName.getText().toString() + " " + getString(R.string.added), Toast.LENGTH_LONG).show();
    }

    private void endSale(){
        FinishSell fragment = FinishSell.newInstance(items);
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.main_content, fragment)
                .addToBackStack("Sales")
                .commit();
    }

    private void findLastScannedProduct() {
        apiServer.getResponse(Request.Method.GET, URLs.BASE_URL + URLs.FIND_PRODUCT + lastBarcode, null, new VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                isNewProduct = false;
                try {
                    JSONArray _itms = result.getJSONArray("mobilerp");
                    JSONObject _itm = _itms.getJSONObject(0);
                    tvName.setText(_itm.getString("name"));
                    tvPrice.setText(_itm.getString("price"));
                    etAmount.setText("1");
                } catch (JSONException e) {
                    Toast.makeText(context, R.string.srv_err_404_not_found, Toast.LENGTH_LONG).show();
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
        Toast.makeText(context, R.string.srv_op_success, Toast.LENGTH_LONG).show();
        tvName.setText("");
        tvPrice.setText("");
        tvSale.setText("");
        etAmount.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
        barcodeView.resume();
    }

//    public void pause(View view) {
//        barcodeView.pause();
//    }
//
//    public void resume(View view) {
//        barcodeView.resume();
//    }
//
//    public void triggerScan(View view) {
//        barcodeView.decodeSingle(callback);
//    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pause();
    }
}
