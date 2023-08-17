package com.manage.iahead;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
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

public class LeftrightActivity extends AppCompatActivity implements PlayToneListener {
    private ImageView ivClose;
    private TextView tv_proceed;
    private CountDownTimer cTimer = null;
    private PlayTone playTone;
    private boolean isLeft=false, leftclick=false, rightclick=false;
    private LinearLayout ll_left, ll_right;
    private int type=-1, count=0;
    private boolean checkLeft=false;
    private Toast myToast;
    private int dpInt=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leftright);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        dpInt = (int)(Math.round(dpWidth));

        ll_left = findViewById(R.id.ll_left);
        leftMargin();
        ll_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTone.stop();
                playTone=null;
                if(checkLeft) {
                    if(myToast!=null){
                        myToast.cancel();
                    }

                    tv_proceed.setVisibility(View.VISIBLE);
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
        playTone = new PlayTone(this);
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
                    playTone = new PlayTone(LeftrightActivity.this);
                }
                playTone.play(isLeft, 1000, 0.02, 3000);
                startTimer(0);
            }
        });
        tv_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimer();
                Intent intent = new Intent(LeftrightActivity.this, ReadytestActivity.class);
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
        cTimer = new CountDownTimer(4000, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                playTone=null;
                cancelTimer();
                if(type==0) {
                    if (!rightclick) {
                        myToast = Toast.makeText(LeftrightActivity.this, "you have no click RIGHT side", Toast.LENGTH_SHORT);
                        myToast.show();

                        rightMargin();
                    }
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
                    if(playTone==null){
                        playTone = new PlayTone(LeftrightActivity.this);
                    }
                    playTone.play(true, 1000, 0.02, 3000);
                    checkLeft=true;
                    startTimer(1);
                }else if(type==1){
                    if(!leftclick){
                        tv_proceed.setVisibility(View.VISIBLE);
                        myToast = Toast.makeText(LeftrightActivity.this, "you have no click LEFT side", Toast.LENGTH_SHORT);
                        myToast.show();

                        leftMargin();
                    }
                }
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
}