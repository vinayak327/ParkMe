package com.example.parkme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.parkme.BookingSaveActivities.ParkingAvailabilityActivity;
import com.example.parkme.LoginRegisterActivities.LoginActivity;
import com.example.parkme.OtherActivities.AboutActivity;
import com.example.parkme.OtherActivities.RateActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

//hiiii
public class MainActivity2 extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_rating) {
                    startActivity(new Intent(MainActivity2.this, RateActivity.class));
                    return true;

                } else if (id == R.id.nav_about) {
                    startActivity(new Intent(MainActivity2.this, AboutActivity.class));
                    return true;

                } else if (id == R.id.nav_logout) {
                    logoutUser();
                    return true;
                } else {
                    return false;
                }
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        Places.initialize(getApplicationContext(), getString(R.string.my_map_api_key));

        setUpPlaceAutocomplete();



    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(MainActivity2.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity2.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Close the current activity
    }





    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        myMap.getUiSettings().setZoomGesturesEnabled(true);

        // Enable default Google Maps toolbar
        myMap.getUiSettings().setMapToolbarEnabled(true);

        // Check location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // If permission granted, enable My Location layer and zoom to current location
            myMap.setMyLocationEnabled(true);
            getLastKnownLocation();
            showParkingLocations();
        } else {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }

        // Set up marker click listener
        myMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Show custom info window with "Check Parking Availability" message
                marker.setTitle("Check Parking Availability");
                marker.showInfoWindow();
                return true;
            }
        });
    }

    private void getLastKnownLocation() {
        // Check location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                            }
                        }
                    });
        } else {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void showParkingLocations() {
        addParkingLocation(19.1348871, 77.3047966, "Yashosai Hospital");
        addParkingLocation(19.1291753, 77.3086308, "PVR & D mart");
        addParkingLocation(19.1596432, 77.3096987, "Railway Station");
        addParkingLocation(19.1800814, 77.3267823, "MGM college");
        addParkingLocation(19.1517754, 77.3181155, "Sachkhand Gurudwara");
        addParkingLocation(19.1668493, 77.3090335, "Visawa Garden");
        addParkingLocation(19.1509516, 77.3131011, "Hanumanpeth");
        addParkingLocation(19.1524541, 77.3094211, "Zudio");
    }

    private void addParkingLocation(double latitude, double longitude, String title) {
        LatLng location = new LatLng(latitude, longitude);
        MarkerOptions markerOptions = new MarkerOptions().position(location).title(title);
        myMap.addMarker(markerOptions);

        // Set on info window click listener to navigate to ParkingAvailabilityActivity
        myMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MainActivity2.this, ParkingAvailabilityActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpPlaceAutocomplete() {
        // Initialize AutocompleteSupportFragment
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autoCompleteFragment);

        // Specify place data to return
        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up PlaceSelectionListener
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                LatLng latLng = place.getLatLng();
                String placeName = place.getName();
                if (latLng != null) {
                    myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    myMap.addMarker(new MarkerOptions().position(latLng).title(placeName));
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(MainActivity2.this, "Error: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    myMap.setMyLocationEnabled(true);
                    getLastKnownLocation();
                }
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void geoLocate(String query) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocationName(query, 1);

            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                double latitude = address.getLatitude();
                double longitude = address.getLongitude();

                LatLng searchLocation = new LatLng(latitude, longitude);
                myMap.addMarker(new MarkerOptions().position(searchLocation).title(query));
                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(searchLocation, 15));
                Toast.makeText(this, "Moved to location: " + query, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No results found for: " + query, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Geocoding failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void checkNearbyParking(View view) {
        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Get the last known location of the user
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                // User's current location
                                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                                // Find the nearest parking location based on the current location
                                LatLng nearestParkingLocation = findNearestParkingLocation(currentLocation);

                                // Move the map camera to the nearest parking location
                                if (nearestParkingLocation != null) {
                                    myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nearestParkingLocation, 15));
                                    // Show a message indicating that the user is navigating to the nearest parking location
                                    Toast.makeText(MainActivity2.this, "Navigating to the nearest parking location", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity2.this, "No parking locations found nearby", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        } else {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private LatLng findNearestParkingLocation(LatLng currentLocation) {
        List<LatLng> parkingLocations = getParkingLocations();
        LatLng nearestParkingLocation = null;
        double minDistance = Double.MAX_VALUE;

        for (LatLng parkingLocation : parkingLocations) {
            double distance = calculateDistance(currentLocation, parkingLocation);
            if (distance < minDistance) {
                minDistance = distance;
                nearestParkingLocation = parkingLocation;
            }
        }

        return nearestParkingLocation;
    }

    private List<LatLng> getParkingLocations() {
        // This method should fetch the list of parking locations from your data source
        // For simplicity, let's assume there's a hardcoded list of parking locations
        List<LatLng> parkingLocations = new ArrayList<>();
        parkingLocations.add(new LatLng(19.1348871, 77.3047966));
        parkingLocations.add(new LatLng(19.1291753, 77.3086308));
        parkingLocations.add(new LatLng(19.1596432, 77.3096987));
        parkingLocations.add(new LatLng(19.1800814, 77.3267823));
        parkingLocations.add(new LatLng(19.1517754, 77.3181155));
        parkingLocations.add(new LatLng(19.1668493, 77.3090335));
        parkingLocations.add(new LatLng(19.1509516, 77.3131011));
        parkingLocations.add(new LatLng(19.1524541, 77.3094211));

        return parkingLocations;
    }

    private double calculateDistance(LatLng location1, LatLng location2) {

        double lat1 = location1.latitude;
        double lon1 = location1.longitude;
        double lat2 = location2.latitude;
        double lon2 = location2.longitude;

        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lon2 - lon1, 2));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
