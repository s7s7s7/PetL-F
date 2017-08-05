package edu.rosehulman.lix4.petlf;

import android.util.Log;

import edu.rosehulman.lix4.petlf.models.User;

/**
 * Created by phillee on 8/3/2017.
 */

public class ConstantUser {
    public static User currentUser = null;

    public static void setCurrentUser(User user) {
        Log.d("setCurrentUser: ", user + "");
        currentUser = user;
    }

    public static boolean hasUser() {
        return currentUser != null;
    }

    public static void removeCurrentUser() {
        currentUser = null;
    }


}
