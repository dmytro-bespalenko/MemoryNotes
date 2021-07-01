package com.example.memorynotes.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.core.data.Note
import com.example.memorynotes.R
import kotlinx.android.synthetic.main.item_note.view.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SetTextI18n", "SimpleDateFormat")
class NotesListAdapter(var notes: ArrayList<Note>, val action: ListAction) :
    RecyclerView.Adapter<NotesListAdapter.NoteViewHolder>() {


    fun updateNotes(newNote: List<Note>) {
        notes.clear()
        notes.addAll(newNote)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])

    }

    override fun getItemCount(): Int = notes.size

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val layout = view.noteLayout
        private val noteTitle = view.title
        private val noteContent = view.content
        private val noteDate = view.date
        private val noteWords = view.wordCount


        fun bind(note: Note) {
            noteTitle.text = note.title
            noteContent.text = note.content

            val sdf = SimpleDateFormat("MMM dd, HH:mm:ss")
            val resultDate = Date(note.updateTime)
            noteDate.text = "Last updated ${sdf.format(resultDate)}"

            layout.setOnClickListener {
                action.onClick(note.id)
            }
            noteWords.text = "Words: ${note.wordCount}"
        }

    }

}
