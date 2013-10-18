package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class Game {
    public static Game game;
    
    AssetManager assetManager;
    Node rootNode;
    Node gameNode;
    InputManager inputManager;
    Camera cam;
    
    Dictionary<String, Material> materials;
    public List<Plupp> plupps;
    
    public Material loadMaterial(String path) {
        if(materials.get(path) == null) {
            materials.put(path, new Material(assetManager, path)); }
        return materials.get(path);
    }
    
    public ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean value, float tpf) {
            if (name.equals("pick target")) {
                if (!value) { return; }
                
                CollisionResults results = new CollisionResults();
                
                Vector2f click2d = Main.app.getInputManager()
                    .getCursorPosition();
                Vector3f click3d = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 0f).clone();
        
                Vector3f dir = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 1f).normalizeLocal();
                
                Ray ray = new Ray(click3d, dir);
                gameNode.collideWith(ray, results);
                if(results.size() < 1) { return; }
                
                CollisionResult cr = results.getClosestCollision();
                
                Vector3f pt = cr.getContactPoint();
                
                applyShockWave(pt);
            }
        };
    };
    
    public void applyShockWave(Vector3f vec) {
        for(int i = 0;i<plupps.size();i++) {
            Plupp p = plupps.get(i);
            
            Vector3f ppos = p.geometry.geometry.getLocalTranslation();
            float distance = ppos.distance(vec);

            //the following are merely test values
            float maxDistance = 5;
            
            if(FastMath.abs(
                distance) > maxDistance) { continue; }
            
            float speed = 
                Plupp.PushStrength-
                (FastMath.abs(distance)/maxDistance)*Plupp.PushStrength;

            Vector3f pos = p.geometry.geometry.getLocalTranslation();
            Vector2f dir = new Vector2f(pos.x-vec.x, pos.z-vec.z).mult(speed);
            
            p.ApplyForce(dir);

            plupps.set(i, p);
        }
    }
    
    public Plupp createPlupp() {
        Plupp p = new Plupp();
        plupps.add(p);
        return p;
    }
    
    public void Start() {
        
        plupps = new ArrayList();
        
        Material mat = new Material(
            assetManager,
            "Common/MatDefs/Misc/Unshaded.j3md"
        );
        
        mat.setTexture("ColorMap",
            assetManager.loadTexture("Textures/Penguins.jpg")
        );
        
        (gameNode = new Node()).attachChild(
            (new BoxGeomWrapper())
                .setGeometry(Helper.createCube(10,0.1f,10,"alfred"))
                .setMaterial(mat)
                .getGeometry()
        );
        
        rootNode.attachChild(gameNode);
        
        Plupp p;
        
        rootNode.attachChild(
            ((p = createPlupp()).geometry = new BoxGeomWrapper()
                .setGeometry(Helper.createCube(1,1,1,"plupp"))
                .setMaterial(mat))
            .getGeometry()
        );
        rootNode.attachChild(
            ((p = createPlupp()).geometry = new BoxGeomWrapper()
                .setGeometry(Helper.createCube(1,1,1,"plupp"))
                .setMaterial(mat)).move(new Vector3f(2,0,0))
            .getGeometry()
        );
    }
    
    float t = 0;
    
    public void Update(float tpf) {
        
        //before moving, calculate where they are all going to be and handle
        //collisions beforehand.
        
        for(Plupp p : plupps) {
            Vector2f movement = p.velocity.mult(tpf/1000f);
            
            float x = movement.x;
            float z = movement.y;
            
            p.geometry.move(new Vector3f(x, 0, z));
            
            p.bounds();
            p.bounce();
        }
    }
}