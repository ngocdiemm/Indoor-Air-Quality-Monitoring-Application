package com.uit.myairquality.Interfaces;


import com.uit.myairquality.Model.TokenResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CallToken {
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST("auth/realms/master/protocol/openid-connect/token")
    Call<TokenResponse> sendRequest(
            @Field("grant_type") String grantType,
            @Field("client_id") String clientId,
            @Field("username") String username,
            @Field("password") String password
    );
}
