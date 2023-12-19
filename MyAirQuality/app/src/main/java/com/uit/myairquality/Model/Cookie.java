package com.uit.myairquality.Model;

import java.util.ArrayList;
import java.util.List;

import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class Cookie implements CookieJar {
    private List<okhttp3.Cookie> cookies = new ArrayList<>();

    @Override
    public void saveFromResponse(HttpUrl url, List<okhttp3.Cookie> cookies) {
        if (this.cookies.isEmpty())
            this.cookies =  cookies;
    }
    @Override
    public List<okhttp3.Cookie> loadForRequest(HttpUrl url) {
        return cookies;
    }
}
