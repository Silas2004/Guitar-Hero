package state;

import view.GameSettingsView;

//State Pattern for each State

public class SettingsState implements GameState {
    private GameSettingsView view;
    
    public SettingsState(GameSettingsView view) {
        this.view = view;
    }
    
    @Override
    public void enter() {
        System.out.println("Entering Settings State");
    }
    
    @Override
    public void exit() {
        System.out.println("Exiting Settings State");
    }
    
    @Override
    public void update(long deltaMillis) {
        //@Todo
    }
}