package com.example.patrik.alieninvaders;

import java.util.Random;


public class Star {
    private int x;
    private int y;
    private int speed;

    private int maxX;
    private int maxY;
    private int minX;
    private int minY;



    public Star(int screenX, int screenY) {
        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;
        Random generator = new Random();
        speed = generator.nextInt(10);

        //generating a random coordinate
        //but keeping the coordinate inside the screen size
        x = generator.nextInt(maxX);
        y = generator.nextInt(maxY);
    }

    public void update(int playerSpeed) {
        //animating the star
        y += playerSpeed;
        y += speed;
        //if the star reached the left edge of the screen
        if (y > maxY) {
            //again starting the star from right edge
            //this will give a infinite scrolling background effect

            Random generator = new Random();
            x = generator.nextInt(maxX);
            y = 0;
            speed = generator.nextInt(15) + 1;
        }
    }

    public float getStarWidth() {
        //Making the star width random so that
        float minX = 1.0f;
        float maxX = 4.0f;
        Random rand = new Random();
        float finalX = rand.nextFloat() * (maxX - minX) + minX;
        return finalX;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
