package ru.geekbrains.notes.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ru.geekbrains.notes.MainActivity;
import ru.geekbrains.notes.R;
import ru.geekbrains.notes.data.CardsSourceImpl;
import ru.geekbrains.notes.data.Note;
import ru.geekbrains.notes.data.NoteSource;


public class SocialNetworkFragment extends Fragment implements OnItemClickListener {

    SocialNetworkAdapter socialNetworkAdapter;
    NoteSource noteSource;
    RecyclerView recyclerView;

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
        socialNetworkAdapter = new SocialNetworkAdapter();
        socialNetworkAdapter.setData(getData());
        socialNetworkAdapter.setOnItemClickListener(SocialNetworkFragment.this);
    }

    NoteSource getData() {
        noteSource = new CardsSourceImpl(getResources()).init();
        return noteSource;
    }

    void initRecycler(View view) {
        recyclerView = view.findViewById(R.id.recycler_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(socialNetworkAdapter);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator,null));
        recyclerView.addItemDecoration(itemDecoration);
    }


    @Override
    public void onItemClick(int position) {
        NoteTextFragment noteTextFragment = NoteTextFragment.newInstance(noteSource.getNote(position));
        requireActivity().getSupportFragmentManager().beginTransaction().add(R.id.container, noteTextFragment).addToBackStack("").commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.toolbar_about:
               // MainActivity.getSupportFragmentManager().beginTransaction().addToBackStack("").add(R.id.container, new AboutFragment()).commit();
                return true;
            case R.id.toolbar_exit:
               // new DialogFragmentExit().show(getSupportFragmentManager(), DialogFragmentExit.TAG);
                return true;
            case R.id.toolbar_add:
                noteSource.addNote(new Note("New notes", "Text notes", R.color.blue_100));
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
}