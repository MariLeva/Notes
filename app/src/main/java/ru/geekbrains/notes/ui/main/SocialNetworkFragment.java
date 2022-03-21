package ru.geekbrains.notes.ui.main;

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

import java.util.Calendar;

import ru.geekbrains.notes.ui.MainActivity;
import ru.geekbrains.notes.R;
import ru.geekbrains.notes.data.CardsSourceImpl;
import ru.geekbrains.notes.data.Note;
import ru.geekbrains.notes.data.NoteSource;
import ru.geekbrains.notes.observe.Observer;
import ru.geekbrains.notes.ui.editor.AboutFragment;
import ru.geekbrains.notes.ui.editor.DialogFragmentExit;
import ru.geekbrains.notes.ui.editor.NoteTextFragment;


public class SocialNetworkFragment extends Fragment implements OnItemClickListener {

    private SocialNetworkAdapter socialNetworkAdapter;
    private NoteSource noteSource;
    private RecyclerView recyclerView;


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
        initAdapter();
        initRecycler(view);
    }

    void initAdapter() {
        socialNetworkAdapter = new SocialNetworkAdapter(this);
        noteSource = new CardsSourceImpl(getResources()).init();
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
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator,null));
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
        ((MainActivity) requireActivity()).getNavigation().addFragment(NoteTextFragment.newInstance(noteSource.getNote(position)),true);
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
        requireActivity().getMenuInflater().inflate(R.menu.menu_note_context,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = socialNetworkAdapter.getPosition();
        switch (item.getItemId()){
            case R.id.toolBar_del:{
                noteSource.deleteNote(position);
                socialNetworkAdapter.notifyItemRemoved(position);
                return true;
            }

        }

        return super.onContextItemSelected(item);
    }
}