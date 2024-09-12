package com.alkyeservices.recyclerviewexample.location;

// LocationUtil.java

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.core.app.ActivityCompat;

import com.alkyeservices.recyclerviewexample.application.MyApplication;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationUtil {
    private FusedLocationProviderClient fusedLocationClient;

    public LocationUtil(Context context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    /*public void getLastLocation(final LocationCallback callback) {
        if (ActivityCompat.checkSelfPermission(fusedLocationClient.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(fusedLocationClient.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                callback.onLocationResult(LocationResult.create(task.getResult()));
            }
        });
    }*/

    public void requestLocationUpdates(final LocationCallback callback) {
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(10000)
                .setFastestInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        callback.onLocationResult(locationResult);
                    }
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(MyApplication.myApplication, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MyApplication.myApplication, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }
}
