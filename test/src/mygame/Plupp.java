package mygame;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class Plupp {
    BoxGeomWrapper geometry;
    Vector2f velocity;
    
    Vector2f newVelocity;
    boolean collided;
    
    public static float PushStrength = 16000;
    public static float MaxSpeed = 4800000;
    public static float Radius = 1;

    public Plupp() {
        velocity = Vector2f.ZERO;
    }
    
    public void ApplyCollision() {
        velocity = newVelocity;
        collided = false;
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
    
    public void bounce() {
        //check if we've hit the wall, and bounce if so.
        //later: implement pluppbouncing here as well
        
        Vector3f pos = geometry.geometry.getLocalTranslation();
        
        if(pos.z >= 10 || pos.z <= -10) velocity.y *= -1;
        if(pos.x >= 10 || pos.x <= -10) velocity.x *= -1;
        
        //pluppbouncing
        
        for(Plupp q : Game.game.plupps) {
            if (q.equals(this)) continue;
            else {
                
                Vector2f pv =
                        Helper.FlattenV3f(this.geometry.getLocalTranslation());
                Vector2f qv =
                        Helper.FlattenV3f(q.geometry.getLocalTranslation());
                
                float d = pv.distance(qv);
                
                //angle betweewn
                float a = Helper.AngleBetweenV2f(pv, qv);
                
                //float pt = Helper.AngleBetweenV2f(pv, pv.add(this.velocity));
                //float qt = Helper.AngleBetweenV2f(qv, qv.add(q.velocity));

                float pt = this.velocity.getAngle();
                float qt = q.velocity.getAngle();
                
                //System.out.println(a);
                
                if (d < Radius * 2) {
                    if (pt > a) {
                        //foo bar
                    }
                }
            }
        }
    }
    
    public void bounds() {
        Vector3f pos = geometry.geometry.getLocalTranslation();
        if(pos.x >  10) { pos.x =  10; }
        if(pos.x < -10) { pos.x = -10; }
        if(pos.z >  10) { pos.z =  10; }
        if(pos.z < -10) { pos.z = -10; }
    }
}
