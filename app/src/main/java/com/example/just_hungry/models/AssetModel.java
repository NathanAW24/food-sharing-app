package com.example.just_hungry.models;

import java.util.UUID;

public class AssetModel {
    public String assetUrl;
    public String title;
    public String assetId;

    public AssetModel() {
        //set to default value
        this.assetUrl = "https://www.astronauts.id/blog/wp-content/uploads/2022/08/Makanan-Khas-Daerah-tiap-Provinsi-di-Indonesia-Serta-Daerah-Asalnya.jpg";
        this.title = "assetTitle";
        this.assetId = UUID.randomUUID().toString();
    }
    AssetModel(String title, String assetUrl) {
        //set to default value
        this.assetUrl = assetUrl;
        this.title = title;
        this.assetId = UUID.randomUUID().toString();
    }

    // getters and setters

    public String getTitle() {
        return title;
    }
    public String getAssetUrl() {
        return assetUrl;
    }
    public String setTitle(String title) {
        this.title = title;
        return title;
    }
    public String setAssetUrl(String assetUrl) {
        this.assetUrl = assetUrl;
        return assetUrl;
    }
    public String getAssetId() {
        return assetId;
    }
    public String setAssetId(String assetId) {
        this.assetId = assetId;
        return assetId;
    }
}