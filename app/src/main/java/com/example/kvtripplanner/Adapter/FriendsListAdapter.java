package com.example.kvtripplanner.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kvtripplanner.Classes.Trips;
import com.example.kvtripplanner.Classes.User;
import com.example.kvtripplanner.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FriendsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<User> oUsers;

    public FriendsListAdapter(ArrayList<User> oUsers) {
        this.oUsers = oUsers;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friends_layout, parent, false);
        return new FriendsListAdapter.FriendsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FriendsListAdapter.FriendsViewHolder friendsViewHolder = (FriendsListAdapter.FriendsViewHolder) holder;
        friendsViewHolder.txtUsername.setText(oUsers.get(position).username);
    }

    @Override
    public int getItemCount() {
        return oUsers.size();
    }

    public void setFriends(ArrayList<User> oUsers)
    {
        this.oUsers = oUsers;
        notifyDataSetChanged();
    }

    class FriendsViewHolder extends RecyclerView.ViewHolder{
        TextView txtUsername;
        public FriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.txtFriendsUsername);
        }
    }
}
