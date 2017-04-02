package com.canite.spaceslime.Types;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Austin on 3/18/2017.
 */

public abstract class Shape {
    public Shapes type;
    public Vector2 position;

    public abstract float CalcMass(float density);
    public abstract float CalcInertia(float mass);
    public abstract void setRotation(float rot);
    public abstract void UpdatePosition(Vector2 pos);
}
