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

import org.json.JSONObject;

public class RequestResult {
    private JSONObject jsonResult;
    private String stringResult;
    private int codeResult;


    // Init with actual objects
    public RequestResult(JSONObject jo, String st, int cr) {
        jsonResult = jo;
        stringResult = st;
        codeResult = cr;
    }

    // Init everything as null
    public RequestResult() {
        jsonResult = null;
        stringResult = null;
        codeResult = -1;
    }

    public JSONObject getJsonResult() {
        return jsonResult;
    }

    public void setJsonResult(JSONObject jsonResult) {
        this.jsonResult = jsonResult;
    }

    public String getStringResult() {
        return stringResult;
    }

    public void setStringResult(String stringResult) {
        this.stringResult = stringResult;
    }

    public int getCodeResult() {
        return codeResult;
    }

    public void setCodeResult(int codeResult) {
        this.codeResult = codeResult;
    }

}
