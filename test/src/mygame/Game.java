package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.TouchListener;
import com.jme3.input.event.TouchEvent;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

//migrate input out of this file pls

public class Game {
    public static Game game;
    
    AssetManager assetManager;
    Node rootNode;
    Node gameNode;
    Node guiNode;
    InputManager inputManager;
    Camera cam;
    
    Dictionary<String, Material> materials;
    public List<Plupp> plupps;
    
    public kRectangle gameArea;
    kRectangle redScoreArea;
    kRectangle bluScoreArea;
    
    List<Plupp> respawnQueue;
    float respawnTime = 1.5f; //s
    float respawnTimer;
    
    int redScore = 0;
    int bluScore = 0;
    
    public BitmapFont font;
    BitmapText gameLog;
    List<String> logStrings;
    
    public Material loadMaterial(String path)
    {
        if(materials.get(path) == null) {
            materials.put(path, new Material(assetManager, path)); }
        return materials.get(path);
    }
    
    public TouchListener touchListener = new TouchListener()
    {
        public void onTouch(String name, TouchEvent evt, float tpf) {
            if(evt.getType() != TouchEvent.Type.DOWN) return;
            CollisionResults results = new CollisionResults();
                
            Vector3f click3d = cam.getWorldCoordinates(
                new Vector2f(evt.getX(), evt.getY()), 0f).clone();

            Vector3f dir = cam.getWorldCoordinates(
                new Vector2f(evt.getX(), evt.getY()), 1f).normalizeLocal();

            Ray ray = new Ray(click3d, dir);
            gameNode.collideWith(ray, results);
            if(results.size() < 1) { return; }

            CollisionResult cr = results.getClosestCollision();

            Vector3f pt = cr.getContactPoint();

            applyShockWave(pt);
        }
    };
    
    public ActionListener actionListener = new ActionListener()
    {
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
    
    public void applyShockWave(Vector3f vec)
    {
        for(int i = 0;i<plupps.size();i++) {
            Plupp p = plupps.get(i);
            
            Vector3f ppos = p.geometry.geometry.getLocalTranslation();
            float distance = ppos.distance(vec);

            //the following are merely test values
            float maxDistance = 5;
            
            if(FastMath.abs(
                distance) > maxDistance) { continue; }
            
            distance = Math.min(distance, maxDistance);
            
            float speed = 0;
            speed = maxDistance - distance;
            speed /= maxDistance;
            speed *= speed;
            speed *= Plupp.PushStrength;

            Vector3f pos = p.geometry.geometry.getLocalTranslation();
            Vector2f dir = new Vector2f(pos.x-vec.x, pos.z-vec.z).mult(speed);
            
            p.ApplyForce(dir);

            plupps.set(i, p);
        }
    }
    
    public Plupp createPlupp()
    {
        Plupp p = new Plupp();
        plupps.add(p);
        return p;
    }
    
    public void respawn(Plupp p)
    {
    }
    
    void log(String s)
    {
        //join here ok logStrings.
        gameLog.setText(s);
    }
    
    public void Start() {
        gameArea = new kRectangle(-16, -10, 32, 20);
        redScoreArea = new kRectangle(-10, -10, 3, 3);
        bluScoreArea = new kRectangle(7, 7, 3, 3);
        
        plupps = new ArrayList();
        
        respawnTimer = 0;
        
        gameLog = new BitmapText(font, false);
        gameLog.setSize(font.getCharSet().getRenderedSize());
        gameLog.setLocalTranslation(200, 100, 0);
        gameLog.setColor(ColorRGBA.White);
        guiNode.attachChild(gameLog);
        logStrings = new ArrayList<String>();
        
        //migrate everything below here out somewhere? idk seems messy atm
        
        Material mat = new Material(
            assetManager,
            "Common/MatDefs/Misc/Unshaded.j3md"
        );
        
        mat.setTexture("ColorMap",
            assetManager.loadTexture("Textures/url.png")
        );
        
        (gameNode = new Node()).attachChild(
            (new BoxGeomWrapper())
                .setGeometry(Helper.createCube(16,0.1f,10,"alfred"))
                .setMaterial(mat)
                .getGeometry()
        );
        
        rootNode.attachChild(gameNode);
        
        Plupp p;
        
        rootNode.attachChild(
            ((p = createPlupp()).geometry = new BoxGeomWrapper()
                .setGeometry(Helper.createCube(1.1f,1,1.1f,"plupp"))
                .setMaterial(mat)).move(new Vector3f(-2,0,-2))
            .getGeometry()
        );
        
        rootNode.attachChild(
            ((p = createPlupp()).geometry = new BoxGeomWrapper()
                .setGeometry(Helper.createCube(1.1f,1,1.1f,"plupp"))
                .setMaterial(mat))
            .getGeometry()
        );
    }
    
    public void Update(float tpf) {
        respawnTimer += tpf;
        
        if(respawnTimer >= respawnTime)
        {
            log("yolo\nswag");
        }
        
        for(Plupp p : plupps)
        {
            p.bounced = false;
        }
        
        for(Plupp p : plupps)
        {
            //keep in bounds
            p.bounds();
            //calculate bouncing
            p.bounce();
            
            p.move(tpf);
            
            if(p.insideArea(redScoreArea)) {
                bluScore+=1;
                System.out.println("Red "+redScore+" - Blue "+bluScore);
                //respawn
            }
            
            if(p.insideArea(bluScoreArea)) {
                redScore+=1;
                System.out.println("Red "+redScore+" - Blue "+bluScore);
                //respawn
            }
            
            p.velocity = p.velocity.mult(0.99999f);
        }
    }
}