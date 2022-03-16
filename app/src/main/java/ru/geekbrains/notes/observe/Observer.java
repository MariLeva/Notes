package ru.geekbrains.notes.observe;

import ru.geekbrains.notes.data.Note;

public interface Observer {
    void updateNote(Note note);
}
