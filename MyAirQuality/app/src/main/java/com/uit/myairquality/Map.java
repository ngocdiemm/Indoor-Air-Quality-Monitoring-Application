package com.uit.myairquality;


import static androidx.core.content.ContentProviderCompat.requireContext;
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
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraBoundsOptions;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.LocationPuck2D;
import com.mapbox.maps.plugin.Plugin;
import com.mapbox.maps.plugin.animation.CameraAnimationsPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.AnnotationType;
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.CircleAnnotationOptions;
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener;
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener;
import com.uit.myairquality.Interfaces.APIInterface;
import com.uit.myairquality.Interfaces.CallMap;
import com.uit.myairquality.Interfaces.CallToken;
import com.uit.myairquality.Interfaces.NearbyUsersCallback;
import com.uit.myairquality.Model.APIClient;
import com.uit.myairquality.Model.AccessToken;
import com.uit.myairquality.Model.Asset;
import com.uit.myairquality.Model.MapRespone;
import com.uit.myairquality.Model.RespondMap;
import com.uit.myairquality.Model.RespondWeather;
import com.uit.myairquality.Model.RetrofitClient;
import com.uit.myairquality.Model.TokenResponse;
import com.uit.myairquality.Model.URL;
import com.uit.myairquality.Model.User;
import com.uit.myairquality.Model.Token;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Map extends AppCompatActivity {

    static MapboxMap mapboxMap;
    MapView mapView;
    APIInterface apiInterface;
    AnnotationConfig annoConfig;
    AnnotationPlugin annoPlugin;

    String authorization = "";
    PointAnnotationManager pointAnnoManager;
    FloatingActionButton floatingActionButton;
    String access_token;
    String defaultWeatherId= "5zI6XqkQVSfdgOrZ1MyWEf";
    String lightId="6iWtSbgqMQsVq8RPkJJ9vo";
    TextView txtHumidity,txtManufacturer,txtPlace,txtRainFall,txtTempInfor,txtWindDirection,txtWindSpeed;
    TextView txtEmailInfor,txtBrightness,txtcolourTemperature,txtonOff;
    Point pointUser1;
    Point pointUser2;
    RespondWeather userLocation1;
    RespondWeather userLocation2;
    RespondMap mapData;

    TextView txtLight,txtWeather;

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

    //Cũ
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

    private void DrawMap() {
        mapView.setVisibility(View.INVISIBLE);

        new Thread(() -> {
            while (!RespondMap.isReady) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                   e.printStackTrace();
                }
           }
            runOnUiThread(() -> setMapView());

        }).start();
    }
    private void GetUserNearbyLocation(){
        GetUserNearBy(defaultWeatherId, access_token, new NearbyUsersCallback() {
            @Override
            public void onDataFetchComplete() {
                pointUser1 = Point.fromLngLat(userLocation1.getAttributeWeather().getLocationSuperIdol().getValueSuperIdol().getCoordinates().get(0),userLocation1.getAttributeWeather().getLocationSuperIdol().getValueSuperIdol().getCoordinates().get(1));
            }
        });
        GetUserNearBy(lightId, access_token, new NearbyUsersCallback() {
            @Override
            public void onDataFetchComplete() {
                pointUser2 = Point.fromLngLat(userLocation2.getAttributeWeather().getLocationSuperIdol().getValueSuperIdol().getCoordinates().get(0),userLocation2.getAttributeWeather().getLocationSuperIdol().getValueSuperIdol().getCoordinates().get(1));
            }
        });
    }


    private void GetUserNearBy(String assetId, String token, NearbyUsersCallback listener){
        apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        Call<RespondWeather> call = apiInterface.getWeather(assetId, "Bearer "+ token);
        call.enqueue(new Callback<RespondWeather>() {
            @Override
            public void onResponse(Call<RespondWeather> call, Response<RespondWeather> response) {
                if(response.body() != null) {
                    if (assetId.equals(defaultWeatherId)) {
                        userLocation1 = response.body();
                    } else if (assetId.equals(lightId)) {
                        userLocation2 = response.body();
                    }
                    listener.onDataFetchComplete();
                }
            }

            @Override
            public void onFailure(Call<RespondWeather> call, Throwable t) {
                Log.d("API CALL", t.getMessage().toString());
            }
        });
    }



    private void setMapView() {
        mapData = RespondMap.respondMap.getRespondMapData();
        mapView.setVisibility(View.VISIBLE);
        mapboxMap = mapView.getMapboxMap();
        if (mapboxMap != null) {
            //Objects.requireNonNull(new Gson().toJson(mapData));
            mapboxMap.loadStyleUri("mapbox://styles/ngocdiemm/clq3aocdo00eg01qs4gr19yop", style -> {
                style.removeStyleLayer("poi-level-1");
                style.removeStyleLayer("highway-name-major");

                annoPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
                annoConfig = new AnnotationConfig("map_annotation");
                pointAnnoManager = (PointAnnotationManager) annoPlugin.createAnnotationManager(AnnotationType.PointAnnotation, annoConfig);
                pointAnnoManager.addClickListener(pointAnnotation -> {
                    String id = Objects.requireNonNull(pointAnnotation.getData()).getAsJsonObject().get("id").getAsString();
                    GetUserNearBy(id, AccessToken.getToken(), new NearbyUsersCallback() {
                        @Override
                        public void onDataFetchComplete() {
                        }
                    });
                    showDialog(id);
                    return true;
                });

                if(pointUser1 != null && pointUser2 != null) {
                    createPointAnnotation(pointUser1, "5zI6XqkQVSfdgOrZ1MyWEf", R.drawable.baseline_location_on_24);
                    createPointAnnotation(pointUser2, "6iWtSbgqMQsVq8RPkJJ9vo", R.drawable.baseline_location_on_24);
                }

                // Set camera values
                setCameraValues();

                CameraAnimationsPlugin cameraAnimationsPlugin = mapView.getPlugin(Plugin.MAPBOX_CAMERA_PLUGIN_ID);
                if (cameraAnimationsPlugin != null) {
                    mapView.setVisibility(View.VISIBLE);
                }
                mapView.setVisibility(View.VISIBLE);
            });
        }
    }
    private void showDialog(String idUser) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        try {
            if(idUser.equals(lightId)){

                if (userLocation2 != null) {
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.activity_bottom_sheet_light);
                    txtEmailInfor = dialog.findViewById(R.id.txtInforEmail);
                    txtBrightness = dialog.findViewById(R.id.txtInforBrightness);
                    txtcolourTemperature = dialog.findViewById(R.id.txtCoulourTemp);
                    txtonOff = dialog.findViewById(R.id.txtOnOff);

                    txtEmailInfor.setText(String.valueOf(userLocation2.getAttributeWeather().getEmailSuperIdol().getValueSuperIdolemail()));
                    txtBrightness.setText(String.valueOf(userLocation2.getAttributeWeather().getBrightness().getValueBrightnessSuperIdol())+" %");
                    txtcolourTemperature.setText(String.valueOf(userLocation2.getAttributeWeather().getColourTemperature().getValueColorSuperIdol()));
                    txtonOff.setText(String.valueOf(userLocation2.getAttributeWeather().getOnOff().getValueOnOffSuperIdol()));

                    // Set click listener for txtLight
                    txtLight = dialog.findViewById(R.id.txtLight);
                    txtLight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent_light = new Intent(Map.this, Settings.class);
                            startActivity(intent_light);
                            dialog.dismiss();
                        }
                    });
                }

            }
            if(idUser.equals(defaultWeatherId)){
                if (userLocation1 != null) {
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.activity_bottom_sheet_default_weather);

                    txtHumidity = dialog.findViewById(R.id.txtHumidity);
                    txtManufacturer = dialog.findViewById(R.id.txtManufacturer);
                    txtPlace = dialog.findViewById(R.id.txtPlace);
                    txtRainFall = dialog.findViewById(R.id.txtRainFall);
                    txtTempInfor = dialog.findViewById(R.id.txtTempInfor);
                    txtWindDirection = dialog.findViewById(R.id.txtWindDirection);
                    txtWindSpeed = dialog.findViewById(R.id.txtWindSpeed);

                    txtHumidity.setText(String.valueOf(userLocation1.getAttributeWeather().getHumidity().getValueHuminitySuperIdol())+" %");
                    txtManufacturer.setText(String.valueOf(userLocation1.getAttributeWeather().getManufacturer().getValueManufacture()));
                    txtPlace.setText(String.valueOf(userLocation1.getAttributeWeather().getPlace().getValuePlace()));
                    txtRainFall.setText(String.valueOf(userLocation1.getAttributeWeather().getRainfall().getValueRainfall()));
                    txtTempInfor.setText(String.valueOf(userLocation1.getAttributeWeather().getTemperature().getValueTemperatureSuperIdol())+" °C");
                    txtWindDirection.setText(String.valueOf(userLocation1.getAttributeWeather().getWindDirection().getValueWinDirection()));
                    txtWindSpeed.setText(String.valueOf(userLocation1.getAttributeWeather().getWindSpeed().getValueWinSpeed()));

                    // Set click listener for txtWeather
                    txtWeather = dialog.findViewById(R.id.txtDefaultWeather);
                    txtWeather.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent_weather = new Intent(Map.this, Settings.class);
                            startActivity(intent_weather);
                            dialog.dismiss();
                        }
                    });
                }


            }
            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DialogError", "Error in showDialog: " + e.getMessage());
        }

    }

    private void createPointAnnotation(Point point, String id, int iconResource) {
        ArrayList<PointAnnotationOptions> markerList = new ArrayList<>();
        Drawable iconDrawable = getResources().getDrawable(iconResource);
        Bitmap iconBitmap = drawableToBitmap(iconDrawable);
        JsonObject idDeviceTemperature = new JsonObject();
        idDeviceTemperature.addProperty("id", id);
        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(iconBitmap)
                .withData(idDeviceTemperature);
        markerList.add(pointAnnotationOptions);
        pointAnnoManager.create(markerList);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private void setCameraValues() {
        if (mapboxMap != null) {
            mapboxMap.setCamera(
                    new CameraOptions.Builder()
                            .center(Point.fromLngLat(mapData.getOptionSuperIdol().getDefaultSuperIdol().getCenter().get(0),mapData.getOptionSuperIdol().getDefaultSuperIdol().getCenter().get(1) ))
                            .zoom(mapData.getOptionSuperIdol().getDefaultSuperIdol().getZoom())
                            .build()
            );

            mapboxMap.setBounds(
                    new CameraBoundsOptions.Builder()
                            .minZoom(0.0)
                            .maxZoom(19.0)
                            .bounds(mapData.getOptionSuperIdol().getDefaultSuperIdol().getBounds())
                            .build()
            );
        }
    }
    private void GetDataMap(){
        Retrofit retrofit = APIClient.getClient(URL.mainURL);
        apiInterface = retrofit.create(APIInterface.class);
        Call<RespondMap> call = apiInterface.getMap();
        call.enqueue(new Callback<RespondMap>() {
            @Override
            public void onResponse(Call<RespondMap> call, Response<RespondMap> response) {
                if (response.isSuccessful()) {
                    RespondMap.setRespondMapData(response.body());
                }
                else{
                    Log.d("Error Map", "Khong lay duoc");
                }

            }

            @Override
            public void onFailure(Call<RespondMap> call, Throwable t) {
                Log.d("success", "deny");
            }
    });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        access_token =  getIntent().getStringExtra("access_token");
        mapView = findViewById(R.id.mapView);
        floatingActionButton = findViewById(R.id.focusLocation);
        authorization = "Bearer"+ access_token;

        if (ActivityCompat.checkSelfPermission(Map.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        apiInterface = RetrofitClient.getClient().create(APIInterface.class);
        GetUserNearbyLocation();
        GetDataMap();
        DrawMap();
    };
}
