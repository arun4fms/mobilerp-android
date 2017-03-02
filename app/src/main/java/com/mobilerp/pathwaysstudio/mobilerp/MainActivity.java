package com.mobilerp.pathwaysstudio.mobilerp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

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

    public void fetchProduct(View view){
        final TextView showProduct = (TextView)findViewById(R.id.tvProductName);
        //invokeWS();
        ws.findProduct("123", new RequestResponse(){
            @Override
            public void onResponseReceived(RequestResult result){
                showProduct.setText(result.getStringResult());
            }
        });
    }

    public void invokeWS(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.setBasicAuth("carlo","123");
        client.get(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        TextView tvProductName = (TextView)findViewById(R.id.tvProductName);
                        String str = new String(responseBody);
                        JSONObject res = null;
                        String result;
                        try{
                            res =new JSONObject(str);
                            result=res.toString();
                        }catch (JSONException e){
                            result=e.toString();
                        }
                        tvProductName.append(result+"\n"+str);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody,
                                          Throwable
                            throwable) {
                        TextView tvProductName = (TextView)findViewById(R.id.tvProductName);
                        if (statusCode == 404){
                            tvProductName.setText("Producto no encontrado");
                        }
                        if (statusCode == 401){
                            tvProductName.setText("Acceso no autorizado");
                        }
                        if (statusCode == 500){
                            tvProductName.setText("Error del servidor");
                        }
                    }
                }
        );
    }
}
