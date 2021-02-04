package com.example.kvtripplanner.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kvtripplanner.Classes.FriendRequest;
import com.example.kvtripplanner.Classes.FriendsBudget;
import com.example.kvtripplanner.Interface.AcceptedFriendButtonPress;
import com.example.kvtripplanner.Interface.DidPayButtonPressed;
import com.example.kvtripplanner.R;

import java.util.ArrayList;

public class BudgetFriendDidPayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<FriendsBudget> oBudget;
    public DidPayButtonPressed didPayButtonPressed;

    public BudgetFriendDidPayAdapter(ArrayList<FriendsBudget> oBudget)
    {
        this.oBudget = oBudget;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_did_pay_layout, parent, false);
        return new BudgetFriendDidPayAdapter.RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BudgetFriendDidPayAdapter.RequestViewHolder friendsViewHolder = (BudgetFriendDidPayAdapter.RequestViewHolder) holder;
        friendsViewHolder.txtName.setText(oBudget.get(position).username);
        friendsViewHolder.txtAmount.setText(oBudget.get(position).amountToPay);
        if (oBudget.get(position).did_pay)
        {
            friendsViewHolder.btnDidPay.setImageResource(R.drawable.like_icon);
        }
        else
        {
            friendsViewHolder.btnDidPay.setImageResource(R.drawable.dislike_icon);
        }
        friendsViewHolder.btnDidPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                didPayButtonPressed.didPressPay(String.valueOf(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return oBudget.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder{
        TextView txtName;
        TextView txtAmount;
        ImageButton btnDidPay;
        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtFriendName);
            txtAmount = itemView.findViewById(R.id.txtAmountToPay);
            btnDidPay = itemView.findViewById(R.id.btnDidPay);
        }
    }
}
