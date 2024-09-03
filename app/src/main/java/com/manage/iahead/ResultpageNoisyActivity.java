package com.manage.iahead;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.manage.iahead.Utility.SharedPref;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ResultpageNoisyActivity extends AppCompatActivity {
    private LinearLayout ll_home, ll_whatsapp, ll_email, ll_followup;
    private TextView tv_id, tvLeft, tvRight, tvNoise, tv_date, tv_location, tv_screening,
             tv1000left, tv2000left, tv4000left,  tv1000right, tv2000right, tv4000right,
            tv1000leftNoise, tv2000leftNoise, tv4000leftNoise,
            tv1000rightNoise, tv2000rightNoise, tv4000rightNoise, tvText, tv_1, tv_2, tv_3, tv_test, tv_time;
    private String left, right;
    private String Id, leftStr, rightStr, noisy, result, type, name, phone, email, typecat, datetime, testlocation,
    screeninglevel, right1000, right2000, right4000,  left1000, left2000, left4000, datetime1,
            left500db, left1000db, left2000db, left4000db,
            right500db, right1000db, right2000db, right4000db, startendtime, followuplink;
    private int countBack=0;
    private CountDownTimer cTimerBack = null;
    private Button btnLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultpagenoisy);

        Intent intent = getIntent();
        right1000 = intent.getStringExtra("right1000");
        right2000 = intent.getStringExtra("right2000");
        right4000 = intent.getStringExtra("right4000");
        left1000 = intent.getStringExtra("left1000");
        left2000 = intent.getStringExtra("left2000");
        left4000 = intent.getStringExtra("left4000");

        right500db = intent.getStringExtra("right500db");
        right1000db = intent.getStringExtra("right1000db");
        right2000db = intent.getStringExtra("right2000db");
        right4000db = intent.getStringExtra("right4000db");
        left500db = intent.getStringExtra("left500db");
        left1000db = intent.getStringExtra("left1000db");
        left2000db = intent.getStringExtra("left2000db");
        left4000db = intent.getStringExtra("left4000db");
        String logs = intent.getStringExtra("logs");
        startendtime = intent.getStringExtra("startendtime");

        tv1000left = findViewById(R.id.tv1000left);
        tv2000left = findViewById(R.id.tv2000left);
        tv4000left = findViewById(R.id.tv4000left);
        tv1000right = findViewById(R.id.tv1000right);
        tv2000right = findViewById(R.id.tv2000right);
        tv4000right = findViewById(R.id.tv4000right);

        tv1000leftNoise = findViewById(R.id.tv1000leftnoise);
        tv2000leftNoise = findViewById(R.id.tv2000leftnoise);
        tv4000leftNoise = findViewById(R.id.tv4000leftnoise);
        tv1000rightNoise = findViewById(R.id.tv1000rightnoise);
        tv2000rightNoise = findViewById(R.id.tv2000rightnoise);
        tv4000rightNoise = findViewById(R.id.tv4000rightnoise);
        tv1000leftNoise.setText("Noise: "+left1000db);
        tv2000leftNoise.setText("Noise: "+left2000db);
        tv4000leftNoise.setText("Noise: "+left4000db);
        tv1000rightNoise.setText("Noise: "+right1000db);
        tv2000rightNoise.setText("Noise: "+right2000db);
        tv4000rightNoise.setText("Noise: "+right4000db);

        tv_time = findViewById(R.id.tv_time);

        tv1000left.setText("1000Hz: "+left1000);
        tv2000left.setText("2000Hz: "+left2000);
        tv4000left.setText("4000Hz: "+left4000);

        tv1000right.setText("1000Hz: "+right1000);
        tv2000right.setText("2000Hz: "+right2000);
        tv4000right.setText("4000Hz: "+right4000);

        tvText = findViewById(R.id.tvText);
        tv_1 = findViewById(R.id.tv_1);
        tv_2 = findViewById(R.id.tv_2);
        tv_3 = findViewById(R.id.tv_3);
        tv_test = findViewById(R.id.tv_test);
        ll_followup = findViewById(R.id.ll_followup);

        SharedPref.init(this);
        Id = SharedPref.read(SharedPref.Id, "");
        name = SharedPref.read(SharedPref.NAME, "");
        phone = SharedPref.read(SharedPref.PHONE, "");
        email = SharedPref.read(SharedPref.EMAIL, "");
        typecat = SharedPref.read(SharedPref.TYPE, "");
        datetime = SharedPref.read(SharedPref.DATETIME, "");
        testlocation = SharedPref.read(SharedPref.TESTLOCATION, "");
        screeninglevel = SharedPref.read(SharedPref.SCREENINGLEVEL, "");
        String DEBUGMODESTR = SharedPref.read(SharedPref.DEBUGMODE, "");
        followuplink = SharedPref.read(SharedPref.FOLLOWUPLINK, "https://go.cheesze.com/iaheaddefaultform");
        tv_time.setText("Start & End Date Time : "+startendtime);

        ll_followup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(followuplink));
                startActivity(browserIntent);
            }
        });

        btnLogs = findViewById(R.id.btnLogs);
        btnLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultpageNoisyActivity.this, LogsActivity.class);
                intent.putExtra("logs", logs);
                startActivity(intent);
            }
        });
        if(DEBUGMODESTR.equalsIgnoreCase("Y")){
            btnLogs.setVisibility(View.VISIBLE);
        }

        tv_date = findViewById(R.id.tv_date);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/M/yy hh:mm aa");
        datetime1 = dateformat.format(c.getTime());
        tv_date.setText(datetime1);

        tv_location = findViewById(R.id.tv_location);
        tv_location.setText("Test Place : "+testlocation.toUpperCase());

        tv_screening = findViewById(R.id.tv_screening);
        tv_screening.setText("Screening Level : "+screeninglevel+ "dB");
        String sourceString="";

        if(screeninglevel.equalsIgnoreCase("25")){
            tv_test.setText("Test : Quiet");
            sourceString = "<b>" + "PASS " + "</b> " +
                    " - pass at all test frequencies<br>"+
                    " • Your hearing should be OK<br>"+
                    "<b>" + "REFER " + "</b> " +
                    " - • Find an audiologist for further hearing assessment.<br>"+
                    "<b>" + "NOISE " + "</b> " +
                    " - • Find a quieter place and re-test.";
        }else{
            tv_test.setText("Test : Noisy");
            sourceString = "<b>" + "PASS " + "</b> " +
                    " - pass at all test frequencies<br>"+
                    " - • You do not have a signficant hearing loss<br>"+
                    "<b>" + "REFER " + "</b> " +
                    " - • Find an audiologist for further hearing assessment<br>"+
                    "<b>" + "NOISE " + "</b> " +
                    " - • Find a quieter place and re-test.";
        }

        tvText.setText(Html.fromHtml(sourceString));

        ll_whatsapp = findViewById(R.id.ll_whatsapp);
        ll_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy HH:mm:ss z");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                String gmtDateString = sdf.format(currentDate);
                String textContent = getResultAsText();
                String shareContent = textContent; //+ "\n\nShared on: " + gmtDateString;
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
                startActivity(Intent.createChooser(shareIntent, "Share With"));
            }
        });
        ll_email= findViewById(R.id.ll_email);
        ll_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(Intent.ACTION_SEND);

                mIntent.setData(Uri.parse("mailto:"));
                mIntent.setType("text/plain");

                mIntent.putExtra(Intent.EXTRA_SUBJECT, "Test result for ID " + Id);

                String shareText = getResultAsText();
                shareText += "\n\nThis test result is based on individual self testing using the iAhead hearing test application. " +
                        "For a more detailed hearing test with the recommended headset, please contact us at irichment@gmail.com " +
                        "or go to www.irichment.com/iAhead for more information.";
                mIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                try {
                    startActivity(Intent.createChooser(mIntent, "Choose Email Client..."));
                } catch (Exception e) {
                    Toast.makeText(ResultpageNoisyActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        tv_id = findViewById(R.id.tv_id);
        if(typecat.equalsIgnoreCase("child")){
//            tv_id.setText(Id + " / "+ name + " / "+ datetime);
            tv_1.setText("ID : "+Id);
            tv_2.setText("Name : "+name);
            tv_3.setText("DOB "+datetime);
        }else{
//            tv_id.setText(Id + " / "+ phone + " / "+ email);
            tv_1.setText("ID : "+Id);
            tv_2.setText("Phone : "+phone);
            tv_3.setText("Email "+email);
        }

        ll_home = findViewById(R.id.ll_home);
        ll_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private String getResultAsText(){
        String text="";
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy HH:mm:ss z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String gmtDateString = sdf.format(currentDate);

        text+="iAhead Test Result:";
        text+="\nID: "+Id;
        String type = SharedPref.read(SharedPref.TYPE, "");
        if(type.equalsIgnoreCase("child")){
            text+="\nName : "+ name;
        }
        text+="\nDOB : "+ datetime1;
        text+="\nScreening Level: "+screeninglevel+"dB";
        String test="";
        if(screeninglevel.equalsIgnoreCase("25")){
            test="Quiet";
        }else{
            test="Test : Noisy";
        }
        text+="\nTest :"+test+"";
        text+="\nTest Place :"+testlocation+"\n";
        text+="\nRight:";
        text+= "\n500Hz : N/A";
//        text+= "\nNoise : "+right1000db;
        text+="\n1000Hz : "+right1000;
//        text+= "\nNoise : "+right2000db;
        text+="\n2000Hz : "+right2000;
//        text+= "\nNoise : "+right4000db;
        text+="\n4000Hz : "+right4000;
        text+="\n\nLeft:";
        text+= "\n500Hz : N/A";
//        text+= "\nNoise : "+left1000db;
        text+="\n1000Hz : "+left1000;
//        text+= "\nNoise : "+left2000db;
        text+="\n2000Hz : "+left2000;
//        text+= "\nNoise : "+left4000db;
        text+="\n4000Hz : "+left4000;
        text+="\n\n";
        if(screeninglevel.equalsIgnoreCase("25")) {
            text += "PASS - pass at all test frequencies\n" +
                    "Your hearing should be OK \n";
            text += "REFER - • Find an audiologist for further hearing assessment.\n";
            text += "NOISY - • Find a quieter place and re-test.";
        }else{
            text += "PASS - pass at all test frequencies\n" +
                    "You do not have a significant hearing loss \n";
            text += "REFER - • Find an audiologist for further hearing assessment.\n";
            text += "NOISY - • Find a quieter place and re-test.";
        }

        return text;
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
//            Toast.makeText(ResultpageNoisyActivity.this, "Press back again to quit the app", Toast.LENGTH_SHORT).show();
//            countBack = 1;
//        }else{
//            cancelTimerBack();
//            finish();
//        }
//
//    }

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
}