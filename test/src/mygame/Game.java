package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class Game {
    public AssetManager assetManager;
    Node rootNode;
    Camera cam;

    DirectionalLight sun;
    
    static Geometry gamePlane;
    Dictionary<String, Material> materials;
    List<Plupp> plupps;
    
    public static Game game;
    
    public Material loadMaterial(String path) {
        if(materials.get(path) != null) { return materials.get(path); }
        return new Material(
            assetManager, path);
    }
    
    public Game() {
        game = this;
    }
    
    public void applyShockWave(Vector3f vec) {
        for(Plupp p : plupps) {
            Vector3f ppos = p.geometry.geometry.getLocalTranslation();
            float distance = ppos.distance(vec);
            System.out.println(distance);
        }	
    }
    
    public Plupp createPlupp() {
        Plupp p = new Plupp();
        plupps.add(p);
        return p;
    }
    
    public void Start() {        
        plupps = new ArrayList();
        
        World w = new World();
        w.Load(rootNode);
    }
    
    
    float t = 0;
    
    public void Update(float tpf) {
        t += tpf;
        plupps.get(0).geometry.geometry.setLocalTranslation(
            new Vector3f(0,0,1).mult(5));
        
        cam.lookAt(
            new Vector3f(0,0,0),
            new Vector3f(0,0,-1));
        
        sun.setDirection(new Vector3f(0f,-1f,-4f+t*.1f).normalizeLocal());
    }
}
