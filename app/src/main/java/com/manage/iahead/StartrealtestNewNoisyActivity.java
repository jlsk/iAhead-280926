package com.manage.iahead;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.manage.iahead.Jsyn.PlayTone;
import com.manage.iahead.Jsyn.PlayToneListener;
import com.manage.iahead.Utility.SharedPref;

import java.util.ArrayList;
import java.util.Random;

public class StartrealtestNewNoisyActivity extends AppCompatActivity implements PlayToneListener {
    private LinearLayout llNext;
    private CountDownTimer cTimer = null;
    private ImageView ivHit;
    private int count=0, random, countall500=0, countall1000=0, countall2000=0, countall4000=0;
    private boolean checkPass=false;
    private PlayTone playTone;
    private TextView tvText, tvTime, tv_next, tvFrequency, tvDisplay;
    private String levelType="level1left";
    private String leftStr="Refer", rightStr="Refer";
    private Handler handler;
    private boolean isLeft=false;
    private ArrayList<String>arrtone500=new ArrayList<>();
    private ArrayList<String>arrtone1000=new ArrayList<>();
    private ArrayList<String>arrtone2000=new ArrayList<>();
    private ArrayList<String>arrtone4000=new ArrayList<>();
    private int type=1, countSeq=0;
    final int min = 0;
    final int max = 1;
    private Handler handler1;
    private String result="";
    private boolean check500press1=false, check500press2=false, check500press3=false,
            check1000press1=false, check1000press2=false, check1000press3=false, check2000press1=false, check2000press2=false,
            check2000press3=false, check4000press1=false, check4000press2=false, check4000press3=false, check500Leftpress1=false,
            check500Leftpress2=false, check500Leftpress3=false,
            check1000Leftpress1=false, check1000Leftpress2=false, check1000Leftpress3=false, check2000Leftpress1=false,
            check2000Leftpress2=false,
            check2000Leftpress3=false, check4000Leftpress1=false, check4000Leftpress2=false, check4000Leftpress3=false, ableClick=true;

    @Override
    public void onPlay() {

    }

    @Override
    public void onSleep() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(handler1!=null){
            handler1.removeCallbacksAndMessages(null);
        }
        handler.removeCallbacksAndMessages(null);
        cancelTimer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starttest);

        AudioManager am =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        am.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                80,
                0);

        playTone = new PlayTone(this, StartrealtestNewNoisyActivity.this);

        SharedPref.init(this);
        String DEBUGMODESTR = SharedPref.read(SharedPref.DEBUGMODE, "");

        tvDisplay = findViewById(R.id.tvDisplay);
        tvFrequency = findViewById(R.id.tvFrequency);
        tvFrequency.setText("Frequency-"+String.valueOf(500));
        if(DEBUGMODESTR.equalsIgnoreCase("")){
            tvFrequency.setVisibility(View.GONE);
        }
        tv_next = findViewById(R.id.tv_next);
        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                Log.d("test", "testpress-isleft-"+String.valueOf(isLeft));
                Log.d("test", "testpress-type-"+String.valueOf(type));
                if(!ableClick){
                    return;
                }
                checkPass=true;
                cancelTimer();
                ivHit.setBackground(getResources().getDrawable(R.drawable.yay));
                if(playTone==null){
                    return;
                }
                playTone.stop();
                playTone=null;
                if(type==1) {
                    if(countall1000==1){
                        if(!isLeft) {
                            check1000press1 = true;
                        }else{
                            check1000Leftpress1 = true;
                        }
                    }else if(countall1000==2){
                        if(!isLeft) {
                            check1000press2 = true;
                        }else{
                            check1000Leftpress2 = true;
                        }
                    }else if(countall1000==3){
                        if(!isLeft) {
                            check1000press3 = true;
                        }else{
                            check1000Leftpress3 = true;
                        }
                    }
                    if(!isLeft) {
                        if(check1000press1 && check1000press2){
                            startTimerFirst2000(isLeft);
                        }else{
                            if(countall1000==3){
                                startTimerFirst2000(isLeft);
                            }else {
                                startTimerFirst1000(isLeft);
                            }
                        }
                    }else{
                        if(check1000Leftpress1 && check1000Leftpress2){
                            startTimerFirst2000(isLeft);
                        }else{
                            if(countall1000==3){
                                startTimerFirst2000(isLeft);
                            }else {
                                startTimerFirst1000(isLeft);
                            }
                        }
                    }
                }else if(type==2) {
                    if(countall2000==1){
                        if(!isLeft) {
                            check2000press1 = true;
                        }else{
                            check2000Leftpress1 = true;
                        }
                    }else if(countall2000==2){
                        if(!isLeft) {
                            check2000press2 = true;
                        }else{
                            check2000Leftpress2 = true;
                        }
                    }else if(countall2000==3){
                        if(!isLeft) {
                            check2000press3 = true;
                        }else{
                            check2000Leftpress3 = true;
                        }
                    }
                    if(!isLeft) {
                        if(check2000press1 && check2000press2){
                            startTimerFirst4000(isLeft);
                        }else{
                            if(countall2000==3){
                                startTimerFirst4000(isLeft);
                            }else {
                                startTimerFirst2000(isLeft);
                            }
                        }
                    }else{
                        if(check2000Leftpress1 && check2000Leftpress2){
                            startTimerFirst4000(isLeft);
                        }else{
                            if(countall2000==3){
                                startTimerFirst4000(isLeft);
                            }else {
                                startTimerFirst2000(isLeft);
                            }
                        }
                    }
                }else if(type==3) {
                    if(countall4000==1){
                        if(!isLeft) {
                            check4000press1 = true;
                        }else{
                            check4000Leftpress1 = true;
                        }
                    }else if(countall4000==2){
                        if(!isLeft) {
                            check4000press2 = true;
                        }else{
                            check4000Leftpress2 = true;
                        }
                    }else if(countall4000==3){
                        if(!isLeft) {
                            check4000press3 = true;
                        }else{
                            check4000Leftpress3 = true;
                        }
                    }
                    if(!isLeft) {
                        if(check4000press1 && check4000press2){
                            isLeft=true;
                            type=1;
                            countall1000=0;
                            countall2000=0;
                            countall4000=0;
                            startTimerFirst1000(isLeft);
                        }else{
                            if(countall4000==3){
                                isLeft=true;
                                type=1;
                                countall1000=0;
                                countall2000=0;
                                countall4000=0;
                                startTimerFirst1000(isLeft);
                            }else {
                                startTimerFirst4000(isLeft);
                            }
                        }
                    }else{
                        if(check4000Leftpress1 && check4000Leftpress2){
                            goNextPage();
                        }else{
                            if(countall4000==3){
                                goNextPage();
                            }else {
                                startTimerFirst4000(isLeft);
                            }
                        }
                    }

                }
            }
        });
        type=1;
        startTimerFirst1000(isLeft);
    }

    private int getInternal(){
        int randomInt = new Random().nextInt((6 - 3) + 1) + 3;
        randomInt=randomInt*1000;
        return randomInt;
    }

    private void startTimerFirst1000(boolean flag) {
        type=1;
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacksAndMessages(null);
                ivHit.setBackground(getResources().getDrawable(R.drawable.ongoing));
                countall1000++;
                checkPass=false;
                tvFrequency.setText("Frequency-"+String.valueOf(1000));
                if(playTone==null){
                    playTone = new PlayTone(StartrealtestNewNoisyActivity.this, StartrealtestNewNoisyActivity.this);
                }
                handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.removeCallbacksAndMessages(null);
                        playTone.play(flag, 1000, 0.02, 3000);
                    }
                }, 200);
                countSeq++;
                cTimer = new CountDownTimer(getInternal(), 1000) {
                    public void onTick(long millisUntilFinished) {
                        tvTime.setText("00:0"+String.valueOf(millisUntilFinished/1000));
                        String leftright="";
                        if(flag){
                            leftright="Left";
                        }else{
                            leftright="Right";
                        }
                        tvDisplay.setText(String.valueOf(countSeq)+", 1000hz, "+String.valueOf(millisUntilFinished/1000)+ "secs, 0.02amp, "+leftright);
                        if(String.valueOf(millisUntilFinished/1000).equalsIgnoreCase("0")){
                            ableClick=false;
                        }else{
                            ableClick=true;
                        }
                    }
                    public void onFinish() {

                        Log.d("test", "testnopress1000-isleft-"+String.valueOf(isLeft));
                        Log.d("test", "testnopress1000-type-"+String.valueOf(type));
                        playTone=null;
                        cancelTimer();
                        if(!checkPass){
                            ivHit.setBackground(getResources().getDrawable(R.drawable.tryagain));
                            arrtone1000.add("N");
                        }
                        if(!isLeft) {
                            if(check1000press1 && check1000press2){
                                startTimerFirst2000(isLeft);
                            }else{
                                if(countall1000==3){
                                    startTimerFirst2000(isLeft);
                                }else {
                                    startTimerFirst1000(isLeft);
                                }
                            }
                        }else{
                            if(check1000Leftpress1 && check1000Leftpress2){
                                startTimerFirst2000(isLeft);
                            }else{
                                if(countall1000==3){
                                    startTimerFirst2000(isLeft);
                                }else {
                                    startTimerFirst1000(isLeft);
                                }
                            }
                        }
                    }
                };
                cTimer.start();
            }
        }, 3000);
    }

    private void startTimerFirst2000(boolean flag) {
        type=2;
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacksAndMessages(null);
                ivHit.setBackground(getResources().getDrawable(R.drawable.ongoing));
                countall2000++;
                checkPass=false;
                tvFrequency.setText("Frequency-"+String.valueOf(2000));
                if(playTone==null){
                    playTone = new PlayTone(StartrealtestNewNoisyActivity.this, StartrealtestNewNoisyActivity.this);
                }
                handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.removeCallbacksAndMessages(null);
                        playTone.play(flag, 2000, 0.02, 3000);
                    }
                }, 200);
                countSeq++;
                cTimer = new CountDownTimer(getInternal(), 1000) {
                    public void onTick(long millisUntilFinished) {
                        tvTime.setText("00:0"+String.valueOf(millisUntilFinished/1000));
                        String leftright="";
                        if(flag){
                            leftright="Left";
                        }else{
                            leftright="Right";
                        }
                        tvDisplay.setText(String.valueOf(countSeq)+", 2000hz, "+String.valueOf(millisUntilFinished/1000)+ "secs, 0.02amp, "+leftright);
                        if(String.valueOf(millisUntilFinished/1000).equalsIgnoreCase("0")){
                            ableClick=false;
                        }else{
                            ableClick=true;
                        }
                    }
                    public void onFinish() {

                        Log.d("test", "testnopress2000-isleft-"+String.valueOf(isLeft));
                        Log.d("test", "testnopress2000-type-"+String.valueOf(type));

                        playTone=null;
                        cancelTimer();
                        if(!checkPass){
                            ivHit.setBackground(getResources().getDrawable(R.drawable.tryagain));
                            arrtone2000.add("N");
                        }
                        if(!isLeft) {
                            if(check2000press1 && check2000press2){
                                startTimerFirst4000(isLeft);
                            }else{
                                if(countall2000==3){
                                    startTimerFirst4000(isLeft);
                                }else {
                                    startTimerFirst2000(isLeft);
                                }
                            }
                        }else{
                            if(check2000Leftpress1 && check2000Leftpress2){
                                startTimerFirst4000(isLeft);
                            }else{
                                if(countall2000==3){
                                    startTimerFirst4000(isLeft);
                                }else {
                                    startTimerFirst2000(isLeft);
                                }
                            }
                        }
                    }
                };
                cTimer.start();
            }
        }, 3000);
    }

    private void startTimerFirst4000(boolean flag) {
        type=3;
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacksAndMessages(null);
                ivHit.setBackground(getResources().getDrawable(R.drawable.ongoing));
                countall4000++;
                checkPass=false;
                tvFrequency.setText("Frequency-"+String.valueOf(4000));
                if(playTone==null){
                    playTone = new PlayTone(StartrealtestNewNoisyActivity.this, StartrealtestNewNoisyActivity.this);
                }
                handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.removeCallbacksAndMessages(null);
                        playTone.play(flag, 4000, 0.02, 3000);
                    }
                }, 200);
                countSeq++;
                cTimer = new CountDownTimer(getInternal(), 1000) {
                    public void onTick(long millisUntilFinished) {
                        tvTime.setText("00:0"+String.valueOf(millisUntilFinished/1000));
                        String leftright="";
                        if(flag){
                            leftright="Left";
                        }else{
                            leftright="Right";
                        }
                        tvDisplay.setText(String.valueOf(countSeq)+", 4000hz, "+String.valueOf(millisUntilFinished/1000)+ "secs, 0.02amp, "+leftright);
                        if(String.valueOf(millisUntilFinished/1000).equalsIgnoreCase("0")){
                            ableClick=false;
                        }else{
                            ableClick=true;
                        }
                    }
                    public void onFinish() {

                        Log.d("test", "testnopress4000-isleft-"+String.valueOf(isLeft));
                        Log.d("test", "testnopress4000-type-"+String.valueOf(type));

                        playTone=null;
                        cancelTimer();
                        if(!checkPass){
                            ivHit.setBackground(getResources().getDrawable(R.drawable.tryagain));
                            arrtone4000.add("N");
                        }
                        if(!isLeft) {
                            if(check4000press1 && check4000press2){
                                isLeft=true;
                                type=1;
                                countall500=0;
                                countall1000=0;
                                countall2000=0;
                                countall4000=0;
                                startTimerFirst1000(isLeft);
                            }else{
                                if(countall4000==3){
                                    isLeft=true;
                                    type=1;
                                    countall500=0;
                                    countall1000=0;
                                    countall2000=0;
                                    countall4000=0;
                                    startTimerFirst1000(isLeft);
                                }else {
                                    startTimerFirst4000(isLeft);
                                }
                            }
                        }else{
                            if(check4000Leftpress1 && check4000Leftpress2){
                                goNextPage();
                            }else{
                                if(countall4000==3){
                                    goNextPage();
                                }else {
                                    startTimerFirst4000(isLeft);
                                }
                            }
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

    private void goNextPage(){
        Intent intent = new Intent(StartrealtestNewNoisyActivity.this, ResultpageNoisyActivity.class);
        String Str1000 = "REFER", Str2000 = "REFER", Str4000 = "REFER";
        String Str1000Left = "REFER", Str2000Left = "REFER", Str4000Left = "REFER";
        if(check1000press1 && check1000press2 || check1000press2 && check1000press3
                || check1000press1 && check1000press3){
            Str1000="PASS";
        }
        if(check2000press1 && check2000press2 || check2000press2 && check2000press3
                || check2000press1 && check2000press3){
            Str2000="PASS";
        }
        if(check4000press1 && check4000press2 || check4000press2 && check4000press3
                || check4000press1 && check4000press3){
            Str4000="PASS";
        }
        if(check1000Leftpress1 && check1000Leftpress2 || check1000Leftpress2 && check1000Leftpress3
                || check1000Leftpress1 && check1000Leftpress3){
            Str1000Left="PASS";
        }
        if(check2000Leftpress1 && check2000Leftpress2 || check2000Leftpress2 && check2000Leftpress3
                || check2000Leftpress1 && check2000Leftpress3){
            Str2000Left="PASS";
        }
        if(check4000Leftpress1 && check4000Leftpress2 || check4000Leftpress2 && check4000Leftpress3
                || check4000Leftpress1 && check4000Leftpress3){
            Str4000Left="PASS";
        }
        intent.putExtra("right1000", Str1000);
        intent.putExtra("right2000", Str2000);
        intent.putExtra("right4000", Str4000);
        intent.putExtra("left1000", Str1000Left);
        intent.putExtra("left2000", Str2000Left);
        intent.putExtra("left4000", Str4000Left);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Toast.makeText(StartrealtestNewNoisyActivity.this, "Press back again to quit the app", Toast.LENGTH_SHORT).show();
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