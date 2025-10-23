package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Lane {
    private int x;            
    private char key;          
    private List<Note> notes;  

    public Lane(int x, char key) {
        this.x = x;
        this.key = key;
        this.notes = new ArrayList<>();
    }

    public int getX() {
        return x;
    }

    public char getKey() {
        return key;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void addNote(Note note) {
        notes.add(note);
    }

    public int removeHitOrMissedNotes(int bottomY) {
        int missedNotes = 0;
        Iterator<Note> iter = notes.iterator();

        while (iter.hasNext()) {
            Note n = iter.next();
            if (n.isHit()) {
                iter.remove();
            } else if (n.getY() > bottomY) {
                iter.remove();
                missedNotes++;
            }
        }
        return missedNotes;
    }


    public void updateNotes(double deltaY) {
        for (Note n : notes) {
            n.moveDown(deltaY);
        }
    }
}
