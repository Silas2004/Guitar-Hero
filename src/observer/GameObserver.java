package observer;

/*
 * Best Practice Obersable Pattern, reused from Old Monopoly Projekt
 */
public interface GameObserver {

    void onGameStateChanged();
    

    void onNoteHit(int points);
    

    void onNoteMissed();
    

    void onGameOver(int finalScore);
}
