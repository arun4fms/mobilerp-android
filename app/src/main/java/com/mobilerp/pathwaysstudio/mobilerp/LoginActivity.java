/**
 * Created by Eligio Becerra on 01/03/2017.
 * Copyright (C) 2017 Eligio Becerra
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mobilerp.pathwaysstudio.mobilerp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {

    private final String TAG = "Revisando credenciales";
    Context contx;
    ProgressDialog pd;
    APIServer apiServer;
    EditText user_txt;
    EditText pass_txt;
    User user = User.getInstance();

    public LoginActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //get context
        contx = this;
        pd = new ProgressDialog(contx, ProgressDialog.STYLE_SPINNER);
        //apiInterface = APIClient.getClient().create(APIInterface.class);
        user_txt = (EditText) findViewById(R.id.editText);
        pass_txt = (EditText) findViewById(R.id.editText2);
        user = User.getInstance();
        apiServer = new APIServer(contx);
    }

    public void checkLogin(View view){
        user.setName(user_txt.getText().toString());
        user.setPass(pass_txt.getText().toString());
        String url = APIServer.BASE_URL + APIServer.LOGIN;
        apiServer.getResponse(Request.Method.GET, url, new JSONObject(), new
                VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                try {
                    if (result.getBoolean("logged")) {
                        user.setIsLoginIn(true);
                        Toast.makeText(contx, R.string.success, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(contx, AdminActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    Toast.makeText(contx, R.string.fail, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    if (response.statusCode == 401)
                        Toast.makeText(contx, R.string._401_access_denied, Toast.LENGTH_LONG).show();
                    if (response.statusCode == 500)
                        Toast.makeText(contx, R.string._500_server_error, Toast.LENGTH_LONG).show();
                }
            }
        });
        /*pd.setMessage(TAG);
        pd.show();*/
    }
}
