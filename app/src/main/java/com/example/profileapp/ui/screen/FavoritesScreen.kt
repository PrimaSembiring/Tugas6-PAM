package com.example.profileapp.ui.screen

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.profileapp.viewmodel.NoteViewModel

@Composable
fun FavoritesScreen(viewModel: NoteViewModel) {

    val notes by viewModel.notes.collectAsState()
    val favorites = notes.filter { it.isFavorite }

    if (favorites.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Belum ada favorites")
        }
    } else {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(favorites) { note ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(note.title)
                        Text(note.content, maxLines = 2)
                    }
                }
            }
        }
    }
}