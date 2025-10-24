package controller;

import dispatcher.GameDispatcher;
import model.GameModel;
import model.GameSettings;
import observer.GameObserver;
import services.LeaderboardService;
import services.SettingsService;
import state.*;
import view.*;

import javax.swing.*;
import java.awt.event.*;

/**
 * Main controller coordinating game components.
 * Manages game states, view transitions, and user input.
 */
public class MainController implements GameObserver {
    private JFrame frame;
    private GameModel model;
    private GameController controller;
    private GameDispatcher dispatcher;

    private GameState currentState;

    private MenuState menuState;
    private PlayState playState;
    private PauseState pauseState;
    private GameOverState gameOverState;
    private SettingsState settingsState;

    private MenuView menuView;
    private GameView gameView;
    private GameOverView gameOverView;
    private GameSettingsView gameSettingsView;

    private boolean isPaused = false;
    private String playerName = "Player";
    
    private static final int HIT_LINE_Y = 700;

    public void start() {
        initializeFrame();
        initializeGame();
        initializeViews();
        initializeStates();
        
        setState(menuState);
        
        frame.setVisible(true);
        setupInputHandling();
        startGameLoop();
    }
    
    private void initializeFrame() {
        frame = new JFrame("Guitar Hero");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 800);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setFocusTraversalKeysEnabled(false);
    }

    private void initializeGame() {
        GameSettings settings = SettingsService.getInstance().getSettings();
        model = new GameModel(settings, HIT_LINE_Y);
        model.addObserver(this);
        
        controller = new GameController(model);
        dispatcher = new GameDispatcher(model);
    }
    
    private void initializeViews() {
        menuView = new MenuView();
        menuView.setOnStartGame(() -> {
            if (menuView.isPlayerNameValid()) {
                playerName = menuView.getPlayerName();
                startGame();
            } else {
                menuView.showInvalidNameError();
            }
        });
        menuView.setOnOpenSettings(() -> {
            setState(settingsState);
        });
        
        gameView = new GameView(model);
        gameOverView = new GameOverView(0);
        
        gameSettingsView = new GameSettingsView();
        gameSettingsView.setOnBack(() -> {
            if (isPaused) {
                setState(pauseState);
            } else {
                setState(menuState);
            }
        });
        gameSettingsView.setOnSettingsSaved(() -> {
            if (controller != null) {
                controller.reloadKeys();
            }
        });
    }
    
    private void initializeStates() {
        menuState = new MenuState(controller, menuView);
        playState = new PlayState(model, controller, gameView);
        pauseState = new PauseState(gameView);
        gameOverState = new GameOverState(gameOverView);
        settingsState = new SettingsState(gameSettingsView);
    }
    
    private void setupInputHandling() {
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                e.consume();
                
                if (currentState == playState) {
                    handlePlayStateInput(e);
                } else if (currentState == pauseState) {
                    handlePauseStateInput(e);
                } else if (currentState == menuState) {
                    handleMenuStateInput(e);
                } else if (currentState == gameOverState) {
                    handleGameOverStateInput(e);
                }
            }
        });
    }
    
    private void handlePlayStateInput(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {
            togglePause();
        } else {
            controller.keyPressed(e);
        }
    }
    
    private void handlePauseStateInput(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {
            togglePause();
        }
    }
    
    private void handleMenuStateInput(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            setState(settingsState);
        }
    }
    
    private void handleGameOverStateInput(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            setState(menuState);
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            setState(settingsState);
        }
    }
    
    private void startGameLoop() {
        dispatcher.start();
        
        Timer gameLoopTimer = new Timer(16, e -> {
            if (currentState == playState && !isPaused) {
                model.update(16);
            }
            frame.repaint();
        });
        gameLoopTimer.setCoalesce(true);
        gameLoopTimer.start();
    }

    private void setState(GameState newState) {
        if (currentState != null) currentState.exit();
        currentState = newState;
        currentState.enter();

        updateView(newState);
        
        frame.revalidate();
        frame.repaint();
        
        SwingUtilities.invokeLater(() -> frame.requestFocus());
    }
    
    private void updateView(GameState state) {
        if (state instanceof MenuState) {
            frame.setContentPane(menuView);
            SwingUtilities.invokeLater(() -> menuView.focusNameField());
        } else if (state instanceof PlayState || state instanceof PauseState) {
            frame.setContentPane(gameView);
            gameView.setFocusable(true);
            SwingUtilities.invokeLater(() -> gameView.requestFocusInWindow());
        } else if (state instanceof GameOverState) {
            frame.setContentPane(gameOverView);
            SwingUtilities.invokeLater(() -> frame.requestFocus());
        } else if (state instanceof SettingsState) {
            frame.setContentPane(gameSettingsView);
        }
    }
    
    private void startGame() {
        GameSettings settings = SettingsService.getInstance().reloadSettings();
        model = new GameModel(settings, HIT_LINE_Y);
        model.addObserver(this);
        
        controller = new GameController(model);
        dispatcher = new GameDispatcher(model);
        gameView = new GameView(model);
        playState = new PlayState(model, controller, gameView);
        pauseState = new PauseState(gameView);
        
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
        isPaused = false;
        int finalScore = model.getScore().getTotalScore();
        LeaderboardService.getInstance().addScore(
                new leaderboard.PlayerScore(playerName, finalScore));
        gameOverView = new GameOverView(finalScore);
        gameOverState = new GameOverState(gameOverView);
        setState(gameOverState);
    }
    
    @Override
    public void onGameStateChanged() {
    }
    
    //Maybe soudn effects in future?, should be similiar to the songs in the xamarin Version
    @Override
    public void onNoteHit(int points) {
    }
    
    @Override
    public void onNoteMissed() {
    }
    
    @Override
    public void onGameOver(int finalScore) {
        SwingUtilities.invokeLater(this::endGame);
    }
}