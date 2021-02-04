package com.example.kvtripplanner;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.kvtripplanner.Classes.FriendsBudget;
import com.example.kvtripplanner.Classes.FriendsBudgetModel;
import com.example.kvtripplanner.Classes.Item;
import com.example.kvtripplanner.Classes.Trips;
import com.example.kvtripplanner.Classes.User;
import com.example.kvtripplanner.Classes.UserSingleton;
import com.example.kvtripplanner.Interface.NavigationSingleton;
import com.example.kvtripplanner.Interface.TripDetailsNeedsData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class TripDetailsFragment extends Fragment implements View.OnClickListener {

    public TripDetailsNeedsData tripDetailsNeedsData;
    private Trips oTrip;
    EditText inptDesc;
    EditText inptLocation;
    EditText inptDate;
    MultiSelectionSpinner inptFriends;
    Button btnDelete;
    Button btnSave;
    final Calendar myCalendar = Calendar.getInstance();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_details, container, false);
        setupView(view);
        return view;
    }

    private void setupView(View view)
    {
        inptDate = view.findViewById(R.id.inptDate);
        inptLocation = view.findViewById(R.id.inptLokacija);
        inptDesc = view.findViewById(R.id.inptDesc);
        inptFriends = view.findViewById(R.id.inptFriendsForTrip);
        btnDelete = view.findViewById(R.id.btnDeleteTripButton);
        btnDelete.setOnClickListener(this);
        btnSave = view.findViewById(R.id.btnSaveButton);
        btnSave.setOnClickListener(this);



        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                myCalendar.set(Calendar.YEAR, i);
                myCalendar.set(Calendar.MONTH, i1);
                myCalendar.set(Calendar.DAY_OF_MONTH, i2);
                updateLabel();
            }
        };

        inptDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        oTrip = tripDetailsNeedsData.getData();
        setupData();
    }

    private void updateLabel() {
        String myFormat = "dd.MM.yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        inptDate.setText(sdf.format(myCalendar.getTime()));
    }

    void setupSpinner()
    {
        ArrayList<Item> items = new ArrayList<>();
        ArrayList<Item> oSelectedItems = new ArrayList<>();
        for (FriendsBudget oFriend : oTrip.getFriends()) {
            oSelectedItems.add(new Item(oFriend.username, oFriend.id));
        }

        for (User oFriend : UserSingleton.getInstance().getUsers()) {
            if (!UserSingleton.getInstance().getoCurrentUser().id.equals(oFriend.id))
            {
                items.add(new Item(oFriend.username, oFriend.id));
            }

        }
        inptFriends.setItems(items);
        inptFriends.setSelection(oSelectedItems);
    }

    private void setupData()
    {
        if (!oTrip.getHost().equals(UserSingleton.getInstance().getoCurrentUser().id))
        {
            inptDate.setOnClickListener(null);
            inptLocation.setFocusable(false);
            inptDesc.setFocusable(false);
            inptFriends.setEnabled(false);
            btnSave.setEnabled(false);
            btnDelete.setEnabled(false);
        }
        inptDesc.setText(oTrip.getDesc());
        inptLocation.setText(oTrip.getLocation());
        inptDate.setText(oTrip.getDate());
        setupSpinner();
    }

    @Override
    public void onResume() {
        super.onResume();
        oTrip = tripDetailsNeedsData.getData();
        setupData();
    }

    public void saveChanges()
    {
        String sDescription = inptDesc.getText().toString();
        String sLocation = inptLocation.getText().toString();
        String sDate = inptDate.getText().toString();
        ArrayList<Item> oFriends = inptFriends.getSelectedItems();
        ArrayList<FriendsBudgetModel> friendsBudgetModels = new ArrayList<>();
        String sIds = "";

        if(sDescription.isEmpty())
        {
            inptDesc.setError("Description is missing!");
            inptDesc.requestFocus();
            return;
        }

        if (sDate.isEmpty())
        {
            inptDate.setError("Date is missing!");
            inptDate.requestFocus();
            return;
        }

        if (sLocation.isEmpty())
        {
            inptLocation.setError("Location is missing!");
            inptLocation.requestFocus();
            return;
        }

        if (oFriends.isEmpty())
        {
            oFriends = new ArrayList<>();
        }
        else
        {
            for (Item oFriend: oFriends)
            {
                for (User oUser: UserSingleton.getInstance().getFriends())
                {
                    if (oFriend.getName().equals(oUser.username))
                    {
                        friendsBudgetModels.add(new FriendsBudgetModel(oUser.id, false));
                    }
                }
            }
        }
        friendsBudgetModels.add(new FriendsBudgetModel(UserSingleton.getInstance().getoCurrentUser().id, false));

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Trips")
                .child(oTrip.getId().toString());
        db.child("description").setValue(sDescription);
        db.child("friends").setValue(friendsBudgetModels);
        db.child("location").setValue(sLocation);
        db.child("date").setValue(sDate);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnDeleteTripButton:
                DatabaseReference deleteRef = FirebaseDatabase.getInstance().getReference("Trips").child(Integer.toString(oTrip.getId()));
                deleteRef.removeValue();
                getActivity().onBackPressed();
                break;
            case R.id.btnSaveButton:
                saveChanges();
                break;
        }
    }
}