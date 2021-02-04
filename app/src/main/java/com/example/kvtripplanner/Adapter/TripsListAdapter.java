package com.example.kvtripplanner.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kvtripplanner.Classes.Trips;
import com.example.kvtripplanner.Interface.TripSelectedListener;
import com.example.kvtripplanner.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TripsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    ArrayList<Trips> oTrips;
    public TripSelectedListener tripSelectedListener;

    public TripsListAdapter(ArrayList<Trips> oTrips)
    {
        this.oTrips = oTrips;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_layout, parent, false);
        return new TripsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TripsViewHolder tripsViewHolder = (TripsViewHolder) holder;
        tripsViewHolder.txtDesc.setText(oTrips.get(position).getDesc());
        tripsViewHolder.txtTitle.setText(oTrips.get(position).getTitle());
        Picasso.get().load(oTrips.get(position).getImgUrl()).into(tripsViewHolder.imgMain);
        tripsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripSelectedListener.didClickItem(oTrips.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return oTrips.size();
    }


    public void setTrips(ArrayList<Trips> trips)
    {
        this.oTrips = trips;
        notifyDataSetChanged();
    }

    class TripsViewHolder extends RecyclerView.ViewHolder{
        TextView txtTitle;
        TextView txtDesc;
        ImageView imgMain;
        public TripsViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTripTitle);
            txtDesc = itemView.findViewById(R.id.txtDesc);
            imgMain = itemView.findViewById(R.id.mainImg);
        }
    }
}
