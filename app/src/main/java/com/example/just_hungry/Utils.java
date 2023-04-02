package com.example.just_hungry;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.just_hungry.models.LocationModel;
import com.example.just_hungry.models.UserModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.C;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Utils {
    static final int COARSE_LOCATION_REQUEST_CODE = 100;
    static final String TAG = "UtilsTag";
    static final String UTILS_TAG = "UtilsTag";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static Utils instance;

    public Utils(){

    }

    public static synchronized Utils getInstance() {
        if (null != instance){
            return instance;
        } else {
            instance = new Utils();
            return instance;
        }
    }
    private interface OnGetDataListener {
        //this is for callbacks
        void onSuccess(QuerySnapshot dataSnapshotValue);
    }
//    private void GetAllPostsFirestore(final PostActivity.OnGetDataListener listener, String userId) {
//        Task<QuerySnapshot> querySnapshotTask = db.collection("users").whereEqualTo("userId", userId).get();
//        querySnapshotTask.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){
//
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                listener.onSuccess(queryDocumentSnapshots);
//            }
//        });
//    }

//    public static UserModel getUserInstanceFromId(String userId){
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        DocumentReference docRef = db.collection("users").document(userId);
//        DocumentSnapshot docSnapshot = docRef.get().getResult();
//
//        try {
//            Query query = db.collection("users").whereEqualTo("userId", userId);
//            Future<QuerySnapshot> querySnapshot = query.get();
//            if (doc.exists()) {
//                UserModel user = doc.toObject(UserModel.class);
//                return user;
//            }
//            else {
//                return null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public static void getUserById(String userId, OnSuccessListener<UserModel> successListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Query the 'users' collection for the user with the specified ID.
        db.collection("users").whereEqualTo("userId", userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.isEmpty()) {
                        // User with the specified ID does not exist.
                        successListener.onSuccess(null);
                        return;
                    }
                    // Convert the Firestore document to a User object.
                    DocumentSnapshot result = documentSnapshot.getDocuments().get(0);
                    UserModel user = new UserModel(result);
                    if (user != null) {
                        // User with the specified ID exists.
                        successListener.onSuccess(user);
                    } else {
                        // User with the specified ID does not exist.
                        successListener.onSuccess(null);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors.
                    System.err.println("Error getting user by ID: " + e.getMessage());
                    successListener.onSuccess(null);
                });
    }
    public interface OnGetPostByUserDataListener {
        //this is for callbacks
        void onSuccess(List<DocumentSnapshot> dataSnapshotValue);
    }
    public static void getAllPostsByUserId(String userId, OnGetPostByUserDataListener successListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Query the 'users' collection for the user with the specified ID.
        db.collection("posts").whereEqualTo("posterId", userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.isEmpty()) {
                        // User with the specified ID does not exist.
                        successListener.onSuccess(null);
                        return;
                    }
                    // Convert the Firestore document to a User object.
                    List<DocumentSnapshot> results = documentSnapshot.getDocuments();

                    if (results.size() > 0) {
                        // User with the specified ID exists.
                        successListener.onSuccess(results);
                    } else {
                        // User with the specified ID does not exist.
                        successListener.onSuccess(null);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors.
                    System.err.println("Error getting posts by user Id " + e.getMessage());
                    successListener.onSuccess(null);
                });
    }

    /**
     * Load the image from a url
     *
     * Code is taken from below
     * https://stackoverflow.com/questions/6407324/how-to-display-image-from-url-on-android
     *
     * @param url
     * @return
     */
    public static void LoadImageFromWebOperations(String url, Container<Drawable> tempDrawableContainer) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                // we are in background thread
                // We want to store the Drawable object into the container so that it can be accessed later

                Log.i(TAG, "LoadImageFromWebOperations: using Utils function load image form web operations");
                try {
                    InputStream is = (InputStream) new URL(url).getContent();
                    Drawable d = Drawable.createFromStream(is, "src name");
                    Log.i(null, "Displaying image");
                    tempDrawableContainer.setT(d);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // back in the main thread
                            tempDrawableContainer.getT();
                        }
                    });
                } catch (Exception e) {
                    Log.i(null, "Couldn't load the image, error: " +e);
                }

            }
        });

    }

    final static class Container<T> {
        private T t;

        public T getT() {
            return t;
        }

        public void setT(T t) {
            this.t = t;
        }
    }



    /**
     * This method checks if an Activity has a network connection
     * @param  context a Context object (Context is the superclass of AppCompatActivity
     * @return a boolean object
     */

    static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean haveNetwork = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        Log.i(UTILS_TAG, "Active Network: " + haveNetwork);
        return haveNetwork;
    }

    // location access
    public static LocationModel getDeviceLocation(Activity activity, FusedLocationProviderClient fusedLocationClient, LocationModel currentLocation){
        System.out.println("FUSED LOCATION CLIENT INSIDE getLastLocation()" + fusedLocationClient);

        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation() // 100 is HIGH_ACCURACY
                    .addOnSuccessListener(new OnSuccessListener<Location>() { // try addOnCompleteListener
                        @Override
                        public void onSuccess(Location location) {
                            System.out.println("location object " + location);
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                System.out.println(String.valueOf(latitude) + " " + String.valueOf(longitude));
                                currentLocation.setLatitude(latitude);
                                currentLocation.setLongitude(longitude);
                            }
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("TAG", "Exception: " + e.getMessage());
                            System.out.println("KEGAGALAN HAKIKI " + e.getMessage());
                        }
                    })
            ;
        } else {
            askPermission(activity, fusedLocationClient);
            return getDeviceLocation(activity, fusedLocationClient, currentLocation);
        }
        return currentLocation;
    }

    public static void askPermission(Activity activity, FusedLocationProviderClient fusedLocationClient) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, COARSE_LOCATION_REQUEST_CODE);
    }

    public static double distFrom(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 3958.75; // miles (or 6371.0 kilometers)
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lon2-lon1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;
    }

    public static int distFrom(LocationModel loc1, LocationModel loc2) {
        double earthRadius = 6371; // kilometers

        double lat1 = loc1.getLatitude();
        double lat2 = loc2.getLatitude();
        double lon1 = loc1.getLongitude();
        double lon2 = loc2.getLongitude();

        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lon2-lon1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return (int) dist;
    }

}
