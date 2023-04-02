package com.example.just_hungry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.just_hungry.models.LocationModel;
import com.example.just_hungry.models.PostModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PostsFragment extends Fragment {

    public ArrayList<PostModel> posts = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    RecyclerView postRecyclerView;
    PostRecyclerAdapter adapter;

    //scrolling stuff
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
    private final static int COARSE_LOCATION_REQUEST_CODE = 100;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationModel deviceLocation = new LocationModel(0, 0); // instantiate on SUTD coordinates



    /** onCreateView mainly handles firestore database post getting
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_main, container, false);


        postRecyclerView = (RecyclerView) rootView.findViewById(R.id.postRecyclerView);

        // firebase has its own threading operations
        Task<QuerySnapshot> postsQuery = db.collection("posts").get();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getActivity());
        deviceLocation = Utils.getDeviceLocation(this.getActivity(), fusedLocationProviderClient, deviceLocation);


        OnGetDataListener listener = new OnGetDataListener() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                System.out.println("QuerySnapshot: " + queryDocumentSnapshots);
                posts.clear();
                // create a new posts ArrayList which stores all the PostModel objects
                for (int i = 0; i < queryDocumentSnapshots.size(); i++) {
                    HashMap<String, Object> post = (HashMap<String, Object>) queryDocumentSnapshots.getDocuments().get(i).getData();
                    posts.add(new PostModel((DocumentSnapshot) queryDocumentSnapshots.getDocuments().get(i)));
                    //posts.add(new PostModel(queryDocumentSnapshots.getDocuments().get(i).getData()));
                    System.out.println(queryDocumentSnapshots.getDocuments().get(i).getData());
                }
                Collections.sort(posts, new PostsByDistanceComparator(deviceLocation));

                adapter = new PostRecyclerAdapter(rootView.getContext(), posts, deviceLocation);
                System.out.println("SETTING UP ADAPTER DONE" + posts);
                postRecyclerView.setLayoutManager(mLayoutManager);
                postRecyclerView.setAdapter(adapter);
            }
        };

        final SwipeRefreshLayout pullToRefresh = rootView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetAllPostsFirestore(listener); // your code
                pullToRefresh.setRefreshing(false);
            }
        });
        GetAllPostsFirestore(listener);
        return rootView;
    }


    // FIREBASE STACK OVER FLOW STUFF
    public interface OnGetDataListener {
        //this is for callbacks
        void onSuccess(QuerySnapshot dataSnapshotValue);
    }
    public void GetAllPostsFirestore(final OnGetDataListener listener) {
        Task<QuerySnapshot> querySnapshotTask = db.collection("posts").get();
        querySnapshotTask.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>(){

            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                listener.onSuccess(queryDocumentSnapshots);
            }
        });
    }
}