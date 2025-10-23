package controller;

import model.GameModel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameController extends KeyAdapter {
    private final GameModel model;
    private final InputHandler inputHandler;

    public GameController(GameModel model) {
        this.model = model;
        this.inputHandler = new InputHandler();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        char keyChar = Character.toUpperCase(e.getKeyChar());
        if (inputHandler.isValidKey(keyChar)) {
            model.tryHit(keyChar, 30);
        }
    }
}
