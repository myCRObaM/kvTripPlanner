package com.example.kvtripplanner.Classes;

import java.util.ArrayList;

public class Trips {
    private Integer id;
    private String title;
    private String description;
    private String imgUrl;
    private String budget;
    private String perPrice;
    private ArrayList<FriendsBudget> friends;
    private ArrayList<TripExpense> tripExpenses;
    private String location;
    private String date;

    public String getPerPrice() {
        return perPrice;
    }

    public void setPerPrice(String perPrice) {
        this.perPrice = perPrice;
    }

    public ArrayList<TripExpense> getTripExpenses() {
        return tripExpenses;
    }

    public void setTripExpenses(ArrayList<TripExpense> tripExpenses) {
        this.tripExpenses = tripExpenses;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    private String host;



    public Trips(Integer id, String title, String description, String imgUrl, String budget, String perPrice, ArrayList<FriendsBudget> friends, ArrayList<TripExpense> tripExpenses, String location, String date, String host) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imgUrl = imgUrl;
        this.budget = budget;
        this.perPrice = perPrice;
        this.friends = friends;
        this.location = location;
        this.date = date;
        this.host = host;
        this.tripExpenses = tripExpenses;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public ArrayList<FriendsBudget> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<FriendsBudget> friends) {
        this.friends = friends;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return description;
    }

    public void setDesc(String desc) {
        this.description = desc;
    }
}
