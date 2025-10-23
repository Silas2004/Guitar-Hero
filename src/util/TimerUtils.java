package util;

public class TimerUtils {
    private long lastTime;

    public TimerUtils() {
        lastTime = System.currentTimeMillis();
    }

    public long elapsedMillis() {
        return System.currentTimeMillis() - lastTime;
    }

    public void reset() {
        lastTime = System.currentTimeMillis();
    }
}
