package ru.geekbrains.notes.data;

public interface NoteSource {
   Note getNote (int position);
   int size();
}
