package com.manage.iahead;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.manage.iahead.Utility.SharedPref;

public class ResultpageActivity extends AppCompatActivity {
    private LinearLayout ll_home;
    private TextView tv_id, tvLeft, tvRight, tvNoise;
    private String left, right;
    private ImageView ivWhatsapp, ivEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_resultpage);

        Intent intent = getIntent();
        String left = intent.getStringExtra("left");
        String right = intent.getStringExtra("right");
        String noisy = intent.getStringExtra("noisy");

        ivWhatsapp = findViewById(R.id.ivWhatsapp);
        ivWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "My iAhead result left Pass : Right Pass");
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    //ToastHelper.MakeShortText("Whatsapp have not been installed.");
                }
            }
        });
        ivEmail= findViewById(R.id.ivEmail);
        ivEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, "");
                intent.putExtra(Intent.EXTRA_SUBJECT, "My iAhead result left Pass : Right Pass");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        tvNoise = findViewById(R.id.tvNoise);
        tvNoise.setText(noisy+ "dB");
        tvLeft = findViewById(R.id.tvLeft);
        tvRight = findViewById(R.id.tvRight);
        tv_id = findViewById(R.id.tv_id);
        tvLeft.setText(left);
        tvRight.setText(right);

        SharedPref.init(this);
        String Id = SharedPref.read(SharedPref.Id, "");
        tv_id.setText(Id);

        ll_home = findViewById(R.id.ll_home);
        ll_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}