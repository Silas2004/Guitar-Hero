package model;

// Score with combo System and Metrics for highest combo in Score
public class Score {
    private int totalScore;
    private int combo;  
    private int maxCombo; 

    public Score() {
        totalScore = 0;
        combo = 0;
        maxCombo = 0;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getCombo() {
        return combo;
    }

    public int getMaxCombo() {
        return maxCombo;
    }


    public void registerHit(int points) {
        totalScore += points;
        combo++;
        if (combo > maxCombo) {
            maxCombo = combo;
        }
    }


    public void registerMiss() {
        combo = 0;
    }
}
