package com.manage.iahead;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.manage.iahead.Utility.Uti;

public class HeadphonesettingActivity extends AppCompatActivity {
    BroadcastReceiver broadcastReceiver;
    boolean Microphone_Plugged_in = false;
    private ImageView iv_alert;
    private LinearLayout ll_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_headphonesetting);

        ll_next = findViewById(R.id.ll_next);
        ll_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeadphonesettingActivity.this, LeftrightActivity.class);
                startActivity(intent);
                finish();
            }
        });
        iv_alert = findViewById(R.id.iv_alert);
        boolean check = Uti.isHeadphonesPlugged(this);
        if(!check){
            Toast.makeText(this, "Please plug in your headset", Toast.LENGTH_SHORT).show();
            return;
        }

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final String action = intent.getAction();
                int iii;
                if (Intent.ACTION_HEADSET_PLUG.equals(action)) {
                    iii = intent.getIntExtra("state", -1);
                    if (iii == 0) {
                        Microphone_Plugged_in = false;
                        iv_alert.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "microphone not plugged in", Toast.LENGTH_LONG).show();
                    }
                    if (iii == 1) {
                        Microphone_Plugged_in = true;
                        Toast.makeText(getApplicationContext(), "microphone plugged in",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(broadcastReceiver, receiverFilter);

    }
}