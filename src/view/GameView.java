package view;

import model.GameModel;
import model.Lane;
import model.Note;
import observer.GameObserver;

import javax.swing.*;
import java.awt.*;


public class GameView extends JPanel implements GameObserver {
    private final GameModel model;
    
    private final BasicStroke laneStroke = new BasicStroke(2);
    private final BasicStroke noteStroke = new BasicStroke(2);
    private final BasicStroke glowStroke = new BasicStroke(6);


    public GameView(GameModel model) {
        this.model = model;
        this.model.addObserver(this);
        
        setPreferredSize(new Dimension(600, 800));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);
        setOpaque(true);
    }

    @Override
    public void onGameStateChanged() {
        repaint();
    }
    
    //Maybe sound or glowing system. In future Song note system with song import. Coming up in Xamarin Version
    @Override
    public void onNoteHit(int points) {
        repaint();
    }
    
    @Override
    public void onNoteMissed() {
        repaint();
    }
    
    @Override
    public void onGameOver(int finalScore) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        
        drawBackground(g2d);
        drawLanes(g2d);
        drawHitLine(g2d);
        drawNotes(g2d);
        drawScore(g2d);
        drawLives(g2d);
    }
    
    private void drawBackground(Graphics2D g2d) {
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(10, 0, 30),
            0, 800, Color.BLACK
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, 600, 800);
    }

    private void drawLanes(Graphics2D g2d) {
        int laneWidth = 80;
        int panelHeight = getHeight();

        for (Lane lane : model.getLanes()) {
            int x = lane.getX();

            GradientPaint gradient = new GradientPaint(
                x - laneWidth / 2, 0, new Color(40, 40, 60),
                x - laneWidth / 2, panelHeight, new Color(20, 20, 35));
            g2d.setPaint(gradient);
            g2d.fillRect(x - laneWidth / 2, 0, laneWidth, panelHeight);

            g2d.setColor(new Color(80, 80, 120, 150));
            g2d.setStroke(laneStroke);
            g2d.drawRect(x - laneWidth / 2, 0, laneWidth, panelHeight);

            g2d.setColor(new Color(0, 255, 255));
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            String keyStr = String.valueOf(lane.getKey()).toUpperCase();
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(keyStr);
            int textX = x - textWidth / 2;
            int textY = panelHeight - 20;
            g2d.drawString(keyStr, textX, textY);
        }
    }

    private void drawNotes(Graphics2D g2d) {
        int laneWidth = 80;
        int noteHeight = 20;
        int noteArc = 12;

        for (Lane lane : model.getLanes()) {
            for (Note note : lane.getNotes()) {
                int x = lane.getX() - laneWidth / 2 + 10;
                int y = note.getY();
                int height = note.isHoldNote() ? note.getHoldLength() : noteHeight;

                if (!note.isHit()) {
                    GradientPaint noteGradient = new GradientPaint(
                        x, y, new Color(138, 43, 226),
                        x, y + height, new Color(218, 112, 214));
                    g2d.setPaint(noteGradient);
                    g2d.fillRoundRect(x, y, laneWidth - 20, height, noteArc, noteArc);

                    g2d.setColor(new Color(255, 200, 255, 200));
                    g2d.setStroke(noteStroke);
                    g2d.drawRoundRect(x, y, laneWidth - 20, height, noteArc, noteArc);
                } else if (note.isHitEffectActive()) {
                    float alpha = Math.min(1f, note.getHitEffectTimer() / 300f);
                    Color glowColor = new Color(0, 255, 255, (int) (alpha * 200));
                    g2d.setColor(glowColor);
                    g2d.setStroke(glowStroke);
                    g2d.drawRoundRect(x - 3, y - 3, laneWidth - 14, height + 6, noteArc + 5, noteArc + 5);

                    GradientPaint noteGradient = new GradientPaint(
                        x, y, new Color(0, 255, 255),
                        x, y + height, new Color(0, 200, 200));
                    g2d.setPaint(noteGradient);
                    g2d.fillRoundRect(x, y, laneWidth - 20, height, noteArc, noteArc);

                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(noteStroke);
                    g2d.drawRoundRect(x, y, laneWidth - 20, height, noteArc, noteArc);
                }
            }
        }
    }

    private void drawHitLine(Graphics2D g2d) {
        int y = model.getHitLineY();

        GradientPaint gradient = new GradientPaint(
            0, y, new Color(255, 0, 100),
            getWidth(), y, new Color(255, 100, 150));
        g2d.setPaint(gradient);
        g2d.fillRect(0, y - 3, getWidth(), 6);

        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(0, y, getWidth(), y);
    }

    private void drawScore(Graphics2D g2d) {
        g2d.setColor(new Color(0, 255, 255));
        g2d.setFont(new Font("Arial", Font.BOLD, 28));
        g2d.drawString("Score: " + model.getScore().getTotalScore(), 15, 40);
        
        g2d.setColor(new Color(255, 215, 0));
        g2d.drawString("Combo: " + model.getScore().getCombo(), 15, 75);
        
        if (model.getScore().getMaxCombo() > 0) {
            g2d.setColor(new Color(255, 165, 0));
            g2d.setFont(new Font("Arial", Font.PLAIN, 16));
            g2d.drawString("Best: " + model.getScore().getMaxCombo(), 15, 95);
        }
    }
    
    private void drawLives(Graphics2D g2d) {
        int lives = model.getLives();
        int heartSize = 30;
        int padding = 8;
        int startX = getWidth() - 15 - (lives * (heartSize + padding));
        int startY = 15;

        for (int i = 0; i < lives; i++) {
            drawHeart(g2d, startX + i * (heartSize + padding), startY, heartSize);
        }
    }

    private void drawHeart(Graphics2D g2d, int x, int y, int size) {
        g2d.setColor(new Color(255, 20, 60));
        g2d.fillOval(x, y, size / 2, size / 2);
        g2d.fillOval(x + size / 2, y, size / 2, size / 2);
        
        int[] xs = {x, x + size / 4, x + size / 2, x + size * 3 / 4, x + size};
        int[] ys = {y + size / 3, y + size, y + size * 4 / 5, y + size, y + size / 3};
        g2d.fillPolygon(xs, ys, 5);
        
        g2d.setColor(new Color(255, 100, 120));
        g2d.setStroke(noteStroke);
        g2d.drawOval(x, y, size / 2, size / 2);
        g2d.drawOval(x + size / 2, y, size / 2, size / 2);
    }
}
