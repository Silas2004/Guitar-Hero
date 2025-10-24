package view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuView extends JPanel {
    private JTextField nameField;
    private JLabel nameLabel;
    private Runnable onStartGame;
    private Runnable onOpenSettings;

    public MenuView() {
        setPreferredSize(new Dimension(600, 800));
        setBackground(Color.BLACK);
        setLayout(null);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        nameLabel = new JLabel("Enter your name:");
        nameLabel.setBounds(200, 350, 200, 30);
        nameLabel.setForeground(new Color(0, 255, 255));
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(nameLabel);

        nameField = new JTextField("Player");
        nameField.setBounds(175, 390, 250, 40);
        nameField.setFont(new Font("Arial", Font.PLAIN, 20));
        nameField.setBackground(new Color(30, 30, 30));
        nameField.setForeground(Color.WHITE);
        nameField.setCaretColor(new Color(0, 255, 255));
        nameField.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 2));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        add(nameField);

        nameField.addActionListener(e -> {
            if (onStartGame != null) {
                onStartGame.run();
            }
        });

        nameField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                resetNameFieldBorder();
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() != KeyEvent.VK_ESCAPE) {
                    resetNameFieldBorder();
                }
            }
        });
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                focusNameField();
                
                Timer focusTimer = new Timer(100, evt -> {
                    focusNameField();
                    ((Timer) evt.getSource()).stop();
                });
                focusTimer.setRepeats(false);
                focusTimer.start();
            }
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                focusNameField();
            }
        });
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE && onOpenSettings != null) {
                    onOpenSettings.run();
                }
            }
        });
    }

    public void setOnStartGame(Runnable callback) {
        this.onStartGame = callback;
    }
    
    public void setOnOpenSettings(Runnable callback) {
        this.onOpenSettings = callback;
    }

    public String getPlayerName() {
        String name = nameField.getText().trim();
        return name.isEmpty() ? "Player" : name;
    }

    public boolean isPlayerNameValid() {
        String name = nameField.getText().trim();
        return !name.isEmpty() && name.length() >= 2 && name.length() <= 20;
    }

    public void showInvalidNameError() {
        nameField.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        Timer shakeTimer = new Timer(50, null);
        final int[] shakeCount = {0};
        shakeTimer.addActionListener(e -> {
            if (shakeCount[0]++ < 6) {
                int offset = (shakeCount[0] % 2 == 0) ? 5 : -5;
                nameField.setLocation(175 + offset, 390);
            } else {
                nameField.setLocation(175, 390);
                ((Timer) e.getSource()).stop();
            }
        });
        shakeTimer.start();
    }

    public void resetNameFieldBorder() {
        nameField.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 2));
    }

    public void focusNameField() {
        SwingUtilities.invokeLater(() -> {
            nameField.setFocusable(true);
            nameField.requestFocusInWindow();
            nameField.selectAll();
            nameField.getCaret().setVisible(true);
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gradient = new GradientPaint(
                0, 0, new Color(15, 0, 50),
                0, 800, Color.BLACK
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, 600, 800);

        g2d.setColor(new Color(80, 80, 80, 100));
        for (int i = 0; i < 5; i++) {
            g2d.fillRect(100 + i * 100, 0, 3, 800);
        }

        g2d.setColor(new Color(138, 43, 226, 100));
        g2d.setFont(new Font("Impact", Font.BOLD, 52));
        for (int i = 1; i <= 3; i++) {
            g2d.drawString("GUITAR HERO", 148 + i, 148 + i);
            g2d.drawString("GUITAR HERO", 152 - i, 152 - i);
        }

        g2d.setColor(new Color(218, 112, 214));
        g2d.setFont(new Font("Impact", Font.BOLD, 50));
        g2d.drawString("GUITAR HERO", 150, 150);

        g2d.setColor(Color.WHITE);
        g2d.drawString("GUITAR HERO", 150, 148);

        g2d.setColor(new Color(0, 255, 255));
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("Press ENTER to start", 175, 500);

        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        g2d.drawString("Press ESC for Settings", 200, 530);

        g2d.setColor(new Color(255, 255, 0, 180));
        int[] starX = {100, 500, 80, 520, 300};
        int[] starY = {250, 280, 650, 680, 600};
        for (int i = 0; i < starX.length; i++) {
            g2d.fillOval(starX[i], starY[i], 4, 4);
        }

        g2d.setColor(new Color(100, 100, 100));
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString("© 2025 - Silas", 240, 750);
    }
}