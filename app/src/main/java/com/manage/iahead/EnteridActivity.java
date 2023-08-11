package com.manage.iahead;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    private String blockCharacterSet = "<>,.{}[]~#^|$%&*!@_-+(|=)/':;?\"";

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
        et_id.setFilters(new InputFilter[] { filter });
        chk = findViewById(R.id.chk);
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
                if(!chk.isChecked()){
                    Toast.makeText(EnteridActivity.this, "Please check the checkbox.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_id.getText().toString().length()==0){
                    Toast.makeText(EnteridActivity.this, "Please enter your id.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(et_id.getText().toString().length()<3){
                    Toast.makeText(EnteridActivity.this, "Minumum 3 Characters.", Toast.LENGTH_SHORT).show();
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