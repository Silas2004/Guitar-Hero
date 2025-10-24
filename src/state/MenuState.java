package state;

import controller.GameController;
import view.MenuView;

import java.awt.Graphics;

//State Pattern for each State

public class MenuState implements GameState {
    private final GameController controller;
    private final MenuView view;

    public MenuState(GameController controller, MenuView view) {
        this.controller = controller;
        this.view = view;
    }

    @Override
    public void enter() {}

    @Override
    public void exit() {}

    @Override
    public void update(long deltaMillis) {}
}
