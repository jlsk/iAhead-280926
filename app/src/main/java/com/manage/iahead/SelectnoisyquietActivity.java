package com.manage.iahead;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import com.manage.iahead.Utility.SharedPref;

public class SelectnoisyquietActivity extends AppCompatActivity {
    private ImageView ivNoisy, ivQuiet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectnoisyquiet);

        SharedPref.init(this);
        ivNoisy = findViewById(R.id.ivNoisy);
        ivNoisy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.write(SharedPref.TESTLOCATION, "Noisy");
                Intent intent = new Intent(SelectnoisyquietActivity.this, StartrealtestNewNoisyActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ivQuiet = findViewById(R.id.ivQuiet);
        ivQuiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPref.write(SharedPref.TESTLOCATION, "Quiet");
                Intent intent = new Intent(SelectnoisyquietActivity.this, StartrealtestNewActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        2000);
            }
        }
        else {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2000:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Toast.makeText(SelectnoisyquietActivity.this, "Press back again to quit the app", Toast.LENGTH_SHORT).show();
    }
}