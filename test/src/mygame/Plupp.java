package mygame;

import com.jme3.math.Vector2f;

public class Plupp {
    BoxGeomWrapper geometry;
    Vector2f velocity;

    public static float PushStrength = 8000;
    public static float MaxSpeed = 24000;
    public static float Radius = 1;

    public Plupp() {
        velocity = Vector2f.ZERO;
    }

    public void ApplyForce(Vector2f force) {
        Vector2f resultant;
        
        //resultant = velocity.add(force);
        
        //test version, halving current velocity first to see if it feels
        //more responsive.
        
        resultant = velocity.mult(0.5f).add(force);
        
        if(resultant.length()>MaxSpeed) {
            resultant = resultant.normalize().mult(MaxSpeed);
        }
        
        velocity = resultant;
    }
}
