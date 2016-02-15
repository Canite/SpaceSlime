package com.canite.spaceslime.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.canite.spaceslime.Bodies.SpriteBody;
import com.canite.spaceslime.Screens.PlayScreen;
import com.canite.spaceslime.Types.ColBody;
import com.canite.spaceslime.World.World;

import java.util.HashMap;

/**
 * Created by Austin on 2/7/2016.
 */
public class Slime extends Collidable {
    public enum State {FALLING, JUMPING, LANDING, STANDING, RUNNING, HOOKING}
    public State currentState, previousState;
    private HashMap<State, Animation> animations;
    private World world;
    private PlayScreen screen;
    private boolean onGround = true;

    public float xVel, yVel, xAccel, yAccel;

    public Slime(World world, PlayScreen screen, int startX, int startY) {
        this.world = world;
        this.screen = screen;
        setX(startX);
        setY(startY);
        xVel = 0;
        yVel = 0;
        xAccel = 0;
        yAccel = 0;

        vertBody = new SpriteBody(new Rectangle(getX() + getWidth() / 4, getY(), getWidth() / 2, getHeight()));
        horiBody = new SpriteBody(new Rectangle(getX(), getY() + getHeight() / 4, getWidth(), getHeight() / 2));

        /* Initialize sprites */

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void collide(ColBody type, Collidable colObj) {
        if (colObj instanceof Ground) {
            switch(type) {
                case HORIZONTAL:
                    /* Horizontal collision */
                    /* Moving right */
                    if (xVel > 0) {
                        /* Move to the difference between collision overlap */
                        setX(colObj.horiBody.colBox.x - horiBody.colBox.width);
                    }
                    /* Moving left */
                    else if (xVel < 0) {
                        /* Move to the difference between collision overlap */
                        setX(colObj.horiBody.colBox.x + colObj.horiBody.colBox.width);
                    }
                    xVel = 0;
                    xAccel = 0;
                    break;

                case VERTICAL:
                    /* Vertical collision */
                    /* Moving down */
                    if (yVel > 0) {
                        onGround = true;
                        setY(colObj.vertBody.colBox.y - vertBody.colBox.height);
                    }
                    /* Moving up */
                    else if (yVel < 0) {
                        setY(colObj.vertBody.colBox.y + colObj.vertBody.colBox.width);
                    }
                    yVel = 0;
                    yAccel = 0;
                    break;

            }
        }
    }
}
