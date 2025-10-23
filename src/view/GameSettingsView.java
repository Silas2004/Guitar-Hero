package view;

import model.GameSettings;
import services.SettingsService;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class GameSettingsView extends JPanel {
    private GameSettings settings;
    private SettingsService service;
    
    private JTextField lane1Field;
    private JTextField lane2Field;
    private JTextField lane3Field;
    private JTextField lane4Field;
    
    private JSlider baseSpeedSlider;
    private JSlider incrementSlider;
    private JSlider maxSpeedSlider;
    private JSlider livesSlider;
    
    private JLabel baseSpeedLabel;
    private JLabel incrementLabel;
    private JLabel maxSpeedLabel;
    private JLabel livesLabel;
    
    private JButton saveButton;
    private JButton resetButton;
    private JButton importButton;
    private JButton exportButton;
    private JButton backButton;
    
    private Runnable onBack;
    private Runnable onSettingsSaved;
    
    public GameSettingsView() {
        service = SettingsService.getInstance();
        settings = service.getSettings();
        
        setPreferredSize(new Dimension(600, 800));
        setBackground(Color.BLACK);
        setLayout(null);
        setFocusable(true);
        
        initComponents();
        loadSettingsToUI();
    }
    
    private void initComponents() {
        JLabel titleLabel = new JLabel("SETTINGS");
        titleLabel.setBounds(220, 30, 200, 40);
        titleLabel.setForeground(new Color(0, 255, 255));
        titleLabel.setFont(new Font("Impact", Font.BOLD, 36));
        add(titleLabel);
        
        JLabel keysLabel = new JLabel("Key Bindings:");
        keysLabel.setBounds(80, 90, 150, 25);
        keysLabel.setForeground(new Color(0, 255, 255));
        keysLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(keysLabel);
        
        String[] laneLabels = {"Lane 1:", "Lane 2:", "Lane 3:", "Lane 4:"};
        lane1Field = createKeyField(80, 125);
        lane2Field = createKeyField(80, 165);
        lane3Field = createKeyField(80, 205);
        lane4Field = createKeyField(80, 245);
        
        JTextField[] fields = {lane1Field, lane2Field, lane3Field, lane4Field};
        
        for (int i = 0; i < laneLabels.length; i++) {
            JLabel label = new JLabel(laneLabels[i]);
            label.setBounds(80, 125 + i * 40, 80, 25);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.PLAIN, 16));
            add(label);
            add(fields[i]);
        }
        
        JLabel speedLabel = new JLabel("Speed Settings:");
        speedLabel.setBounds(320, 90, 200, 25);
        speedLabel.setForeground(new Color(0, 255, 255));
        speedLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(speedLabel);
        
        baseSpeedLabel = new JLabel("Base Speed: 1.5");
        baseSpeedLabel.setBounds(320, 125, 200, 20);
        baseSpeedLabel.setForeground(Color.WHITE);
        baseSpeedLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(baseSpeedLabel);
        
        baseSpeedSlider = createSlider(320, 145, 10, 50, (int)(settings.getBaseSpeed() * 10));
        baseSpeedSlider.addChangeListener(e -> {
            double value = baseSpeedSlider.getValue() / 10.0;
            baseSpeedLabel.setText("Base Speed: " + value);
        });
        add(baseSpeedSlider);
        
        incrementLabel = new JLabel("Speed Increment: 0.1");
        incrementLabel.setBounds(320, 180, 200, 20);
        incrementLabel.setForeground(Color.WHITE);
        incrementLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(incrementLabel);
        
        incrementSlider = createSlider(320, 200, 1, 20, (int)(settings.getSpeedIncrement() * 10));
        incrementSlider.addChangeListener(e -> {
            double value = incrementSlider.getValue() / 10.0;
            incrementLabel.setText("Speed Increment: " + value);
        });
        add(incrementSlider);
        
        maxSpeedLabel = new JLabel("Max Speed: 5.0");
        maxSpeedLabel.setBounds(320, 235, 200, 20);
        maxSpeedLabel.setForeground(Color.WHITE);
        maxSpeedLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(maxSpeedLabel);
        
        maxSpeedSlider = createSlider(320, 255, 30, 100, (int)(settings.getMaxSpeed() * 10));
        maxSpeedSlider.addChangeListener(e -> {
            double value = maxSpeedSlider.getValue() / 10.0;
            maxSpeedLabel.setText("Max Speed: " + value);
        });
        add(maxSpeedSlider);
        
        livesLabel = new JLabel("Starting Lives: 3");
        livesLabel.setBounds(80, 290, 200, 20);
        livesLabel.setForeground(Color.WHITE);
        livesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(livesLabel);
        
        livesSlider = createSlider(80, 310, 1, 10, settings.getStartLives());
        livesSlider.addChangeListener(e -> {
            int value = livesSlider.getValue();
            livesLabel.setText("Starting Lives: " + value);
        });
        add(livesSlider);
        
        saveButton = createButton("Save Settings", 150, 400, 300, 40, new Color(0, 200, 0));
        saveButton.addActionListener(e -> saveSettings());
        add(saveButton);
        
        resetButton = createButton("Reset to Defaults", 150, 450, 300, 40, new Color(200, 100, 0));
        resetButton.addActionListener(e -> resetSettings());
        add(resetButton);
        
        importButton = createButton("Import Settings", 150, 520, 140, 40, new Color(100, 100, 200));
        importButton.addActionListener(e -> importSettings());
        add(importButton);
        
        exportButton = createButton("Export Settings", 310, 520, 140, 40, new Color(100, 100, 200));
        exportButton.addActionListener(e -> exportSettings());
        add(exportButton);
        
        backButton = createButton("Back to Menu", 150, 600, 300, 50, new Color(200, 0, 0));
        backButton.addActionListener(e -> {
            if (onBack != null) onBack.run();
        });
        add(backButton);
    }
    
    private JTextField createKeyField(int x, int y) {
        JTextField field = new JTextField(1);
        field.setBounds(x + 80, y, 40, 25);
        field.setFont(new Font("Arial", Font.BOLD, 16));
        field.setBackground(new Color(30, 30, 30));
        field.setForeground(Color.WHITE);
        field.setCaretColor(new Color(0, 255, 255));
        field.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 2));
        field.setHorizontalAlignment(JTextField.CENTER);
        
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (field.getText().length() >= 1) {
                    e.consume();
                }
                char c = Character.toUpperCase(e.getKeyChar());
                if (!Character.isLetter(c)) {
                    e.consume();
                }
            }
        });
        
        return field;
    }
    
    private JSlider createSlider(int x, int y, int min, int max, int value) {
        JSlider slider = new JSlider(min, max, value);
        slider.setBounds(x, y, 250, 30);
        slider.setBackground(Color.BLACK);
        slider.setForeground(new Color(0, 255, 255));
        return slider;
    }
    
    private JButton createButton(String text, int x, int y, int width, int height, Color color) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(color.brighter(), 2));
        return button;
    }
    
    private void loadSettingsToUI() {
        settings = service.getSettings();
        
        lane1Field.setText(String.valueOf(settings.getKeyLane1()));
        lane2Field.setText(String.valueOf(settings.getKeyLane2()));
        lane3Field.setText(String.valueOf(settings.getKeyLane3()));
        lane4Field.setText(String.valueOf(settings.getKeyLane4()));
        
        baseSpeedSlider.setValue((int)(settings.getBaseSpeed() * 10));
        incrementSlider.setValue((int)(settings.getSpeedIncrement() * 10));
        maxSpeedSlider.setValue((int)(settings.getMaxSpeed() * 10));
        livesSlider.setValue(settings.getStartLives());
        
        baseSpeedLabel.setText("Base Speed: " + settings.getBaseSpeed());
        incrementLabel.setText("Speed Increment: " + settings.getSpeedIncrement());
        maxSpeedLabel.setText("Max Speed: " + settings.getMaxSpeed());
        livesLabel.setText("Starting Lives: " + settings.getStartLives());
    }
    
    private void saveSettings() {
        try {
            String key1 = lane1Field.getText().toUpperCase();
            String key2 = lane2Field.getText().toUpperCase();
            String key3 = lane3Field.getText().toUpperCase();
            String key4 = lane4Field.getText().toUpperCase();
            
            if (key1.isEmpty() || key2.isEmpty() || key3.isEmpty() || key4.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All key bindings must be set!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            settings.setKeyLane1(key1.charAt(0));
            settings.setKeyLane2(key2.charAt(0));
            settings.setKeyLane3(key3.charAt(0));
            settings.setKeyLane4(key4.charAt(0));
            
            settings.setBaseSpeed(baseSpeedSlider.getValue() / 10.0);
            settings.setSpeedIncrement(incrementSlider.getValue() / 10.0);
            settings.setMaxSpeed(maxSpeedSlider.getValue() / 10.0);
            settings.setStartLives(livesSlider.getValue());
            
            service.saveSettings();
            
            if (onSettingsSaved != null) {
                onSettingsSaved.run();
            }
            
            JOptionPane.showMessageDialog(this, "Settings saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to save settings!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setOnSettingsSaved(Runnable callback) {
        this.onSettingsSaved = callback;
    }
    
    private void resetSettings() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Reset all settings to defaults?", 
            "Confirm Reset", 
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            service.resetToDefaults();
            loadSettingsToUI();
            JOptionPane.showMessageDialog(this, "Settings reset to defaults!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void importSettings() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if (service.importSettings(path)) {
                loadSettingsToUI();
                JOptionPane.showMessageDialog(this, "Settings imported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to import settings!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void exportSettings() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", "json"));
        fileChooser.setSelectedFile(new File("guitar_hero_settings.json"));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if (!path.endsWith(".json")) {
                path += ".json";
            }
            if (service.exportSettings(path)) {
                JOptionPane.showMessageDialog(this, "Settings exported successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to export settings!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void setOnBack(Runnable callback) {
        this.onBack = callback;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(15, 0, 50),
            0, 800, Color.BLACK
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, 600, 800);
    }
}