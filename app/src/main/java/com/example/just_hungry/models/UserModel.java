package com.example.just_hungry.models;


import android.annotation.SuppressLint;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserModel {
    private String email;
    private String password;
    private String name;
    private String userId;
    private Integer reputation;
    private LocationModel location;
    private String dateCreated;
    private ArrayList<AssetModel> assets;
    private AssetModel profilePictureUrl;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");


    public UserModel() {
        //set to default value
        this.email = "email";
        this.password = "password";
        this.name = "name";
        this.userId = UUID.randomUUID().toString();
        this.reputation = 0;
        this.profilePictureUrl = new AssetModel();
        this.assets = new ArrayList<AssetModel>();
        this.location = new LocationModel();
        this.dateCreated = ISO_8601_FORMAT.format(new Date()).toString();
    }
    public UserModel(String email, String password, String name, String userId) {
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.name = name;
        this.reputation = 0;
        this.profilePictureUrl = new AssetModel();
        this.assets = new ArrayList<AssetModel>();
        this.location = new LocationModel();
        this.dateCreated = ISO_8601_FORMAT.format(new Date()).toString();
    }
    public UserModel(String email,
                     String password,
                     String name,
                     String userId,
                     Integer reputation,
                     LocationModel location,
                     String dateCreated,
                     ArrayList<AssetModel> assets,
                     AssetModel profilePictureUrl) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.userId = userId;
        this.reputation = reputation;
        this.profilePictureUrl = profilePictureUrl;
        this.assets = assets;
        this.location = location;
        this.dateCreated = dateCreated;
    }

    public UserModel(DocumentSnapshot documentSnapshot){
        this.email = documentSnapshot.getString("email");
        this.password = documentSnapshot.getString("password");
        this.name = documentSnapshot.getString("name");
        this.userId = documentSnapshot.getString("userId");
        try {
            this.reputation = documentSnapshot.getLong("reputation").intValue();
        } catch (Exception e) {
            this.reputation = 0;
        }
        // this.reputation = Math.toIntExact((documentSnapshot.getLong("reputation")));
        //this.profilePictureUrl = (AssetModel) documentSnapshot.get("profilePictureUrl");
        //this.assets = (ArrayList<AssetModel>) documentSnapshot.get("assets");
        //this.location = (LocationModel) documentSnapshot.get("location");
        this.dateCreated = documentSnapshot.getString("dateCreated");
    }

    public Map<String, Object> getHashMapForFirestore() {
        Map<String, Object> user = new HashMap<>();
        user.put("email", this.email);
        user.put("password", this.password);
        user.put("name", this.name);
        user.put("userId", this.userId);
        user.put("reputation", this.reputation);
        user.put("profilePictureUrl", this.profilePictureUrl);
        user.put("assets", this.assets);
        user.put("location", this.location);
        user.put("dateCreated", this.dateCreated);
        return user;
    }


    // getters and setters
    public String getName() {
        return name;
    }

    public AssetModel getProfilePictureUrl() {
        return profilePictureUrl;
    }
    //gettersetter
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getUserId() {
        return userId;
    }
    public Integer getReputation() {
        return reputation;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }
    public void setProfilePictureUrl(AssetModel profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
    public ArrayList<AssetModel> getAssets() {
        return assets;
    }
    public void setAssets(ArrayList<AssetModel> assets) {
        this.assets = assets;
    }
    public LocationModel getLocation() {
        return location;
    }
    public void setLocation(LocationModel location) {
        this.location = location;
    }
    public String getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    // gett

}
