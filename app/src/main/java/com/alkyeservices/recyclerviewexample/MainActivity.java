package com.alkyeservices.recyclerviewexample;

// MainActivity.java


import android.annotation.SuppressLint;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alkyeservices.recyclerviewexample.ViewModel.UserViewModel;
import com.alkyeservices.recyclerviewexample.location.LocationUtil;
import com.alkyeservices.recyclerviewexample.model.Data;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private UserAdapter userAdapter;
    private LocationUtil locationUtil;
    RecyclerView recyclerView;
    private List<Data> data = new ArrayList<>();
    private boolean isLoading = false;
    private int pageNumber = 1;
    private int totalPages = 6; // Assuming you know the total number of pages
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    TextView locationTextView;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationUtil = new LocationUtil(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted
            getCurrentLocation();
        }
        recyclerView = findViewById(R.id.recyclerView);
        locationTextView = findViewById(R.id.tvLocation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!isLoading && !recyclerView.canScrollVertically(1) && pageNumber <= totalPages) {
                    // Reached the bottom of the list
                    pageNumber++;
                    apiCall(pageNumber);
                }
            }
        });
        apiCall(pageNumber);
        userAdapter = new UserAdapter(this, data);
        recyclerView.setAdapter(userAdapter);

    }

    private void apiCall(int page) {
        userViewModel.getUsers(page).observe(this, users -> {
            if (pageNumber == 1) {
                data.clear();
            }
            data.addAll(users.getData());

            userAdapter.notifyDataSetChanged();
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println("requestCode = " + requestCode + ", permissions = " + Arrays.toString(permissions) + ", grantResults = " + Arrays.toString(grantResults));
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                getCurrentLocation();
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied. Unable to access location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location
                        List<android.location.Address> addresses;
                        if (location != null) {
                            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                            try {
                                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            // Display the location in the TextView
                            String locationText = "Lat: " + location.getLatitude() + ", Lng: " + location.getLongitude();

                            if (addresses != null && !addresses.isEmpty()) {
                                android.location.Address address = addresses.get(0);
                                StringBuilder addressString = new StringBuilder();
                                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                                    addressString.append(address.getAddressLine(i)).append("\n");
                                }
                                locationText = locationText + ",\n Address:-" + addressString;
                            }
                            locationTextView.setText(locationText);
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    LOCATION_PERMISSION_REQUEST_CODE);
                            locationTextView.setText("Please allow your location to get your address.");
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the error
                        Toast.makeText(MainActivity.this, "Failed to get location.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

