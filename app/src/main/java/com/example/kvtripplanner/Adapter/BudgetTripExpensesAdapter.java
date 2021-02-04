package com.example.kvtripplanner.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kvtripplanner.Classes.TripExpense;
import com.example.kvtripplanner.Interface.AcceptedFriendButtonPress;
import com.example.kvtripplanner.R;

import java.util.ArrayList;

public class BudgetTripExpensesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<TripExpense> oExpense;
    public AcceptedFriendButtonPress acceptedFriendButtonPress;

    public BudgetTripExpensesAdapter(ArrayList<TripExpense> oExpense)
    {
        this.oExpense = oExpense;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_expense_layout, parent, false);
        return new BudgetTripExpensesAdapter.RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BudgetTripExpensesAdapter.RequestViewHolder friendsViewHolder = (BudgetTripExpensesAdapter.RequestViewHolder) holder;
        friendsViewHolder.txtName.setText(oExpense.get(position).username);
        friendsViewHolder.txtAmount.setText(oExpense.get(position).amount);
        friendsViewHolder.txtItemName.setText(oExpense.get(position).itemName);
    }

    @Override
    public int getItemCount() {
        return oExpense.size();
    }

    class RequestViewHolder extends RecyclerView.ViewHolder{
        TextView txtName;
        TextView txtAmount;
        TextView txtItemName;
        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtFriendNameExpense);
            txtAmount = itemView.findViewById(R.id.txtAmountToPayExpense);
            txtItemName = itemView.findViewById(R.id.txtItemName);
        }
    }
}
