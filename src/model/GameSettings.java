package model;

import java.io.ObjectInputStream.GetField;

public class GameSettings {
    private double baseSpeed;      
    private double speedIncrement;  
    private double maxSpeed;  
    private int startLives;

    public GameSettings(double baseSpeed, double speedIncrement, double maxSpeed, int startLives) {
        this.baseSpeed = baseSpeed;
        this.speedIncrement = speedIncrement;
        this.maxSpeed = maxSpeed;
        this.startLives = startLives;
    }

    public int getStartLives() {
        return startLives;
    }

    public void setStartLives(int startLives) {
        this.startLives = startLives;
    }
    
    public double getBaseSpeed() {
        return baseSpeed;
    }

    public void setBaseSpeed(double baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    public double getSpeedIncrement() {
        return speedIncrement;
    }

    public void setSpeedIncrement(double speedIncrement) {
        this.speedIncrement = speedIncrement;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
}
