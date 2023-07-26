package com.manage.iahead;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.manage.iahead.Utility.SharedPref;

public class EnteridActivity extends AppCompatActivity {
    private TextView tv_type;
    private EditText et_id;
    private CheckBox chk;
    private LinearLayout ll_enter;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_enterid);

        intent = getIntent();
        String type = intent.getStringExtra("type");
        tv_type = findViewById(R.id.tv_type);
        if(type.equalsIgnoreCase("child")){
            tv_type.setText("I'm a child");
        }else if(type.equalsIgnoreCase("parent")){
            tv_type.setText("I'm a parent");
        }else{
            tv_type.setText("I'm a therapist");
        }
        et_id = findViewById(R.id.et_id);
        chk = findViewById(R.id.chk);
        ll_enter = findViewById(R.id.ll_enter);
        ll_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!chk.isChecked()){
                    Toast.makeText(EnteridActivity.this, "Please check the checkbox.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_id.getText().toString().length()==0){
                    Toast.makeText(EnteridActivity.this, "Please enter your id.", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPref.init(EnteridActivity.this);
                SharedPref.write(SharedPref.Id, et_id.getText().toString());
                intent = new Intent(EnteridActivity.this, HeadphonesettingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}