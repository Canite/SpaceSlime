package com.canite.spaceslime.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
    private float stateTimer, xSpeed, ySpeed;

    public Slime(World world, PlayScreen screen, int startX, int startY) {
        this.world = world;
        this.screen = screen;
        xVel = 0;
        yVel = 0;
        xAccel = 0;
        yAccel = 0;
        stateTimer = 0;
        xSpeed = 40.0f;
        ySpeed = 800.0f;
        maxXVel = 600.0f;
        maxYVel = 600.0f;
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

        vertTopBody = new SpriteBody(new Rectangle(getX(), getY(), getWidth() / 2, getHeight() / 2), (getWidth() / 4), getHeight() / 2);
        vertBotBody = new SpriteBody(new Rectangle(getX(), getY(), getWidth() / 2, getHeight() / 2), (getWidth() / 4), 0);
        horiLeftBody = new SpriteBody(new Rectangle(getX(), getY(), getWidth() / 2, getHeight() / 2), 0, (getHeight() /4));
        horiRightBody = new SpriteBody(new Rectangle(getX(), getY(), getWidth() / 2, getHeight() / 2), getWidth() / 2, (getHeight() / 4));
    }

    private void move(float accel) {
        xAccel = accel;
    }

    private void stop() {
        xAccel = 0.0f;
        // xVel = 0.0f;
    }

    private void jump() {
        if (onGround) {
            yVel = ySpeed;
        }
    }

    private void hook() {
        Hook hook = new Hook(world, screen, getX() + 16, getY() + 16, 50.0f, 600.0f);
        world.insertDynamic(hook);
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
        /* Movement! */
        /* X movement */
        printStats();
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            move(-1 * xSpeed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            move(xSpeed);
        }
        if ((!Gdx.input.isKeyPressed(Input.Keys.A) && !Gdx.input.isKeyPressed(Input.Keys.D))
                || (Gdx.input.isKeyPressed(Input.Keys.A) && Gdx.input.isKeyPressed(Input.Keys.D))) {
            stop();
            if (onGround)
                xVel *= 0.75f;
            else
                xVel *= 0.75f;
        }
        if (Math.abs(xVel) < 1) {
           xVel = 0;
        }

        /* Y movement */
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            jump();
        }

        /* Hookshot */
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            hook();
        }

        if (yVel == 0 && xVel == 0) {
            checkCollisions = false;
        } else {
            checkCollisions = true;
        }
    }

    @Override
    public void collide(ColBody type, Collidable colObj) {
        if (colObj instanceof Ground) {
            switch(type) {
                case RIGHTHORI:
                    /* Moving right */
                    /* Move to the difference between collision overlap */
                    setX(colObj.horiLeftBody.colBox.x - (horiRightBody.colBox.width + horiRightBody.xOffset) /*- 1*/);
                    xVel = Math.min(0.0f, xVel);
                    xAccel = Math.min(0.0f, xAccel);
                    break;

                case LEFTHORI:
                    /* Moving left */
                    /* Move to the difference between collision overlap */
                    setX(colObj.horiRightBody.colBox.x + colObj.horiRightBody.colBox.width + horiLeftBody.xOffset /*+ 1*/);
                    xVel = Math.max(0.0f, xVel);
                    xAccel = Math.max(0.0f, xAccel);
                    break;

                case BOTVERT:
                    /* Vertical collision */
                    /* Moving down */
                    setY(colObj.vertTopBody.colBox.y + colObj.vertTopBody.colBox.height + vertBotBody.yOffset /*+ 1*/);
                    yVel = Math.max(0.0f, yVel);
                    yAccel = Math.max(0.0f, yAccel);
                    break;

                case TOPVERT:
                    /* Vertical collision */
                    /* Moving up */
                    setY(colObj.vertBotBody.colBox.y - (vertTopBody.colBox.height + vertTopBody.yOffset) /*- 1*/);
                    yVel = Math.min(0.0f, yVel);
                    yAccel = Math.min(0.0f, yAccel);
                    break;
            }
        }
        updateColBox();
    }

    public void printStats() {
        //Gdx.app.log("player", "x: " + Float.toString(getX()) + " y: " + Float.toString(getY()) + " xvel: " + Float.toString(xVel) + " yvel: " + Float.toString(yVel));
        Gdx.app.log("player", Float.toString(yVel));
    }
}
