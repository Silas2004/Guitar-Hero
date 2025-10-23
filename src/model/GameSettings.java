package model;

public class GameSettings {
    private char keyLane1;
    private char keyLane2;
    private char keyLane3;
    private char keyLane4;
    
    private double baseSpeed;
    private double speedIncrement;
    private double maxSpeed;
    private int startLives;
    
    public GameSettings() {
        this.keyLane1 = 'A';
        this.keyLane2 = 'S';
        this.keyLane3 = 'D';
        this.keyLane4 = 'F';
        this.baseSpeed = 1.5;
        this.speedIncrement = 0.1;
        this.maxSpeed = 5.0;
        this.startLives = 3;
    }
    
    public GameSettings(char keyLane1, char keyLane2, char keyLane3, char keyLane4,
                       double baseSpeed, double speedIncrement, double maxSpeed, int startLives) {
        this.keyLane1 = keyLane1;
        this.keyLane2 = keyLane2;
        this.keyLane3 = keyLane3;
        this.keyLane4 = keyLane4;
        this.baseSpeed = baseSpeed;
        this.speedIncrement = speedIncrement;
        this.maxSpeed = maxSpeed;
        this.startLives = startLives;
    }
    
    public char getKeyLane1() { return keyLane1; }
    public void setKeyLane1(char key) { this.keyLane1 = Character.toUpperCase(key); }
    
    public char getKeyLane2() { return keyLane2; }
    public void setKeyLane2(char key) { this.keyLane2 = Character.toUpperCase(key); }
    
    public char getKeyLane3() { return keyLane3; }
    public void setKeyLane3(char key) { this.keyLane3 = Character.toUpperCase(key); }
    
    public char getKeyLane4() { return keyLane4; }
    public void setKeyLane4(char key) { this.keyLane4 = Character.toUpperCase(key); }
    
    public double getBaseSpeed() { return baseSpeed; }
    public void setBaseSpeed(double speed) { this.baseSpeed = speed; }
    
    public double getSpeedIncrement() { return speedIncrement; }
    public void setSpeedIncrement(double increment) { this.speedIncrement = increment; }
    
    public double getMaxSpeed() { return maxSpeed; }
    public void setMaxSpeed(double speed) { this.maxSpeed = speed; }
    
    public int getStartLives() { return startLives; }
    public void setStartLives(int lives) { this.startLives = lives; }
}