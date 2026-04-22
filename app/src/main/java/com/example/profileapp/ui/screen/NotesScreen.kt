package com.example.profileapp.ui.screen

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.profileapp.data.PostListUiState
import com.example.profileapp.viewmodel.NoteViewModel
import com.example.profileapp.viewmodel.PostViewModel

@Composable
fun NotesScreen(
    viewModel: NoteViewModel,
    postViewModel: PostViewModel,
    onClickDetail: (Int) -> Unit,
    onAdd: () -> Unit
) {

    val listUiState by postViewModel.listUiState.collectAsState()
    val isRefreshing by postViewModel.isRefreshing.collectAsState()
    val createState by postViewModel.createState.collectAsState()

    var title by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Text("+")
            }
        }
    ) { padding ->

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = { postViewModel.refresh() },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            when (val state = listUiState) {

                // 🔥 LOADING
                is PostListUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                // 🔥 SUCCESS
                is PostListUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {

                        // 🔥 FORM POST (HEADER)
                        item {

                            Text(
                                text = "Tambah Post",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            TextField(
                                value = title,
                                onValueChange = { title = it },
                                label = { Text("Title") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            TextField(
                                value = body,
                                onValueChange = { body = it },
                                label = { Text("Body") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    postViewModel.createPost(title, body)
                                    title = ""
                                    body = ""
                                }
                            ) {
                                Text("Submit")
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // 🔥 STATE POST
                            when (createState) {
                                is PostListUiState.Loading -> {
                                    CircularProgressIndicator()
                                }

                                is PostListUiState.Success -> {
                                    Text("Berhasil tambah data", color = Color.Green)
                                }

                                is PostListUiState.Error -> {
                                    Text("Gagal tambah data", color = Color.Red)
                                }

                                null -> {}
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // 🔥 LIST DATA
                        items(state.posts) { post ->
                            PostCard(
                                id = post.id,
                                title = post.title,
                                body = post.body,
                                onClick = { onClickDetail(post.id) }
                            )
                        }
                    }
                }

                // 🔥 ERROR
                is PostListUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "😕 Gagal memuat data",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = state.message,
                                color = Color.Gray
                            )
                            Button(onClick = { postViewModel.loadPosts() }) {
                                Text("Coba Lagi")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PostCard(
    id: Int,
    title: String,
    body: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            AsyncImage(
                model = "https://picsum.photos/seed/$id/400/180",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )

            Column(modifier = Modifier.padding(12.dp)) {

                Text(
                    text = title.replaceFirstChar { it.uppercase() },
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = body,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Post #$id",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}