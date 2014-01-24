package mygame;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class Helper {
    public static Geometry createCube(float x, float y, float z, String name) {
        Box b = new Box(Vector3f.ZERO, x, y, z);
        Geometry g = new Geometry(name, b);
        return g;
    }
    
    public static Vector2f FlattenV3f(Vector3f vector) {
        return new Vector2f(vector.x, vector.z);
    }
    
    public static float AngleBetweenV2f(Vector2f a, Vector2f b) {
        return FastMath.atan2(b.y-a.y, b.x-a.x);
    }
}
