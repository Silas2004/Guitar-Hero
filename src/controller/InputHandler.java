package controller;

import java.util.HashSet;
import java.util.Set;

public class InputHandler {
    private final Set<Character> validKeys;

    public InputHandler() {
        validKeys = new HashSet<>();
        validKeys.add('A');
        validKeys.add('S');
        validKeys.add('D');
        validKeys.add('F');
    }

    public boolean isValidKey(char key) {
        return validKeys.contains(Character.toUpperCase(key));
    }
}
