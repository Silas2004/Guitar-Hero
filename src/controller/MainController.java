package controller;

import controller.GameController;
import dispatcher.GameDispatcher;
import leaderboard.LeaderboardService;
import model.GameModel;
import model.GameSettings;
import state.*;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainController {
    private JFrame frame;
    private GameModel model;
    private GameController controller;
    private GameDispatcher dispatcher;

    private GameState currentState;

    private MenuState menuState;
    private PlayState playState;
    private PauseState pauseState;
    private GameOverState gameOverState;

    private MenuView menuView;
    private GameView gameView;
    private GameOverView gameOverView;

    private boolean isPaused = false;
    private String playerName = "Player";

    public void start() {
        frame = new JFrame("Guitar Hero");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 800);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        GameSettings settings = new GameSettings(100, 1.5, 10, 3);
        int hitLineY = 700;

        model = new GameModel(settings, hitLineY);
        controller = new GameController(model);
        dispatcher = new GameDispatcher(model);

        menuView = new MenuView();
        menuView.setOnStartGame(() -> {
            if (menuView.isPlayerNameValid()) {
                playerName = menuView.getPlayerName();
                startGame();
            } else {
                menuView.showInvalidNameError();
            }
        });
        gameView = new GameView(model);
        gameOverView = new GameOverView(0);

        menuState = new MenuState(controller, menuView);
        playState = new PlayState(model, controller, gameView);
        pauseState = new PauseState(gameView);
        gameOverState = new GameOverState(gameOverView);

        setState(menuState);

        frame.setVisible(true);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (currentState == playState) {
                    if (e.getKeyCode() == KeyEvent.VK_P) {
                        togglePause();
                    } else {
                        controller.keyPressed(e);
                    }
                } else if (currentState == pauseState) {
                    if (e.getKeyCode() == KeyEvent.VK_P) {
                        togglePause();
                    }
                } else if (currentState == gameOverState) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        setState(menuState);
                    }
                }
            }
        });

        dispatcher.start();

        Timer gameLoopTimer = new Timer(16, e -> {
            if (currentState == playState && !isPaused) {
                dispatcher.start();
                model.update(16);
                if (model.isGameOver()) {
                    endGame();
                }
            }
            frame.repaint();
        });
        gameLoopTimer.start();
    }

    private void setState(GameState newState) {
        if (currentState != null) currentState.exit();
        currentState = newState;
        currentState.enter();

        if (newState instanceof MenuState) {
            frame.setContentPane(menuView);
            menuView.focusNameField();
        } else if (newState instanceof PlayState) {
            frame.setContentPane(gameView);
            gameView.setFocusable(true);
            gameView.requestFocusInWindow();
        } else if (newState instanceof PauseState) {
            frame.setContentPane(gameView);
            gameView.setFocusable(true);
            gameView.requestFocusInWindow();
        } else if (newState instanceof GameOverState) {
            frame.setContentPane(gameOverView);
        }

        frame.revalidate();
        frame.repaint();
        
        SwingUtilities.invokeLater(() -> {
            frame.requestFocus();
        });
    }
    private void startGame() {
        model.reset();
        isPaused = false;
        dispatcher.start();
        setState(playState);
    }

    private void togglePause() {
        if (isPaused) {
            dispatcher.start();
            setState(playState);
        } else {
            dispatcher.stop();
            setState(pauseState);
        }
        isPaused = !isPaused;
    }

    private void endGame() {
        dispatcher.stop();
        int finalScore = model.getScore().getTotalScore();
        LeaderboardService.getInstance().addScore(
                new leaderboard.PlayerScore(playerName, finalScore));
        gameOverView = new GameOverView(finalScore);
        gameOverState = new GameOverState(gameOverView);
        setState(gameOverState);
    }
}