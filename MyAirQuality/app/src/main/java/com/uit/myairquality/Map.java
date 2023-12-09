package com.uit.myairquality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class Map extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker markerPerth;
    private Marker markerSydney;
    private Marker markerBrisbane;
    private final LatLng PERTH = new LatLng(-31.952854, 115.857342);
    private final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
    private final LatLng BRISBANE = new LatLng(-27.47093, 153.0235);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        // Add some markers to the map, and add a data object to each marker.
        markerPerth = mMap.addMarker(new MarkerOptions().position(PERTH).title("Perth"));
        markerPerth.setTag(0);

        markerSydney = mMap.addMarker(new MarkerOptions().position(SYDNEY).title("Sydney"));
        markerSydney.setTag(0);

        markerBrisbane = mMap.addMarker(new MarkerOptions().position(BRISBANE).title("Brisbane"));
        markerBrisbane.setTag(0);

        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
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

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Retrieve the data from the marker.
        Integer markerTag = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (markerTag != null) {
            showBottomSheetDialog();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }
}