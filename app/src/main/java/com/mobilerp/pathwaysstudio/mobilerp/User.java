/**
 * Created by Eligio Becerra on 01/03/2017.
 * Copyright (C) 2017 Eligio Becerra
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.mobilerp.pathwaysstudio.mobilerp;

import android.util.Base64;

public class User {

    private static User instance = null;
    private boolean isLoggedIn = false;
    private String name = "";
    private String pass = "";

    private String authString = "";

    protected User() {

    }

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public boolean getIsLoginIn() {
        return isLoggedIn;
    }

    public void setIsLoginIn(boolean isLoginIn) {
        this.isLoggedIn = isLoginIn;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
        this.setAuthString();
    }

    public String getPass() {
        return this.pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
        this.setAuthString();
    }

    public void setAuthString() {
        String loginEncoded = new String(Base64.encode((name + ":" + pass).getBytes(),
                Base64.NO_WRAP));
        authString = "Basic " + loginEncoded;
    }

    public String getAuthString() {
        if (authString.isEmpty())
            this.setAuthString();
        return authString;
    }
}
