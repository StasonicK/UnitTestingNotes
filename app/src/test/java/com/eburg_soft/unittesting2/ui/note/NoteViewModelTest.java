package com.eburg_soft.unittesting2.ui.note;

import static com.eburg_soft.unittesting2.repository.NoteRepository.INSERT_SUCCESS;
import static com.eburg_soft.unittesting2.repository.NoteRepository.UPDATE_SUCCESS;
import static com.eburg_soft.unittesting2.ui.note.NoteViewModel.NO_CONTENT_ERROR;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.eburg_soft.unittesting2.models.Note;
import com.eburg_soft.unittesting2.repository.NoteRepository;
import com.eburg_soft.unittesting2.ui.Resource;
import com.eburg_soft.unittesting2.util.InstantExecutorExtension;
import com.eburg_soft.unittesting2.util.LiveDataTestUtil;
import com.eburg_soft.unittesting2.util.TestUtil;
import io.reactivex.Flowable;
import io.reactivex.internal.operators.single.SingleToFlowable;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.function.*;
import org.mockito.*;

@ExtendWith(InstantExecutorExtension.class)
public class NoteViewModelTest {

    @Mock
    private NoteRepository noteRepository;

    // system under test
    private NoteViewModel noteViewModel;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        noteViewModel = new NoteViewModel(noteRepository);
    }

    /*
        can't observe a note that hasn't been set
     */

    @Test
    void dontReturnInsertRowWithoutObserver() throws Exception {

        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);

        // Act
        noteViewModel.setNote(note);

        // Assert
        verify(noteRepository, never()).insertNote(any(Note.class));
    }

    /*
        Observe a note has been set and onChanged will trigger in activity
     */

    /*
        update: don't return a new row without observer
     */
    @Test
    void dontReturnUpdateRowNumWithoutObserver() throws Exception {

        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);

        // Act
        noteViewModel.setNote(note);

        // Assert
        verify(noteRepository, never()).updateNote(any(Note.class));
    }

    /*
        Insert a new note and observe row returned
     */

    @Test
    void insertNote_returnRow() throws Exception {
        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        final int insertedRow = 1;
        Flowable<Resource<Integer>> returnedData = SingleToFlowable
                .just(Resource.success(insertedRow, INSERT_SUCCESS));
        when(noteRepository.insertNote(any(Note.class))).thenReturn(returnedData);

        // Act
        noteViewModel.setNote(note);
        noteViewModel.setIsNewNote(true);
        Resource<Integer> returnedValue = liveDataTestUtil.getValue(noteViewModel.saveNote());

        // Assert
        assertEquals(Resource.success(insertedRow, INSERT_SUCCESS), returnedValue);
    }

    /*
        insert: dont return a new row without observer
     */

    @Test
    void observeEmptyNoteWhenNoteSet() throws Exception {
        // Arrange
        LiveDataTestUtil<Note> liveDataTestUtil = new LiveDataTestUtil<>();

        // Act
        Note note = liveDataTestUtil.getValue(noteViewModel.observeNote());

        // Assert
        assertNull(note);
    }

    /*
        set note, null title, throw exception
     */

    @Test
    void observeNote_whenSet() throws Exception {

        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);
        LiveDataTestUtil<Note> liveDataTestUtil = new LiveDataTestUtil<>();

        // Act
        noteViewModel.setNote(note);
        Note observedNote = liveDataTestUtil.getValue(noteViewModel.observeNote());

        // Assert
        assertEquals(note, observedNote);
    }

    /*
        update a note and observe row returned
     */

    @Test
    void saveNote_shouldAllowSave_returnFalse() throws Exception {
        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);
        note.setContent(null);

        // Act
        noteViewModel.setNote(note);
        noteViewModel.setIsNewNote(true);

        // Assert
        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                noteViewModel.saveNote();
            }
        });

        assertEquals(NO_CONTENT_ERROR, exception.getMessage());
    }

    @Test
    void setNote_nullTitle_throwException() throws Exception {

        // Arrange
        final Note note = new Note(TestUtil.TEST_NOTE_1);
        note.setTitle(null);

        // Assert
        assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {

                // Act
                noteViewModel.setNote(note);
            }
        });
    }

    @Test
    void updateNote_returnRow() throws Exception {
        // Arrange
        Note note = new Note(TestUtil.TEST_NOTE_1);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        final int updatedRow = 1;
        Flowable<Resource<Integer>> returnedData = SingleToFlowable
                .just(Resource.success(updatedRow, UPDATE_SUCCESS));
        when(noteRepository.updateNote(any(Note.class))).thenReturn(returnedData);

        // Act
        noteViewModel.setNote(note);
        noteViewModel.setIsNewNote(false);
        Resource<Integer> returnedValue = liveDataTestUtil.getValue(noteViewModel.saveNote());

        // Assert
        assertEquals(Resource.success(updatedRow, UPDATE_SUCCESS), returnedValue);
    }
}





















