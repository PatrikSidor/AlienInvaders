package com.example.patrik.alieninvaders;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

public class Player {
    private Bitmap bitmap, bitmapLifes;
    private int x;
    private int y;
    private int screenX;
    private int screenY;
    private int speed = 0;
    private boolean boosting;
    private final int GRAVITY = 0;
    private int maxX;
    private int minX;
    private int maxY;
    private int minY;
    private Context context;

    private boolean isSteerLeft, isSteerRight;
    private float steerSpeed = 1;
    private ArrayList<Laser> lasers;

    private final int MIN_SPEED = 0;
    private final int MAX_SPEED = 20;

    private Rect detectCollision;



    public Player(Context context, int screenX, int screenY) {

        speed = 0;
        this.screenX = screenX;
        this.screenY = screenY;
        this.context = context;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        maxX = screenX - bitmap.getWidth();
        minX = 0;
        maxY = screenY - bitmap.getHeight();
        minY = 0;
        boosting = false;
        x = screenX/2 - bitmap.getWidth()/2;
        y = screenY;

        lasers = new ArrayList<>();

        bitmapLifes = BitmapFactory.decodeResource(context.getResources(), R.drawable.life);
        bitmapLifes = Bitmap.createScaledBitmap(bitmapLifes, bitmapLifes.getWidth() * 3/5, bitmapLifes.getHeight() * 3/5, false);

        //System.out.println(bitmap.getHeight());
        //initializing rect object
        detectCollision =  new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void setBoosting() {
        boosting = true;
    }

    public void stopBoosting() {
        boosting = false;
    }

    public void update() {

        if (isSteerLeft){
            x -= 5 * steerSpeed;
            if (x < minX){
                x = minX;
            }
        }else if (isSteerRight){
            x += 5 * steerSpeed;
            if (x > maxX){
                x = maxX;
            }
        }

        if (speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }

        y -= speed + GRAVITY;

        if (y < minY) {
            y = minY;
        }
        if (y > maxY) {
            y = maxY;
        }

        for (Laser l : lasers) {
            l.update();
        }

        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();

    }

    public void fire(){

        lasers.add(new Laser(context, screenX, screenY, x, y, bitmap));
    }

    public ArrayList<Laser> getLasers() {

        return lasers;
    }

    public void steerRight(float speed){

        isSteerLeft = false;
        isSteerRight = true;
        steerSpeed = Math.abs(speed);
    }

    public void steerLeft(float speed){

        isSteerRight = false;
        isSteerLeft = true;
        steerSpeed = Math.abs(speed);
    }

    public void stay(){

        isSteerLeft = false;
        isSteerRight = false;
        steerSpeed = 0;
    }

    public Rect getDetectCollision() {
        return detectCollision;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    public Bitmap getBitmapLives() {

        return bitmapLifes;
    }
}