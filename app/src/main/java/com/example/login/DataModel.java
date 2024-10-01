package com.example.login;

public class DataModel {

    // string variables for our name and job
    private String email;
    private String password;

    public DataModel(String email,  String pass) {
        this.email = email;
        this.password = pass;

    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public String getpass() {
        return password;
    }

    public void setpass(String pass) {
        this.password = pass;
    }

}
