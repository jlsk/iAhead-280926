package com.manage.iahead;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.manage.iahead.Service.NoiseLevelService;
import com.manage.iahead.Utility.AppLogger;
import com.manage.iahead.Utility.SharedPref;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;

public class MainActivity extends AppCompatActivity {
    private ImageView iv_child, iv_parent, iv_therapist;
    private Intent intent;
    private LinearLayout view_hidden, ll_calibration;
    private String DEBUGMODESTR="";
    private int countBack=0;
    private CountDownTimer cTimerBack = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loggingCheck();
        SharedPref.init(this);

//        File dir = getExternalFilesDir(null);
//        if (dir != null) {
//            Log.d("FileOperation", "External Files Directory: " + dir.getAbsolutePath());
//        } else {
//            Log.e("FileOperation", "External Files Directory is null");
//        }

        ll_calibration = findViewById(R.id.ll_calibration);
        ll_calibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CalibrationActivity.class);
                startActivity(intent);
            }
        });
        view_hidden = findViewById(R.id.view_hidden);
//        view_hidden.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                DEBUGMODESTR = SharedPref.read(SharedPref.DEBUGMODE, "");
//                if(DEBUGMODESTR.equalsIgnoreCase("")){
//                    SharedPref.write(SharedPref.DEBUGMODE, "Y");
//                    ll_calibration.setVisibility(View.VISIBLE);
//                    Toast.makeText(MainActivity.this, "Debug Mode Activated", Toast.LENGTH_SHORT).show();
//                    permissionstorage();
//                }else{
//                    SharedPref.write(SharedPref.DEBUGMODE, "");
//                    Toast.makeText(MainActivity.this, "Debug Mode Deactivated", Toast.LENGTH_SHORT).show();
//                }
//                return false;
//            }
//        });

        intent=new Intent(this, EnteridActivity.class);
        iv_child = findViewById(R.id.iv_child);
        iv_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type", "child");
                SharedPref.write(SharedPref.TYPE, "child");
                startActivity(intent);
            }
        });
        iv_parent = findViewById(R.id.iv_parent);
        iv_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent.putExtra("type", "parent");
                SharedPref.write(SharedPref.TYPE, "parent");
                startActivity(intent);
            }
        });
        iv_therapist = findViewById(R.id.iv_therapist);
        iv_therapist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                intent.putExtra("type", "therapist");
//                SharedPref.write(SharedPref.TYPE, "therapist");
//                startActivity(intent);
                Intent intent = new Intent(MainActivity.this, CalibrationActivity.class);
                startActivity(intent);
            }
        });

//        if(DEBUGMODESTR.equalsIgnoreCase("")){
//            SharedPref.write(SharedPref.DEBUGMODE, "Y");
//            ll_calibration.setVisibility(View.VISIBLE);
//        }
    }

    private void permissionstorage(){
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 2000: // Allowed was selected so Permission granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                break;
        }};

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
            Toast.makeText(MainActivity.this, "Press back again to quit the app", Toast.LENGTH_SHORT).show();
            countBack = 1;
        }else{
            cancelTimerBack();
            finishAndRemoveTask();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelTimerBack();
    }

    private void cancelTimerBack(){
        if(cTimerBack!=null) {
            cTimerBack.cancel();
        }
    }

    private void loggingCheck(){
        try {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            loggerContext.reset();
            ContextInitializer ci = new ContextInitializer(loggerContext);
            ci.autoConfig();
            AppLogger.i("test","test");
        } catch (JoranException ex) {
            Log.d("error","error");
        }
    }
}