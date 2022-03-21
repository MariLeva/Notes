package ru.geekbrains.notes.ui.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.Calendar;

import ru.geekbrains.notes.data.FireStoreCardsSourceImpl;
import ru.geekbrains.notes.data.FireStoreResponse;
import ru.geekbrains.notes.data.SharedPreferencesCardsSourceImpl;
import ru.geekbrains.notes.ui.MainActivity;
import ru.geekbrains.notes.R;
import ru.geekbrains.notes.data.ArraysCardsSourceImpl;
import ru.geekbrains.notes.data.Note;
import ru.geekbrains.notes.data.NoteSource;
import ru.geekbrains.notes.observe.Observer;
import ru.geekbrains.notes.ui.editor.AboutFragment;
import ru.geekbrains.notes.ui.editor.DialogFragmentExit;
import ru.geekbrains.notes.ui.editor.NoteTextFragment;


public class SocialNetworkFragment extends Fragment implements OnItemClickListener, FireStoreResponse {

    private SocialNetworkAdapter socialNetworkAdapter;
    private NoteSource noteSource;
    private RecyclerView recyclerView;

    static final int SOURCE_ARRAY = 1;
    static final int SOURCE_SP = 2;
    static final int SOURCE_GF = 3;

    static String KEY_SP_S1 = "key1";
    static String KEY_SP_S1_CELL1 = "s1_cell1";

    public static SocialNetworkFragment newInstance() {
        return new SocialNetworkFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_social_network, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        setupSource();
        initRecycler(view);
        initRadioGroup(view);
    }

    void setupSource() {
        switch (getCurrentSource()) {
            case SOURCE_ARRAY:
                noteSource = new ArraysCardsSourceImpl(getResources()).init();
                break;
            case SOURCE_SP:
                noteSource = new SharedPreferencesCardsSourceImpl(requireContext()
                        .getSharedPreferences(SharedPreferencesCardsSourceImpl.KEY_SP_2, Context.MODE_PRIVATE)).init();
                break;
            case SOURCE_GF:
                noteSource = new FireStoreCardsSourceImpl().init(this);
                break;
        }
        initAdapter();
    }

    private void initRadioGroup(View view) {
        view.findViewById(R.id.sourceArrays).setOnClickListener(listener);
        view.findViewById(R.id.sourceSP).setOnClickListener(listener);
        view.findViewById(R.id.sourceGF).setOnClickListener(listener);

        switch (getCurrentSource()) {
            case SOURCE_ARRAY:
                ((RadioButton) view.findViewById(R.id.sourceArrays)).setChecked(true);
                break;
            case SOURCE_SP:
                ((RadioButton) view.findViewById(R.id.sourceSP)).setChecked(true);
                break;
            case SOURCE_GF:
                ((RadioButton) view.findViewById(R.id.sourceGF)).setChecked(true);
                break;
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.sourceArrays:
                    setCurrentSource(SOURCE_ARRAY);
                    break;
                case R.id.sourceSP:
                    setCurrentSource(SOURCE_SP);
                    break;
                case R.id.sourceGF:
                    setCurrentSource(SOURCE_GF);
                    break;
            }
            setupSource();
        }
    };

    void setCurrentSource(int currentSource) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(KEY_SP_S1, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit().putInt(KEY_SP_S1_CELL1, currentSource);
        editor.apply();
    }

    int getCurrentSource() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(KEY_SP_S1, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_SP_S1_CELL1, SOURCE_ARRAY);
    }

    void initAdapter() {
        if (socialNetworkAdapter == null)
            socialNetworkAdapter = new SocialNetworkAdapter(this);
        socialNetworkAdapter.setData(noteSource);
        socialNetworkAdapter.setOnItemClickListener(SocialNetworkFragment.this);
    }


    void initRecycler(View view) {
        recyclerView = view.findViewById(R.id.recycler_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(socialNetworkAdapter);

        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setChangeDuration(2000);
        animator.setRemoveDuration(2000);
        recyclerView.setItemAnimator(animator);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator, null));
        recyclerView.addItemDecoration(itemDecoration);
    }


    @Override
    public void onItemClick(int position) {
        Observer observer = new Observer() {
            @Override
            public void updateNote(Note note) {
                ((MainActivity) requireActivity()).getPublisher().unsubscribe(this);
                noteSource.updateNote(position, note);
                socialNetworkAdapter.notifyItemChanged(position);
            }
        };
        ((MainActivity) requireActivity()).getPublisher().subscribe(observer);
        ((MainActivity) requireActivity()).getNavigation().addFragment(NoteTextFragment.newInstance(noteSource.getNote(position)), true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.toolbar_about:
                requireActivity().getSupportFragmentManager().beginTransaction().addToBackStack("").add(R.id.container, new AboutFragment()).commit();
                return true;
            case R.id.toolbar_exit:
                new DialogFragmentExit().show(requireActivity().getSupportFragmentManager(), DialogFragmentExit.TAG);
                return true;
            case R.id.toolbar_add:
                noteSource.addNote(new Note("New notes", "Text notes", R.color.blue_100, Calendar.getInstance().getTime()));
                socialNetworkAdapter.notifyItemInserted(noteSource.size() - 1);
                recyclerView.scrollToPosition(noteSource.size() - 1);
                return true;
            case R.id.toolbar_clear:
                noteSource.clearNote();
                socialNetworkAdapter.notifyDataSetChanged();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        requireActivity().getMenuInflater().inflate(R.menu.menu_note_context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = socialNetworkAdapter.getPosition();
        switch (item.getItemId()) {
            case R.id.toolBar_del: {
                noteSource.deleteNote(position);
                socialNetworkAdapter.notifyItemRemoved(position);
                return true;
            }

        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void initialized(NoteSource noteSource) {
        initAdapter();
    }
}