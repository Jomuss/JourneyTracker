package com.joemoss.firebasetest.Models;

import com.google.firebase.auth.FirebaseUser;

public class CurrentUser {
    private static final CurrentUser ourInstance = new CurrentUser();
    public FirebaseUser currentUser;
    private boolean profileDataChange = false;


    public static CurrentUser getInstance() {
        return ourInstance;
    }

    private CurrentUser() {
    }

    public void intializeUser(FirebaseUser curUser){
        currentUser = curUser;
    }

    public void profileDataChanged(boolean value){
        profileDataChange = value;
    }
    public boolean getProfileDataChangedValue(){
        return profileDataChange;
    }


}
