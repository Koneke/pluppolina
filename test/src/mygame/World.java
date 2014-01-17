/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Administratör
 */
public class World {
    public void Load(Node rootNode) {
        //arena
        Material matArena = new Material(
            Game.game.assetManager,
            "Common/MatDefs/Light/Lighting.j3md"
        );
        matArena.setTexture("DiffuseMap",
            Game.game.assetManager.loadTexture("Models/alpha_arena_color.png")
        );
        //matArena.setTexture("NormalMap", 
        //      assetManager.loadTexture("Models/alpha_arena_normal.png"));
        matArena.setTexture("SpecularMap", 
                Game.game.assetManager.loadTexture("Models/alpha_arena_specular.png")); 

        matArena.setBoolean("UseMaterialColors",true);    
        matArena.setColor("Diffuse",ColorRGBA.White);  // minimum material color
        matArena.setColor("Specular",ColorRGBA.White); // for shininess
        matArena.setFloat("Shininess", 64f); // [1,128] for shininess

        Spatial arena = Game.game.assetManager.loadModel("Models/alpha_arena.j3o");
        arena.setMaterial(matArena);
        
        rootNode.attachChild(arena);
        
        //ground
        Material matGround = new Material(
            Game.game.assetManager,
            "Common/MatDefs/Light/Lighting.j3md"
        );
        matGround.setTexture("DiffuseMap",
            Game.game.assetManager.loadTexture("Models/alpha_ground_color.png")
        );
        //matGround.setTexture("NormalMap", 
        //      assetManager.loadTexture("Models/alpha_ground_normal.png"));
        matGround.setTexture("SpecularMap", 
                Game.game.assetManager.loadTexture("Models/alpha_ground_specular.png")); 


        Spatial ground = Game.game.assetManager.loadModel("Models/alpha_ground.j3o");
        ground.setMaterial(matGround);
        
        rootNode.attachChild(ground);
        
        //palm
        Material matPalm = new Material(
            Game.game.assetManager,
            "Common/MatDefs/Light/Lighting.j3md"
        );
        matPalm.setTexture("DiffuseMap",
        Game.game.assetManager.loadTexture("Models/alpha_palm_color.png")
        );
        //matPalm.setTexture("NormalMap", 
        //      assetManager.loadTexture("Models/alpha_palm_normal.png"));
        matPalm.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        
        Spatial palm = Game.game.assetManager.loadModel("Models/alpha_palm.j3o");
        palm.setMaterial(matPalm);
        
        rootNode.attachChild(palm);
        
        //tree
        Material matTree = new Material(
            Game.game.assetManager,
            "Common/MatDefs/Light/Lighting.j3md"
        );
        matTree.setTexture("DiffuseMap",
            Game.game.assetManager.loadTexture("Models/alpha_tree_color.png")
        );
        //matTree.setTexture("NormalMap", 
        //      assetManager.loadTexture("Models/alpha_tree_normal.png"));
        matTree.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

        Spatial tree = Game.game.assetManager.loadModel("Models/alpha_tree.j3o");
        tree.setMaterial(matTree);
        
        rootNode.attachChild(tree);
        
        //plupp
        Plupp p;
        
        rootNode.attachChild(
            ((p = Game.game.createPlupp()).geometry = new BoxGeomWrapper()
                .setGeometry(Helper.createCube(1,1,1,"plupp"))
                .setMaterial(matTree))
            .getGeometry()
        );
    }
}
