package com.example.patrik.alieninvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.ArrayList;


public class Laser {

    private Bitmap bitmap;
    private int x;
    private int y;
    private Rect collision;
    private int screenSizeX;
    private int screenSizeY;

    public Laser(Context context, int screenSizeX, int screenSizeY, int spaceShipX, int spaceShipY, Bitmap spaceShip){

        this.screenSizeX = screenSizeX;
        this.screenSizeY = screenSizeY;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.laser);
        bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * 3/5, bitmap.getHeight() * 3/5, false);

        x = spaceShipX + spaceShip.getWidth()/2 - bitmap.getWidth()/2;
        y = spaceShipY - bitmap.getHeight() - 10;


        collision = new Rect(x, y, x + bitmap.getWidth(), y + bitmap.getHeight());
    }

    public void update(){
            y -= bitmap.getHeight() - 10;
            collision.left = x;
            collision.top = y;
            collision.right = x + bitmap.getWidth();
            collision.bottom = y + bitmap.getHeight();
    }

    public Rect getDetectCollision() {

        return collision;
    }

    public void destroy(){

        y = 0 - bitmap.getHeight() - 1;
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

}
