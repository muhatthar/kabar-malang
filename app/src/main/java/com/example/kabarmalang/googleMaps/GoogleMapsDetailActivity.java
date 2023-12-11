package com.example.kabarmalang.googleMaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.kabarmalang.R;
import com.example.kabarmalang.databinding.ActivityGoogleMapsDetailBinding;
import com.example.kabarmalang.databinding.ActivityGoogleMapsEditBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class GoogleMapsDetailActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityGoogleMapsDetailBinding binding;
    AppCompatButton btnEditLocation;
    ImageButton btnCurrentLocation;
    private static final String TAG = "info :";
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    PlacesClient placesClient;
    private boolean locationSelectedFromAutocomplete = false;
    private LatLng selectedLatLngFromAutocomplete;
    private LatLng markerPosition;
    private Place selectedPlace;
    private static final int MAP_REQUEST_CODE = 102;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGoogleMapsDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            Intent intent = getIntent();
            if (intent != null) {
                Bundle bundle = intent.getBundleExtra("beritaLatLng");
                if (bundle != null) {
                    String latitude = bundle.getString("lat");
                    String longitude = bundle.getString("lng");

                    if (latitude != null && longitude != null) {
                        LatLng location = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                        mMap.addMarker(new MarkerOptions().position(location).title("Disini"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                    }
                }
            }

            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            btnCurrentLocation = findViewById(R.id.btnCurrentLocation);
            btnCurrentLocation.setOnClickListener(v-> {
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                    mMap.addMarker(new MarkerOptions().position(currentLatLng).title("My Location"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
                                }
                            }
                        });
            });

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                Toast.makeText(this, "Hidupkan Akses Google Maps", Toast.LENGTH_SHORT).show();
            }
        }
    }
}