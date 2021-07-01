package com.example.memorynotes.presentation

import android.app.AlertDialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.core.data.Note
import com.example.memorynotes.R
import com.example.memorynotes.framework.NoteViewModel
import kotlinx.android.synthetic.main.fragment_note.*

class NoteFragment : Fragment() {

    private lateinit var viewModel: NoteViewModel
    private var currentNote: Note = Note("", "", 0L, 0L)
    private var noteId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)

        arguments?.let {
            noteId = NoteFragmentArgs.fromBundle(it).noteId
        }

        if (noteId != 0L) {
            viewModel.getNote(noteId)
        }

        checkButton.setOnClickListener {
            if (titleView.text.toString() != "" || contentView.text.toString() != "") {
                val time: Long = System.currentTimeMillis()
                currentNote.title = titleView.text.toString()
                currentNote.content = contentView.text.toString()
                currentNote.updateTime = time
                if (currentNote.id == 0L) {
                    currentNote.creationTime = time
                }
                viewModel.saveNote(currentNote)

            } else {
                Navigation.findNavController(it).popBackStack()
            }
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.saved.observe(viewLifecycleOwner, Observer {
            if (it) {
                Toast.makeText(requireContext(), "Done!", Toast.LENGTH_SHORT).show()
                hideKeyBoard()
                Navigation.findNavController(titleView).popBackStack()
            } else {
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.currentNote.observe(viewLifecycleOwner,
            { note ->
                note?.let {
                    currentNote = it
                    titleView.setText(it.title, TextView.BufferType.EDITABLE)
                    contentView.setText(it.content, TextView.BufferType.EDITABLE)
                }

            }
        )

    }

    private fun hideKeyBoard() {
        val imm: InputMethodManager =
            context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(titleView.windowToken, 0)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.note_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deleteNote -> {
                if (context != null && noteId != 0L) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Delete note")
                        .setMessage("Are you sure you want to delete this note?")
                        .setPositiveButton("Yes") { dialogInterface: DialogInterface, i -> viewModel.remove(currentNote)}
                        .setNegativeButton("Cancel") { dialogInterface: DialogInterface, i -> }
                        .create()
                        .show()
                }
            }

        }
        return true
    }
}