package com.manage.iahead;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.manage.iahead.Jsyn.PlayTone;
import com.manage.iahead.Jsyn.PlayToneListener;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class StarttestActivity extends AppCompatActivity implements PlayToneListener {
    private LinearLayout llNext;
    private CountDownTimer cTimer = null;
    private ImageView ivHit;
    private int count=0, random, countall=0;
    private boolean checkPass=false;
    private PlayTone playTone;
    private TextView tvText, tvTime, tv_next;
    private Handler handler;
    private boolean isLeft=false;
    final int min = 0;
    final int max = 1;

    @Override
    public void onPlay() {

    }

    @Override
    public void onSleep() {

    }

    @Override
    protected void onPause() {
        super.onPause();

        handler.removeCallbacksAndMessages(null);
        cancelTimer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starttest);

        playTone = new PlayTone(this);

        tv_next = findViewById(R.id.tv_next);
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(tv_next.getText().toString().equalsIgnoreCase("next")){
                    Intent intent = new Intent(StarttestActivity.this, StartrealtestNewActivity.class);
                    startActivity(intent);
                    finish();
//                    return;
//                }
//                finish();
            }
        });
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
                cancelTimer();
                ivHit.setBackground(getResources().getDrawable(R.drawable.yay));
                playTone.stop();
                playTone=null;
                checkPass=true;
                count++;
                if(count==2){
                    tvTime.setText("");
                    tvText.setText("CONGRATULATIONS! You have completed the configuration");
                    llNext.setVisibility(View.VISIBLE);
                    return;
                }
                countall++;
                startTimer();
            }
        });
        //randomPlay();
        startTimer();
    }

    private void randomPlay(){
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacksAndMessages(null);
                ivHit.setBackground(getResources().getDrawable(R.drawable.ongoing));
//                random = ThreadLocalRandom.current().nextInt(0, 1);
//                if(random==0){
//                    isLeft=true;
//                }else{
//                    isLeft=false;
//                }
                if(playTone==null){
                    playTone = new PlayTone(StarttestActivity.this);
                }
                playTone.play(true, 1000, 0.02, 3000);
                count++;
            }
        }, 2000);

    }

    private void startTimer() {
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacksAndMessages(null);
                ivHit.setBackground(getResources().getDrawable(R.drawable.ongoing));
                countall++;
                checkPass=false;
                random = new Random().nextInt((max - min) + 1) + min;
                if(random==0){
                    isLeft=true;
                }else{
                    isLeft=false;
                }
                if(playTone==null){
                    playTone = new PlayTone(StarttestActivity.this);
                }
                playTone.play(isLeft, 1000, 0.02, 3000);

                cTimer = new CountDownTimer(5000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        tvTime.setText("00:0"+String.valueOf(millisUntilFinished/1000));
                    }
                    public void onFinish() {
                        playTone=null;
                        cancelTimer();
                        if(countall>3){
                            Toast.makeText(StarttestActivity.this, "Fail, out of 2/3 must pass", Toast.LENGTH_SHORT).show();
                            tvTime.setText("");
                            llNext.setVisibility(View.VISIBLE);
//                            tv_next.setText("Finish");
                            return;
                        }
                        if(!checkPass){
                            ivHit.setBackground(getResources().getDrawable(R.drawable.tryagain));
                            startTimer();
                            countall++;
                        }
                    }
                };
                cTimer.start();
            }
        }, 3000);
    }

    //cancel timer
    private void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }
}