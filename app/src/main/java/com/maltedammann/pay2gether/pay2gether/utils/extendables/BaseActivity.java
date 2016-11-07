package com.maltedammann.pay2gether.pay2gether.utils.extendables;

import android.app.ProgressDialog;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by damma on 21.10.2016.
 */
    public class BaseActivity extends AppCompatActivity {

        @VisibleForTesting
        public ProgressDialog mProgressDialog;

        public void showProgressDialog(String message) {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage(message);
                mProgressDialog.setIndeterminate(true);
            }

            mProgressDialog.show();
        }

        public void hideProgressDialog() {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }

        @Override
        public void onStop() {
            super.onStop();
            hideProgressDialog();
        }

    }
