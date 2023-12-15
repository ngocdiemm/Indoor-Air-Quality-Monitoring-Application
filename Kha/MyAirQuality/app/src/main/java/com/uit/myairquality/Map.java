package com.uit.myairquality;


import static com.mapbox.maps.plugin.gestures.GesturesUtils.getGestures;
import static com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils.getLocationComponent;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.LocationPuck2D;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener;
import com.uit.myairquality.Interfaces.APIInterface;
import com.uit.myairquality.Interfaces.CallMap;
import com.uit.myairquality.Interfaces.CallToken;
import com.uit.myairquality.Model.APIClient;
import com.uit.myairquality.Model.Asset;
import com.uit.myairquality.Model.MapRespone;
import com.uit.myairquality.Model.RespondMap;
import com.uit.myairquality.Model.TokenResponse;
import com.uit.myairquality.Model.User;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Map extends AppCompatActivity {

    MapView mapView;
    Button back;
    APIInterface apiInterface;
    FloatingActionButton floatingActionButton;
    Intent intent = new Intent();
    String access_token;
    private static final String BASE_URL = "https://uiot.ixxc.dev/api/master/";
    private static final String assetId = "4EqQeQ0L4YNWNNTzvTOqjy";
    private static String authorization = "";
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if(result){
                // Khúc này người truy cập cấp quyền truy cập vị trí
                Toast.makeText(Map.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(Map.this, "Permission not granted", Toast.LENGTH_SHORT).show();
            }
        }
    });
    private final OnIndicatorBearingChangedListener onIndicatorBearingChangedListener = new OnIndicatorBearingChangedListener() {
        @Override
        public void onIndicatorBearingChanged(double v) {
            mapView.getMapboxMap().setCamera(new CameraOptions.Builder().bearing(v).build());
        }
    };
    private final OnIndicatorPositionChangedListener onIndicatorPositionChangedListener = new OnIndicatorPositionChangedListener() {
        @Override
        public void onIndicatorPositionChanged(@NonNull Point point) {
            mapView.getMapboxMap().setCamera(new CameraOptions.Builder().center(point).zoom(15.0).build());
            getGestures(mapView).setFocalPoint(mapView.getMapboxMap().pixelForCoordinate(point));
        }
    };

    private final OnMoveListener onMoveListener = new OnMoveListener() {
        @Override
        public void onMoveBegin(@NonNull MoveGestureDetector moveGestureDetector) {
            getLocationComponent(mapView).removeOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
            getLocationComponent(mapView).removeOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
            getGestures(mapView).removeOnMoveListener(onMoveListener);
            floatingActionButton.show();
        }

        @Override
        public boolean onMove(@NonNull MoveGestureDetector moveGestureDetector) {
            return false;
        }

        @Override
        public void onMoveEnd(@NonNull MoveGestureDetector moveGestureDetector) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Intent intent = getIntent();
        access_token =  intent.getStringExtra("access_token");
        Log.d("calling", "access_token");
        Log.d("access token map", "access_token");
        mapView = findViewById(R.id.mapView);
        floatingActionButton = findViewById(R.id.focusLocation);
        floatingActionButton.hide();
        authorization = "Bearer"+access_token;

        if (ActivityCompat.checkSelfPermission(Map.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        mapView.getMapboxMap().loadStyleUri("mapbox://styles/ngocdiemm/clq3aocdo00eg01qs4gr19yop", new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mapView.getMapboxMap().setCamera(new CameraOptions.Builder().zoom(20.0).build());
                LocationComponentPlugin locationComponentPlugin = getLocationComponent(mapView);
                locationComponentPlugin.setEnabled(true);
                LocationPuck2D locationPuck2D = new LocationPuck2D();
                locationPuck2D.setBearingImage(AppCompatResources.getDrawable(Map.this, R.drawable.baseline_location_on_24));

                locationComponentPlugin.setLocationPuck(locationPuck2D);
                locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
                locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                getGestures(mapView).addOnMoveListener(onMoveListener);

                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // Xử lí lấy tọa độ
                        Retrofit retrofit = APIClient.getClient();
                        apiInterface = retrofit.create(APIInterface.class);
                        Call<RespondMap> call = apiInterface.getMap();
                        Log.d("Call", "calling");
                        call.enqueue(new Callback<RespondMap>() {
                            @Override
                            public void onResponse(Call<RespondMap> call, Response<RespondMap> response) {
                                if (response.isSuccessful()) {
                                    Log.d("Call", "successful");
                                    RespondMap respondMap = response.body();
                                    if(respondMap != null){
                                        List<Double> center = respondMap.getOptionSuperIdol().getDefaultSuperIdol().getCenter();
                                        // Lấy tọa độ từ danh sách center
                                        double latitude = center.get(1);
                                        double longitude = center.get(0);

                                        // Thiết lập CameraOptions để di chuyển đến tọa độ mới
                                        CameraOptions cameraOptions = new CameraOptions.Builder()
                                                .center(Point.fromLngLat(longitude, latitude))
                                                .zoom(15.0)
                                                .build();

                                        Call<Asset> getAsset = apiInterface.getAsset(authorization, assetId);
                                        getAsset.enqueue(new Callback<Asset>() {
                                            @Override
                                            public void onResponse(Call<Asset> call, Response<Asset> response) {


                                            }

                                            @Override
                                            public void onFailure(Call<Asset> call, Throwable t) {

                                            }
                                        });

                                        // Thiết lập camera cho bản đồ
                                        mapView.getMapboxMap().setCamera(cameraOptions);

                                    }
                                }
                                else{
                                    Log.d("Eror Map", "Khong lay duoc");
                                }

                            }

                            @Override
                            public void onFailure(Call<RespondMap> call, Throwable t) {
                                Log.d("suscess", "deny");
                            }
                        });

                        locationComponentPlugin.addOnIndicatorBearingChangedListener(onIndicatorBearingChangedListener);
                        locationComponentPlugin.addOnIndicatorPositionChangedListener(onIndicatorPositionChangedListener);
                        getGestures(mapView).addOnMoveListener(onMoveListener);
                        floatingActionButton.hide();
                    }
                });
            }
        });
    }
    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);

        LinearLayout navigateView = bottomSheetDialog.findViewById(R.id.navigation);
        LinearLayout cancelView = bottomSheetDialog.findViewById(R.id.cancel);
        // Listen events are clicked on Bottom Sheet
        navigateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Map.this, Settings.class);
                startActivity(intent);
            }
        });
        cancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
            }
        });

        bottomSheetDialog.show();
    }
}


/*public class Map extends AppCompatActivity {


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
}*/
