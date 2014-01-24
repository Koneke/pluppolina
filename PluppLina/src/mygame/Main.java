package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

public class Main extends SimpleApplication {
    public static Main app;
    
    public static void main(String[] args) {
        app = new Main();
        app.start();
    }
    
    private void setup() {
        this.flyCam.setEnabled(true);
        this.flyCam.setMoveSpeed(100);
        this.cam.setLocation(new Vector3f(0,0.815f,0).mult(30));
        //this.cam.setLocation(new Vector3f(0,0,0).
        this.cam.lookAt(new Vector3f(0,0,0),new Vector3f(-1,0,0));
        
        Spatial skybox = SkyFactory.createSky(
            assetManager, "Textures/Penguins.jpg", true);
        skybox.move(0,-100,0);
        
        rootNode.attachChild(skybox);
    }
    
    @Override
    public void simpleInitApp() {
        setup();
        
        Game.game = new Game();
        Game.game.assetManager = assetManager;
        Game.game.rootNode = rootNode;
        Game.game.guiNode = guiNode;
        Game.game.inputManager = inputManager;
        Game.game.cam = this.cam;
        Game.game.font = guiFont;
        Game.game.Start();
        
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(0,-1,-2).normalizeLocal());
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        
        Controls.Setup();
        
        Game.game.sun = sun;
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        Game.game.Update(tpf);
    }
    
    @Override
    public void simpleRender(RenderManager rm) {
    }
}
