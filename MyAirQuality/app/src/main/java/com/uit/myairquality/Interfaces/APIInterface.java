package com.uit.myairquality.Interfaces;
import com.uit.myairquality.Model.Asset;
import com.uit.myairquality.Model.RespondMap;
import android.media.session.MediaSession;
import com.uit.myairquality.Model.Token;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface APIInterface {
    @GET("api/master/asset/{assetID}")
    Call<Asset> getAsset (@Path("assetID") String assetID);

    @POST("/auth/realms/master/protocol/openid-connect/token")
    @FormUrlEncoded
    Call<Token> Login(
            @Field("client_id") String client_id,
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grantType
    );
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
    @GET("api/master/map")
    Call<RespondMap> getMap();


}
