package com.example.graph.auth;

import com.example.graph.plot.GraphingApp;

public class AuthService {
    public static User authenticated = null;
    GraphingApp app;
    public void logout()
    {
     if(authenticated!=null)
     {
         System.out.println("Logout Succesfully");
         app.openLoginScene();
     }else
     {
         throw new Error("No user is logged in!");
     }
    }

}
