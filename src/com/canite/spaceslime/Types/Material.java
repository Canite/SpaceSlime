package com.canite.spaceslime.Types;

/**
 * Created by Austin on 3/18/2017.
 */

public class Material {
    public float density;
    public float restitution;
    public float static_friction;
    public float dynamic_friction;

    public Material(float density, float restitution, float static_friction, float dynamic_friction) {
        this.density = density;
        this.restitution = restitution;
        this.static_friction = static_friction;
        this.dynamic_friction = dynamic_friction;
    }
}
