package com.canite.spaceslime.Bodies;

import com.badlogic.gdx.math.Vector2;
import com.canite.spaceslime.Sprites.GameObject;
import com.canite.spaceslime.Types.MassData;
import com.canite.spaceslime.Types.Material;
import com.canite.spaceslime.Types.Shape;

/**
 * Created by Austin on 2/7/2016.
 */
public class SpriteBody {
    public Shape shape;
    public Material material;
    public MassData mass_data;
    public Vector2 position;
    public float rotation;
    public float angular_velocity;
    public float torque;
    public Vector2 velocity;
    public Vector2 force;
    public float gravity_scale;
    public GameObject parent;

    public SpriteBody(GameObject parent, Shape shape, Vector2 position, float rotation, float density,
                      float restitution, Vector2 velocity, Vector2 force, float gravity_scale) {
        this.parent = parent;
        this.shape = shape;
        this.position = new Vector2(position);
        this.rotation = rotation;
        this.material = new Material(density, restitution, 0.4f, 0.16f);
        float mass = this.shape.CalcMass(density);
        this.mass_data = new MassData(mass, this.shape.CalcInertia(mass));
        this.velocity = new Vector2(velocity);
        this.force = new Vector2(force);
        this.torque = 0;
        this.angular_velocity = 0;
        this.rotation = 0;
        this.gravity_scale = gravity_scale;
    }

    public Shape GetShape() { return shape; }

    public void applyForce( Vector2 f ) {
        force.add(f);
    }

    public void applyImpulse( Vector2 impulse, float scalar, Vector2 contact_vector ) {
        velocity.mulAdd(impulse, scalar);
        float cross = impulse.crs(contact_vector/*.cpy().nor()*/);
        angular_velocity += mass_data.inv_inertia * cross;
    }

    public void setRotation(float rot) {
        rotation = rot;
        shape.setRotation(rot);
    }
}
