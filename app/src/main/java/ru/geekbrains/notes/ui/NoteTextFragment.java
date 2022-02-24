package ru.geekbrains.notes.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import ru.geekbrains.notes.R;
import ru.geekbrains.notes.data.CardsSourceImpl;
import ru.geekbrains.notes.data.Note;
import ru.geekbrains.notes.data.NoteSource;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteTextFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteTextFragment extends Fragment {
    static final String ARG_INDEX = "index";
    CardsSourceImpl noteSource;
    static final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    public NoteTextFragment() {
        // Required empty public constructor
    }

    public static NoteTextFragment newInstance(Note note) {
        NoteTextFragment fragment = new NoteTextFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_INDEX, note);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_text, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle agr = getArguments();
        if (agr != null) {
            Note note = (Note) agr.getParcelable(ARG_INDEX);
            TextView tvTitle = view.findViewById(R.id.nameNote);
            TextView tvDate = view.findViewById(R.id.date);
            TextView tvNoteText = view.findViewById(R.id.nameNoteText);
            LinearLayout linearLayoutNoteText = view.findViewById(R.id.linearNoteText);
            tvTitle.setText(note.getNoteName());
            tvDate.setText(format.format(note.getDate()));
            tvNoteText.setText(note.getNoteText());
            linearLayoutNoteText.setBackgroundColor(note.getColor());
        }
    }

}