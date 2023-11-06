package com.uit.myairquality.Interfaces;
import androidx.browser.trusted.Token;
import com.google.gson.JsonObject;
import com.uit.myairquality.Model.Asset;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIInterface {
    @GET("api/master/asset/{assetID}")
    Call<Asset> getAsset(@Path("assetID") String assetID);
    @POST("auth/reals/master/protocol/openid-connect/token")
    @FormUrlEncoded

    //Đăng nhập

    Call<com.uit.myairquality.Model.Token> Login(

            @Field("client_id") String clientId,
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grantType
    );

    //Đăng kí
    Call<Token> Register(

            @Field("client_id") String clientId,
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grantType
    );

    //Cập nhật mật khẩu

    @Headers("Content-Type: application/json")
    @PUT("api/master/user/master/reset-password/{userId}")
    Call<String> updatePassword (@Path("userId") String userId, @Body JsonObject body);
}

