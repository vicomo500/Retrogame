package com.vicomo.game.retro;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

public class GamePlayScene implements Scene {
    private RectPlayer player;
    private Point playerPoint;
    private ObstacleManager obstacleManager;
    private boolean movingPlayer = false;
    private boolean gameOver = false;
    private long gameOverTime;
    private OrientationData orientationData;
    private long frameTime = 0;

    private static Rect r = new Rect();
    public GamePlayScene(){
        SceneManager.ACTIVE_SCENE = 0;
        player = new RectPlayer(
                new Rect(100,100,200,200),
                Color.rgb(255,0,0)
        );
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4 );
        player.update(playerPoint);
        obstacleManager = new ObstacleManager(200, 350, 75, Color.BLACK);
        orientationData = new OrientationData();
        orientationData.register();
        frameTime = System.currentTimeMillis();
    }

    @Override
    public void update() {
        if(!gameOver){
            if(frameTime < Constants.INIT_TIME)
                frameTime = Constants.INIT_TIME;
            int elapsedTime = (int) (System.currentTimeMillis() - frameTime);
            frameTime = System.currentTimeMillis();
            if(orientationData.getOrientation() != null &&
                    orientationData.getStartOrientation() != null){
                float pitch = orientationData.getOrientation()[1] - orientationData.getStartOrientation()[1];
                float roll = orientationData.getOrientation()[2] - orientationData.getStartOrientation()[2];
                float xSpeed  = 2 * roll * Constants.SCREEN_WIDTH/1000f;
                float ySpeed  = pitch * Constants.SCREEN_HEIGHT/1000f;
                playerPoint.x += Math.abs(xSpeed*elapsedTime) > 5 ? xSpeed*elapsedTime : 0;
                playerPoint.y -= Math.abs(ySpeed*elapsedTime) > 5 ? ySpeed*elapsedTime : 0;
            }
            if(playerPoint.x < 0)
                playerPoint.x = 0;
            else if(playerPoint.x > Constants.SCREEN_WIDTH)
                playerPoint.x = Constants.SCREEN_WIDTH;
            if(playerPoint.y < 0)
                playerPoint.y = 0;
            else if(playerPoint.y > Constants.SCREEN_HEIGHT)
                playerPoint.y = Constants.SCREEN_HEIGHT;
            player.update(playerPoint);
            obstacleManager.update();
            if(obstacleManager.playerCollide(player)){
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        player.draw(canvas);
        obstacleManager.draw(canvas);
        if(gameOver){
            Paint paint = new Paint();
            paint.setTextSize(100f);
            paint.setColor(Color.MAGENTA);
            drawCenterText(canvas, paint, "Game Over");
        }
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;
    }

    @Override
    public void receiveTouch(MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!gameOver && player.getRectangle().contains((int)motionEvent.getX(), (int) motionEvent.getY())){
                    movingPlayer = true;
                }
                if(gameOver && (System.currentTimeMillis() - gameOverTime) >= 2000){
                    reset();

                    orientationData.newGame();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(movingPlayer && !gameOver){
                    playerPoint.set((int)motionEvent.getX(), (int) motionEvent.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                movingPlayer = false;
                break;
        }
    }

    public void reset(){
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4 );
        player.update(playerPoint);
        obstacleManager = new ObstacleManager(200, 350, 75, Color.BLACK);
        movingPlayer = false;
    }

    private static void drawCenterText(Canvas canvas, Paint paint, String text){
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0 , text.length(), r);
        float x = cWidth/2f - r.width()/2f - r.left;
        float y = cHeight/2f - r.height()/2f - r.bottom;
        canvas.drawText(text, x, y, paint);
    }
}
