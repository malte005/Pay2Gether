package com.maltedammann.pay2gether.pay2gether.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by damma on 02.11.2016.
 */

public class UIHelper {

    public static void snack(View v, String msg){
        Snackbar.make(v, msg, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }
}
