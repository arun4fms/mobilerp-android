package com.mobilerp.pathwaysstudio.mobilerp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

//HTTP
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

//JSON
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    //private final String url = "http://192.168.0.106:5000/mobilerp/api/v1.0/findProduct/123";
    private final String url = "http://192.168.1.70:5000/mobilerp/api/v1.0/findProduct/123";
    Context contx;
    private final String TAG = "FETCH";
    ProgressDialog pd;
    InvokeWS ws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ws = new InvokeWS();
        //get context
        contx = this;
    }

    public void checkLogin(View view){
        String user="carlo";
        String pass="123";
        ws.checkLogin(user, pass, new RequestResponse() {
            @Override
            public void onResponseReceived(RequestResult result) {
                if (result.getCodeResult() == 200) {
                    Toast.makeText(contx, "Exito",Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(contx, "Fallo", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void findProduct(View view){
        Intent intent = new Intent(this, adminActivity.class);
        startActivity(intent);
        //final TextView showProduct = (TextView)findViewById(R.id.tvProductName);
        /*ws.findProduct("123", new RequestResponse(){
            @Override
            public void onResponseReceived(RequestResult result){
                showProduct.setText(result.getStringResult());
            }
        });*/

        /*ws.listProducts(new RequestResponse(){
            @Override
            public void onResponseReceived(RequestResult result){
                showProduct.setText(result.getStringResult());
            }
        });*/
    }
}
