package com.uit.myairquality.Interfaces;

import com.uit.myairquality.Model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface CallWeather {
    @GET("api/master/asset/{assetId}")
    Call<WeatherResponse> callWeather(
            @Path("assetId") String assetId,
            @Header("Authorization") String authorization
    );
}
