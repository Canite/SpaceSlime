package com.canite.spaceslime.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    public Hook(World world, PlayScreen screen, float startX, float startY, float startXVel, float startYVel) {
        this.world = world;
        this.screen = screen;
        stateTimer = 0;
        xVel = startXVel;
        yVel = startYVel;
        xAccel = 0;
        yAccel = 0;
        maxXVel = 600.0f;
        maxYVel = 600.0f;
        applyGravity = false;

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

        vertTopBody = new SpriteBody(new Rectangle(getX(), getY(), 4, 2), (4 / 2) - 2, (4 / 2) - 2);
        vertBotBody = new SpriteBody(new Rectangle(getX(), getY(), 4, 2), (4 / 2) - 2, (4 / 2));
        horiLeftBody = new SpriteBody(new Rectangle(getX(), getY(), 2, 4), (4 / 2) - 2, (4 / 2) - 2);
        horiRightBody = new SpriteBody(new Rectangle(getX(), getY(), 2, 4), (4 / 2), (4 / 2) - 2);
    }

    @Override
    public void collide(ColBody type, Collidable colObj) {
        if (colObj instanceof Ground) {
            switch(type) {
                case RIGHTHORI:
                /* Moving right */
                /* Move to the difference between collision overlap */
                    setX(colObj.horiLeftBody.colBox.x - (horiRightBody.colBox.width + horiRightBody.xOffset) /*- 1*/);
                    xVel = 0;
                    yVel = 0;
                    break;

                case LEFTHORI:
                /* Moving left */
                /* Move to the difference between collision overlap */
                    setX(colObj.horiRightBody.colBox.x + colObj.horiRightBody.colBox.width + horiLeftBody.xOffset /*- 1*/);
                    xVel = 0;
                    yVel = 0;
                    break;

                case BOTVERT:
                    Gdx.app.log("player", "collision");
                /* Vertical collision */
                /* Moving down */
                    setY(colObj.vertTopBody.colBox.y + colObj.vertTopBody.colBox.height + vertBotBody.yOffset /*+ 1*/);
                    xVel = 0;
                    yVel = 0;
                    break;

                case TOPVERT:
                /* Vertical collision */
                /* Moving up */
                    setY(colObj.vertBotBody.colBox.y - (vertTopBody.colBox.height + vertTopBody.yOffset) /*+ 1*/);
                    xVel = 0;
                    yVel = 0;
                    break;
            }
            checkCollisions = false;
        }
        updateColBox();
    }

    @Override
    public void update(float dt) {

    }
}
