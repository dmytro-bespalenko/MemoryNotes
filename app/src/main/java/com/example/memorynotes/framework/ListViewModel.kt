package com.example.memorynotes.framework

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.core.data.Note
import com.example.memorynotes.framework.db.UseCases
import com.example.memorynotes.framework.di.ApplicationModule
import com.example.memorynotes.framework.di.DaggerViewModelComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListViewModel(application: Application) : AndroidViewModel(application) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
//    private val repository: NoteRepository = NoteRepository(RoomNoteDataSource(application))
//    private val useCases = UseCases(
//        AddNote(repository),
//        GetNote(repository),
//        GetAllNotes(repository),
//        RemoveNote(repository)
//    )

    @Inject
    lateinit var useCases: UseCases

    init {

        DaggerViewModelComponent.builder()
            .applicationModule(ApplicationModule(getApplication()))
            .build()
            .inject(this)
    }

    private val notes = MutableLiveData<List<Note>>()

    fun getAllNotes(): LiveData<List<Note>> {
        return notes
    }

    fun updateNotes() {
        coroutineScope.launch {
            val allNotes: List<Note> = useCases.getAllNotes()
            allNotes.forEach {
                it.wordCount = useCases.getWordCount.invoke(it)
            }
            notes.postValue(allNotes)
        }

    }

}