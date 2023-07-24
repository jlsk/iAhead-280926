package com.manage.iahead;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SelectnoisyquietActivity extends AppCompatActivity {
    private ImageView ivNoisy, ivQuiet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_selectnoisyquiet);

        ivNoisy = findViewById(R.id.ivNoisy);
        ivNoisy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectnoisyquietActivity.this, TestfreActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ivQuiet = findViewById(R.id.ivQuiet);
        ivQuiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectnoisyquietActivity.this, TestfreActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}