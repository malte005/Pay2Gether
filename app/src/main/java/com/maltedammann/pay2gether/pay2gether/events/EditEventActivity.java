package com.maltedammann.pay2gether.pay2gether.events;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.maltedammann.pay2gether.pay2gether.R;

public class EditEventActivity extends AppCompatActivity {

    private static final String TAG = EditEventActivity.class.getSimpleName();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        toolbar = (Toolbar) findViewById(R.id.toolbar_editEvent);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
