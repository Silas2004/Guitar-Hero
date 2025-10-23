package state;

public interface GameState {
    void enter();
    void exit();
    void update(long deltaMillis);
}
