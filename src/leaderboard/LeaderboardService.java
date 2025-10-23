package leaderboard;

import java.io.*;
import java.nio.file.*;

public class LeaderboardService {
    private static LeaderboardService instance;
    private Leaderboard leaderboard;
    private final String filePath = "resources/leaderboard.dat";
    private static final int MAX_ENTRIES = 10;

    private LeaderboardService() {
        ensureResourcesDirectory();
        leaderboard = loadLeaderboard();
        if (leaderboard == null) {
            leaderboard = new Leaderboard();
            saveLeaderboard();
        }
    }

    public static synchronized LeaderboardService getInstance() {
        if (instance == null) {
            instance = new LeaderboardService();
        }
        return instance;
    }

    public Leaderboard getLeaderboard() {
        return leaderboard;
    }

    public void addScore(PlayerScore score) {
        if (score == null || score.getPlayerName() == null) {
            return;
        }
        
        leaderboard.addScore(score);
        trimLeaderboard();
        saveLeaderboard();
    }

    private void trimLeaderboard() {
        var scores = leaderboard.getScores();
        while (scores.size() > MAX_ENTRIES) {
            scores.remove(scores.size() - 1);
        }
    }

    private void ensureResourcesDirectory() {
        try {
            Path resourcesPath = Paths.get("resources");
            if (!Files.exists(resourcesPath)) {
                Files.createDirectories(resourcesPath);
            }
        } catch (IOException e) {
            System.err.println("Could not create resources directory: " + e.getMessage());
        }
    }

    private void saveLeaderboard() {
        try {
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());
            
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new BufferedOutputStream(new FileOutputStream(filePath)))) {
                oos.writeObject(leaderboard);
            }
        } catch (IOException e) {
            System.err.println("Failed to save leaderboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Leaderboard loadLeaderboard() {
        File file = new File(filePath);
        
        if (!file.exists()) {
            System.out.println("Leaderboard file does not exist. Creating new leaderboard.");
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(filePath)))) {
            Object obj = ois.readObject();
            if (obj instanceof Leaderboard) {
                System.out.println("Leaderboard loaded successfully.");
                return (Leaderboard) obj;
            } else {
                System.err.println("Invalid leaderboard file format.");
                backupCorruptedFile();
                return null;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading leaderboard: " + e.getMessage());
            backupCorruptedFile();
            return null;
        }
    }

    private void backupCorruptedFile() {
        try {
            Path source = Paths.get(filePath);
            if (Files.exists(source)) {
                Path backup = Paths.get(filePath + ".backup");
                Files.move(source, backup, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Corrupted file backed up to: " + backup);
            }
        } catch (IOException e) {
            System.err.println("Failed to backup corrupted file: " + e.getMessage());
        }
    }

    public void resetLeaderboard() {
        leaderboard = new Leaderboard();
        saveLeaderboard();
    }
}