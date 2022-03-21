package ru.geekbrains.notes.ui.editor;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ru.geekbrains.notes.R;
import ru.geekbrains.notes.data.Note;
import ru.geekbrains.notes.ui.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteTextFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteTextFragment extends Fragment {
    static final String ARG_INDEX = "index";
    Note note;
    Calendar calendar;
    TextView tvTitle;
    TextView tvDate;
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
        linearLayoutNoteText = view.findViewById(R.id.linearNoteText);
        editText = view.findViewById(R.id.edit);
        button = view.findViewById(R.id.btn);
        calendar = Calendar.getInstance();

        tvTitle.setText(note.getNoteName());
        editText.setText(note.getNoteText());
        tvDate.setText(format.format(note.getDate()));
        linearLayoutNoteText.setBackgroundColor(note.getColor());
        calendar.setTime(note.getDate());
        ((DatePicker) view.findViewById(R.id.input_date)).init(calendar.get(Calendar.YEAR)-1,
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((DatePicker) view.findViewById(R.id.input_date)).setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                    calendar.set(Calendar.YEAR,i);
                    calendar.set(Calendar.MONTH,i1);
                    calendar.set(Calendar.DAY_OF_MONTH,i2);
                }
            });
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note.setNoteText(editText.getText().toString());

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
                    DatePicker datePicker = ((DatePicker) view.findViewById(R.id.input_date));
                    calendar.set(Calendar.YEAR, datePicker.getYear());
                    calendar.set(Calendar.MONTH, datePicker.getMonth());
                    calendar.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());
                }
                note.setDate(calendar.getTime());

                ((MainActivity) requireActivity()).getPublisher().notifySingle(note);
                ((MainActivity) requireActivity()).getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void initPopupMenu(View view) {
        editText.setOnClickListener(v -> {
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
                                            editText.setText("");
                                        }
                                    }).show();
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