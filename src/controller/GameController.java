package controller;

import model.GameModel;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class GameController extends KeyAdapter {
    private final GameModel model;
    private final CommandExecutor executor;
    private final InputHandler inputHandler;
    private final Map<Integer, Command> keyCommandMap;
    
    public GameController(GameModel model) {
        this.model = model;
        this.executor = new CommandExecutor();
        this.inputHandler = new InputHandler();
        this.keyCommandMap = new HashMap<>();
    }
    
    public void registerKeyCommand(int keyCode, Command command) {
        keyCommandMap.put(keyCode, command);
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        e.consume();
        
        Command registeredCommand = keyCommandMap.get(e.getKeyCode());
        if (registeredCommand != null) {
            executor.execute(registeredCommand);
            return;
        }
        
        char keyChar = Character.toUpperCase(e.getKeyChar());
        if (inputHandler.isValidKey(keyChar)) {
            Command hitCommand = new HitLaneCommand(
                model, 
                keyChar, 
                GameModel.getHitTolerance()
            );
            executor.execute(hitCommand);
        }
    }
    
    // Relead Keybindings than changing or Importing Settings
    public void reloadKeys() {
        inputHandler.reloadKeys();
    }
    
    // Implement Command Executor for Calling
    public CommandExecutor getExecutor() {
        return executor;
    }
}
