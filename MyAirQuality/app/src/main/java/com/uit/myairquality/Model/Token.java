package com.uit.myairquality.Model;

import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("access_token")
    public static String access_token;

    @SerializedName("expires_in")
    public String expires_in;

    @SerializedName("scope")
    public String scope;

    // GETTERS AND SETTERS

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
