package com.uit.myairquality.Model;

import com.google.gson.annotations.SerializedName;

public class Token {
    @SerializedName("access_token")
    public static String access_token;
    @SerializedName("expire_in")
    public String expire_in;
    @SerializedName("scope")
    public String scope;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpire_in() {
        return expire_in;
    }

    public void setExpire_in(String expire_in) {
        this.expire_in = expire_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
