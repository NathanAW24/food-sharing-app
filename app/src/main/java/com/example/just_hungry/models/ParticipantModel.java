package com.example.just_hungry.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ParticipantModel {
    private String participantId;
    private String userId;
    private String dateJoined;
    SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'");

    public ParticipantModel() {
        //set to default value
        this.participantId = UUID.randomUUID().toString();
        this.userId = UUID.randomUUID().toString();
        this.dateJoined = ISO_8601_FORMAT.format(new Date()).toString();
    }
    public ParticipantModel(String participantId, String userId, String dateJoined) {
        this.participantId = participantId;
        this.userId = userId;
        this.dateJoined = dateJoined;
    }
}
