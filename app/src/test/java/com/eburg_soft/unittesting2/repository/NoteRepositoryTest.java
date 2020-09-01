package com.eburg_soft.unittesting2.repository;


import static com.eburg_soft.unittesting2.repository.NoteRepository.DELETE_FAILURE;
import static com.eburg_soft.unittesting2.repository.NoteRepository.DELETE_SUCCESS;
import static com.eburg_soft.unittesting2.repository.NoteRepository.INSERT_FAILURE;
import static com.eburg_soft.unittesting2.repository.NoteRepository.INSERT_SUCCESS;
import static com.eburg_soft.unittesting2.repository.NoteRepository.INVALID_NOTE_ID;
import static com.eburg_soft.unittesting2.repository.NoteRepository.NOTE_TITLE_NULL;
import static com.eburg_soft.unittesting2.repository.NoteRepository.UPDATE_FAILURE;
import static com.eburg_soft.unittesting2.repository.NoteRepository.UPDATE_SUCCESS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import androidx.lifecycle.MutableLiveData;
import com.eburg_soft.unittesting2.models.Note;
import com.eburg_soft.unittesting2.persistence.NoteDao;
import com.eburg_soft.unittesting2.ui.Resource;
import com.eburg_soft.unittesting2.util.InstantExecutorExtension;
import com.eburg_soft.unittesting2.util.LiveDataTestUtil;
import com.eburg_soft.unittesting2.util.TestUtil;
import io.reactivex.Single;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.function.*;

@ExtendWith(InstantExecutorExtension.class)
public class NoteRepositoryTest {

    private static final Note NOTE1 = new Note(TestUtil.TEST_NOTE_1);

    private NoteDao noteDao;

    // system under test
    private NoteRepository noteRepository;

    @BeforeEach
    public void initEach() {
        noteDao = mock(NoteDao.class);
        noteRepository = new NoteRepository(noteDao);
    }

    /*
        delete note
        delete failure
        return Resource.error
     */
    @Test
    void deleteNote_deleteFailure_returnResourceError() throws Exception {
        // Arrange
        final int deletedRow = -1;
        Resource<Integer> errorResponse = Resource.error(null, DELETE_FAILURE);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        when(noteDao.deleteNote(any(Note.class))).thenReturn(Single.just(deletedRow));

        // Act
        Resource<Integer> observedResponse = liveDataTestUtil.getValue(noteRepository.deleteNote(NOTE1));

        // Assert
        assertEquals(errorResponse, observedResponse);
    }


    /*
        Insert note
        Failure (return -1)
     */

    @Test
    void deleteNote_deleteSuccess_returnResourceSuccess() throws Exception {
        // Arrange
        final int deletedRow = 1;
        Resource<Integer> successResponse = Resource.success(deletedRow, DELETE_SUCCESS);
        LiveDataTestUtil<Resource<Integer>> liveDataTestUtil = new LiveDataTestUtil<>();
        when(noteDao.deleteNote(any(Note.class))).thenReturn(Single.just(deletedRow));

        // Act
        Resource<Integer> observedResponse = liveDataTestUtil.getValue(noteRepository.deleteNote(NOTE1));

        // Assert
        assertEquals(successResponse, observedResponse);
    }

    @Test
    void deleteNote_nullId_throwException() throws Exception {
        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                final Note note = new Note(TestUtil.TEST_NOTE_1);
                note.setId(-1);
                noteRepository.deleteNote(note);
            }
        });

        assertEquals(INVALID_NOTE_ID, exception.getMessage());
    }


    /*
        update note
        verify correct method is called
        confirm observer is trigger
        confirm number of rows updated
     */

    @Test
    void getNotes_returnEmptyList() throws Exception {
        // Arrange
        List<Note> notes = new ArrayList<>();
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Note>> returnedData = new MutableLiveData<>();
        returnedData.setValue(notes);
        when(noteDao.getNotes()).thenReturn(returnedData);

        // Act
        List<Note> observedData = liveDataTestUtil.getValue(noteRepository.getNotes());

        // Assert
        assertEquals(notes, observedData);
    }


    /*
        update note
        Failure (-1)
     */

    @Test
    void getNotes_returnListWithNotes() throws Exception {
        // Arrange
        List<Note> notes = TestUtil.TEST_NOTES_LIST;
        LiveDataTestUtil<List<Note>> liveDataTestUtil = new LiveDataTestUtil<>();
        MutableLiveData<List<Note>> returnedData = new MutableLiveData<>();
        returnedData.setValue(notes);
        when(noteDao.getNotes()).thenReturn(returnedData);

        // Act
        List<Note> observedData = liveDataTestUtil.getValue(noteRepository.getNotes());

        // Assert
        assertEquals(notes, observedData);
    }

    /*
        insert note
        null title
        confirm throw exception
     */
    @Test
    void insertNote_nullTitle_throwException() throws Exception {

        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                final Note note = new Note(TestUtil.TEST_NOTE_1);
                note.setTitle(null);
                noteRepository.insertNote(note);
            }
        });

        assertEquals(NOTE_TITLE_NULL, exception.getMessage());
    }

    /*
        delete note
        null id
        throw exception
     */

    @Test
    void insertNote_returnFailure() throws Exception {

        // Arrange
        final Long failedInsert = -1L;
        final Single<Long> returnedData = Single.just(failedInsert);
        when(noteDao.insertNote(any(Note.class))).thenReturn(returnedData);

        // Act
        final Resource<Integer> returnedValue = noteRepository.insertNote(NOTE1).blockingFirst();

        // Assert
        verify(noteDao).insertNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);

        assertEquals(Resource.error(null, INSERT_FAILURE), returnedValue);
    }

    /*
        delete note
        delete success
        return Resource.success with deleted row
     */

    /*
        insert note
        verify the correct method is called
        confirm observer is triggered
        confirm new rows inserted
     */
    @Test
    void insertNote_returnRow() throws Exception {

        // Arrange
        final Long insertedRow = 1L;
        final Single<Long> returnedData = Single.just(insertedRow);
        when(noteDao.insertNote(any(Note.class))).thenReturn(returnedData);

        // Act
        final Resource<Integer> returnedValue = noteRepository.insertNote(NOTE1).blockingSingle();

        // Assert
        verify(noteDao).insertNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);

        System.out.println("Returned value: " + returnedValue.data);
        assertEquals(Resource.success(1, INSERT_SUCCESS), returnedValue);

//        // Or test using RxJava
//        noteRepository.insertNote(NOTE1)
//                .test()
//                .await()
//                .assertValue(Resource.success(1, INSERT_SUCCESS));
    }

    /*
        update note
        null title
        throw exception
     */
    @Test
    void updateNote_nullTitle_throwException() throws Exception {

        Exception exception = assertThrows(Exception.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                final Note note = new Note(TestUtil.TEST_NOTE_1);
                note.setTitle(null);
                noteRepository.updateNote(note);
            }
        });

        assertEquals(NOTE_TITLE_NULL, exception.getMessage());
    }


    /*
        retrieve notes
        return list of notes
     */

    @Test
    void updateNote_returnFailure() throws Exception {
        // Arrange
        final int failedInsert = -1;
        final Single<Integer> returnedData = Single.just(failedInsert);
        when(noteDao.updateNote(any(Note.class))).thenReturn(returnedData);

        // Act
        final Resource<Integer> returnedValue = noteRepository.updateNote(NOTE1).blockingFirst();

        // Assert
        verify(noteDao).updateNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);

        assertEquals(Resource.error(null, UPDATE_FAILURE), returnedValue);
    }

    /*
        retrieve notes
        return empty list
     */

    @Test
    void updateNote_returnNumRowsUpdated() throws Exception {
        // Arrange
        final int updatedRow = 1;
        when(noteDao.updateNote(any(Note.class))).thenReturn(Single.just(updatedRow));

        // Act
        final Resource<Integer> returnedValue = noteRepository.updateNote(NOTE1).blockingFirst();

        // Assert
        verify(noteDao).updateNote(any(Note.class));
        verifyNoMoreInteractions(noteDao);

        assertEquals(Resource.success(updatedRow, UPDATE_SUCCESS), returnedValue);
    }
}






























