package model;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameModel {
    private List<Lane> lanes;
    private Score score;
    private GameSettings settings;
    private double currentSpeed;
    private long elapsedTime;
    private Random random;
    private boolean gameOver;
    private int hitLineY;
    private int lives; 
    
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
    
    public int getHitLineY() {
        return hitLineY;
    }
    
    private void initLanes() {
        lanes.clear();
        lanes.add(new Lane(100, 'A'));
        lanes.add(new Lane(200, 'S'));
        lanes.add(new Lane(300, 'D'));
        lanes.add(new Lane(400, 'F'));
    }
    
    public void reset() {
        score = new Score();
        currentSpeed = settings.getBaseSpeed();
        elapsedTime = 0L;
        gameOver = false;
        lives = settings.getStartLives();
        lanes.clear();
        initLanes();
    }
    
    public void addLane(Lane lane) {
        lanes.add(lane);
    }
    
    public List<Lane> getLanes() {
        return lanes;
    }
    
    public Score getScore() {
        return score;
    }
    
    public double getCurrentSpeed() {
        return currentSpeed;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public int getLives() {
        return lives; 
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
    }
    
    public boolean tryHit(char key, int toleranceY) {
        for (Lane lane : lanes) {
            if (lane.getKey() == key) {
                for (Note n : lane.getNotes()) {
                    int diff = Math.abs(n.getY() - hitLineY);
                    if (!n.isHit() && diff <= toleranceY) {
                        n.setHit(true);
                        score.registerHit(calculatePoints(diff));
                        return true;
                    }
                }
            }
        }
        score.registerMiss();
        return false;
    }
    
    private void loseLife() {
        lives--;
        if (lives <= 0) {
            gameOver = true;
        }
    }
    
    private int calculatePoints(int diff) {
        int base = 100;
        if (diff < 5) {
            return base + 50;
        } else if (diff < 20) {
            return base + 20;
        } else {
            return base;
        }
    }
    
    public void spawnRandomNote() {
        if (lanes.isEmpty()) return;
        Lane lane = lanes.get(random.nextInt(lanes.size()));
        Note note = new Note(lane.getX(), 0, lane.getKey());
        lane.addNote(note);
    }
}