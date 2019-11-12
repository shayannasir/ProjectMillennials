package com.shayannasir.projectm;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class GameView extends View {

    //this is our custom view class
    Handler handler;//handker is required to schdeule a runnable after some delay
    Runnable runnable;
    final int UPDATE_MILLIS=30;
    Bitmap background;
    Bitmap toptube, bottomtube;
    Display display;
    Point point;
    int dWidth, dHeight; // device wid and ht
    Rect rect;
    //lets create bitmap array for bird
    Bitmap[] birds;
    //we need int var to keep track of bird frame
    int birdFrame = 0;
    int velocity = 5, gravity = 3;
    //we need to keep track of bird pos
    int birdX, birdY;
    boolean gameState = false;
    int gap = 600;  //gap bw tubes
    int minTubeOffset, maxTubeOffset;
    int noOfTubes = 3;
    int distBwTubes;
    int[] tubeX = new int[noOfTubes];
    int[] topTubeY = new int[noOfTubes];
    Random random;
    int tubeVel = 12;

    public GameView(Context context){
        super(context);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();//this will call onDraw
            }
        };
        background = BitmapFactory.decodeResource(getResources(),R.drawable.background1);
        toptube = BitmapFactory.decodeResource(getResources(),R.drawable.toptube);
        bottomtube = BitmapFactory.decodeResource(getResources(),R.drawable.bottomtube);
        display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
        point = new Point();
        display.getSize(point);
        dWidth = point.x;
        dHeight = point.y;
        rect = new Rect(0,0,dWidth,dHeight);
        birds = new Bitmap[2];
        birds[0] = BitmapFactory.decodeResource(getResources(),R.drawable.bird);
        birds[1] = BitmapFactory.decodeResource(getResources(),R.drawable.bird2);
        birdX = dWidth/2 - birds[0].getWidth()/2; //initial pos of bird
        birdY = dHeight/2 - birds[0].getHeight()/2;
        distBwTubes = dWidth+3/4;
        minTubeOffset = gap/2;
        maxTubeOffset = dHeight - minTubeOffset - gap;
        random = new Random();
        for(int i =0; i<noOfTubes;i++){
            tubeX[i] = dWidth + i*distBwTubes;
            topTubeY[i] = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset + 1);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //we'll draw our view innside ondraw()
        //draw the bg on canvas
        canvas.drawBitmap(background,null,rect,null);
        if(birdFrame == 0)
            birdFrame = 1;
        else
            birdFrame = 0;
        if(gameState){
            //bird should be on screen
            if(birdY < dHeight - birds[0].getHeight() || velocity < 0){ //this way bird cant go beyond bottom edge
                velocity += gravity; //as bird falls it gets faster by incrementing vel
                birdY += velocity;
            }
            for(int i=0;i<noOfTubes;i++){
                tubeX[i] -= tubeVel;
                if(tubeX[i]<-toptube.getWidth()){
                    tubeX[i] += noOfTubes * distBwTubes;
                    topTubeY[i] = minTubeOffset + random.nextInt(maxTubeOffset - minTubeOffset + 1);
                }
                canvas.drawBitmap(toptube, tubeX[i], topTubeY[i] - toptube.getHeight(),null);
                canvas.drawBitmap(bottomtube, tubeX[i], topTubeY[i]+gap,null);

            }
        }




        //we want the bird ro be displayed on center of screen
        //both b[0] and birds[1] have same dimension
        canvas.drawBitmap(birds[birdFrame],birdX,birdY,null);
        handler.postDelayed(runnable,UPDATE_MILLIS);

    }

    //getting touch event

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN){ //ie tap is detected on screen
            //here we want bird to move upward by some unit
            velocity = -30; //assuming 30units upwards
            gameState = true;
        }

        return true; //by true indicate that weve done touch event and no further action is req
    }
}