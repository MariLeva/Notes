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
        noteSource = new ArrayList<>(7);
        this.resources = resources;
    }

    public CardsSourceImpl init() {
        String[] notes = resources.getStringArray(R.array.notes);
        String[] note_description = resources.getStringArray(R.array.note_description);
        int[] color = getColors();
        for (int i = 0; i < notes.length; i++){
            noteSource.add(new Note(notes[i], note_description[i], color[i]));
        }
        return this;
    }

    private int[] getColors(){
        TypedArray colors = resources.obtainTypedArray(R.array.colors);
        int[] answer = new int[colors.length()];
        for(int i = 0; i < colors.length(); i++){
            answer[i] = colors.getResourceId(i, 0);
        }
        return answer;
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
