package cmd;

import javax.swing.SwingUtilities;

import controller.MainController;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainController().start());
    }
}
