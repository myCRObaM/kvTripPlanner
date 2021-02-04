package com.example.kvtripplanner.Classes;

public class FriendRequestModel {
    public String recEmail;
    public String recUdid;
    public String senderEmail;
    public String senderUdid;


    public FriendRequestModel()
    {}

    public FriendRequestModel(String recEmail,String recUdid , String senderUdid, String senderEmail) {
        this.recEmail = recEmail;
        this.recUdid = recUdid;
        this.senderEmail = senderEmail;
        this.senderUdid = senderUdid;
    }
}
