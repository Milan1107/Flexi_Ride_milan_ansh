package com.example.finalproject_texi_milan;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.gms.common.api.Status;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class home_f extends Fragment {

    private MapView mapView;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationClient;
    private TextView textViewLocation;
    private AutocompleteSupportFragment autocompleteFragment;

    public home_f() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_f, container, false);

        mapView = view.findViewById(R.id.mapView);
        textViewLocation = view.findViewById(R.id.textView_loc);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        mapView.onCreate(savedInstanceState);

        // Initialize the map
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;

                // Disable zoom controls
                googleMap.getUiSettings().setZoomControlsEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                // Check permissions for location access
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    return;
                }
                googleMap.setMyLocationEnabled(true);  // This will show the default blue circle for the current location

                // Get current location
                fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));

                            Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                if (!addresses.isEmpty()) {
                                    String address = addresses.get(0).getAddressLine(0);

                                    // Limit the address to 40 characters
                                    if (address.length() > 40) {
                                        address = address.substring(0, 40) + "...";
                                    }

                                    // Set the live address in the TextView (textViewLocation)
                                    textViewLocation.setText(address);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                textViewLocation.setText("Unable to fetch location");
                            }
                        } else {
                            textViewLocation.setText("Location not available");
                        }
                    }
                });
            }
        });

        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), getString(R.string.my_map_api_key));
        }



        // Initialize AutocompleteSupportFragment
        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        if (autocompleteFragment != null) {
            autocompleteFragment.getView().setBackgroundColor(Color.WHITE);
        }

        // Specify the types of place data to return
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set listener for place selection
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // Only update the TextView with the selected place, not the map view
                textViewLocation.setText(place.getName());
            }

            @Override
            public void onError(@NonNull Status status) {
                // Handle any error in the place selection process
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
