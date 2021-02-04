package com.example.kvtripplanner.Classes;

public class FriendsBudget {

    public String id;
    public String username;
    public String amountToPay;
    public boolean did_pay;

    public FriendsBudget() {
    }

    public FriendsBudget(String id, String username, String amountToPay, boolean did_pay) {
        this.id = id;
        this.username = username;
        this.did_pay = did_pay;
        this.amountToPay = amountToPay;
    }
}
