package view;

import services.*;
import leaderboard.PlayerScore;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LeaderboardView extends JPanel {

    public LeaderboardView() {
        setPreferredSize(new Dimension(600, 800));
        setBackground(Color.black);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.orange);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("LEADERBOARD", 150, 80);

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 22));

        List<PlayerScore> scores = LeaderboardService.getInstance().getLeaderboard().getScores();

        int y = 150;
        int rank = 1;
        for (PlayerScore ps : scores) {
            if (rank > 10) break;
            
            String line = String.format("%d. %s - %d", rank++, ps.getPlayerName(), ps.getScore());
            g.drawString(line, 180, y);
            y += 32;
        }

        if (scores.isEmpty()) {
            g.drawString("No scores yet.", 220, y);
        }
    }
}