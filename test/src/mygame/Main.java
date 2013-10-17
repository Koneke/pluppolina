package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseButtonTrigger;
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
    public static Main app;
    
    public static void main(String[] args) {
        app = new Main();
        app.start();
    }
    
    public InputManager getInputManager() {
        return inputManager;
    }
    
    private void setup() {
        this.flyCam.setEnabled(false);
        this.cam.setLocation(new Vector3f(0,1,0).mult(30));
        this.cam.lookAt(new Vector3f(0,0,0),new Vector3f(0,0,-1));

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
        inputManager.addListener(game.actionListener, "pick target");
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        game.Update(tpf);
    }
    
    @Override
    public void simpleRender(RenderManager rm) {
    }
}
