package com.canite.spaceslime.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.canite.spaceslime.Bodies.SpriteBody;
import com.canite.spaceslime.Screens.PlayScreen;
import com.canite.spaceslime.SpaceSlime;
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
    private float stateTimer, maxXVel, maxYVel;

    public Slime(World world, PlayScreen screen, int startX, int startY) {
        this.world = world;
        this.screen = screen;
        xVel = 0;
        yVel = 0;
        xAccel = 0;
        yAccel = 0;
        stateTimer = 0;
        maxXVel = 50.0f;
        maxYVel = 50.0f;
        currentState = State.STANDING;
        previousState = State.STANDING;

        /* Initialize sprites */
        animations = new HashMap<State, Animation>();

        Array<TextureRegion> frames = new Array<TextureRegion>();
        int numFrames = 1;
        int frameSize = 64;
        float animationSpeed = 0.1f;

        for (int i = 0; i < numFrames; i++) {
            TextureRegion frameRegion;
            frameRegion = new TextureRegion(screen.getAtlas().findRegion("SSpack" /*slimeStand*/), i*frameSize, 0, frameSize, frameSize);
            PlayScreen.fixBleeding(frameRegion);
            frames.add(frameRegion);
        }

        animations.put(State.STANDING, new Animation(animationSpeed, frames));
        frames.clear();

        /* Running animation
        numFrames = 1;
        frameSize = 64;
        animationSpeed = 0.1f;

        for (int i = 0; i < numFrames; i++) {
            TextureRegion frameRegion;
            frameRegion = new TextureRegion(screen.getAtlas().findRegion("slimeRun"), i*frameSize, 0, frameSize, frameSize);
            PlayScreen.fixBleeding(frameRegion);
            frames.add(frameRegion);
        }

        animations.put(State.RUNNING, new Animation(animationSpeed, frames));
        frames.clear();
        */

        /* Jumping animation
        numFrames = 1;
        frameSize = 64;
        animationSpeed = 0.1f;

        for (int i = 0; i < numFrames; i++) {
            TextureRegion frameRegion;
            frameRegion = new TextureRegion(screen.getAtlas().findRegion("slimeJump"), i*frameSize, 0, frameSize, frameSize);
            PlayScreen.fixBleeding(frameRegion);
            frames.add(frameRegion);
        }

        animations.put(State.JUMPING, new Animation(animationSpeed, frames));
        frames.clear();
        */

        /* Falling animation
        numFrames = 1;
        frameSize = 64;
        animationSpeed = 0.1f;

        for (int i = 0; i < numFrames; i++) {
            TextureRegion frameRegion;
            frameRegion = new TextureRegion(screen.getAtlas().findRegion("slimeFall"), i*frameSize, 0, frameSize, frameSize);
            PlayScreen.fixBleeding(frameRegion);
            frames.add(frameRegion);
        }

        animations.put(State.FALLING, new Animation(animationSpeed, frames));
        frames.clear();
        */

        /* Hooking animation
        numFrames = 1;
        frameSize = 64;
        animationSpeed = 0.1f;

        for (int i = 0; i < numFrames; i++) {
            TextureRegion frameRegion;
            frameRegion = new TextureRegion(screen.getAtlas().findRegion("slimeHook"), i*frameSize, 0, frameSize, frameSize);
            PlayScreen.fixBleeding(frameRegion);
            frames.add(frameRegion);
        }

        animations.put(State.HOOKING, new Animation(animationSpeed, frames));
        frames.clear();
        */

        /* Landing animation
        numFrames = 1;
        frameSize = 64;
        animationSpeed = 0.1f;

        for (int i = 0; i < numFrames; i++) {
            TextureRegion frameRegion;
            frameRegion = new TextureRegion(screen.getAtlas().findRegion("slimeLand"), i*frameSize, 0, frameSize, frameSize);
            PlayScreen.fixBleeding(frameRegion);
            frames.add(frameRegion);
        }

        animations.put(State.LANDING, new Animation(animationSpeed, frames));
        frames.clear();
        */

        setBounds(startX, startY, frameSize / SpaceSlime.PPM, frameSize / SpaceSlime.PPM);
        setRegion(animations.get(State.STANDING).getKeyFrame(stateTimer, false));

        Gdx.app.log("player", Float.toString(getWidth()));
        vertBody = new SpriteBody(new Rectangle(getX(), getY(), getWidth() / 4, getHeight()), (getWidth() * 3) / 8, 0);
        horiBody = new SpriteBody(new Rectangle(getX(), getY(), getWidth(), getHeight() / 4), 0, (getHeight() * 3) / 8);
    }

    public void move(float accel) {
        xAccel = accel;
    }

    public void stop() {
        xAccel = 0.0f;
        // xVel = 0.0f;
    }

    public void jump() {
        if (onGround) {
            yVel = 400.0f;
        }
    }

    public void handleInput(PlayScreen.TouchInfo touchInfo) {
        if (touchInfo.touched) {
            // 1/6 of the screen width
            if (touchInfo.touchX < SpaceSlime.V_WIDTH / (6 * SpaceSlime.PPM)) {
                move(-20.0f);
                touchInfo.type = "move";
            } else if (touchInfo.touchX < SpaceSlime.V_WIDTH / (3 * SpaceSlime.PPM)) {
                move(20.0f);
                touchInfo.type = "move";
            } else {
                jump();
                touchInfo.type = "jump";
            }
        } else {
            if (touchInfo.type.equals("move")) {
                stop();
                touchInfo.type = "";
            } else if (touchInfo.type.equals("jump") && yVel > 0) {
                yVel *= 0.8;
                if (yVel < 0.01) {
                    yVel = 0;
                }
            }
        }
    }

    @Override
    public void update(float dt) {
        /* Control velocity */
        xVel *= 0.8f;
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
                        setX(colObj.horiBody.colBox.x - horiBody.colBox.width - 1);
                    }
                    /* Moving left */
                    else if (xVel < 0) {
                        /* Move to the difference between collision overlap */
                        setX(colObj.horiBody.colBox.x + colObj.horiBody.colBox.width + 1);
                    }
                    xVel = 0;
                    xAccel = 0;
                    break;

                case VERTICAL:
                    //Gdx.app.log("player", "collision");
                    /* Vertical collision */
                    /* Moving down */
                    if (yVel < 0) {
                        onGround = true;
                        setY(colObj.vertBody.colBox.y + vertBody.colBox.height + 1);
                    }
                    /* Moving up */
                    else if (yVel > 0) {
                        setY(colObj.vertBody.colBox.y - colObj.vertBody.colBox.height - 1);
                    }
                    yVel = 0;
                    yAccel = 0;
                    break;
            }
        }
        updateColBox();
    }

    public void printStats() {
        //Gdx.app.log("player", "x: " + Float.toString(getX()) + " y: " + Float.toString(getY()) + " xvel: " + Float.toString(xVel) + " yvel: " + Float.toString(yVel));
        Gdx.app.log("player", Float.toString(vertBody.colBox.y + vertBody.colBox.height));
    }
}
