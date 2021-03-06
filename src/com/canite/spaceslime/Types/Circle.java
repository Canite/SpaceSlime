package com.canite.spaceslime.Types;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Austin on 3/18/2017.
 */

public class Circle extends Shape {
    public float radius;
    public float rotation;

    public Circle(float radius, Vector2 position) {
        this.radius = radius;
        this.position = position;
        this.type = Shapes.CIRCLE;
        this.rotation = 0;
    }

    @Override
    public float CalcMass(float density) {
        return density * (MathUtils.PI * radius * radius);
    }

    @Override
    public float CalcInertia(float mass) {
        return mass * radius * radius;
    }

    @Override
    public void UpdatePosition(Vector2 pos) {
        position.set(pos);
    }

    @Override
    public void setRotation(float rot) {
        rotation = rot;
    }
}
