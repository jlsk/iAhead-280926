package com.manage.iahead;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.manage.iahead.Utility.SharedPref;

import java.util.Calendar;

public class EnteridActivity extends AppCompatActivity {
    private TextView tv_type;
    private EditText et_id, et_calendar, et_phone, et_email, et_name;
    private CheckBox chk;
    private LinearLayout ll_enter;
    private Intent intent;
    private String blockCharacterSet = "<>,.{}[]~#^|$%&*!@_-+(|=)/':;?\"";
    private ImageView iv_twin;
    private RelativeLayout rl_calendar;
    private DatePickerDialog datePickerDialog;
    private RadioGroup radiogroup;
    private String dbStr="";

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enterid);

        radiogroup = findViewById(R.id.radiogroup);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i) {
                    case R.id.radio1:
                        dbStr="25";
                        break;
                    case R.id.radio2:
                        dbStr="40";
                        break;
                }
            }
        });
        et_name = findViewById(R.id.et_name);
        et_phone = findViewById(R.id.et_phone);
        et_email = findViewById(R.id.et_email);
        et_calendar = findViewById(R.id.et_calendar);
        et_calendar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_UP)
                {
                    callDate();
                }
                return true;
            }
        });
        rl_calendar = findViewById(R.id.rl_calendar);
        chk = findViewById(R.id.chk);
        iv_twin = findViewById(R.id.iv_twin);
        intent = getIntent();
        String type = intent.getStringExtra("type");
        tv_type = findViewById(R.id.tv_type);
        if(type.equalsIgnoreCase("child")){
            rl_calendar.setVisibility(View.VISIBLE);
            et_name.setVisibility(View.VISIBLE);
            tv_type.setText("I'm a Child");
            iv_twin.setBackground(ContextCompat.getDrawable(this, R.drawable.child1));
        }else if(type.equalsIgnoreCase("parent")){
            et_phone.setVisibility(View.VISIBLE);
            et_email.setVisibility(View.VISIBLE);
            chk.setVisibility(View.VISIBLE);
            tv_type.setText("I'm a Parent/Teacher");
            iv_twin.setBackground(ContextCompat.getDrawable(this, R.drawable.parent1));
        }else{
            et_email.setVisibility(View.VISIBLE);
            et_phone.setVisibility(View.VISIBLE);
            chk.setVisibility(View.VISIBLE);
            tv_type.setText("I'm a Professional");
            iv_twin.setBackground(ContextCompat.getDrawable(this, R.drawable.professional1));
        }
        et_id = findViewById(R.id.et_id);
        et_id.setFilters(new InputFilter[] { filter });
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    View view = EnteridActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
        });
        ll_enter = findViewById(R.id.ll_enter);
        ll_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_id.getText().toString().length()==0){
                    Toast.makeText(EnteridActivity.this, "Please enter your id.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_id.getText().toString().length()<3){
                    Toast.makeText(EnteridActivity.this, "Minumum 3 Characters.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(type.equalsIgnoreCase("child")) {
                    if (et_calendar.getText().toString().length() == 0) {
                        Toast.makeText(EnteridActivity.this, "Please choose date of birth.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (et_name.getText().toString().length() == 0) {
                        Toast.makeText(EnteridActivity.this, "Please enter your name.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else{
                    if(!chk.isChecked()){
                        Toast.makeText(EnteridActivity.this, "Please check the checkbox.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(et_phone.getText().toString().length()== 0){
                        Toast.makeText(EnteridActivity.this, "Please enter your phone.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(et_email.getText().toString().length()== 0){
                        Toast.makeText(EnteridActivity.this, "Please enter your email.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (radiogroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(EnteridActivity.this, "Please select screening level.", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPref.init(EnteridActivity.this);
                SharedPref.write(SharedPref.Id, et_id.getText().toString());
                SharedPref.write(SharedPref.SCREENINGLEVEL, dbStr);
                SharedPref.write(SharedPref.EMAIL, et_email.getText().toString());
                SharedPref.write(SharedPref.PHONE, et_phone.getText().toString());
                SharedPref.write(SharedPref.NAME, et_name.getText().toString());
                SharedPref.write(SharedPref.DATETIME, et_calendar.getText().toString());
                intent = new Intent(EnteridActivity.this, HeadphonesettingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void callDate(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                et_calendar.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                View view1 = EnteridActivity.this.getCurrentFocus();
                if (view1 != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view1.getWindowToken(), 0);
                }
            }
        };

        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(EnteridActivity.this,
                dateSetListener, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
}