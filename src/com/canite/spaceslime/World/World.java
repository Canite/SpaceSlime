package com.canite.spaceslime.World;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.canite.spaceslime.SpaceSlime;
import com.canite.spaceslime.Sprites.Collidable;
import com.canite.spaceslime.Sprites.GameObject;
import com.canite.spaceslime.Tools.QuadTree;
import com.canite.spaceslime.Types.ColBody;

/**
 * Created by Austin on 2/7/2016.
 */

public class World {
    private Array<GameObject> staticObjects;
    private Array<GameObject> dynamicObjects;
    private QuadTree staticObjectTree;
    private QuadTree dynamicObjectTree;
    private Rectangle worldBounds;

    private float gravity = 9.8f / SpaceSlime.PPM;

    public World(int width, int height) {
        worldBounds = new Rectangle(0, 0, width, height);
        staticObjects = new Array<GameObject>();
        dynamicObjects = new Array<GameObject>();
        staticObjectTree = new QuadTree(0, worldBounds);
        dynamicObjectTree = new QuadTree(0, worldBounds);
    }

    private void applyPhysics(GameObject obj, float dt) {

    }

    private boolean colliding(Rectangle rect1, Rectangle rect2) { return rect1.overlaps(rect2); }

    private void checkCollisions(Collidable obj) {
        Array<Collidable> statColObjs = new Array<Collidable>();
        Array<Collidable> dynColObjs = new Array<Collidable>();
        Rectangle rect = obj.getBoundingRectangle();
        Rectangle horiRect = obj.horiBody.colBox;
        Rectangle vertRect = obj.vertBody.colBox;

        /* Assume all objects in quad trees are collidable (they are) */
        statColObjs = staticObjectTree.retrieve(statColObjs, rect);
        dynColObjs = dynamicObjectTree.retrieve(dynColObjs, rect);

        for (Collidable colObj : statColObjs) {
            Rectangle horiColRect = colObj.horiBody.colBox;
            Rectangle vertColRect = colObj.vertBody.colBox;
            if (colliding(horiRect, horiColRect) || colliding(horiRect, vertColRect)) {
                obj.collide(ColBody.HORIZONTAL, colObj);
            }
            else if (colliding(vertRect, horiColRect) || colliding(vertRect, vertColRect)) {
                obj.collide(ColBody.VERTICAL, colObj);
            }
        }

        for (Collidable colObj : dynColObjs) {
            Rectangle horiColRect = colObj.horiBody.colBox;
            Rectangle vertColRect = colObj.vertBody.colBox;
            if (colliding(horiRect, horiColRect) || colliding(horiRect, vertColRect)) {
                obj.collide(ColBody.HORIZONTAL, colObj);
            }
            else if (colliding(vertRect, horiColRect) || colliding(vertRect, vertColRect)) {
                obj.collide(ColBody.VERTICAL, colObj);
            }
        }
    }

    public void insertDynamic(GameObject dynObject) {
        dynamicObjects.add(dynObject);
        /* Quadtree is only for collidable objects */
        if (dynObject instanceof Collidable) {
            dynamicObjectTree.insert((Collidable) dynObject);
        }
    }

    public void insertStatic(GameObject statObject) {
        staticObjects.add(statObject);
        /* Quadtree is only for collidable objects */
        if (statObject instanceof Collidable) {
            staticObjectTree.insert((Collidable) statObject);
        }
    }

    public void update(float dt) {
        /* Dynamic objects will need to be recalculated
        for quad tree before checking for collisions */
        dynamicObjectTree.clear();

        /* First, apply physics to each object and regenerate the collision tree */
        for (GameObject obj : dynamicObjects) {
            applyPhysics(obj, dt);
            if (obj instanceof Collidable) {
                dynamicObjectTree.insert((Collidable) obj);
            }
        }

        /* Now check for collisions */
        for (GameObject obj : dynamicObjects) {
            if (obj instanceof Collidable) {
                checkCollisions((Collidable) obj);
            }
        }

        /* Then update each (dynamic) object */
        for (GameObject obj : dynamicObjects) {
            obj.update(dt);
        }
    }

    public void draw(Batch batch) {
        /* Static objects are drawn by tiles (maybe) */
        for (Sprite obj : dynamicObjects) {
            obj.draw(batch);
        }
    }

    public void dispose() {
        staticObjectTree.clear();
        dynamicObjectTree.clear();
        staticObjects.clear();
        dynamicObjects.clear();
    }
}
