package state;

import controller.GameController;
import model.GameModel;
import view.GameView;

import java.awt.Graphics;

//State Pattern for each State

public class PlayState implements GameState {
    private final GameModel model;
    private final GameController controller;
    private final GameView view;

    public PlayState(GameModel model, GameController controller, GameView view) {
        this.model = model;
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void enter() {}

    @Override
    public void exit() {}

    @Override
    public void update(long deltaMillis) {
        model.update(deltaMillis);
    }
}
