package com.canite.spaceslime.Tools;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.canite.spaceslime.Collisions.CollisionDispatcher;
import com.canite.spaceslime.Sprites.GameObject;

/**
 * Created by austin on 2/8/16.
 */
public class QuadTree {
    private int MAX_OBJECTS = 5;
    private int MAX_LEVEL = 5;

    private int level;
    private Array<GameObject> objects;
    private Rectangle bounds;
    private QuadTree[] nodes;

    public QuadTree(int level, Rectangle bounds) {
        this.level = level;
        this.bounds = bounds;

        objects = new Array<GameObject>();
        nodes = new QuadTree[4];
    }

    public void clear() {
        objects.clear();

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].clear();
                nodes[i] = null;
            }
        }
    }

    public void split() {
        int subWidth = (int)(bounds.getWidth() / 2);
        int subHeight = (int)(bounds.getHeight() / 2);
        int x = (int)bounds.getX();
        int y = (int)bounds.getY();

        nodes[0] = new QuadTree(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
        nodes[1] = new QuadTree(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));
        nodes[2] = new QuadTree(level + 1, new Rectangle(x, y, subWidth, subHeight));
        nodes[3] = new QuadTree(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
    }

    /*
    Return the index for the node that an object belongs in
    Returns -1 if the object belongs in the parent node
    */
    public int getIndex(Rectangle rect) {
        int index = -1;
        double vertMidpoint = bounds.getX() + (bounds.getWidth() / 2);
        double horiMidpoint = bounds.getY() + (bounds.getHeight() / 2);

        // Not within the bounds at all
        if (rect.getX() + rect.getWidth() < bounds.getX()
                || rect.getY() + rect.getHeight() < bounds.getY()
                || rect.getX() > bounds.getX() + bounds.getWidth()
                || rect.getY() > bounds.getY() + bounds.getWidth()) {
            return -1;
        }

        boolean topQuad = (rect.getY() + rect.getHeight() >= horiMidpoint);
        boolean botQuad = (rect.getY() <= horiMidpoint);
        boolean leftQuad = (rect.getX() <= vertMidpoint);
        boolean rightQuad = (rect.getX() + rect.getWidth() >= vertMidpoint);

        /* ____________
           |     |     |
           | II  |  I  |
           |_____|_____|
           |     |     |
           | III |  IV |
           |_____|_____|
         */
        // Keep overlapping objects in the parent quadtree
        if ((topQuad && botQuad) || (leftQuad && rightQuad)) { index = -2; } // Overlapping!
        else if (topQuad && rightQuad) { index = 0; }
        else if (topQuad && leftQuad) { index = 1; }
        else if (botQuad && leftQuad) { index = 2; }
        else if (botQuad && rightQuad) { index = 3; }

        return index;
    }

    public void insert(GameObject sp) {
        Rectangle spBounds = sp.getBoundingRectangle();
        if (nodes[0] != null) {
            int index = getIndex(spBounds);
            if (index >= 0) {
                nodes[index].insert(sp);
                return;
            }
        }

        objects.add(sp);
        // NOTE: This could break if a large number of objects are overlapping 2 nodes
        // and by break, I mean it will run every frame as objects.size will always be > MAX_OBJECTS
        if (objects.size > MAX_OBJECTS && level < MAX_LEVEL) {
            if (nodes[0] == null) {
                split();
            }

            int i = 0;
            while (i < objects.size) {
                int index = getIndex(objects.get(i).getBoundingRectangle());
                if (index >= 0) {
                    nodes[index].insert(objects.removeIndex(i));
                } else {
                    i++;
                }
            }
        }
    }

    public void retrieve(Array<GameObject> collidingObjects, Rectangle rect, Array<GameObject> checked_objects) {
        int index = getIndex(rect);
        if (nodes[0] != null) {
            if (index >= 0) {
                nodes[index].retrieve(collidingObjects, rect, checked_objects);
            } else if (index == -2){
                // If we're overlapping, get everything
                nodes[0].retrieve(collidingObjects, rect, checked_objects);
                nodes[1].retrieve(collidingObjects, rect, checked_objects);
                nodes[2].retrieve(collidingObjects, rect, checked_objects);
                nodes[3].retrieve(collidingObjects, rect, checked_objects);
            }
        }

        for (int i = 0; i < objects.size; i++) {
            if (!checked_objects.contains(objects.get(i), true)) {
                collidingObjects.add(objects.get(i));
            }
        }
    }

    public void generateManifolds(Array<GameObject> dynamic_objects, Array<Manifold> manifolds) {
        Array<GameObject> checked_objects = new Array<GameObject>();
        for (GameObject object : dynamic_objects) {
            // Only generate manifolds for dynamic objects
            //if (!object.isStatic) {
                checked_objects.add(object);
                Array<GameObject> colliding_objects = new Array<GameObject>();
                // Generate a list of potential colliding objects, ignore already checked objects
                retrieve(colliding_objects, object.getBoundingRectangle(), checked_objects);
                for (GameObject colliding_object : colliding_objects) {
                    Manifold collision = new Manifold(object.body, colliding_object.body);
                    collision.solve();
                    // If we are actually colliding
                    if (collision.num_contacts > 0) {
                        manifolds.add(collision);
                    }
                }
            //}
        }

        /*
        if (nodes[0] != null) {
            nodes[0].generateManifolds(manifolds, checked_objects);
            nodes[1].generateManifolds(manifolds, checked_objects);
            nodes[2].generateManifolds(manifolds, checked_objects);
            nodes[3].generateManifolds(manifolds, checked_objects);
        }
        */
    }

    public void drawDebugQuads(ShapeRenderer renderer) {
        renderer.setColor(0, 0, 1, 1);
        renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].drawDebugQuads(renderer);
            }
        }
    }

    public void drawDebugObjects(ShapeRenderer renderer, float alpha) {
        renderer.setColor(1, 0, 0, alpha);
        for (GameObject obj : objects) {
            Rectangle rect = obj.getBoundingRectangle();
            renderer.rect(rect.x, rect.y, rect.width, rect.height);
        }
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                nodes[i].drawDebugObjects(renderer, alpha + 0.1f);
            }
        }
    }
}
