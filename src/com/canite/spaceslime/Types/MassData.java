package com.canite.spaceslime.Types;

/**
 * Created by Austin on 3/18/2017.
 */

public class MassData {
    public float mass;
    public float inv_mass;
    public float inertia;
    public float inv_inertia;

    public MassData(float mass, float inertia) {
        this.mass = mass;
        this.inertia = inertia;
        if (mass == 0) {
            inv_mass = 0;
            inv_inertia = 0;
        }
        else {
            this.inv_mass = 1 / mass;
            this.inv_inertia = 1 / inertia;
        }
    }
}
