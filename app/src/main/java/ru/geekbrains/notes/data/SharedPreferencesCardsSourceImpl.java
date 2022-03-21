package ru.geekbrains.notes.data;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.geekbrains.notes.R;

public class SharedPreferencesCardsSourceImpl implements NoteSource{
    private List<Note> noteSource;
    private SharedPreferences sharedPreferences;

    static final String KEY_CELL_1 = "cell_2";
    public static final String KEY_SP_2 = "key_sp_2";

    public SharedPreferencesCardsSourceImpl(SharedPreferences sharedPreferences) {
        noteSource = new ArrayList<>();
        this.sharedPreferences = sharedPreferences;
    }

    public SharedPreferencesCardsSourceImpl init() {
        String savedNotes = sharedPreferences.getString(KEY_CELL_1,null);
        if (savedNotes != null) {
            Type type = new TypeToken<ArrayList<Note>>() {}.getType();
            noteSource = new GsonBuilder().create().fromJson(savedNotes, type);
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
        saveToJson();
    }

    @Override
    public void updateNote(int position, Note note) {
        noteSource.set(position, note);
        saveToJson();
    }

    @Override
    public void addNote(Note note) {
        noteSource.add(note);
        saveToJson();
    }

    @Override
    public void clearNote() {
        noteSource.clear();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CELL_1, null).apply();
    }

    public void saveToJson(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_CELL_1, new GsonBuilder().create().toJson(noteSource)).apply();
    }
}
