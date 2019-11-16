package com.vicomo.game.retro;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Obstacle implements GameObject{
    private Rect rectangle, rectangle2;
    private int color;

    public Obstacle(int rectHeight, int color, int startX,int startY, int playerGap) {
        this.color = color;
        this.rectangle = new Rect(0, startY, startX, startY + rectHeight);
        this.rectangle2 = new Rect(startX + playerGap, startY, Constants.SCREEN_WIDTH, startY + rectHeight);
    }

    public boolean playerCollide(RectPlayer player){
        if(
                this.rectangle.contains(player.getRectangle().left, player.getRectangle().top) ||
                this.rectangle.contains(player.getRectangle().right, player.getRectangle().top) ||
                this.rectangle.contains(player.getRectangle().left, player.getRectangle().bottom) ||
                this.rectangle.contains(player.getRectangle().right, player.getRectangle().bottom)
        ){
            return true;
        }
        return false;
    }

    public Rect getRectangle() {
        return rectangle;
    }

    public void incrementY(float y){
        rectangle.top += y;
        rectangle.bottom += y;
        rectangle2.top += y;
        rectangle2.bottom += y;

    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);
        canvas.drawRect(rectangle2, paint);
    }

    @Override
    public void update() {

    }
}
