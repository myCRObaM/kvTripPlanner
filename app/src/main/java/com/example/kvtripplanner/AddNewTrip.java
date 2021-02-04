package com.example.kvtripplanner;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kvtripplanner.Classes.FriendsBudgetModel;
import com.example.kvtripplanner.Classes.Item;
import com.example.kvtripplanner.Classes.TripExpenseModel;
import com.example.kvtripplanner.Classes.TripsModel;
import com.example.kvtripplanner.Classes.User;
import com.example.kvtripplanner.Classes.UserSingleton;
import com.example.kvtripplanner.Interface.NavigationSingleton;
import com.example.kvtripplanner.Interface.OnUrlRedirectComplete;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddNewTrip extends Fragment implements OnUrlRedirectComplete, View.OnClickListener {
    ImageView newTripImg;
    OnUrlRedirectComplete onUrlRedirectComplete;
    Button addNewTripButton;
    EditText inptTitle;
    EditText inptDesc;
    EditText inptBudget;
    String imgUrl;
    ProgressBar progressBar;
    EditText inptLoc;
    EditText inptDate;
    MultiSelectionSpinner inptFriendsSpinner;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_trip, container, false);
        setupView(view);
        setupSpinner(view);
        return view;
    }

    void setupView(View view)
    {
        newTripImg = view.findViewById(R.id.imgNewTrip);
        onUrlRedirectComplete = this;
        addNewTripButton = view.findViewById(R.id.btnAddNewTrip);
        inptTitle = view.findViewById(R.id.inptTitle);
        inptDesc = view.findViewById(R.id.inptDesc);
        inptBudget = view.findViewById(R.id.inptBudget);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.bringToFront();
        addNewTripButton.setOnClickListener(this);
        inptFriendsSpinner = view.findViewById(R.id.inptFriendsForTrip);
        inptLoc = view.findViewById(R.id.inptLokacija);
        inptDate = view.findViewById(R.id.inptDatum);

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

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        getRedirectUrl("https://picsum.photos/" + displayMetrics.widthPixels + "/600");
    }

    private void updateLabel() {
        String myFormat = "dd.MM.yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        inptDate.setText(sdf.format(myCalendar.getTime()));
    }

    void setupSpinner(View view)
    {
        ArrayList<Item> items = new ArrayList<>();
        for (User oFriend : UserSingleton.getInstance().getFriends()) {
            items.add(new Item(oFriend.username, oFriend.id));
        }

        inptFriendsSpinner.setItems(items);
    }

    public void getRedirectUrl(String url) {
        new Thread(new Runnable() {
            public void run()
            {
                URL urlTmp = null;
                String redUrl = null;
                HttpURLConnection connection = null;

                try {
                    urlTmp = new URL(url);
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }

                try {
                    connection = (HttpURLConnection) urlTmp.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    connection.getResponseCode();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                redUrl = connection.getURL().toString();
                connection.disconnect();
                onUrlRedirectComplete.didCompleteRedirect(redUrl);
            }
    }).start();
    }

    private void addNewTrip(View view)
    {
        String sTitle = inptTitle.getText().toString();
        String sDesc = inptDesc.getText().toString();
        String sBudget = inptBudget.getText().toString();
        String sDate = inptDate.getText().toString();
        String sLocation = inptLoc.getText().toString();
        ArrayList<Item> oFriends = inptFriendsSpinner.getSelectedItems();
        ArrayList<FriendsBudgetModel> friendsBudgetModels = new ArrayList<>();
        ArrayList<TripExpenseModel> tripExpenseModels = new ArrayList<>();
        String sHostId = UserSingleton.getInstance().getoCurrentUser().id;

        if (sTitle.isEmpty())
        {
            inptTitle.setError("Title is missing!");
            inptTitle.requestFocus();
            return;
        }

        if (sDesc.isEmpty())
        {
            inptDesc.setError("Description is missing!");
            inptDesc.requestFocus();
            return;
        }

        if (sBudget.isEmpty())
        {
            inptBudget.setError("Budget is missing!");
            inptBudget.requestFocus();
            return;
        }



        if (sDate.isEmpty())
        {
            inptDate.setError("Date is missing!");
            inptDate.requestFocus();
        }

        if (sLocation.isEmpty())
        {
            inptLoc.setError("Location is missing!");
            inptLoc.requestFocus();
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

        TripsModel model = new TripsModel(inptTitle.getText().toString(), inptDesc.getText().toString(), imgUrl, sBudget, friendsBudgetModels, tripExpenseModels, sHostId, sLocation, sDate);
        FirebaseDatabase.getInstance().getReference("Trips")
                .child(NavigationSingleton.getInstance().lastId.toString())
                .setValue(model);

        NavController navController = Navigation.findNavController(view);
        navController.navigate(R.id.action_addNewTrip_to_mainScreenFragment);
    }

    @Override
    public void didCompleteRedirect(String url) {
        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable(){
            @Override
            public void run() {
                imgUrl = url;
                Picasso.get().load(url).into(newTripImg);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case (R.id.btnAddNewTrip):
                addNewTrip(view);

                break;
        }
    }
}