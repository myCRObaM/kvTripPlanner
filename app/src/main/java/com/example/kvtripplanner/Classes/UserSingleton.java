package com.example.kvtripplanner.Classes;

import android.net.Uri;

import com.example.kvtripplanner.Interface.GetUsersInterface;
import com.example.kvtripplanner.Interface.UsersListDone;
import com.example.kvtripplanner.db.DbConnection;

import java.util.ArrayList;

public class UserSingleton {
    private static UserSingleton oInstance;
    private User oCurrentUser;
    private ArrayList<FriendRequest> oFriendRequests = new ArrayList<>();
    private ArrayList<User> oUsers = new ArrayList<>();
    private ArrayList<User> oFriends = new ArrayList<>();
    public Integer nRequestsCount = 0;
    public GetUsersInterface getUsersInterface;
    public UsersListDone usersListDone;

    public static UserSingleton getInstance()
    {
        if (oInstance == null)
        {
            oInstance = new UserSingleton();
        }
        return oInstance;
    }


    private UserSingleton()
    {

    }

    public User getoCurrentUser() {
        return oCurrentUser;
    }

    public void setoCurrentUser(User oCurrentUser) {
        this.oCurrentUser = oCurrentUser;
    }

    public ArrayList<FriendRequest> getFriendRequests() {
        return oFriendRequests;
    }

    public void setFriendRequests(ArrayList<FriendRequest> oFriendRequests) {
        this.oFriendRequests = oFriendRequests;
    }

    public ArrayList<User> getUsers() {
        return oUsers;
    }

    public void setUsers(ArrayList<User> oUsers) {
        this.oUsers = oUsers;
    }

    public ArrayList<User> getFriends() {
        return oFriends;
    }

    public void setFriends(ArrayList<User> oFriends) {
        this.oFriends = oFriends;
    }

    public void resetFriendRequests()
    {
        oFriendRequests.clear();
    }

}
