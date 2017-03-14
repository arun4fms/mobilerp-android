package com.mobilerp.pathwaysstudio.mobilerp;

/**
 * Created by mloki on 13/03/2017.
 */

public class User {

    private static User instance = null;
    private boolean isLoginIn = false;
    private String name = "";
    private String pass = "";

    protected User() {

    }

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }

    public boolean getIsLoginIn() {
        return isLoginIn;
    }

    public void setIsLoginIn(boolean isLoginIn) {
        this.isLoginIn = isLoginIn;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return this.pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
