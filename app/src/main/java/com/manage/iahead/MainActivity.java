package com.manage.iahead;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private ImageView iv_child, iv_parent, iv_therapist;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intent=new Intent(this, EnteridActivity.class);
        iv_child = findViewById(R.id.iv_child);
        iv_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type", "child");
                startActivity(intent);
            }
        });
        iv_parent = findViewById(R.id.iv_parent);
        iv_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type", "parent");
                startActivity(intent);
            }
        });
        iv_therapist = findViewById(R.id.iv_therapist);
        iv_therapist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type", "therapist");
                startActivity(intent);
            }
        });
    }
}