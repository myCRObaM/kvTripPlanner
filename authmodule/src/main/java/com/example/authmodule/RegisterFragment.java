package com.example.authmodule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.authmodule.Classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterFragment extends Fragment implements View.OnClickListener {

    EditText inptUsername, inptMail, inptPassword;
    Button btnRegister;
    FirebaseAuth mAuth;
    NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView(view);
    }

    private void setupView(View view)
    {
        inptUsername = view.findViewById(R.id.inptUserNameRegister);
        inptPassword = view.findViewById(R.id.inptPasswordRegister);
        inptMail = view.findViewById(R.id.inptEmailRegister);
        btnRegister = view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        navController = Navigation.findNavController(view);
    }

    private void registerUser()
    {
        String email, password, username;
        email = inptMail.getText().toString();
        password = inptPassword.getText().toString();
        username = inptUsername.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser userFire = mAuth.getCurrentUser();
                        Toast.makeText(getContext(), "SignUp successfull please login.",
                                Toast.LENGTH_SHORT).show();
                            User user = new User(username, password, email);
                            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Users").child(userFire.getUid());
                                    mRef.child("email").setValue(user.email);
                                    mRef.child("username").setValue(user.username);

                        navController.navigate(R.id.action_registerFragment_to_loginFragment);
                    }
                });
    }


    @Override
    public void onClick(View view) {
        if (inptUsername.getText().toString().isEmpty())
        {
            inptUsername.setError("Username is required!");
            inptUsername.requestFocus();
            return;
        }
        if (inptPassword.getText().toString().isEmpty())
        {
            inptPassword.setError("Password is required!");
            inptPassword.requestFocus();
            return;
        }
        if (inptMail.getText().toString().isEmpty())
        {
            inptMail.setError("Email is required!");
            inptMail.requestFocus();
            return;
        }
        registerUser();
    }
}