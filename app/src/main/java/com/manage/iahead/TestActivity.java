package com.manage.iahead;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.manage.iahead.Jsyn.PlayTone;
import com.manage.iahead.Jsyn.PlayToneListener;

public class TestActivity extends AppCompatActivity implements PlayToneListener {
    private PlayTone playTone;
    private CountDownTimer cTimerBack;

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

        playTone = new PlayTone(this, this);

        playTone.play(false, 1000, 0.02, 5000);
//        playTone.start();
        timerRecord();
    }

    private void timerRecord(){
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("test","testsleepstop");
                handler.removeCallbacksAndMessages(null);
                playTone.stop();
                handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.removeCallbacksAndMessages(null);
//                        playTone.start();
                        Log.d("test","testsleepstart");
                        timerRecord();
                    }
                }, 2000);
            }
        }, 5000);
    }
}
