package com.example.kvtripplanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kvtripplanner.Adapter.BudgetFriendDidPayAdapter;
import com.example.kvtripplanner.Adapter.BudgetTripExpensesAdapter;
import com.example.kvtripplanner.Adapter.FriendsListAdapter;
import com.example.kvtripplanner.Classes.FriendsBudget;
import com.example.kvtripplanner.Classes.FriendsBudgetModel;
import com.example.kvtripplanner.Classes.TripExpense;
import com.example.kvtripplanner.Classes.TripExpenseModel;
import com.example.kvtripplanner.Classes.Trips;
import com.example.kvtripplanner.Classes.User;
import com.example.kvtripplanner.Classes.UserSingleton;
import com.example.kvtripplanner.Interface.DidPayButtonPressed;
import com.example.kvtripplanner.Interface.NavigationSingleton;
import com.example.kvtripplanner.Interface.TripBudgetNeedsData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class BudgetDetailsFragment extends Fragment implements View.OnClickListener, DidPayButtonPressed {

    public TripBudgetNeedsData tripBudgetNeedsData;
    private Trips oTrip;
    private TextView txtAmount;
    private RecyclerView rvDidPay;
    private RecyclerView rvExpenses;
    private BudgetFriendDidPayAdapter oAdapterBudget;
    private BudgetTripExpensesAdapter oAdapterExpenses;
    private Button btnNewTripExpense;
    private View newExpensePopupView;
    private PopupWindow newExpensePopup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_budget_details, container, false);
        oTrip = tripBudgetNeedsData.getData();
        setupView(view);
        return view;
    }

    private void setupView(View view)
    {
        txtAmount = view.findViewById(R.id.txtWholeCostOfTrip);
        rvDidPay = view.findViewById(R.id.rvFriendDidPay);
        rvExpenses = view.findViewById(R.id.rvExpensesList);
        btnNewTripExpense = view.findViewById(R.id.btnAddNewTripExpense);
        btnNewTripExpense.setOnClickListener(this);
    }

    private void setupData()
    {
        txtAmount.setText("Total budget amount: " + oTrip.getBudget());

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvDidPay.setLayoutManager(mLayoutManager);
        oAdapterBudget = new BudgetFriendDidPayAdapter(oTrip.getFriends());
        oAdapterBudget.didPayButtonPressed = this;
        rvDidPay.setAdapter(oAdapterBudget);

        LinearLayoutManager mManager = new LinearLayoutManager(getContext());
        rvExpenses.setLayoutManager(mManager);
        oAdapterExpenses = new BudgetTripExpensesAdapter(oTrip.getTripExpenses());
        rvExpenses.setAdapter(oAdapterExpenses);
    }

    private void addNewExpense() {
        EditText inptExpenseName = newExpensePopupView.findViewById(R.id.inptExpenseName);
        EditText inptExpenseValue = newExpensePopupView.findViewById(R.id.inptExpenseAmount);
        String sExpenseName = inptExpenseName.getText().toString();
        String sExpenseValue = inptExpenseValue.getText().toString();
        Boolean canSave = true;
        if (sExpenseName.isEmpty())
        {
            inptExpenseName.setError("Please write an expense name!");
            inptExpenseName.requestFocus();
            canSave = false;
        }

        if (sExpenseValue.isEmpty())
        {
            inptExpenseValue.setError("Please write an expense amount!");
            inptExpenseValue.requestFocus();
            canSave = false;
        }

        if (canSave)
        {
            ArrayList<TripExpense> expenses = oTrip.getTripExpenses();
            ArrayList<TripExpense> newExpenses = new ArrayList<>();
            ArrayList<TripExpenseModel> expensesModel = new ArrayList<>();

            if (expenses.isEmpty())
            {
                expenses = new ArrayList<>();
            }
            Integer counter = 0;
            float dAmountToPay = Float.parseFloat(oTrip.getPerPrice());

            for (TripExpense trip: expenses)
            {
                dAmountToPay += Float.parseFloat(trip.amount) / oTrip.getFriends().size();
                expensesModel.add(new TripExpenseModel(counter.toString(), trip.itemName, trip.username, trip.amount));
                newExpenses.add(new TripExpense(trip.id, trip.itemName, trip.username, trip.amount));
                counter += 1;
            }
            ArrayList<FriendsBudgetModel> friendsModel = new ArrayList<>();
            ArrayList<FriendsBudget> newFriendsBudget = new ArrayList<>();

            Boolean addOwner = true;
            for (FriendsBudget oFriend: oTrip.getFriends())
            {
                if (oFriend.id.equals(UserSingleton.getInstance().getoCurrentUser().id))
                {
                    addOwner = false;
                }
                friendsModel.add(new FriendsBudgetModel(oFriend.id, false));
                newFriendsBudget.add(new FriendsBudget(oFriend.id, oFriend.username, "0" , false));
            }

            dAmountToPay += Float.parseFloat(sExpenseValue) / oTrip.getFriends().size();

            for (FriendsBudget oFriend: newFriendsBudget)
            {
                oFriend.amountToPay = String.valueOf(dAmountToPay);
            }


            if (addOwner)
            {
                friendsModel.add(new FriendsBudgetModel(UserSingleton.getInstance().getoCurrentUser().id, false));
            }
            expensesModel.add(new TripExpenseModel(counter.toString(),
                    sExpenseName,
                    UserSingleton.getInstance().getoCurrentUser().username,
                    sExpenseValue));

            newExpenses.add(new TripExpense(counter.toString(),
                    sExpenseName,
                    UserSingleton.getInstance().getoCurrentUser().username,
                    sExpenseValue));
            oTrip.setBudget(String.valueOf(dAmountToPay * oTrip.getFriends().size()));
            oTrip.setTripExpenses(newExpenses);
            oTrip.setFriends(newFriendsBudget);
            DatabaseReference msRef = FirebaseDatabase.getInstance().getReference("Trips").child(oTrip.getId().toString());
            msRef.child("tripExpense").setValue(expensesModel);
            msRef.child("friends").setValue(friendsModel);
            newExpensePopup.dismiss();
            setupData();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        oTrip = tripBudgetNeedsData.getData();
        setupData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnAddNewTripExpense:
                newExpensePopupView = getActivity().getLayoutInflater().inflate(R.layout.add_new_expense_popup, null);
                newExpensePopup = new PopupWindow(newExpensePopupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                newExpensePopup.setFocusable(true);
                newExpensePopup.showAtLocation(newExpensePopupView , Gravity.CENTER, 0, 0);
                Button btnSendFriendRequest = newExpensePopupView.findViewById(R.id.btnNewExpense);
                btnSendFriendRequest.setOnClickListener(this);
                Button btnCancelRequest = newExpensePopupView.findViewById(R.id.btnCancelExpense);
                btnCancelRequest.setOnClickListener(this);
                break;
            case R.id.btnNewExpense:
                addNewExpense();
                break;
            case R.id.btnCancelExpense:
                newExpensePopup.dismiss();

        }
    }


    @Override
    public void didPressPay(String id) {
        if (UserSingleton.getInstance().getoCurrentUser().id.equals(oTrip.getHost()))
        {
            DatabaseReference msRef = FirebaseDatabase.getInstance().getReference("Trips").child(oTrip.getId().toString()).child("friends");
            msRef.child(id).child("did_pay").setValue(!oTrip.getFriends().get(Integer.parseInt(id)).did_pay);
            oTrip.getFriends().get(Integer.parseInt(id)).did_pay = !oTrip.getFriends().get(Integer.parseInt(id)).did_pay;
            setupData();
        }
        else
        {
            Toast.makeText(getContext(), "Only trip host can change that", Toast.LENGTH_SHORT).show();
        }
    }
}