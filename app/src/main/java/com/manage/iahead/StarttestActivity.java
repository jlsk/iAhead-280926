package com.manage.iahead;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.manage.iahead.Jsyn.PlayTone;
import com.manage.iahead.Jsyn.PlayToneListener;

import java.util.concurrent.ThreadLocalRandom;

public class StarttestActivity extends AppCompatActivity implements PlayToneListener {
    private LinearLayout llNext;
    private CountDownTimer cTimer = null;
    private ImageView ivHit;
    private int count=0, random;
    private boolean isLeft=false;
    private PlayTone playTone;
    private TextView tvText, tvTime;

    @Override
    public void onPlay() {

    }

    @Override
    public void onSleep() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_starttest);

        playTone = new PlayTone(this);

        tvTime = findViewById(R.id.tvTime);
        llNext = findViewById(R.id.llNext);
        llNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvText = findViewById(R.id.tvText);
        ivHit = findViewById(R.id.ivHit);
        ivHit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivHit.setBackground(getResources().getDrawable(R.drawable.yay));
                playTone.stop();

                if(count==5){
                    tvText.setText("CONGRATULATIONS! You have completed the configuration");
                    llNext.setVisibility(View.VISIBLE);
                    cancelTimer();
                    return;
                }
                randomPlay();
            }
        });
        randomPlay();
        startTimer();
    }

    private void randomPlay(){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacksAndMessages(null);
                ivHit.setBackground(getResources().getDrawable(R.drawable.ongoing));
                random = ThreadLocalRandom.current().nextInt(0, 1);
                if(random==0){
                    isLeft=true;
                }else{
                    isLeft=false;
                }
                playTone.play(isLeft, 1000, 0.02, 3000);
                count++;
            }
        }, 2000);

    }

    private void startTimer() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacksAndMessages(null);
                tvTime.setText("00:00");
                cTimer = new CountDownTimer(5000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        tvTime.setText(String.valueOf(millisUntilFinished/1000));
                    }
                    public void onFinish() {
                        ivHit.setBackground(getResources().getDrawable(R.drawable.tryagain));
                        cancelTimer();
                        startTimer();
                    }
                };
                cTimer.start();
            }
        }, 2000);

    }


    //cancel timer
    private void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }
}