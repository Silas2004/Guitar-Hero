package cmd;

import javax.swing.SwingUtilities;

import controller.MainController;

public class Main {
	
	//Start Game
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainController().start());
    }
}
