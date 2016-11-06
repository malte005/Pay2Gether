package com.maltedammann.pay2gether.pay2gether.utils;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.android.gms.ads.MobileAds;
import com.maltedammann.pay2gether.pay2gether.R;

/**
 * Created by damma on 25.10.2016.
 */

public class PayApplication extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);

        MobileAds.initialize(getApplicationContext(), getString(R.string.banner_ad_unit_id));
    }
}
