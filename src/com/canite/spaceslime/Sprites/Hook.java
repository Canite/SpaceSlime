package com.canite.spaceslime.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.canite.spaceslime.Bodies.SpriteBody;
import com.canite.spaceslime.Screens.PlayScreen;
import com.canite.spaceslime.SpaceSlime;
import com.canite.spaceslime.Tools.Manifold;
import com.canite.spaceslime.Types.Circle;
import com.canite.spaceslime.World.World;

/**
 * Created by austin on 4/10/16.
 */
public class Hook extends GameObject {
    private World world;
    private PlayScreen screen;
    private Animation animation;
    private float stateTimer;
    private Vector2 startPos;
    public float minDistance;
    public float maxDistance;
    private Slime player;
    private boolean hooked;
    public float length;
    public float angle;

    public Hook(World world, PlayScreen screen, Slime player, float startX, float startY, Vector2 velocity) {
        this.world = world;
        this.screen = screen;
        this.player = player;
        stateTimer = 0;
        maxDistance = 25000.0f;
        minDistance = 2500.0f;
        applyGravity = false;
        hooked = false;
        isStatic = false;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        int numFrames = 4;
        int frameSize = 8;
        float animationSpeed = 0.1f;

        for (int i = 0; i < numFrames; i++) {
            TextureRegion frameRegion;
            frameRegion = new TextureRegion(screen.getAtlas().findRegion("SlimeHook"), i*frameSize, 0, frameSize, frameSize);
            PlayScreen.fixBleeding(frameRegion);
            frames.add(frameRegion);
        }

        animation = new Animation(animationSpeed, frames);

        setBounds(startX - frameSize / 2, startY - frameSize / 2, frameSize / SpaceSlime.PPM, frameSize / SpaceSlime.PPM);
        setRegion((TextureRegion) animation.getKeyFrame(stateTimer));
        setOriginCenter();

        body = new SpriteBody(this, new Circle(frameSize / 2, new Vector2(startX, startY)),
                new Vector2(startX, startY), 0.0f, 0.0f, 0.0f, new Vector2(velocity), new Vector2(0.0f, 0.0f), 0.0f);

        startPos = new Vector2(startX, startY);

    }

    @Override
    public void update(float dt) {
        if (hooked) {
            angle = body.position.cpy().sub(player.body.position).angleRad();
        }
        else {
            if (startPos.dst2(body.position) >= maxDistance) {
                player.removeHook();
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
        if (!hooked && (manifold.B.parent instanceof Ground || manifold.A.parent instanceof Ground)) {
            hooked = true;
            player.hooked = true;
            length = body.position.dst(player.body.position);
        }
    }
}
