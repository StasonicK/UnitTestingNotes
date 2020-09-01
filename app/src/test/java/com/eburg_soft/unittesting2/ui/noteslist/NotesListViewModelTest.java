package com.eburg_soft.unittesting2.ui.noteslist;

import static com.eburg_soft.unittesting2.repository.NoteRepository.DELETE_FAILURE;
import static com.eburg_soft.unittesting2.repository.NoteRepository.DELETE_SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import androidx.lifecycle.MutableLiveData;
import com.eburg_soft.unittesting2.models.Note;
import com.eburg_soft.unittesting2.repository.NoteRepository;
import com.eburg_soft.unittesting2.ui.Resource;
import com.eburg_soft.unittesting2.util.InstantExecutorExtension;
import com.eburg_soft.unittesting2.util.LiveDataTestUtil;
import com.eburg_soft.unittesting2.util.TestUtil;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;

@ExtendWith(InstantExecutorExtension.class)
public class NotesListViewModelTest {

    @Mock
    private NoteRepository noteRepository;

    // system under test
    private NotesListViewModel viewModel;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        viewModel = new NotesListViewModel(noteRepository);
    }

    /*
        Retrieve list of notes
        observe list
        return list
     */

    /*
        delete note
        observe Resource.error
        return Resource.error
     */
    @Test
    void deleteNote_observeResourceError() throws Exception {
        // Arrange
        Note deletedNote = new Note(TestUtil.TEST_NOTE_1);
        Resource<Integer> returnedData = Resource.error(null, DELETE_FAILURE);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<Resource<Integer>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(noteRepository.deleteNote(any(Note.class))).thenReturn(returnedValue);

        // Act
        Resource<Integer> observedValue = liveDataTestUtil.getValue(viewModel.deleteNote(deletedNote));

        // Assert
        assertEquals(returnedData, observedValue);
    }
    /*
        retrieve list of notes
        observe the list
        return empty list
     */

    @Test
    void deleteNote_observeResourceSuccess() throws Exception {
        // Arrange
        Note deletedNote = new Note(TestUtil.TEST_NOTE_1);
        Resource<Integer> returnedData = Resource.success(1, DELETE_SUCCESS);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<Resource<Integer>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(noteRepository.deleteNote(any(Note.class))).thenReturn(returnedValue);

        // Act
        Resource<Integer> observedValue = liveDataTestUtil.getValue(viewModel.deleteNote(deletedNote));

        // Assert
        assertEquals(returnedData, observedValue);
    }


    /*
        delete note
        observe Resource.success
        return Resource.success
     */

    @Test
    void retrieveNotes_returnEmptyNotesList() throws Exception {
        // Arrange
        List<Note> returnedData = new ArrayList<>();
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Note>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(noteRepository.getNotes()).thenReturn(returnedValue);

        // Act
        viewModel.getNotes();
        List<Note> observedData = liveDataTestUtil.getValue(viewModel.observeNotes());

        // Assert
        assertEquals(returnedData, observedData);
    }

    @Test
    void retrieveNotes_returnNotesList() throws Exception {
        // Arrange
        List<Note> returnedData = TestUtil.TEST_NOTES_LIST;
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Note>> returnedValue = new MutableLiveData<>();
        returnedValue.setValue(returnedData);
        when(noteRepository.getNotes()).thenReturn(returnedValue);

        // Act
        viewModel.getNotes();
        List<Note> observedData = liveDataTestUtil.getValue(viewModel.observeNotes());

        // Assert
        assertEquals(returnedData, observedData);
    }

}














