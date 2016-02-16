package com.canite.spaceslime.Bodies;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Austin on 2/7/2016.
 */
public class SpriteBody {
    public Rectangle colBox;
    public float xOffset, yOffset;
    public SpriteBody(Rectangle rect, float xOffset, float yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        colBox = rect;
        colBox.setX(colBox.getX() + xOffset);
        colBox.setY(colBox.getY() + yOffset);
    }
}
