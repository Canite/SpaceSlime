package com.canite.spaceslime.Tools;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.canite.spaceslime.Sprites.Collidable;

/**
 * Created by austin on 2/8/16.
 */
public class QuadTree {
    private int MAX_OBJECTS = 10;
    private int MAX_LEVEL = 5;

    private int level;
    private Array<Collidable> objects;
    private Rectangle bounds;
    private QuadTree[] nodes;

    public QuadTree(int level, Rectangle bounds) {
        this.level = level;
        this.bounds = bounds;

        objects = new Array<Collidable>();
        nodes = new QuadTree[4];
    }

    public void clear() {
        objects.clear();

        for (int i = 0; i < nodes.length; i++) {
            nodes[i].clear();
            nodes[i] = null;
        }
    }

    public void split() {
        int subWidth = (int)(bounds.getWidth() / 2);
        int subHeight = (int)(bounds.getHeight() / 2);
        int x = (int)bounds.getX();
        int y = (int)bounds.getY();

        nodes[0] = new QuadTree(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
        nodes[1] = new QuadTree(level + 1, new Rectangle(x, y, subWidth, subHeight));
        nodes[2] = new QuadTree(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));
        nodes[3] = new QuadTree(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
    }

    /*
    Return the index for the node that an object belongs in
    Returns -1 if the object belongs in the parent node
    */
    public int getIndex(Rectangle rect) {
        int index = -1;
        double vertMidpoint = bounds.getX() + (bounds.getWidth() / 2);
        double horiMidpoint = bounds.getY() + (bounds.getHeight() / 2);

        boolean topQuad = (rect.getY() + rect.getHeight() < horiMidpoint);
        boolean botQuad = (rect.getY() > horiMidpoint);
        boolean leftQuad = (rect.getX() + rect.getWidth() < vertMidpoint);
        boolean rightQuad = (rect.getX() > vertMidpoint);

        /* ____________
           |     |     |
           | II  |  I  |
           |_____|_____|
           |     |     |
           | III |  IV |
           |_____|_____|
         */
        if (topQuad && rightQuad) { index = 0; }
        else if (topQuad && leftQuad) { index = 1; }
        else if (botQuad && leftQuad) { index = 2; }
        else if (botQuad && rightQuad) { index = 3; }

        return index;
    }

    public void insert(Collidable sp) {
        Rectangle spBounds = sp.getBoundingRectangle();
        if (nodes[0] != null) {
            int index = getIndex(spBounds);
            if (index != -1) {
                nodes[index].insert(sp);
                return;
            }
        }

        objects.add(sp);
        if (objects.size > MAX_OBJECTS && level < MAX_LEVEL) {
            if (nodes[0] == null) {
                split();
            }

            int i = 0;
            while (i < objects.size) {
                int index = getIndex(objects.get(i).getBoundingRectangle());
                if (index != -1) {
                    nodes[index].insert(objects.removeIndex(i));
                } else {
                    i++;
                }
            }
        }
    }

    public Array<Collidable> retrieve(Array<Collidable> collidingObjects, Rectangle rect) {
        int index = getIndex(rect);
        if (index != -1 && nodes[0] != null) {
            nodes[index].retrieve(collidingObjects, rect);
        }

        collidingObjects.addAll(objects);
        return collidingObjects;
    }
}
