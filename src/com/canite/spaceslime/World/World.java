package com.canite.spaceslime.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.canite.spaceslime.SpaceSlime;
import com.canite.spaceslime.Sprites.Collidable;
import com.canite.spaceslime.Sprites.GameObject;
import com.canite.spaceslime.Sprites.Ground;
import com.canite.spaceslime.Sprites.Slime;
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

    private float gravity = -30.0f / SpaceSlime.PPM;

    public Slime player;

    public World(int width, int height) {
        worldBounds = new Rectangle(0, 0, width, height);
        staticObjects = new Array<GameObject>();
        dynamicObjects = new Array<GameObject>();
        staticObjectTree = new QuadTree(0, worldBounds);
        dynamicObjectTree = new QuadTree(0, worldBounds);
    }

    private void applyPhysics(GameObject obj, float dt) {
        /* Apply gravity */
        if (!obj.onGround) {
            if (obj.applyGravity)
                obj.yAccel = gravity;
        } else {
            obj.yAccel = 0;
        }

        /* Update velocity based on acceleration */
        if ((Math.abs(obj.xVel + obj.xAccel) < obj.maxXVel) || ((obj.xVel > 0) != (obj.xAccel > 0)))
            obj.xVel += obj.xAccel;
        // if we are not at our max velocity and the acceleration is trying to add to the velocity
        if ((Math.abs(obj.yVel + obj.yAccel) < obj.maxYVel) || ((obj.yVel > 0) != (obj.yAccel > 0)))
            obj.yVel += obj.yAccel;

        /* Apply velocity to objects position */
        obj.setX(obj.getX() + Math.round(obj.xVel * dt));
        obj.setY(obj.getY() + Math.round(obj.yVel * dt));
    }

    private boolean colliding(Rectangle rect1, Rectangle rect2) { return rect1.overlaps(rect2); }

    private void checkCollisions(Collidable obj) {
        Array<Collidable> colObjs = new Array<Collidable>();
        Rectangle rect = obj.getBoundingRectangle();
        Rectangle horiLeftRect = obj.horiLeftBody.colBox;
        Rectangle horiRightRect = obj.horiRightBody.colBox;
        Rectangle vertTopRect = obj.vertTopBody.colBox;
        Rectangle vertBotRect = obj.vertBotBody.colBox;

        //player.printStats();
        /* Assume all objects in quad trees are collidable (they are) */
        staticObjectTree.retrieve(colObjs, rect);
        dynamicObjectTree.retrieve(colObjs, rect);

        for (Collidable colObj : colObjs) {
            if (colObj != obj) {
                Rectangle horiLeftColRect = colObj.horiLeftBody.colBox;
                Rectangle horiRightColRect = colObj.horiRightBody.colBox;
                Rectangle vertTopColRect = colObj.vertTopBody.colBox;
                Rectangle vertBotColRect = colObj.vertBotBody.colBox;
                if (colliding(horiLeftRect, horiLeftColRect) || colliding(horiLeftRect, horiRightColRect)
                        || colliding(horiLeftRect, vertTopColRect) || colliding(horiLeftRect, vertBotColRect)) {
                    obj.collide(ColBody.LEFTHORI, colObj);
                }
                if (colliding(horiRightRect, horiLeftColRect) || colliding(horiRightRect, horiRightColRect)
                        || colliding(horiRightRect, vertTopColRect) || colliding(horiRightRect, vertBotColRect)) {
                    obj.collide(ColBody.RIGHTHORI, colObj);
                }
                if (colliding(vertTopRect, horiLeftColRect) || colliding(vertTopRect, horiRightColRect)
                        || colliding(vertTopRect, vertTopColRect) || colliding(vertTopRect, vertBotColRect)) {
                    obj.collide(ColBody.TOPVERT, colObj);
                }
                if (colliding(vertBotRect, horiLeftColRect) || colliding(vertBotRect, horiRightColRect)
                        || colliding(vertBotRect, vertTopColRect) || colliding(vertBotRect, vertBotColRect)) {
                    obj.collide(ColBody.BOTVERT, colObj);
                }
                /* Check if on ground */
                if (colObj instanceof Ground) {
                    Rectangle feet = new Rectangle(obj.vertBotBody.colBox.x, obj.vertBotBody.colBox.y - 2, obj.vertBotBody.colBox.width, 2);
                    if (colliding(feet, horiLeftColRect) || colliding(feet, horiRightColRect)
                            || colliding(feet, vertTopColRect) || colliding(feet, vertBotColRect)) {
                        obj.onGround = true;
                        //Gdx.app.log("World", "on ground");
                    }
                }
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
            obj.update(dt);
            if (obj instanceof Collidable) {
                ((Collidable) obj).updateColBox();
                dynamicObjectTree.insert((Collidable) obj);
            }
        }

        /* Now check for collisions */
        for (GameObject obj : dynamicObjects) {
            if (obj instanceof Collidable) {
                if (((Collidable) obj).checkCollisions) {
                    obj.onGround = false;
                    checkCollisions((Collidable) obj);
                    if (!obj.onGround)
                        Gdx.app.log("World", "not");
                }
            }
        }

        /* Then update each (dynamic) object */
        //for (GameObject obj : dynamicObjects) {
        //}
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
