package com.manage.iahead;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
    private Handler handler;

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

        playTone = new PlayTone(this, HeadphonesettingActivity.this);
        ll_next = findViewById(R.id.ll_next);
        ll_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playTone!=null) {
                    playTone.stop();
                }
                handler.removeCallbacksAndMessages(null);
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
                        playTone.play(false, 1000, 0.02, 2000);
                        repeatPlay();
                    }
                }
            }
        };
        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(broadcastReceiver, receiverFilter);

    }

    private void repeatPlay(){
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playTone.stop();
                playTone.play(false, 1000, 0.02, 2000);
                repeatPlay();
            }
        }, 4000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Toast.makeText(HeadphonesettingActivity.this, "Press back, back once. Press again to quit app", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
        if(playTone!=null){
            playTone.stop();
        }
    }
}