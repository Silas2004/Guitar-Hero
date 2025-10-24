package state;

import view.GameView;

import java.awt.Graphics;

//State Pattern for each State

public class PauseState implements GameState {
    private final GameView view;

    public PauseState(GameView view) {
        this.view = view;
    }

    @Override
    public void enter() {}

    @Override
    public void exit() {}

    @Override
    public void update(long deltaMillis) {}
}
