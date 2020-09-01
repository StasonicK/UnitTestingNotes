package com.eburg_soft.unittesting2.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.eburg_soft.unittesting2.models.Note;
import io.reactivex.Single;
import java.util.List;

@Dao
public interface NoteDao {

    @Delete
    Single<Integer> deleteNote(Note note) throws Exception;

    @Query("SELECT * FROM notes")
    LiveData<List<Note>> getNotes();

    @Insert
    Single<Long> insertNote(Note note) throws Exception;

    @Update
    Single<Integer> updateNote(Note note) throws Exception;
}














