package com.example.authmodule;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authmodule.Interface.LoginDoneInterface;
import com.example.authmodule.Interface.LoginModuleNavigator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;


public class LoginFragment extends Fragment implements View.OnClickListener{

    Button btnLoginButton;
    TextView btnForgotPassword, btnRegister;
    EditText inptEmail, inptPassword;
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupView(view);
    }

    private void setupView(View view)
    {
        mAuth = FirebaseAuth.getInstance();
        inptEmail = view.findViewById(R.id.inptEmail);
        inptPassword = view.findViewById(R.id.inptPassword);

        btnRegister = view.findViewById(R.id.txtRegister);
        btnRegister.setOnClickListener(this::onClick);

        btnLoginButton = view.findViewById(R.id.btnLogin);
        btnLoginButton.setOnClickListener(this::onClick);

        btnForgotPassword = view.findViewById(R.id.txtForgotPassword);
        btnForgotPassword.setOnClickListener(this::onClick);
    }

    private void loginButtonPressed()
    {
        mAuth.signInWithEmailAndPassword(inptEmail.getText().toString(), inptPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Login succesful", Toast.LENGTH_LONG).show();
                            LoginModuleNavigator.getInstance().loginDoneInterface.loginDone();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Error logging in", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txtRegister)
        {
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_loginFragment_to_registerFragment);
        }
        else if (view.getId() == R.id.btnLogin)
        {
            loginButtonPressed();
        }
        else if (view.getId() == R.id.txtForgotPassword)
        {
            LoginModuleNavigator.getInstance().loginDoneInterface.loginDone();
        }
    }
}