package com.example.kvtripplanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.*;
import com.example.authmodule.Interface.LoginDoneInterface;
import com.example.authmodule.Interface.LoginModuleNavigator;
import com.example.authmodule.Interface.ReturnToMainScreen;
import com.example.kvtripplanner.Classes.Trips;
import com.example.kvtripplanner.Interface.ChangeFragmentInterface;
import com.example.kvtripplanner.Interface.NavigationSingleton;
import com.example.kvtripplanner.Interface.SingleTripGetData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.core.view.Change;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoginDoneInterface, ChangeFragmentInterface, SingleTripGetData {

    FirebaseAuth mAuth;
    Trips oTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            notLoggedIn();
        }
        else
        {
            setContentView(R.layout.activity_main);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setupView();
            NavigationSingleton.getInstance().changeFragmentInterface = this;
        }
    }


    void setupView()
    {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            notLoggedIn();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_logout:
                mAuth.signOut();
                notLoggedIn();
                break;
            case R.id.menu_friends:
                NavController navController = Navigation.findNavController(findViewById(R.id.main_screen_host_fragment));
                switch (navController.getCurrentDestination().getId())
                {
                    case R.id.mainScreenFragment:
                        navController.navigate(R.id.action_mainScreenFragment_to_friendsFragment);
                        break;
                    case R.id.addNewTrip:
                        navController.navigate(R.id.action_addNewTrip_to_friendsFragment);
                        break;
                    case R.id.tripSingleFragment:
                        navController.navigate(R.id.action_tripSingleFragment_to_friendsFragment);
                        break;
                }

                break;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loginDone() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void notLoggedIn()
    {
        LoginModuleNavigator loginModuleNavigator = LoginModuleNavigator.getInstance();
        loginModuleNavigator.loginDoneInterface = this;
        loginModuleNavigator.navigateToMainTest(this);
    }

    @Override
    public void gotoSingle(Trips trip) {
        this.oTrip = trip;
        NavController navController = Navigation.findNavController(findViewById(R.id.main_screen_host_fragment));
        navController.navigate(R.id.action_mainScreenFragment_to_tripSingleFragment);
        NavigationSingleton.getInstance().singleTripGetData = this;
    }

    @Override
    public Trips getData() {
        return oTrip;
    }
}