package com.example.patrik.alieninvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;


public class Enemy {
    private Bitmap bitmap;
    private int x;
    private int y;
    private int speed = 1;

    private int maxX;
    private int minX;

    private int maxY;
    private int minY;
    private int HP = 3;

    private Rect detectCollision;

    public Enemy(Context context, int screenX, int screenY) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemyship);
        maxX = screenX;
        maxY = screenY;
        minX = bitmap.getWidth();
        minY = 0;
        Random generator = new Random();
        speed = generator.nextInt(6) + 10;
        //x = screenX;
        //y = generator.nextInt(maxY) - bitmap.getHeight();
        x = generator.nextInt(maxX) - bitmap.getWidth();
        y = 0;

        detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update() {
        y += speed;
        if (y > maxY - bitmap.getHeight()) {
            Random generator = new Random();
            speed = generator.nextInt(5) + 5;
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


    public void hit(){
        if (--HP <= 0 ){
            setX(-300);
        }
    }

    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public void setHP(int hp) {this.HP = hp;}


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

    public int getHP() {return HP;}

    public int getSpeed() {
        return speed;
    }

}