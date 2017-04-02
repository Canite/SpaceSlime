package com.canite.spaceslime.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
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
    private HashMap<State, Animation> animations;
    private World world;
    private PlayScreen screen;
    private float stateTimer;
    private Vector2 x_speed;
    private Vector2 y_speed;
    private float max_xSpeed;
    private float max_ySpeed;
    private boolean canHook;
    private Vector2 normal_vector = new Vector2();
    private Hook hook;

    public enum State {FALLING, JUMPING, LANDING, STANDING, RUNNING, HOOKING}
    public State currentState, previousState;
    public boolean hooked;

    public Slime(World world, PlayScreen screen, int startX, int startY) {
        this.world = world;
        this.screen = screen;
        stateTimer = 0;
        currentState = State.STANDING;
        previousState = State.STANDING;
        x_speed = new Vector2(4500.0f, 0.0f);
        y_speed = new Vector2(0.0f, 400.0f);
        max_xSpeed = 700.0f;
        canJump = true;
        canHook = true;
        hooked = false;
        isStatic = false;

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
        body.applyImpulse(y_speed, 1.0f, new Vector2(0.0f, 0.0f));
    }

    private void hook(float angle) {
        Vector2 hook_velocity = new Vector2(600.0f, 600.0f);
        hook_velocity.setAngle(angle);
        hook = new Hook(world, screen, this, body.position.x,
                body.position.y + ((Circle)body.shape).radius, hook_velocity);
        world.insertObject(hook);
    }

    public void removeHook() {
        world.removeObject(hook);
        hook = null;
        hooked = false;
    }

    public void handleInput(PlayScreen.TouchInfo touchInfo) {
        if (touchInfo.touched) {
            // 1/6 of the screen width
            if (touchInfo.touchX < Gdx.graphics.getWidth() / 6 &&
                    touchInfo.touchY > Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 6) {

                if (body.velocity.x > -max_xSpeed) {
                    body.applyForce(x_speed.cpy().scl(-1.0f));
                }
                moving = true;
                touchInfo.type = "move";
            } else if (touchInfo.touchX < Gdx.graphics.getWidth()  / 3 &&
                    touchInfo.touchX > Gdx.graphics.getWidth() / 6 &&
                    touchInfo.touchY > Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 6) {

                if (body.velocity.x < max_xSpeed) {
                    body.applyForce(x_speed);
                }
                moving = true;
                touchInfo.type = "move";
            } else if (touchInfo.touchX > (Gdx.graphics.getWidth() * 5) / 6 &&
                       touchInfo.touchY > Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 6) {
                if (touchInfo.type == "") {
                    if (canJump) {
                        canJump = false;
                        jump();
                        touchInfo.type = "jump";
                    }
                }
            } else {
                if (canHook) {
                    float radius = (Gdx.graphics.getWidth() / 8) * (Gdx.graphics.getWidth() / 8);
                    Vector2 center = new Vector2((Gdx.graphics.getWidth() * 7) / 8, (Gdx.graphics.getHeight() * 5) / 6);
                    if (touchInfo.touchY < center.y && center.dst2(touchInfo.touchX, touchInfo.touchY) < radius) {
                        canHook = false;
                        float angle = MathUtils.clamp(180.0f - center.sub(touchInfo.touchX, touchInfo.touchY).angle(), 45.0f, 135.0f);
                        hook(angle);
                        touchInfo.type = "hook";
                    }
                }
            }
        } else {
            if (touchInfo.type.equals("move")) {
                moving = false;
                touchInfo.type = "";
            } else if (touchInfo.type.equals("jump")) {
                // Slow the jump if we're still going up
                if (body.velocity.y > 0) {
                    body.velocity.scl(1.0f, 0.6f);
                }
                touchInfo.type = "";
                //canJump = true;
            } else if (touchInfo.type.equals("hook")) {
                removeHook();
                canHook = true;
                touchInfo.type = "";
            }
        }
    }

    @Override
    public void update(float dt) {
        //Gdx.app.log("player", body.velocity.toString());
        if (hooked) {
            float angle = hook.body.position.cpy().sub(body.position).angleRad();
            // Subtract the vector component pointing away from the hook from our velocity
            normal_vector.set((float)Math.cos(angle), (float)Math.sin(angle)).nor();
            float normal_dot = normal_vector.dot(body.velocity);
            //normal_vector.scl(normal_dot);
            body.velocity.sub(normal_vector.cpy().scl(normal_dot));
            if (body.position.dst2(hook.body.position) > hook.minDistance) {
                body.velocity.add(normal_vector.scl(8.0f));
            }
        }
    }

    @Override
    public void updatePosition() {
        setPosition(body.shape.position.x - ((Circle)body.shape).radius, body.shape.position.y - ((Circle)body.shape).radius);
    }

    @Override
    public void updateRotation() {
        setRotation(MathUtils.radiansToDegrees * body.rotation);
    }

    @Override
    public void collided(Manifold manifold) {
        if ((manifold.A.parent instanceof Ground || manifold.B.parent instanceof Ground) && !moving) {
            body.angular_velocity *= 0.95f;
        }
    }

    public void printStats() {
        //Gdx.app.log("player", Float.toString(yVel));
    }
}
