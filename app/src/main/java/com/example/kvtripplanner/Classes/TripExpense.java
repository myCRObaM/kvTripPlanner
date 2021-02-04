package com.example.kvtripplanner.Classes;

public class TripExpense {
    public String id;
    public String itemName;
    public String username;
    public String amount;

    public TripExpense() {
    }

    public TripExpense(String id,String itemName, String username, String amount) {
        this.id = id;
        this.itemName = itemName;
        this.username = username;
        this.amount = amount;
    }
}
