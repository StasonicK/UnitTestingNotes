package com.eburg_soft.unittesting2;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import com.eburg_soft.unittesting2.persistence.NoteDao;
import com.eburg_soft.unittesting2.persistence.NoteDatabase;
import org.junit.*;

public abstract class NoteDatabaseTest {

    // system under test
    private NoteDatabase noteDatabase;

    @After
    public void finish() {
        noteDatabase.close();
    }

    public NoteDao getNoteDao() {
        return noteDatabase.getNoteDao();
    }

    @Before
    public void init() {
        noteDatabase = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                NoteDatabase.class
        ).build();
    }
}






