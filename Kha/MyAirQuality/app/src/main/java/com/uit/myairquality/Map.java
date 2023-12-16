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
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
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
import com.uit.myairquality.Model.Asset;
import com.uit.myairquality.Model.MapRespone;
import com.uit.myairquality.Model.RespondMap;
import com.uit.myairquality.Model.RespondWeather;
import com.uit.myairquality.Model.TokenResponse;
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

    MapView mapView;
    Button back;
    APIInterface apiInterface;
    AnnotationConfig annoConfig;
    AnnotationPlugin annoPlugin;
    PointAnnotationManager pointAnnoManager;
    FloatingActionButton floatingActionButton;
    Intent intent = new Intent();
    String access_token;
    private static final String BASE_URL = "https://uiot.ixxc.dev/api/master/";
    private static final String assetId = "4EqQeQ0L4YNWNNTzvTOqjy";
    String defaultWeatherId= "5zI6XqkQVSfdgOrZ1MyWEf";
    String lightId="6iWtSbgqMQsVq8RPkJJ9vo";
    private static String authorization = "";
    TextView txtHumidity,txtManufacturer,txtPlace,txtRainFall,txtTempInfor,txtWindDirection,txtWindSpeed;
    TextView txtEmailInfor,txtBrightness,txtcolourTemperature,txtonOff;
    Point pointUser1;
    Point pointUser2;
    RespondWeather userLocation1;
    RespondWeather userLocation2;

    RespondMap mapData;
    //InterfaceWeather weatherInterFace;
    static MapboxMap mapboxMap;
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
//    private void DrawMap() {
//        mapView.setVisibility(View.INVISIBLE);
//
//        new Thread(() -> {
//            while (!RespondMap.isReady) {
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            requireActivity().runOnUiThread(() -> setMapView());
//        }).start();
//    }
    private void GetUserNearbyLocation(){
        GetUserNearBy(defaultWeatherId, Token.getAccess_token(), new NearbyUsersCallback() {
            @Override
            public void onDataFetchComplete() {
                pointUser1 = Point.fromLngLat(userLocation1.getAttributeWeather().getLocationSuperIdol().getValueSuperIdol().getCoordinates().get(0),userLocation1.getAttributeWeather().getLocationSuperIdol().getValueSuperIdol().getCoordinates().get(1));
            }
        });
        GetUserNearBy(lightId, Token.getAccess_token(), new NearbyUsersCallback() {
            @Override
            public void onDataFetchComplete() {
                pointUser2 = Point.fromLngLat(userLocation2.getAttributeWeather().getLocationSuperIdol().getValueSuperIdol().getCoordinates().get(0),userLocation2.getAttributeWeather().getLocationSuperIdol().getValueSuperIdol().getCoordinates().get(1));
            }
        });
    }

    private void GetUserNearBy(String assetId, String token, NearbyUsersCallback listener){
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<RespondWeather> call = apiInterface.getWeather(assetId, "Bearer "+ token);
        call.enqueue(new Callback<RespondWeather>() {
            @Override
            public void onResponse(Call<RespondWeather> call, Response<RespondWeather> response) {
                if (assetId.equals(defaultWeatherId)) {
                    userLocation1 = response.body();
                } else if (assetId.equals(lightId)) {
                    userLocation2 = response.body();
                }
                listener.onDataFetchComplete();
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
        Log.d("Coordinate", String.valueOf(mapData.getOptionSuperIdol().getDefaultSuperIdol().getBounds()));
        if (mapboxMap != null) {
            //Objects.requireNonNull(new Gson().toJson(mapData))
            mapboxMap.loadStyleUri("mapbox://styles/minhtoan87/clpv8na1301ew01paeo5u0l7c", style -> {
                style.removeStyleLayer("poi-level-1");
                style.removeStyleLayer("highway-name-major");

                annoPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
                annoConfig = new AnnotationConfig("map_annotation");
                pointAnnoManager = (PointAnnotationManager) annoPlugin.createAnnotationManager(AnnotationType.PointAnnotation, annoConfig);
                pointAnnoManager.addClickListener(pointAnnotation -> {
                    String id = Objects.requireNonNull(pointAnnotation.getData()).getAsJsonObject().get("id").getAsString();
                    GetUserNearBy(id, Token.getAccess_token(), new NearbyUsersCallback() {
                        @Override
                        public void onDataFetchComplete() {
                        }
                    });
                    showDialog(id);
                    return true;
                });

                // Create point annotations
                createPointAnnotation(pointUser1, "5zI6XqkQVSfdgOrZ1MyWEf", R.drawable.baseline_location_on_24);
                createPointAnnotation(pointUser2, "6iWtSbgqMQsVq8RPkJJ9vo", R.drawable.baseline_location_on_24);

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

        final Dialog dialog = new Dialog(getBaseContext());
//        Toast.makeText(requireContext(), String.valueOf(idUser), Toast.LENGTH_SHORT).show();
        try {
            if(idUser.equals(lightId)){

                if (userLocation2 != null) {
//                    Toast.makeText(requireContext(),"Color "+ String.valueOf(userLocation2.getAttributeWeather().getColourTemperature().getValueColorSuperIdol()), Toast.LENGTH_SHORT).show();
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
                }

//            Toast.makeText(requireContext(), "Onoff: "+ String.valueOf(userLocation2.getAttributeWeather().getOnOff().getValueOnOffSuperIdol()), Toast.LENGTH_SHORT).show();
            }
            if(idUser.equals(defaultWeatherId)){
                if (userLocation1 != null) {
//                    Toast.makeText(requireContext(),"Temperature "+ String.valueOf(userLocation1.getAttributeWeather().getTemperature().getValueTemperatureSuperIdol()), Toast.LENGTH_SHORT).show();
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

