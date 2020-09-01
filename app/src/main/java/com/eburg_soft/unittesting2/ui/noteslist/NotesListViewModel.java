package com.eburg_soft.unittesting2.ui.noteslist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import com.eburg_soft.unittesting2.models.Note;
import com.eburg_soft.unittesting2.repository.NoteRepository;
import com.eburg_soft.unittesting2.ui.Resource;
import java.util.List;
import javax.inject.Inject;

public class NotesListViewModel extends ViewModel {

    private static final String TAG = "NotesListViewModel";

    // inject
    private final NoteRepository noteRepository;

    private MediatorLiveData<List<Note>> notes = new MediatorLiveData<>();

    @Inject
    public NotesListViewModel(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public LiveData<Resource<Integer>> deleteNote(final Note note) throws Exception {
        return noteRepository.deleteNote(note);
    }

    public void getNotes() {
        final LiveData<List<Note>> source = noteRepository.getNotes();
        notes.addSource(source, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notesList) {
                if (notesList != null) {
                    notes.setValue(notesList);
                }
                notes.removeSource(source);
            }
        });
    }

    public LiveData<List<Note>> observeNotes() {
        return notes;
    }
}















