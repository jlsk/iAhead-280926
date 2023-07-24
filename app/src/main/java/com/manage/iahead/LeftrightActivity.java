package com.manage.iahead;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class LeftrightActivity extends AppCompatActivity {
    private ImageView ivClose;
    private TextView tv_proceed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_leftright);

        tv_proceed = findViewById(R.id.tv_proceed);
        ivClose = findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivClose.setVisibility(View.GONE);
                tv_proceed.setVisibility(View.VISIBLE);
            }
        });
        tv_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeftrightActivity.this, ReadytestActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}