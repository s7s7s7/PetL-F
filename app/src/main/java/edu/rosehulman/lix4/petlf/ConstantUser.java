package edu.rosehulman.lix4.petlf;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.rosehulman.lix4.petlf.models.User;

/**
 * Created by phillee on 8/3/2017.
 */

public class ConstantUser {
    public static FirebaseUser currentUser = null;
    public static FirebaseAuth currentAuth = null;

    public static void setCurrentUser(FirebaseUser user) {
        Log.d("setCurrentUser: ", user + "");
        currentUser = user;
    }

    public static void setCurrentAuth(FirebaseAuth auth){
        currentAuth = auth;
    }

    public static boolean hasUser() {
        return currentUser != null;
    }

    public static void removeCurrentUser() {
        currentUser = null;
    }


}
