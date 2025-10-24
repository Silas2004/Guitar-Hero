package model;

import observer.GameObservable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameModel extends GameObservable {
    private List<Lane> lanes;
    private Score score;
    private GameSettings settings;
    private double currentSpeed;
    private long elapsedTime;
    private Random random;
    private boolean gameOver;
    private int hitLineY;
    private int lives;
    
    private static final int HIT_TOLERANCE = 40;
    

    public GameModel(GameSettings settings, int hitLineY) {
        this.settings = settings;
        this.hitLineY = hitLineY;
        this.lanes = new ArrayList<>();
        this.score = new Score();
        this.currentSpeed = settings.getBaseSpeed();
        this.elapsedTime = 0L;
        this.random = new Random();
        this.gameOver = false;
        this.lives = settings.getStartLives();
        initLanes();
    }

    // Filling List Interface with the Lanes for the UI
    private void initLanes() {
        lanes.clear();
        lanes.add(new Lane(100, settings.getKeyLane1()));
        lanes.add(new Lane(200, settings.getKeyLane2()));
        lanes.add(new Lane(300, settings.getKeyLane3()));
        lanes.add(new Lane(400, settings.getKeyLane4()));
    }
    

    public void reset() {
        score = new Score();
        currentSpeed = settings.getBaseSpeed();
        elapsedTime = 0L;
        gameOver = false;
        lives = settings.getStartLives();
        lanes.clear();
        initLanes();
        notifyStateChanged();
    }
    
 
    public void update(long deltaMillis) {
        if (gameOver) return;
        
        double increment = settings.getSpeedIncrement() * (deltaMillis / 1000.0);
        currentSpeed = Math.min(settings.getMaxSpeed(), currentSpeed + increment);
        
        for (Lane lane : lanes) {
            lane.updateNotes(currentSpeed);
        }
        
        int bottomY = hitLineY + 200;
        for (Lane lane : lanes) {
            int missedNotes = lane.removeHitOrMissedNotes(bottomY);
            for (int i = 0; i < missedNotes; i++) {
                loseLife();
            }
        }
        
        notifyStateChanged();
    }
    

    public boolean tryHit(char key, int toleranceY) {
        for (Lane lane : lanes) {
            if (lane.getKey() == key) {
                Note closestNote = null;
                int closestDiff = Integer.MAX_VALUE;
                
                for (Note n : lane.getNotes()) {
                    if (!n.isHit()) {
                        int diff = Math.abs(n.getY() - hitLineY);
                        if (diff < closestDiff && diff <= toleranceY) {
                            closestNote = n;
                            closestDiff = diff;
                        }
                    }
                }
                
                if (closestNote != null) {
                    closestNote.setHit(true);
                    int points = calculatePoints(closestDiff);
                    score.registerHit(points);
                    notifyNoteHit(points);
                    notifyStateChanged();
                    return true;
                }
            }
        }
        
        score.registerMiss();
        notifyNoteMissed();
        notifyStateChanged();
        return false;
    }
    

    public void spawnRandomNote() {
        if (lanes.isEmpty()) return;
        Lane lane = lanes.get(random.nextInt(lanes.size()));
        Note note = new Note(lane.getX(), 0, lane.getKey());
        lane.addNote(note);
        notifyStateChanged();
    }
    
    private void loseLife() {
        lives--;
        if (lives <= 0) {
            gameOver = true;
            notifyGameOver(score.getTotalScore());
        }
        notifyStateChanged();
    }
    
    private int calculatePoints(int diff) {
        int base = 100;
        if (diff < 5) {
            return base + 50; // Perfect: 150
        } else if (diff < 15) {
            return base + 30; // Great: 130
        } else if (diff < 25) {
            return base + 20; // Good: 120
        } else {
            return base; // OK: 100
        }
    }
    
    // Getters
    public int getHitLineY() { return hitLineY; }
    public List<Lane> getLanes() { return lanes; }
    public Score getScore() { return score; }
    public double getCurrentSpeed() { return currentSpeed; }
    public boolean isGameOver() { return gameOver; }
    public int getLives() { return lives; }
    public static int getHitTolerance() { return HIT_TOLERANCE; }
}
