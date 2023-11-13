package com.manage.iahead;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
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
    private Handler handler, handler1;

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

        AudioManager am =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        am.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                80,
                0);

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
                        handler1 = new Handler(Looper.getMainLooper());
                        handler1.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                handler1.removeCallbacksAndMessages(null);
                                playTone.play(false, 1000, 0.02, 2000);
                            }
                        }, 200);
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

        Toast.makeText(HeadphonesettingActivity.this, "Press back again to quit the app", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
        if(handler1!=null){
            handler1.removeCallbacksAndMessages(null);
        }
        if(playTone!=null){
            playTone.stop();
        }
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }
}