package com.canite.spaceslime.Tools;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by austin on 2/8/16.
 */
public class QuadTree {
    private int MAX_OBJECTS = 10;
    private int MAX_LEVEL = 5;

    private int level;
    private Array<Sprite> objects;
    private Rectangle bounds;
    private QuadTree[] nodes;

    public QuadTree(int level, Rectangle bounds) {
        this.level = level;
        this.bounds = bounds;

        objects = new Array<Sprite>();
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

        boolean topQuad = (rect.getX())
    }
}
