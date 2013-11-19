package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.PssmShadowRenderer;
import com.jme3.util.SkyFactory;

/**
 * test
 * @author normenhansen
 */

public class Main extends SimpleApplication {
    
    private PssmShadowRenderer pssmRenderer;
    
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
                
                game.applyShockWave(pt);
            }
        };
    };
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    private void setup() {
        this.flyCam.setEnabled(true);
        this.flyCam.setMoveSpeed(100);
        this.cam.setLocation(new Vector3f(0,0.85f,0).mult(30));
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
        
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(0,-1,-2).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        
        game.sun = sun;
        
        inputManager.addMapping(
            "pick target", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "pick target");
                
        //PSSM
        pssmRenderer = new PssmShadowRenderer(assetManager, 1024, 3);
        viewPort.addProcessor(pssmRenderer);
        
        //Point light
        PointLight lamp_light = new PointLight();
        lamp_light.setColor(ColorRGBA.Yellow.mult(10));
        lamp_light.setRadius(5);
        lamp_light.setPosition(new Vector3f(1,3,-6.5f));
        rootNode.addLight(lamp_light);
        
        //Box light
        Box box1 = new Box(.5f,.5f,.5f);
        Geometry blue = new Geometry("Box", box1);
        blue.setLocalTranslation(new Vector3f(1,3,-6.5f));
        Material mat1 = new Material(assetManager, 
                "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Blue);
        blue.setMaterial(mat1);
        
        rootNode.attachChild(blue);
        
        //ambient light
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1f));
        rootNode.addLight(al);
        
        //FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        //SSAOFilter ssaoFilter = new SSAOFilter(12.94f, 43.92f, 0.33f, 0.61f);
        //fpp.addFilter(ssaoFilter);
        //viewPort.addProcessor(fpp);
    }
    
    float t = 0;
    
    @Override
    public void simpleUpdate(float tpf) {
        game.Update(tpf);
        t += tpf;
        pssmRenderer.setDirection(new Vector3f(0,-1,0f+t*.1f).normalizeLocal());
    }
    
    @Override
    public void simpleRender(RenderManager rm) {
    }
}
