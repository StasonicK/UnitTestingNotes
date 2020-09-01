package com.eburg_soft.unittesting2.di;

import static com.eburg_soft.unittesting2.persistence.NoteDatabase.DATABASE_NAME;

import android.app.Application;
import androidx.room.Room;
import com.eburg_soft.unittesting2.persistence.NoteDao;
import com.eburg_soft.unittesting2.persistence.NoteDatabase;
import com.eburg_soft.unittesting2.repository.NoteRepository;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;


@Module
class AppModule {

    @Singleton
    @Provides
    static NoteDao provideNoteDao(NoteDatabase noteDatabase) {
        return noteDatabase.getNoteDao();
    }

    @Singleton
    @Provides
    static NoteDatabase provideNoteDatabase(Application application) {
        return Room.databaseBuilder(
                application,
                NoteDatabase.class,
                DATABASE_NAME
        ).build();
    }

    @Singleton
    @Provides
    static NoteRepository provideNoteRepository(NoteDao noteDao) {
        return new NoteRepository(noteDao);
    }
}















