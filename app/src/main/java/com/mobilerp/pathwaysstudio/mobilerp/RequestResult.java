package com.mobilerp.pathwaysstudio.mobilerp;

import org.json.JSONObject;

/**
 * Created by mloki on 01/03/2017.
 */

public class RequestResult {
    private JSONObject jsonResult;
    private String stringResult;
    private int codeResult;


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

}
