package com.example.kvtripplanner;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kvtripplanner.Adapter.TripSingleAdapter;
import com.example.kvtripplanner.Classes.Trips;
import com.example.kvtripplanner.Interface.NavigationSingleton;
import com.example.kvtripplanner.Interface.TripBudgetNeedsData;
import com.example.kvtripplanner.Interface.TripDetailsNeedsData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TripSingleFragment extends Fragment implements View.OnClickListener, TripDetailsNeedsData, TripBudgetNeedsData {

    Trips oTrip;
    ImageView imgTrip;
    ViewPager2 oViewPager;
    Button btnDetails;
    Button btnBudget;
    ConstraintLayout oOverTop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip_single, container, false);
        setupView(view);
        setupViewPager();
        return view;
    }

    private void setupView(View view)
    {
        oTrip = NavigationSingleton.getInstance().singleTripGetData.getData();
        imgTrip = view.findViewById(R.id.imgTrip);
        Picasso.get().load(oTrip.getImgUrl()).into(imgTrip);
        oViewPager = view.findViewById(R.id.viewPagerForDetails);
        btnDetails = view.findViewById(R.id.btnTripDetails);
        btnDetails.setOnClickListener(this);
        btnBudget = view.findViewById(R.id.btnBudgetDetails);
        btnBudget.setOnClickListener(this);

        btnBudget.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
        btnDetails.setBackgroundTintList(ColorStateList.valueOf(Color.DKGRAY));
        btnDetails.setTextColor(Color.WHITE);
        btnBudget.setTextColor(Color.BLACK);
        oOverTop = view.findViewById(R.id.overTopLayout);

        btnBudget.setOnTouchListener(new OnSwipeTouchListener() {
            public boolean onSwipeTop() {
                ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) oOverTop.getLayoutParams();
                newLayoutParams.topMargin = 0;
                oOverTop.setLayoutParams(newLayoutParams);
                return true;
            }
            public boolean onSwipeBottom() {
                ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) oOverTop.getLayoutParams();
                newLayoutParams.topMargin = 500;
                oOverTop.setLayoutParams(newLayoutParams);
                return true;
            }
        });


        btnDetails.setOnTouchListener(new OnSwipeTouchListener() {
            public boolean onSwipeTop() {
                ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) oOverTop.getLayoutParams();
                newLayoutParams.topMargin = 0;
                oOverTop.setLayoutParams(newLayoutParams);
                return true;
            }
            public boolean onSwipeBottom() {
                ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) oOverTop.getLayoutParams();
                newLayoutParams.topMargin = 500;
                oOverTop.setLayoutParams(newLayoutParams);
                return true;
            }
        });


    }



    private void setupViewPager()
    {
        TripSingleAdapter oAdapter = new TripSingleAdapter(getActivity());
        ArrayList<Fragment> fragments = new ArrayList<>();
        TripDetailsFragment tripDetailsFragment = new TripDetailsFragment();
        tripDetailsFragment.tripDetailsNeedsData = this;
        BudgetDetailsFragment tripBudgetFragment = new BudgetDetailsFragment();
        tripBudgetFragment.tripBudgetNeedsData = this;
        fragments.add(tripDetailsFragment);
        fragments.add(tripBudgetFragment);

        oAdapter.addFragments(fragments);

        oViewPager.setAdapter(oAdapter);
        oViewPager.setUserInputEnabled(false);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnTripDetails:
                oViewPager.setCurrentItem(0);
                btnBudget.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                btnDetails.setBackgroundTintList(ColorStateList.valueOf(Color.DKGRAY));
                btnDetails.setTextColor(Color.WHITE);
                btnBudget.setTextColor(Color.BLACK);
                break;
            case R.id.btnBudgetDetails:
                oViewPager.setCurrentItem(1);
                btnDetails.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                btnBudget.setBackgroundTintList(ColorStateList.valueOf(Color.DKGRAY));
                btnBudget.setTextColor(Color.WHITE);
                btnDetails.setTextColor(Color.BLACK);
                break;
        }
    }

    @Override
    public Trips getData() {
        return oTrip;
    }
}