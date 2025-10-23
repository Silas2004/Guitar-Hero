package services;

import model.GameSettings;

import java.io.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class SettingsService {
    private static SettingsService instance;
    private GameSettings settings;
    private final String filePath = "resources/settings.json";
    
    private SettingsService() {
        ensureResourcesDirectory();
        settings = loadSettings();
        if (settings == null) {
            System.out.println("No settings found - creating defaults");
            settings = new GameSettings();
            saveSettings();
        } else {
            System.out.println("Settings loaded from file at startup!");
        }
    }
    
    public static synchronized SettingsService getInstance() {
        if (instance == null) {
            instance = new SettingsService();
        }
        return instance;
    }
    
    public GameSettings getSettings() {
        return settings;
    }
    
    public void updateSettings(GameSettings newSettings) {
        this.settings = newSettings;
        saveSettings();
    }
    
    public void saveSettings() {
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            
            String json = settingsToJson(settings);
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(json);
            }
            System.out.println("✓ Settings saved successfully to: " + filePath);
        } catch (IOException e) {
            System.err.println("✗ Failed to save settings: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private GameSettings loadSettings() {
        File file = new File(filePath);
        
        if (!file.exists()) {
            System.out.println("Settings file does not exist. Will create default settings.");
            return null;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            
            GameSettings loaded = jsonToSettings(jsonBuilder.toString());
            if (loaded != null) {
                System.out.println("✓ Settings AUTO-LOADED from: " + filePath);
                System.out.println("  → Lives: " + loaded.getStartLives());
                System.out.println("  → Keys: " + loaded.getKeyLane1() + loaded.getKeyLane2() + 
                                   loaded.getKeyLane3() + loaded.getKeyLane4());
                return loaded;
            } else {
                System.err.println("✗ Loaded settings are null.");
                return null;
            }
        } catch (IOException e) {
            System.err.println("✗ Error loading settings: " + e.getMessage());
            e.printStackTrace();
            backupCorruptedFile();
            return null;
        }
    }
    
    
    public GameSettings reloadSettings() {
        System.out.println("Reloading settings from file...");
        settings = loadSettings();
        if (settings == null) {
            System.out.println("Reload failed - using defaults");
            settings = new GameSettings();
            saveSettings();
        }
        return settings;
    }
    
    public boolean exportSettings(String exportPath) {
        try {
            File exportFile = new File(exportPath);
            if (!exportPath.endsWith(".json")) {
                exportPath += ".json";
                exportFile = new File(exportPath);
            }
            
            if (exportFile.getParentFile() != null) {
                Files.createDirectories(exportFile.getParentFile().toPath());
            }
            
            String json = settingsToJson(settings);
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(exportFile))) {
                writer.write(json);
            }
            System.out.println("✓ Settings exported successfully to: " + exportPath);
            return true;
        } catch (IOException e) {
            System.err.println("✗ Failed to export settings: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean importSettings(String importPath) {
        File file = new File(importPath);
        
        if (!file.exists()) {
            System.err.println("✗ Import file does not exist: " + importPath);
            return false;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(importPath))) {
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            
            GameSettings imported = jsonToSettings(jsonBuilder.toString());
            if (imported != null) {
                this.settings = imported;
                saveSettings(); // Speichert auch lokal!
                System.out.println("✓ Settings imported and saved locally from: " + importPath);
                return true;
            } else {
                System.err.println("✗ Failed to parse settings from file.");
                return false;
            }
        } catch (IOException e) {
            System.err.println("✗ Failed to import settings: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public void resetToDefaults() {
        settings = new GameSettings();
        saveSettings();
        System.out.println("✓ Settings reset to defaults and saved.");
    }
    
    private void ensureResourcesDirectory() {
        try {
            Path resourcesPath = Paths.get("resources");
            if (!Files.exists(resourcesPath)) {
                Files.createDirectories(resourcesPath);
                System.out.println("Created resources directory.");
            }
        } catch (IOException e) {
            System.err.println("Could not create resources directory: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void backupCorruptedFile() {
        try {
            Path source = Paths.get(filePath);
            if (Files.exists(source)) {
                String timestamp = String.valueOf(System.currentTimeMillis());
                Path backup = Paths.get(filePath + ".backup." + timestamp);
                Files.move(source, backup, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Corrupted file backed up to: " + backup);
            }
        } catch (IOException e) {
            System.err.println("Failed to backup corrupted file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String settingsToJson(GameSettings settings) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"keyLane1\": \"").append(settings.getKeyLane1()).append("\",\n");
        json.append("  \"keyLane2\": \"").append(settings.getKeyLane2()).append("\",\n");
        json.append("  \"keyLane3\": \"").append(settings.getKeyLane3()).append("\",\n");
        json.append("  \"keyLane4\": \"").append(settings.getKeyLane4()).append("\",\n");
        json.append("  \"baseSpeed\": ").append(settings.getBaseSpeed()).append(",\n");
        json.append("  \"speedIncrement\": ").append(settings.getSpeedIncrement()).append(",\n");
        json.append("  \"maxSpeed\": ").append(settings.getMaxSpeed()).append(",\n");
        json.append("  \"startLives\": ").append(settings.getStartLives()).append("\n");
        json.append("}");
        return json.toString();
    }
    
    private GameSettings jsonToSettings(String json) {
        try {
            json = json.replaceAll("\\s+", "");
            
            char keyLane1 = extractChar(json, "keyLane1");
            char keyLane2 = extractChar(json, "keyLane2");
            char keyLane3 = extractChar(json, "keyLane3");
            char keyLane4 = extractChar(json, "keyLane4");
            
            double baseSpeed = extractDouble(json, "baseSpeed");
            double speedIncrement = extractDouble(json, "speedIncrement");
            double maxSpeed = extractDouble(json, "maxSpeed");
            int startLives = extractInt(json, "startLives");
            
            return new GameSettings(keyLane1, keyLane2, keyLane3, keyLane4, 
                                   baseSpeed, speedIncrement, maxSpeed, startLives);
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    private char extractChar(String json, String key) {
        String searchKey = "\"" + key + "\":\"";
        int startIndex = json.indexOf(searchKey);
        if (startIndex != -1) {
            startIndex += searchKey.length();
            return json.charAt(startIndex);
        }
        return 'A';
    }
    
    private double extractDouble(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int startIndex = json.indexOf(searchKey);
        if (startIndex != -1) {
            startIndex += searchKey.length();
            int endIndex = json.indexOf(",", startIndex);
            if (endIndex == -1) {
                endIndex = json.indexOf("}", startIndex);
            }
            String value = json.substring(startIndex, endIndex);
            return Double.parseDouble(value);
        }
        return 1.0;
    }
    
    private int extractInt(String json, String key) {
        String searchKey = "\"" + key + "\":";
        int startIndex = json.indexOf(searchKey);
        if (startIndex != -1) {
            startIndex += searchKey.length();
            int endIndex = json.indexOf(",", startIndex);
            if (endIndex == -1) {
                endIndex = json.indexOf("}", startIndex);
            }
            String value = json.substring(startIndex, endIndex);
            return Integer.parseInt(value);
        }
        return 0;
    }
}