package com.example.patrik.alieninvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class Meteor {

    private Bitmap bitmap;
    private int x;
    private int y;
    private int speed;

    private int maxX;
    private int minX;

    private int maxY;
    private int minY;

    private Rect detectCollision;


    public Meteor(Context context, int screenX, int screenY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.meteorsmall);
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        Random generator = new Random();
        speed = generator.nextInt(6) + 10;
        //x = screenX;
        //y = generator.nextInt(maxY) - bitmap.getHeight();
        x = generator.nextInt(maxX) - bitmap.getWidth();
        y = 0;

        //initializing rect object
        detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update() {
        y += speed;
        if (y > maxY - bitmap.getHeight()) {
            Random generator = new Random();
            speed = generator.nextInt(10) + 10;
            y = 0;
            //x = generator.nextInt(maxX) - bitmap.getWidth();
            x = generator.nextInt(maxX - bitmap.getWidth());
            if(x < bitmap.getWidth()/2){
                x = bitmap.getWidth()/2;
            }
            //nextInt(max + 1 - min) + min
        }

        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();
    }



    public void setX(int x){
        this.x = x;
    }

    public Rect getDetectCollision() {
        return detectCollision;
    }

    //getters
    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
