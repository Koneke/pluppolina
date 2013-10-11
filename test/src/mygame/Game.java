package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class Game {
    AssetManager assetManager;
    Node rootNode;
    Node gameNode;
    Camera cam;
    
    static Geometry gamePlane;
    Dictionary<String, Material> materials;
    List<Plupp> plupps;
    
    public Material loadMaterial(String path) {
        if(materials.get(path) != null) { return materials.get(path); }
        return new Material(
            assetManager, path);
    }
    
    public Game() {
    }
    
        public ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean value, float tpf) {
            if (name.equals("pick target")) {
                if (!value) { return; }
                
                CollisionResults results = new CollisionResults();
                
                Vector2f click2d = Main.app.getInputManager().getCursorPosition();
                Vector3f click3d = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 0f).clone();
        
                Vector3f dir = cam.getWorldCoordinates(
                    new Vector2f(click2d.x, click2d.y), 1f).normalizeLocal();
                
                Ray ray = new Ray(click3d, dir);
                
                gameNode.collideWith(ray, results);
                
                if(results.size() < 1) { return; }
                
                CollisionResult cr = results.getClosestCollision();
		
                String target = cr.getGeometry().getName();
                Vector3f pt = cr.getContactPoint();
                float dist = cr.getDistance();
                
                applyShockWave(pt);
            }
        };
    };
    
    public void applyShockWave(Vector3f vec) {
        //for(Plupp p : plupps) {
        for(int i = 0;i<plupps.size();i++) {
            Plupp p = plupps.get(i);
            
            Vector3f ppos = p.geometry.geometry.getLocalTranslation();
            float distance = ppos.distance(vec);

            //the following are merely test values
            float maxDistance = 2;
            float maxSpeed = 6000; //in ups
            
            if(distance>maxDistance) continue;
            if(distance<-maxDistance) continue;
            
            //float speed = maxSpeed - maxSpeed*(distance/maxDistance);
            float speed = (FastMath.abs(maxDistance-distance))*maxSpeed;

            //get direction between shock point and plupp
            Vector3f direction =
                    p.geometry.geometry.getLocalTranslation().subtract(vec).
                    normalize();
            
            Vector3f pos = p.geometry.geometry.getLocalTranslation();
            Vector2f dir = new Vector2f(pos.x-vec.x, pos.z-vec.z).mult(speed);
            
            //set velocity to that direction times the previously calculated speed
            //note: this disregards any current velocity on the plupp, we might want to
            //consider rewriting this bit because of that. the current version is just
            //for testing purposes.
            p.velocity = direction.mult(speed);
            p.velocity = new Vector3f(dir.x, 0, dir.y);
            p.velocity.y = 0;
            System.out.println(p.velocity);

            //debug text
            System.out.println(distance);
            
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
        
        gameNode = new Node();
        
        gameNode.attachChild(
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
        
        //Spatial arena = assetManager.loadModel("Models/alpha_arena.obj");
        //arena.setMaterial(mat);
        
        //rootNode.attachChild(arena);
    }
    
    float t = 0;
    
    public void Update(float tpf) {
        t += tpf;

        for(Plupp p : plupps) {
            //p.velocity = new Vector3f(500,0,0);
            p.geometry.move(p.velocity.mult(tpf/1000f));
        }
    }
}
