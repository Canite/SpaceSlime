package com.canite.spaceslime.Collisions;

import com.badlogic.gdx.math.Vector2;
import com.canite.spaceslime.Bodies.SpriteBody;
import com.canite.spaceslime.Tools.Manifold;
import com.canite.spaceslime.Types.Circle;
import com.canite.spaceslime.Types.Polygon;

/**
 * Created by Austin on 3/18/2017.
 */

public class CirclePolygonCollision extends Collision {

    public static CirclePolygonCollision instance = new CirclePolygonCollision();

    @Override
    public void HandleCollision(Manifold manifold, SpriteBody a, SpriteBody b) {
        Circle A = (Circle)a.GetShape();
        Polygon B = (Polygon)b.GetShape();

        // Center of the circle relative to the polygons coordinate space
        Vector2 circle_center = B.rotation_matrix.transpose().muli(a.position.cpy().sub( b.position ));

        manifold.num_contacts = 0;
        // Find least penetration edge
        float max_separation = -Float.MAX_VALUE;
        int edge_normal_index = 0;
        for (int i = 0; i < B.vertex_count; i++) {

            // edge (dot) (center - vertex)
            float separation = B.normals[i].cpy().dot(circle_center.cpy().sub(B.vertices[i]));

            // If we are farther away than the radius, no collision
            if (separation > A.radius) {
                return;
            }


            if (separation > max_separation) {
                max_separation = separation;
                edge_normal_index = i;
            }
        }

        Vector2 vertex1 = new Vector2(B.vertices[edge_normal_index]);
        Vector2 vertex2 = new Vector2(B.vertices[(edge_normal_index + 1) % B.vertex_count]); // Wrap around to 0 if we have to

        // Inside the polygon
        if (max_separation < 0.0001f) {
            manifold.num_contacts = 1;
            B.rotation_matrix.mul(B.normals[edge_normal_index], manifold.normal);
            manifold.normal.scl(-1.0f);
            manifold.contacts[0].set( manifold.normal ).mulAdd(a.position, A.radius);
            manifold.penetration = A.radius;
            return;
        }

        // Otherwise, find which vertex/edge we are closest to
        // Dot(center - v1, v2 - v1)
        float vertex1_dot = circle_center.cpy().sub(vertex1).dot(vertex2.cpy().sub(vertex1));
        // Dot(center - v2, v1 - v2)
        float vertex2_dot = circle_center.cpy().sub(vertex2).dot(vertex1.cpy().sub(vertex2));

        if (vertex1_dot <= 0.0f) {
            // Closest to vertex 1
            if (circle_center.dst2(vertex1) > A.radius * A.radius) {
                // No collision
                return;
            }

            manifold.num_contacts = 1;
            B.rotation_matrix.muli(manifold.normal.set(vertex1).sub(circle_center));
            manifold.normal.nor();
            B.rotation_matrix.mul(vertex1, manifold.contacts[0]);
            manifold.contacts[0].add(b.position);
        }
        else if (vertex2_dot <= 0.0f) {
            // Closest to vertex 2
            if (circle_center.dst2(vertex2) > A.radius * A.radius) {
                // No collision
                return;
            }

            manifold.num_contacts = 1;
            B.rotation_matrix.muli(manifold.normal.set(vertex2).sub(circle_center));
            manifold.normal.nor();
            B.rotation_matrix.mul(vertex2, manifold.contacts[0]);
            manifold.contacts[0].add(b.position);
        }
        else {
            // Closest to edge
            Vector2 edge_normal = B.normals[edge_normal_index];

            if (circle_center.cpy().sub(vertex1).dot(edge_normal) > A.radius) {
                return;
            }

            manifold.num_contacts = 1;
            B.rotation_matrix.mul(edge_normal, manifold.normal);
            manifold.normal.scl(-1.0f);
            manifold.contacts[0].set( a.position ).mulAdd(manifold.normal, A.radius);
        }
    }
}
