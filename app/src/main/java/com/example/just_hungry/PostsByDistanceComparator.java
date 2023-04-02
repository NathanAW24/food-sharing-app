package com.example.just_hungry;

import com.example.just_hungry.models.LocationModel;
import com.example.just_hungry.models.PostModel;

import java.util.Comparator;

public class PostsByDistanceComparator implements Comparator<PostModel> {

    LocationModel currentDeviceLocation;

    public PostsByDistanceComparator() {
        this.currentDeviceLocation = new LocationModel(0,0);
    }

    public PostsByDistanceComparator(LocationModel currentDeviceLocation) {
        this.currentDeviceLocation = currentDeviceLocation;
    }

    @Override
    public int compare(PostModel post1, PostModel post2) {
        // posts latitude and longitude
        double lat1 = post1.getLocation().getLatitude();
        double lat2 = post2.getLocation().getLatitude();
        double lon1 = post1.getLocation().getLongitude();
        double lon2 = post2.getLocation().getLongitude();

        // device latitude and longitude
        double latDevice = currentDeviceLocation.getLatitude();
        double lonDevice = currentDeviceLocation.getLongitude();

        // device distance to each post
        double distToPost1 = Utils.distFrom(latDevice, lonDevice, lat1, lon1);
        double distToPost2 = Utils.distFrom(latDevice, lonDevice, lat2, lon2);

        if (distToPost1 < distToPost2) {
            return 1;
        }
        else if (distToPost1 == distToPost2) {
            return 0;
        }
        else {
            return -1;
        }

    }


}
