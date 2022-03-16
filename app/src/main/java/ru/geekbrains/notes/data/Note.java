package ru.geekbrains.notes.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Note implements Parcelable {
    private String noteText;
    private Date date;
    private String noteName;
    private int color;

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Note(String note, String noteText, int color) {
        this.noteName = note;
        this.noteText = noteText;
        this.color = color;
        this.date = new Date();
    }

    protected Note(Parcel in) {
        noteText = in.readString();
        noteName = in.readString();
        color = in.readInt();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(noteText);
        parcel.writeString(noteName);
        parcel.writeInt(color);
    }
}
