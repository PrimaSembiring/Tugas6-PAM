package com.example.profileapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.profileapp.viewmodel.ProfileViewModel
import com.example.profileapp.viewmodel.NoteViewModel
import com.example.profileapp.viewmodel.PostViewModel   // BARU
import com.example.profileapp.ui.screen.*
import com.example.profileapp.navigation.Screen
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val profileVM: ProfileViewModel = viewModel()
            val noteVM: NoteViewModel = viewModel()
            val postVM: PostViewModel = viewModel()   // BARU: ViewModel untuk data API
            val navController = rememberNavController()

            Scaffold(
                bottomBar = {

                    val currentRoute =
                        navController.currentBackStackEntryAsState().value?.destination?.route

                    NavigationBar {

                        NavigationBarItem(
                            selected = currentRoute == Screen.Notes.route,
                            onClick = {
                                navController.navigate(Screen.Notes.route) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            },
                            icon = { Icon(Icons.Default.List, null) },
                            label = { Text("Notes") }
                        )

                        NavigationBarItem(
                            selected = currentRoute == Screen.Favorites.route,
                            onClick = {
                                navController.navigate(Screen.Favorites.route) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            },
                            icon = { Icon(Icons.Default.Favorite, null) },
                            label = { Text("Favorites") }
                        )

                        NavigationBarItem(
                            selected = currentRoute == Screen.Profile.route,
                            onClick = {
                                navController.navigate(Screen.Profile.route) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            },
                            icon = { Icon(Icons.Default.Person, null) },
                            label = { Text("Profile") }
                        )
                    }
                }
            ) { padding ->

                NavHost(
                    navController = navController,
                    startDestination = Screen.Profile.route,
                    modifier = Modifier.padding(padding)
                ) {

                    composable(Screen.Profile.route) {
                        ProfileScreen(
                            viewModel = profileVM,
                            onEditClick = {
                                navController.navigate(Screen.EditProfile.route)
                            }
                        )
                    }

                    composable(Screen.EditProfile.route) {
                        EditProfileScreen(
                            viewModel = profileVM,
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(Screen.Notes.route) {
                        NotesScreen(
                            viewModel = noteVM,
                            postViewModel = postVM,   // BARU: teruskan PostViewModel
                            onClickDetail = {
                                navController.navigate(Screen.NoteDetail.createRoute(it))
                            },
                            onAdd = {
                                navController.navigate(Screen.AddNote.route)
                            }
                        )
                    }

                    composable(Screen.NoteDetail.route) { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("noteId")?.toInt() ?: 0

                        NoteDetailScreen(
                            noteId = id,
                            viewModel = noteVM,
                            postViewModel = postVM,   // BARU: teruskan PostViewModel
                            onBack = { navController.popBackStack() },
                            onEdit = {
                                navController.navigate(Screen.EditNote.createRoute(it))
                            }
                        )
                    }

                    composable(Screen.AddNote.route) {
                        AddNoteScreen(
                            viewModel = noteVM,
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(Screen.EditNote.route) { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("noteId")?.toInt() ?: 0

                        EditNoteScreen(
                            noteId = id,
                            viewModel = noteVM,
                            onBack = { navController.popBackStack() }
                        )
                    }

                    composable(Screen.Favorites.route) {
                        FavoritesScreen(viewModel = noteVM)
                    }
                }
            }
        }
    }
}