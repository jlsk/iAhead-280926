package com.manage.iahead;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.manage.iahead.Utility.AppLogger;
import com.manage.iahead.Utility.SharedPref;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;

public class LogsActivity extends AppCompatActivity {
    private Intent intent;
    private TextView tvLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        tvLogs = findViewById(R.id.tvLogs);
        intent = getIntent();
        String logs = intent.getStringExtra("logs");
        tvLogs.setSingleLine(false);

        SharedPref.init(this);
        String type = SharedPref.read(SharedPref.TYPE, "");
        String text = "";
        if(type.equalsIgnoreCase("child")){
            String date = SharedPref.read(SharedPref.DATETIME, "");
            String name = SharedPref.read(SharedPref.NAME, "");
            text+="I'm a Child\n";
            text+="Date-"+date+"\n";
            text+="Name-"+name+"\n";
        }else if(type.equalsIgnoreCase("parent")){
            String phone = SharedPref.read(SharedPref.PHONE, "");
            String email = SharedPref.read(SharedPref.EMAIL, "");
            text+="I'm a Parent/Teacher\n";
            text+="Phone-"+phone+"\n";
            text+="Email-"+email+"\n";
        }else{
            String phone = SharedPref.read(SharedPref.PHONE, "");
            String email = SharedPref.read(SharedPref.EMAIL, "");
            text+="I'm a Professional\n";
            text+="Phone-"+phone+"\n";
            text+="Email-"+email+"\n";
        }

        tvLogs.setText(text+logs);
        registerForContextMenu(tvLogs);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        menu.add(0, v.getId(),0, "Copy");
        menu.setHeaderTitle("Copy text"); //setting header title for menu
        TextView textView = (TextView) v; //calling our textView
        ClipboardManager manager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", textView.getText());
        manager.setPrimaryClip(clipData);
    }
}