package com.example.kvtripplanner.db;

import com.example.kvtripplanner.Classes.Trips;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DbConnection {
    private static DbConnection oInstance;
    private FirebaseDatabase db;

    public static DbConnection getInstance()
    {
        if (oInstance == null)
        {
            oInstance = new DbConnection();
        }
        return oInstance;
    }

    private DbConnection()
    {
        db = FirebaseDatabase.getInstance();
    }

    public FirebaseDatabase GetDatabase()
    {
        return db;
    }

}
