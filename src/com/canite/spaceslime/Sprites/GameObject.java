package com.canite.spaceslime.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.canite.spaceslime.Bodies.SpriteBody;
import com.canite.spaceslime.Tools.Manifold;

/**
 * Created by austin on 2/12/16.
 */
public abstract class GameObject extends Sprite {
    public boolean moving = false;
    public boolean canJump = false;
    public boolean onGround = false;
    public int onGroundGrace = 10;
    public boolean applyGravity = true;
    public SpriteBody body;
    public boolean isStatic = false;
    public abstract void update(float dt);
    public abstract void updatePosition();
    public abstract void updateRotation();
    public abstract void collided(Manifold manifold);
}
