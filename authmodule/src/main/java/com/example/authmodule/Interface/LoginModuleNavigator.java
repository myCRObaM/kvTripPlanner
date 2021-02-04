package com.example.authmodule.Interface;

import android.content.Context;
import android.content.Intent;

import com.example.authmodule.ActivityLoginHost;

public class LoginModuleNavigator {
    private LoginModuleNavigator(){}
    public LoginDoneInterface loginDoneInterface;
    private static LoginModuleNavigator oInstance;


    public static LoginModuleNavigator getInstance()
    {
        if (oInstance == null)
        {
            oInstance = new LoginModuleNavigator();
        }
        return oInstance;
    }

    public void navigateToMainTest(Context context) {

        Intent intent = new Intent(context, ActivityLoginHost.class);
        context.startActivity(intent);
    }
}
