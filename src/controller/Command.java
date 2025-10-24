package controller;

import model.GameModel;

public interface Command {
    void execute();

    default void undo() {
        // No Functionen need i hope xD
    }
}

// Command System for Hitting Lane
class HitLaneCommand implements Command {
    private final GameModel model;
    private final char key;
    private final int tolerance;
    
    public HitLaneCommand(GameModel model, char key, int tolerance) {
        this.model = model;
        this.key = key;
        this.tolerance = tolerance;
    }
    
    @Override
    public void execute() {
        model.tryHit(key, tolerance);
    }
}

// Command to Pause Game
class PauseCommand implements Command {
    private final Runnable pauseAction;
    
    public PauseCommand(Runnable pauseAction) {
        this.pauseAction = pauseAction;
    }
    
    @Override
    public void execute() {
        pauseAction.run();
    }
}

// Command to open Game
class OpenSettingsCommand implements Command {
    private final Runnable settingsAction;
    
    public OpenSettingsCommand(Runnable settingsAction) {
        this.settingsAction = settingsAction;
    }
    
    @Override
    public void execute() {
        settingsAction.run();
    }
}
