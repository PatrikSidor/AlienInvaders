package com.example.patrik.alieninvaders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GameView extends SurfaceView implements Runnable {

    Context context;
    volatile boolean playing;
    private Thread gameThread = null;
    private Player player;
    private Meteor meteor;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Enemy[] enemies;
    private ArrayList<Laser> lasers;
    private int counter = 0;
    private int enemyCount = 3;
    private ArrayList<Star> stars = new ArrayList<Star>();
    private Boom boom;
    int screenX;
    int screenY;
    int life;
    boolean flag ;
    private boolean isGameOver ;
    static MediaPlayer gameOnsound;
    final MediaPlayer killedEnemysound;
    final MediaPlayer gameOversound;
    int score;
    int highScore[] = new int[4];
    SharedPreferences sharedPreferences;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        player = new Player(context, screenX, screenY);
        this.context = context;

        surfaceHolder = getHolder();
        paint = new Paint();

        this.screenX = screenX;
        this.screenY = screenY;
        life = 3;
        isGameOver = false;

        score = 0;

        sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME",Context.MODE_PRIVATE);

        highScore[0] = sharedPreferences.getInt("score1",0);
        highScore[1] = sharedPreferences.getInt("score2",0);
        highScore[2] = sharedPreferences.getInt("score3",0);
        highScore[3] = sharedPreferences.getInt("score4",0);

        int starNums = 100;
        for (int i = 0; i < starNums; i++) {
            Star s = new Star(screenX, screenY);
            stars.add(s);
        }

        enemies = new Enemy[enemyCount];
        for(int i=0; i<enemyCount; i++){
            enemies[i] = new Enemy(context, screenX, screenY);
        }

        boom = new Boom(context);

        meteor = new Meteor(context, screenX, screenY);

        gameOnsound = MediaPlayer.create(context,R.raw.gameon);
        killedEnemysound = MediaPlayer.create(context,R.raw.killedenemy);
        gameOversound = MediaPlayer.create(context,R.raw.gameover);

        gameOnsound.start();
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update() {
        score++;

        player.update();

        boom.setX(-250);
        boom.setY(-250);

        for (Star s : stars) {
            s.update(player.getSpeed());
        }

        meteor.update();

        if (counter % 200 == 0) {
            player.fire();
        }

        for(int i=0; i<enemyCount; i++) {
            enemies[i].update();

            if (Rect.intersects(player.getDetectCollision(), enemies[i].getDetectCollision())) {

                boom.setX(enemies[i].getX());
                boom.setY(enemies[i].getY());

                enemies[i].setX(-400);
                life -= 1;
            }


            for (Laser l : player.getLasers()) {

                if (Rect.intersects(l.getDetectCollision(),enemies[i].getDetectCollision())) {

                    enemies[i].hit();

                    if(enemies[i].getHP() <= 0){
                        killedEnemysound.start();
                        enemies[i].setHP(3);
                    }
                    l.destroy();
                }
            }

        }

        if (Rect.intersects(player.getDetectCollision(), meteor.getDetectCollision())) {
            boom.setX(meteor.getX());
            boom.setY(meteor.getY());

            meteor.setX(-400);

            life -= 1;
        }
                if(life == 0){
                    playing = false;
                    isGameOver = true;

                    gameOnsound.stop();

                    gameOversound.start();

                    for(int j=0;j<4;j++){
                        if(highScore[j]<score){
                            highScore[j] = score;
                            break;
                        }
                    }

                    SharedPreferences.Editor e = sharedPreferences.edit();
                    for(int k=0;k<4;k++){
                        int l = k+1;
                        e.putInt("score"+l,highScore[k]);
                    }
                    e.apply();
                }



    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);

            paint.setColor(Color.WHITE);

            for (Star s : stars) {
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }

            paint.setTextSize(30);
            canvas.drawText("Score:"+score,100,50,paint);

            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);

            for (int i = 0; i < enemyCount; i++) {
                canvas.drawBitmap(
                        enemies[i].getBitmap(),
                        enemies[i].getX(),
                        enemies[i].getY(),
                        paint
                );
            }

            canvas.drawBitmap(
                    boom.getBitmap(),
                    boom.getX(),
                    boom.getY(),
                    paint
            );

            canvas.drawBitmap(
                    meteor.getBitmap(),
                    meteor.getX(),
                    meteor.getY(),
                    paint
            );

            for (Laser l : player.getLasers()) {
                canvas.drawBitmap(l.getBitmap(), l.getX(), l.getY(), paint);
            }


            if(isGameOver){
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);

                int yPos=(int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Game Over",canvas.getWidth()/2,yPos,paint);
            }

            drawLives();

            surfaceHolder.unlockCanvasAndPost(canvas);

        }
    }

    public void control() {

        try {
            if (counter == 10000) {
                counter = 0;
            }
            gameThread.sleep(20);
            counter += 20;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
            System.out.println("Pause");
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    void drawLives(){

        int j = screenX/4;

        for (int i= 0; i < life; i++) {

            canvas.drawBitmap(player.getBitmapLives(), (screenX-(screenX/2)+j),  screenY/30, paint);
            j += 60;
        }
    }

    public void steerLeft(float speed) {

        player.steerLeft(speed);
    }

    public void steerRight(float speed) {

        player.steerRight(speed);
    }

    public void stay() {

        player.stay();
    }

    public static void stopMusic(){
        gameOnsound.stop();
    }


    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if(isGameOver){
            if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                context.startActivity(new Intent(context,MainActivity.class));
            }
        }
        return true;
    }
}