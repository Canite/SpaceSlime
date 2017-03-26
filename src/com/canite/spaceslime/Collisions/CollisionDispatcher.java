package com.canite.spaceslime.Collisions;

/**
 * Created by Austin on 3/18/2017.
 */

public class CollisionDispatcher {

    // Call the dispatcher using the Shape.type field ( 0 for circles, 1 for polygons )
    public static Collision[][] dispatch =
            {
                    { CircleCircleCollision.instance, CirclePolygonCollision.instance },
                    { PolygonCircleCollision.instance, PolygonPolygonCollision.instance }
            };
}
