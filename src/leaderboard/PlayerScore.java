package leaderboard;

import java.io.Serializable;
import java.time.LocalDateTime;

/*
 * Playerscore of ever single Player. The leaderboard system is based on this model. But only needed for Leaderboard. So it shouldnt be a general Model i hope xD.
 */

public class PlayerScore implements Serializable, Comparable<PlayerScore> {
    private static final long serialVersionUID = 1L;

    private String playerName;
    private int score;
    private LocalDateTime timestamp;

    public PlayerScore(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
        this.timestamp = LocalDateTime.now();
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(PlayerScore other) {
        return Integer.compare(other.score, this.score);
    }
}
