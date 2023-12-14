package com.manage.iahead;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
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

public class ResultpageActivity extends AppCompatActivity {
    private LinearLayout ll_home, ll_whatsapp, ll_email;
    private TextView tv_id, tvLeft, tvRight, tvNoise, tv_date, tv_location, tv_screening,
            tv500left, tv1000left, tv2000left, tv4000left, tv500right, tv1000right, tv2000right, tv4000right;
    private String left, right;
    private String Id, leftStr, rightStr, noisy, result, type, name, phone, email, typecat, datetime, testlocation,
    screeninglevel, right500, right1000, right2000, right4000, left500, left1000, left2000, left4000, datetime1;
    private int countBack=0;
    private CountDownTimer cTimerBack = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultpage);

        Intent intent = getIntent();
        right500 = intent.getStringExtra("right500");
        right1000 = intent.getStringExtra("right1000");
        right2000 = intent.getStringExtra("right2000");
        right4000 = intent.getStringExtra("right4000");
        left500 = intent.getStringExtra("left500");
        left1000 = intent.getStringExtra("left1000");
        left2000 = intent.getStringExtra("left2000");
        left4000 = intent.getStringExtra("left4000");

        tv500left = findViewById(R.id.tv500left);
        tv1000left = findViewById(R.id.tv1000left);
        tv2000left = findViewById(R.id.tv2000left);
        tv4000left = findViewById(R.id.tv4000left);
        tv500right = findViewById(R.id.tv500right);
        tv1000right = findViewById(R.id.tv1000right);
        tv2000right = findViewById(R.id.tv2000right);
        tv4000right = findViewById(R.id.tv4000right);

        tv500left.setText("500hz: "+left500);
        tv1000left.setText("1000hz: "+left1000);
        tv2000left.setText("2000hz: "+left2000);
        tv4000left.setText("4000hz: "+left4000);

        tv500right.setText("500hz: "+right500);
        tv1000right.setText("1000hz: "+right1000);
        tv2000right.setText("2000hz: "+right2000);
        tv4000right.setText("4000hz: "+right4000);

        SharedPref.init(this);
        Id = SharedPref.read(SharedPref.Id, "");
        name = SharedPref.read(SharedPref.NAME, "");
        phone = SharedPref.read(SharedPref.PHONE, "");
        email = SharedPref.read(SharedPref.EMAIL, "");
        typecat = SharedPref.read(SharedPref.TYPE, "");
        datetime = SharedPref.read(SharedPref.DATETIME, "");
        testlocation = SharedPref.read(SharedPref.TESTLOCATION, "");
        screeninglevel = SharedPref.read(SharedPref.SCREENINGLEVEL, "");

        tv_date = findViewById(R.id.tv_date);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yy hh:mm aa");
        datetime1 = dateformat.format(c.getTime());
        tv_date.setText(datetime1);

        tv_location = findViewById(R.id.tv_location);
        tv_location.setText("Test Location : "+testlocation);

        tv_screening = findViewById(R.id.tv_screening);
        tv_screening.setText("Screening Level : "+screeninglevel+ "db");

        ll_whatsapp = findViewById(R.id.ll_whatsapp);
        ll_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date currentDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy HH:mm:ss z");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
                String gmtDateString = sdf.format(currentDate);
                String textContent = getResultAsText();
                String shareContent = textContent + "\n\nShared on: " + gmtDateString;
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
                    Toast.makeText(ResultpageActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        tv_id = findViewById(R.id.tv_id);
        if(typecat.equalsIgnoreCase("child")){
            tv_id.setText(Id + " / "+ name + " / "+ datetime);
        }else{
            tv_id.setText(Id + " / "+ phone + " / "+ email);
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
        text+="\nDate & Time : "+ datetime1;
        text+="\nTest Location :"+testlocation+"\n";
        text+="\nScreening Level: "+screeninglevel+"db\n";
        text+="\nRight:";
        text += "\n500hz : " + right500;

        text+="\n1000hz : "+right1000;
        text+="\n2000hz : "+right2000;
        text+="\n4000hz : "+right4000;
        text+="\nLeft:";
        text += "\n500hz : " + left500;

        text+="\n1000hz : "+left1000;
        text+="\n2000hz : "+left2000;
        text+="\n4000hz : "+left4000;

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
//            Toast.makeText(ResultpageActivity.this, "Press back again to quit the app", Toast.LENGTH_SHORT).show();
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