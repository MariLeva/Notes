package ru.geekbrains.notes.data;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.geekbrains.notes.R;

public class ArraysCardsSourceImpl implements NoteSource{
    private List<Note> noteSource;
    private Resources resources;

    public ArraysCardsSourceImpl(Resources resources) {
        noteSource = new ArrayList<>();
        this.resources = resources;
    }

    public ArraysCardsSourceImpl init() {
        String[] notes = resources.getStringArray(R.array.notes);
        String[] note_description = resources.getStringArray(R.array.note_description);
        int[] color = resources.getIntArray(R.array.colors);
        for (int i = 0; i < notes.length; i++){
            noteSource.add(new Note(notes[i], note_description[i], color[i], Calendar.getInstance().getTime()));
        }
        return this;
    }

    @Override
    public Note getNote(int position) {
        return noteSource.get(position);
    }

    @Override
    public int size() {
        return noteSource.size();
    }

    @Override
    public void deleteNote(int position) {
        noteSource.remove(position);
    }

    @Override
    public void updateNote(int position, Note note) {
        noteSource.set(position, note);
    }

    @Override
    public void addNote(Note note) {
        noteSource.add(note);
    }

    @Override
    public void clearNote() {
        noteSource.clear();
    }
}
