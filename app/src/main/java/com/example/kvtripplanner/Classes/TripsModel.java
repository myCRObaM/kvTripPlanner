package com.example.kvtripplanner.Classes;

import java.util.ArrayList;

public class TripsModel {
    public String title;
    public String description;
    public String imgUrl;
    public String budget;
    public ArrayList<FriendsBudgetModel> friends;
    public ArrayList<TripExpenseModel> tripExpense;
    public String host;
    public String location;
    public String date;

    public TripsModel()
    {}

    public TripsModel(String title, String description, String imgUrl, String budget, ArrayList<FriendsBudgetModel> friends, ArrayList<TripExpenseModel> tripExpense, String host, String location, String date)
    {
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.budget = budget;
        this.friends = friends;
        this.host = host;
        this.date = date;
        this.location = location;
        this.tripExpense = tripExpense;
    }
}
