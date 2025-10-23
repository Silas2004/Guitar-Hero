package controller;

import java.util.HashSet;
import java.util.Set;
import model.GameSettings;
import services.SettingsService;

public class InputHandler {
    private final Set<Character> validKeys;

    public InputHandler() {
        validKeys = new HashSet<>();
        loadKeysFromSettings();
    }

    private void loadKeysFromSettings() {
        GameSettings settings = SettingsService.getInstance().getSettings();
        
        validKeys.add(Character.toUpperCase(settings.getKeyLane1()));
        validKeys.add(Character.toUpperCase(settings.getKeyLane2()));
        validKeys.add(Character.toUpperCase(settings.getKeyLane3()));
        validKeys.add(Character.toUpperCase(settings.getKeyLane4()));
    }

    public boolean isValidKey(char key) {
        return validKeys.contains(Character.toUpperCase(key));
    }
    
    public void reloadKeys() {
        validKeys.clear();
        loadKeysFromSettings();
    }
}