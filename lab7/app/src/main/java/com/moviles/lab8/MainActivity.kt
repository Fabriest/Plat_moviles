package com.moviles.lab8

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moviles.lab8.ui.LoginScreen
import com.moviles.lab8.ui.MainScreen
import kotlinx.serialization.Serializable


@Serializable object LoginRoute
@Serializable object MainRoute

@Serializable object CharactersGraph
@Serializable object CharactersListRoute
@Serializable data class CharacterDetailRoute(val characterId: Int)

@Serializable object LocationsGraph
@Serializable object LocationsListRoute
@Serializable data class LocationDetailRoute(val locationId: Int)

@Serializable object ProfileRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                RickAndMortyApp()
            }
        }
    }
}


@Composable
fun RickAndMortyApp() {
    val rootNavController = rememberNavController()

    NavHost(navController = rootNavController, startDestination = LoginRoute) {

        composable<LoginRoute> {
            LoginScreen(
                onLoginClick = {
                    rootNavController.navigate(MainRoute) {
                        popUpTo<LoginRoute> { inclusive = true }
                    }
                }
            )
        }

        composable<MainRoute> {
            MainScreen(rootNavController = rootNavController)
        }
    }
}