package com.canite.spaceslime.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Austin on 2/3/2016.
 */
public class Ground extends Sprite {
    public Rectangle bounds;
    public Ground(Rectangle bounds) {
        this.bounds = bounds;
    }
}
