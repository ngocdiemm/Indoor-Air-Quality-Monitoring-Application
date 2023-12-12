package com.uit.myairquality.Model;

import java.io.IOException;
import java.security.cert.CertificateException;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;


import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    public static String token =null;
    private static Retrofit retrofit = null;
    private static String URL_BASE = "https://uiot.ixxc.dev/";


    public static OkHttpClient getUnsafeOkHttpClient() {
        String token="eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJoREkwZ2hyVlJvaE5zVy1wSXpZeDBpT2lHMzNlWjJxV21sRk4wWGE1dWkwIn0.eyJleHAiOjE3MDEzOTQzNTUsImlhdCI6MTcwMTMwNzk1NSwiYXV0aF90aW1lIjoxNzAxMzA3OTU1LCJqdGkiOiJlNTk5MWUzMS1mZGM0LTRiYWMtYmZhMy1hMzJlNDc3YjM4YjkiLCJpc3MiOiJodHRwczovL3Vpb3QuaXh4Yy5kZXYvYXV0aC9yZWFsbXMvbWFzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjRlM2E0NDk2LTJmMTktNDgxMy1iZjAwLTA5NDA3ZDFlZThjYiIsInR5cCI6IkJlYXJlciIsImF6cCI6Im9wZW5yZW1vdGUiLCJzZXNzaW9uX3N0YXRlIjoiNWIwNDNiMGItMmViMy00MjYyLTg4YmEtOTc2NjllYzczZjBhIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwczovL3Vpb3QuaXh4Yy5kZXYiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbImRlZmF1bHQtcm9sZXMtbWFzdGVyIiwib2ZmbGluZV9hY2Nlc3MiLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7Im9wZW5yZW1vdGUiOnsicm9sZXMiOlsicmVhZDptYXAiLCJyZWFkOnJ1bGVzIiwicmVhZDppbnNpZ2h0cyIsInJlYWQ6YXNzZXRzIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJzaWQiOiI1YjA0M2IwYi0yZWIzLTQyNjItODhiYS05NzY2OWVjNzNmMGEiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJGaXJzdCBOYW1lIExhc3QgbmFtZSIsInByZWZlcnJlZF91c2VybmFtZSI6InVzZXIiLCJnaXZlbl9uYW1lIjoiRmlyc3QgTmFtZSIsImZhbWlseV9uYW1lIjoiTGFzdCBuYW1lIiwiZW1haWwiOiJ1c2VyQGl4eGMuZGV2In0.f5Llx_jH5BCa1OLJlTT38WwBHDSFOs2uJeWJJP-QEkLJB1ImhJIM-4HeMkwuPd0-SRRbYTG7VHvuFNzhu8M6IdBqjMUaSKVShRyVmTMbmECYpFv5ffQve0-qcuHmiW6lpSmt8NbKPD0JFOPI9nyHbTH6byFhBlOaYqdg-qAPSQGMeDEouuGC9vYSCd5Li8ucK_awCRv4pi7o0tpYGi8l_J-gVBdzZdO_ge_-lrRdAjYSXLI72OSe9ncZlt2IxnFoNTm3_9K5QVaHpibUk6d2YE_GhXDTHg8uuurOH5loRO1MowjKwfi7zwmoB1UlMvd58aoljaJr8saDK2zLg8OdBA";
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);

            builder.addInterceptor(chain -> {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .addHeader("Content-Type", "application/json");

                if (originalRequest.body() != null) {
                    long contentLength = originalRequest.body().contentLength();
                    requestBuilder.addHeader("Content-Length", String.valueOf(contentLength));
                }

                Request newRequest = requestBuilder.build();
                return chain.proceed(newRequest);
            });

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Retrofit getClient() {
        OkHttpClient client = getUnsafeOkHttpClient();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

