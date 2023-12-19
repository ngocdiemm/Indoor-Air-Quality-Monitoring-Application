package com.uit.myairquality.Model;

public class AccessToken {
    private static String access_token="";
    public static String getToken() {
        return access_token;
    }

    public static void SetToken(String Token){
        access_token = Token;
    }
}
