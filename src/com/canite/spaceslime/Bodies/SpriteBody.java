package com.canite.spaceslime.Bodies;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Austin on 2/7/2016.
 */
public class SpriteBody {
    public Rectangle colBox;
    public SpriteBody(int x, int y, int w, int h) {
        colBox = new Rectangle(x,y,w,h);
    }
}
