package com.canite.spaceslime.Types;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.canite.spaceslime.Tools.Matrix2;

/**
 * Created by Austin on 3/18/2017.
 */

public class Polygon extends Shape {
    public static final int MAX_VERTEX_COUNT = 64;
    public int vertex_count;
    public Vector2 vertices[] = new Vector2[MAX_VERTEX_COUNT];
    public Vector2 normals[] = new Vector2[MAX_VERTEX_COUNT];
    public Matrix2 rotation_matrix = new Matrix2();

    public Polygon() {
        this.type = Shapes.POLYGON;
        for (int i = 0; i < MAX_VERTEX_COUNT; i++) {
            vertices[i] = new Vector2();
            normals[i] = new Vector2();
        }
    }
//hello cutie pie
//welcome to my code
    public void set(Array<Vector2> vertex_list) {

    }

    public void setBox(Vector2 position, float width, float height) {
        vertex_count = 4;
        // Relative to position
        vertices[0].set(-width/2, height/2);
        vertices[1].set(width/2, height/2);
        vertices[2].set(width/2, -height/2);
        vertices[3].set(-width/2, -height/2);
        normals[0].set(0.0f, 1.0f);
        normals[1].set(1.0f, 0.0f);
        normals[2].set(0.0f, -1.0f);
        normals[3].set(-1.0f, 0.0f);
        this.position = new Vector2(position);
    }

    @Override
    public float CalcMass(float density) {
        return density;
    }

    @Override
    public float CalcInertia(float mass) {
        return 0.0f;
    }

    @Override
    public void UpdatePosition(Vector2 pos) {
    }

    @Override
    public void setRotation(float rot) {
        rotation_matrix.set(rot);
    }
}
