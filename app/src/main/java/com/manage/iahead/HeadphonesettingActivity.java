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

import com.manage.iahead.Jsyn.PlayTone;
import com.manage.iahead.Jsyn.PlayToneListener;
import com.manage.iahead.Utility.Uti;

public class HeadphonesettingActivity extends AppCompatActivity implements PlayToneListener {
    BroadcastReceiver broadcastReceiver;
    boolean Microphone_Plugged_in = false;
    private ImageView iv_alert;
    private LinearLayout ll_next;
    private PlayTone playTone;

    @Override
    public void onPlay() {

    }

    @Override
    public void onSleep() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headphonesetting);

        playTone = new PlayTone(this);
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
        iv_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_alert.setVisibility(View.GONE);
            }
        });
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
                        playTone.play(true, 1000, 0.02, 3000);
                    }
                }
            }
        };
        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(broadcastReceiver, receiverFilter);

    }
}