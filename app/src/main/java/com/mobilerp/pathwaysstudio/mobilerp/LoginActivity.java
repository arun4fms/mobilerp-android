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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private final String TAG = "Revisando credenciales";
    Context contx;
    ProgressDialog pd;
    APIInterface apiInterface;
    EditText user_txt;
    EditText pass_txt;

    public LoginActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //get context
        contx = this;
        pd = new ProgressDialog(contx, ProgressDialog.STYLE_SPINNER);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        user_txt = (EditText) findViewById(R.id.editText);
        pass_txt = (EditText) findViewById(R.id.editText2);
    }

    public void checkLogin(View view){
        final User user = User.getInstance();
        user.setName(user_txt.getText().toString());
        user.setPass(pass_txt.getText().toString());
        Call call = apiInterface.checkUser(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    Toast.makeText(contx, R.string.success, Toast.LENGTH_LONG).show();
                    user.setIsLoginIn(true);
                    Intent intent = new Intent(contx, AdminActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(contx, R.string._401_access_denied, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(contx, R.string._500_server_error, Toast.LENGTH_LONG).show();
            }
        });
        /*pd.setMessage(TAG);
        pd.show();*/
    }
}
