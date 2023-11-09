package com.manage.iahead;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.manage.iahead.Utility.SharedPref;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private ImageView iv_child, iv_parent, iv_therapist;
    private Intent intent;
    private LinearLayout view_hidden;
    private String DEBUGMODESTR="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPref.init(this);
        DEBUGMODESTR = SharedPref.read(SharedPref.DEBUGMODE, "");

        view_hidden = findViewById(R.id.view_hidden);
        view_hidden.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(DEBUGMODESTR.equalsIgnoreCase("")){
                    SharedPref.write(SharedPref.DEBUGMODE, "Y");
                    Toast.makeText(MainActivity.this, "Debug Mode Activated", Toast.LENGTH_SHORT).show();
                }else{
                    SharedPref.write(SharedPref.DEBUGMODE, "");
                    Toast.makeText(MainActivity.this, "Debug Mode Deactivated", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        intent=new Intent(this, EnteridActivity.class);
        iv_child = findViewById(R.id.iv_child);
        iv_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type", "child");
                SharedPref.write(SharedPref.TYPE, "child");
                startActivity(intent);

            }
        });
        iv_parent = findViewById(R.id.iv_parent);
        iv_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type", "parent");
                SharedPref.write(SharedPref.TYPE, "parent");
                startActivity(intent);
            }
        });
        iv_therapist = findViewById(R.id.iv_therapist);
        iv_therapist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type", "therapist");
                SharedPref.write(SharedPref.TYPE, "therapist");
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Toast.makeText(MainActivity.this, "Press back, back once. Press again to quit app", Toast.LENGTH_SHORT).show();
    }
}