package com.example.memorynotes.framework.di

import com.example.core.repository.NoteRepository
import com.example.core.usecase.*
import com.example.memorynotes.framework.db.UseCases
import dagger.Module
import dagger.Provides

@Module
class UseCasesModule {

    @Provides
    fun getUseCases(repository: NoteRepository) = UseCases(
        AddNote(repository),
        GetNote(repository),
        GetAllNotes(repository),
        RemoveNote(repository),
        GetWordCount()
    )
}