package mygame;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class BoxGeomWrapper {
    
    Box box;
    Geometry geometry;
    
    public BoxGeomWrapper() {
    }
    
    public Geometry getGeometry() {
        return geometry;
    }
    
    public BoxGeomWrapper setGeometry(Geometry geometry) {
        this.geometry = geometry;
        return this;
    }
    
    //throughcall for readabilities sake
    public Vector3f getLocalTranslation() {
        return geometry.getLocalTranslation();
    }
    
    public Box getBox() {
        return box;
    }
    
    public BoxGeomWrapper setBox(Box box) {
        this.box = box;
        return this;
    }
    
    public BoxGeomWrapper move(Vector3f offset) {
        geometry.move(offset);
        return this;
    }
    
    public BoxGeomWrapper setMaterial(Material mat) {
        geometry.setMaterial(mat);
        return this;
    }
}