package com.eburg_soft.unittesting2.di;

import com.eburg_soft.unittesting2.ui.note.NoteActivity;
import com.eburg_soft.unittesting2.ui.noteslist.NotesListActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract NoteActivity contributeNotesActivity();

    @ContributesAndroidInjector
    abstract NotesListActivity contributeNotesListActivity();
}
