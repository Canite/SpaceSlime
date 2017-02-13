package com.canite.spaceslime.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by austin on 2/12/16.
 */
public abstract class GameObject extends Sprite {
    public float xVel, yVel, prevX, prevY, xAccel, yAccel, maxXVel, maxYVel;
    public double speed, maxSpeed;
    public boolean moving = false;
    public boolean canJump = false;
    public boolean onGround = false;
    public boolean applyGravity = true;
    public abstract void update(float dt);
    public void applyForce(float direction, float magnitude) {
        double rads = Math.toRadians(direction);
        double xf, yf;
        xf = Math.cos(rads) * magnitude;
        yf = Math.sin(rads) * magnitude;
        xAccel += xf;
        yAccel += yf;
    }
}
