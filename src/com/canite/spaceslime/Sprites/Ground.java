package com.canite.spaceslime.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.canite.spaceslime.Bodies.SpriteBody;
import com.canite.spaceslime.Types.ColBody;

/**
 * Created by Austin on 2/3/2016.
 */
public class Ground extends Collidable {
    public Ground(Rectangle bounds) {
        vertBody = new SpriteBody(bounds);
        horiBody = vertBody;
    }

    @Override
    public void collide(ColBody type, Collidable colObj) {
        /* Does not call its own collide function */
    }

    @Override
    public void update(float dt) {
        /* Static object, does not update */
    }
}
