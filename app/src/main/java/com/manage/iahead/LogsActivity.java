package com.manage.iahead;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import androidx.core.content.FileProvider;

import com.manage.iahead.Utility.AppLogger;
import com.manage.iahead.Utility.SharedPref;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;

public class LogsActivity extends AppCompatActivity {
    private Intent intent;
    private TextView tvLogs;
    private LinearLayout ll_email, ll_whatsapp, ll_locally;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        tvLogs = findViewById(R.id.tvLogs);
        ll_email = findViewById(R.id.ll_email);
        ll_whatsapp = findViewById(R.id.ll_whatsapp);
        ll_locally = findViewById(R.id.ll_locally);
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

        ll_locally.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTextViewToFile(tvLogs, LogsActivity.this, 0);
            }
        });
        ll_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTextViewToFile(tvLogs, LogsActivity.this, 1);
            }
        });
        ll_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTextViewToFile(tvLogs, LogsActivity.this, 2);
            }
        });
    }

    private void saveTextViewToFile(TextView textView, Context context, int check) {
        String text = textView.getText().toString();
        FileOutputStream fos = null;

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        File file=null;
        try {
            file = new File(context.getExternalFilesDir(null), "logs_"+dateFormat+".txt");
            fos = new FileOutputStream(file);
            fos.write(text.getBytes());
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if(check==1){
            sendEmailWithAttachment(context, file);
        }else if(check==2){
            sendFileViaWhatsApp(context, file);
        }else{
            Toast.makeText(context, "Save successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendFileViaWhatsApp(Context context, File file) {
        // File you want to send
//        File file = new File(context.getExternalFilesDir(null), "myfile.txt");

        // Ensure the file exists
        if (!file.exists()) {
            return;
        }

        // Get URI for the file
        Uri uri = FileProvider.getUriForFile(context, "com.manage.iahead.fileprovider", file);

        // Create the share intent
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Here's a file for you");
        shareIntent.setPackage("com.whatsapp"); // Specify WhatsApp package to open WhatsApp directly

        // Grant read URI permission
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        // Start the intent
        context.startActivity(Intent.createChooser(shareIntent, "Share file via"));
    }

    private void sendEmailWithAttachment(Context context, File file) {
//        File file = new File(context.getExternalFilesDir(null), "myfile.txt");

        Uri uri = FileProvider.getUriForFile(context, "com.manage.iahead.fileprovider", file);

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject of Email");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body of Email");
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
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