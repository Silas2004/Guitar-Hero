package observer;

import java.util.ArrayList;
import java.util.List;
/*
 * Best Practice Obersable Pattern, reused from Old Monopoly Projekt
 */
public abstract class GameObservable {
    private final List<GameObserver> observers = new ArrayList<>();

    public void addObserver(GameObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }
    

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }
    
 
    protected void notifyStateChanged() {
        for (GameObserver observer : observers) {
            observer.onGameStateChanged();
        }
    }
    

    protected void notifyNoteHit(int points) {
        for (GameObserver observer : observers) {
            observer.onNoteHit(points);
        }
    }

    protected void notifyNoteMissed() {
        for (GameObserver observer : observers) {
            observer.onNoteMissed();
        }
    }
    
    /**
     * Notify all observers that the game is over.
     * @param finalScore Final score achieved
     */
    protected void notifyGameOver(int finalScore) {
        for (GameObserver observer : observers) {
            observer.onGameOver(finalScore);
        }
    }
}
