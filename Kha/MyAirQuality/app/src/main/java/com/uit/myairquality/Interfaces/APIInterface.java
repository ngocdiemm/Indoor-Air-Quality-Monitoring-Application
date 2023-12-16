package com.uit.myairquality.Interfaces;
import com.uit.myairquality.Model.ChartResponse;
import com.uit.myairquality.Model.MapRespone;
import com.uit.myairquality.Model.Asset;

import com.uit.myairquality.Model.RespondMap;
import com.uit.myairquality.Model.RespondWeather;
import com.uit.myairquality.Model.Token;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface APIInterface {
    @GET("api/master/asset/{assetId}")
    Call<RespondWeather> getWeather(@Header("Authorization") String auth, @Path("assetId") String assetId);



    @POST("/auth/realms/master/protocol/openid-connect/token")
    @FormUrlEncoded
    Call<Token> Login(
            @Field("client_id") String client_id,
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grantType
    );
    @FormUrlEncoded
    @POST("/auth/realms/master/protocol/openid-connect/token")
    Call<ResponseBody> Login_for_chart(@Field("client_id") String client_id,
                              @Field("username") String username,
                              @Field("password") String password,
                              @Field("grant_type") String grant_type);
    @POST("auth/realms/master/login-actions/registration")
    @FormUrlEncoded
    Call<Token> Register (
            @Field("email") String email,
            @Field("username") String username,
            @Field("password") String password,
            @Field("password-confirm") String passwordconfirm
    );

    @POST("/auth/realms/master/login-actions/reset-credentials")
    Call<Token> ResetPassword (
            @Field("email") String email,
            @Field("username") String username
    );

    /*@GET ("api/master/map")
    Call <MapRespone> getMap (@Header("Authorization") String auth);*/

    @GET("api/master/map")
    Call<RespondMap> getMap(/*@Header("Authorization") String auth*/);
    @Headers({"Accept: application/json"})
    @POST("/api/master/asset/datapoint/5zI6XqkQVSfdgOrZ1MyWEf/attribute/{attributeName}")
    Call<List<ChartResponse>> CallChart(@Header("Authorization") String token,
                                       @Path("attributeName") String attributeName,
                                       @Body RequestBody Message);

}
