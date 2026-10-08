package com.moviles.lab8.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.moviles.lab8.*

@Composable
fun MainScreen(rootNavController: NavController) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceVariant) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Face, contentDescription = "Characters") },
                    label = { Text("Characters") },
                    selected = false,
                    onClick = {
                        bottomNavController.navigate(CharactersGraph) {
                            popUpTo(bottomNavController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Place, contentDescription = "Locations") },
                    label = { Text("Locations") },
                    selected = false,
                    onClick = {
                        bottomNavController.navigate(LocationsGraph) {
                            popUpTo(bottomNavController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = false,
                    onClick = {
                        bottomNavController.navigate(ProfileRoute) {
                            popUpTo(bottomNavController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = CharactersGraph,
            modifier = Modifier.padding(paddingValues)
        ) {

            navigation<CharactersGraph>(startDestination = CharactersListRoute) {
                composable<CharactersListRoute> {
                    CharactersScreen(
                        onCharacterClick = { id ->
                            bottomNavController.navigate(CharacterDetailRoute(characterId = id))
                        }
                    )
                }
                composable<CharacterDetailRoute> {
                    CharacterDetailScreen(
                        onBackClick = { bottomNavController.popBackStack() }
                    )
                }
            }

            navigation<LocationsGraph>(startDestination = LocationsListRoute) {
                composable<LocationsListRoute> {
                    LocationsScreen(
                        onLocationClick = { id ->
                            bottomNavController.navigate(LocationDetailRoute(locationId = id))
                        }
                    )
                }
                composable<LocationDetailRoute> {
                    LocationDetailScreen(
                        onBackClick = { bottomNavController.popBackStack() }
                    )
                }
            }

            // Profile
            composable<ProfileRoute> {
                ProfileScreen(
                    onLogoutClick = {
                        rootNavController.navigate(LoginRoute) {
                            popUpTo<MainRoute> { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}