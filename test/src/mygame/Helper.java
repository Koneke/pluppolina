package mygame;

import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class Helper {
    public static Geometry createCube(float x, float y, float z, String name) {
        Box b = new Box(Vector3f.ZERO, x, y, z);
        Geometry g = new Geometry(name, b);
        return g;
    }
}
