package com.example.memorynotes.framework.db

import com.example.core.usecase.*

data class UseCases(
    val addNote: AddNote,
    val getNote: GetNote,
    val getAllNotes: GetAllNotes,
    val removeNote: RemoveNote,
    val getWordCount: GetWordCount

)
