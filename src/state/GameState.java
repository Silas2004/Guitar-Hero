package state;

//State Pattern for each State

public interface GameState {
    void enter();
    void exit();
    void update(long deltaMillis);
}
