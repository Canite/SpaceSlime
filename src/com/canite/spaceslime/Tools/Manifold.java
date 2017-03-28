package com.canite.spaceslime.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.canite.spaceslime.Bodies.SpriteBody;
import com.canite.spaceslime.Collisions.CollisionDispatcher;

/**
 * Created by Austin on 3/18/2017.
 */

public class Manifold {
    public SpriteBody A;
    public SpriteBody B;
    public float penetration;
    public final Vector2 normal = new Vector2();
    public final Vector2 contacts[] = {new Vector2(), new Vector2()}; // max of 2 contacts
    public int num_contacts;
    public float restitution;
    public float dyn_friction;
    public float stat_friction;
    public float contact_velocity;

    public Manifold( SpriteBody a, SpriteBody b) {
        A = a;
        B = b;
    }

    public void solve() {
        int A_shape = A.GetShape().type.ordinal();
        int B_shape = B.GetShape().type.ordinal();

        // Dispatch the solver to the correct collision instance (based on shape)
        CollisionDispatcher.dispatch[A_shape][B_shape].HandleCollision(this, A, B);
    }

    public void initialize() {
        restitution = StrictMath.min(A.material.restitution, B.material.restitution);

        stat_friction = (float)StrictMath.sqrt(A.material.static_friction * A.material.static_friction
                                                + B.material.static_friction * B.material.static_friction);
        dyn_friction = (float)StrictMath.sqrt(A.material.dynamic_friction * A.material.dynamic_friction
                + B.material.dynamic_friction * B.material.dynamic_friction);

        for (int i = 0; i < num_contacts; i++) {
            // Radius to center of mass
            //Vector2 a_rad = contacts[i].cpy().sub(A.position);
            //Vector2 b_rad = contacts[i].cpy().sub(B.position);

            // Velocity vector
            Vector2 velocity = A.velocity.cpy().sub(B.velocity);
            float resting_velocity = new Vector2(0.0f, -800.0f).scl(1.0f / Gdx.graphics.getFramesPerSecond()).len2() + 0.0001f;
            if (velocity.len2() < resting_velocity) {
                restitution = 0.0f;
            }
        }
    }

    public void applyImpulse() {
        // If both objects have infinite mass
        if (A.mass_data.inv_mass + B.mass_data.inv_mass == 0) {
            // Stop both objects
            A.velocity.set(0,0);
            B.velocity.set(0,0);
            // Notify the objects that they collided with something
            A.parent.collided(this);
            B.parent.collided(this);

            return;
        }

        for (int i = 0; i < num_contacts; i++) {
            // Radius to center of mass
            Vector2 a_rad = contacts[i].cpy().sub(A.position);
            Vector2 b_rad = contacts[i].cpy().sub(B.position);

            Vector2 velocity = B.velocity.cpy().sub(A.velocity);
            // Velocity along the normal
            contact_velocity = velocity.dot(normal);

            // If velocities are moving away from each other
            if (contact_velocity > 0) {
                // Do not resolve
                return;
            }

            float inv_mass_sum = A.mass_data.inv_mass + B.mass_data.inv_mass;
            float impulse_scalar;
            if (contact_velocity == 0) {
                impulse_scalar = -(1.0f + restitution) * penetration;
            }
            else {
                impulse_scalar = -(1.0f + restitution) * contact_velocity;
            }
            impulse_scalar /= inv_mass_sum;
            impulse_scalar /= num_contacts;

            Vector2 impulse = normal.cpy().scl(impulse_scalar);
            A.applyImpulse(impulse, -A.mass_data.inv_mass, a_rad);
            B.applyImpulse(impulse, B.mass_data.inv_mass, b_rad);

            // Friction
            Vector2 tangent = velocity.cpy();
            tangent.mulAdd(normal, -velocity.dot(normal));
            tangent.nor();

            // Tangent scalar impulse
            float tangent_scalar = -velocity.dot(tangent) * 0.75f;
            tangent_scalar /= inv_mass_sum;
            tangent_scalar /= num_contacts;

            if (Math.abs(tangent_scalar) < 0.0001f) {
                break;
            }

            // Coulumbs law
            Vector2 tangent_impulse;
            if (StrictMath.abs( tangent_scalar ) < impulse_scalar * stat_friction) {
                tangent_impulse = tangent.cpy().scl(tangent_scalar * stat_friction);
            }
            else {
                tangent_impulse = tangent.cpy().scl(-dyn_friction * impulse_scalar);
            }

            A.applyImpulse(tangent_impulse, -A.mass_data.inv_mass, a_rad);
            B.applyImpulse(tangent_impulse, B.mass_data.inv_mass, b_rad);
        }

        // Notify the objects that they collided with something
        A.parent.collided(this);
        B.parent.collided(this);
    }

    public void correctPosition() {
        if (A.mass_data.inv_mass + B.mass_data.inv_mass == 0.0f) {
            return;
        }

        float penetration_slop = 0.05f;
        float penetration_percent = 0.6f;

        float correction = (StrictMath.max(penetration - penetration_slop, 0.0f) / (A.mass_data.inv_mass + B.mass_data.inv_mass)) * penetration_percent;
        A.position.mulAdd(normal, -A.mass_data.inv_mass * correction);
        B.position.mulAdd(normal, B.mass_data.inv_mass * correction);
    }
}
