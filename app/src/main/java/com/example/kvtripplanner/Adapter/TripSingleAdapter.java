package com.example.kvtripplanner.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class TripSingleAdapter extends FragmentStateAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    public TripSingleAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    public void addFragments(ArrayList<Fragment> fragments)
    {
        this.fragments = fragments;
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

}