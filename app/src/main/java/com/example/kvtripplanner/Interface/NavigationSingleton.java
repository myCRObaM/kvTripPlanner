package com.example.kvtripplanner.Interface;
import com.example.kvtripplanner.Classes.Trips;

public class NavigationSingleton {
    private static NavigationSingleton oInstance;
    private Trips singleTrip;
    public ChangeFragmentInterface changeFragmentInterface;
    public Integer lastId;
    public SingleTripGetData singleTripGetData;

    public static NavigationSingleton getInstance() {
        if (oInstance == null) {
            oInstance = new NavigationSingleton();
        }
        return oInstance;
    }

    private NavigationSingleton() {
    }

    public Trips getSingleTrip() {
        return singleTrip;
    }

    public void setSingleTrip(Trips trip)
    {
        this.singleTrip = trip;
    }
}
