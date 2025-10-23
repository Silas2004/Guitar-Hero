package model;

public class Note {
    private int x, y;
    private char key;
    private boolean hit;
    private int hitEffectTimer;
    private int holdLength;

    public Note(int x, int y, char key) {
        this.x = x;
        this.y = y;
        this.key = key;
        this.hit = false;
        this.hitEffectTimer = 0;
        this.holdLength = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getKey() {
        return key;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
        if (hit) {
            this.hitEffectTimer = 300;
        }
    }

    public int getHitEffectTimer() {
        return hitEffectTimer;
    }

    public boolean isHitEffectActive() {
        return hitEffectTimer > 0;
    }

    public int getHoldLength() {
        return holdLength;
    }

    public void setHoldLength(int holdLength) {
        this.holdLength = holdLength;
    }

    public boolean isHoldNote() {
        return holdLength > 0;
    }

    public void moveDown(double deltaY) {
        this.y += deltaY;
    }

    public void update(int deltaMillis) {
        if (hitEffectTimer > 0) {
            hitEffectTimer -= deltaMillis;
            if (hitEffectTimer < 0) hitEffectTimer = 0;
        }
    }
}
