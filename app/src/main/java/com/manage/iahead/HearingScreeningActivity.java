package com.manage.iahead;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.jsyn.unitgen.Select;

public class HearingScreeningActivity extends AppCompatActivity {
    private LinearLayout ll_start, ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hearingscreening);

        ll_start = findViewById(R.id.ll_start);
        ll_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HearingScreeningActivity.this, SelectnoisyquietActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ll_back = findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HearingScreeningActivity.this, LeftrightActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}