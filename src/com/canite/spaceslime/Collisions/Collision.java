package com.canite.spaceslime.Collisions;

import com.canite.spaceslime.Bodies.SpriteBody;
import com.canite.spaceslime.Tools.Manifold;

/**
 * Created by Austin on 3/18/2017.
 */

public abstract class Collision {

    public abstract void HandleCollision(Manifold manifold, SpriteBody a, SpriteBody b);

}
