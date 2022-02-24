package ru.geekbrains.notes.ui;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

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
    Note note;
    TextView tvTitle;
    TextView tvDate;
    TextView tvNoteText;
    LinearLayout linearLayoutNoteText;
    EditText editText;
    MaterialButton button;

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
            note = (Note) agr.getParcelable(ARG_INDEX);
        }
        initView(view);
        initPopupMenu(view);
    }

    private void initView(View view){
        tvTitle = view.findViewById(R.id.nameNote);
        tvDate = view.findViewById(R.id.date);
        tvNoteText = view.findViewById(R.id.nameNoteText);
        linearLayoutNoteText = view.findViewById(R.id.linearNoteText);
        editText = view.findViewById(R.id.edit);
        button = view.findViewById(R.id.btn);

        tvTitle.setText(note.getNoteName());
        tvDate.setText(format.format(note.getDate()));
        tvNoteText.setText(note.getNoteText());
        linearLayoutNoteText.setBackgroundColor(note.getColor());
    }

    private void initPopupMenu(View view) {
        TextView textView = view.findViewById(R.id.nameNoteText);
        textView.setOnClickListener(v -> {
                    Activity activity = requireActivity();
                    PopupMenu popupMenu = new PopupMenu(activity, v);
                    activity.getMenuInflater().inflate(R.menu.popup, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.popup_clean:
                                    Snackbar.make(linearLayoutNoteText, "Очистить заметку?", Snackbar.LENGTH_LONG)
                                    .setAction("Clear", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            tvNoteText.setText("");
                                        }
                                    }).show();
                                    return true;
                                case R.id.popup_edit:
                                    editText.setText(tvNoteText.getText());
                                    editText.setVisibility(View.VISIBLE);
                                    button.setVisibility(View.VISIBLE);
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            tvNoteText.setText(editText.getText());
                                            editText.setText("");
                                            editText.setVisibility(View.INVISIBLE);
                                            button.setVisibility(View.INVISIBLE);
                                        }
                                    });
                                   return true;
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                }
        );
    }
}