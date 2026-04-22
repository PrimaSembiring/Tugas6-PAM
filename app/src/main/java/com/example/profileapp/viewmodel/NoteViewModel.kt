package com.example.profileapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.profileapp.data.Note

class NoteViewModel : ViewModel() {

    private val _notes = MutableStateFlow(
        listOf(
            Note(1, "Belajar Compose", "Navigasi itu penting"),
            Note(2, "Tugas PAM", "Kerjakan sebelum deadline")
        )
    )

    val notes: StateFlow<List<Note>> = _notes

    fun addNote(title: String, content: String) {
        val newNote = Note(
            id = (_notes.value.maxOfOrNull { it.id } ?: 0) + 1,
            title = title,
            content = content
        )
        _notes.value = _notes.value + newNote
    }

    fun getNoteById(id: Int): Note? {
        return _notes.value.find { it.id == id }
    }

    fun updateNote(id: Int, title: String, content: String) {
        _notes.value = _notes.value.map {
            if (it.id == id) it.copy(title = title, content = content)
            else it
        }
    }

    fun toggleFavorite(id: Int) {
        _notes.value = _notes.value.map {
            if (it.id == id) it.copy(isFavorite = !it.isFavorite)
            else it
        }
    }
}