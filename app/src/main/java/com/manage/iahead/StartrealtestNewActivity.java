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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.manage.iahead.Jsyn.PlayTone;
import com.manage.iahead.Jsyn.PlayToneListener;
import com.manage.iahead.Service.NoiseLevelService;
import com.manage.iahead.Utility.AppLogger;
import com.manage.iahead.Utility.SharedPref;

import java.util.ArrayList;
import java.util.Random;

public class StartrealtestNewActivity extends AppCompatActivity implements PlayToneListener {
    private LinearLayout llNext;
    private CountDownTimer cTimer = null;
    private ImageView ivHit;
    private int count=0, random, countall500=0, countall1000=0, countall2000=0, countall4000=0;
    private boolean checkPass=false;
    private PlayTone playTone;
    private TextView tvText, tvTime, tv_next, tvFrequency, tvDisplay, tvDb;
    private String levelType="level1left";
    private String leftStr="Refer", rightStr="Refer";
    private Handler handler;
    private boolean isLeft=false;
    private ArrayList<String>arrtone500=new ArrayList<>();
    private ArrayList<String>arrtone1000=new ArrayList<>();
    private ArrayList<String>arrtone2000=new ArrayList<>();
    private ArrayList<String>arrtone4000=new ArrayList<>();
    private int type=0, countSeq=0;
    final int min = 0;
    final int max = 1;
    private String result="";
    private Handler handler1;
    private boolean check500press1=false, check500press2=false, check500press3=false,
            check1000press1=false, check1000press2=false, check1000press3=false, check2000press1=false, check2000press2=false,
            check2000press3=false, check4000press1=false, check4000press2=false, check4000press3=false, check500Leftpress1=false,
            check500Leftpress2=false, check500Leftpress3=false,
            check1000Leftpress1=false, check1000Leftpress2=false, check1000Leftpress3=false, check2000Leftpress1=false,
            check2000Leftpress2=false,
            check2000Leftpress3=false, check4000Leftpress1=false, check4000Leftpress2=false, check4000Leftpress3=false, ableClick=true;
    private int countBack=0;
    private CountDownTimer cTimerBack = null;
    private int randomInt;
    private ArrayList<String>arr_noisy=new ArrayList<>();
    private Intent serviceIntent;
    private String TAG="QuietSelection";
    private String appendStr="", screeninglevel, Left500db="", Left1000db="", Left2000db="", Left4000db="",
            Right500db="", Right1000db="", Right2000db="", Right4000db="";

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(NoiseLevelService.ACTION_DATA_UPDATED)) {
                String data = intent.getStringExtra(NoiseLevelService.EXTRA_DATA);
                String frequency = intent.getStringExtra(NoiseLevelService.EXTRA_DATA1);
//                if(data.equalsIgnoreCase("Noisy")){
//                    arr_noisy.add(frequency);
//                }
                tvDb.setText(frequency);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the BroadcastReceiver when the activity is destroyed
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
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
        if(handler1!=null){
            handler1.removeCallbacksAndMessages(null);
        }
        handler.removeCallbacksAndMessages(null);
        cancelTimer();
        cancelTimerBack();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starttest);

        tvDb = findViewById(R.id.tvDb);
        tvDisplay = findViewById(R.id.tvDisplay);

        String DEBUGMODESTR = SharedPref.read(SharedPref.DEBUGMODE, "");
        if(DEBUGMODESTR.equalsIgnoreCase("Y")){
            AppLogger.isDebug=true;
        }else{
            tvDb.setVisibility(View.GONE);
            tvDisplay.setVisibility(View.GONE);
        }
//        Intent serviceIntent = new Intent(this, NoiseLevelService.class);
//        startService(serviceIntent);
//
//        IntentFilter filter = new IntentFilter(NoiseLevelService.ACTION_DATA_UPDATED);
//        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);

        AudioManager am =
                (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        SharedPref.init(this);
        screeninglevel = SharedPref.read(SharedPref.SCREENINGLEVEL, "");
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

        playTone = new PlayTone(this, StartrealtestNewActivity.this);

//        String DEBUGMODESTR = SharedPref.read(SharedPref.DEBUGMODE, "");

        tvFrequency = findViewById(R.id.tvFrequency);
//        tvFrequency.setText("Frequency-"+String.valueOf(500));
//        if(DEBUGMODESTR.equalsIgnoreCase("")){
            tvFrequency.setVisibility(View.INVISIBLE);
//        }
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
                if (tvFrequency.getText().toString().length() > 0) {
                    AppLogger.i(TAG, "PRESS-" + tvFrequency.getText().toString() + ", 0.02amp, " + isLeft + ", DB-" + tvDb.getText().toString());
                    appendStr += "\n " + TAG + " PRESS-" + tvFrequency.getText().toString() + ", 0.02amp, " + isLeft + ", DB-" + tvDb.getText().toString() + ", " + checkStatus(tvFrequency.getText().toString(), returnMax(), isLeft);

                    Log.d("test", "testpress-isleft-" + String.valueOf(isLeft));
                    Log.d("test", "testpress-type-" + String.valueOf(type));
                    if (!ableClick) {
                        return;
                    }
                    checkPass = true;
                    cancelTimer();
                    ivHit.setBackground(getResources().getDrawable(R.drawable.yay));
                    if (playTone == null) {
                        return;
                    }
                    playTone.stop();
                    playTone = null;
                    if (type == 0) {
                        if (countall500 == 1) {
                            if (!isLeft) {
                                check500press1 = true;
                            } else {
                                check500Leftpress1 = true;
                            }
                        } else if (countall500 == 2) {
                            if (!isLeft) {
                                check500press2 = true;
                            } else {
                                check500Leftpress2 = true;
                            }
                        } else if (countall500 == 3) {
                            if (!isLeft) {
                                check500press3 = true;
                            } else {
                                check500Leftpress3 = true;
                            }
                        }
                        if (!isLeft) {
                            if (check500press1 && check500press2) {
                                startTimerFirst1000(isLeft);
                            } else {
                                if (countall500 == 3) {
                                    startTimerFirst1000(isLeft);
                                } else {
                                    startTimerFirst500(isLeft);
                                }
                            }
                        } else {
                            if (check500Leftpress1 && check500Leftpress2) {
                                startTimerFirst1000(isLeft);
                            } else {
                                if (countall500 == 3) {
                                    startTimerFirst1000(isLeft);
                                } else {
                                    startTimerFirst500(isLeft);
                                }
                            }
                        }
                    } else if (type == 1) {
                        if (countall1000 == 1) {
                            if (!isLeft) {
                                check1000press1 = true;
                            } else {
                                check1000Leftpress1 = true;
                            }
                        } else if (countall1000 == 2) {
                            if (!isLeft) {
                                check1000press2 = true;
                            } else {
                                check1000Leftpress2 = true;
                            }
                        } else if (countall1000 == 3) {
                            if (!isLeft) {
                                check1000press3 = true;
                            } else {
                                check1000Leftpress3 = true;
                            }
                        }
                        if (!isLeft) {
                            if (check1000press1 && check1000press2) {
                                startTimerFirst2000(isLeft);
                            } else {
                                if (countall1000 == 3) {
                                    startTimerFirst2000(isLeft);
                                } else {
                                    startTimerFirst1000(isLeft);
                                }
                            }
                        } else {
                            if (check1000Leftpress1 && check1000Leftpress2) {
                                startTimerFirst2000(isLeft);
                            } else {
                                if (countall1000 == 3) {
                                    startTimerFirst2000(isLeft);
                                } else {
                                    startTimerFirst1000(isLeft);
                                }
                            }
                        }
                    } else if (type == 2) {
                        if (countall2000 == 1) {
                            if (!isLeft) {
                                check2000press1 = true;
                            } else {
                                check2000Leftpress1 = true;
                            }
                        } else if (countall2000 == 2) {
                            if (!isLeft) {
                                check2000press2 = true;
                            } else {
                                check2000Leftpress2 = true;
                            }
                        } else if (countall2000 == 3) {
                            if (!isLeft) {
                                check2000press3 = true;
                            } else {
                                check2000Leftpress3 = true;
                            }
                        }
                        if (!isLeft) {
                            if (check2000press1 && check2000press2) {
                                startTimerFirst4000(isLeft);
                            } else {
                                if (countall2000 == 3) {
                                    startTimerFirst4000(isLeft);
                                } else {
                                    startTimerFirst2000(isLeft);
                                }
                            }
                        } else {
                            if (check2000Leftpress1 && check2000Leftpress2) {
                                startTimerFirst4000(isLeft);
                            } else {
                                if (countall2000 == 3) {
                                    startTimerFirst4000(isLeft);
                                } else {
                                    startTimerFirst2000(isLeft);
                                }
                            }
                        }
                    } else if (type == 3) {
                        if (countall4000 == 1) {
                            if (!isLeft) {
                                check4000press1 = true;
                            } else {
                                check4000Leftpress1 = true;
                            }
                        } else if (countall4000 == 2) {
                            if (!isLeft) {
                                check4000press2 = true;
                            } else {
                                check4000Leftpress2 = true;
                            }
                        } else if (countall4000 == 3) {
                            if (!isLeft) {
                                check4000press3 = true;
                            } else {
                                check4000Leftpress3 = true;
                            }
                        }
                        if (!isLeft) {
                            if (check4000press1 && check4000press2) {
                                isLeft = true;
                                type = 0;
                                countall500 = 0;
                                countall1000 = 0;
                                countall2000 = 0;
                                countall4000 = 0;
                                startTimerFirst500(isLeft);
                            } else {
                                if (countall4000 == 3) {
                                    isLeft = true;
                                    type = 0;
                                    countall500 = 0;
                                    countall1000 = 0;
                                    countall2000 = 0;
                                    countall4000 = 0;
                                    startTimerFirst500(isLeft);
                                } else {
                                    startTimerFirst4000(isLeft);
                                }
                            }
                        } else {
                            if (check4000Leftpress1 && check4000Leftpress2) {
                                goNextPage();
                            } else {
                                if (countall4000 == 3) {
                                    goNextPage();
                                } else {
                                    startTimerFirst4000(isLeft);
                                }
                            }
                        }

                    }
                }
            }
        });
        type=0;
        startTimerFirst500(isLeft);
    }

    private int returnMax(){
        String checkmax = tvDb.getText().toString();
        String mySentence = "MAX-";
        int index = checkmax.indexOf(mySentence);
        checkmax=checkmax.substring(index+4, checkmax.length());
        int intValue=0;
        try {
            double parsedValue = Double.parseDouble(checkmax);
            intValue = (int) parsedValue;
        } catch (NumberFormatException e) {
        }
        return intValue;
    }

    private int getInternal(){
        randomInt = new Random().nextInt((6 - 3) + 1) + 3;
        randomInt=randomInt*1000;
        return randomInt;
    }

    private void startTimerFirst500(boolean flag) {
        if(countall500==0) {
            restartNoiseService();
        }
        AppLogger.i(TAG, "Frequency 500 start");
        String maxmin;
        if(screeninglevel.equalsIgnoreCase("25")){
            maxmin="41";
        }else{
            maxmin="56";
        }
        appendStr+="\n "+TAG +" "+ "Frequency 500 start,"+screeninglevel+"db,,,Max-"+maxmin;

        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacksAndMessages(null);
                ivHit.setBackground(getResources().getDrawable(R.drawable.ongoing));
                countall500++;
                checkPass=false;
                tvFrequency.setText("Frequency-"+String.valueOf(500));
                if(playTone==null){
                    playTone = new PlayTone(StartrealtestNewActivity.this, StartrealtestNewActivity.this);
                }
                    playTone.play(flag, 500, 0.02, 2000);
                    Log.d("test","test-random-"+String.valueOf(randomInt));

                countSeq++;
                cTimer = new CountDownTimer(3000, 1000) {
                    public void onTick(long millisUntilFinished) {
                        tvTime.setText("00:0"+String.valueOf(millisUntilFinished/1000));
                        String leftright="";
                        if(flag){
                            leftright="Left";
                        }else{
                            leftright="Right";
                        }
                        tvDisplay.setText(String.valueOf(countSeq)+", 500hz, "+String.valueOf(millisUntilFinished/1000)+ "secs, 0.02amp, "+leftright);

                        if(String.valueOf(millisUntilFinished/1000).equalsIgnoreCase("0")){
                            ableClick=false;
                        }else{
                            ableClick=true;
                        }
                    }
                    public void onFinish() {
                        AppLogger.i(TAG, String.valueOf(countSeq)+", 500hz, "+String.valueOf(0)+ "secs, 0.02amp, "+isLeft+", DB-"+tvDb.getText().toString());
                        appendStr+="\n "+TAG+" "+String.valueOf(countSeq)+", 500hz, "+String.valueOf(0)+ "secs, 0.02amp, "+isLeft+", DB-"+tvDb.getText().toString()+", "+checkStatus("Frequency-500", returnMax(), isLeft);

                        Log.d("test", "testnopress500-isleft-"+String.valueOf(isLeft));
                        Log.d("test", "testnopress500-type-"+String.valueOf(type));
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
                                if(!checkPass){
                                    ivHit.setBackground(getResources().getDrawable(R.drawable.tryagain));
                                    arrtone500.add("N");
                                }

                                if(!isLeft) {
                                    if(check500press1 && check500press2){
                                        startTimerFirst1000(isLeft);
                                    }else{
                                        if(countall500==3){
                                            startTimerFirst1000(isLeft);
                                        }else {
                                            startTimerFirst500(isLeft);
                                        }
                                    }
                                }else{
                                    if(check500Leftpress1 && check500Leftpress2){
                                        startTimerFirst1000(isLeft);
                                    }else{
                                        if(countall500==3){
                                            startTimerFirst1000(isLeft);
                                        }else {
                                            startTimerFirst500(isLeft);
                                        }
                                    }
                                }
                            }
                        }, getInternal());
                    }
                };
                cTimer.start();
            }
        }, getInternal());
    }

    private void startTimerFirst1000(boolean flag) {
        if(countall1000==0) {
            restartNoiseService();
        }
        AppLogger.i(TAG, "Frequency 1000 start");
        String maxmin;
        if(screeninglevel.equalsIgnoreCase("25")){
            maxmin="46";
        }else{
            maxmin="61";
        }
        appendStr+="\n "+TAG +" "+ "Frequency 1000 start,"+screeninglevel+"db,,,Max-"+maxmin;

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
                    playTone = new PlayTone(StartrealtestNewActivity.this, StartrealtestNewActivity.this);
                }
                playTone.play(flag, 1000, 0.02, 2000);
                Log.d("test","test-random-"+String.valueOf(randomInt));

                countSeq++;
                cTimer = new CountDownTimer(3000, 1000) {
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
                        AppLogger.i(TAG, String.valueOf(countSeq)+", 1000hz, "+String.valueOf(0)+ "secs, 0.02amp, "+isLeft+", DB-"+tvDb.getText().toString());
                        appendStr+="\n "+TAG+" "+String.valueOf(countSeq)+", 1000hz, "+String.valueOf(0)+ "secs, 0.02amp, "+isLeft+", DB-"+tvDb.getText().toString()+", "+checkStatus("Frequency-1000", returnMax(), isLeft);

                        Log.d("test", "testnopress1000-isleft-"+String.valueOf(isLeft));
                        Log.d("test", "testnopress1000-type-"+String.valueOf(type));
                        if(playTone!=null){
                            playTone.stop();
                        }
                        playTone=null;

                        handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                handler.removeCallbacksAndMessages(null);
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
                        }, getInternal());
                    }
                };
                cTimer.start();
            }
        }, getInternal());
    }

    private void startTimerFirst2000(boolean flag) {
        if(countall2000==0) {
            restartNoiseService();
        }
        AppLogger.i(TAG, "Frequency 2000 start");
        String maxmin;
        if(screeninglevel.equalsIgnoreCase("25")){
            maxmin="54";
        }else{
            maxmin="69";
        }
        appendStr+="\n "+TAG +" "+ "Frequency 2000 start,"+screeninglevel+"db,,,Max-"+maxmin;

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
                    playTone = new PlayTone(StartrealtestNewActivity.this, StartrealtestNewActivity.this);
                }
                    playTone.play(flag, 3000, 0.02, 2000);
                    Log.d("test","test-random-"+String.valueOf(randomInt));

                countSeq++;
                cTimer = new CountDownTimer(3000, 1000) {
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
                        AppLogger.i(TAG, String.valueOf(countSeq)+", 2000hz, "+String.valueOf(0)+ "secs, 0.02amp, "+isLeft+", DB-"+tvDb.getText().toString());
                        appendStr+="\n "+TAG+" "+String.valueOf(countSeq)+", 2000hz, "+String.valueOf(0)+ "secs, 0.02amp, "+isLeft+", DB-"+tvDb.getText().toString()+", "+checkStatus("Frequency-2000", returnMax(), isLeft);

                        Log.d("test", "testnopress2000-isleft-"+String.valueOf(isLeft));
                        Log.d("test", "testnopress2000-type-"+String.valueOf(type));

                        if(playTone!=null){
                            playTone.stop();
                        }
                        playTone=null;

                        handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                handler.removeCallbacksAndMessages(null);
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
                        }, getInternal());
                    }
                };
                cTimer.start();
            }
        }, getInternal());
    }

    private void startTimerFirst4000(boolean flag) {
        if(countall4000==0) {
            restartNoiseService();
        }
        AppLogger.i(TAG, "Frequency 4000 start");
        String maxmin;
        if(screeninglevel.equalsIgnoreCase("25")){
            maxmin="57";
        }else{
            maxmin="72";
        }
        appendStr+="\n "+TAG +" "+ "Frequency 4000 start,"+screeninglevel+"db,,,Max-"+maxmin;
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
                    playTone = new PlayTone(StartrealtestNewActivity.this, StartrealtestNewActivity.this);
                }
                    playTone.play(flag, 4000, 0.02, 2000);
                    Log.d("test","test-random-"+String.valueOf(randomInt));

                countSeq++;
                cTimer = new CountDownTimer(3000, 1000) {
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
                        AppLogger.i(TAG, String.valueOf(countSeq)+", 4000hz, "+String.valueOf(0)+ "secs, 0.02amp, "+isLeft+", DB-"+tvDb.getText().toString());
                        appendStr+="\n "+TAG+" "+String.valueOf(countSeq)+", 4000hz, "+String.valueOf(0)+ "secs, 0.02amp, "+isLeft+", DB-"+tvDb.getText().toString()+", "+checkStatus("Frequency-4000", returnMax(), isLeft);

                        Log.d("test", "testnopress4000-isleft-"+String.valueOf(isLeft));
                        Log.d("test", "testnopress4000-type-"+String.valueOf(type));

                        if(playTone!=null){
                            playTone.stop();
                        }
                        playTone=null;

                        handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                handler.removeCallbacksAndMessages(null);
                                cancelTimer();
                                if(!checkPass){
                                    ivHit.setBackground(getResources().getDrawable(R.drawable.tryagain));
                                    arrtone4000.add("N");
                                }
                                if(!isLeft) {
                                    if(check4000press1 && check4000press2){
                                        isLeft=true;
                                        type=0;
                                        countall500=0;
                                        countall1000=0;
                                        countall2000=0;
                                        countall4000=0;
                                        startTimerFirst500(isLeft);
                                    }else{
                                        if(countall4000==3){
                                            isLeft=true;
                                            type=0;
                                            countall500=0;
                                            countall1000=0;
                                            countall2000=0;
                                            countall4000=0;
                                            startTimerFirst500(isLeft);
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
                        }, getInternal());
                    }
                };
                cTimer.start();
            }
        }, getInternal());
    }

    //cancel timer
    private void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }

    private void goNextPage(){

        Intent serviceIntent = new Intent(this, NoiseLevelService.class);
        stopService(serviceIntent);

        AppLogger.i(TAG, "---------------------------------");
        appendStr+="\n "+TAG+" ------------------------------------";

        Intent intent = new Intent(StartrealtestNewActivity.this, ResultpageActivity.class);
        String Str500 = "REFER", Str1000 = "REFER", Str2000 = "REFER", Str4000 = "REFER";
        String Str500Left = "REFER", Str1000Left = "REFER", Str2000Left = "REFER", Str4000Left = "REFER";
        if(check500press1 && check500press2 || check500press2 && check500press3
                || check500press1 && check500press3){
            Str500="PASS";
        }
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
        if(check500Leftpress1 && check500Leftpress2 || check500Leftpress2 && check500Leftpress3
                || check500Leftpress1 && check500Leftpress3){
            Str500Left="PASS";
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
        intent.putExtra("right500", Str500);
        intent.putExtra("right1000", Str1000);
        intent.putExtra("right2000", Str2000);
        intent.putExtra("right4000", Str4000);
        intent.putExtra("left500", Str500Left);
        intent.putExtra("left1000", Str1000Left);
        intent.putExtra("left2000", Str2000Left);
        intent.putExtra("left4000", Str4000Left);
        String Right500dbnew, Right1000dbnew="", Right2000dbnew="", Right4000dbnew="",
                Left500dbnew, Left1000dbnew="", Left2000dbnew="", Left4000dbnew="";
        if(Right500db.contains("Fail")){
            Right500dbnew="Fail";
        }else{
            Right500dbnew="Pass";
        }
        intent.putExtra("right500db", Right500dbnew);
        if(Right1000db.contains("Fail")){
            Right1000dbnew="Fail";
        }else{
            Right1000dbnew="Pass";
        }
        intent.putExtra("right1000db", Right1000dbnew);
        if(Right2000db.contains("Fail")){
            Right2000dbnew="Fail";
        }else{
            Right2000dbnew="Pass";
        }
        intent.putExtra("right2000db", Right2000dbnew);
        if(Right4000db.contains("Fail")){
            Right4000dbnew="Fail";
        }else{
            Right4000dbnew="Pass";
        }
        intent.putExtra("right4000db", Right4000dbnew);
        if(Left500db.contains("Fail")){
            Left500dbnew="Fail";
        }else{
            Left500dbnew="Pass";
        }
        intent.putExtra("left500db", Left500dbnew);
        if(Left1000db.contains("Fail")){
            Left1000dbnew="Fail";
        }else{
            Left1000dbnew="Pass";
        }
        intent.putExtra("left1000db", Left1000dbnew);
        if(Left2000db.contains("Fail")){
            Left2000dbnew="Fail";
        }else{
            Left2000dbnew="Pass";
        }
        intent.putExtra("left2000db", Left2000dbnew);
        if(Left4000db.contains("Fail")){
            Left4000dbnew="Fail";
        }else{
            Left4000dbnew="Pass";
        }
        intent.putExtra("left4000db", Left4000dbnew);
        intent.putExtra("logs", appendStr);
        startActivity(intent);
        finish();
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
//            Toast.makeText(StartrealtestNewActivity.this, "Press back again to quit the app", Toast.LENGTH_SHORT).show();
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

    private void restartNoiseService(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

        if(serviceIntent!=null) {
            stopService(serviceIntent);
        }
        serviceIntent = new Intent(this, NoiseLevelService.class);
        startService(serviceIntent);

        IntentFilter filter = new IntentFilter(NoiseLevelService.ACTION_DATA_UPDATED);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
    }

    private String checkStatus(String frequency, int dbnoise, boolean isLeft){
        String status="FAIL";
        String db = SharedPref.read(SharedPref.SCREENINGLEVEL, "");
        if(db.equalsIgnoreCase("25")){
            switch (frequency){
                case "Frequency-500":
                    if(dbnoise<41){
                        status="PASS";
                    }
                    break;
                case "Frequency-1000":
                    if(dbnoise<46){
                        status="PASS";
                    }
                    break;
                case "Frequency-2000":
                    if(dbnoise<54){
                        status="PASS";
                    }
                    break;
                case "Frequency-4000":
                    if(dbnoise<57){
                        status="PASS";
                    }
                    break;
            }
        }else{
            switch (frequency){
                case "Frequency-500":
                    if(dbnoise<56){
                        status="PASS";
                    }
                    break;
                case "Frequency-1000":
                    if(dbnoise<61){
                        status="PASS";
                    }
                    break;
                case "Frequency-2000":
                    if(dbnoise<69){
                        status="PASS";
                    }
                    break;
                case "Frequency-4000":
                    if(dbnoise<72){
                        status="PASS";
                    }
                    break;
            }
        }
        //check noisy level
        String newstatus="Fail";
        if(status.equalsIgnoreCase("PASS")){
            newstatus="Pass";
        }
        switch (frequency){
            case "Frequency-500":
                if(isLeft){
                    Left500db=Left500db+newstatus+"/";
                }else{
                    Right500db=Right500db+newstatus+"/";
                }
                break;
            case "Frequency-1000":
                if(isLeft){
                    Left1000db=Left1000db+newstatus+"/";
                }else{
                    Right1000db=Right1000db+newstatus+"/";
                }
                break;
            case "Frequency-2000":
                if(isLeft){
                    Left2000db=Left2000db+newstatus+"/";
                }else{
                    Right2000db=Right2000db+newstatus+"/";
                }
                break;
            case "Frequency-4000":
                if(isLeft){
                    Left4000db=Left4000db+newstatus+"/";
                }else{
                    Right4000db=Right4000db+newstatus+"/";
                }
                break;
        }
        return status;
    }

}