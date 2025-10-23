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
        g.drawString("LEADERBOARD", 150, 100);

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 24));

        List<PlayerScore> scores = LeaderboardService.getInstance().getLeaderboard().getScores();

        int y = 160;
        int rank = 1;
        for (PlayerScore ps : scores) {
            String line = String.format("%d. %s - %d", rank++, ps.getPlayerName(), ps.getScore());
            g.drawString(line, 180, y);
            y += 35;
        }

        if (scores.isEmpty()) {
            g.drawString("No scores yet.", 220, y);
        }
    }
}
