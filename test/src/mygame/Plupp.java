package mygame;

import com.jme3.math.Vector2f;

public class Plupp {
    BoxGeomWrapper geometry;
    Vector2f velocity;

    public static float MaxSpeed = 5000;

    public Plupp() {
        velocity = Vector2f.ZERO;
    }

    public void ApplyForce(Vector2f force) {
        Vector2f resultant = velocity.add(force);
        if(resultant.length()>MaxSpeed) {
            resultant = resultant.normalize().mult(MaxSpeed);
        }
        velocity = resultant;
    }
}
