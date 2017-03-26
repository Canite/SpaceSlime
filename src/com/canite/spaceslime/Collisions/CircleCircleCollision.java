package com.canite.spaceslime.Collisions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.canite.spaceslime.Bodies.SpriteBody;
import com.canite.spaceslime.Tools.Manifold;
import com.canite.spaceslime.Types.Circle;

/**
 * Created by Austin on 3/18/2017.
 */

public class CircleCircleCollision extends Collision {

    public static CircleCircleCollision instance = new CircleCircleCollision();

    @Override
    public void HandleCollision(Manifold manifold, SpriteBody a, SpriteBody b) {
        Circle A = (Circle)a.GetShape();
        Circle B = (Circle)b.GetShape();

        // Vector2().sub() modifies the calling vector, make a copy first
        Vector2 normal = b.position.cpy().sub(a.position);

        float square_dist = normal.len2();
        float radius = A.radius + B.radius;

        if (square_dist >= radius * radius) {
            manifold.num_contacts = 0;
            return;
        }

        float distance = (float) StrictMath.sqrt(square_dist);

        // collision
        manifold.num_contacts = 1;

        if (distance == 0.0f) {
            manifold.penetration = A.radius;
            manifold.normal.set(1.0f, 0.0f);
            manifold.contacts[0].set(a.position);
        }
        else
        {
            manifold.penetration = radius - distance;
            manifold.normal.set(normal).nor();
            manifold.contacts[0].set(manifold.normal).scl(A.radius).add(a.position);
        }
    }
}
