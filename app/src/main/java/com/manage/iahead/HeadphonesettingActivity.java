package com.manage.iahead;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.manage.iahead.Jsyn.PlayTone;
import com.manage.iahead.Jsyn.PlayToneListener;
import com.manage.iahead.Utility.SharedPref;
import com.manage.iahead.Utility.Uti;

public class HeadphonesettingActivity extends AppCompatActivity implements PlayToneListener {
    BroadcastReceiver broadcastReceiver;
    boolean Microphone_Plugged_in = false;
    private ImageView iv_alert;
    private LinearLayout ll_next;
    private PlayTone playTone;
    private Handler handler, handler1;
    private int countBack=0;
    private CountDownTimer cTimerBack = null;

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

        SharedPref.init(this);
        String screeninglevel = SharedPref.read(SharedPref.SCREENINGLEVEL, "");
        if(screeninglevel.equalsIgnoreCase("40")) {
            am.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    2,
                    0);
        }else{
            am.setStreamVolume(
                    AudioManager.STREAM_MUSIC,
                    1,
                    0);
        }

        playTone = new PlayTone(this, HeadphonesettingActivity.this);
        ll_next = findViewById(R.id.ll_next);
        ll_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playTone!=null) {
                    playTone.stop();
                }
                if(handler!=null) {
                    handler.removeCallbacksAndMessages(null);
                }
                if(handler1!=null){
                    handler1.removeCallbacksAndMessages(null);
                }
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
                        Toast.makeText(getApplicationContext(), "Earphones and Microphone not plugged in", Toast.LENGTH_LONG).show();
                    }
                    if (iii == 1) {
                        Microphone_Plugged_in = true;
                        Toast.makeText(getApplicationContext(), "Earphones and Microphone plugged in",
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
                handler.removeCallbacksAndMessages(null);
                playTone.stop();
                playTone.play(false, 1000, 0.02, 2000);
                repeatPlay();
            }
        }, 4000);
    }

//    @Override
//    public void onBackPressed() {
//
//        if(countBack==0) {
//            cTimerBack = new CountDownTimer(3000, 1000) {
//                public void onTick(long millisUntilFinished) {
//                }
//                public void onFinish() {
//                    countBack=0;
//                }
//            };
//            cTimerBack.start();
//            Toast.makeText(HeadphonesettingActivity.this, "Press back again to quit the app", Toast.LENGTH_SHORT).show();
//            countBack = 1;
//        }else{
//            cancelTimerBack();
//            finish();
//        }
//
//    }

    private void cancelTimerBack(){
        if(cTimerBack!=null) {
            cTimerBack.cancel();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(handler!=null) {
            handler.removeCallbacksAndMessages(null);
        }
        if(handler1!=null){
            handler1.removeCallbacksAndMessages(null);
        }
        if(playTone!=null){
            playTone.stop();
        }
        if(broadcastReceiver!=null) {
            unregisterReceiver(broadcastReceiver);
        }
        cancelTimerBack();
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