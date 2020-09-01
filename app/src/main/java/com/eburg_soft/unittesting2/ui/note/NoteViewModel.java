package com.eburg_soft.unittesting2.ui.note;

import static com.eburg_soft.unittesting2.repository.NoteRepository.NOTE_TITLE_NULL;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.eburg_soft.unittesting2.models.Note;
import com.eburg_soft.unittesting2.repository.NoteRepository;
import com.eburg_soft.unittesting2.ui.Resource;
import com.eburg_soft.unittesting2.util.DateUtil;
import io.reactivex.functions.Consumer;
import java.util.Objects;
import javax.inject.Inject;
import org.reactivestreams.Subscription;

public class NoteViewModel extends ViewModel {

    public enum ViewState {VIEW, EDIT}

    private static final String TAG = "NoteViewModel";

    public static final String NO_CONTENT_ERROR = "Can't save note with no content";

    private boolean isNewNote;

    // vars
    private MutableLiveData<Note> note = new MutableLiveData<>();

    // inject
    private final NoteRepository noteRepository;

    private Subscription updateSubscription, insertSubscription;

    private MutableLiveData<ViewState> viewState = new MutableLiveData<>();


    @Inject
    public NoteViewModel(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public LiveData<Resource<Integer>> insertNote() throws Exception {
        return LiveDataReactiveStreams.fromPublisher(
                noteRepository.insertNote(note.getValue())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(Subscription subscription) {
                                insertSubscription = subscription;
                            }
                        })
        );
    }

    public LiveData<Note> observeNote() {
        return note;
    }

    public LiveData<ViewState> observeViewState() {
        return viewState;
    }

    public LiveData<Resource<Integer>> saveNote() throws Exception {

        if (!shouldAllowSave()) {
//            throw new Exception(NO_CONTENT_ERROR);
        }
        cancelPendingTransactions();

        return new NoteInsertUpdateHelper<Integer>() {

            @Override
            public String defineAction() {
                if (isNewNote) {
                    return ACTION_INSERT;
                } else {
                    return ACTION_UPDATE;
                }
            }

            @Override
            public LiveData<Resource<Integer>> getAction() throws Exception {
                if (isNewNote) {
                    return insertNote();
                } else {
                    return updateNote();
                }
            }

            @Override
            public void onTransactionComplete() {
                updateSubscription = null;
                insertSubscription = null;
            }

            @Override
            public void setNoteId(int noteId) {
                isNewNote = false;
                Note currentNote = note.getValue();
                if (currentNote != null) {
                    currentNote.setId(noteId);
                }
                note.setValue(currentNote);
            }
        }.getAsLiveData();
    }

    public void setIsNewNote(boolean isNewNote) {
        this.isNewNote = isNewNote;
    }

    public void setNote(Note note) throws Exception {
        if (note.getTitle() == null || note.getTitle().equals("")) {
            throw new Exception(NOTE_TITLE_NULL);
        }
        this.note.setValue(note);
    }

    public void setViewState(ViewState viewState) {
        this.viewState.setValue(viewState);
    }

    public boolean shouldNavigateBack() {
        return viewState.getValue() == ViewState.VIEW;
    }

    public LiveData<Resource<Integer>> updateNote() throws Exception {
        return LiveDataReactiveStreams.fromPublisher(
                noteRepository.updateNote(note.getValue())
                        .doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(Subscription subscription) {
                                updateSubscription = subscription;
                            }
                        })
        );
    }

    public void updateNote(String title, String content) throws Exception {
        if (title == null || title.equals("")) {
            throw new NullPointerException("Title can't be null");
        }
        String temp = removeWhiteSpace(content);
        if (temp.length() > 0) {
            Note updatedNote = new Note(note.getValue());
            updatedNote.setTitle(title);
            updatedNote.setContent(content);
            updatedNote.setTimestamp(DateUtil.getCurrentTimeStamp());

            note.setValue(updatedNote);
        }
    }

    private void cancelInsertTransaction() {
        insertSubscription.cancel();
        insertSubscription = null;
    }

    private void cancelPendingTransactions() {
        if (insertSubscription != null) {
            cancelInsertTransaction();
        }
        if (updateSubscription != null) {
            cancelUpdateTransaction();
        }
    }

    private void cancelUpdateTransaction() {
        updateSubscription.cancel();
        updateSubscription = null;
    }

    private String removeWhiteSpace(String string) {
        string = string.replace("\n", "");
        string = string.replace(" ", "");
        string.trim();
        return string;
    }

    private boolean shouldAllowSave() throws Exception {
        try {
            return removeWhiteSpace(Objects.requireNonNull(note.getValue()).getContent()).length() > 0;
        } catch (NullPointerException e) {
            throw new Exception(NO_CONTENT_ERROR);
        }
    }

}








