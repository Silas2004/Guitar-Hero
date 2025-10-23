package state;

import view.GameOverView;

import java.awt.Graphics;

public class GameOverState implements GameState {
    private final GameOverView view;

    public GameOverState(GameOverView view) {
        this.view = view;
    }

    @Override
    public void enter() {}

    @Override
    public void exit() {}

    @Override
    public void update(long deltaMillis) {}
}
