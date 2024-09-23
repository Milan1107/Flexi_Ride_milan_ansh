package com.example.finalproject_texi_milan;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import android.content.Intent;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class mapActivity extends AppCompatActivity implements OnMapReadyCallback {



    private GoogleMap mymap;
    private SearchView pickupSearch, destinationSearch;
    private ListView pickupSuggestionsListView, destinationSuggestionsListView;
    private PlacesClient placesClient;
    private ArrayAdapter<String> pickupSuggestionsAdapter, destinationSuggestionsAdapter;

    private LatLng currentLocation, pickupLocation, destinationLocation;
    private Button chooseCabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialize the Places API
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.my_map_api_key)); // Replace with your API key
        }

        placesClient = Places.createClient(this);
        pickupSearch = findViewById(R.id.pickupSearch);
        destinationSearch = findViewById(R.id.destinationSearch);
        pickupSuggestionsListView = findViewById(R.id.pickupSuggestionsListView);
        destinationSuggestionsListView = findViewById(R.id.destinationSuggestionsListView);

        pickupSuggestionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        destinationSuggestionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());

        pickupSuggestionsListView.setAdapter(pickupSuggestionsAdapter);
        destinationSuggestionsListView.setAdapter(destinationSuggestionsAdapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Handle pickup search
        pickupSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                performSearch(s, true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (!query.isEmpty()) {
                    showSuggestions(query, true);
                } else {
                    pickupSuggestionsAdapter.clear();
                }
                return false;
            }
        });

        // Handle destination search
        destinationSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                performSearch(s, false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (!query.isEmpty()) {
                    showSuggestions(query, false);
                } else {
                    destinationSuggestionsAdapter.clear();
                }
                return false;
            }
        });

        pickupSuggestionsListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedSuggestion = pickupSuggestionsAdapter.getItem(position);
            if (selectedSuggestion != null) {
                performSearch(selectedSuggestion, true);
            }
        });

        destinationSuggestionsListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedSuggestion = destinationSuggestionsAdapter.getItem(position);
            if (selectedSuggestion != null) {
                performSearch(selectedSuggestion, false);
            }
        });

        chooseCabButton = findViewById(R.id.chooseCabButton);
        chooseCabButton.setOnClickListener(v -> {
            if (destinationLocation != null) {
                Intent intent = new Intent(mapActivity.this, choose_cab.class);
                startActivity(intent);
            } else {
                Toast.makeText(mapActivity.this, "Please select a destination first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mymap = googleMap;
        mymap.getUiSettings().setZoomControlsEnabled(true);
        mymap.getUiSettings().setCompassEnabled(true);

        // Set the current location as the default pickup location
        currentLocation = new LatLng(37.7749, -122.4194); // Replace with actual current location
        pickupLocation = currentLocation;

        // Add the default pickup marker in green
        mymap.addMarker(new MarkerOptions()
                .position(currentLocation)
                .title("Pickup Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mymap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12));
    }

    private void performSearch(String location, boolean isPickup) {
        List<Address> addressList = null;
        if (location != null && !location.isEmpty()) {
            Geocoder geocoder = new Geocoder(mapActivity.this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                // Update the pickup or destination location
                if (isPickup) {
                    pickupLocation = latLng;
                    mymap.addMarker(new MarkerOptions()
                            .position(pickupLocation)
                            .title("Pickup Location")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                } else {
                    destinationLocation = latLng;
                    mymap.addMarker(new MarkerOptions()
                            .position(destinationLocation)
                            .title("Destination Location")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                }

                // Move the camera to the new location
                mymap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

                // Optionally show the route if both locations are set
                if (pickupLocation != null && destinationLocation != null) {
                    showRoute(pickupLocation, destinationLocation);
                }

            } else {
                Toast.makeText(mapActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        }

        if (pickupLocation != null && destinationLocation != null) {
            chooseCabButton.setVisibility(View.VISIBLE); // Show the button
            showRoute(pickupLocation, destinationLocation);
        } else {
            chooseCabButton.setVisibility(View.GONE); // Hide the button if conditions are not met
        }
    }


    private void showSuggestions(String query, boolean isPickup) {
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            List<String> suggestions = new ArrayList<>();
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                suggestions.add(prediction.getFullText(null).toString());
            }

            if (isPickup) {
                pickupSuggestionsAdapter.clear();
                pickupSuggestionsAdapter.addAll(suggestions);
                pickupSuggestionsAdapter.notifyDataSetChanged();
                pickupSuggestionsListView.setVisibility(View.VISIBLE);
            } else {
                destinationSuggestionsAdapter.clear();
                destinationSuggestionsAdapter.addAll(suggestions);
                destinationSuggestionsAdapter.notifyDataSetChanged();
                destinationSuggestionsListView.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener((exception) -> {
            Toast.makeText(mapActivity.this, "Error getting suggestions", Toast.LENGTH_SHORT).show();
        });
    }

    private void showRoute(LatLng pickup, LatLng destination) {
        mymap.animateCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds.Builder()
                .include(pickup)
                .include(destination)
                .build(), 100));
    }
}
