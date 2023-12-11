package com.example.kabarmalang.googleMaps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.kabarmalang.databinding.ActivityGoogleMapsBinding;
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

public class GoogleMapsEditActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityGoogleMapsEditBinding binding;
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

        binding = ActivityGoogleMapsEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String apiKey = getString(R.string.api_key);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        placesClient = Places.createClient(this);
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

                locationSelectedFromAutocomplete = true;
                selectedLatLngFromAutocomplete = place.getLatLng();
                selectedPlace = place;

                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                LatLng selectedLatLng = place.getLatLng();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLatLng, 15));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedLatLng, 15), 1000, new GoogleMap.CancelableCallback() {
                    @Override
                    public void onCancel() {
                        handleButtonClick(selectedLatLngFromAutocomplete);
                    }

                    @Override
                    public void onFinish() {
                        handleButtonClick(selectedLatLngFromAutocomplete);
                    }
                });
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(selectedLatLng).title(place.getName()));

            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

    }

    private void handleButtonClick(LatLng location) {
        if (location != null) {
            String getLatitude = String.valueOf(location.latitude);
            String getLongitude = String.valueOf(location.longitude);

            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(location).title(getLatitude + ", " + getLongitude));

            btnEditLocation = findViewById(R.id.btnEditLokasi);
            btnEditLocation.setOnClickListener(v -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("editSelectedLatLng", location);
                resultIntent.putExtra("editPlaceName", "Nama Spesifik Daerah Tidak diketahui");
                setResult(RESULT_OK, resultIntent);
                finish();
            });

            if (selectedPlace != null) {
                double placeLatitude = selectedPlace.getLatLng().latitude;
                double placeLongitude = selectedPlace.getLatLng().longitude;

                if (placeLatitude == location.latitude && placeLongitude == location.longitude) {
                    btnEditLocation.setOnClickListener(v -> {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("editSelectedLatLng", location);
                        resultIntent.putExtra("editPlaceName", selectedPlace.getName());
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    });
                } else {
                    Toast.makeText(this, "Lokasi tidak sesuai dengan tempat terpilih", Toast.LENGTH_SHORT).show();
                    btnEditLocation.setOnClickListener(v -> {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("editSelectedLatLng", location);
                        resultIntent.putExtra("editPlaceName", "Nama Spesifik Daerah Tidak diketahui");
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    });
                }
            }
        }
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
                                    markerPosition = currentLatLng;
                                    handleButtonClick(markerPosition);
                                }
                            }
                        });
            });

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                markerPosition = latLng;
                locationSelectedFromAutocomplete = false;
                handleButtonClick(markerPosition);
            }
        });
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