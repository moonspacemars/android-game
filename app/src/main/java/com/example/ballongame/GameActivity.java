/*
This activity is for the playing field.
 */


package com.example.ballongame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private GestureDetector iDetector;
    private DrawView dview;
    private TextView tview;
    private TextView countView;
    private TextView missedView;
    private Button btn_rank;
    private int count = 0;
    private float timeCost = 0;
    private String shapeType = "";
    private String colorType = "";
    private int missedCount = 0;
    private int EndGameCount=10;

    public void onClickBtnStart(View view) {
        count = 0;
        timeCost = 0;
        missedCount = 0;
        dview.onStop();
        dview = null;
        btn_rank.setEnabled(false);
        Random r = new Random();
        int destShape = r.nextInt(2);
        int destColor = r.nextInt(7);
        initDrawView(destShape, destColor);
    }
    class AGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            int x = (int)e.getX();
            int y = (int)e.getY();
            dview.getPosition(x, y);
            return super.onDown(e);
        }
    }


    private  void initDrawView(int target_shape, int target_color) {
        LinearLayout layout =  findViewById(R.id.view_layout);
        layout.removeAllViews();

        int destShape=target_shape;
        int destColor=target_color;


        switch (destShape) {
            case 0:
                shapeType = "Circle";
                break;
            default:
                shapeType = "Square";
                break;
        }
        switch (destColor) {
            case 0:
                colorType = "Red";
                break;
            case 1:
                colorType = "Orange";
                break;
            case 2:
                colorType = "Yellow";
                break;
            case 3:
                colorType = "Green";
                break;
            case 4:
                colorType = "Blue";
                break;
            case 5:
                colorType = "Purple";
                break;
            default:
                colorType = "White";
                break;

        }
        tview.setText("Touch only " + colorType + " " + shapeType + "s. The cost Time is :");
        countView.setText("");
        missedView.setText("");

        dview = new DrawView(this, destShape, destColor);
        dview.setOnTouchListener(new TouchListener() {

            // calculate for the hit count
            @Override
            public void onCount(int i) {
                count += i;
                countView.setText("Hit count: " + count);
                if (count >= EndGameCount){
                    dview.onStop();
                    LinearLayout layout = findViewById(R.id.view_layout);
                    layout.removeAllViews();
                    btn_rank.setEnabled(true);

                }
                else{
                    btn_rank.setEnabled(false);
                }


            }
            //calculate the total score which is also the cost time
            @Override
            public void onTimeCost(long i) {
                timeCost += i / 1000.0;
                // round to 2 decimal point
                timeCost = (float)(Math.round(timeCost*100.0)/100.0);
//                System.out.println("Time is cost :" + timeCost);

                int minute = (int)(timeCost/60);
                float second = timeCost - 60*minute;


                tview.setText("Touch only " + colorType + " " + shapeType + "s. The cost Time is : " + minute+":"+second);

            }
            //calculate the miss count
            @Override
            public void missedCount(int i) {
                missedCount++;
                missedView.setText("Miss count: " + missedCount);


            }
        });
        dview.invalidate();
        // get the gesture detector
        iDetector = new GestureDetector(this, new GameActivity.AGestureListener());


        dview.setOnTouchListener(touchListener);
        layout.addView(dview);
        layout.setBackgroundColor(Color.BLACK);

    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            return iDetector.onTouchEvent(event);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        tview = findViewById(R.id.txt_description);
        countView = findViewById(R.id.txt_count);
        missedView = findViewById(R.id.txt_missed);
//        Button btn_add = findViewById(R.id.btn_addNewRecord);
        btn_rank = findViewById(R.id.btn_rank);
        btn_rank.setEnabled(false);
        btn_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rank_intent = new Intent(GameActivity.this, RankActivity.class);
                rank_intent.putExtra("score", Float.toString(timeCost));
                startActivity(rank_intent);

            }
        });


//        btn_add.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent add_intent = new Intent(GameActivity.this, AddActivity.class);
//                add_intent.putExtra("score",Float.toString(timeCost));
//                startActivity(add_intent);
//
//            }
//        });

        Intent intent = getIntent();
        int target_shape = Integer.parseInt(intent.getStringExtra("target_shape"));
        int target_color = Integer.parseInt(intent.getStringExtra("target_color"));

        initDrawView(target_shape, target_color);
    }



}