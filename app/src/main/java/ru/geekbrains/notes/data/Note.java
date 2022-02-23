package ru.geekbrains.notes.data;

import java.util.Date;

public class Note {
    private final String noteText;
    private Date date;
    private final String noteName;
    private final int color;

    public Note(String note, String noteText, int color) {
        this.noteName = note;
        this.noteText = noteText;
        this.color = color;
        this.date = new Date();
    }

    public Date getDate() {
        return date;
    }

    public String getNoteName() {
        return noteName;
    }

    public int getColor() {
        return color;
    }

    public String getNoteText() {
        return noteText;
    }
}
