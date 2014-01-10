package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class Main extends SimpleApplication {
    public static Main app;
    
    public static void main(String[] args) {
        app = new Main();
        app.setShowSettings(false);
        app.start();
    }
    
    private void setup() {
        this.flyCam.setEnabled(false);
        this.cam.setLocation(new Vector3f(0,1,0).mult(30));
        this.cam.lookAt(new Vector3f(0,0,0),new Vector3f(0,0,-1));

        //this.setDisplayStatView(false);
        
        //light
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
        rootNode.addLight(sun);
    }
    
    @Override
    public void simpleInitApp() {
        setup();
        
        Game.game = new Game();
        Game.game.assetManager = assetManager;
        Game.game.rootNode = rootNode;
        Game.game.inputManager = inputManager;
        Game.game.cam = this.cam;
        Game.game.Start();
        
        Controls.Setup();
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        Game.game.Update(tpf);
    }
}
