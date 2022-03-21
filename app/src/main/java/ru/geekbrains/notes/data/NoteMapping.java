package ru.geekbrains.notes.data;


import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ru.geekbrains.notes.R;

public class NoteMapping {

    public static class Fields {
        public final static String NOTE_TEXT = "noteText";
        public final static String DATE = "date";
        public final static String NOTE_NAME = "noteName";
        public final static String COLOR = "color";
    }

    public static Note toNote(String id, Map<String, Object> doc){
        long indexColor = (long) doc.get(Fields.COLOR);
        Timestamp timestamp = (Timestamp) doc.get(Fields.DATE);
        Note answer = new Note(
                (String) doc.get(Fields.NOTE_NAME),
                (String) doc.get(Fields.NOTE_TEXT),
                ColorIndexConverter.getColorByIndex((int) indexColor),
                timestamp.toDate());
        answer.setId(id);
        return answer;
    }

    public static Map<String, Object> toDoc(Note note){
        Map<String, Object> answer = new HashMap<>();
        answer.put(Fields.NOTE_NAME, note.getNoteName());
        answer.put(Fields.NOTE_TEXT, note.getNoteText());
        answer.put(Fields.COLOR, ColorIndexConverter.getIndexByColor(note.getColor()));
        answer.put(Fields.DATE, note.getDate());
        return answer;
    }
}
