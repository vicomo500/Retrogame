package com.vicomo.game.retro;

import android.graphics.Canvas;
import android.graphics.Rect;

public class Obstacle implements GameObject{
    private Rect rectangle;
    private int color;

    public Obstacle(Rect rectangle, int color) {
        this.rectangle = rectangle;
        this.color = color;
    }

    public boolean playerCollide(RectPlayer player){
        if(this.rectangle.contains(player.getRectangle().left, player.getRectangle().top) ||
                this.rectangle.contains(player.getRectangle().left, player.getRectangle().top)
        ){

        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void update() {

    }
}
