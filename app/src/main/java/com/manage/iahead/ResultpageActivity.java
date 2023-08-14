package com.manage.iahead;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.manage.iahead.Utility.SharedPref;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ResultpageActivity extends AppCompatActivity {
    private LinearLayout ll_home;
    private TextView tv_id, tvLeft, tvRight, tvNoise;
    private String left, right;
    private ImageView ivWhatsapp, ivEmail;
    private String Id, leftStr, rightStr, noisy, result, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultpage);

        Intent intent = getIntent();
        leftStr = intent.getStringExtra("left");
        rightStr = intent.getStringExtra("right");
        noisy = intent.getStringExtra("noisy");
        result = intent.getStringExtra("result");
        type = intent.getStringExtra("type");

        SharedPref.init(this);
        Id = SharedPref.read(SharedPref.Id, "");

        ivWhatsapp = findViewById(R.id.ivWhatsapp);
        ivWhatsapp.setOnClickListener(new View.OnClickListener() {
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
        ivEmail= findViewById(R.id.ivEmail);
        ivEmail.setOnClickListener(new View.OnClickListener() {
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
        tvNoise = findViewById(R.id.tvNoise);
        tvLeft = findViewById(R.id.tvLeft);
        tvRight = findViewById(R.id.tvRight);
        tv_id = findViewById(R.id.tv_id);
        tvLeft.setText(leftStr);
        tvRight.setText(rightStr);
        tvNoise.setText(noisy);
        tv_id.setText(Id);

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
        text+="\nDate : "+ gmtDateString;
        text+="\nResult :"+result;
        text+="\nNoisy Level: "+noisy+"HZ";
        if(type.equalsIgnoreCase("0")){
            text+="\nTone Level: 500: Pass";
            text+="\n1000 : Pass";
            text+="\n2000 : Pass";
            text+="\n4000 : Pass";
        }else if(type.equalsIgnoreCase("500")){
            text+="\nTone Level: 500: Refer";
            text+="\n1000 : Refer";
            text+="\n2000 : Refer";
            text+="\n4000 : Refer";
        }else if(type.equalsIgnoreCase("1000")){
            text+="\nTone Level: 500: Pass";
            text+="\n1000 : Refer";
            text+="\n2000 : Refer";
            text+="\n4000 : Refer";
        }else if(type.equalsIgnoreCase("2000")){
            text+="\nTone Level: 500: Pass";
            text+="\n1000 : Pass";
            text+="\n2000 : Refer";
            text+="\n4000 : Refer";
        }else if(type.equalsIgnoreCase("4000")){
            text+="\nTone Level: 500: Pass";
            text+="\n1000 : Pass";
            text+="\n2000 : Pass";
            text+="\n4000 : Refer";
        }
        return text;
    }
}