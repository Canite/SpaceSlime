package com.canite.spaceslime.Sprites;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.canite.spaceslime.Bodies.SpriteBody;
import com.canite.spaceslime.Tools.Manifold;
import com.canite.spaceslime.Types.Polygon;

/**
 * Created by Austin on 2/3/2016.
 */
public class Ground extends GameObject {

    public Ground(Rectangle bounds) {
        setBounds(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
        Vector2 position = new Vector2(bounds.getX() + bounds.getWidth()/2, bounds.getY() + bounds.getHeight()/2);
        // All zeroes. 0 density = infinite mass
        body = new SpriteBody(this, new Polygon(), position, 0.0f, 0.0f, 0.1f, new Vector2(0,0), new Vector2(0,0), 0.0f);
        ((Polygon)body.shape).setBox(position, bounds.getWidth(), bounds.getHeight());
        ((Polygon)body.shape).rotation_matrix.set(0.0f);
        isStatic = true;
    }

    @Override
    public void update(float dt) {
        /* Static object, does not update */
    }

    @Override
    public void updatePosition() {
    }

    @Override
    public void updateRotation() {
    }


    @Override
    public void collided(Manifold manifold) {

    }
}
