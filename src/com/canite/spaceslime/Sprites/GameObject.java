package com.canite.spaceslime.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by austin on 2/12/16.
 */
public abstract class GameObject extends Sprite {
    public float xVel, yVel, xAccel, yAccel, maxXVel, maxYVel;
    public boolean onGround = false;
    public boolean applyGravity = true;
    public abstract void update(float dt);
}
