package com.manage.iahead;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class ReadytestActivity extends AppCompatActivity {
    private LinearLayout ll_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readytest);

        ll_start = findViewById(R.id.ll_start);
        ll_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReadytestActivity.this, SelectnoisyquietActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}