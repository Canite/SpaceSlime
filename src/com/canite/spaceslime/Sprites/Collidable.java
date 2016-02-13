package com.canite.spaceslime.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.canite.spaceslime.Bodies.SpriteBody;
import com.canite.spaceslime.Types.ColBody;

/**
 * Created by austin on 2/10/16.
 */
public abstract class Collidable extends GameObject {
    public SpriteBody horiBody = new SpriteBody(getBoundingRectangle());
    public SpriteBody vertBody = horiBody;
    /* int body is 0 for horizontal collision, 1 for vertical collision */
    public abstract void collide(ColBody type, Collidable colObj);
}
