package com.manage.iahead;

import android.content.Intent;
import android.media.MediaRecorder;
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
import com.manage.iahead.Utility.World;

import java.text.DecimalFormat;

public class StartrealtestActivity extends AppCompatActivity implements PlayToneListener {
    private LinearLayout llNext;
    private CountDownTimer cTimer = null;
    private ImageView ivHit;
    private int count=0, random, countall=0;
    private boolean checkPass=false;
    private PlayTone playTone;
    private TextView tvText, tvTime, tv_next;
    private String levelType="level1left";
    private String leftStr="Refer", rightStr="Refer";
    private Handler handler;
    TextView mStatusView;
    Thread runner;
    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;
    float volume = 10000;
    private MediaRecorder mRecorder;

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

        updateTv();
        playTone = new PlayTone(this, StartrealtestActivity.this);

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
                cancelTimer();
                ivHit.setBackground(getResources().getDrawable(R.drawable.yay));
                playTone.stop();
                checkPass=true;
                if(count==2){
                    if(levelType.equalsIgnoreCase("level1left")) {
                        tvTime.setText("");
                        levelType="level2left";
                        count = 0;
                        countall = 0;
                    }else if(levelType.equalsIgnoreCase("level2left")){
                        leftStr="Pass";
                        Intent intent = new Intent(StartrealtestActivity.this, ResultpageActivity.class);
                        intent.putExtra("left", leftStr);
                        intent.putExtra("right", rightStr);
                        DecimalFormat df1 = new DecimalFormat("####.0");
                        intent.putExtra("noisy", df1.format(World.dbCount) + " dB");
                        startActivity(intent);
                        finish();
                        return;
                    }else if(levelType.equalsIgnoreCase("level1right")){
                        rightStr="Pass";
                        count = 0;
                        countall = 0;
                        startTimerlevel1Right();
                        return;
                    }
                    startTimerLevel2();
                    return;
                }
                count++;
                countall++;
                if(levelType.equalsIgnoreCase("level1left")) {
                    startTimer();
                }else{
                    startTimerLevel2();
                }
            }
        });
        startTimer();
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
                playTone.play(true, 1000, 0.02, 3000);

                cTimer = new CountDownTimer(5000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        tvTime.setText("00:0"+String.valueOf(millisUntilFinished/1000));
                    }
                    public void onFinish() {
                        cancelTimer();
                        if(countall>4){
                            Toast.makeText(StartrealtestActivity.this, "Fail, out of 3/5 must pass", Toast.LENGTH_SHORT).show();
//                            tvTime.setText("");
//                            llNext.setVisibility(View.VISIBLE);
//                            tv_next.setText("Finish");
                            count=0;
                            countall=0;
                            startTimerlevel1Right();
                            levelType="level1right";
                            return;
                        }
                        if(!checkPass){
                            ivHit.setBackground(getResources().getDrawable(R.drawable.tryagain));
                            startTimer();
                        }
                    }
                };
                cTimer.start();
            }
        }, 3000);
    }

    private void startTimerLevel2() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacksAndMessages(null);
                ivHit.setBackground(getResources().getDrawable(R.drawable.ongoing));
                countall++;
                checkPass=false;
                playTone.play(true, 2000, 0.02, 3000);

                cTimer = new CountDownTimer(5000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        tvTime.setText("00:0"+String.valueOf(millisUntilFinished/1000));
                    }
                    public void onFinish() {
                        cancelTimer();
                        if(countall>4){
                            Toast.makeText(StartrealtestActivity.this, "Fail, out of 3/5 must pass", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(StartrealtestActivity.this, ResultpageActivity.class);
                            intent.putExtra("left", leftStr);
                            intent.putExtra("right", rightStr);
                            startActivity(intent);
                            finish();
                            return;
                        }
                        if(!checkPass){
                            ivHit.setBackground(getResources().getDrawable(R.drawable.tryagain));
                            startTimer();
                        }
                    }
                };
                cTimer.start();
            }
        }, 3000);
    }

    private void startTimerlevel1Right() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacksAndMessages(null);
                ivHit.setBackground(getResources().getDrawable(R.drawable.ongoing));
                countall++;
                checkPass=false;
                playTone.play(false, 1000, 0.02, 3000);

                cTimer = new CountDownTimer(5000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        tvTime.setText("00:0"+String.valueOf(millisUntilFinished/1000));
                    }
                    public void onFinish() {
                        cancelTimer();
                        if(countall>4){
                            Toast.makeText(StartrealtestActivity.this, "Fail, out of 3/5 must pass", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(StartrealtestActivity.this, ResultpageActivity.class);
                            intent.putExtra("left", leftStr);
                            intent.putExtra("right", rightStr);
                            startActivity(intent);
                            finish();
                            return;
                        }
                        if(!checkPass){
                            ivHit.setBackground(getResources().getDrawable(R.drawable.tryagain));
                            startTimer();
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

    private void updateTv(){
        volume = mRecorder.getMaxAmplitude();  //Get the sound pressure value
        if(volume > 0 && volume < 1000000) {
            World.setDbCount(20 * (float)(Math.log10(volume)), mStatusView);  //Change the sound pressure value to the decibel value
        }

        DecimalFormat df1 = new DecimalFormat("####.0");

        //mStatusView.setText(df1.format(World.dbCount) + " dB");
    }
    private double soundDb(double ampl){
        return  20 * Math.log10(getAmplitudeEMA() / ampl);
    }
    private double getAmplitude() {
        if (mRecorder != null)
            return  (mRecorder.getMaxAmplitude());
        else
            return 0;

    }
    private double getAmplitudeEMA() {
        double amp =  getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }
}