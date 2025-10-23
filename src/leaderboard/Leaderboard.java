package leaderboard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Leaderboard implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<PlayerScore> scores;

    public Leaderboard() {
        this.scores = new ArrayList<>();
    }

    public void addScore(PlayerScore score) {
        if (score != null) {
            scores.add(score);
            Collections.sort(scores);
        }
    }

    public List<PlayerScore> getScores() {
        return scores;
    }
}