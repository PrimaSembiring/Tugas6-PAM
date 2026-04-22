package com.example.profileapp.ui.screen

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.profileapp.viewmodel.NoteViewModel
import com.example.profileapp.ui.components.MyTextField

@Composable
fun AddNoteScreen(
    viewModel: NoteViewModel,
    onBack: () -> Unit
) {

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {

        Text("Tambah Note")

        MyTextField(title, { title = it }, "Judul")

        Spacer(modifier = Modifier.height(8.dp))

        MyTextField(content, { content = it }, "Isi")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.addNote(title, content)
            onBack()
        }) {
            Text("Simpan")
        }
    }
}