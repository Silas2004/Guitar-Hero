package view;
import leaderboard.LeaderboardService;
import leaderboard.PlayerScore;
import javax.swing.*;
import java.awt.*;

public class GameOverView extends JPanel {
    private final int finalScore;
    
    public GameOverView(int finalScore) {
        this.finalScore = finalScore;
        setPreferredSize(new Dimension(600, 800));
        setBackground(Color.BLACK);
        setFocusable(true);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(40, 0, 0),
            0, 800, Color.BLACK
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, 600, 800);
        
        g2d.setColor(new Color(255, 50, 50));
        g2d.setFont(new Font("Impact", Font.BOLD, 56));
        g2d.drawString("GAME OVER", 140, 100);
        
        g2d.setColor(new Color(255, 215, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        String scoreText = "Your Score: " + finalScore;
        g2d.drawString(scoreText, 170, 180);
        
        g2d.setColor(new Color(0, 255, 255));
        g2d.setFont(new Font("Arial", Font.BOLD, 30));
        g2d.drawString("LEADERBOARD", 190, 260);
        
        g2d.setColor(new Color(50, 50, 50));
        g2d.fillRoundRect(80, 290, 440, 380, 20, 20);
        g2d.setColor(new Color(0, 255, 255));
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(80, 290, 440, 380, 20, 20);
        
        var scores = LeaderboardService.getInstance().getLeaderboard().getScores();
        int y = 340;
        int rank = 1;
        
        for (PlayerScore ps : scores) {
            if (rank <= 3) {
                g2d.setColor(new Color(255, 215, 0));
            } else {
                g2d.setColor(new Color(180, 180, 180));
            }
            
            g2d.setFont(new Font("Arial", Font.BOLD, 22));
            String entry = rank + ". " + ps.getPlayerName();
            g2d.drawString(entry, 110, y);
            
            String score = ps.getScore() + " pts";
            int scoreWidth = g2d.getFontMetrics().stringWidth(score);
            g2d.drawString(score, 480 - scoreWidth, y);
            
            y += 40;
            rank++;
            
            if (rank > 10) break;
        }
        
        g2d.setColor(new Color(0, 255, 255));
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("Press ENTER to continue", 145, 730);
    }
}