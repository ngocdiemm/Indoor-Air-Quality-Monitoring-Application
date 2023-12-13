package com.uit.myairquality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import com.uit.myairquality.Interfaces.CallMap;
import com.uit.myairquality.Interfaces.CallToken;
import com.uit.myairquality.Model.APIClient;
import com.uit.myairquality.Model.MapRespone;
import com.uit.myairquality.Model.TokenResponse;
import com.uit.myairquality.Model.User;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Map extends AppCompatActivity {


    private MapView mapView;
    MapRespone MapRespone;
    User userCallApi, user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        double[] center=new double[2];
        double[] bounds=new double[4];
        float zoom=0;
        double minZoom=0 ;
        double maxZoom=0;
        boolean boxZoom ;
        // Initialize the osmdroid configuration
        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE));

        // Get a reference to the MapView from the layout
        mapView = findViewById(R.id.MapView);

        fetchDataFromApi();

        mapView.setMinZoomLevel(minZoom); // Giá trị minZoom
        mapView.setMaxZoomLevel(maxZoom);
        GeoPoint defaultLocation = new GeoPoint(center[1], center[0]);
        Log.i("DATA:",center[1]+" "+center[0]);
        mapView.getController().setCenter(defaultLocation);
        mapView.getController().setZoom(16.0);
        // Add a marker to the default location
        mapView.getOverlays().add(new Marker(mapView));

        // Refresh the map view
        mapView.invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    private void setMapValues(MapRespone mapResponse) {
        if (mapResponse != null) {
            MapRespone.Options options = mapResponse.getOptions();
            if (options != null) {
                MapRespone.DefaultOptions defaultOptions = options.getDefaultOptions();
                if (defaultOptions != null) {
                    double[] center = defaultOptions.getCenter();
                    double[] bounds = defaultOptions.getBounds();
                    float zoom = defaultOptions.getZoom();
                    double minZoom = defaultOptions.getMinZoom();
                    double maxZoom = defaultOptions.getMaxZoom();
                    boolean boxZoom = defaultOptions.getBoxZoom();


                }

            }
        }
    }
    private void fetchDataFromApi() {
        CallToken callToken = APIClient.CallToken();
        Call<TokenResponse> usercallToken = callToken.sendRequest(
                "password",
                "openremote",
                userCallApi.getUsername(),
                userCallApi.getPassword()
        );
        usercallToken.enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.body() != null) {
                    String accessToken = response.body().getAccessToken();
                    userCallApi.setToken(accessToken);

                    Log.d("Failure on call map", userCallApi.getToken());

                     CallMap callMap = APIClient.CallMap();
                    Call<MapRespone> userCallMap = callMap.getMapData(
                            "Bearer " + userCallApi.getToken()
                    );

                    userCallMap.enqueue(new Callback<MapRespone>() {

                        @Override
                        public void onResponse(Call<MapRespone> call, Response<MapRespone> response) {
                            if (response.isSuccessful()) {
                                MapRespone mapResponse = response.body();
                                if (mapResponse != null) {
                                    MapRespone.Options options = mapResponse.getOptions();
                                    if (options != null) {
                                        MapRespone.DefaultOptions defaultOptions = options.getDefaultOptions();
                                        if (defaultOptions != null) {
                                            double[] center = defaultOptions.getCenter();
                                            double[] bounds = defaultOptions.getBounds();
                                            float zoom = defaultOptions.getZoom();
                                            double minZoom = defaultOptions.getMinZoom();
                                            double maxZoom = defaultOptions.getMaxZoom();
                                            boolean boxZoom = defaultOptions.getBoxZoom();


                                        }

                                    }
                                }
                            } else {
                                // Xử lý khi có lỗi trong response
                            }
                        }
                        @Override
                        public void onFailure(Call<com.uit.myairquality.Model.MapRespone> call, Throwable t) {
                            Log.d("Failure on call map", "onFailure");
                        }
                    });
                }
                else {
                    Log.d("Failure on call map", "Không có dữ liệu");
                }
            }
            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                Log.d("Failure on call map", "onFailure");
            }
        });

    }
}
