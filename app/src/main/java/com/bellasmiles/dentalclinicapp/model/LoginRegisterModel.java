package com.bellasmiles.dentalclinicapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRegisterModel {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;

    public LoginRegisterModel(String username, String success, String message) {
        this.username = username;
        this.success = success;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
