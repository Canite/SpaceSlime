package com.canite.spaceslime.Collisions;

import com.canite.spaceslime.Bodies.SpriteBody;
import com.canite.spaceslime.Tools.Manifold;

/**
 * Created by Austin on 3/18/2017.
 */

public class PolygonCircleCollision extends Collision {

    public static PolygonCircleCollision instance = new PolygonCircleCollision();

    @Override
    public void HandleCollision(Manifold manifold, SpriteBody a, SpriteBody b) {
        // Use same collision handle, but negate normal afterwards
        CirclePolygonCollision.instance.HandleCollision(manifold, b, a);

        if (manifold.num_contacts > 0) {
            manifold.normal.scl(-1.0f);
        }
    }
}
