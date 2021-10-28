/*
This is a game program.

The guide shows a hint to the player that needs to be touched. Every shape has a lifetime and will
disappear when it reaches to lifetime. If the target shape doesn't be touched before its lifetime,
the lifetime will add to the score. The score is calculated by the sum of the time spent on each
target shape. The lower the score, the better the performance.
 */


package com.example.ballongame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_GAME = 2;
    private TextView txt_guide;
    private int destShape;
    private int destColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn_start = findViewById((R.id.btn_start));
        txt_guide = findViewById(R.id.txt_guide);
        generateRandomGuide();
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);

                intent.putExtra("target_shape", Integer.toString(destShape));
                intent.putExtra("target_color", Integer.toString(destColor));
                startActivity(intent);
            }
        });


    }

    private void generateRandomGuide() {
        Random r = new Random();

        destShape = r.nextInt(2);
        destColor = r.nextInt(7);
        String shapeType = "";
        String colorType = "";


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
        txt_guide.setText("Touch only " + colorType + " " + shapeType + "s. The game ends after 10 correct shapes be touched.");
    }


}