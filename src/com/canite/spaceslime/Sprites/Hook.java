package com.canite.spaceslime.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.canite.spaceslime.Bodies.SpriteBody;
import com.canite.spaceslime.Screens.PlayScreen;
import com.canite.spaceslime.SpaceSlime;
import com.canite.spaceslime.Types.ColBody;
import com.canite.spaceslime.World.World;

/**
 * Created by austin on 4/10/16.
 */
public class Hook extends Collidable {
    private World world;
    private PlayScreen screen;
    private Animation animation;
    private float stateTimer;
    private float distance;
    private float maxDistance;
    private Slime player;
    private boolean hooked;
    public float length;
    public double angle;

    public Hook(World world, PlayScreen screen, Slime player, float startX, float startY, float startXVel, float startYVel) {
        this.world = world;
        this.screen = screen;
        this.player = player;
        stateTimer = 0;
        distance = 0;
        maxDistance = 2000.0f;
        xVel = startXVel;
        yVel = startYVel;
        xAccel = 0;
        yAccel = 0;
        maxXVel = 600.0f;
        maxYVel = 600.0f;
        applyGravity = false;
        hooked = false;

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

        animation = new Animation(animationSpeed, frames);

        setBounds(startX, startY, frameSize / SpaceSlime.PPM, frameSize / SpaceSlime.PPM);
        setRegion(animation.getKeyFrame(stateTimer));

        vertTopBody = new SpriteBody(new Rectangle(getX(), getY(), 4, 2), (getWidth() / 4), getHeight() / 2);
        vertBotBody = new SpriteBody(new Rectangle(getX(), getY(), 4, 2), (getWidth() / 4), 0);
        horiLeftBody = new SpriteBody(new Rectangle(getX(), getY(), 2, 4), 0, (getHeight() /4));
        horiRightBody = new SpriteBody(new Rectangle(getX(), getY(), 2, 4), getWidth() / 2, (getHeight() / 4));
    }

    @Override
    public void collide(ColBody type, Collidable colObj) {
        if (colObj instanceof Ground) {
            switch(type) {
                case RIGHTHORI:
                    /* Moving right */
                    /* Move to the difference between collision overlap */
                    setX(colObj.horiLeftBody.colBox.x - (horiRightBody.colBox.width + horiRightBody.xOffset) /*- 1*/);
                    break;

                case LEFTHORI:
                    /* Moving left */
                    /* Move to the difference between collision overlap */
                    setX(colObj.horiRightBody.colBox.x + colObj.horiRightBody.colBox.width + horiLeftBody.xOffset /*+ 1*/);
                    break;

                case BOTVERT:
                    /* Vertical collision */
                    /* Moving down */
                    setY(colObj.vertTopBody.colBox.y + colObj.vertTopBody.colBox.height + vertBotBody.yOffset /*+ 1*/);
                    break;

                case TOPVERT:
                    /* Vertical collision */
                    /* Moving up */
                    setY(colObj.vertBotBody.colBox.y - (vertTopBody.colBox.height + vertTopBody.yOffset) /*- 1*/);
                    break;
            }
            xVel = 0;
            yVel = 0;
            xAccel = 0;
            yAccel = 0;
            checkCollisions = false;
            player.hooked = true;
            hooked = true;
            float xDiff = getX() - player.getX();
            float yDiff = getY() - player.getY();
            length = (float)Math.sqrt((xDiff * xDiff + yDiff * yDiff));
        }
        updateColBox();
    }

    @Override
    public void update(float dt) {
        if (!hooked) {
            distance += Math.sqrt((double) (Math.round((xVel * xVel + yVel * yVel) * dt)));
            Gdx.app.log("Hook", Float.toString(distance));
            if (distance > maxDistance) {
                player.removeHook();
            }
        } else {
            float xDiff = player.getX() - getX();
            float yDiff = player.getY() - getY();
            angle = Math.toDegrees(Math.atan2(yDiff, xDiff));
        }
    }
}
