package mygame;

import com.jme3.input.MouseInput;
import com.jme3.input.controls.MouseButtonTrigger;

public class Controls {
    public static void Setup() {
        Game.game.inputManager.addMapping(
            "pick target", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        Game.game.inputManager.addListener(
            Game.game.actionListener, "pick target");
    }
}
