/*
This class inherits from View and customs onDraw method
 */

package com.example.ballongame;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

public class DrawView extends View implements ViewInterface {
    private int width;
    private int height;
    List<Shape> shapeGroup;
    private TouchListener iTouchListener;
    private final int destShape;
    private final int destColor;
    private int checkLifeCycle;
    final Handler h = new Handler();
    
    Runnable r = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };


    @Override
    public void getPosition(int x, int y) {
        //find touched  shape and remove it
        for (int i = 0; i < shapeGroup.size(); i++) {
            Shape s = shapeGroup.get(i);
            if (x <= (s.cx + s.size) && x >= (s.cx - s.size) && y <= (s.cy + s.size) && y >= (s.cy - s.size)) {
                if (iTouchListener != null && destColor == s.colorFlag && destShape == s.shapeFlag) {
                    iTouchListener.onCount(1);
                    long endTime = System.currentTimeMillis();
                    iTouchListener.onTimeCost((endTime - s.createdTime));

                }
                shapeGroup.remove(s);
                addShape();


                break;
            }
        }
    }

    @Override
    public void onStop() {
        h.removeCallbacks(r);
    }

    public void setOnTouchListener(TouchListener eventListener) {
        iTouchListener = eventListener;
    }

    public class Shape {
        private float cx;
        private float cy;
        private float vx;
        private float vy;
        private float size;
        private int colorFlag;
        private int shapeFlag;
        private int remainTime;
        private int countTime;
        private long createdTime;
    }

    public DrawView(Context context, int destShape, int destColor) {
        super(context);
        this.destColor = destColor;
        this.destShape = destShape;
    }

    private boolean checkShapeCollide(Pair<Integer, Integer> l1, Pair<Integer, Integer> r1, Pair<Integer, Integer> l2, Pair<Integer, Integer> r2) {
        // horizontal check
        if (l1.first >= r2.first || l2.first >= r1.first)
            return false;

        // vertical check
        return l1.second < r2.second && l2.second < r1.second;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = canvas.getWidth();
        height = canvas.getHeight();

        //draw the outline of playing ground
        Paint outline_paint = new Paint();
        outline_paint.setAntiAlias(true);
        outline_paint.setColor(Color.GREEN);
        outline_paint.setStyle(Paint.Style.STROKE);
        outline_paint.setStrokeWidth(10);
        canvas.drawRect(0, 0, (float) width, (float) height, outline_paint);


        if (checkLifeCycle == 0) {
            shapeGroup = new ArrayList<>();
            for (int i = 0; i < 12; i++) {
                addShape();
            }
            checkOverLap();
        }
//        System.out.println("Canvas is width : " + width);
        checkLifeCycle++;

        //maintain status every second
        if (checkLifeCycle % 50 == 0) {
            //add the lifetime of disappeared untouched target color shape to the score
            for (int i = 0; i < shapeGroup.size(); i++) {
                Shape s = shapeGroup.get(i);
                s.countTime--;

                if (s.countTime == 0) {
                    if (iTouchListener != null && destShape == s.shapeFlag && destColor == s.colorFlag) {
                        iTouchListener.onTimeCost((s.remainTime * 1000));
                        iTouchListener.missedCount(1);

                    }
                    shapeGroup.remove(i);
                }
            }
            if (shapeGroup.size() < 12) {
                addShape();
            }
        }
        //init shape
        for (int i = 0; i < shapeGroup.size(); i++) {
            Shape s = shapeGroup.get(i);
            Paint p = new Paint();
            p.setAntiAlias(true);
            switch (s.colorFlag) {
                case 0:
                    p.setColor(Color.RED);
                    break;
                case 1:
                    p.setColor(0xffFFA500);
                    break;
                case 2:
                    p.setColor(Color.YELLOW);
                    break;
                case 3:
                    p.setColor(Color.GREEN);
                    break;
                case 4:
                    p.setColor(Color.BLUE);
                    break;
                case 5:
                    p.setColor(0xff8A2BE2);
                    break;
                default:
                    p.setColor(Color.WHITE);
                    break;
            }


            if (((s.cx + s.size + s.vx) > width) || ((s.cx - s.size + s.vx) < 0)) {
                s.vx = -s.vx;
            }
            if (((s.cy + s.size + s.vy) > height) || ((s.cy - s.size + s.vy) < 0)) {
                s.vy = -s.vy;
            }
            s.cx += s.vx;
            s.cy += s.vy;
            // shape Flag = 1 Square
            if (s.shapeFlag == 1) {
                p.setStyle(Paint.Style.FILL);
                canvas.drawRect(s.cx - s.size, s.cy - s.size, s.cx + s.size, s.cy + s.size, p);
            } else {
                canvas.drawCircle(s.cx, s.cy, s.size, p);
            }
        }

        checkOverLap();
        int frameRate = 20;
        h.postDelayed(r, frameRate);
    }

    private boolean checkInitOverlap(Shape s) {
        for (int i = 0; i < shapeGroup.size(); i++) {
            Shape si = shapeGroup.get(i);

            Pair lpi = new Pair((int) si.cx - (int) si.size, (int) si.cy - (int) si.size);
            Pair rpi = new Pair((int) si.cx + (int) si.size, (int) si.cy + (int) si.size);
            Pair lpj = new Pair((int) s.cx - (int) s.size, (int) s.cy - (int) s.size);
            Pair rpj = new Pair((int) s.cx + (int) s.size, (int) s.cy + (int) s.size);
            if (checkShapeCollide(lpi, rpi, lpj, rpj)) {
                return true;
            }
        }
        return false;
    }

    private void checkOverLap() {
        for (int i = 0; i < shapeGroup.size(); i++) {
            for (int j = i + 1; j < shapeGroup.size(); j++) {
                Shape si = shapeGroup.get(i);
                Shape sj = shapeGroup.get(j);
                //get the left and right limit point of 2 shapes
                Pair lpi = new Pair((int) si.cx - (int) si.size, (int) si.cy - (int) si.size);
                Pair rpi = new Pair((int) si.cx + (int) si.size, (int) si.cy + (int) si.size);
                Pair lpj = new Pair((int) sj.cx - (int) sj.size, (int) sj.cy - (int) sj.size);
                Pair rpj = new Pair((int) sj.cx + (int) sj.size, (int) sj.cy + (int) sj.size);
//                System.out.println(i+","+j);
                if (checkShapeCollide(lpi, rpi, lpj, rpj)) {
                    si.vx *= -1;
                    si.vy *= -1;
                    si.cx += si.vx;
                    si.cy += si.vy;
                    sj.vx *= -1;
                    sj.vy *= -1;
                    sj.cx += sj.vx;
                    sj.cy += sj.vy;
//                    lst.set(i,si);
//                    lst.set(j,sj);

                }

            }
        }

    }

    private void addShape() {
        Shape s = new Shape();
        Random r = new Random();
        s.size = (32 + r.nextInt(33)) / 2;
        s.colorFlag = r.nextInt(7);
        s.shapeFlag = r.nextInt(2);
        s.cx = (int) s.size + r.nextInt(width - 2 * (int) s.size);
        s.cy = (int) s.size + r.nextInt(height - 2 * (int) s.size);
        s.remainTime = 3 + r.nextInt(5);
        s.countTime = s.remainTime;
        s.createdTime = System.currentTimeMillis();
//        while (s.vx == 0) s.vx = 11 - r.nextInt(20);
//        while(s.vy == 0) s.vy = 11 - r.nextInt(20);

        //calculate the velocity of shape = 20dp/s
        double tmp = 0.16 * r.nextInt(10) / 10.0;
        s.vx = (float) (Math.sqrt(tmp) * Math.pow(-1.0, 1.0 * r.nextInt(2)));
        s.vy = (float) (Math.sqrt(0.16 - tmp) * Math.pow(-1.0, 1.0 * r.nextInt(2)));
        if (!checkInitOverlap(s)) {
            shapeGroup.add(s);
        } else {
            addShape();
        }

    }
}
