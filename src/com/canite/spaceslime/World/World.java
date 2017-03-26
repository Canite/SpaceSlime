package com.canite.spaceslime.World;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.canite.spaceslime.Bodies.SpriteBody;
import com.canite.spaceslime.Sprites.GameObject;
import com.canite.spaceslime.Sprites.Ground;
import com.canite.spaceslime.Sprites.Slime;
import com.canite.spaceslime.Tools.Manifold;
import com.canite.spaceslime.Tools.QuadTree;

/**
 * Created by Austin on 2/7/2016.
 */

/*
    !!!!!!!!!!!!!!!!!!!
    PAY ATTENTION!!!!!!
    LIBGDX USES BOTTOM-LEFT AS THE ORIGIN, NOT THE TOP-LEFT!!!!!!!!
    PLEASE DON'T FORGET AGAIN!!!!!!!!!!
    !!!!!!!!!!!!!!!!!!!
 */

public class World {
    private Array<GameObject> objects;
    public Array<Manifold> collisions;
    public QuadTree objectTree;
    private Rectangle worldBounds;
    public Array<GameObject> dynamic_objects;

    public Slime player;

    private Vector2 GRAVITY = new Vector2(0.0f, -600.0f);

    public World(int width, int height) {
        worldBounds = new Rectangle(0, 0, width, height);
        objects = new Array<GameObject>();
        objectTree = new QuadTree(0, worldBounds);
        collisions = new Array<Manifold>();
        dynamic_objects = new Array<GameObject>();
    }

    public void insertObject(GameObject obj) {
        objects.add(obj);
        if (!obj.isStatic) {
            dynamic_objects.add(obj);
        }
        /* Quadtree is only for collidable objects */
        objectTree.insert(obj);
    }

    public void removeObject(GameObject obj) {
        objects.removeValue(obj, true);
    }

    public void update(float dt) {
        /* Dynamic objects will need to be recalculated
        for quad tree before checking for collisions */
        objectTree.clear();
        collisions.clear();

        /* First, regenerate the collision tree */
        /* BROAD PHASE */
        for (GameObject obj : objects) {
            obj.onGround = false; // This will be reset later
            objectTree.insert(obj);
        }

        objectTree.generateManifolds(dynamic_objects, collisions);

        /* Add force(acceleration) to objects velocities */
        for (GameObject obj : objects) {
            if (!obj.isStatic) {
                integrateForces(obj.body, dt);
            }
        }

        /* Initialize collisions */
        for (Manifold collision: collisions) {
            collision.initialize();
        }

        /* Apply impulses to objects */
        for (Manifold collision: collisions) {
            collision.applyImpulse();
        }

        /* Integrate velocities (add to position) */
        for (GameObject obj: objects) {
            obj.update(dt);
            if (!obj.isStatic) {
                integrateVelocity(obj, dt);
            }
        }

        /* Correct positions */
        for (Manifold collision: collisions) {

            if (collision.B.parent instanceof Ground) {
                if (collision.normal.angle() > 195 && collision.normal.angle() < 345) {
                    collision.A.parent.onGround = true;
                }
            }
        }

        /* Apply new positions to sprites */
        for (GameObject obj: objects) {
            obj.updatePosition();
        }

        /* Clear forces */
        for (GameObject obj: objects) {
            obj.body.force.set(0, 0);
            //obj.body.torque = 0;
        }
    }

    public void draw(Batch batch) {
        /* Static objects are drawn by tiles (maybe) */
        for (GameObject obj : objects) {
            if (!obj.isStatic) {
                obj.draw(batch);
            }
        }
    }

    public void dispose() {
        objectTree.clear();
        objects.clear();
    }

    public void integrateForces(SpriteBody body, float dt) {
        dt = 1.0f/64.0f;
        float dts = dt * 0.5f;

        body.velocity.mulAdd(body.force, body.mass_data.inv_mass * dts);
        body.velocity.mulAdd(GRAVITY, dts * body.gravity_scale);
        //body.angular_velocity += body.torque * body.mass_data.inv_inertia * dts;
    }

    public void integrateVelocity(GameObject obj, float dt) {
        dt = 1.0f/64.0f;

        obj.body.position.mulAdd(obj.body.velocity, dt);
        obj.body.shape.UpdatePosition(obj.body.position);

        integrateForces(obj.body, dt);
    }
}
