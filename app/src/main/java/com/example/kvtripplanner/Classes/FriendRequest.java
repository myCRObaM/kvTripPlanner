package com.example.kvtripplanner.Classes;

public class FriendRequest {
    public String id;
    public String recId;
    public String recMail;
    public String sendId;
    public String sendMail;

    public FriendRequest(String id, String recId, String recMail, String sendId, String sendMail) {
        this.recId = recId;
        this.recMail = recMail;
        this.sendId = sendId;
        this.sendMail = sendMail;
    }
}
