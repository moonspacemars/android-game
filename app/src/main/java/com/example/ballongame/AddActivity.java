/*
This activity is used for adding new record. The user must enter a user name, and a correct date format.
The score is assigned by the program.
If the input is not valid, the save button will be disabled.
 */


package com.example.ballongame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {
    private static final int REQUEST_CODE=1;
    private EditText edt_name;
    private EditText edt_score;
    private EditText edt_date;
    private String score;

    //check input edit text
    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean checkInput(){
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        String datestr=edt_date.getText().toString();
        int name_length = edt_name.getText().toString().length();
//        int score_length = edt_score.getText().toString().length();
        Date date=null;
        Integer score_int = 0;
        try {
//            if (score_length>0){
//                score_int=Integer.parseInt(edt_score.getText().toString());
//            }
            date= dateformat.parse(datestr);
            if (!datestr.equals(dateformat.format(date))) {
                date = null;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date!=null && name_length!=0  && date.compareTo(Calendar.getInstance().getTime())<=0){

            return true;
        }
        else{
            if (name_length==0){//name can't be empty
                edt_name.setHintTextColor(Color.RED);
                edt_name.setHint("Name required");
            }

            //The date must not over today
            if (date==null || date.compareTo(Calendar.getInstance().getTime())>0){
                Log.d("check_input", "date_error");
                edt_date.setHintTextColor(Color.RED);
                edt_date.setHint("Date: yyyy-mm-dd");
            }
            return false;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        EditText txt_score = findViewById(R.id.edt_score);
        final Intent intentFromrank =getIntent();
        score=intentFromrank.getStringExtra("score");
        txt_score.setText(score);
        txt_score.setInputType(InputType.TYPE_NULL);

        final Button btn_save = findViewById(R.id.btn_save);
        btn_save.setEnabled(false);

        edt_name = findViewById(R.id.edt_name);
        edt_score = findViewById(R.id.edt_score);
        edt_date = findViewById(R.id.edt_date);

        edt_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void afterTextChanged(Editable s) {
                btn_save.setEnabled(checkInput());

            }
        });


        edt_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void afterTextChanged(Editable s) {
                btn_save.setEnabled(checkInput());
            }
        });





        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = getIntent();
                Record player = new Record();
                player.setPlayer_name(edt_name.getText().toString());
                player.setScore(score);
                player.setDate(edt_date.getText().toString());
//                intent.putExtra("play_record", player);
//                setResult(REQUEST_CODE, intent);
                intentFromrank.putExtra("play_record", player);
                setResult(REQUEST_CODE, intentFromrank);
                finish();

            }
        });

    }
}