package com.example.kvtripplanner.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kvtripplanner.Classes.FriendRequest;
import com.example.kvtripplanner.Interface.AcceptedFriendButtonPress;
import com.example.kvtripplanner.R;

import java.util.ArrayList;

public class FriendRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<FriendRequest> oUsers;
    public AcceptedFriendButtonPress acceptedFriendButtonPress;

    public FriendRequestAdapter(ArrayList<FriendRequest> oUsers)
    {
        this.oUsers = oUsers;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_request_layout, parent, false);
        return new FriendRequestAdapter.RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FriendRequestAdapter.RequestViewHolder friendsViewHolder = (FriendRequestAdapter.RequestViewHolder) holder;
        friendsViewHolder.txtEmail.setText(oUsers.get(position).sendMail);
        friendsViewHolder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptedFriendButtonPress.didPressButton(oUsers.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return oUsers.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder{
        TextView txtEmail;
        Button btnAccept;
        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            txtEmail = itemView.findViewById(R.id.txtRequestEmail);
            btnAccept = itemView.findViewById(R.id.btnAcceptFriend);
        }
    }
}
