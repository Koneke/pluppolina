package mygame;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class Plupp {
    BoxGeomWrapper geometry;
    Vector2f velocity;
    
    public static float PushStrength = 40000;
    public static float MaxSpeed = 4800000;
    public static float Radius = 1;

    public Plupp() {
        velocity = Vector2f.ZERO;
    }
    
    public void ApplyForce(Vector2f force) {
        Vector2f resultant;
        
        resultant = velocity.mult(0.5f).add(force);
        
        if(resultant.length() > MaxSpeed) {
            resultant = resultant.normalize().mult(MaxSpeed);
        }
        
        velocity = resultant;
    }
    
    public boolean bounced = false;
    
    public void bounce() {
        //check if we've hit the wall, and bounce if so.
        
        Vector3f pos = geometry.geometry.getLocalTranslation();
        
        if(pos.z >= Game.game.gameArea.h/2 ||
           pos.z <= -Game.game.gameArea.h/2) { velocity.y *= -1; }
        if(pos.x >= Game.game.gameArea.w/2 ||
           pos.x <= -Game.game.gameArea.w/2) { velocity.x *= -1; }
        
        //pluppbouncing
        
        if (bounced) return;
        
        for(Plupp q : Game.game.plupps) {
            if (q.bounced) continue;
            if (q.equals(this)) continue;
            
            Vector2f pos_a = Helper.FlattenV3f(
                    this.geometry.getLocalTranslation()
            );

            Vector2f pos_b = Helper.FlattenV3f(
                    q.geometry.getLocalTranslation()
            );

            float d = pos_a.distance(pos_b);

            
            
            if (d < Radius * 2) {

                float x_a =   velocity.x;
                float y_a =   velocity.y;
                float x_b = q.velocity.x;
                float y_b = q.velocity.y;

                  velocity.x = x_b;
                  velocity.y = y_b;
                q.velocity.x = x_a;
                q.velocity.y = y_a;

                  bounced = true;
                q.bounced = true;
                
                this.move(0.01f);
                   q.move(0.01f);
            }
        }
    }
    
    public void bounds() {
        Vector3f pos = geometry.geometry.getLocalTranslation();
        if(pos.x >  Game.game.gameArea.w/2) { pos.x =  Game.game.gameArea.w/2; }
        if(pos.x < -Game.game.gameArea.w/2) { pos.x = -Game.game.gameArea.w/2; }
        if(pos.z >  Game.game.gameArea.h/2) { pos.z =  Game.game.gameArea.h/2; }
        if(pos.z < -Game.game.gameArea.h/2) { pos.z = -Game.game.gameArea.h/2; }
    }
    
    float lasttpf;
    
    public void move(float tpf) {
        Vector2f movement = velocity.mult(tpf/1000f);

        float x = movement.x;
        float z = movement.y;

        geometry.move(new Vector3f(x, 0, z));
        lasttpf = tpf;
    }
    
    public void unmove(float m) {
        Vector2f movement = velocity.mult(-lasttpf/1000f).mult(m);

        float x = movement.x;
        float z = movement.y;

        geometry.move(new Vector3f(x, 0, z));
    }   
    
    public boolean insideArea(kRectangle _r) {
        Vector2f _p = Helper.FlattenV3f(geometry.getLocalTranslation());
        return
            _p.x > _r.x &&
            _p.y > _r.x &&
            _p.x < _r.x + _r.w &&
            _p.y < _r.y + _r.h;
    }
}
