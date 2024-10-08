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
    private SearchView pickupSearch;
    private ListView pickupSuggestionsListView;
    private PlacesClient placesClient;
    private ArrayAdapter<String> pickupSuggestionsAdapter;

    private LatLng currentLocation, pickupLocation;
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
        pickupSuggestionsListView = findViewById(R.id.pickupSuggestionsListView);

        pickupSuggestionsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        pickupSuggestionsListView.setAdapter(pickupSuggestionsAdapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Handle pickup search
        pickupSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                performSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (!query.isEmpty()) {
                    showSuggestions(query);
                } else {
                    pickupSuggestionsAdapter.clear();
                }
                return false;
            }
        });

        pickupSuggestionsListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedSuggestion = pickupSuggestionsAdapter.getItem(position);
            if (selectedSuggestion != null) {
                pickupSearch.setQuery(selectedSuggestion, false);  // Set the selected suggestion to the SearchView
                performSearch(selectedSuggestion);  // Perform search for the selected suggestion
            }
        });

        chooseCabButton = findViewById(R.id.chooseCabButton);
        chooseCabButton.setOnClickListener(v -> {
            if (pickupLocation != null) {
                Intent intent = new Intent(mapActivity.this, choose_cab.class);
                startActivity(intent);
            } else {
                Toast.makeText(mapActivity.this, "Please select a pickup location first", Toast.LENGTH_SHORT).show();
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

    private void performSearch(String location) {
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

                // Update the pickup location
                pickupLocation = latLng;
                mymap.clear(); // Clear previous markers
                mymap.addMarker(new MarkerOptions()
                        .position(pickupLocation)
                        .title("Pickup Location")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                // Move the camera to the new location
                mymap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

            } else {
                Toast.makeText(mapActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        }

        if (pickupLocation != null) {
            chooseCabButton.setVisibility(View.VISIBLE); // Show the button
        } else {
            chooseCabButton.setVisibility(View.GONE); // Hide the button if conditions are not met
        }
    }

    private void showSuggestions(String query) {
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            List<String> suggestions = new ArrayList<>();
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                suggestions.add(prediction.getFullText(null).toString());
            }

            pickupSuggestionsAdapter.clear();
            pickupSuggestionsAdapter.addAll(suggestions);
            pickupSuggestionsAdapter.notifyDataSetChanged();
            pickupSuggestionsListView.setVisibility(View.VISIBLE);
        }).addOnFailureListener((exception) -> {
            Toast.makeText(mapActivity.this, "Error getting suggestions", Toast.LENGTH_SHORT).show();
        });
    }
}
