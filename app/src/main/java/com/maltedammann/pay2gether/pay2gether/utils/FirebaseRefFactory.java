package com.maltedammann.pay2gether.pay2gether.utils;

import com.firebase.client.Firebase;

/**
 * Created by damma on 26.10.2016.
 */

public class FirebaseRefFactory {

    final static private String mUrl = "https://pay2gether-30874.firebaseio.com";

    public static Firebase getRef(String path) {
        if (path == null || path.isEmpty()) {
            return new Firebase(mUrl);
        }
        return new Firebase(mUrl).child(path);
    }

    public static Firebase getRef() { return getRef(null); }

    public static Firebase getUsersRef() { return getRef("users"); }


}
