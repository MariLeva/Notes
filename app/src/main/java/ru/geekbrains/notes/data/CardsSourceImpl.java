package ru.geekbrains.notes.data;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import ru.geekbrains.notes.R;

public class CardsSourceImpl implements NoteSource{
    private List<Note> noteSource;
    private Resources resources;

    public CardsSourceImpl(Resources resources) {
        noteSource = new ArrayList<>();
        this.resources = resources;
    }

    public CardsSourceImpl init() {
        String[] notes = resources.getStringArray(R.array.notes);
        String[] note_description = resources.getStringArray(R.array.note_description);
        int[] color = resources.getIntArray(R.array.colors);
        for (int i = 0; i < notes.length; i++){
            noteSource.add(new Note(notes[i], note_description[i], color[i]));
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
}
