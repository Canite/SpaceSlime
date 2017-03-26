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
        this.material = new Material(density, restitution, 0.5f, 0.3f);
        this.mass_data = new MassData(this.shape.CalcMass(density), 0.0f);
        this.velocity = new Vector2(velocity);
        this.force = new Vector2(force);
        this.gravity_scale = gravity_scale;
    }

    public Shape GetShape() { return shape; }

    public void applyForce( Vector2 f ) {
        force.add(f);
    }

    public void applyImpulse( Vector2 impulse, float scalar, Vector2 contact_vector ) {
        velocity.mulAdd(impulse, scalar);
        // angular_velocity += mass_data.inv_inertia * contact_vector.cross(impulse);
    }
}
