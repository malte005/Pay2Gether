package com.maltedammann.pay2gether.pay2gether.utils.interfaces;

/**
 * Created by damma on 10.11.2016.
 */

public interface ReadDataInterface {
    void onSignOutCleanup();

    void onSignInInitializer();

    void attachDatabaseReadListener();

    void detachDatabaseListener();
}
