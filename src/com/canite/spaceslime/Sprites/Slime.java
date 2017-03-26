package com.canite.spaceslime.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.canite.spaceslime.Bodies.SpriteBody;
import com.canite.spaceslime.Screens.PlayScreen;
import com.canite.spaceslime.SpaceSlime;
import com.canite.spaceslime.Tools.Manifold;
import com.canite.spaceslime.Types.Circle;
import com.canite.spaceslime.Types.Shape;
import com.canite.spaceslime.World.World;

import java.util.HashMap;

/**
 * Created by Austin on 2/7/2016.
 */
public class Slime extends GameObject {
    public enum State {FALLING, JUMPING, LANDING, STANDING, RUNNING, HOOKING}
    public State currentState, previousState;
    private HashMap<State, Animation> animations;
    private World world;
    private PlayScreen screen;
    private float stateTimer;
    private Vector2 x_speed;
    private Vector2 y_speed;
    private boolean canHook;
    public boolean hooked;
    private Vector2 normal_vector = new Vector2();
    private Hook hook;

    public Slime(World world, PlayScreen screen, int startX, int startY) {
        this.world = world;
        this.screen = screen;
        stateTimer = 0;
        currentState = State.STANDING;
        previousState = State.STANDING;
        x_speed = new Vector2(1200.0f, 0.0f);
        y_speed = new Vector2(0.0f, 400.0f);
        canJump = true;
        canHook = true;
        hooked = false;

        /* Initialize sprites */
        animations = new HashMap<State, Animation>();

        Array<TextureRegion> frames = new Array<TextureRegion>();
        int numFrames = 1;
        int frameSize = 32;
        float animationSpeed = 0.1f;

        for (int i = 0; i < numFrames; i++) {
            TextureRegion frameRegion;
            frameRegion = new TextureRegion(screen.getAtlas().findRegion("slime"), i*frameSize, 0, frameSize, frameSize);
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
        setRegion((TextureRegion) animations.get(State.STANDING).getKeyFrame(stateTimer, false));

        Shape shape = new Circle(frameSize /2, new Vector2(startX + frameSize /2, startY + frameSize /2));
        body = new SpriteBody(this, shape, new Vector2(startX + frameSize /2, startY + frameSize /2), 0.0f, 0.01f,
                              0.4f, new Vector2(0, 0), new Vector2(0, 0), 1.0f);
        setOriginCenter();
    }

    private void jump() {
        if (onGround) {
            body.applyImpulse(y_speed, 1.0f, new Vector2(0.0f, 0.0f));
        }
    }

    private void hook() {
        hook = new Hook(world, screen, this, body.position.x + ((Circle)body.shape).radius,
                body.position.y + ((Circle)body.shape).radius, 600.0f, 600.0f);
        world.insertObject(hook);
    }

    public void removeHook() {
        world.removeObject(hook);
        hook = null;
        canHook = true;
        hooked = false;
    }

    public void handleInput(PlayScreen.TouchInfo touchInfo) {
        if (touchInfo.touched) {
            // 1/6 of the screen width
            if (touchInfo.touchX < SpaceSlime.V_WIDTH / (6 * SpaceSlime.PPM)) {
                body.applyForce(x_speed.cpy().scl(-1.0f));
                moving = true;
                touchInfo.type = "move";
            } else if (touchInfo.touchX < SpaceSlime.V_WIDTH / (3 * SpaceSlime.PPM)) {
                body.applyForce(x_speed);
                moving = true;
                touchInfo.type = "move";
            } else {
                if (touchInfo.type == "") {
                    if (onGround) {
                        if (canJump) {
                            canJump = false;
                            jump();
                            touchInfo.type = "jump";
                        }
                    } else {
                        if (canHook) {
                            canHook = false;
                            hook();
                            touchInfo.type = "hook";
                        }
                    }
                }
            }
        } else {
            if (touchInfo.type.equals("move")) {
                moving = false;
                touchInfo.type = "";
            } else if (touchInfo.type.equals("jump")) {
                /*
                if (yVel > 0) {
                    yVel *= 0.6;
                }
                */
                touchInfo.type = "";
                canJump = true;
            } else if (touchInfo.type.equals("hook")) {
                removeHook();
                touchInfo.type = "";
            }
        }
    }

    @Override
    public void update(float dt) {
        if (hooked) {
            // Subtract the vector component pointing away from the hook from our velocity
            normal_vector.set((float)Math.cos(hook.angle), (float)Math.sin(hook.angle)).nor();
            float normal_dot = normal_vector.dot(body.velocity);
            normal_vector.scl(normal_dot);
            body.velocity.sub(normal_vector);
        }
    }

    @Override
    public void updatePosition() {
        setPosition(body.position.x - 16, body.position.y - 16);
    }

    @Override
    public void collided(Manifold manifold) {

    }

    public void printStats() {
        //Gdx.app.log("player", Float.toString(yVel));
    }
}
