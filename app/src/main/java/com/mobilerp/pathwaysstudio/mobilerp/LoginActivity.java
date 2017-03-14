package com.mobilerp.pathwaysstudio.mobilerp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;


public class LoginActivity extends AppCompatActivity {

    private final String TAG = "FETCH";
    Context contx;
    ProgressDialog pd;
    InvokeWS ws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ws = new InvokeWS();
        //get context
        contx = this;
    }

    public void checkLogin(View view){
        User user = User.getInstance();
        user.setName("carlo");
        user.setPass("123");
        ws.checkLogin(user, new RequestResponse() {
            @Override
            public void onResponseReceived(RequestResult result) {
                if (result.getCodeResult() == 200) {
                    Toast.makeText(contx, "Exito",Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(contx, "Fallo", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
