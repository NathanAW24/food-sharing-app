package com.example.just_hungry;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.just_hungry.models.LocationModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;

public class DeviceLocation {
    private final static int COARSE_LOCATION_REQUEST_CODE = 100;
    FusedLocationProviderClient fusedLocationClient;

    private LocationModel getLastLocation(AppCompatActivity activity) {

        Context activityContext = activity.getApplicationContext();

        LocationModel locationModel = new LocationModel();

        if (ActivityCompat.checkSelfPermission(activityContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            System.out.println("location object " + location);
                            if (location != null) {
                                System.out.println(String.valueOf(location.getLatitude()) + " " + String.valueOf(location.getLongitude()));
                                locationModel.setLatitude(location.getLatitude());
                                locationModel.setLongitude(location.getLongitude());
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TAG", "Exception: " + e.getMessage());
                        }
                    });
        } else {
            askPermission(activity);
        }
        return locationModel;
    }

    public void askPermission(AppCompatActivity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, COARSE_LOCATION_REQUEST_CODE);
    }


}
