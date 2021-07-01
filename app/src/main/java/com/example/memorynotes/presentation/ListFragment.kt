package com.example.memorynotes.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.core.data.Note
import com.example.memorynotes.R
import com.example.memorynotes.framework.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*


class ListFragment : Fragment(), ListAction {

    private lateinit var viewModel: ListViewModel
    private var notes: ArrayList<Note> = ArrayList()

    private var recyclerAdapter = NotesListAdapter(notes, this)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)

        notesListView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }


        addNote.setOnClickListener { goToNoteDetails() }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.getAllNotes().observe(
            viewLifecycleOwner,
            { noteList ->
                loadingView.visibility = View.GONE
                notesListView.visibility = View.VISIBLE
                recyclerAdapter.updateNotes(noteList.sortedByDescending { it.updateTime })
            }
        )

    }

    private fun goToNoteDetails(id: Long = 0) {
        val action: NavDirections = ListFragmentDirections.actionGoToNote(id)
        Navigation.findNavController(notesListView).navigate(action)

    }

    override fun onResume() {
        super.onResume()
        viewModel.updateNotes()

    }

    override fun onClick(id: Long) {
        goToNoteDetails(id)
    }
}