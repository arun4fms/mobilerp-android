/**
 * Created by mloki on 01/03/2017.
 * Copyright (C) 2017  mloki
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
