package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

/**
 * test
 * @author normenhansen
 */

public class Main extends SimpleApplication {
    public ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean value, float tpf) {
            if (name.equals("pick target")) {
                if (!value) { return; }
                
                CollisionResults results = new CollisionResults();
                
                Vector2f click2d = inputManager.getCursorPosition();
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
		
            	/*System.out.println(target+" at "+pt+
                    ", "+dist+" WU away.");*/
                
                game.applyShockWave(pt);
            }
        };
    };
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    private void setup() {
        this.flyCam.setEnabled(false);
        this.cam.setLocation(new Vector3f(0,1,0).mult(30));
        this.cam.lookAt(new Vector3f(0,0,0),new Vector3f(-1,0,0));

        Spatial skybox = SkyFactory.createSky(
            assetManager, "Textures/Penguins.jpg", true);
        skybox.move(0,-100,0);
        
        rootNode.attachChild(skybox);
    }
    
    Game game;
    Node gameNode;
    
    @Override
    public void simpleInitApp() {
        setup();
        
        game = new Game();
        game.assetManager = assetManager;
        game.rootNode = (gameNode = new Node());
        game.cam = this.cam;
        rootNode.attachChild(gameNode);
        game.Start();
        
        inputManager.addMapping(
            "pick target", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "pick target");
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        game.Update(tpf);
    }
    
    @Override
    public void simpleRender(RenderManager rm) {
    }
}
