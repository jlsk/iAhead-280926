package com.manage.iahead;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.manage.iahead.Jsyn.PlayTone;
import com.manage.iahead.Jsyn.PlayToneListener;

import java.util.ArrayList;
import java.util.Random;

public class StartrealtestNewActivity extends AppCompatActivity implements PlayToneListener {
    private LinearLayout llNext;
    private CountDownTimer cTimer = null;
    private ImageView ivHit;
    private int count=0, random, countall500=0, countall1000=0, countall2000=0, countall4000=0;
    private boolean checkPass=false;
    private PlayTone playTone;
    private TextView tvText, tvTime, tv_next, tvFrequency;
    private String levelType="level1left";
    private String leftStr="Refer", rightStr="Refer";
    private Handler handler;
    private boolean isLeft=false;
    private ArrayList<String>arrtone500=new ArrayList<>();
    private ArrayList<String>arrtone1000=new ArrayList<>();
    private ArrayList<String>arrtone2000=new ArrayList<>();
    private ArrayList<String>arrtone4000=new ArrayList<>();
    private int type=0;
    final int min = 0;
    final int max = 1;
    private String result="";

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

//        tvFrequency = findViewById(R.id.tvFrequency);
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
                checkPass=true;
                cancelTimer();
                ivHit.setBackground(getResources().getDrawable(R.drawable.yay));
                playTone.stop();
                playTone=null;
                if(type==0) {
                    countall500++;
                    arrtone500.add("Y");
                    checkLast500();
                }else if(type==1) {
                    countall1000++;
                    arrtone1000.add("Y");
                    checkLast1000();
                }else if(type==2) {
                    countall2000++;
                    arrtone2000.add("Y");
                    checkLast2000();
                }else if(type==3) {
                    countall4000++;
                    arrtone4000.add("Y");
                    checkLast4000();
                }
            }
        });
        type=0;
        startTimerFirst500(true);
    }

    private void checkLast500(){
        for(int i=0;i<arrtone500.size();i++) {
            Log.d("testA", "testA-500-" +arrtone500.get(i));
        }
        if(arrtone500.size()==5 || arrtone500.size()==6){
            boolean checkfirsttone=false, checksecondtone=false;
            for(int i=0;i<arrtone500.size();i++){
                if(i==1 || i==2){
                    if(arrtone500.get(i).equalsIgnoreCase("N")){
                        checkfirsttone=true;
                    }
                }
                if(i==3 || i==4){
                    if(arrtone500.get(i).equalsIgnoreCase("N")){
                        checksecondtone=true;
                    }
                }
            }
            //first and second tone any = N then run third tone
            if(arrtone500.size()==5) {
                random = new Random().nextInt((max - min) + 1) + min;
                if (random == 0) {
                    isLeft = true;
                } else {
                    isLeft = false;
                }
                if (checkfirsttone && checksecondtone) {
                    startTimerFirst500(isLeft);
                }else{
                    //proceed next level
                    type=1;
                    Log.d("testA", "testA-500next1000");
                    startTimerFirst1000(isLeft);
                }
            }else{
                if (checkfirsttone && checksecondtone) {
                    Intent intent = new Intent(StartrealtestNewActivity.this, ResultpageActivity.class);
                    intent.putExtra("left", "Refer");
                    intent.putExtra("right", "Refer");
                    intent.putExtra("result", "Refer");
                    intent.putExtra("type", "500");
                    startActivity(intent);
                    finish();
                }
            }
        }else{
            random = new Random().nextInt((max - min) + 1) + min;
            if(random==0){
                isLeft=true;
            }else{
                isLeft=false;
            }
            startTimerFirst500(isLeft);
        }
    }

    private void startTimerFirst500(boolean flag) {
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacksAndMessages(null);
                ivHit.setBackground(getResources().getDrawable(R.drawable.ongoing));
                countall500++;
                checkPass=false;
//                tvFrequency.setText(String.valueOf(500));
                if(playTone==null){
                    playTone = new PlayTone(StartrealtestNewActivity.this);
                }
                playTone.play(flag, 500, 0.02, 3000);

                cTimer = new CountDownTimer(5000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        tvTime.setText("00:0"+String.valueOf(millisUntilFinished/1000));
                    }
                    public void onFinish() {
                        playTone=null;
                        cancelTimer();
                        if(!checkPass){
                            ivHit.setBackground(getResources().getDrawable(R.drawable.tryagain));
                            arrtone500.add("N");
                        }
                        checkLast500();
                    }
                };
                cTimer.start();
            }
        }, 3000);
    }

    private void checkLast1000(){
        for(int i=0;i<arrtone1000.size();i++) {
            Log.d("testA", "testA-1000-" +arrtone1000.get(i));
        }
        if(arrtone1000.size()==5 || arrtone1000.size()==6){
            boolean checkfirsttone=false, checksecondtone=false;
            for(int i=0;i<arrtone1000.size();i++){
                if(i==1 || i==2){
                    if(arrtone1000.get(i).equalsIgnoreCase("N")){
                        checkfirsttone=true;
                    }
                }
                if(i==3 || i==4){
                    if(arrtone1000.get(i).equalsIgnoreCase("N")){
                        checksecondtone=true;
                    }
                }
            }
            //first and second tone any = N then run third tone
            if(arrtone1000.size()==5) {
                random = new Random().nextInt((max - min) + 1) + min;
                if (random == 0) {
                    isLeft = true;
                } else {
                    isLeft = false;
                }
                if (checkfirsttone && checksecondtone) {
                    startTimerFirst1000(isLeft);
                }else{
                    //proceed next level
                    type=2;
                    Log.d("testA", "testA-500next2000");
                    startTimerFirst2000(isLeft);
                }
            }else{
                if (checkfirsttone && checksecondtone) {
                    Intent intent = new Intent(StartrealtestNewActivity.this, ResultpageActivity.class);
                    intent.putExtra("left", "Refer");
                    intent.putExtra("right", "Refer");
                    intent.putExtra("result", "Refer");
                    intent.putExtra("type", "1000");
                    startActivity(intent);
                    finish();
                }
            }
        }else{
            random = new Random().nextInt((max - min) + 1) + min;
            if(random==0){
                isLeft=true;
            }else{
                isLeft=false;
            }
            startTimerFirst1000(isLeft);
        }
    }

    private void startTimerFirst1000(boolean flag) {
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacksAndMessages(null);
                ivHit.setBackground(getResources().getDrawable(R.drawable.ongoing));
                countall1000++;
                checkPass=false;
//                tvFrequency.setText(String.valueOf(1000));
                if(playTone==null){
                    playTone = new PlayTone(StartrealtestNewActivity.this);
                }
                playTone.play(flag, 1000, 0.02, 3000);

                cTimer = new CountDownTimer(5000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        tvTime.setText("00:0"+String.valueOf(millisUntilFinished/1000));
                    }
                    public void onFinish() {
                        playTone=null;
                        cancelTimer();
                        if(!checkPass){
                            ivHit.setBackground(getResources().getDrawable(R.drawable.tryagain));
                            arrtone1000.add("N");
                        }
                        checkLast1000();
                    }
                };
                cTimer.start();
            }
        }, 3000);
    }

    private void checkLast2000(){
        if(arrtone2000.size()==5 || arrtone2000.size()==6){
            boolean checkfirsttone=false, checksecondtone=false;
            for(int i=0;i<arrtone2000.size();i++){
                if(i==1 || i==2){
                    if(arrtone2000.get(i).equalsIgnoreCase("N")){
                        checkfirsttone=true;
                    }
                }
                if(i==3 || i==4){
                    if(arrtone2000.get(i).equalsIgnoreCase("N")){
                        checksecondtone=true;
                    }
                }
            }
            //first and second tone any = N then run third tone
            if(arrtone2000.size()==5) {
                random = new Random().nextInt((max - min) + 1) + min;
                if (random == 0) {
                    isLeft = true;
                } else {
                    isLeft = false;
                }
                if (checkfirsttone && checksecondtone) {
                    startTimerFirst2000(isLeft);
                }else{
                    type=3;
                    //proceed next level
                    startTimerFirst4000(isLeft);
                }
            }else{
                if (checkfirsttone && checksecondtone) {
                    Intent intent = new Intent(StartrealtestNewActivity.this, ResultpageActivity.class);
                    intent.putExtra("left", "Refer");
                    intent.putExtra("right", "Refer");
                    intent.putExtra("result", "Refer");
                    intent.putExtra("type", "2000");
                    startActivity(intent);
                    finish();
                }
            }
        }else{
            random = new Random().nextInt((max - min) + 1) + min;
            if(random==0){
                isLeft=true;
            }else{
                isLeft=false;
            }
            startTimerFirst2000(isLeft);
        }
    }

    private void startTimerFirst2000(boolean flag) {
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacksAndMessages(null);
                ivHit.setBackground(getResources().getDrawable(R.drawable.ongoing));
                countall2000++;
                checkPass=false;
//                tvFrequency.setText(String.valueOf(2000));
                if(playTone==null){
                    playTone = new PlayTone(StartrealtestNewActivity.this);
                }
                playTone.play(flag, 2000, 0.02, 3000);

                cTimer = new CountDownTimer(5000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        tvTime.setText("00:0"+String.valueOf(millisUntilFinished/1000));
                    }
                    public void onFinish() {
                        playTone=null;
                        cancelTimer();
                        if(!checkPass){
                            ivHit.setBackground(getResources().getDrawable(R.drawable.tryagain));
                            arrtone2000.add("N");
                        }
                        checkLast2000();
                    }
                };
                cTimer.start();
            }
        }, 3000);
    }

    private void checkLast4000(){
        if(arrtone4000.size()==5 || arrtone4000.size()==6){
            boolean checkfirsttone=false, checksecondtone=false;
            for(int i=0;i<arrtone4000.size();i++){
                if(i==1 || i==2){
                    if(arrtone4000.get(i).equalsIgnoreCase("N")){
                        checkfirsttone=true;
                    }
                }
                if(i==3 || i==4){
                    if(arrtone4000.get(i).equalsIgnoreCase("N")){
                        checksecondtone=true;
                    }
                }
            }
            //first and second tone any = N then run third tone
            if(arrtone4000.size()==5) {
                if (checkfirsttone && checksecondtone) {
                    random = new Random().nextInt((max - min) + 1) + min;
                    if (random == 0) {
                        isLeft = true;
                    } else {
                        isLeft = false;
                    }
                    startTimerFirst4000(isLeft);
                }else{
                    //proceed next level
                    Intent intent = new Intent(StartrealtestNewActivity.this, ResultpageActivity.class);
                    intent.putExtra("left", "Pass");
                    intent.putExtra("right", "Pass");
                    intent.putExtra("result", "Pass}");
                    intent.putExtra("type", "0");
                    startActivity(intent);
                    finish();
                }
            }else{
                if (checkfirsttone && checksecondtone) {
                    Intent intent = new Intent(StartrealtestNewActivity.this, ResultpageActivity.class);
                    intent.putExtra("left", "Refer");
                    intent.putExtra("right", "Refer");
                    intent.putExtra("result", "Refer");
                    intent.putExtra("type", "4000");
                    startActivity(intent);
                    finish();
                }
            }
        }else{
            random = new Random().nextInt((max - min) + 1) + min;
            if(random==0){
                isLeft=true;
            }else{
                isLeft=false;
            }
            startTimerFirst4000(isLeft);
        }
    }

    private void startTimerFirst4000(boolean flag) {
        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacksAndMessages(null);
                ivHit.setBackground(getResources().getDrawable(R.drawable.ongoing));
                countall4000++;
                checkPass=false;
//                tvFrequency.setText(String.valueOf(4000));
                if(playTone==null){
                    playTone = new PlayTone(StartrealtestNewActivity.this);
                }
                playTone.play(flag, 4000, 0.02, 3000);

                cTimer = new CountDownTimer(5000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        tvTime.setText("00:0"+String.valueOf(millisUntilFinished/1000));
                    }
                    public void onFinish() {
                        playTone=null;
                        cancelTimer();
                        if(!checkPass){
                            ivHit.setBackground(getResources().getDrawable(R.drawable.tryagain));
                            arrtone4000.add("N");
                        }
                        checkLast4000();
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