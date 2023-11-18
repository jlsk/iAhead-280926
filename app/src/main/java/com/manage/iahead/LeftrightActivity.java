package com.manage.iahead;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.manage.iahead.Jsyn.PlayTone;
import com.manage.iahead.Jsyn.PlayToneListener;
import com.manage.iahead.Utility.SharedPref;

import java.util.Random;

public class LeftrightActivity extends AppCompatActivity implements PlayToneListener {
    private ImageView ivClose;
    private TextView tv_proceed, tv_repeat;
    private CountDownTimer cTimer = null;
    private PlayTone playTone;
    private boolean isLeft=false, leftclick=false, rightclick=false;
    private LinearLayout ll_left, ll_right, ll_proceed;
    private int type=-1, count=0;
    private boolean checkLeft=false;
    private Toast myToast;
    private int dpInt=0;
    private int countTone=0;
    private Handler handler;
    private int countBack=0;
    private CountDownTimer cTimerBack = null;
    private int random, randomInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leftright);

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

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        dpInt = (int)(Math.round(dpWidth));

        tv_repeat = findViewById(R.id.tv_repeat);
        tv_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeftrightActivity.this, LeftrightActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ll_proceed = findViewById(R.id.ll_proceed);
        ll_left = findViewById(R.id.ll_left);
        leftMargin();
        ll_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playTone==null){
                    return;
                }
                playTone.stop();
                playTone=null;
                if(checkLeft) {
                    if(myToast!=null){
                        myToast.cancel();
                    }
                    Log.d("test","test-"+String.valueOf(countTone));
                    if(countTone>4) {
                        ll_proceed.setVisibility(View.VISIBLE);
                    }
                    leftclick = true;
                    leftMargin();
                }
            }
        });

        ll_right = findViewById(R.id.ll_right);
        rightMargin();
        ll_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playTone==null){
                    return;
                }
                playTone.stop();
                playTone=null;
                if(myToast!=null){
                    myToast.cancel();
                }

                rightclick=true;
                rightMargin();
            }
        });
        tv_proceed = findViewById(R.id.tv_proceed);
        ivClose = findViewById(R.id.ivClose);
        playTone = new PlayTone(this, LeftrightActivity.this);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivClose.setVisibility(View.GONE);

                RelativeLayout.LayoutParams paramsRight;
                if(dpInt<350){
                    paramsRight = new RelativeLayout.LayoutParams(
                            200,
                            600
                    );
                }else {
                    paramsRight = new RelativeLayout.LayoutParams(
                            300,
                            900
                    );
                }
                paramsRight.setMargins(30, 0, 0, 0);
                paramsRight.addRule(RelativeLayout.CENTER_VERTICAL);
                paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                ll_right.setLayoutParams(paramsRight);

                myToast = Toast.makeText(LeftrightActivity.this, "Now presenting a tone on the RIGHT ear.", Toast.LENGTH_SHORT);
                myToast.show();
                if(playTone==null){
                    playTone = new PlayTone(LeftrightActivity.this, LeftrightActivity.this);
                }
                playTone.play(isLeft, 1000, 0.02, 2000);

                startTimer(0);
            }
        });
        tv_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimer();
                Intent intent = new Intent(LeftrightActivity.this, SelectnoisyquietActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void leftMargin(){
        RelativeLayout.LayoutParams params;
        if(dpInt<350){
            params = new RelativeLayout.LayoutParams(
                    200,
                    450
            );
        }else {
            params = new RelativeLayout.LayoutParams(
                    300,
                    750
            );
        }
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.setMargins(0, 0, 60, 0);
        ll_left.setLayoutParams(params);
    }

    private void rightMargin(){
        RelativeLayout.LayoutParams paramsRight;
        if(dpInt<350){
            paramsRight = new RelativeLayout.LayoutParams(
                    200,
                    450
            );
        }else {
            paramsRight = new RelativeLayout.LayoutParams(
                    300,
                    750
            );
        }
        paramsRight.setMargins(60, 0, 0, 0);
        paramsRight.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ll_right.setLayoutParams(paramsRight);
    }

    private void startTimer(int type){
//                random = new Random().nextInt((6 - 3) + 1) + 3;
//                random=random*1000;
                cTimer = new CountDownTimer(getInternal(), 1000) {
                    public void onTick(long millisUntilFinished) {
                    }
                    public void onFinish() {
                        if(playTone!=null) {
                            playTone.stop();
                        }
                        playTone=null;
                        handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                handler.removeCallbacksAndMessages(null);

                                cancelTimer();
                                if(type==0) {
                                    if (!rightclick) {
                                        myToast = Toast.makeText(LeftrightActivity.this, "you have no click RIGHT side", Toast.LENGTH_SHORT);
                                        myToast.show();

                                        rightMargin();
                                    }

                                    if(playTone==null){
                                        playTone = new PlayTone(LeftrightActivity.this, LeftrightActivity.this);
                                    }
                                    checkLeft=true;
                                    countTone++;
                                    Log.d("test", "test-"+String.valueOf(countTone));
                                    if(countTone>2) {
                                        playTone.play(true, 1000, 0.02, randomInt);

                                        RelativeLayout.LayoutParams params;
                                        if(dpInt<350){
                                            params = new RelativeLayout.LayoutParams(
                                                    200,
                                                    600
                                            );
                                        }else {
                                            params = new RelativeLayout.LayoutParams(
                                                    300,
                                                    900
                                            );
                                        }
                                        params.setMargins(0, 0, 30, 0);
                                        params.addRule(RelativeLayout.CENTER_VERTICAL);
                                        ll_left.setLayoutParams(params);

                                        myToast = Toast.makeText(LeftrightActivity.this, "Now presenting a tone on the LEFT ear.", Toast.LENGTH_SHORT);
                                        myToast.show();
                                        startTimer(1);
                                    }else{
                                        //right
                                        playTone.play(false, 1000, 0.02, randomInt);

                                        RelativeLayout.LayoutParams paramsRight;
                                        if(dpInt<350){
                                            paramsRight = new RelativeLayout.LayoutParams(
                                                    200,
                                                    600
                                            );
                                        }else {
                                            paramsRight = new RelativeLayout.LayoutParams(
                                                    300,
                                                    900
                                            );
                                        }
                                        paramsRight.setMargins(30, 0, 0, 0);
                                        paramsRight.addRule(RelativeLayout.CENTER_VERTICAL);
                                        paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                                        ll_right.setLayoutParams(paramsRight);

                                        startTimer(0);
                                    }
                                }else if(type==1){
                                    if(!leftclick) {
                                        myToast = Toast.makeText(LeftrightActivity.this, "you have no click LEFT side", Toast.LENGTH_SHORT);
                                        myToast.show();
                                        leftMargin();
                                    }

                                        countTone++;
                                        if(countTone<6) {
                                            if(playTone==null){
                                                playTone = new PlayTone(LeftrightActivity.this, LeftrightActivity.this);
                                            }
                                            playTone.play(true, 1000, 0.02, randomInt);

                                            RelativeLayout.LayoutParams params;
                                            if(dpInt<350){
                                                params = new RelativeLayout.LayoutParams(
                                                        200,
                                                        600
                                                );
                                            }else {
                                                params = new RelativeLayout.LayoutParams(
                                                        300,
                                                        900
                                                );
                                            }
                                            params.setMargins(0, 0, 30, 0);
                                            params.addRule(RelativeLayout.CENTER_VERTICAL);
                                            ll_left.setLayoutParams(params);

                                            startTimer(1);
                                        }else{
                                            ll_proceed.setVisibility(View.VISIBLE);
                                        }
                                }
                            }
                        }, 2000);
                    }
                };

                cTimer.start();
    }

    private void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }

    @Override
    public void onPlay() {

    }

    @Override
    public void onSleep() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelTimer();
        cancelTimerBack();
        if(handler!=null){
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onBackPressed() {

        if(countBack==0) {
            cTimerBack = new CountDownTimer(3000, 1000) {
                public void onTick(long millisUntilFinished) {
                }
                public void onFinish() {
                    countBack=0;
                }
            };
            cTimerBack.start();
            Toast.makeText(LeftrightActivity.this, "Press back again to quit the app", Toast.LENGTH_SHORT).show();
            countBack = 1;
        }else{
            cancelTimerBack();
            finishAndRemoveTask();
        }

    }

    private void cancelTimerBack(){
        if(cTimerBack!=null) {
            cTimerBack.cancel();
        }
    }

    private int getInternal(){
        randomInt = new Random().nextInt((6 - 3) + 1) + 3;
        randomInt=randomInt*1000;
        return randomInt;
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