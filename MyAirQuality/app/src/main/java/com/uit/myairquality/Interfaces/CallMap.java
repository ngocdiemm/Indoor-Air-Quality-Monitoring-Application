package com.uit.myairquality.Interfaces;

import com.uit.myairquality.Model.MapRespone;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface CallMap {
    @GET("api/master/map/js")
    Call<MapRespone> getMapData(
            @Header("Authorization") String authorization
    );
}
