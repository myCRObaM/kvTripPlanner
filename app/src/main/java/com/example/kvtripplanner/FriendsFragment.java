package com.example.kvtripplanner;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kvtripplanner.Adapter.FriendRequestAdapter;
import com.example.kvtripplanner.Adapter.FriendsListAdapter;
import com.example.kvtripplanner.Adapter.TripsListAdapter;
import com.example.kvtripplanner.Classes.FriendRequest;
import com.example.kvtripplanner.Classes.FriendRequestModel;
import com.example.kvtripplanner.Classes.Trips;
import com.example.kvtripplanner.Classes.TripsModel;
import com.example.kvtripplanner.Classes.User;
import com.example.kvtripplanner.Classes.UserModel;
import com.example.kvtripplanner.Classes.UserSingleton;
import com.example.kvtripplanner.Interface.AcceptedFriendButtonPress;
import com.example.kvtripplanner.Interface.UsersListDone;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendsFragment extends Fragment implements View.OnClickListener, AcceptedFriendButtonPress, UsersListDone {
    RecyclerView oFriendsListView;
    ImageButton oAddNewFriend;
    Button btnFriendRequest;
    FriendsListAdapter oAdapter;
    View newFriendRequestView;

    PopupWindow oNewFriendRequest;
    PopupWindow oFriendRequestsWindow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        setupView(view);
        setupRecView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void setupView(View view)
    {
        oFriendsListView = view.findViewById(R.id.recFriendsView);
        oAddNewFriend = view.findViewById(R.id.btnAddNewFriend);
        btnFriendRequest = view.findViewById(R.id.btnFriendRequests);
        btnFriendRequest.setOnClickListener(this);
        oAddNewFriend.setOnClickListener(this);
        UserSingleton.getInstance().usersListDone = this;
    }

    private void setupRecView()
    {
        ArrayList<User> oUsers = new ArrayList<>();
        if (UserSingleton.getInstance().getFriends().size() == 0)
        {
            oUsers.add(new User("12", "mail", "No friends, add some!"));
        }
        else
        {
            oUsers = UserSingleton.getInstance().getFriends();
        }
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        oFriendsListView.setLayoutManager(mLayoutManager);
        oAdapter = new FriendsListAdapter(oUsers);
        oFriendsListView.setAdapter(oAdapter);
    }

    private void sendFriendRequest()
    {
        EditText inptEmail = newFriendRequestView.findViewById(R.id.inptFriendsMail);
        TextView txtError = newFriendRequestView.findViewById(R.id.errorTextView);
        String email = inptEmail.getText().toString();
        String recUdid = "";
        Boolean canSaveMail = false;
        Boolean canSave = false;
        if (email.isEmpty())
        {
            inptEmail.setError("Please write a valid email!");
            inptEmail.requestFocus();
            canSave = false;
        }
        else if (UserSingleton.getInstance().getUsers().size() == 0)
        {
            inptEmail.setError("Connection problem, try again later!");
            canSave = false;
        }
        for (User user : UserSingleton.getInstance().getUsers()) {
            if (user.email.equals(email))
            {
                recUdid = user.id;
                canSave = true;
                canSaveMail = true;
            }
        }
        if (!canSaveMail)
        {
            inptEmail.setError("This email does not exist, try again!");
            inptEmail.requestFocus();
            canSave = false;
        }

        if (canSave)
        {
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("FriendRequests").child(UserSingleton.getInstance().nRequestsCount.toString());
            mRef.child("recEmail").setValue(email);
            mRef.child("recUdid").setValue(recUdid);
            mRef.child("senderUdid").setValue(UserSingleton.getInstance().getoCurrentUser().id);
            mRef.child("senderEmail").setValue(UserSingleton.getInstance().getoCurrentUser().email);
            oNewFriendRequest.dismiss();
            Toast.makeText(getActivity(),"Friend request sent!",Toast.LENGTH_SHORT).show();
        }
    }

    private void setupFriendRequests(View view)
    {
        RecyclerView recView = view.findViewById(R.id.recViewFriendRequests);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recView.setLayoutManager(mLayoutManager);
        FriendRequestAdapter adapter = new FriendRequestAdapter(UserSingleton.getInstance().getFriendRequests());
        adapter.acceptedFriendButtonPress = this;
        recView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnAddNewFriend:
                newFriendRequestView = getActivity().getLayoutInflater().inflate(R.layout.add_new_friend_popup, null);
                oNewFriendRequest = new PopupWindow(newFriendRequestView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                oNewFriendRequest.setFocusable(true);
                oNewFriendRequest.showAtLocation(newFriendRequestView , Gravity.CENTER, 0, 0);
                Button btnSendFriendRequest = newFriendRequestView.findViewById(R.id.btnSendRequest);
                btnSendFriendRequest.setOnClickListener(this);
                Button btnCancelRequest = newFriendRequestView.findViewById(R.id.btnCancelRequest);
                btnCancelRequest.setOnClickListener(this);
                break;

            case R.id.btnFriendRequests:
                View popView = getActivity().getLayoutInflater().inflate(R.layout.friend_request_popup, null);
                oFriendRequestsWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                oFriendRequestsWindow.setFocusable(true);
                oFriendRequestsWindow.showAtLocation(popView , Gravity.CENTER, 0, 0);
                setupFriendRequests(popView);
                break;
            case R.id.btnSendRequest:
                sendFriendRequest();
                break;
            case R.id.btnCancelRequest:
                oNewFriendRequest.dismiss();
        }
    }

    @Override
    public void didPressButton(FriendRequest oRequest) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Friends").child(oRequest.recId);
        mRef.push().setValue(oRequest.sendId);
        DatabaseReference msRef = FirebaseDatabase.getInstance().getReference("Friends").child(oRequest.sendId);
        msRef.push().setValue(oRequest.recId);
        String id = oRequest.id;
        if (oRequest.id == null)
        {
            id = "0";
        }
        DatabaseReference deleteRef = FirebaseDatabase.getInstance().getReference("FriendRequests").child(id);
        deleteRef.removeValue();

        oFriendRequestsWindow.dismiss();
        Toast.makeText(getActivity(),"Friend request accepted!",Toast.LENGTH_SHORT).show();
        UserSingleton.getInstance().getUsers();

    }

    @Override
    public void UsersListDone() {
        oAdapter.setFriends(UserSingleton.getInstance().getFriends());
    }
}