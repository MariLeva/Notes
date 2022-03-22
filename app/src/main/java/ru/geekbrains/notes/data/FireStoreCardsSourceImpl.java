package ru.geekbrains.notes.data;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import ru.geekbrains.notes.R;

public class FireStoreCardsSourceImpl implements NoteSource{
    private List<Note> noteSource;
    private static final String NOTES_COLLECTION = "notes";
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = firebaseFirestore.collection(NOTES_COLLECTION);


    public FireStoreCardsSourceImpl() {
        noteSource = new ArrayList<>();
    }

    public FireStoreCardsSourceImpl init(FireStoreResponse fireStoreResponse) {
        collectionReference.orderBy(NoteMapping.Fields.DATE, Query.Direction.ASCENDING).get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            noteSource = new ArrayList<Note>();
                            for (QueryDocumentSnapshot queryDocumentSnapshot:task.getResult()){
                                Map<String, Object> doc = queryDocumentSnapshot.getData();
                                String id = queryDocumentSnapshot.getId();
                                noteSource.add(NoteMapping.toNote(id,doc));
                            }
                        }
                    fireStoreResponse.initialized(FireStoreCardsSourceImpl.this);
                    }
                }
        );
        return this;
    }

    @Override
    public Note getNote(int position) {
        return noteSource.get(position);
    }

    @Override
    public int size() {
        return noteSource.size();
    }

    @Override
    public void deleteNote(int position) {
        collectionReference.document(noteSource.get(position).getId()).delete();
        noteSource.remove(position);
    }

    @Override
    public void updateNote(int position, Note note) {
        noteSource.set(position, note);
        collectionReference.document(note.getId()).set(NoteMapping.toDoc(note));
    }

    @Override
    public void addNote(Note note) {
        noteSource.add(note);
        collectionReference.add(NoteMapping.toDoc(note)).addOnSuccessListener(
                new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        note.setId(documentReference.getId());
                    }
                }
        );
    }

    @Override
    public void clearNote() {
        for (Note note:noteSource)
            collectionReference.document(note.getId()).delete();
        noteSource = new ArrayList<Note>();
        noteSource.clear();
    }
}
