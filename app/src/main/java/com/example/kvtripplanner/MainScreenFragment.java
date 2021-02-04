package com.example.kvtripplanner;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.kvtripplanner.Adapter.TripsListAdapter;
import com.example.kvtripplanner.Classes.FriendRequest;
import com.example.kvtripplanner.Classes.FriendRequestModel;
import com.example.kvtripplanner.Classes.FriendsBudget;
import com.example.kvtripplanner.Classes.FriendsBudgetModel;
import com.example.kvtripplanner.Classes.TripExpense;
import com.example.kvtripplanner.Classes.TripExpenseModel;
import com.example.kvtripplanner.Classes.Trips;
import com.example.kvtripplanner.Classes.TripsModel;
import com.example.kvtripplanner.Classes.User;
import com.example.kvtripplanner.Classes.UserModel;
import com.example.kvtripplanner.Classes.UserSingleton;
import com.example.kvtripplanner.Interface.GetUsersInterface;
import com.example.kvtripplanner.Interface.NavigationSingleton;
import com.example.kvtripplanner.Interface.TripSelectedListener;
import com.example.kvtripplanner.db.DbConnection;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainScreenFragment extends Fragment implements TripSelectedListener, View.OnClickListener, GetUsersInterface {

    RecyclerView tripsView;
    TripsListAdapter oAdapter;
    ProgressBar progressBar;
    ImageView plusButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);
        setupView(view);
        setupRecView();
        getUser();
        return view;
    }

    void setupView(View view)
    {
        tripsView = view.findViewById(R.id.tripsRecView);
        progressBar = view.findViewById(R.id.progressBar);
        plusButton = view.findViewById(R.id.btnAddNew);
        plusButton.setOnClickListener(this::onClick);
    }

    void setupRecView()
    {
        ArrayList<Trips> oTrips = new ArrayList<>();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        tripsView.setLayoutManager(mLayoutManager);
        oAdapter = new TripsListAdapter(oTrips);
        oAdapter.tripSelectedListener = this;
        tripsView.setAdapter(oAdapter);
    }


    void getTrips()
    {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference("Trips")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Trips> tripsarray = new ArrayList<>();

                        for (DataSnapshot single: snapshot.getChildren())
                        {
                            Boolean canShowTrip = false;
                            Boolean addHostAsFriend = true;
                            ArrayList<FriendsBudgetModel> friendsBudgetModels = new ArrayList<>();
                            ArrayList<TripExpenseModel> tripExpenseModels = new ArrayList<>();
                            TripsModel model = single.getValue(TripsModel.class);
                            if (model.friends != null)
                            {
                                friendsBudgetModels = model.friends;
                            }
                            if (model.tripExpense != null)
                            {
                                tripExpenseModels = model.tripExpense;
                            }
                            if (model.host.equals(UserSingleton.getInstance().getoCurrentUser().id))
                            {
                                canShowTrip = true;
                            }
                            float dAmountToPay = Float.parseFloat(model.budget);
                            ArrayList<TripExpense> oTripExpense = new ArrayList<>();
                            for (TripExpenseModel expenseModel: tripExpenseModels)
                            {
                                dAmountToPay += Float.parseFloat(expenseModel.amount) / friendsBudgetModels.size();
                                oTripExpense.add(new TripExpense(expenseModel.id, expenseModel.itemName, expenseModel.username, expenseModel.amount));
                            }


                            ArrayList<FriendsBudget> oFriendsForTrip = new ArrayList<>();

                            for (FriendsBudgetModel friendsId: friendsBudgetModels)
                            {
                                if (friendsId.id.equals(UserSingleton.getInstance().getoCurrentUser().id))
                                {
                                    canShowTrip = true;

                                }
                                for (User oFriend: UserSingleton.getInstance().getUsers())
                                {
                                    if (friendsId.id.equals(oFriend.id))
                                    {
                                        oFriendsForTrip.add(new FriendsBudget(friendsId.id, oFriend.username, String.valueOf(dAmountToPay), friendsId.did_pay));
                                    }
                                }
                            }

                            String finalBudget = String.valueOf(dAmountToPay * oFriendsForTrip.size());


                            Trips trips = new Trips(Integer.parseInt(single.getKey()), model.title,
                                    model.description, model.imgUrl, finalBudget, model.budget, oFriendsForTrip, oTripExpense,
                                    model.location, model.date, model.host);
                            if (canShowTrip)
                            {
                                tripsarray.add(trips);
                            }
                        }
                        oAdapter.setTrips(tripsarray);
                    progressBar.setVisibility(View.GONE);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getUsers()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference("Users")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<User> userArray = new ArrayList<>();
                        for (DataSnapshot single: snapshot.getChildren())
                        {
                            UserModel model = single.getValue(UserModel.class);
                            User user = new User(single.getKey(), model.email, model.username);
                            userArray.add(user);
                        }

                        UserSingleton.getInstance().setUsers(userArray);
                        getFriends();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        db.getReference("FriendRequests")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserSingleton.getInstance().nRequestsCount = Math.toIntExact(snapshot.getChildrenCount());
                        ArrayList<FriendRequest> friendReq = new ArrayList<>();
                        for (DataSnapshot single: snapshot.getChildren())
                        {
                            FriendRequestModel model = single.getValue(FriendRequestModel.class);
                            if (model.recEmail.equals(UserSingleton.getInstance().getoCurrentUser().email))
                            {
                                String key = single.getKey();

                                friendReq.add(new FriendRequest(key,model.recUdid, model.recEmail, model.senderUdid, model.senderEmail));
                            }

                        }

                        UserSingleton.getInstance().setFriendRequests(friendReq);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getFriends()
    {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        db.getReference("Friends")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot single: snapshot.getChildren())
                        {
                            if (single.getKey().equals(UserSingleton.getInstance().getoCurrentUser().id))
                            {
                                ArrayList<User> oFriends = new ArrayList<>();
                                Iterable<DataSnapshot> friends = single.getChildren();
                                for (DataSnapshot friend: friends)
                                {
                                    String userId = friend.getValue(String.class);
                                    for (User currentUser: UserSingleton.getInstance().getUsers())
                                    {
                                        if (currentUser.id.equals(userId))
                                        {
                                            oFriends.add(currentUser);
                                        }
                                    }
                                    UserSingleton.getInstance().setFriends(oFriends);
                                }
                            }

                        }
                        if (UserSingleton.getInstance().usersListDone != null)
                        {
                            UserSingleton.getInstance().usersListDone.UsersListDone();
                        }
                        getTrips();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    void getUser()
    {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        FirebaseAuth aDb = FirebaseAuth.getInstance();
        db.getReference("Users").child(aDb.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserSingleton oUser = UserSingleton.getInstance();
                        UserModel oUserModel = snapshot.getValue(UserModel.class);
                        oUser.setoCurrentUser(new User(aDb.getUid(), oUserModel.email, oUserModel.username));

                        getUsers();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public void didClickItem(Trips trip) {
        NavigationSingleton.getInstance().changeFragmentInterface.gotoSingle(trip);
    }

    @Override
    public void onResume() {
        super.onResume();
        UserSingleton.getInstance().getUsersInterface = this;
        getUser();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnAddNew:
                Integer tripCounter = oAdapter.getItemCount();
                NavigationSingleton.getInstance().lastId = tripCounter;
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_mainScreenFragment_to_addNewTrip);
                break;
        }
    }

    @Override
    public void refreshUsersList() {
        getUsers();
    }
}